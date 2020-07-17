package com.vtech.check.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;


 public class DrawLineView extends SurfaceView implements SurfaceHolder.Callback {

        //    最多的触摸点数量
        private static final int MAX_TOUCHPOINTS=10;
        //    提示问题
        private static final String START_TEXT="请单击或多点触摸屏幕进行测试";
        //    文字笔画
        private Paint textPaint=new Paint();
        //    圆形画笔
        private Paint touchPaints[]=new Paint[MAX_TOUCHPOINTS];
        //   对应每一个圆形画笔的颜色
        private int colors[]=new int[MAX_TOUCHPOINTS];
        //    记录屏幕的宽度和高度
        private int width,height;
        //    放大的倍数
        private float scale =2.0f;


        public DrawLineView(Context context) {
            super(context);
            init();

        }
        public DrawLineView(Context context, AttributeSet attrs) {
         super(context, attrs);
         init();
        }


        public DrawLineView(Context context, AttributeSet attrs, int defStyle) {

         super(context, attrs, defStyle);
         init();
        }



     private void init() {
         //        得到当前的view的surfaceHolder对象
         SurfaceHolder holder=getHolder();
         //        设置当前holder的回调方法
         holder.addCallback(this);
         //        确保我们的View能获得输入焦点
         setFocusable(true);
         //        确保能接受到触屏事件
         setFocusableInTouchMode(true);
         //        初始化文字笔的颜色
            textPaint.setColor(Color.WHITE);
         //        定义十种按键的颜色值
            colors[0]=Color.BLUE;//蓝色
            colors[1]=Color.RED;//红色
            colors[2]=Color.GREEN;//绿色
            colors[3]=Color.YELLOW;//黄色
            colors[4]=Color.CYAN;//蓝绿色
            colors[5]=Color.MAGENTA;//洋红色
            colors[6]=Color.DKGRAY;//深灰色
            colors[7]=Color.WHITE;//白色
            colors[8]=Color.LTGRAY;//浅灰色
            colors[9]=Color.GRAY;//灰色
          //        分别初始化每个手指的颜色值的笔
            for (int i=0;i<MAX_TOUCHPOINTS;i++){
                touchPaints[i]=new Paint();
                touchPaints[i].setColor(colors[i]);
            }
        }


        /**
         * 处理触屏事件
         */
        @Override
        public boolean onTouchEvent(MotionEvent event){
//        获得屏幕触点数量
            int pointerCount=event.getPointerCount();
            if (pointerCount>MAX_TOUCHPOINTS){
                pointerCount=MAX_TOUCHPOINTS;
            }
//        锁定Canvas，开始进行相应的界面处理
            Canvas c=getHolder().lockCanvas();
            if (c!=null){
//            定义canvas的背景颜色值为黑色
                c.drawColor(Color.BLACK);
                if (event.getAction()==MotionEvent.ACTION_UP){
//                当手离开屏幕是，清屏
                }else {
//                先在屏幕上画一个十字，横向贯穿屏幕，纵向贯穿屏幕
                    for(int i=0;i<pointerCount;i++){
//                    获取一个触点的坐标，然后开始绘制
                        int id=event.getPointerId(i);
                        int x=(int) event.getX(i);
                        int y=(int) event.getY(i);
                        drawCrossHairsAndText(x,y,touchPaints[id],i,id,c);
                    }
//                使用不同的颜色在每个手指的位置画圆
                    for (int i = 0; i < pointerCount; i++) {
                        int id=event.getPointerId(i);
                        int x=(int) event.getX(i);
                        int y=(int) event.getY(i);
                        drawCircle(x, y, touchPaints[id], c);
                    }
                }
//            画完后，解锁显示
                getHolder().unlockCanvasAndPost(c);
            }
            return true;
        }
        /**
         * 画十字交叉线及坐标信息
         * @param x:线的x坐标
         * @param y:线的y坐标
         * @param paint:线的颜色
         * @param ptr:第几个点
         * @param id:id值
         * @param c:画布
         */
        private void drawCrossHairsAndText(int x, int y, Paint paint,
                                           int ptr, int id, Canvas c) {
//        在（0，y）和（width ，y）这两个点上画直线
            c.drawLine(0,y,width,y,paint);
//        在（x，0）和（x，height）这两个点上画直线
            c.drawLine(x,0,x,height,paint);
//        定义文字的大小
            int textY=(int)((20+25*ptr)*scale);
//        画出x的值
            c.drawText("x"+ptr+"="+x,10*scale,textY,textPaint);
//        画出Y的值
            c.drawText("y"+ptr+"="+y,90*scale,textY,textPaint);
//        画出id的值
            c.drawText("id"+ptr+"="+id,width-55*scale,textY,textPaint);
        }


        /**
         * 画手指单击的实心圆
         * @param x:实心圆的x值
         * @param y:实心圆的y值
         * @param paint:实心圆的画笔
         * @param c:在这个画布上画
         */
        private void drawCircle(int x,int y,Paint paint,Canvas c){
//     在canvas上画圆
            c.drawCircle(x,y,20*scale,paint);
        }




        //在创建时激发，一般在这里调用画图的线程。
        @Override
        public void surfaceCreated(SurfaceHolder holder) {


        }


        //在surface的大小发生改变时激发
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {


//        得到屏幕的宽度
            this.width=width;
//        得到屏幕的高度
            this.height=height;
//        得到屏幕的放大比例
            if (width>height){
                this.scale=width/480f;
                this.scale=height/480f;
            }
//        通过放大比例计算出字体大小
            textPaint.setTextSize(20*scale);
//        得到当前View的holder对象
            Canvas c=getHolder().lockCanvas();
//        设置背景为黑色
            if (c!=null){
//            背景黑色
                c.drawColor(Color.BLACK);
//            在屏幕中间画上提示语
                float tWidth=textPaint.measureText(START_TEXT);
                c.drawText(START_TEXT,width/2-tWidth/2,height/2,textPaint);
//            解锁显示
                getHolder().unlockCanvasAndPost(c);
            }
        }
        //销毁时激发，一般在这里将画图的线程停止、释放。
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {


        }
    }


