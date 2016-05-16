package suda.sudamodweather.util;

import suda.sudamodweather.R;

/**
 * Created by ghbha on 2016/5/15.
 */
public class WeatherIconUtil {

    public static int getWeatherIconID(String weatherCondition) {
        if (weatherCondition.equals("晴")) {
            return R.drawable.ic_ziwaixian_white;
        }
        if (weatherCondition.equals("阴")) {
            return R.drawable.ic_overcast;
        }
        if (weatherCondition.equals("多云")) {
            return R.drawable.ic_cloudy;
        }
        if (weatherCondition.equals("雾")) {
            return R.drawable.ic_fog;
        }
        if (weatherCondition.equals("小雨")) {
            return R.drawable.ic_xiaoyu;
        }
        if (weatherCondition.equals("小雪")) {
            return R.drawable.ic_xiaoxue;
        }
        if (weatherCondition.equals("中雨")) {
            return R.drawable.ic_zhongyu;
        }
        if (weatherCondition.equals("阵雨")) {
            return R.drawable.ic_baoyu;
        }
        if (weatherCondition.equals("雷阵雨")) {
            return R.drawable.ic_leizhenyu;
        }
        if (weatherCondition.equals("阵雪")) {
            return R.drawable.ic_zhenxue;
        }
        if (weatherCondition.equals("中雪")) {
            return R.drawable.ic_zhongxue;
        }
        if (weatherCondition.equals("大雪")) {
            return R.drawable.ic_daxue;
        }
        if (weatherCondition.equals("暴雪")) {
            return R.drawable.ic_baoxue;
        }
        if (weatherCondition.equals("雨夹雪")) {
            return R.drawable.ic_yujiaxue;
        }
        if (weatherCondition.equals("霾") || weatherCondition.equals("浮尘") || weatherCondition.equals("扬沙")) {
            return R.drawable.ic_mai;
        }
        if (weatherCondition.contains("雷")) {
            return R.drawable.ic_cloudy;
        }
        return R.drawable.ic_dayu;
    }

}
