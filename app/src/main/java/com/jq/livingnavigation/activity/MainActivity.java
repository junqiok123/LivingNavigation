package com.jq.livingnavigation.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.amap.api.location.AMapLocalDayWeatherForecast;
import com.amap.api.location.AMapLocalWeatherForecast;
import com.amap.api.location.AMapLocalWeatherListener;
import com.amap.api.location.AMapLocalWeatherLive;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.jq.livingnavigation.R;
import com.jq.livingnavigation.config.Constant;
import com.jq.livingnavigation.utils.LogUtil;
import com.jq.livingnavigation.utils.ToastUtil;
import com.zzt.inbox.interfaces.OnDragStateChangeListener;
import com.zzt.inbox.widget.InboxBackgroundScrollView;
import com.zzt.inbox.widget.InboxLayoutBase;
import com.zzt.inbox.widget.InboxLayoutScrollView;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends ActionBarActivity implements View.OnClickListener, AMapLocationListener, AMapLocalWeatherListener {

    private InboxLayoutScrollView guessLayout, robotLayout;
    private WebView webView;
    private GifImageView gif;
    private LinearLayout layout_shuaxin;
    private GifDrawable gifDrawable;

    //高德
    private LocationManagerProxy mLocationManagerProxy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF004567));
        getSupportActionBar().setTitle("首页");


        initInbox();
        init();

        initAMap();
    }

    private void initInbox() {
        final InboxBackgroundScrollView inboxBackgroundScrollView = (InboxBackgroundScrollView) findViewById(R.id.scroll);
        guessLayout = (InboxLayoutScrollView) findViewById(R.id.guesslayout);
        guessLayout.setBackgroundScrollView(inboxBackgroundScrollView);//绑定scrollview
        guessLayout.setCloseDistance(50);
        guessLayout.setOnDragStateChangeListener(new OnDragStateChangeListener() {
            @Override
            public void dragStateChange(InboxLayoutBase.DragState state) {
                switch (state) {
                    case CANCLOSE:
                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xff87D3E3));
                        getSupportActionBar().setTitle("松开返回");
                        break;
                    case CANNOTCLOSE:
                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF004567));
                        getSupportActionBar().setTitle("首页");
                        break;
                }
            }
        });
        robotLayout = (InboxLayoutScrollView) findViewById(R.id.robotlayout);
        robotLayout.setBackgroundScrollView(inboxBackgroundScrollView);//绑定scrollview
        robotLayout.setCloseDistance(50);
        robotLayout.setOnDragStateChangeListener(new OnDragStateChangeListener() {
            @Override
            public void dragStateChange(InboxLayoutBase.DragState state) {
                switch (state) {
                    case CANCLOSE:
                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xff87D3E3));
                        getSupportActionBar().setTitle("松开返回");
                        break;
                    case CANNOTCLOSE:
                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF004567));
                        getSupportActionBar().setTitle("首页");
                        break;
                }
            }
        });
    }


    private void init() {

        final LinearLayout dingdan = (LinearLayout) findViewById(R.id.ding_dan);
        dingdan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportActionBar().setTitle("逗比猜一猜");
                guessLayout.openWithAnim(dingdan);
            }
        });

        final LinearLayout yuding = (LinearLayout) findViewById(R.id.yuding);
        yuding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportActionBar().setTitle("无聊机器人");
                robotLayout.openWithAnim(yuding);
            }
        });

        final LinearLayout tuijian = (LinearLayout) findViewById(R.id.tuijian);
        tuijian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                inboxLayoutListView.openWithAnim(tuijian);
                startActivity(new Intent(MainActivity.this, LocationActivity.class));
            }
        });

        webView = (WebView) findViewById(R.id.hello_world);
        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        ws.setUseWideViewPort(true);
        ws.setLoadWithOverviewMode(true);
        ws.setLoadsImagesAutomatically(true);
//        webView.setHorizontalScrollBarEnabled(false);//设置水平滚动条
//        webView.setVerticalScrollBarEnabled(false);//设置竖直滚动条
        webView.loadUrl(Constant.HELLO_WORLD_URL);
        webView.setWebViewClient(new MyWebViewClient());

        gif = (GifImageView) findViewById(R.id.gif);
        try {
            gifDrawable = new GifDrawable(getResources(), R.drawable.gif);
            gif.setImageDrawable(gifDrawable);
        } catch (IOException e) {
            e.printStackTrace();
        }
        layout_shuaxin = (LinearLayout) findViewById(R.id.layout_shuaxin);
        layout_shuaxin.setOnClickListener(this);
    }

    private void initAMap() {
        //高德
        mLocationManagerProxy = LocationManagerProxy.getInstance(this);
        //此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        //注意设置合适的定位时间的间隔，并且在合适时间调用removeUpdates()方法来取消定位请求
        //在定位结束后，在合适的生命周期调用destroy()方法
        //其中如果间隔时间为-1，则定位只定一次
        mLocationManagerProxy.requestLocationData(
                LocationProviderProxy.AMapNetwork, -1, 15, this);
        mLocationManagerProxy.setGpsEnable(false);
    }

    private void stopLocation() {
        if (mLocationManagerProxy != null) {
            mLocationManagerProxy.removeUpdates(this);
            mLocationManagerProxy.destroy();
        }
        mLocationManagerProxy = null;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null && aMapLocation.getAMapException().getErrorCode() == 0) {
            //获取位置信息
            Double geoLat = aMapLocation.getLatitude();
            Double geoLng = aMapLocation.getLongitude();
            StringBuffer b = new StringBuffer();
            b.append(aMapLocation.getAdCode()).append(",").append(aMapLocation.getAddress()).append(",").append(aMapLocation.getAMapException()).append(",")
                    .append(aMapLocation.getCity()).append(",").append(aMapLocation.getCityCode()).append(",").append(aMapLocation.getCountry()).append(",")
                    .append(aMapLocation.getDistrict()).append(",").append(aMapLocation.getFloor()).append(",").append(aMapLocation.getPoiId()).append(",")
                    .append(aMapLocation.getPoiName()).append(",").append(aMapLocation.getPoiId()).append(",").append(aMapLocation.getProvince()).append(",")
                    .append(aMapLocation.getRoad()).append(",");
            LogUtil.e("AMapLocation", b.toString());

            /**
             * 注册天气监听
             */
            mLocationManagerProxy = LocationManagerProxy.getInstance(this);
            mLocationManagerProxy.requestWeatherUpdates(
                    LocationManagerProxy.WEATHER_TYPE_LIVE, this);
//            mLocationManagerProxy.requestWeatherUpdates(
//                    LocationManagerProxy.WEATHER_TYPE_FORECAST, this);

        } else {
            ToastUtil.show(MainActivity.this, "定位失败，请重试!");
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onWeatherLiveSearched(AMapLocalWeatherLive aMapLocalWeatherLive) {
        if (aMapLocalWeatherLive != null && aMapLocalWeatherLive.getAMapException().getErrorCode() == 0) {
            String city = aMapLocalWeatherLive.getCity();//城市
            String weather = aMapLocalWeatherLive.getWeather();//天气情况
            String windDir = aMapLocalWeatherLive.getWindDir();//风向
            String windPower = aMapLocalWeatherLive.getWindPower();//风力
            String humidity = aMapLocalWeatherLive.getHumidity();//空气湿度
            String reportTime = aMapLocalWeatherLive.getReportTime();//数据发布时间
            LogUtil.e("WeatherLive", city + "," + weather + "," + windDir + "," + windPower + "," + humidity + "," + reportTime);
        } else {
            // 获取天气预报失败
            ToastUtil.show(MainActivity.this, "获取天气预报失败:" + aMapLocalWeatherLive.getAMapException().getErrorMessage());
        }
        try {
            gifDrawable = new GifDrawable(getResources(), R.drawable.gif);
            gif.setImageDrawable(gifDrawable);
            gifDrawable.stop();
            stopLocation();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onWeatherForecaseSearched(AMapLocalWeatherForecast aMapLocalWeatherForecast) {
        if (aMapLocalWeatherForecast != null && aMapLocalWeatherForecast.getAMapException().getErrorCode() == 0) {
            AMapLocalDayWeatherForecast forcast = aMapLocalWeatherForecast.getWeatherForecast().get(0);
            String city = forcast.getCity();
            String today = "今天 ( " + forcast.getDate() + " )";
            String todayWeather = forcast.getDayWeather() + "    "
                    + forcast.getDayTemp() + "/" + forcast.getNightTemp()
                    + "    " + forcast.getDayWindPower();
            LogUtil.e("DayWeatherForecast", city + today + todayWeather);
        } else {
            // 获取天气预报失败
            ToastUtil.show(MainActivity.this, "获取天气预报失败:" + aMapLocalWeatherForecast.getAMapException().getErrorMessage());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_shuaxin:
                if (!gifDrawable.isRunning()) {
                    initAMap();
                    gifDrawable.start();
                }
                break;
        }
    }

    private class MyWebViewClient extends WebViewClient {

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
