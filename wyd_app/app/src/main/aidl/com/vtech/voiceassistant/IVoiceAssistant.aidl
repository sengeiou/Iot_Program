// IVoiceAssistant.aidlrespire
package com.vtech.voiceassistant;

// Declare any non-default types here with import statements

interface IVoiceAssistant {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
//    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString);

    void play(String aString);
    int  bpressure();
    int  distance();
    int  respire();
    void start_hwatch();
    void stop_hwatch();
    double get_latitude();
    double get_longitude();
    String get_addr();
    String get_deviceid();
    String get_username();
    String getHealthData();

    /*注册人脸 */
    void reg_face(String name,String group);

    /*人脸认证： 阻塞5秒从注册的人脸中获取匹配的名称
      返回"" 则为匹配失败，否则返回匹配的用户名成
    */
    String auth_face();
    }
