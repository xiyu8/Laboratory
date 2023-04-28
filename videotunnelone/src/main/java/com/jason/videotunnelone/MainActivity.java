package com.jason.videotunnelone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.google.gson.Gson;

import org.webrtc.AudioSource;
import org.webrtc.AudioTrack;
import org.webrtc.Camera1Enumerator;
import org.webrtc.Camera2Enumerator;
import org.webrtc.CameraEnumerator;
import org.webrtc.DataChannel;
import org.webrtc.DefaultVideoDecoderFactory;
import org.webrtc.DefaultVideoEncoderFactory;
import org.webrtc.EglBase;
import org.webrtc.IceCandidate;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.RendererCommon;
import org.webrtc.SessionDescription;
import org.webrtc.SurfaceTextureHelper;
import org.webrtc.SurfaceViewRenderer;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoSource;
import org.webrtc.VideoTrack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 使用AndroidWebRTC api采集本地摄像头画面，并显示；
 * 基于AndroidWebRTC，将 采集的本地摄像头画面，传输到另一个窗口显示
 *
 */
public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    public final String TAG = "video_tunnel";
    public final String TAGT = "VideoStep";
    public final String  VIDEO_TRACK_ID = "ARDAMSv0";
    public final String  AUDIO_TRACK_ID = "ARDAMSa0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gson = new Gson();
        initView();
        initData();
    }
    Gson gson;

    SurfaceView mSurfaceView;
    SurfaceHolder surfaceHolder;
    CameraManager mCameraManager;
    Handler cameraHandler;
    private void initView() {
        mSurfaceView =findViewById(R.id.local_video);
        mLocalSurfaceView =findViewById(R.id.LocalSurfaceView);
        mRemoteSurfaceView =findViewById(R.id.RemoteSurfaceView);

//        surfaceHolder = mSurfaceView.getHolder();
//        surfaceHolder.setKeepScreenOn(true);
//        surfaceHolder.addCallback(this);

    }

    PeerConnectionFactory mPeerConnectionFactory;
    VideoTrack mLocalVideoTrack;
    VideoTrack mVideoTrackRemote;
    AudioTrack mAudioTrack;
    SurfaceTextureHelper mSurfaceTextureHelper;
    VideoCapturer mVideoCapturer;
    SurfaceViewRenderer mLocalSurfaceView,mRemoteSurfaceView;
    EglBase.Context eglBaseContext;
    MediaStream localMediaStream;
    PeerConnection localPeerConnection;
    PeerConnection remotePeerConnection;
    private void initData() {
        eglBaseContext = EglBase.create().getEglBaseContext();
        // step 1 创建PeerConnectionFactory
        PeerConnectionFactory.initialize(
                PeerConnectionFactory.InitializationOptions.builder(this)
                        .setEnableInternalTracer(true)
                        .createInitializationOptions()
        );

        //创建编解码对象
        DefaultVideoEncoderFactory videoEncode = new DefaultVideoEncoderFactory(eglBaseContext, true, true);
        DefaultVideoDecoderFactory videoDecode = new DefaultVideoDecoderFactory(eglBaseContext);
        PeerConnectionFactory.Options options = new PeerConnectionFactory.Options();
        mPeerConnectionFactory = PeerConnectionFactory.builder()
                .setOptions(options)
                .setVideoDecoderFactory(videoDecode)
                .setVideoEncoderFactory(videoEncode)
                .createPeerConnectionFactory();
        AudioSource audioSource = mPeerConnectionFactory.createAudioSource(new MediaConstraints());
        mPeerConnectionFactory.createAudioTrack(AUDIO_TRACK_ID, audioSource);



        mLocalSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                VideoCapturer videoCapturer = createVideoCapturer();
                VideoSource videoSource = mPeerConnectionFactory.createVideoSource(videoCapturer.isScreencast());
                //拿到 surface工具类，用来表示camera 初始化的线程
                mSurfaceTextureHelper = SurfaceTextureHelper.create("caputerTHread", eglBaseContext);
                //用来表示当前初始化 camera 的线程，和 application context，当调用 startCapture 才会回调。
                videoCapturer.initialize(mSurfaceTextureHelper, getApplicationContext(), videoSource.getCapturerObserver());
                //开始采集
                videoCapturer.startCapture(
                        mLocalSurfaceView.getWidth(),
                        mLocalSurfaceView.getHeight(),
                        30
                );
                // 初始化 SurfaceViewRender ，这个方法非常重要，不初始化黑屏
                mLocalSurfaceView.init(eglBaseContext, null);
//                mLocalSurfaceView.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL);
//                mLocalSurfaceView.setMirror(true);
//                mLocalSurfaceView.setEnableHardwareScaler(false /* enabled */);
                //添加视频轨道
                mLocalVideoTrack = mPeerConnectionFactory.createVideoTrack(VIDEO_TRACK_ID, videoSource);
                // 添加渲染接收端器到轨道中，画面开始呈现
                mLocalVideoTrack.addSink(mLocalSurfaceView);
                // 创建 mediastream
                localMediaStream = mPeerConnectionFactory.createLocalMediaStream("MediaStream");
                // 将视频轨添加到 mediastram 中，等待连接时传输
                localMediaStream.addTrack(mLocalVideoTrack);

//                mVideoTrack.setEnabled(true);

            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
            }
        });




//        //初始化 ICE 服务器创建 PC 时使用
//        List<PeerConnection.IceServer> iceServers = new ArrayList<>();
//        iceServers.add(new PeerConnection.IceServer("stun:23.21.150.121"));
//        iceServers.add(new PeerConnection.IceServer("stun:stun.l.google.com:19302"));
//        PeerConnection.RTCConfiguration rtcConfig = new PeerConnection.RTCConfiguration(iceServers);
//        PeerConnection connection = mPeerConnectionFactory.createPeerConnection(rtcConfig, mPeerConnectionObserver);
//        connection.addTrack(mVideoTrack, mediaStreamLabels);
//        connection.addTrack(mAudioTrack, mediaStreamLabels);

///////////////////////////////////////本地流远端回显/////////////////////////////////////////////////////////////
        mRemoteSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                //等待接收数据，这里只要初始化即可
                mRemoteSurfaceView.init(eglBaseContext, null);
                mRemoteSurfaceView.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FIT);
                mRemoteSurfaceView.setEnableHardwareScaler(false);
            }
            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {}
            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {}
        });

    }



    public void onClick(View view) {
        initRemote();
    }
    private void initRemote() {
        List<PeerConnection.IceServer> iceServers = new ArrayList<>();
        //内部会转成 RTCConfiguration
        localPeerConnection = mPeerConnectionFactory.createPeerConnection(iceServers, new PeerObserver() {
            @Override
            public void onAddStream(MediaStream mediaStream) {
                super.onAddStream(mediaStream);
            }
            @Override
            public void onIceCandidate(IceCandidate iceCandidate) { //3次
                super.onIceCandidate(iceCandidate);
                Log.d(TAG, "创建 local offer success: ");
                Log.d(TAGT, "7_localPeerConnection->PeerObserver.onIceCandidate: ");
                localPeerConnection.addIceCandidate(iceCandidate);
                if(remotePeerConnection!=null) {
                    Log.d(TAGT, "8_localPeerConnection->PeerObserver.onIceCandidate: remotePeerConnection.addIceCandidate:"+gson.toJson(iceCandidate));
                    remotePeerConnection.addIceCandidate(iceCandidate);
                }
            }
        });
        remotePeerConnection = mPeerConnectionFactory.createPeerConnection(iceServers,new PeerObserver(){
            public void onAddStream(MediaStream strem) {
                super.onAddStream(strem);
                Log.d(TAGT, "4_remotePeerConnection->PeerObserver.onAddStream: ");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAGT, "6_localPeerConnection->PeerObserver.onIceCandidate: strem.videoTracks.get(0).addSink(mRemoteSurfaceView)");
                        strem.videoTracks.get(0).addSink(mRemoteSurfaceView);
                    }
                });
            }
            public void onIceCandidate(IceCandidate iceCandidate) { //3次
                super.onIceCandidate(iceCandidate);
                Log.d(TAGT, "12_remotePeerConnection->PeerObserver.onIceCandidate: ");
                if(localPeerConnection!=null){
                    Log.d(TAGT, "13_remotePeerConnection->PeerObserver.onIceCandidate: localPeerConnection.addIceCandidate(iceCandidate):"+gson.toJson(iceCandidate));
                    localPeerConnection.addIceCandidate(iceCandidate);
                }
            }
        });
        localPeerConnection.addStream(localMediaStream);

        //客户端创建 offer
        localPeerConnection.createOffer(new SdpObserver("创建local offer") {
            public void onCreateSuccess(SessionDescription sessionDescription) {
//                super.onCreateSuccess(p0);
                Log.d(TAG, "创建 local offer success: ");
                Log.d(TAGT, "1_localPeerConnection.createOffer->SdpObserver.onCreateSuccess: ");
                /**
                              * 此时调用setLocalDescription()方法将该Offer保存到本地Local域，
                              * 然后将Offer发送给对方
                              */
                Log.d(TAGT, "2_localPeerConnection.createOffer->SdpObserver.onCreateSuccess:localPeerConnection.setLocalDescription:"+gson.toJson(sessionDescription));
                localPeerConnection.setLocalDescription(new SdpObserver("local 设置本地 sdp"), sessionDescription);
                //2. 同时，接收端 setRemoteDescription 把 offer 保存到远端域
                Log.d(TAGT, "3_localPeerConnection.createOffer->SdpObserver.onCreateSuccess:remotePeerConnection.setRemoteDescription:"+gson.toJson(sessionDescription));
                remotePeerConnection.setRemoteDescription(new SdpObserver("把 sdp 给到 remote"), sessionDescription);

                //3. 此时remote 已经接收端了 offer ,所以创建 answer
                Log.d(TAGT, "5_localPeerConnection.createOffer->SdpObserver.onCreateSuccess:remotePeerConnection.createAnswer ");
                remotePeerConnection.createAnswer(new SdpObserver("创建 remote answer") {
                    public void onCreateSuccess(SessionDescription sessionDescription1) {
//                        super.onCreateSuccess(p0);
                        Log.d(TAG, " 创建 remote answer success: ");
                        Log.d(TAGT, "9_remotePeerConnection.createAnswer->SdpObserver.onCreateSuccess: ");
                        /**
                                               * 4. 此时调用setLocalDescription()方法将该 answer 保存到本地 remote 域，
                                               * 然后将 answer 发送给对方
                                               */
                        Log.d(TAGT, "10_remotePeerConnection.createAnswer->SdpObserver.onCreateSuccess: remotePeerConnection.setLocalDescription:"+gson.toJson(sessionDescription1));
                        remotePeerConnection.setLocalDescription(new SdpObserver("remote 设置本地 sdp"), sessionDescription1);
                        //5. local 把 answer 保存到它的 remote 端，此时 sdp 交换结束
                        Log.d(TAGT, "11_remotePeerConnection.createAnswer->SdpObserver.onCreateSuccess: localPeerConnection.setRemoteDescription:"+gson.toJson(sessionDescription1));
                        localPeerConnection.setRemoteDescription(new SdpObserver("把 sdp 给到 local"), sessionDescription1);
                    }
                }, new MediaConstraints());
            }
        }, new MediaConstraints());

    }

    private VideoCapturer createVideoCapturer() {
        if (Camera2Enumerator.isSupported(this)) {
            return createCameraCapturer(new Camera2Enumerator(this));
        } else {
            return createCameraCapturer(new Camera1Enumerator(true));
        }
    }

    private VideoCapturer createCameraCapturer(CameraEnumerator enumerator) {
        final String[] deviceNames = enumerator.getDeviceNames();

        // First, try to find front facing camera
        Log.d(TAG, "Looking for front facing cameras.");
        for (String deviceName : deviceNames) {
            if (enumerator.isFrontFacing(deviceName)) {
                Log.d(TAG, "Creating front facing camera capturer.");
                VideoCapturer videoCapturer = enumerator.createCapturer(deviceName, null);
                if (videoCapturer != null) {
                    return videoCapturer;
                }
            }
        }

        // Front facing camera not found, try something else
        Log.d(TAG, "Looking for other cameras.");
        for (String deviceName : deviceNames) {
            if (!enumerator.isFrontFacing(deviceName)) {
                Log.d(TAG, "Creating other camera capturer.");
                VideoCapturer videoCapturer = enumerator.createCapturer(deviceName, null);
                if (videoCapturer != null) {
                    return videoCapturer;
                }
            }
        }

        return null;
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        try {
            //需要相机权限
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            //获取可用相机设备列表
            mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            String[] CameraIdList = mCameraManager.getCameraIdList();
            String cameraId = null;
//            Size[] outputSizes = new Size[0];
            for (String id : CameraIdList) {
                CameraCharacteristics characteristics=mCameraManager.getCameraCharacteristics(id);
                if (characteristics.get(CameraCharacteristics.LENS_FACING).equals(CameraCharacteristics.LENS_FACING_FRONT)) {
                    cameraId = id;
                    break;
                }
            }
            if(cameraId==null) cameraId = CameraIdList[0];
            //打开相机
            mCameraManager.openCamera(cameraId, mCameraDeviceCallback, new Handler(getMainLooper()));
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        // SurfaceView尺寸发生改变时（首次在屏幕上显示同样会调用此方法），初始化mCamera参数，启动Camera预览
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        closeCamera();
    }


    private CameraDevice mCamera;
    boolean isClose = true;
    private final CameraDevice.StateCallback mCameraDeviceCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            //表示相机打开成功，可以真正开始使用相机，创建Capture会话
            mCamera = camera;
            isClose = false;
            //创建Capture会话
            try {
                // 创建CameraCaptureSession，该对象负责管理处理预览请求和拍照请求
                HandlerThread cameraThread = new HandlerThread("Camera2");
                cameraThread.start();
                cameraHandler = new Handler(cameraThread.getLooper());
                mCamera.createCaptureSession(Arrays.asList(surfaceHolder.getSurface()), mSessionStateCallback, cameraHandler);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onClosed(@NonNull CameraDevice camera) {
            //调用Camera.close()后的回调方法
            isClose =true;
        }
        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            //当相机断开连接时回调该方法，需要进行释放相机的操作
            mCamera = null;
        }
        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            //当相机打开失败时，需要进行释放相机的操作
            mCamera = null;
        }
    };
    private CameraCaptureSession.StateCallback mSessionStateCallback = new CameraCaptureSession.StateCallback() {
        @Override
        public void onConfigured(CameraCaptureSession session) {
            try {
                // 创建预览需要的CaptureRequest.Builder
                CaptureRequest.Builder mPreviewBuilder = mCamera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                // 将SurfaceView的surface作为CaptureRequest.Builder的目标
                mPreviewBuilder.addTarget(surfaceHolder.getSurface());
                mPreviewBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_OFF);
                mPreviewBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_OFF);
                //设置拍摄图像时相机设备是否使用光学防抖（OIS）。
                mPreviewBuilder.set(CaptureRequest.LENS_OPTICAL_STABILIZATION_MODE, CaptureRequest.LENS_OPTICAL_STABILIZATION_MODE_ON);
                //感光灵敏度
                mPreviewBuilder.set(CaptureRequest.SENSOR_SENSITIVITY, 1600);
                //曝光补偿//
                mPreviewBuilder.set(CaptureRequest.CONTROL_AE_EXPOSURE_COMPENSATION, 0);
                session.setRepeatingRequest(mPreviewBuilder.build(), new CameraCaptureSession.CaptureCallback() {
                    @Override
                    public void onCaptureStarted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, long timestamp, long frameNumber) {
                        super.onCaptureStarted(session, request, timestamp, frameNumber);
                    }
                }, cameraHandler);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onConfigureFailed(CameraCaptureSession session) {}
    };













    private void closeCamera() {
        if (mCamera != null) {
            try {
                mCamera.close();
            } catch (Exception e) {
            }
        }
    }

    public void onClickk(View view) {
    }
}