package com.jason.customcamera;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.hardware.Camera.Size;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

public class CameraActivity extends AppCompatActivity implements
        SurfaceHolder.Callback {
    private static final String TAG = "TestCameraActivity";
    public static final String KEY_FILENAME = "filename";
    private Button mTakePhoto;
    private SurfaceView mSurfaceView;
    private CameraDevice mCamera;
    private String mFileName;
    private OrientationEventListener mOrEventListener; // 设备方向监听器
    private Boolean mCurrentOrientation; // 当前设备方向 横屏false,竖屏true




    private CameraManager manager;
    private Handler childHandler, mainHandler;
    private CaptureRequest.Builder mPreviewBuilder;
    private CameraCaptureSession mSession;
    private ImageReader mImageReader;
    // 创建拍照需要的CaptureRequest.Builder
    private CaptureRequest.Builder captureRequestBuilder;
    //6.0结束

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);


        mTakePhoto = (Button) findViewById(R.id.take_photo);

//        startOrientationChangeListener(); // 监听方向发生改变
        mSurfaceView = (SurfaceView) findViewById(R.id.my_surfaceView);
        surfaceHolder = mSurfaceView.getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.addCallback(this); // 回调接口




        //很多过程都变成了异步的了，所以这里需要一个子线程的looper
        HandlerThread handlerThread = new HandlerThread("Camera2");
        handlerThread.start();
        childHandler = new Handler(handlerThread.getLooper());
        mainHandler = new Handler(getMainLooper());
        manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        surfaceHolder = mSurfaceView.getHolder();
        surfaceHolder.setKeepScreenOn(true);
        surfaceHolder.addCallback(this);
        //设置照片的大小
        mImageReader = ImageReader.newInstance(3264, 1840, ImageFormat.JPEG, 2);
        mImageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader imageReader) {
                // 拿到拍照照片数据
                Image image = imageReader.acquireNextImage();
                ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);//由缓冲区存入字节数组
                image.close();
                //saveBitmap(bytes);//保存照片的处理
            }
        }, mainHandler);

    }

    /**
     * 单拍照片
     */
    private void takePicture() {
        if (mCamera == null) {
            return;
        }
        if (mSession != null && captureRequestBuilder != null) {
            //拍照
            try {
                CaptureRequest cr = captureRequestBuilder.build();
                mSession.capture(cr, null, null);//单拍API，也可以调连拍的哦
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }

//    /**
//     * 6.0开
//     */
//    public void openLight6() {
//        try {
//            mPreviewBuilder.set(CaptureRequest.FLASH_MODE,
//                    CaptureRequest.FLASH_MODE_TORCH);
//            updatePreview(mSession);
//        } catch (CameraAccessException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 6.0关
//     */
//    public void closeLight6() {
//        try {
//            mPreviewBuilder.set(CaptureRequest.FLASH_MODE,
//                    CaptureRequest.FLASH_MODE_OFF);
//            updatePreview(mSession);
//        } catch (CameraAccessException e) {
//            e.printStackTrace();
//        }
//    }


    CameraManager mCameraManager;

    @SuppressLint("NewApi")
    @Override
    public void onResume() {
        super.onResume();
        // 开启相机
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {

//            try {
//                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                    // TODO: Consider calling
//                    //    ActivityCompat#requestPermissions
//                    // here to request the missing permissions, and then overriding
//                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                    //                                          int[] grantResults)
//                    // to handle the case where the user grants the permission. See the documentation
//                    // for ActivityCompat#requestPermissions for more details.
//                    return;
//                }
//                mCameraManager.openCamera(mCameraId, mCameraDeviceCallback, null);
//            } catch (CameraAccessException e) {
//                e.printStackTrace();
//            }
        } else {
//            mCamera = CameraX.open();
        }
    }
    CameraCharacteristics mCameraCharacteristics;
    String mCameraId;
    private boolean chooseCameraIdByFacing() {
        try {
//            int internalFacing = INTERNAL_FACINGS.get(mFacing);
            final String[] ids = mCameraManager.getCameraIdList();
            for (String id : ids) {
                CameraCharacteristics characteristics = mCameraManager.getCameraCharacteristics(id);
                Integer level = characteristics.get(
                        CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL);
                if (level == null ||
                        level == CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY) {
                    continue;
                }
                Integer internal = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (internal == null) {
                    throw new NullPointerException("Unexpected state: LENS_FACING null");
                }
//                if (internal == internalFacing) {
                    mCameraId = id;
                    mCameraCharacteristics = characteristics;
                    return true;
//                }
            }

            return true;
        } catch (CameraAccessException e) {
            throw new RuntimeException("Failed to get a list of camera devices", e);
        }
    }

    private final CameraDevice.StateCallback mCameraDeviceCallback
            = new CameraDevice.StateCallback() {

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
                camera.createCaptureSession(Arrays.asList(surfaceHolder.getSurface(), mImageReader.getSurface()), mSessionStateCallback, childHandler);
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

    /**
     * 会话状态回调
     */
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
//
//        Parameters parameters = mCamera.getParameters();// 获取mCamera的参数对象
//        Size largestSize = getBestSupportedSize(parameters.getSupportedPreviewSizes());
//        parameters.setPreviewSize(largestSize.width, largestSize.height);// 设置预览图片尺寸
//        largestSize = getBestSupportedSize(parameters.getSupportedPictureSizes());// 设置捕捉图片尺寸
//        parameters.setPictureSize(largestSize.width, largestSize.height);
//        mCamera.setParameters(parameters);
//
//        try {
//            mCamera.startPreview();
//        } catch (Exception e) {
//            if (mCamera != null) {
//                mCamera.release();
//                mCamera = null;
//            }
//        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            //需要相机权限
            if (ActivityCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            //获取可用相机设备列表
            String[] CameraIdList = manager.getCameraIdList();
            //打开相机
            manager.openCamera(CameraIdList[0], mCameraDeviceCallback, mainHandler);
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

    private Size getBestSupportedSize(List<Size> sizes) {
        // 取能适用的最大的SIZE
        Size largestSize = sizes.get(0);
        int largestArea = sizes.get(0).height * sizes.get(0).width;
        for (Size s : sizes) {
            int area = s.width * s.height;
            if (area > largestArea) {
                largestArea = area;
                largestSize = s;
            }
        }
        return largestSize;
    }


}