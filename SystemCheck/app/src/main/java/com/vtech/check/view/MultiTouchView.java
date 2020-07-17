package com.vtech.check.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.vtech.check.R;

import java.util.ArrayList;
import java.util.List;

public class MultiTouchView extends View {

    public MultiTouchView(Context context) {
        super(context);
    }

    public MultiTouchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MultiTouchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 用来存放点的信息
     */
    List<Point> pointList = null;

    @Override
    public void onDraw(Canvas canvas) {
        Log.d("TouchView","onDraw");
        if (pointList == null){
            return;
        }
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.blue));
        paint.setStrokeWidth(1);
        Paint paint2 = new Paint();
        paint2.setColor(getResources().getColor(R.color.white));
        paint2.setStrokeWidth(1);
        paint2.setStyle(Paint.Style.FILL);
        paint2.setStrokeWidth(1);
        paint2.setTextSize(200);
        paint2.setTextAlign(Paint.Align.CENTER);

        //遍历画点
        for (Point point:pointList) {
            canvas.drawLine(0, (float)point.y, canvas.getWidth(),(float)point.y, paint);
            canvas.drawLine((float)point.x, 0, point.x,canvas.getHeight(), paint);

            canvas.drawText(String.valueOf(pointList.size()), canvas.getWidth()/2, canvas.getHeight()/2, paint2);
//            canvas.drawCircle((float)point.x,(float)point.y,100,paint);//画圈
        }

    }
    public void setOnMultiTouchListener(OnMultiTouchListener listener){
        setOnTouchListener(listener);
    }


    public static class OnMultiTouchListener implements OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            MultiTouchView multiTouchView = (MultiTouchView) v;
            if(multiTouchView.pointList == null){
                multiTouchView.pointList = new ArrayList<Point>();
            }
            multiTouchView.pointList.clear();
            Paint paint = new Paint();
            paint.setColor(Color.RED);
            int pointerId = (event.getAction()&MotionEvent.ACTION_POINTER_ID_MASK)>>> MotionEvent.ACTION_POINTER_ID_SHIFT;
            int count = event.getPointerCount();
            for (int i = 0; i < count; i++) {
                float x=event.getX(i);
                float y=event.getY(i);
                if(pointerId==i&&(event.getAction()& MotionEvent.ACTION_MASK)==MotionEvent.ACTION_POINTER_UP){
                    continue;
                }else if(count == 1 && (event.getAction()& MotionEvent.ACTION_MASK)==MotionEvent.ACTION_UP){

                }else{
                    multiTouchView.pointList.add(new Point((int) x, (int) y));
                }

//                Log.d("OnMultiTouchListener_onTouch",i+"pinter-----x = "+x+",y = "+y);
            }
            multiTouchView.postInvalidate();


            return true;
        }
    }
}
