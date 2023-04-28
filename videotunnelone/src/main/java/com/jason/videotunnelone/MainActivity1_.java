package com.jason.videotunnelone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
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
import java.util.List;


/**
 *
 * 基于AndroidWebRTC，将 采集的本地摄像头画面，传输到另一个客户端videotunneltwo  显示
 * 信令传输使用java nio
 *
 */
public class MainActivity1_ extends AppCompatActivity implements NioPeriodChronicService.View {
    public final String TAG = "video_tunnel";
    public final String TAGT = "VideoStep";
    public final String  VIDEO_TRACK_ID = "ARDAMSv0";
    public final String  AUDIO_TRACK_ID = "ARDAMSa0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gson = new Gson();
//        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},
//                111);

        nioConnect();
        initView();
        initData();
    }

    private void nioConnect() {
        startService(new Intent(this, NioPeriodChronicService.class));
        bindService(new Intent(this, NioPeriodChronicService.class), new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {



                nioBinder = (NioPeriodChronicService.NioBinder) service;
                nioBinder.initNio(MainActivity1_.this);


            }
            @Override
            public void onServiceDisconnected(ComponentName name) {}
        }, Context.BIND_AUTO_CREATE);
    }

    Gson gson;

    int resumeCount = 0;
    NioPeriodChronicService.NioBinder nioBinder;
    @Override
    protected void onResume() {
        super.onResume();
        resumeCount++;
        if (resumeCount == 2) {
            bindService(new Intent(this, NioPeriodChronicService.class), new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    nioBinder = (NioPeriodChronicService.NioBinder) service;
                    nioBinder.setView(MainActivity1_.this);
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {

                }
            }, Context.BIND_AUTO_CREATE);
        }
    }

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

        ////////////////////////////////////////////////////////////////////////////////////////////////////
        mRemoteSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                //等待接收数据，这里只要初始化即可
                if(!surfaceIsInit){
                    mRemoteSurfaceView.init(eglBaseContext, null);
                    mRemoteSurfaceView.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FIT);
                    mRemoteSurfaceView.setEnableHardwareScaler(false);
                    surfaceIsInit = true;
                }
            }
            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {}
            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {}
        });

    }

    boolean surfaceIsInit = false;

    int commandCount = 0;
    public void onClick(View view) {
        if (commandCount == 0) {
            A0(); //创建 localPeerConnection
            localPeerConnection.addStream(localMediaStream); //添加 本地视频流 到localPeerConnection
        } else  if (commandCount == 1) {
            A1(); //将 localPeerConnection 提供给 其它端连接
        }
        commandCount++;
    }

    public void onClickk(View view) {
        startActivity(new Intent(this,NioPeriodChronicActivity.class));
    }

    private void A0() {
        List<PeerConnection.IceServer> iceServers = new ArrayList<>();
        //        iceServers.add(new PeerConnection.IceServer("stun:stun.l.google.com:19302"));
        localPeerConnection = mPeerConnectionFactory.createPeerConnection(iceServers, new PeerObserver() {
            @Override
            public void onAddStream(MediaStream mediaStream) {
                super.onAddStream(mediaStream);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAGT, "mediaStream.videoTracks.get(0).addSink(mRemoteSurfaceView)");
                        mediaStream.videoTracks.get(0).addSink(mRemoteSurfaceView);
                    }
                });
            }
            @Override
            public void onIceCandidate(IceCandidate iceCandidate) {
                super.onIceCandidate(iceCandidate);
                Log.d(TAGT, "localPeerConnection->onIceCandidate");
                localPeerConnection.addIceCandidate(iceCandidate);
                Log.d(TAGT, "localPeerConnection->onIceCandidate->nioBinder.nioWriteString");
                nioBinder.nioWriteString("user1","localPeerConnection->onIceCandidate"+gson.toJson(iceCandidate));
            }
        });

    }

    private void A1() {
        //客户端创建 offer
        localPeerConnection.createOffer(new SdpObserver("创建local offer") {
            public void onCreateSuccess(SessionDescription sessionDescription) {
                Log.d(TAGT, "localPeerConnection.createOffer->onCreateSuccess"+gson.toJson(sessionDescription));
                localPeerConnection.setLocalDescription(new SdpObserver("local 设置本地 sdp"){
                    @Override
                    public void onCreateSuccess(SessionDescription sessionDescription) {
                        super.onCreateSuccess(sessionDescription);
                    }
                }, sessionDescription);

                Log.d(TAGT, "localPeerConnection.createOffer->onCreateSuccess->nioBinder.nioWriteString"+gson.toJson(sessionDescription));
                nioBinder.nioWriteString("user1","localPeerConnection.createOffer->onCreateSuccess"+gson.toJson(sessionDescription));
            }
        }, new MediaConstraints());
    }


    @Override
    public void showData(String user, String ss) {
        if (ss.startsWith("remotePeerConnection.createOffer->onCreateSuccess")) {
            SessionDescription sessionDescription = gson.fromJson(
                    ss.substring("remotePeerConnection.createOffer->onCreateSuccess".length()), SessionDescription.class);
            Log.d(TAGT, "remotePeerConnection.createOffer->onCreateSuccess:localPeerConnection.setRemoteDescription");
            localPeerConnection.setRemoteDescription(new SdpObserver("把 sdp 给到 local"), sessionDescription);
            Log.d(TAGT, "remotePeerConnection.createOffer->onCreateSuccess:localPeerConnection.createAnswer");
            localPeerConnection.createAnswer(new SdpObserver("创建 remote answer") {
                public void onCreateSuccess(SessionDescription sessionDescription1) {
                    Log.d(TAGT, "remotePeerConnection.createOffer->onCreateSuccess:localPeerConnection.createAnswer->onCreateSuccess");
                    localPeerConnection.setLocalDescription(new SdpObserver("remote 设置本地 sdp"), sessionDescription1);
                    nioBinder.nioWriteString("user1","localPeerConnection.createAnswer->onCreateSuccess"+gson.toJson(sessionDescription1));
                }
            }, new MediaConstraints());
        } else if (ss.startsWith("remotePeerConnection.createAnswer->onCreateSuccess")) {
            SessionDescription sessionDescription = gson.fromJson(
                    ss.substring("remotePeerConnection.createAnswer->onCreateSuccess".length()), SessionDescription.class);
            Log.d(TAGT, "remotePeerConnection.createAnswer->onCreateSuccess:localPeerConnection.setRemoteDescription");
            localPeerConnection.setRemoteDescription(new SdpObserver("把 sdp 给到 local"), sessionDescription);
        } else if (ss.startsWith("remotePeerConnection->onIceCandidate")) {
            IceCandidate iceCandidate = gson.fromJson(
                    ss.substring("remotePeerConnection->onIceCandidate".length()), IceCandidate.class);
            Log.d(TAGT, "remotePeerConnection->onIceCandidate:localPeerConnection.addIceCandidate");
            localPeerConnection.addIceCandidate(iceCandidate);
        } else {
            Log.d(TAGT, "----------"+ss);
        }
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
            if (enumerator.isBackFacing(deviceName)) {
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
    public void showError(String ss) {

    }

    @Override
    public void showConnectedUsers(String userNames) {

    }

}