package com.example.tongmin.mycamera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

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
        paint.setColor(Color.parseColor("#2D2C34"));
        mPaint.setColor(Color.parseColor("#B3B1C6"));


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
                changePosition(downY , true);
                break;
            case MotionEvent.ACTION_MOVE:
                moveX = (int) event.getX();
                moveY = (int) event.getY();
                if (Math.abs(moveY - downY) > 10) {
                    changePosition(moveY , true);

                }
                break;
            case MotionEvent.ACTION_UP:

                changePosition((int) event.getY() , false);
                break;
        }
        return true;

    }


//flag 标志是否回调 true回调 -1，false回调 正常值 避免递归导致栈溢出
    private void changePosition(int value , boolean flag ) {

        rect.set(barWidth, value, 2*barWidth, viewBottom);
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
        if (circle.getPointY() <circle.getR()) {
            circle.setPointY(circle.getR());
        } else if (circle.getPointY() > getHeight() - circle.getR()) {
            circle.setPointY( getHeight() - circle.getR());
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
        rectBar.set(barWidth, 0, 2 * barWidth, barHeight);
        canvas.drawRoundRect(rectBar, 10, 10, paint);
    }

    public interface OnChangeLinstener {
        //返回一个比例
        void changeValue(float ratio);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBar(canvas);
        drawCircle(canvas);
        canvas.drawRoundRect(rect, 10, 10, mPaint);
    }
}
