package suda.sudamodweather.util;

import java.util.HashMap;
import java.util.Map;

import suda.sudamodweather.R;

/**
 * Created by Suda on 2015/10/10.
 */
public class Constant {

    public final static int MSG_ERROR = 0;
    public final static int MSG_SUCCESS = 1;
    public static final String URL_WEATHER_2345 =
            "http://tianqi.2345.com/t/new_mobile_json/%s.json";
    public static final String URL_FORECAST_FLYME =
            "http://aider.meizu.com/app/weather/listWeather?cityIds=%s";
    public final static Map<String, Integer> ZHISHU = new HashMap();
    private static final String URL_MIUI_WEATHER =
            "http://weatherapi.market.xiaomi.com/wtr-v2/weather?cityId=%s&language=zh_CN&imei=e32c8a29d0e8633283737f5d9f381d47&device=HM2013023&miuiVersion=JHBCNBD16.0&mod";

    static {
        ZHISHU.put("紫外线强度指数", R.drawable.ic_ziwaixian_white);
        ZHISHU.put("穿衣指数", R.drawable.ic_chuanyizhishu_white);
        ZHISHU.put("运动指数", R.drawable.ic_yundongzhishu_white);
        ZHISHU.put("洗车指数", R.drawable.ic_xichezhishu_white);
        ZHISHU.put("晾晒指数", R.drawable.ic_liangshaizhishu_white);
    }


}
