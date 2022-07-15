package com.jason.customcamera;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements
        SurfaceHolder.Callback {
    private static final String TAG = "TestCameraActivity";
    private SurfaceView mSurfaceView;
    private CameraDevice mCamera;
    private Handler childHandler, mainHandler;
    private CaptureRequest.Builder mPreviewBuilder;
    CameraManager mCameraManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSurfaceView = (SurfaceView) findViewById(R.id.my_surfaceView);

        mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        //很多过程都变成了异步的了，所以这里需要一个子线程的looper
        HandlerThread handlerThread = new HandlerThread("Camera2");
        handlerThread.start();
        childHandler = new Handler(handlerThread.getLooper());
        mainHandler = new Handler(getMainLooper());
        surfaceHolder = mSurfaceView.getHolder();
        surfaceHolder.setKeepScreenOn(true);
        surfaceHolder.addCallback(this);


        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        int width=(outMetrics.heightPixels)*4/6;
        mSurfaceView.getLayoutParams().width = width;
        mSurfaceView.requestLayout();
    }


    private final CameraDevice.StateCallback mCameraDeviceCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            //表示相机打开成功，可以真正开始使用相机，创建Capture会话
            mCamera = camera;
            //创建Capture会话
            try {
                // 创建预览需要的CaptureRequest.Builder
                mPreviewBuilder = camera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
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
                // 创建CameraCaptureSession，该对象负责管理处理预览请求和拍照请求
                camera.createCaptureSession(Arrays.asList(surfaceHolder.getSurface()), mSessionStateCallback, childHandler);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onClosed(@NonNull CameraDevice camera) {
            //调用Camera.close()后的回调方法
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            //当相机断开连接时回调该方法，需要进行释放相机的操作
            mCamera = null;
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            //当相机打开失败时，需要进行释放相机的操作
            Log.e(TAG, "onError: " + camera.getId() + " (" + error + ")");
            mCamera = null;
        }
    };
    private CameraCaptureSession mSession;
    private CaptureRequest.Builder captureRequestBuilder;

    private CameraCaptureSession.StateCallback mSessionStateCallback = new CameraCaptureSession.StateCallback() {
        @Override
        public void onConfigured(CameraCaptureSession session) {
            mSession = session;
            if (mCamera != null && captureRequestBuilder == null) {
                try {
                    captureRequestBuilder = mCamera.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
                    // 将imageReader的surface作为CaptureRequest.Builder的目标
                    captureRequestBuilder.addTarget(surfaceHolder.getSurface());
                    //关闭自动对焦
                    captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_OFF);
                    captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_OFF);
                    //设置拍摄图像时相机设备是否使用光学防抖（OIS）。
                    captureRequestBuilder.set(CaptureRequest.LENS_OPTICAL_STABILIZATION_MODE, CaptureRequest.LENS_OPTICAL_STABILIZATION_MODE_ON);
                    captureRequestBuilder.set(CaptureRequest.SENSOR_SENSITIVITY, 400);
                    //曝光补偿//
                    captureRequestBuilder.set(CaptureRequest.CONTROL_AE_EXPOSURE_COMPENSATION, 0);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }
            try {
                session.setRepeatingRequest(mPreviewBuilder.build(), new CameraCaptureSession.CaptureCallback() {
                    @Override
                    public void onCaptureStarted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, long timestamp, long frameNumber) {
                        super.onCaptureStarted(session, request, timestamp, frameNumber);
                    }
                }, childHandler);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onConfigureFailed(CameraCaptureSession session) {

        }
    };



    @Override
    public void onPause() {
        super.onPause();
        // 释放相机
        if (mCamera != null) {
            mCamera.close();
            mCamera = null;
        }
    }

    ////////////////////////////////SurfaceView callback//////////////////////////////////////////
    SurfaceHolder surfaceHolder;
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // SurfaceView尺寸发生改变时（首次在屏幕上显示同样会调用此方法），初始化mCamera参数，启动Camera预览
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            //需要相机权限
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            //获取可用相机设备列表
            String[] CameraIdList = mCameraManager.getCameraIdList();
            String cameraId = null;
//            Size[] outputSizes = new Size[0];
            for (String id : CameraIdList) {
                CameraCharacteristics characteristics=mCameraManager.getCameraCharacteristics(id);
                if (characteristics.get(CameraCharacteristics.LENS_FACING).equals(CameraCharacteristics.LENS_FACING_FRONT)) {
                    cameraId = id;
                    StreamConfigurationMap streamConfigurationMap = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                    Size[] outputSizes=streamConfigurationMap.getOutputSizes(SurfaceHolder.class);
//                    holder.setFixedSize(outputSizes[0].getWidth(), outputSizes[0].getHeight());
                    for (Size outputSize : outputSizes) {
                        //TODO:
                    }
                    break;
                }
            }
            if(cameraId==null) cameraId = CameraIdList[0];
            //打开相机
            mCameraManager.openCamera(cameraId, mCameraDeviceCallback, mainHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //释放资源
        if (mCamera != null) {
            mCamera.close();
            mCamera = null;
        }
    }


}