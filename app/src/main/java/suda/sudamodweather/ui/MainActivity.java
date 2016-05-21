package suda.sudamodweather.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;
import suda.sudamodweather.R;
import suda.sudamodweather.dao.CityDao;
import suda.sudamodweather.dao.bean.City;
import suda.sudamodweather.dao.bean.OptDO;
import suda.sudamodweather.dao.greendao.Alarms;
import suda.sudamodweather.dao.greendao.Aqi;
import suda.sudamodweather.dao.greendao.RealWeather;
import suda.sudamodweather.dao.greendao.UseArea;
import suda.sudamodweather.dao.greendao.Zhishu;
import suda.sudamodweather.manager.WeatherInfo;
import suda.sudamodweather.manager.WeatherManager;
import suda.sudamodweather.ui.adapter.OptMenuAdapter;
import suda.sudamodweather.ui.adapter.ZhiShuAdapter;
import suda.sudamodweather.util.Constant;
import suda.sudamodweather.util.GpsUtil;
import suda.sudamodweather.util.TextUtil;
import suda.sudamodweather.widget.AqiView;
import suda.sudamodweather.widget.HourForeCastView;
import suda.sudamodweather.widget.MyListView;
import suda.sudamodweather.widget.SunRiseView;
import suda.sudamodweather.widget.WeekForecastView;
import suda.sudamodweather.widget.WindForecastView;
import suda.sudamodweather.widget.WindmillView;
import suda.sudamodweather.widget.weather.SkyView;

public class MainActivity extends AppCompatActivity implements BDLocationListener {

    private CityDao cityDao = new CityDao(this);
    private String weatherID = "101190501";
    private boolean openOrClose = false;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private TextView mCurrentAreaTv;
    private OptMenuAdapter optMenuAdapter;
    private ListView mLvOptItems;
    private WeatherManager weatherManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ScrollView contentMian;
    //实时天气
    private TextView mRealTempTv, mWeatherAndFeelTemp;
    private TextView mRealAqiTv;
    private TextView mUpdateTimeTv;
    private SkyView mSkyView;

    //风速湿度
    private WindmillView windViewBig;
    private WindmillView windViewSmall;
    private TextView mWindDegreeTv, mWindLevelTv;
    private TextView mShiduTv;
    private ProgressBar progressBar;
    //空气指数
    private AqiView mAqi;
    private TextView mPm2_5Tv, mPm10Tv, mSo2Tv, mNo2Tv;
    //日出日落图
    private SunRiseView mSunRiseView;
    //周报 时报
    private WeekForecastView weekForeCastView;
    private HourForeCastView hourForeCastView;
    private WindForecastView windForecastView;
    //指数
    private MyListView mZhishuLv;
    private ZhiShuAdapter mZhiShuAdapter;
    private List<Zhishu> zhishuList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_main);
        weatherManager = new WeatherManager(this);
        initWidget();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        } else {
            initParam();
        }

    }

    protected void getPermission(final String permission) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, 0);
        } else {
            initParam();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        initParam();
    }

    private void initParam() {
        UseArea useArea = weatherManager.queryMianUseArea();
        if (useArea == null) {
            GpsUtil gpsUtil = new GpsUtil(this, this);
            gpsUtil.start();
        } else {
            weatherID = useArea.getAreaid();
            mCurrentAreaTv.setText(useArea.getAreaName());
            refresh(true);
        }
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        Log.d("aaa", bdLocation.getCity() + bdLocation.getDistrict());
        String areaName = TextUtil.getFormatArea(bdLocation.getDistrict());
        String cityName = TextUtil.getFormatArea(bdLocation.getCity());
        City city = cityDao.getCityByCityAndArea(cityName, areaName);
        if (city == null) {
            city = cityDao.getCityByCityAndArea(cityName, cityName);
            if (city == null) {
                swipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
                return;
            }
        }
        weatherManager.insertNewUseArea(city, true);
        weatherID = city.getWeatherId();
        mCurrentAreaTv.setText(city.getAreaName());
        refresh(true);
    }

    private void initWidget() {
        contentMian = (ScrollView) findViewById(R.id.content_main);
        contentMian.setVisibility(View.INVISIBLE);

        mCurrentAreaTv = (TextView) findViewById(R.id.tv_topCity);
        mCurrentAreaTv.setText("正在刷新");

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh(false);
            }
        });

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawLayout);

        setDrawerLayout();
        setRealWeather();
        setForeCast();
        setWind();
        setAqi();
        setSunRiseView();
        setZhiShu();
    }

    private void setZhiShu() {
        mZhishuLv = (MyListView) findViewById(R.id.lv_livingIndex);
        zhishuList = new ArrayList<>();
        mZhiShuAdapter = new ZhiShuAdapter(zhishuList, this);
        mZhishuLv.setAdapter(mZhiShuAdapter);
    }

    private void refresh(boolean useLocal) {
        weatherManager.refreshWeather(weatherID, useLocal, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                contentMian.setVisibility(View.VISIBLE);
                swipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });

                if (msg.what == Constant.MSG_ERROR) {
                    Toast.makeText(MainActivity.this, "刷新失败", Toast.LENGTH_SHORT).show();
                } else {
                    WeatherInfo weatherInfo = (WeatherInfo) msg.obj;
                    Aqi aqi = weatherInfo.getAqi();

                    //实时
                    RealWeather realWeather = weatherInfo.getRealWeather();
                    mSkyView.setWeather(realWeather.getWeatherCondition(), realWeather.getSunrise(), realWeather.getSundown());
                    swipeRefreshLayout.setColorSchemeColors(mSkyView.getBackGroundColor());
                    mCurrentAreaTv.setText(realWeather.getAreaName());
                    mRealTempTv.setText(realWeather.getTemp() + "");
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
                    mUpdateTimeTv.setText(
                            String.format(getResources().getString(R.string.activity_home_refresh_time), simpleDateFormat.format(realWeather.getLastUpdate()))
                    );
                    mRealAqiTv.setText("空气" + aqi.getQuality() + " " + aqi.getAqi());
                    mWeatherAndFeelTemp.setText(
                            String.format(getResources().getString(R.string.activity_home_type_and_real_feel_temp),
                                    realWeather.getWeatherCondition(), realWeather.getFeeltemp())
                    );

                    //周报&&时报
                    weekForeCastView.setForeCasts(weatherInfo.getWeekForeCasts());
                    hourForeCastView.setHourForeCasts(weatherInfo.getHourForeCasts());
                    windForecastView.setForeCasts(weatherInfo.getWeekForeCasts());

                    //风速湿度
                    windViewBig.setWindSpeedDegree(Integer.parseInt(realWeather.getFj().replace("级", "")));

                    windViewSmall.setWindSpeedDegree(Integer.parseInt(realWeather.getFj().replace("级", "")));
                    mWindDegreeTv.setText(realWeather.getFx());
                    mWindLevelTv.setText(realWeather.getFj());
                    progressBar.setProgress(realWeather.getShidu());
                    mShiduTv.setText(realWeather.getShidu() + "");
                    //空气
                    mAqi.setProgressAndLabel(aqi.getAqi(), "空气" + aqi.getQuality());
                    mPm2_5Tv.setText(aqi.getPm2_5() + " μg/m³");
                    mPm10Tv.setText(aqi.getPm10() + " μg/m³");
                    mSo2Tv.setText(aqi.getSo2() + " μg/m³");
                    mNo2Tv.setText(aqi.getNo2() + " μg/m³");
                    //日出
                    mSunRiseView.setSunRiseDownTime(realWeather.getSunrise(), realWeather.getSundown());

                    //指数
                    zhishuList.clear();
                    zhishuList.addAll(weatherInfo.getZhishu());
                    mZhiShuAdapter.notifyDataSetChanged();
                    contentMian.smoothScrollTo(0,0);

                    //预警
                    final Alarms alarms = weatherInfo.getAlarms();
                    if (alarms != null) {
                        mRealAqiTv.setClickable(true);
                        mRealAqiTv.setText(alarms.getAlarmLevelNoDesc() + alarms.getAlarmTypeDesc());
                        mRealAqiTv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(MainActivity.this, AlarmActivity.class);
                                intent.putExtra("alarminfo", alarms);
                                startActivity(intent);
                            }
                        });
                    } else
                        mRealAqiTv.setClickable(false);
                }
            }
        });
    }

    private void setForeCast() {
        weekForeCastView = (WeekForecastView) findViewById(R.id.weekForecast);
        hourForeCastView = (HourForeCastView) findViewById(R.id.hourForecast);
        windForecastView = (WindForecastView) findViewById(R.id.wind_forecast);
    }

    /**
     * 实时
     */
    private void setRealWeather() {
        mSkyView = (SkyView) findViewById(R.id.myWeatherView);
        mRealTempTv = (TextView) findViewById(R.id.tv_RTTemp);
        mWeatherAndFeelTemp = (TextView) findViewById(R.id.tv_RTTypeAndRealFeel);
        mRealAqiTv = (TextView) findViewById(R.id.tv_aqi);
        mUpdateTimeTv = (TextView) findViewById(R.id.tv_updateTime);
    }

    /**
     * 初始化空气
     */
    private void setSunRiseView() {
        mSunRiseView = (SunRiseView) findViewById(R.id.view_sun);
        mSunRiseView.setSunRiseDownTime("05:00", "18:46");
    }

    /**
     * 初始化空气
     */
    private void setAqi() {
        mPm2_5Tv = (TextView) findViewById(R.id.tv_pm25);
        mPm10Tv = (TextView) findViewById(R.id.tv_pm10);
        mSo2Tv = (TextView) findViewById(R.id.tv_so2);
        mNo2Tv = (TextView) findViewById(R.id.tv_no2);

        mAqi = (AqiView) findViewById(R.id.view_aqi);
    }

    /**
     * 初始化风速
     */
    private void setWind() {
        progressBar = (ProgressBar) findViewById(R.id.pb_humidity);
        mShiduTv = (TextView) findViewById(R.id.tv_humidity);

        windViewBig = (WindmillView) findViewById(R.id.windViewBig);
        windViewSmall = (WindmillView) findViewById(R.id.windViewSmall);
        mWindDegreeTv = (TextView) findViewById(R.id.tv_windDire);
        mWindLevelTv = (TextView) findViewById(R.id.tv_windSpeed);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    windViewBig.refreshView();
                    windViewSmall.refreshView();
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    private void setDrawerLayout() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                mToolbar, R.string.open, R.string.close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
                openOrClose = false;
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
                openOrClose = true;
            }
        };
        mDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        //初始化菜单
        mLvOptItems = (ListView) findViewById(R.id.lv_drawer_menu);

        List<OptDO> optDOs = new ArrayList<>();

        optDOs.add(new OptDO(null, 0, R.drawable.ic_drawer_add, "城市管理"));
        optDOs.add(new OptDO(null, 1, R.drawable.ic_drawer_setting, "应用设置"));
        optDOs.add(new OptDO(null, 2, R.drawable.ic_drawer_help, "使用帮助"));
        optDOs.add(new OptDO(null, 4, R.drawable.ic_drawer_contact, "联系我们"));
        optDOs.add(new OptDO(null, 4, R.drawable.ic_drawer_check_update, "检查更新"));
        optDOs.add(new OptDO(null, 5, R.drawable.ic_drawer_about, "关于"));
        optMenuAdapter = new OptMenuAdapter(optDOs, this);
        mLvOptItems.setAdapter(optMenuAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.weather_preview:
                showPreview();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showPreview() {
        final ArrayAdapter<String> arrayAdapter
                = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1);
        arrayAdapter.add("晴");
        arrayAdapter.add("多云");
        arrayAdapter.add("阴");
        arrayAdapter.add("雾");
        arrayAdapter.add("雨");
        arrayAdapter.add("雨夹雪");
        arrayAdapter.add("雪");
        arrayAdapter.add("霾");

        ListView listView = new ListView(this);
        listView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        listView.setDividerHeight(1);
        listView.setAdapter(arrayAdapter);

        final MaterialDialog alert = new MaterialDialog(this).setContentView(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSkyView.setWeather(parent.getAdapter().getItem(position).toString());
                alert.dismiss();
            }
        });
        alert.setCanceledOnTouchOutside(true);
        alert.show();
    }

}
