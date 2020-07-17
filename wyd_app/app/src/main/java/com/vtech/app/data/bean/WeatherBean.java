package com.vtech.app.data.bean;

public class WeatherBean {

    /**
     * code : 0
     * msg : 成功
     * data : {"tid":6,"cityid":"101030100","city":"天津市","parent":"天津","weathertime":"08:50","weatherdate":"20190920","fl":"<3级","fx":"西南风","shidu":"66%","type":"阴","high":"高温 26℃","low":"低温 17℃","pm25":"35.0","pm10":"80.0","quality":"良","createtime":"1568945526","updatetime":"1568945526","createby":"admin","updateby":"admin"}
     */

    private int code;
    private String msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public final class DataBean {
        /**
         * tid : 6
         * cityid : 101030100
         * city : 天津市
         * parent : 天津
         * weathertime : 08:50
         * weatherdate : 20190920
         * fl : <3级
         * fx : 西南风
         * shidu : 66%
         * type : 阴
         * high : 高温 26℃
         * low : 低温 17℃
         * pm25 : 35.0
         * pm10 : 80.0
         * quality : 良
         * createtime : 1568945526
         * updatetime : 1568945526
         * createby : admin
         * updateby : admin
         */

        private int tid;
        private String cityid;
        private String city;
        private String parent;
        private String weathertime;
        private String weatherdate;
        private String fl;
        private String fx;
        private String shidu;
        private String wendu;
        private String type;
        private String high;
        private String low;
        private String pm25;
        private String pm10;
        private String quality;
        private String createtime;
        private String updatetime;
        private String createby;
        private String updateby;

        public int getTid() {
            return tid;
        }

        public void setTid(int tid) {
            this.tid = tid;
        }

        public String getCityid() {
            return cityid;
        }

        public void setCityid(String cityid) {
            this.cityid = cityid;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getParent() {
            return parent;
        }

        public void setParent(String parent) {
            this.parent = parent;
        }

        public String getWeathertime() {
            return weathertime;
        }

        public void setWeathertime(String weathertime) {
            this.weathertime = weathertime;
        }

        public String getWeatherdate() {
            return weatherdate;
        }

        public void setWeatherdate(String weatherdate) {
            this.weatherdate = weatherdate;
        }

        public String getFl() {
            return fl;
        }

        public void setFl(String fl) {
            this.fl = fl;
        }

        public String getFx() {
            return fx;
        }

        public void setFx(String fx) {
            this.fx = fx;
        }

        public String getShidu() {
            return shidu;
        }

        public void setShidu(String shidu) {
            this.shidu = shidu;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getHigh() {
            return high;
        }

        public void setHigh(String high) {
            this.high = high;
        }

        public String getLow() {
            return low;
        }

        public void setLow(String low) {
            this.low = low;
        }

        public String getWendu() {
            return wendu;
        }

        public void setWendu(String wendu) {
            this.wendu = wendu;
        }

        public String getPm25() {
            return pm25;
        }

        public void setPm25(String pm25) {
            this.pm25 = pm25;
        }

        public String getPm10() {
            return pm10;
        }

        public void setPm10(String pm10) {
            this.pm10 = pm10;
        }

        public String getQuality() {
            return quality;
        }

        public void setQuality(String quality) {
            this.quality = quality;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public String getUpdatetime() {
            return updatetime;
        }

        public void setUpdatetime(String updatetime) {
            this.updatetime = updatetime;
        }

        public String getCreateby() {
            return createby;
        }

        public void setCreateby(String createby) {
            this.createby = createby;
        }

        public String getUpdateby() {
            return updateby;
        }

        public void setUpdateby(String updateby) {
            this.updateby = updateby;
        }
    }
}
