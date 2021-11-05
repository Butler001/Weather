package com.example.weather_butler.bean;

import java.util.List;

/**
 * 解析出来的城市的实体类
 */
public class CityResponse {

    private String name;
    private List<CityBean> city;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CityBean> getCity() {
        return city;
    }

    public void setCity(List<CityBean> city) {
        this.city = city;
    }

    public static class CityBean {

        private String name;
        private List<AreaBean> area;


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public static class AreaBean {
            private String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
