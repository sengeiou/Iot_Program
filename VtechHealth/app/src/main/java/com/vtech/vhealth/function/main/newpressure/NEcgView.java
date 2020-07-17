package com.vtech.vhealth.function.main.newpressure;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class NEcgView extends View {
    private static final String TAG="NEcgView";
    private static final int FLAG=0x1234;
    private int high=200;
    private int  maxY=4000;
    private int scale = 10;  //刻度长度  默认是20 调宽度
    private int xScale = 20;  //刻度长度  默认是20 调宽度
    private int width = 500;
    private int maxDataSize = width / scale;
    private int xPoint = 2;
    private int yPoint = 0;
    private Paint paint = new Paint();

    private int padding=5;
    private Bitmap bitmap;
    private Bitmap blackBitmap;
    private int lineColor=Color.GRAY;

    private List<Integer> data = new ArrayList<>();

    private Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            if(msg.what == FLAG){
                NEcgView.this.invalidate();
//                Log.i(TAG,"===handleMessage");
            }
        };
    };

    public NEcgView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
//        Log.i(TAG,"===");
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        Log.i(TAG,"===onLayout"+getWidth());
        width =getWidth();
        high=getHeight();
        maxDataSize = width / scale;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        Log.i(TAG,"===onMeasure"+getWidth());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        Log.i(TAG,"===onDraw"+getWidth());

        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true); //去锯齿
        paint.setStrokeWidth(3);
//        paint.setColor(Color.BLUE); //可以去配置
        paint.setColor(lineColor); //可以去配置
//       绘制背景图片
        canvas.drawBitmap(getBitmap(), 0, 0, paint);
//        canvas.drawBitmap(getBlackBitmap(), 0, 0, paint);
        drawLine(canvas ,paint);
        drawText(canvas);

    }

    //绘制表格作为背景
    private void drawBg(Canvas canvas){
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true); //去锯齿
        paint.setColor(Color.GRAY); //可以去配置
        //绘制了Y轴
        int xLen=maxDataSize+1;
        for (int i = 0; i < xLen; i++) {
            canvas.drawLine(xPoint+i* scale, padding, xPoint+i* scale, high-padding, paint);
        }
        //绘制X轴
        int yLen=high / scale;
        for (int i = 0; i < yLen; i++) {
            canvas.drawLine(xPoint, padding+i* scale, width, padding+i* scale, paint);
        }
    }

    //背景图
    private Bitmap getBitmap(){
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(width, high, Bitmap.Config.ARGB_8888);
            Canvas bitmapCanvas = new Canvas(bitmap);
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setAntiAlias(true); //去锯齿
            paint.setColor(Color.GRAY); //可以去配置

            //绘制了Y轴
            int xLen=maxDataSize+1;
            for (int i = 0; i < xLen; i++) {
                bitmapCanvas.drawLine(xPoint+i* xScale, padding, xPoint+i* xScale, high, paint);
            }
            //绘制X轴
            int yLen=high / xScale;
            for (int i = 0; i < yLen; i++) {
                bitmapCanvas.drawLine(xPoint, padding+i* xScale, width, padding+i* xScale, paint);
            }
            bitmapCanvas.drawLine(xPoint, high, width, high, paint);
        }
        return bitmap;
    }

    private String[] text={"240","200","160","120","80","40","0"};

    private void drawText(Canvas canvas){
        Paint paint=new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(2);
        paint.setTextSize(10);
        paint.setColor(Color.YELLOW);

        int yLen=high / xScale;
        int  count=0;
        for (int i = 0; i < yLen; i++) {
            if (i%2==1) {
              canvas.drawText(text[count], xPoint,  i* xScale, paint);
              count++;
            }
        }

//
//        //画两条线标记位置
//        paint.setStrokeWidth(4);
//        paint.setColor(Color.RED);
//        canvas.drawLine(0, 400, 2000, 400, paint);
//        paint.setColor(Color.BLUE);
//        canvas.drawLine(200, 0, 200, 2000, paint);
    }

    //背景图
    private Bitmap getBlackBitmap(){
        if (blackBitmap == null) {
            blackBitmap = Bitmap.createBitmap(width, high, Bitmap.Config.ARGB_8888);
            Canvas bitmapCanvas = new Canvas(blackBitmap);
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setAntiAlias(true); //去锯齿
            paint.setColor(Color.BLACK); //可以去配置
            bitmapCanvas.drawRect(0, 0, width,  high, paint);
        }
        return blackBitmap;
    }

    private void drawLine(Canvas canvas, Paint paint) {
        if(data.size() > 1){
            for(int i=1; i<data.size(); i++){
                float y0=data.get(i-1)*high/maxY;
                float y1=data.get(i)*high/maxY;
//                Log.e(TAG,"  drawLine y0= "+y0+"  y1"+y1);
                canvas.drawLine(xPoint + (i-1) * scale, high  - y0-yPoint,
                        xPoint + i * scale,       high- y1-yPoint, paint);
            }
        }
    }

    public void clearData(){
        data.clear();
        handler.sendEmptyMessage(FLAG);
    }

    public void setData( List<Integer> d ){
        if(data.size() >= maxDataSize){
            data.remove(0);
        }
        data.addAll(d);
        handler.sendEmptyMessage(FLAG);
    }

    public List<Integer> getData(){
        return data;
    }

    public void setGreenColor(){
        lineColor=Color.GREEN;
    }
    public void setRedColor(){
        lineColor=Color.RED;
    }
    public void setGrayColor(){
        lineColor=Color.GRAY;
    }
    public static void main(String[] args) {
        int color=Color.rgb(254, 0, 0);
        System.out.printf("===="+color);
    }

}
