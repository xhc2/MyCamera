package com.example.tongmin.mycamera;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * Created by xhc on 2015/12/16.
 */
public class MyVideoView extends VideoView {

    public MyVideoView(Context context) {
        super(context);
    }

    public MyVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int width = getDefaultSize(0, widthMeasureSpec);
        int height = getDefaultSize(0, heightMeasureSpec);
        setMeasuredDimension(width , height);
    }

}
