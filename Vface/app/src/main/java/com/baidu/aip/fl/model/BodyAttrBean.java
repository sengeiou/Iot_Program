package com.baidu.aip.fl.model;

import java.util.List;

public class BodyAttrBean {
    /**
     * person_num : 1
     * person_info : [{"attributes":{"upper_wear_fg":{"score":0.9982017278671265,"name":"衬衫"},"cellphone":{"score":0.4751138389110565,"name":"不确定"},"lower_cut":{"score":0.6139751076698303,"name":"有下方截断"},"umbrella":{"score":0.9996742606163025,"name":"未打伞"},"orientation":{"score":0.9983522891998291,"name":"正面"},"is_human":{"score":0.9506919384002686,"name":"正常人体"},"headwear":{"score":0.9850633144378662,"name":"无帽"},"gender":{"score":0.8867764472961426,"name":"男性"},"age":{"score":0.8826483488082886,"name":"青年"},"upper_cut":{"score":0.9527388215065002,"name":"无上方截断"},"glasses":{"score":0.5668045878410339,"name":"无眼镜"},"lower_color":{"score":0.5017809271812439,"name":"不确定"},"bag":{"score":0.9698227643966675,"name":"无背包"},"upper_wear_texture":{"score":0.9178072810173035,"name":"纯色"},"smoke":{"score":0.6747806072235107,"name":"未吸烟"},"vehicle":{"score":0.9986880421638489,"name":"无交通工具"},"lower_wear":{"score":0.8652642369270325,"name":"不确定"},"carrying_item":{"score":0.5255060791969299,"name":"不确定"},"upper_wear":{"score":0.565967321395874,"name":"长袖"},"occlusion":{"score":0.6093065738677979,"name":"无遮挡"},"upper_color":{"score":0.9973198771476746,"name":"白"}},"location":{"height":397,"width":122,"top":119,"score":0.9942339062690735,"left":195}}]
     * log_id : 5695918095127344594
     */

    private int person_num;
    private long log_id;
    private List<PersonInfoBean> person_info;

    public int getPerson_num() {
        return person_num;
    }

    public void setPerson_num(int person_num) {
        this.person_num = person_num;
    }

    public long getLog_id() {
        return log_id;
    }

    public void setLog_id(long log_id) {
        this.log_id = log_id;
    }

    public List<PersonInfoBean> getPerson_info() {
        return person_info;
    }

    public void setPerson_info(List<PersonInfoBean> person_info) {
        this.person_info = person_info;
    }

    public static class PersonInfoBean {
        /**
         * attributes : {"upper_wear_fg":{"score":0.9982017278671265,"name":"衬衫"},"cellphone":{"score":0.4751138389110565,"name":"不确定"},"lower_cut":{"score":0.6139751076698303,"name":"有下方截断"},"umbrella":{"score":0.9996742606163025,"name":"未打伞"},"orientation":{"score":0.9983522891998291,"name":"正面"},"is_human":{"score":0.9506919384002686,"name":"正常人体"},"headwear":{"score":0.9850633144378662,"name":"无帽"},"gender":{"score":0.8867764472961426,"name":"男性"},"age":{"score":0.8826483488082886,"name":"青年"},"upper_cut":{"score":0.9527388215065002,"name":"无上方截断"},"glasses":{"score":0.5668045878410339,"name":"无眼镜"},"lower_color":{"score":0.5017809271812439,"name":"不确定"},"bag":{"score":0.9698227643966675,"name":"无背包"},"upper_wear_texture":{"score":0.9178072810173035,"name":"纯色"},"smoke":{"score":0.6747806072235107,"name":"未吸烟"},"vehicle":{"score":0.9986880421638489,"name":"无交通工具"},"lower_wear":{"score":0.8652642369270325,"name":"不确定"},"carrying_item":{"score":0.5255060791969299,"name":"不确定"},"upper_wear":{"score":0.565967321395874,"name":"长袖"},"occlusion":{"score":0.6093065738677979,"name":"无遮挡"},"upper_color":{"score":0.9973198771476746,"name":"白"}}
         * location : {"height":397,"width":122,"top":119,"score":0.9942339062690735,"left":195}
         */

        private AttributesBean attributes;
        private LocationBean location;

        public AttributesBean getAttributes() {
            return attributes;
        }

        public void setAttributes(AttributesBean attributes) {
            this.attributes = attributes;
        }

        public LocationBean getLocation() {
            return location;
        }

        public void setLocation(LocationBean location) {
            this.location = location;
        }

        public static class AttributesBean {
            /**
             * upper_wear_fg : {"score":0.9982017278671265,"name":"衬衫"}
             * cellphone : {"score":0.4751138389110565,"name":"不确定"}
             * lower_cut : {"score":0.6139751076698303,"name":"有下方截断"}
             * umbrella : {"score":0.9996742606163025,"name":"未打伞"}
             * orientation : {"score":0.9983522891998291,"name":"正面"}
             * is_human : {"score":0.9506919384002686,"name":"正常人体"}
             * headwear : {"score":0.9850633144378662,"name":"无帽"}
             * gender : {"score":0.8867764472961426,"name":"男性"}
             * age : {"score":0.8826483488082886,"name":"青年"}
             * upper_cut : {"score":0.9527388215065002,"name":"无上方截断"}
             * glasses : {"score":0.5668045878410339,"name":"无眼镜"}
             * lower_color : {"score":0.5017809271812439,"name":"不确定"}
             * bag : {"score":0.9698227643966675,"name":"无背包"}
             * upper_wear_texture : {"score":0.9178072810173035,"name":"纯色"}
             * smoke : {"score":0.6747806072235107,"name":"未吸烟"}
             * vehicle : {"score":0.9986880421638489,"name":"无交通工具"}
             * lower_wear : {"score":0.8652642369270325,"name":"不确定"}
             * carrying_item : {"score":0.5255060791969299,"name":"不确定"}
             * upper_wear : {"score":0.565967321395874,"name":"长袖"}
             * occlusion : {"score":0.6093065738677979,"name":"无遮挡"}
             * upper_color : {"score":0.9973198771476746,"name":"白"}
             */

            private UpperWearFgBean upper_wear_fg;
            private CellphoneBean cellphone;
            private LowerCutBean lower_cut;
            private UmbrellaBean umbrella;
            private OrientationBean orientation;
            private IsHumanBean is_human;
            private HeadwearBean headwear;
            private GenderBean gender;
            private AgeBean age;
            private UpperCutBean upper_cut;
            private GlassesBean glasses;
            private LowerColorBean lower_color;
            private BagBean bag;
            private UpperWearTextureBean upper_wear_texture;
            private SmokeBean smoke;
            private VehicleBean vehicle;
            private LowerWearBean lower_wear;
            private CarryingItemBean carrying_item;
            private UpperWearBean upper_wear;
            private OcclusionBean occlusion;
            private UpperColorBean upper_color;

            public UpperWearFgBean getUpper_wear_fg() {
                return upper_wear_fg;
            }

            public void setUpper_wear_fg(UpperWearFgBean upper_wear_fg) {
                this.upper_wear_fg = upper_wear_fg;
            }

            public CellphoneBean getCellphone() {
                return cellphone;
            }

            public void setCellphone(CellphoneBean cellphone) {
                this.cellphone = cellphone;
            }

            public LowerCutBean getLower_cut() {
                return lower_cut;
            }

            public void setLower_cut(LowerCutBean lower_cut) {
                this.lower_cut = lower_cut;
            }

            public UmbrellaBean getUmbrella() {
                return umbrella;
            }

            public void setUmbrella(UmbrellaBean umbrella) {
                this.umbrella = umbrella;
            }

            public OrientationBean getOrientation() {
                return orientation;
            }

            public void setOrientation(OrientationBean orientation) {
                this.orientation = orientation;
            }

            public IsHumanBean getIs_human() {
                return is_human;
            }

            public void setIs_human(IsHumanBean is_human) {
                this.is_human = is_human;
            }

            public HeadwearBean getHeadwear() {
                return headwear;
            }

            public void setHeadwear(HeadwearBean headwear) {
                this.headwear = headwear;
            }

            public GenderBean getGender() {
                return gender;
            }

            public void setGender(GenderBean gender) {
                this.gender = gender;
            }

            public AgeBean getAge() {
                return age;
            }

            public void setAge(AgeBean age) {
                this.age = age;
            }

            public UpperCutBean getUpper_cut() {
                return upper_cut;
            }

            public void setUpper_cut(UpperCutBean upper_cut) {
                this.upper_cut = upper_cut;
            }

            public GlassesBean getGlasses() {
                return glasses;
            }

            public void setGlasses(GlassesBean glasses) {
                this.glasses = glasses;
            }

            public LowerColorBean getLower_color() {
                return lower_color;
            }

            public void setLower_color(LowerColorBean lower_color) {
                this.lower_color = lower_color;
            }

            public BagBean getBag() {
                return bag;
            }

            public void setBag(BagBean bag) {
                this.bag = bag;
            }

            public UpperWearTextureBean getUpper_wear_texture() {
                return upper_wear_texture;
            }

            public void setUpper_wear_texture(UpperWearTextureBean upper_wear_texture) {
                this.upper_wear_texture = upper_wear_texture;
            }

            public SmokeBean getSmoke() {
                return smoke;
            }

            public void setSmoke(SmokeBean smoke) {
                this.smoke = smoke;
            }

            public VehicleBean getVehicle() {
                return vehicle;
            }

            public void setVehicle(VehicleBean vehicle) {
                this.vehicle = vehicle;
            }

            public LowerWearBean getLower_wear() {
                return lower_wear;
            }

            public void setLower_wear(LowerWearBean lower_wear) {
                this.lower_wear = lower_wear;
            }

            public CarryingItemBean getCarrying_item() {
                return carrying_item;
            }

            public void setCarrying_item(CarryingItemBean carrying_item) {
                this.carrying_item = carrying_item;
            }

            public UpperWearBean getUpper_wear() {
                return upper_wear;
            }

            public void setUpper_wear(UpperWearBean upper_wear) {
                this.upper_wear = upper_wear;
            }

            public OcclusionBean getOcclusion() {
                return occlusion;
            }

            public void setOcclusion(OcclusionBean occlusion) {
                this.occlusion = occlusion;
            }

            public UpperColorBean getUpper_color() {
                return upper_color;
            }

            public void setUpper_color(UpperColorBean upper_color) {
                this.upper_color = upper_color;
            }

            public static class UpperWearFgBean {
                /**
                 * score : 0.9982017278671265
                 * name : 衬衫
                 */

                private double score;
                private String name;

                public double getScore() {
                    return score;
                }

                public void setScore(double score) {
                    this.score = score;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }

            public static class CellphoneBean {
                /**
                 * score : 0.4751138389110565
                 * name : 不确定
                 */

                private double score;
                private String name;

                public double getScore() {
                    return score;
                }

                public void setScore(double score) {
                    this.score = score;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }

            public static class LowerCutBean {
                /**
                 * score : 0.6139751076698303
                 * name : 有下方截断
                 */

                private double score;
                private String name;

                public double getScore() {
                    return score;
                }

                public void setScore(double score) {
                    this.score = score;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }

            public static class UmbrellaBean {
                /**
                 * score : 0.9996742606163025
                 * name : 未打伞
                 */

                private double score;
                private String name;

                public double getScore() {
                    return score;
                }

                public void setScore(double score) {
                    this.score = score;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }

            public static class OrientationBean {
                /**
                 * score : 0.9983522891998291
                 * name : 正面
                 */

                private double score;
                private String name;

                public double getScore() {
                    return score;
                }

                public void setScore(double score) {
                    this.score = score;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }

            public static class IsHumanBean {
                /**
                 * score : 0.9506919384002686
                 * name : 正常人体
                 */

                private double score;
                private String name;

                public double getScore() {
                    return score;
                }

                public void setScore(double score) {
                    this.score = score;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }

            public static class HeadwearBean {
                /**
                 * score : 0.9850633144378662
                 * name : 无帽
                 */

                private double score;
                private String name;

                public double getScore() {
                    return score;
                }

                public void setScore(double score) {
                    this.score = score;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }

            public static class GenderBean {
                /**
                 * score : 0.8867764472961426
                 * name : 男性
                 */

                private double score;
                private String name;

                public double getScore() {
                    return score;
                }

                public void setScore(double score) {
                    this.score = score;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }

            public static class AgeBean {
                /**
                 * score : 0.8826483488082886
                 * name : 青年
                 */

                private double score;
                private String name;

                public double getScore() {
                    return score;
                }

                public void setScore(double score) {
                    this.score = score;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }

            public static class UpperCutBean {
                /**
                 * score : 0.9527388215065002
                 * name : 无上方截断
                 */

                private double score;
                private String name;

                public double getScore() {
                    return score;
                }

                public void setScore(double score) {
                    this.score = score;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }

            public static class GlassesBean {
                /**
                 * score : 0.5668045878410339
                 * name : 无眼镜
                 */

                private double score;
                private String name;

                public double getScore() {
                    return score;
                }

                public void setScore(double score) {
                    this.score = score;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }

            public static class LowerColorBean {
                /**
                 * score : 0.5017809271812439
                 * name : 不确定
                 */

                private double score;
                private String name;

                public double getScore() {
                    return score;
                }

                public void setScore(double score) {
                    this.score = score;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }

            public static class BagBean {
                /**
                 * score : 0.9698227643966675
                 * name : 无背包
                 */

                private double score;
                private String name;

                public double getScore() {
                    return score;
                }

                public void setScore(double score) {
                    this.score = score;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }

            public static class UpperWearTextureBean {
                /**
                 * score : 0.9178072810173035
                 * name : 纯色
                 */

                private double score;
                private String name;

                public double getScore() {
                    return score;
                }

                public void setScore(double score) {
                    this.score = score;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }

            public static class SmokeBean {
                /**
                 * score : 0.6747806072235107
                 * name : 未吸烟
                 */

                private double score;
                private String name;

                public double getScore() {
                    return score;
                }

                public void setScore(double score) {
                    this.score = score;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }

            public static class VehicleBean {
                /**
                 * score : 0.9986880421638489
                 * name : 无交通工具
                 */

                private double score;
                private String name;

                public double getScore() {
                    return score;
                }

                public void setScore(double score) {
                    this.score = score;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }

            public static class LowerWearBean {
                /**
                 * score : 0.8652642369270325
                 * name : 不确定
                 */

                private double score;
                private String name;

                public double getScore() {
                    return score;
                }

                public void setScore(double score) {
                    this.score = score;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }

            public static class CarryingItemBean {
                /**
                 * score : 0.5255060791969299
                 * name : 不确定
                 */

                private double score;
                private String name;

                public double getScore() {
                    return score;
                }

                public void setScore(double score) {
                    this.score = score;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }

            public static class UpperWearBean {
                /**
                 * score : 0.565967321395874
                 * name : 长袖
                 */

                private double score;
                private String name;

                public double getScore() {
                    return score;
                }

                public void setScore(double score) {
                    this.score = score;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }

            public static class OcclusionBean {
                /**
                 * score : 0.6093065738677979
                 * name : 无遮挡
                 */

                private double score;
                private String name;

                public double getScore() {
                    return score;
                }

                public void setScore(double score) {
                    this.score = score;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }

            public static class UpperColorBean {
                /**
                 * score : 0.9973198771476746
                 * name : 白
                 */

                private double score;
                private String name;

                public double getScore() {
                    return score;
                }

                public void setScore(double score) {
                    this.score = score;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }
        }

        public static class LocationBean {
            /**
             * height : 397
             * width : 122
             * top : 119
             * score : 0.9942339062690735
             * left : 195
             */

            private int height;
            private int width;
            private int top;
            private double score;
            private int left;

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public int getTop() {
                return top;
            }

            public void setTop(int top) {
                this.top = top;
            }

            public double getScore() {
                return score;
            }

            public void setScore(double score) {
                this.score = score;
            }

            public int getLeft() {
                return left;
            }

            public void setLeft(int left) {
                this.left = left;
            }
        }
    }
}
