package com.example.tongmin.mycamera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by TongMin on 2015/12/17.
 */
public class MySeekBar extends View {

    private Paint paint;
    private int width, height;
    private RectF rect = new RectF();
    private RectF rectBar = new RectF();
    private int barWidth, barHeight, viewLeft, viewRight, viewTop, viewBottom;
    private int downX, downY, moveX, moveY;

    public MySeekBar(Context context) {
        this(context, null);
    }

    public MySeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MySeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        paint.setColor(Color.parseColor("#ffffff"));
//        paint.setColor(Color.parseColor("#B2B2C4"));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        Toast.makeText(getContext(), "width"+width+"height"+height, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        width = getWidth();
        height = getHeight();
        viewLeft = getLeft();
        viewTop = getTop();
        viewBottom = getBottom();
        viewRight = getRight();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                downY = (int) event.getY();
                rect.set(0, downY, viewRight, viewBottom);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                moveX = (int) event.getX();
                moveY = (int) event.getY();
                if (Math.abs(moveY - downY) > 10) {
                    rect.set(0, moveY, viewRight, viewBottom);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;

    }

    //绘画中间的bar
    private void drawBar(Canvas canvas) {
        barWidth = getWidth() / 3;
        barHeight = getHeight();
        viewLeft = getLeft();
        viewTop = getTop();
        viewBottom = getBottom();
        viewRight = getRight();
        rectBar.set(viewLeft + barWidth, viewTop, viewLeft + 2 * barWidth, barHeight);
        Log.e("xhc", "left " + (viewLeft + barWidth) + " viewTop " + viewTop + " right " + (viewLeft + 2 * barWidth) + " bottom " + barHeight);
        canvas.drawRect(rectBar, paint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBar(canvas);
//        canvas.drawRoundRect(rect,10,10,paint);
//        canvas.drawRect(rect,paint);

    }
}
