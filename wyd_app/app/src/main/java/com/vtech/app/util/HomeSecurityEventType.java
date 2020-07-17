package com.vtech.app.util;

public class HomeSecurityEventType {
    // 探测器
    //public static final int Probe = "Probe";
    public static final int OuterHornsLearn = 100;// 学习喇叭
    public static final int OuterHornsInt = 101;// 外置喇叭
    public static final int RemoteControlInt = 102;// 遥控器
    public static final int MagneticInt = 103;// 门磁探测器
    public static final int InfraredInt = 104;// 红外人体探测器
    public static final int UrgentBtnInt = 105;// 紧急按钮
    public static final int SmokeInt = 106; // 烟雾探测器
    public static final int GasInt = 107; // 燃气探测器
    public static final int InfraredRayInt = 108; // 红外对射探测器 ("事件名--停留时间已达5秒")
    public static final int TemperatureInt = 109; //温度传感器
    public static final int Joint_protection = 200; //联动布防
}
