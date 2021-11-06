package com.example.weather_butler;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.mvplibrary.mvp.MvpActivity;
import com.example.mvplibrary.utils.CityWindow;
import com.example.mvplibrary.utils.ObjectUtils;
import com.example.mvplibrary.utils.RecycleViewAnimation;
import com.example.mvplibrary.view.WhiteWindmills;
import com.example.weather_butler.adapter.AreaAdapter;
import com.example.weather_butler.adapter.CityAdapter;
import com.example.weather_butler.adapter.ProvinceAdapter;
import com.example.weather_butler.adapter.WeatherForecastAdapter;
import com.example.weather_butler.api.ApiService;
import com.example.weather_butler.bean.CityResponse;
import com.example.weather_butler.bean.LifeSuggestion;
import com.example.weather_butler.bean.LocationInfo;
import com.example.weather_butler.bean.TodayResponse;
import com.example.weather_butler.bean.WeatherForcastResponse;
import com.example.weather_butler.contract.WeatherContract;
import com.example.weather_butler.utils.StatusBarUtil;
import com.example.weather_butler.utils.ToastUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends MvpActivity<WeatherContract.WeatherPresenter> implements WeatherContract.WeatherPresenter.IWeatherView {
    @BindView(R.id.area_tv)
    TextView areaTv;
    @BindView(R.id.temperature_current)
    TextView temperatureCurrent;
    @BindView(R.id.temperature_max)
    TextView temperatureMax;
    @BindView(R.id.temperature_min)
    TextView temperatureMin;
    @BindView(R.id.temperature_time_update)
    TextView temperatureTimeUpdate;
    @BindView(R.id.temperature_condition)
    TextView temperatureCondition;
    @BindView(R.id.temp_forecast)
    RecyclerView tempForecast;
    @BindView(R.id.tv_comf)
    TextView tvComf;
    @BindView(R.id.tv_trav)
    TextView tvTrav;
    @BindView(R.id.tv_sport)
    TextView tvSport;
    @BindView(R.id.tv_cw)
    TextView tvCw;
    @BindView(R.id.tv_air)
    TextView tvAir;
    @BindView(R.id.tv_drsg)
    TextView tvDrsg;
    @BindView(R.id.tv_flu)
    TextView tvFlu;
    @BindView(R.id.tv_fis)
    TextView tvFis;
    @BindView(R.id.tv_uv)
    TextView tvUv;
    @BindView(R.id.tv_ptfc)
    TextView tvPtfc;
    @BindView(R.id.wm_big)
    WhiteWindmills wmBig;
    @BindView(R.id.wm_small)
    WhiteWindmills wmSmall;
    @BindView(R.id.wind_direction)
    TextView windDirection;
    @BindView(R.id.wind_power)
    TextView windPower;
    @BindView(R.id.iv_city_select)
    ImageView ivCitySelect;


    private RxPermissions rxPermissions;
    //TAG
    public static final String TAG = "LQL";
    //定位器
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();
    //get locationId
    private ApiService apiService_location;
    //get resource for weather
    List<WeatherForcastResponse.Daily> list_forecast_weather;
    //get adapter
    WeatherForecastAdapter adapter_forecast;

    /**
     * 配置城市列表
     */
    private List<String> list;
    private List<CityResponse> provinceList;
    private List<CityResponse.CityBean> cityList;
    private List<CityResponse.CityBean.AreaBean> areaList;
    ProvinceAdapter provinceAdapter;
    CityAdapter cityAdapter;
    AreaAdapter areaAdapter;
    private String provinceTitle;
    CityWindow cityWindow;

    //permissions check
    private void permissionVersion() {
        if (Build.VERSION.SDK_INT >= 23) {
            //动态权限申请
            permissionsRequest();
        } else {
            ToastUtils.showShortToast(this, "系统版本在6.0以下，无需申请");
        }
    }

    //动态权限申请
    private void permissionsRequest() {
        rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(granted -> {
                    if (granted) {
                        //得到权限之后开始定位
                        startLocation();
                    } else {
                        ToastUtils.showShortToast(this, "权限未开启");
                    }
                });
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(granted -> {
            if (granted) {
                Log.d(TAG, "permissionsRequest: get permission");
            }
        });
    }

    //定位方法
    private void startLocation() {
        mLocationClient = new LocationClient(this);
        //注册监听函数
        mLocationClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();

        option.setIsNeedAddress(true);
        option.setNeedNewVersionRgc(true);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    //绑定presenter
    @Override
    protected WeatherContract.WeatherPresenter createPresenter() {
        return new WeatherContract.WeatherPresenter();
    }

    //数据初始化 主线程
    @Override
    public void initData(Bundle savedInstanceState) {
        StatusBarUtil.setStatusBarColor(context, R.color.snow_green);
        initWeatherList();
        rxPermissions = new RxPermissions(this);
        permissionVersion();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    /**
     * 给各个视图绑定点击事件
     */
//    public void onViewClicked(View view){
//        switch (view.getId()){
//            case R.id.city:
//                showCityWindow();
//                break;
//            default:
//                break;
//        }
//    }
    @OnClick(R.id.iv_city_select)
    public void onViewClicked() {
        showCityWindow();
    }

    /**
     * 显示城市弹窗
     */
    private void showCityWindow() {
        provinceList = new ArrayList<>();
        cityList = new ArrayList<>();
        areaList = new ArrayList<>();
        list = new ArrayList<>();
        cityWindow = new CityWindow(context);
        final View view = LayoutInflater.from(context).inflate(R.layout.window_city_list, null);
        ImageView cityBack = view.findViewById(R.id.iv_back_city);
        ImageView areaBack = view.findViewById(R.id.iv_back_direct);
        TextView windowTitle = view.findViewById(R.id.window_title);
        RecyclerView rv_city = view.findViewById(R.id.rv_city_list);
        cityWindow.showRightPopupWindow(view);
        initCityData(rv_city, cityBack, areaBack, windowTitle);
    }


    /**
     * 返回今天的天气数据
     *
     * @param response
     */
    @Override
    public void getTodayWeatherResult(retrofit2.Response<TodayResponse> response) {
        //定位数据结束之后，关闭定位
        mLocationClient.stop();
        if (response.body() != null) {
            //布局数据
            temperatureCurrent.setText(response.body().getNow().getTemp());
            temperatureCondition.setText(response.body().getNow().getText());
            temperatureTimeUpdate.setText("更新时间: " + response.body().getUpdateTime().replace("+08:00", "").replace("T", " "));

            windDirection.setText("风向   " + response.body().getNow().getWindDir());
            windPower.setText("风力    " + response.body().getNow().getWindScale() + "级");
            wmBig.start();
            wmSmall.start();
        } else {
            ToastUtils.showShortToast(context, response.body().getCode());
        }

    }

    /**
     * 获得天气预测
     *
     * @param response
     */
    @Override
    public void getWeatherForecast(retrofit2.Response<WeatherForcastResponse> response) {
        if (response.body().getCode().equals("200")) {
            temperatureMax.setText(response.body().getDaily().get(0).getTempMax());
            temperatureMin.setText(response.body().getDaily().get(0).getTempMin());

            if (response.body().getDaily() != null) {
                List<WeatherForcastResponse.Daily> list = response.body().getDaily();
                list_forecast_weather.clear();
                list_forecast_weather.addAll(list);
                adapter_forecast.notifyDataSetChanged();//刷新数据源
            } else {
                ToastUtils.showShortToast(context, "天气数据为空");
            }
        } else {
            ToastUtils.showShortToast(context, response.body().getCode());
        }
    }

    /**
     * 获得生活建议
     *
     * @param response
     */
    @Override
    public void getLifeSuggestion(retrofit2.Response<LifeSuggestion> response) {
        if (response.body().getCode().equals("200")) {
            List<LifeSuggestion.Daily> data = response.body().getDaily();
            if (!ObjectUtils.isEmpty(data)) {
                for (int i = 0; i < data.size(); i++) {
                    if (data.get(i).getType().equals("1")) {
                        tvSport.setText("运动指数：" + data.get(i).getText());
                    } else if (data.get(i).getType().equals("2")) {
                        tvCw.setText("洗车指数：" + data.get(i).getText());
                    } else if (data.get(i).getType().equals("3")) {
                        tvDrsg.setText("穿衣指数：" + data.get(i).getText());
                    } else if (data.get(i).getType().equals("4")) {
                        tvFis.setText("钓鱼指数：" + data.get(i).getText());
                    } else if (data.get(i).getType().equals("5")) {
                        tvUv.setText("紫外线指数：" + data.get(i).getText());
                    } else if (data.get(i).getType().equals("6")) {
                        tvTrav.setText("旅游指数：" + data.get(i).getText());
                    } else if (data.get(i).getType().equals("8")) {
                        tvComf.setText("舒适度：" + data.get(i).getText());
                    } else if (data.get(i).getType().equals("9")) {
                        tvFlu.setText("感冒指数：" + data.get(i).getText());
                    } else if (data.get(i).getType().equals("15")) {
                        tvPtfc.setText("交通指数：" + data.get(i).getText());
                    } else if (data.get(i).getType().equals("10")) {
                        tvAir.setText("空气指数：" + data.get(i).getText());
                    }

                }
            }
        }
    }

    @Override
    public void getDataFailed() {
        ToastUtils.showShortToast(context, "error happened");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    /**
     * 控制风车销毁
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        wmBig.stop();
        wmSmall.stop();
    }

    /**
     * 定位结果返回, 并返回天气数据
     */
    private class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            double latitude = bdLocation.getLatitude();//获取纬度信息
            double longitude = bdLocation.getLongitude();//获取经度信息
            float radius = bdLocation.getRadius();//获取定位精度
            String coorType = bdLocation.getCoorType();//获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准
            int errorCode = bdLocation.getLocType();// 161 表示网络定位结果
            String addr = bdLocation.getAddrStr();
            String country = bdLocation.getCountry();
            String province = bdLocation.getProvince();
            String city = bdLocation.getCity();
            String district = bdLocation.getDistrict();
            String street = bdLocation.getStreet();
            String locationDescribe = bdLocation.getLocationDescribe();
            areaTv.setText(district);

            //get locationId

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://geoapi.qweather.com/v2/city/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            apiService_location = retrofit.create(ApiService.class);
            retrofit2.Call<LocationInfo> call = apiService_location.getCityId(city);
            call.enqueue(new retrofit2.Callback<LocationInfo>() {
                @Override
                public void onResponse(retrofit2.Call<LocationInfo> call, retrofit2.Response<LocationInfo> response) {
                    if (response.body() != null) {
                        String locationId = "";
                        locationId = response.body().getLocation().get(1).getId();
                        //设置今天的天气
                        mPresenter.todayWeather(context, locationId);
                        //设置预报天气
                        mPresenter.WeatherForecast(context, locationId);
                        //获得生活建议
                        mPresenter.lifeSuggestion(context, locationId);
                        Log.e(TAG, "onReceiveLocation: " + locationId);
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<LocationInfo> call, Throwable t) {
                }
            });


        }
    }

    /**
     * 初始化天气预测列表
     */
    @SuppressLint("ResourceType")
    public void initWeatherList() {
        list_forecast_weather = new ArrayList<WeatherForcastResponse.Daily>();
        adapter_forecast = new WeatherForecastAdapter(R.layout.item_weather_forecast_list, list_forecast_weather);//为Adapter适配页面布局和数据源
        LinearLayoutManager manager = new LinearLayoutManager(context);//为布局设置管理器，默认是纵向
        tempForecast.setLayoutManager(manager);
        tempForecast.setAdapter(adapter_forecast);

    }

    /**
     * 渲染城市 省市县三级数据
     *
     * @param recyclerView
     * @param cityBack
     * @param areaBack
     * @param windowTitle
     */
    private void initCityData(RecyclerView recyclerView, ImageView cityBack, ImageView areaBack, TextView windowTitle) {
        /**
         * 初始化省级数据，添加到列表中
         */
        try {
            //读取城市数据
            InputStream inputStream = getResources().getAssets().open("City");
            int size = inputStream.available();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer = new StringBuffer();
            String lines = bufferedReader.readLine();
            while (lines != null) {
                stringBuffer.append(lines);
                lines = bufferedReader.readLine();
            }
                /**
                 * 用数组获取省级数据 ，显示到列表上
                 */
                final JSONArray data = new JSONArray(stringBuffer.toString());
                for (int i = 0; i < data.length(); i++) {
                    JSONObject provinceObject = data.getJSONObject(i);
                    String provinceName = provinceObject.getString("name");
                    CityResponse response = new CityResponse();
                    response.setName(provinceName);
                    provinceList.add(response);
                }


                /**
                 * 定义省份显示适配器
                 */
                provinceAdapter = new ProvinceAdapter(R.layout.item_city_list, provinceList);
                LinearLayoutManager manager = new LinearLayoutManager(context);
                recyclerView.setLayoutManager(manager);
                recyclerView.setAdapter(provinceAdapter);
                provinceAdapter.notifyDataSetChanged();
                RecycleViewAnimation.runLayoutAnimationRight(recyclerView);
                provinceAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                    @Override
                    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                        try {
                            //返回上一级数据
                            cityBack.setVisibility(View.VISIBLE);
                            cityBack.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    recyclerView.setAdapter(provinceAdapter);
                                    provinceAdapter.notifyDataSetChanged();
                                    cityBack.setVisibility(View.GONE);
                                    windowTitle.setText("china");
                                }
                            });

                            /**
                             * 根据当前所在省份的数组获取城市的数组
                             */
                            JSONObject provinceObject = data.getJSONObject(position);
                            windowTitle.setText(provinceList.get(position).getName());
                            provinceTitle = provinceList.get(position).getName();
                            final JSONArray cityArray = provinceObject.getJSONArray("city");

                            //更新列表数据
                            if (cityList != null) {
                                cityList.clear();
                            }
                            for (int i = 0; i < cityArray.length(); i++) {
                                JSONObject cityObject = cityArray.getJSONObject(i);
                                String cityName = cityObject.getString("name");
                                CityResponse.CityBean response = new CityResponse.CityBean();
                                response.setName(cityName);
                                cityList.add(response);
                            }

                            cityAdapter = new CityAdapter(R.layout.item_city_list, cityList);
                            LinearLayoutManager manager = new LinearLayoutManager(context);
                            recyclerView.setLayoutManager(manager);
                            recyclerView.setAdapter(cityAdapter);
                            RecycleViewAnimation.runLayoutAnimationRight(recyclerView);

                            cityAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                                @Override
                                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                                    //返回上一级数据
                                    areaBack.setVisibility(View.VISIBLE);
                                    areaBack.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            recyclerView.setAdapter(cityAdapter);
                                            cityAdapter.notifyDataSetChanged();
                                            areaBack.setVisibility(View.GONE);
                                            windowTitle.setText(provinceTitle);
                                            areaList.clear();
                                        }
                                    });

                                    try {
                                        /**
                                         * 获取县级数据
                                         */
                                        windowTitle.setText(cityList.get(position).getName());
                                        JSONObject cityObject = cityArray.getJSONObject(position);
                                        JSONArray areaArray = cityObject.getJSONArray("area");
                                        if (areaList != null) {
                                            areaList.clear();
                                        }
                                        if (list != null) {
                                            list.clear();
                                        }
                                        for (int i = 0; i < areaArray.length(); i++) {
                                            list.add(areaArray.getString(i));
                                        }
                                        for (int i = 0; i < list.size(); i++) {
                                            CityResponse.CityBean.AreaBean response = new CityResponse.CityBean.AreaBean();
                                            response.setName(list.get(i).toString());
                                            areaList.add(response);
                                        }
                                        areaAdapter = new AreaAdapter(R.layout.item_city_list, areaList);
                                        LinearLayoutManager manager = new LinearLayoutManager(context);
                                        recyclerView.setLayoutManager(manager);
                                        recyclerView.setAdapter(areaAdapter);
                                        RecycleViewAnimation.runLayoutAnimationRight(recyclerView);

                                        areaAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                                            @Override
                                            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                                                retrofit2.Call<LocationInfo> call = apiService_location.getCityId(areaList.get(position).getName());
                                                call.enqueue(new retrofit2.Callback<LocationInfo>() {
                                                    @Override
                                                    public void onResponse(retrofit2.Call<LocationInfo> call, retrofit2.Response<LocationInfo> response) {
                                                        if (response.body() != null) {
                                                            String locationId;
                                                            locationId = response.body().getLocation().get(0).getId();
                                                            mPresenter.todayWeather(context, locationId);
                                                            mPresenter.WeatherForecast(context, locationId);
                                                            mPresenter.lifeSuggestion(context, locationId);
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(retrofit2.Call<LocationInfo> call, Throwable t) {
                                                    }
                                                });
                                                areaTv.setText(areaList.get(position).getName());
                                                cityWindow.closePopupWindow();
                                            }
                                        });
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

        //获得今天的天气
    private void getTodayWeather(double longitude, double latitude) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://devapi.qweather.com/v7/weather/now?location=" + longitude + "," + latitude + "&key=7724e0d43ee447109f46928e8b08667e")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: get it");
                    Log.d(TAG, "response.code()==" + response.code());
                    Log.d(TAG, "response.body().string()== " + response.body().string());
                }
            }
        });
    }
}