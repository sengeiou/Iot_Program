package com.vtech.app.data;

public interface Constant {
    String URL_AMAP_WEATHER = "https://restapi.amap.com/v3/weather/weatherInfo";

    String AMAP_KEY = "cc8a752b44789c1ee9c1ae557a70199d";

    String SUCCESS = "1";

    String URL_AMAP_REGEO = "https://restapi.amap.com/v3/geocode/regeo";

    String URL_VTECH_WEATHER = " http://47.112.217.188:8999/spo/weather/findweatherbyname.do";

    String URL_BAIDU_MAP = "http://api.map.baidu.com/geocoder";

    //撤防
    public final static int DEPLOY_CMD_VALUE_REMOVE = 0;
    //在家布防
    public final static int DEPLOY_CMD_VALUE_HOME = 1;
    //离家布防
    public final static int DEPLOY_CMD_VALUE_OUTER = 2;

	//在家布防失败
    public final static int DEPLOY_CMD_VALUE_HOME_FAIL = 4;
	//离家布防失败
    public final static int DEPLOY_CMD_VALUE_OUTER_FAIL = 5;
    //撤防失败
    public final static int DEPLOY_CMD_VALUE_REMOVE_FAIL = 6;
}
