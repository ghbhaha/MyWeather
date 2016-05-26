package suda.sudamodweather.widget.weather;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import suda.sudamodweather.R;
import suda.sudamodweather.util.DateTimeUtil;


/**
 * Created by ghbha on 2016/5/15.
 */
public class SkyView extends FrameLayout {

    LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    private String weather, oldWeather = "";
    private String sunrise = "06:00", sunset = "18:00";
    private Context context;
    private BaseAnimView baseView;
    private int backGroundColor = R.color.clear_sky_day_start;


    public SkyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBackgroundColor(getResources().getColor(R.color.clear_sky_day_start));
        this.context = context;
    }

    public SkyView(Context context) {
        super(context);
        setBackgroundColor(getResources().getColor(R.color.clear_sky_day_start));
        this.context = context;

    }

    public SkyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(getResources().getColor(R.color.clear_sky_day_start));
        this.context = context;
    }


    public void setWeather(String weather, String sunrise, String sunset) {
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.weather = weather;
        refreshView();
    }

    public void setWeather(String weather) {
        this.weather = weather;
        refreshView();
    }

    private void refreshView() {

        if (oldWeather.equals(weather)) {
            baseView.reset();
            return;
        }
        oldWeather = weather;

        this.removeAllViews();
        if (baseView != null) {
            baseView = null;
        }

        boolean isNight = DateTimeUtil.isNight(sunrise, sunset);

        if (weather.equals("晴")) {
            backGroundColor = getResources().getColor(!isNight ?
                    R.color.clear_sky_day_start :
                    R.color.clear_sky_night_start);
            if (isNight) {
                baseView = new SunnyNightView(context, backGroundColor);
            } else {
                baseView = new SunnyDayView(context, backGroundColor);
            }
            setBackgroundColor(backGroundColor);
            addView(baseView, layoutParams);
            return;
        }
        if (weather.equals("多云")) {
            backGroundColor = getResources().getColor(!isNight ?
                    R.color.cloudy_sky_day_start :
                    R.color.cloudy_sky_night_start);
            setBackgroundColor(backGroundColor);
            baseView = new CloudyView(context, backGroundColor);
            addView(baseView, layoutParams);
            return;
        }
        if (weather.contains("雨") || weather.contains("雪")) {

            backGroundColor = getResources().getColor(!isNight ?
                    R.color.rain_sky_day_start :
                    R.color.rain_sky_night_start);
            if (weather.contains("雨") && !weather.contains("雪")) {
                baseView = new RainSnowHazeView(context, RainSnowHazeView.Type.RAIN, backGroundColor);
            } else if (!weather.contains("雨") && weather.contains("雪")) {
                baseView = new RainSnowHazeView(context, RainSnowHazeView.Type.SNOW, backGroundColor);
            } else {
                baseView = new RainSnowHazeView(context, RainSnowHazeView.Type.RAIN_SNOW, backGroundColor);
            }
            setBackgroundColor(backGroundColor);
            addView(baseView, layoutParams);
            return;
        }
        if (weather.equals("霾") || weather.equals("浮尘") || weather.equals("扬沙")) {

            backGroundColor = getResources().getColor(!isNight ?
                    R.color.haze_sky_day_start :
                    R.color.haze_sky_night_start);
            setBackgroundColor(backGroundColor);
            baseView = new RainSnowHazeView(context, RainSnowHazeView.Type.HAZE, backGroundColor);
            addView(baseView, layoutParams);
            return;
        }
        if (weather.contains("阴")) {
            backGroundColor = getResources().getColor(!isNight ?
                    R.color.overcast_sky_day_start :
                    R.color.overcast_sky_night_start);
            setBackgroundColor(backGroundColor);
            baseView = new CloudyView(context, backGroundColor);
            addView(baseView, layoutParams);
            return;
        }
        if (weather.contains("雾")) {
            backGroundColor = getResources().getColor(!isNight ?
                    R.color.fog_sky_day_start :
                    R.color.fog_sky_night_start);
            setBackgroundColor(backGroundColor);
            baseView = new FogView(context, backGroundColor);
            addView(baseView, layoutParams);
            return;
        }
    }

    public int getBackGroundColor() {
        return backGroundColor;
    }
}
