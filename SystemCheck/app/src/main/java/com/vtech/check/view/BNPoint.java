package com.vtech.check.view;

import android.graphics.Canvas;
import android.graphics.Paint;

public class BNPoint {

    float x;//触控点x坐标
    float y;//触控点y坐标
    int id;//触控点id
    int[] color;//触控点的绘制颜色


    public BNPoint(float x,float y,int[] color,int id){
        this.x = x;
        this.y = y;
        this.color = color;
        this.id = id;
    }








}
