package suda.sudamodweather.dao;

import android.content.Context;

import java.util.List;

import suda.sudamodweather.dao.greendao.Aqi;
import suda.sudamodweather.dao.greendao.AqiDao;
import suda.sudamodweather.dao.greendao.HourForeCast;
import suda.sudamodweather.dao.greendao.HourForeCastDao;
import suda.sudamodweather.dao.greendao.RealWeather;
import suda.sudamodweather.dao.greendao.RealWeatherDao;
import suda.sudamodweather.dao.greendao.UseArea;
import suda.sudamodweather.dao.greendao.UseAreaDao;
import suda.sudamodweather.dao.greendao.WeekForeCast;
import suda.sudamodweather.dao.greendao.WeekForeCastDao;
import suda.sudamodweather.dao.greendao.Zhishu;
import suda.sudamodweather.dao.greendao.ZhishuDao;

/**
 * Created by ghbha on 2016/5/14.
 */
public class WeatherDao extends BaseLocalDao {

    public void insertRealweather(Context context, RealWeather realWeather) {
        RealWeatherDao realWeatherDao = getDaoSession(context).getRealWeatherDao();
        realWeatherDao.insert(realWeather);
    }

    public void delRealweatherByAreaId(Context context, String areaId) {
        RealWeatherDao realWeatherDao = getDaoSession(context).getRealWeatherDao();
        realWeatherDao.queryBuilder().where(RealWeatherDao.Properties.Areaid.eq(areaId)).buildDelete().executeDeleteWithoutDetachingEntities();
    }

    public RealWeather queryRealweatherByAreaId(Context context, String areaId) {
        RealWeatherDao realWeatherDao = getDaoSession(context).getRealWeatherDao();
        return getSingleData(realWeatherDao.queryBuilder().where(RealWeatherDao.Properties.Areaid.eq(areaId)).list());
    }

    public void insertWeekForeCast(Context context, List<WeekForeCast> weekForeCasts) {
        WeekForeCastDao weekForeCastDao = getDaoSession(context).getWeekForeCastDao();
        weekForeCastDao.insertInTx(weekForeCasts);
    }

    public List<WeekForeCast> queryWeekForeCastByAreaId(Context context, String areaId) {
        WeekForeCastDao weekForeCastDao = getDaoSession(context).getWeekForeCastDao();
        return weekForeCastDao.queryBuilder().where(WeekForeCastDao.Properties.Areaid.eq(areaId)).list();
    }

    public void delWeekForeCastByAreaId(Context context, String areaId) {
        WeekForeCastDao weekForeCastDao = getDaoSession(context).getWeekForeCastDao();
        weekForeCastDao.queryBuilder().where(WeekForeCastDao.Properties.Areaid.eq(areaId)).buildDelete().executeDeleteWithoutDetachingEntities();
    }

    public void insertHourForeCast(Context context, List<HourForeCast> hourForeCasts) {
        HourForeCastDao hourForeCastDao = getDaoSession(context).getHourForeCastDao();
        hourForeCastDao.insertInTx(hourForeCasts);
    }

    public void delHourForeCastByAreaId(Context context, String areaId) {
        HourForeCastDao hourForeCastDao = getDaoSession(context).getHourForeCastDao();
        hourForeCastDao.queryBuilder().where(HourForeCastDao.Properties.Areaid.eq(areaId)).buildDelete().executeDeleteWithoutDetachingEntities();
    }

    public List<HourForeCast> queryHourForeCastByAreaId(Context context, String areaId) {
        HourForeCastDao hourForeCastDao = getDaoSession(context).getHourForeCastDao();
        return hourForeCastDao.queryBuilder().where(HourForeCastDao.Properties.Areaid.eq(areaId)).list();
    }


    public void delAqiByAreaId(Context context, String areaId) {
        AqiDao aqiDao = getDaoSession(context).getAqiDao();
        aqiDao.queryBuilder().where(AqiDao.Properties.Areaid.eq(areaId)).buildDelete().executeDeleteWithoutDetachingEntities();
    }

    public void insertAqi(Context context, Aqi aqi) {
        AqiDao aqiDao = getDaoSession(context).getAqiDao();
        aqiDao.insert(aqi);
    }

    public Aqi queryAqiByAreaId(Context context, String areaId) {
        AqiDao aqiDao = getDaoSession(context).getAqiDao();
        return getSingleData(aqiDao.queryBuilder().where(AqiDao.Properties.Areaid.eq(areaId)).list());
    }

    public void delZhishuByAreaId(Context context, String areaId) {
        ZhishuDao zhishuDao = getDaoSession(context).getZhishuDao();
        zhishuDao.queryBuilder().where(ZhishuDao.Properties.Areaid.eq(areaId)).buildDelete().executeDeleteWithoutDetachingEntities();
    }

    public void insertZhishu(Context context, List<Zhishu> zhishus) {
        ZhishuDao zhishuDao = getDaoSession(context).getZhishuDao();
        zhishuDao.insertInTx(zhishus);
    }

    public List<Zhishu> queryZhishuByAreaId(Context context, String areaId) {
        ZhishuDao zhishuDao = getDaoSession(context).getZhishuDao();
        return zhishuDao.queryBuilder().where(ZhishuDao.Properties.Areaid.eq(areaId)).list();
    }


    public void insetNewUseArea(Context context, UseArea useArea) {
        UseAreaDao useAreaDao = getDaoSession(context).getUseAreaDao();
        if (useArea.getMain()) {
            List<UseArea> list = useAreaDao.queryBuilder().list();
            for (UseArea useArea1 : list) {
                useArea1.setMain(false);
            }
            if (list.size() > 0)
                useAreaDao.updateInTx(list);
        }
        useAreaDao.insert(useArea);
    }

    public UseArea queryMainUseArea(Context context) {
        UseAreaDao useAreaDao = getDaoSession(context).getUseAreaDao();
        return getSingleData(useAreaDao.queryBuilder().where(UseAreaDao.Properties.Main.eq(true)).list());
    }

}
