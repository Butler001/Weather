package com.example.weather_butler;

import android.Manifest;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.mvplibrary.mvp.MvpActivity;
import com.example.mvplibrary.utils.CityWindow;
import com.example.mvplibrary.utils.ObjectUtils;
import com.example.mvplibrary.utils.RecycleViewAnimation;
import com.example.mvplibrary.view.RoundProgressBar;
import com.example.mvplibrary.view.WhiteWindmills;
import com.example.weather_butler.adapter.AreaAdapter;
import com.example.weather_butler.adapter.CityAdapter;
import com.example.weather_butler.adapter.ProvinceAdapter;
import com.example.weather_butler.adapter.WeatherForecastAdapter;
import com.example.weather_butler.adapter.WeatherHourlyAdapter;
import com.example.weather_butler.api.ApiService;
import com.example.weather_butler.bean.AirConditionResponse;
import com.example.weather_butler.bean.BingImageResponse;
import com.example.weather_butler.bean.CityResponse;
import com.example.weather_butler.bean.HourlyResponse;
import com.example.weather_butler.bean.LifeSuggestion;
import com.example.weather_butler.bean.LocationInfo;
import com.example.weather_butler.bean.TodayResponse;
import com.example.weather_butler.bean.WeatherForcastResponse;
import com.example.weather_butler.contract.WeatherContract;
import com.example.weather_butler.utils.StatusBarUtil;
import com.example.weather_butler.utils.ToastUtils;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
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
    @BindView(R.id.weather_color)
    ImageView Background;
    @BindView(R.id.temperature_air_condition)
    TextView temperatureAirConditon;
    @BindView(R.id.rl_wind)
    RelativeLayout rlWind;
    @BindView(R.id.scroll)
    ScrollView scroll;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.iv_location)
    ImageView ivLocation;
    @BindView(R.id.rv_hourly)
    RecyclerView rvHourly;
    @BindView(R.id.airCond_RPB)
    RoundProgressBar airCondRPB;
    @BindView(R.id.tv_PM10)
    TextView tvPM10;
    @BindView(R.id.tv_PM25)
    TextView tvPM25;
    @BindView(R.id.tv_NO2)
    TextView tvNO2;
    @BindView(R.id.tv_SO2)
    TextView tvSO2;
    @BindView(R.id.tv_O3)
    TextView tvO3;
    @BindView(R.id.tv_CO)
    TextView tvCO;


    private RxPermissions rxPermissions;
    //TAG
    public static final String TAG = "LQL";
    //?????????
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();
    //????????????????????????????????????
    private String district;
    //???????????????????????????????????????
    private String city;
    //get resource for weather
    List<WeatherForcastResponse.Daily> list_forecast_weather;
    //get adapter
    WeatherForecastAdapter adapter_forecast;
    //???????????????????????????????????????
    List<HourlyResponse.Hourly> list_hourly_weather;
    WeatherHourlyAdapter adapter_hourly;

    //??????????????????
    private boolean flag = true;

    /**
     * ??????????????????
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
            //??????????????????
            permissionsRequest();
        } else {
            ToastUtils.showShortToast(this, "???????????????6.0?????????????????????");
        }
    }

    //??????????????????
    private void permissionsRequest() {
        rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(granted -> {
                    if (granted) {
                        //??????????????????????????????
                        startLocation();
                    } else {
                        ToastUtils.showShortToast(this, "???????????????");
                    }
                });
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(granted -> {
            if (granted) {
                Log.d(TAG, "permissionsRequest: get permission");
            }
        });
    }

    //????????????
    private void startLocation() {
        mLocationClient = new LocationClient(this);
        //??????????????????
        mLocationClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();

        option.setIsNeedAddress(true);
        option.setNeedNewVersionRgc(true);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    //??????presenter
    @Override
    protected WeatherContract.WeatherPresenter createPresenter() {
        return new WeatherContract.WeatherPresenter();
    }

    //??????????????? ?????????
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
     * ?????????????????????????????????
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
     * ??????????????????
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
     * ???????????????????????????
     *
     * @param response
     */
    @Override
    public void getTodayWeatherResult(retrofit2.Response<TodayResponse> response) {
        //????????????
        refresh.finishRefresh();
        //????????????
        dismissLoadingDialog();
        //???????????????????????????????????????
        mLocationClient.stop();
        if (response.body() != null) {
            //????????????
            temperatureCurrent.setText(response.body().getNow().getTemp());

            if (flag) {
                ivLocation.setVisibility(View.VISIBLE);
            } else {
                ivLocation.setVisibility(View.GONE);
            }
            temperatureCondition.setText(response.body().getNow().getText());
            temperatureTimeUpdate.setText("????????????: " + response.body().getUpdateTime().replace("+08:00", "").replace("T", " "));

            windDirection.setText("??????   " + response.body().getNow().getWindDir());
            windPower.setText("??????    " + response.body().getNow().getWindScale() + "???");
            wmBig.start();
            wmSmall.start();
        } else {
            ToastUtils.showShortToast(context, response.body().getCode());
        }

    }

    /**
     * ??????????????????
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
                adapter_forecast.notifyDataSetChanged();//???????????????
            } else {
                ToastUtils.showShortToast(context, "??????????????????");
            }
        } else {
            ToastUtils.showShortToast(context, response.body().getCode());
        }
    }

    /**
     * ??????????????????
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
                        tvSport.setText("???????????????" + data.get(i).getText());
                    } else if (data.get(i).getType().equals("2")) {
                        tvCw.setText("???????????????" + data.get(i).getText());
                    } else if (data.get(i).getType().equals("3")) {
                        tvDrsg.setText("???????????????" + data.get(i).getText());
                    } else if (data.get(i).getType().equals("4")) {
                        tvFis.setText("???????????????" + data.get(i).getText());
                    } else if (data.get(i).getType().equals("5")) {
                        tvUv.setText("??????????????????" + data.get(i).getText());
                    } else if (data.get(i).getType().equals("6")) {
                        tvTrav.setText("???????????????" + data.get(i).getText());
                    } else if (data.get(i).getType().equals("8")) {
                        tvComf.setText("????????????" + data.get(i).getText());
                    } else if (data.get(i).getType().equals("9")) {
                        tvFlu.setText("???????????????" + data.get(i).getText());
                    } else if (data.get(i).getType().equals("15")) {
                        tvPtfc.setText("???????????????" + data.get(i).getText());
                    } else if (data.get(i).getType().equals("10")) {
                        tvAir.setText("???????????????" + data.get(i).getText());
                    }

                }
            }
        }
    }

    /**
     * ?????????????????????
     *
     * @param response
     */
    @Override
    public void getHourlyWeather(retrofit2.Response<HourlyResponse> response) {
        //????????????
        dismissLoadingDialog();
        if (response.body().getCode().equals("200")) {
            if (response.body().getHourly() != null) {
                List<HourlyResponse.Hourly> data = response.body().getHourly();
                list_hourly_weather.clear();
                list_hourly_weather.addAll(data);
                adapter_hourly.notifyDataSetChanged();
            } else {
                ToastUtils.showShortToast(context, "???????????????????????????");
            }

        } else {
            ToastUtils.showShortToast(context, "??????????????????" + response.body().getCode());
        }
    }

    /**
     * ????????????????????????
     *
     * @param response
     */
    @Override
    public void getAirCond(retrofit2.Response<AirConditionResponse> response) {
        //????????????
        dismissLoadingDialog();
        if (response.body().getCode().equals("200")) {
            AirConditionResponse.Now data = response.body().getNow();
            if (!ObjectUtils.isEmpty(data)){
                airCondRPB.setMaxProgress(500);//?????????????????? ????????????
                airCondRPB.setMinText("0");//??????????????????
                airCondRPB.setMinTextSize(32f);
                airCondRPB.setMaxText("500");
                airCondRPB.setMaxTextSize(32f);
                airCondRPB.setArcBgColor(R.color.arc_bg_color);
                airCondRPB.setProgressColor(R.color.arc_progress_color);
                airCondRPB.setCurrentProgress(Float.parseFloat(data.getAqi()));
                airCondRPB.setFirstText(data.getCategory());
                temperatureAirConditon.setText("??????"+data.getCategory());
                airCondRPB.setFirstTextSize(44f);
                airCondRPB.setSecondText(data.getAqi());
                airCondRPB.setSecondTextSize(64f);

                tvPM10.setText(data.getPm10());
                tvPM25.setText(data.getPm2p5());
                tvNO2.setText(data.getNo2());
                tvSO2.setText(data.getSo2());
                tvO3.setText(data.getO3());
                tvCO.setText(data.getCo());
            }

        } else {
            ToastUtils.showShortToast(context, "??????????????????" + response.body().getCode());
        }
    }

    /**
     * ????????????????????????
     *
     * @param response
     */
    @Override
    public void getBingResult(retrofit2.Response<BingImageResponse> response) {
        dismissLoadingDialog();

        if (response.body().getImages() != null) {
            //???????????????????????????????????????????????????????????????
            String imageUrl = "http://cn.bing.com" + response.body().getImages().get(0).getUrl();

            //??????Glide??????????????????
            Glide.with(context)
                    .asBitmap()
                    .load(imageUrl)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull @NotNull Bitmap resource, @Nullable @org.jetbrains.annotations.Nullable Transition<? super Bitmap> transition) {
                            Drawable drawable = new BitmapDrawable(context.getResources(), resource);
                            Background.setBackground(drawable);
                        }

                        @Override
                        public void onLoadCleared(@Nullable @org.jetbrains.annotations.Nullable Drawable placeholder) {

                        }
                    });
        } else {
            ToastUtils.showShortToast(context, "????????????");
        }
    }


    /**
     * ???????????????????????????
     */
    @Override
    public void getDataFailed() {
        //?????????????????????????????????
        dismissLoadingDialog();
        refresh.finishRefresh();
        ToastUtils.showShortToast(context, "error happened");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    /**
     * ??????????????????
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        wmBig.stop();
        wmSmall.stop();
    }

    /**
     * ??????????????????, ?????????????????????
     */
    private class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            double latitude = bdLocation.getLatitude();//??????????????????
            double longitude = bdLocation.getLongitude();//??????????????????
            float radius = bdLocation.getRadius();//??????????????????
            String coorType = bdLocation.getCoorType();//?????????????????????????????????LocationClientOption?????????????????????????????????
            int errorCode = bdLocation.getLocType();// 161 ????????????????????????
            String addr = bdLocation.getAddrStr();
            String country = bdLocation.getCountry();
            String province = bdLocation.getProvince();
            city = bdLocation.getCity();
            district = bdLocation.getDistrict();
            String street = bdLocation.getStreet();
            String locationDescribe = bdLocation.getLocationDescribe();
            areaTv.setText(district);
            //???????????????????????????????????????
            showLoadingDialog();
            //????????????????????????
            mPresenter.bing(context);
            //get locationId
            /**
             * ???????????????????????????
             * @params String locationName
             *
             */
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://geoapi.qweather.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ApiService apiService_location = retrofit.create(ApiService.class);
            //????????????ID
            retrofit2.Call<LocationInfo> call = apiService_location.getCityId(district);
            call.enqueue(new retrofit2.Callback<LocationInfo>() {
                @Override
                public void onResponse(retrofit2.Call<LocationInfo> call, retrofit2.Response<LocationInfo> response) {
                    if (response.body() != null) {
                        String value = response.body().getLocation().get(0).getId();
                        mPresenter.todayWeather(context, value);//?????????????????????
                        //??????????????????
                        mPresenter.WeatherForecast(context, value);
                        //??????????????????
                        mPresenter.lifeSuggestion(context, value);
                        //???????????????
                        mPresenter.hourlyWeather(context, value);
                        Log.e(TAG, "onResponse:-get " + value);
                        //????????????
                        refresh.setOnRefreshListener(refreshLayout -> {

                            mPresenter.todayWeather(context, value);
                            mPresenter.WeatherForecast(context, value);
                            mPresenter.lifeSuggestion(context, value);
                            mPresenter.hourlyWeather(context, value);

                        });
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<LocationInfo> call, Throwable t) {
                }
            });
            //????????????ID
            retrofit2.Call call_city = apiService_location.getCityId(city);
            call_city.enqueue(new retrofit2.Callback<LocationInfo>() {
                @Override
                public void onResponse(retrofit2.Call<LocationInfo> call, retrofit2.Response<LocationInfo> response) {
                    if (response.body() != null) {
                        String value = response.body().getLocation().get(0).getId();
                        mPresenter.airCondition(context, value);


                        refresh.setOnRefreshListener(refreshLayout -> {
                            mPresenter.airCondition(context, value);
                        });
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<LocationInfo> call, Throwable t) {

                }
            });
        }
    }


    /**
     * ???????????????????????????
     */
    @SuppressLint("ResourceType")
    public void initWeatherList() {
        //??????????????????
        list_forecast_weather = new ArrayList<WeatherForcastResponse.Daily>();
        adapter_forecast = new WeatherForecastAdapter(R.layout.item_weather_forecast_list, list_forecast_weather);//???Adapter??????????????????????????????
        LinearLayoutManager manager = new LinearLayoutManager(context);//??????????????????????????????????????????
        tempForecast.setLayoutManager(manager);
        tempForecast.setAdapter(adapter_forecast);
        //?????????????????????
        list_hourly_weather = new ArrayList<>();
        adapter_hourly = new WeatherHourlyAdapter(R.layout.item_weather_hourly_list, list_hourly_weather);
        LinearLayoutManager manager_hourly = new LinearLayoutManager(context);
        manager_hourly.setOrientation(RecyclerView.HORIZONTAL);
        rvHourly.setLayoutManager(manager_hourly);
        rvHourly.setAdapter(adapter_hourly);
    }

    /**
     * ???????????? ?????????????????????
     *
     * @param recyclerView
     * @param cityBack
     * @param areaBack
     * @param windowTitle
     */
    private void initCityData(RecyclerView recyclerView, ImageView cityBack, ImageView areaBack, TextView windowTitle) {
        /**
         * ??????????????????????????????????????????
         */
        try {
            //??????????????????
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
             * ??????????????????????????? ?????????????????????
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
             * ???????????????????????????
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
                        //?????????????????????
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
                         * ??????????????????????????????????????????????????????
                         */
                        JSONObject provinceObject = data.getJSONObject(position);
                        windowTitle.setText(provinceList.get(position).getName());
                        provinceTitle = provinceList.get(position).getName();
                        final JSONArray cityArray = provinceObject.getJSONArray("city");

                        //??????????????????
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
                                //?????????????????????
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
                                    //?????????????????????????????????????????????????????????????????????
                                    city = cityList.get(position).getName();
                                    /**
                                     * ??????????????????
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
                                            /**
                                             * ???????????????????????????
                                             * @params String locationName
                                             *
                                             */
                                            Retrofit retrofit = new Retrofit.Builder()
                                                    .baseUrl("https://geoapi.qweather.com")
                                                    .addConverterFactory(GsonConverterFactory.create())
                                                    .build();
                                            ApiService apiService_location = retrofit.create(ApiService.class);
                                            //????????????ID
                                            retrofit2.Call<LocationInfo> call = apiService_location.getCityId(areaList.get(position).getName());
                                            call.enqueue(new retrofit2.Callback<LocationInfo>() {
                                                @Override
                                                public void onResponse(retrofit2.Call<LocationInfo> call, retrofit2.Response<LocationInfo> response) {
                                                    if (response.body() != null) {
                                                        String value = response.body().getLocation().get(0).getId();
                                                        //?????????????????????
                                                        mPresenter.todayWeather(context, value);
                                                        //??????????????????
                                                        mPresenter.WeatherForecast(context, value);
                                                        //??????????????????
                                                        mPresenter.lifeSuggestion(context, value);
                                                        //???????????????
                                                        mPresenter.hourlyWeather(context, value);
                                                        Log.e(TAG, "onResponse:-get " + value);
                                                        refresh.setOnRefreshListener(refreshLayout -> {
                                                            //?????????????????????
                                                            mPresenter.todayWeather(context, value);
                                                            //??????????????????
                                                            mPresenter.WeatherForecast(context, value);
                                                            //??????????????????
                                                            mPresenter.lifeSuggestion(context, value);
                                                            //???????????????
                                                            mPresenter.hourlyWeather(context, value);
                                                        });
                                                    }
                                                }

                                                @Override
                                                public void onFailure(retrofit2.Call<LocationInfo> call, Throwable t) {
                                                }
                                            });
                                            //????????????ID
                                            retrofit2.Call call_city = apiService_location.getCityId(city);
                                            call_city.enqueue(new retrofit2.Callback<LocationInfo>() {
                                                @Override
                                                public void onResponse(retrofit2.Call<LocationInfo> call, retrofit2.Response<LocationInfo> response) {
                                                    if (response.body() != null) {
                                                        String value = response.body().getLocation().get(0).getId();
                                                        mPresenter.airCondition(context, value);

                                                        refresh.setOnRefreshListener(refreshLayout -> {
                                                            mPresenter.airCondition(context, city);
                                                        });
                                                    }
                                                }

                                                @Override
                                                public void onFailure(retrofit2.Call<LocationInfo> call, Throwable t) {

                                                }
                                            });
                                            district = areaList.get(position).getName();//???????????????????????????????????????????????????????????????
                                            flag = false;
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

    //?????????????????????
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