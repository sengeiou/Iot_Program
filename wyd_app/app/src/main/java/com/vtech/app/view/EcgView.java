package com.vtech.app.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/8/23.
 */

public class EcgView extends View {
    //画笔
    protected Paint mPaint;

    protected Paint mPath;

    //网格颜色
//    protected int mGridColor = Color.parseColor("#1b4200");
    protected int mGridColor = Color.parseColor("#092100");

    //小网格颜色
//    protected int mSGridColor = Color.parseColor("#092100");
    protected int mSGridColor = Color.parseColor("#e3e3e3");
    //背景颜色
    protected int mBackgroundColor = Color.WHITE;
    //自身的大小
    protected int mWidth, mHeight;

    //网格宽度
    protected int mGridWidth = 50;
    //小网格的宽度
    protected int mSGridWidth = 10;
    //#FA8E7E
    protected int mPathColor = Color.parseColor("#FA8E7E");

    private int maxY = 5000;

    Context context;

    public EcgView(Context context) {
        super(context);
        this.context = context;
    }

    public EcgView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public EcgView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public EcgView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth = w;
        mHeight = h;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initBackground(canvas);
    }

    private void initBackground(Canvas canvas) {
        mPaint = new Paint();
        mPath = new Paint();
        canvas.drawColor(mBackgroundColor);
        //画小网格

        //竖线个数
        int vSNum = mWidth / mSGridWidth;

        //横线个数
        int hSNum = mHeight / mSGridWidth;
        mPaint.setColor(mSGridColor);
        mPaint.setStrokeWidth(2);
        //画竖线
        for (int i = 0; i < vSNum + 1; i++) {
            canvas.drawLine(i * mSGridWidth, 0, i * mSGridWidth, mHeight, mPaint);
        }
        //画横线
        for (int i = 0; i < hSNum + 1; i++) {

            canvas.drawLine(0, i * mSGridWidth, mWidth, i * mSGridWidth, mPaint);
        }

        //竖线个数
        int vNum = mWidth / mGridWidth;
        //横线个数
        int hNum = mHeight / mGridWidth;
        mPaint.setColor(mGridColor);
        mPaint.setStrokeWidth(2);
        //画竖线
        for (int i = 0; i < vNum + 1; i++) {
            canvas.drawLine(i * mGridWidth, 0, i * mGridWidth, mHeight, mPaint);
        }
        //画横线
        for (int i = 0; i < hNum + 1; i++) {
            canvas.drawLine(0, i * mGridWidth, mWidth, i * mGridWidth, mPaint);
        }
        mPath.setColor(mPathColor);
        mPath.setStrokeWidth(3);
        final float scale = context.getResources().getDisplayMetrics().density;

        if (data.size() > 0) {
            //    画搏动折线
            for (int k = 1; k < data.size(); k++) {
                if (data.get(k) == xx) {
                    float b = (k) * startX * scale;
                    float x = 0;
                    for (int i = 1; i < xlist.size(); i++) {
                        HashMap<Integer, Integer> map1 = xlist.get(i - 1);
                        int x1 = map1.get(0);
                        int y1 = map1.get(1);
                        HashMap<Integer, Integer> map2 = xlist.get(i);
                        int x2 = map2.get(0);
                        int y2 = map2.get(1);
                        if (i == 1) {
                            x = x1 - b;
                        }
                        canvas.drawLine(x1 - x, y1 * mHeight / maxY, x2 - x, y2 * mHeight / maxY, mPath);
                    }
                } else {
                    float y2 = data.get(k - 1) * scale;
                    float yStop = data.get(k) * scale;
                    canvas.drawLine((k - 1) * startX * scale, y2 * mHeight / maxY, k * startX * scale, yStop * mHeight / maxY, mPath);
                }
            }
        }

        if (data.size() >= 600) {
            data.remove(0);//移除左边图形  让图形移动起来
        }
    }

    ArrayList<Integer> data = new ArrayList<>();

    public void setData(int i) {
        data.add(getMobileHeight(context) + (i / 5));
        postInvalidate();
    }

    public void setDatas(List<Integer> datas) {
        if (datas != null) {
            int size = datas.size();
            for (int i = 0; i < size; i++) {
                int value = datas.get(i);
                if (value >= 65200) {
                    value = 0;
                }
                postData(value, i);
            }
        }

    }

    private void postData(int a, int i) {
        EcgRun mRunnable = new EcgRun();
        mRunnable.seti(a);
        postDelayed(mRunnable, 100 * i);
    }

    public void clearData() {
        if (data != null) {
            data.clear();
            postInvalidate();
        }
    }

    int xx;

    int startX = 3;

    public int getMobileHeight(Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;
        xx = (int) (mHeight / scale);
        return (int) (mHeight / scale );
    }

    ArrayList<HashMap<Integer, Integer>> xlist = new ArrayList<>();

    class EcgRun implements Runnable {
        private int data;

        public void seti(int data) {
            this.data = data;
        }

        public EcgRun() {
        }

        @Override
        public void run() {
            setData(data);
        }
    }
}
