package com.example.tongmin.mycamera;

import android.content.Context;
import android.hardware.Camera;
import android.widget.Toast;

/**
 * Created by TongMin on 2015/12/15.
 */
public class Constant {

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(Context context){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
            Toast.makeText(context,"你的相机不可用",Toast.LENGTH_SHORT).show();
        }
        return c; // returns null if camera is unavailable
    }
}
