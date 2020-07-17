package com.vtech.app.data.bean;

import java.util.List;

public class RegeoInfo {

    /**
     * status : 1
     * regeocode : {"addressComponent":{"city":"深圳市","province":"广东省","adcode":"440305","district":"南山区","towncode":"440305007000","streetNumber":{"number":"5号","location":"113.954764,22.535235","direction":"西南","distance":"40.3591","street":"科技南十二路"},"country":"中国","township":"粤海街道","businessAreas":[{"location":"113.943834,22.545057","name":"科技园","id":"440305"},{"location":"113.954529,22.544106","name":"大冲","id":"440305"}],"building":{"name":[],"type":[]},"neighborhood":{"name":[],"type":[]},"citycode":"0755"},"formatted_address":"广东省深圳市南山区粤海街道伟易达研发大楼"}
     * info : OK
     * infocode : 10000
     */
    private String status;
    private RegeocodeBean regeocode;
    private String info;
    private String infocode;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public RegeocodeBean getRegeocode() {
        return regeocode;
    }

    public void setRegeocode(RegeocodeBean regeocode) {
        this.regeocode = regeocode;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfocode() {
        return infocode;
    }

    public void setInfocode(String infocode) {
        this.infocode = infocode;
    }

    public static class RegeocodeBean {
        /**
         * addressComponent : {"city":"深圳市","province":"广东省","adcode":"440305","district":"南山区","towncode":"440305007000","streetNumber":{"number":"5号","location":"113.954764,22.535235","direction":"西南","distance":"40.3591","street":"科技南十二路"},"country":"中国","township":"粤海街道","businessAreas":[{"location":"113.943834,22.545057","name":"科技园","id":"440305"},{"location":"113.954529,22.544106","name":"大冲","id":"440305"}],"building":{"name":[],"type":[]},"neighborhood":{"name":[],"type":[]},"citycode":"0755"}
         * formatted_address : 广东省深圳市南山区粤海街道伟易达研发大楼
         */
        private AddressComponentBean addressComponent;
        private String formatted_address;

        public AddressComponentBean getAddressComponent() {
            return addressComponent;
        }

        public void setAddressComponent(AddressComponentBean addressComponent) {
            this.addressComponent = addressComponent;
        }

        public String getFormatted_address() {
            return formatted_address;
        }

        public void setFormatted_address(String formatted_address) {
            this.formatted_address = formatted_address;
        }

        public static class AddressComponentBean {
            /**
             * city : 深圳市
             * province : 广东省
             * adcode : 440305
             * district : 南山区
             * towncode : 440305007000
             * streetNumber : {"number":"5号","location":"113.954764,22.535235","direction":"西南","distance":"40.3591","street":"科技南十二路"}
             * country : 中国
             * township : 粤海街道
             * businessAreas : [{"location":"113.943834,22.545057","name":"科技园","id":"440305"},{"location":"113.954529,22.544106","name":"大冲","id":"440305"}]
             * building : {"name":[],"type":[]}
             * neighborhood : {"name":[],"type":[]}
             * citycode : 0755
             */

            private String city;
            private String province;
            private String adcode;
            private String district;
            private String towncode;
            private StreetNumberBean streetNumber;
            private String country;
            private String township;
            private List<BusinessAreasBean> businessAreas;
            //private BuildingBean building;
            private NeighborhoodBean neighborhood;
            private String citycode;


            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getProvince() {
                return province;
            }

            public void setProvince(String province) {
                this.province = province;
            }

            public String getAdcode() {
                return adcode;
            }

            public void setAdcode(String adcode) {
                this.adcode = adcode;
            }

            public String getDistrict() {
                return district;
            }

            public void setDistrict(String district) {
                this.district = district;
            }

            public String getTowncode() {
                return towncode;
            }

            public void setTowncode(String towncode) {
                this.towncode = towncode;
            }

            public StreetNumberBean getStreetNumber() {
                return streetNumber;
            }

            public void setStreetNumber(StreetNumberBean streetNumber) {
                this.streetNumber = streetNumber;
            }

            public String getCountry() {
                return country;
            }

            public void setCountry(String country) {
                this.country = country;
            }

            public String getTownship() {
                return township;
            }

            public void setTownship(String township) {
                this.township = township;
            }

            //public BuildingBean getBuilding() {
            //    return building;
            //}

            //public void setBuilding(BuildingBean building) {
            //    this.building = building;
            //}

            public NeighborhoodBean getNeighborhood() {
                return neighborhood;
            }

            public void setNeighborhood(NeighborhoodBean neighborhood) {
                this.neighborhood = neighborhood;
            }

            public String getCitycode() {
                return citycode;
            }

            public void setCitycode(String citycode) {
                this.citycode = citycode;
            }

            public List<BusinessAreasBean> getBusinessAreas() {
                return businessAreas;
            }

            public void setBusinessAreas(List<BusinessAreasBean> businessAreas) {
                this.businessAreas = businessAreas;
            }

            public static class StreetNumberBean {
                /**
                 * number : 5号
                 * location : 113.954764,22.535235
                 * direction : 西南
                 * distance : 40.3591
                 * street : 科技南十二路
                 */

                private String number;
                private String location;
                private String direction;
                private String distance;
                private String street;

                public String getNumber() {
                    return number;
                }

                public void setNumber(String number) {
                    this.number = number;
                }

                public String getLocation() {
                    return location;
                }

                public void setLocation(String location) {
                    this.location = location;
                }

                public String getDirection() {
                    return direction;
                }

                public void setDirection(String direction) {
                    this.direction = direction;
                }

                public String getDistance() {
                    return distance;
                }

                public void setDistance(String distance) {
                    this.distance = distance;
                }

                public String getStreet() {
                    return street;
                }

                public void setStreet(String street) {
                    this.street = street;
                }
            }

            public static class BuildingBean {
                private List<?> name;
                private List<?> type;

                public List<?> getName() {
                    return name;
                }

                public void setName(List<?> name) {
                    this.name = name;
                }

                public List<?> getType() {
                    return type;
                }

                public void setType(List<?> type) {
                    this.type = type;
                }
            }

            public static class NeighborhoodBean {
                private List<?> name;
                private List<?> type;

                public List<?> getName() {
                    return name;
                }

                public void setName(List<?> name) {
                    this.name = name;
                }

                public List<?> getType() {
                    return type;
                }

                public void setType(List<?> type) {
                    this.type = type;
                }
            }

            public final class BusinessAreasBean {
                /**
                 * location : 113.943834,22.545057
                 * name : 科技园
                 * id : 440305
                 */

                private String location;
                private String name;
                private String id;

                public String getLocation() {
                    return location;
                }

                public void setLocation(String location) {
                    this.location = location;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }
            }
        }
    }
}
