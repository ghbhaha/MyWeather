package suda.sudamodweather.manager;

import java.util.List;

import suda.sudamodweather.dao.greendao.Aqi;
import suda.sudamodweather.dao.greendao.HourForeCast;
import suda.sudamodweather.dao.greendao.RealWeather;
import suda.sudamodweather.dao.greendao.WeekForeCast;
import suda.sudamodweather.dao.greendao.Zhishu;

/**
 * Created by ghbha on 2016/5/15.
 */
public class WeatherInfo {
    private List<WeekForeCast> weekForeCasts;
    private List<HourForeCast> hourForeCasts;
    private RealWeather realWeather;
    private Aqi aqi;
    private List<Zhishu> zhishu;

    public List<WeekForeCast> getWeekForeCasts() {
        return weekForeCasts;
    }

    public void setWeekForeCasts(List<WeekForeCast> weekForeCasts) {
        this.weekForeCasts = weekForeCasts;
    }

    public List<HourForeCast> getHourForeCasts() {
        return hourForeCasts;
    }

    public void setHourForeCasts(List<HourForeCast> hourForeCasts) {
        this.hourForeCasts = hourForeCasts;
    }

    public RealWeather getRealWeather() {
        return realWeather;
    }

    public void setRealWeather(RealWeather realWeather) {
        this.realWeather = realWeather;
    }

    public Aqi getAqi() {
        return aqi;
    }

    public void setAqi(Aqi aqi) {
        this.aqi = aqi;
    }


    public List<Zhishu> getZhishu() {
        return zhishu;
    }

    public void setZhishu(List<Zhishu> zhishu) {
        this.zhishu = zhishu;
    }
}
