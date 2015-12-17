package com.example.tongmin.mycamera;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Camera mCamera;
    private CameraPreview mPreview;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private MediaRecorder mediaRecorder;
    private FrameLayout preview;
    private boolean isRecording = false;
    private Camera.Parameters params;
    private TextView tvTime;
    private ImageButton changeCamera, imgAlbum, imgRecord, imgBack;
    private ImageView imgRedPoint;
    private AlphaAnimation alpha;
    //默认前置 记录当前的方向
    private int nowCameraDirection = Camera.CameraInfo.CAMERA_FACING_FRONT;
    private TimeThread timeThread;
    private long startTime;
    private SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
    private MyHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.cameralayout);

        findViewById();
        initView();
    }

    private void startOrStopRecrod() {

        if (isRecording) {
            stopRecord();
        } else {
            startRecord();
        }
    }

    private void startRedPointAnimationAndTime() {
        alpha = new AlphaAnimation(1.0f, 0f);
        alpha.setDuration(1000);
        alpha.setRepeatCount(-1);
        alpha.setRepeatMode(Animation.REVERSE);
        imgRedPoint.startAnimation(alpha);
        timeThread = new TimeThread();
        timeThread.start();
        startTime = System.currentTimeMillis();
    }

    static class MyHandler extends Handler {
        TextView tv;

        MyHandler(TextView tv) {
            this.tv = tv;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                tv.setText(msg.obj.toString());
            }
        }
    }

    private void stopRecord() {
        // stop recording and release camera
        try {
            //当刚start的时候就stop还没有录到数据就会报异常
            mediaRecorder.stop();  // stop the recording
            imgRedPoint.clearAnimation();

        } catch (Exception e) {
            Toast.makeText(this, "没有数据就停止", Toast.LENGTH_SHORT).show();
        }

        releaseMediaRecorder(); // release the MediaRecorder object
        mCamera.lock();         // take camera access back from MediaRecorder
        imgRecord.setImageResource(R.drawable.start);
        // inform the user that recording has stopped
        isRecording = false;
        changeCamera.setVisibility(View.VISIBLE);
    }

    private void startRecord() {
        if (prepareVideoRecorder()) {
            startRedPointAnimationAndTime();
            mediaRecorder.start();
            imgRecord.setImageResource(R.drawable.stop);
            isRecording = true;
            changeCamera.setVisibility(View.GONE);
        } else {
            Toast.makeText(MainActivity.this, "here????", Toast.LENGTH_SHORT).show();
            // prepare didn't work, release the camera
            releaseMediaRecorder();
            isRecording = false;
            changeCamera.setVisibility(View.VISIBLE);
            // inform user
        }
    }


    private void initView() {
        if (!checkCameraHardware(this)) {
            Toast.makeText(this, "你的手机不支持相机", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        if (!this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
            Toast.makeText(this, "你的手机不支持前置摄像", Toast.LENGTH_SHORT).show();
            nowCameraDirection = Camera.CameraInfo.CAMERA_FACING_BACK;
        }
        handler = new MyHandler(tvTime);
        // Create an instance of Camera
        mCamera = Constant.getCameraInstance(this, nowCameraDirection);


        params = mCamera.getParameters();
        params.set("orientation", "portrait");
        mCamera.setParameters(params);
        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        preview.addView(mPreview);
        mediaRecorder = new MediaRecorder();


        changeCamera.setOnClickListener(this);
        imgAlbum.setOnClickListener(this);
        imgRecord.setOnClickListener(this);
        imgBack.setOnClickListener(this);

        if (params.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        }
    }

    private void findViewById() {
        imgRecord = (ImageButton) findViewById(R.id.button_capture);
        changeCamera = (ImageButton) findViewById(R.id.camera_change);
        imgAlbum = (ImageButton) findViewById(R.id.img_album);
        imgBack = (ImageButton) findViewById(R.id.button_back);
        imgRedPoint = (ImageView) findViewById(R.id.img_red);
        tvTime = (TextView) findViewById(R.id.tvtime);
        preview = (FrameLayout) findViewById(R.id.camera_preview);
    }

    /**
     * Check if this device has a camera
     */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaRecorder();       // if you are using MediaRecorder, release it first
        releaseCamera();
    }


    private void releaseCamera() {
        if (mCamera != null) {
//            mCamera.stopPreview();
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

    /**
     * Create a file Uri for saving an image or video
     */
    private static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * Create a File for saving an image or video
     */
    private static File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IPEARL_VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }


    private boolean prepareVideoRecorder() {

        mediaRecorder = new MediaRecorder();

        int rotation = 0;
//        if (nowCameraDirection != OrientationEventListener.ORIENTATION_UNKNOWN) {

        if(nowCameraDirection == Camera.CameraInfo.CAMERA_FACING_FRONT){
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_FRONT, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
//                rotation = (cameraInfo.orientation - nowCameraDirection + 360) % 360;
//                rotation = (cameraInfo.orientation - 180 + 360) % 360;
                mediaRecorder.setOrientationHint(180);

            } else {  // back-facing camera
            }

        }

        // Step 1: Unlock and set camera to MediaRecorder
        mCamera.unlock();
        mediaRecorder.setCamera(mCamera);

        // Step 2: Set sources
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        // Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
        mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));

        // Step 4: Set output file
        mediaRecorder.setOutputFile(getOutputMediaFile(MEDIA_TYPE_VIDEO).toString());

        // Step 5: Set the preview output
        mediaRecorder.setPreviewDisplay(mPreview.getHolder().getSurface());

        // Step 6: Prepare configured MediaRecorder
        try {
            mediaRecorder.prepare();
        } catch (IllegalStateException e) {
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            releaseMediaRecorder();
            return false;
        }
        return true;
    }

    private void releaseMediaRecorder() {

        if (mediaRecorder != null) {
            mediaRecorder.reset();   // clear recorder configuration
            mediaRecorder.release(); // release the recorder object
            mediaRecorder = null;
            timeThread.stop_();
            mCamera.lock();           // lock camera for later use
        }
    }

    private void changeCamera() {
        nowCameraDirection = nowCameraDirection ==
                Camera.CameraInfo.CAMERA_FACING_FRONT ?
                Camera.CameraInfo.CAMERA_FACING_BACK :
                Camera.CameraInfo.CAMERA_FACING_FRONT;
        switchCamera(nowCameraDirection);
    }

    public void switchCamera(int cameraType) {
        if (this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
            if (Camera.getNumberOfCameras() > cameraType) {
                // Set selected camera
                this.nowCameraDirection = cameraType;

            } else {
                // Set default camera (Rear)
                this.nowCameraDirection = Camera.CameraInfo.CAMERA_FACING_BACK;


            }

            if (mCamera != null) {
                releaseCamera();
                mCamera = Constant.getCameraInstance(this, nowCameraDirection);
                // Destroy previuos Holder


                mPreview.surfaceDestroyed(mPreview.getHolder());
                mPreview.getHolder().removeCallback(mPreview);
                mPreview.destroyDrawingCache();
                preview.removeView(mPreview);
                // Remove and re-Add SurfaceView

                mPreview = new CameraPreview(this, mCamera);
                preview.addView(mPreview);

            }
        }
    }

    class TimeThread extends Thread {

        private boolean flag = true;

        @Override
        public void run() {
            while (flag) {

                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }

                Message msg = Message.obtain(handler, 1);
                msg.obj = sdf.format(System.currentTimeMillis() - startTime);
                msg.sendToTarget();
            }
        }

        void stop_() {
            this.flag = false;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_capture:
                startOrStopRecrod();
                break;
            case R.id.img_album:
                startActivity(new Intent(MainActivity.this, RecordMoveActivity.class));
                break;
            case R.id.camera_change:
                changeCamera();
                break;
            case R.id.button_back:
                finish();
                break;

        }
    }


}
