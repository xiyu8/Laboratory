package com.jason.videotunneltwo;

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
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import org.webrtc.AudioTrack;
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
import org.webrtc.VideoTrack;

import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity implements NioPeriodChronicService.View {
    public final String TAG = "video_tunnel";
    public final String TAGT = "VideoStep";
    public final String  VIDEO_TRACK_ID = "ARDAMSv0";
    public final String  AUDIO_TRACK_ID = "ARDAMSa0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        gson = new Gson();
        initView();
        initData();
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
                    nioBinder.setView(MainActivity2.this);
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {

                }
            }, Context.BIND_AUTO_CREATE);
        }
    }


    //    SurfaceView mSurfaceView;
    SurfaceHolder surfaceHolder;
    CameraManager mCameraManager;
    Handler cameraHandler;
    private void initView() {
//        mSurfaceView =findViewById(R.id.local_video);
//        mLocalSurfaceView =findViewById(R.id.LocalSurfaceView);
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
    SurfaceViewRenderer /*mLocalSurfaceView,*/mRemoteSurfaceView;
    EglBase.Context eglBaseContext;
//    MediaStream localMediaStream;
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


///////////////////////////////////////本地流远端回显/////////////////////////////////////////////////////////////
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
            B0(); //将 localPeerConnection 提供给 其它端连接
        }
        commandCount++;
    }

    public void onClickk(View view) {
        startActivity(new Intent(this,NioPeriodChronicActivity.class));
    }


    private void B0() {
        List<PeerConnection.IceServer> iceServers = new ArrayList<>();
//        iceServers.add(new PeerConnection.IceServer("stun:stun.l.google.com:19302"));
        remotePeerConnection = mPeerConnectionFactory.createPeerConnection(iceServers, new PeerObserver() {
            public void onAddStream(MediaStream strem) {
                super.onAddStream(strem);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        strem.videoTracks.get(0).addSink(mRemoteSurfaceView);
                    }
                });
            }

            public void onIceCandidate(IceCandidate iceCandidate) {
                super.onIceCandidate(iceCandidate);
                Log.d(TAGT, "12_remotePeerConnection->PeerObserver.onIceCandidate: ");
                //command B->A 2:  localPeerConnection.addIceCandidate(iceCandidate);
                nioBinder.nioWriteString("localPeerConnection.addIceCandidate"+gson.toJson(iceCandidate));

            }
        });
    }




    @Override
    public void showError(String ss) {

    }

    @Override
    public void showData(String ss) {
        if (ss.startsWith("remotePeerConnection.setRemoteDescription")) {
            SessionDescription sessionDescription = gson.fromJson(
                    ss.substring("remotePeerConnection.setRemoteDescription".length()), SessionDescription.class);
            Log.d(TAGT, "5_localPeerConnection.createOffer->SdpObserver.onCreateSuccess:remotePeerConnection.setRemoteDescription ");
            remotePeerConnection.setRemoteDescription(new SdpObserver("把 sdp 给到 remote") {
                public void onCreateSuccess(SessionDescription sessionDescription1) {
                }
            }, sessionDescription);

            Log.d(TAGT, "5_localPeerConnection.createOffer->SdpObserver.onCreateSuccess:remotePeerConnection.createAnswer ");
            remotePeerConnection.createAnswer(new SdpObserver("创建 remote answer") {
                public void onCreateSuccess(SessionDescription sessionDescription1) {
                    /**
                     * 4. 此时调用setLocalDescription()方法将该 answer 保存到本地 remote 域，
                     * 然后将 answer 发送给对方
                     */
                    Log.d(TAGT, "9_remotePeerConnection.createAnswer->SdpObserver.onCreateSuccess: ");
                    Log.d(TAGT, "10_remotePeerConnection.createAnswer->SdpObserver.onCreateSuccess: remotePeerConnection.setLocalDescription:"+gson.toJson(sessionDescription1));
                    remotePeerConnection.setLocalDescription(new SdpObserver("remote 设置本地 sdp"), sessionDescription1);
                    //5. local 把 answer 保存到它的 remote 端，此时 sdp 交换结束
                    //command B->A 2:  remotePeerConnection.setRemoteDescription(new SdpObserver("把 sdp 给到 remote"), sessionDescription);
                    // localPeerConnection.setRemoteDescription(new SdpObserver("把 sdp 给到 local"), sessionDescription1);
                    nioBinder.nioWriteString("localPeerConnection.setRemoteDescription"+gson.toJson(sessionDescription1));
                }
            }, new MediaConstraints());
        } else
        if (ss.startsWith("remotePeerConnection.createAnswer")) {
        } else
        if (ss.startsWith("remotePeerConnection.addIceCandidate")) {
            IceCandidate iceCandidate = gson.fromJson(
                    ss.substring("remotePeerConnection.addIceCandidate".length()), IceCandidate.class);
            Log.d(TAGT, "8_localPeerConnection->PeerObserver.onIceCandidate: remotePeerConnection.addIceCandidate:"+gson.toJson(iceCandidate));
            remotePeerConnection.addIceCandidate(iceCandidate);
        } else
        if (ss.startsWith("remotePeerConnection.createPeerConnection")) {
            Log.d(TAGT, "8_localPeerConnection->PeerObserver.onIceCandidate: remotePeerConnection.createPeerConnection:");
            B0();
        } else{
            Log.d(TAGT, "----------"+ss);
        }

    }

    @Override
    public void showConnection(String ip, String port, String user) {

    }
}