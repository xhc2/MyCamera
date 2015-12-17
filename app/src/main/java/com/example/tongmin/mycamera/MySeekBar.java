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
    private Paint mPaint;
    private int width, height;
    private RectF rect = new RectF();
    private RectF rectBar = new RectF();
    private int barWidth, barHeight, viewLeft, viewRight, viewTop, viewBottom;
    private int downX, downY, moveX, moveY;
    private MyCircle circle;
    private float max, current;
    private OnChangeLinstener changeLinstener;

    public MySeekBar(Context context) {
        this(context, null);
    }

    public MySeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MySeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        mPaint = new Paint();
//        paint.setColor(Color.parseColor("#000000"));
        paint.setColor(Color.parseColor("#B2B2C4"));
        mPaint.setColor(Color.parseColor("#ffffff"));


    }


    public void setChangeLinstener(OnChangeLinstener changeLinstener) {
        this.changeLinstener = changeLinstener;
    }

    public void setMax(float max) {
        this.max = max;
    }

    public void setCurrent(float current) {
        if(current >= max) current = max ;
        this.current = current;
        changePosition((int)(((max - current) / max) * getHeight()) , true);
    }

    public float getCurrent() {
        return current;
    }

    public float getMax() {
        return max;
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
                changePosition(downY , false);
                break;
            case MotionEvent.ACTION_MOVE:
                moveX = (int) event.getX();
                moveY = (int) event.getY();
                if (Math.abs(moveY - downY) > 10) {
                    changePosition(moveY , false);

                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;

    }


//flag 标志是否回调 true回调 -1，false回调 正常值 避免递归导致栈溢出
    private void changePosition(int value , boolean flag ) {

        rect.set(viewLeft + barWidth, value, viewRight - barWidth, viewBottom);
        circle.setPointY(value);
        if (changeLinstener != null && !flag) {
            changeLinstener.changeValue((float)((getHeight() * 1.0 - value *1.0) / getHeight()*1.0));
        }
        else if(changeLinstener != null && flag){
            changeLinstener.changeValue(-1);
        }
        invalidate();
    }

    private void drawCircle(Canvas canvas) {
        if (circle == null) {
            circle = new MyCircle();
            circle.setPointX(getWidth() / 2);
            circle.setPointY(getHeight());
            circle.setR(getWidth() / 2);

        }
        if (circle.getPointY() < viewTop + circle.getR()) {
            circle.setPointY(viewTop + circle.getR());
        } else if (circle.getPointY() > viewBottom - circle.getR()) {
            circle.setPointY(viewBottom - circle.getR());
        }
        canvas.drawCircle(circle.getPointX(), circle.getPointY(), circle.getR(), mPaint);
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

        canvas.drawRoundRect(rectBar, 100, 100, paint);
    }

    public interface OnChangeLinstener {
        //返回一个比例
        void changeValue(float ratio);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBar(canvas);
        canvas.drawRoundRect(rect, 100, 100, mPaint);
        drawCircle(canvas);
    }
}
