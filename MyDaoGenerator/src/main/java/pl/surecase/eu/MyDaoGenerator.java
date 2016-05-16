package pl.surecase.eu;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MyDaoGenerator {

    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(2, "suda.sudamodweather.dao.greendao");
        addForeCast(schema);
        addRealWeather(schema);
        addHourForeCast(schema);
        addAqi(schema);
        addZhishu(schema);
        addUseArea(schema);
        new DaoGenerator().generateAll(schema, "app/src/main/java");
    }

    private static void addRealWeather(Schema schema) {
        Entity realWeather = schema.addEntity("RealWeather");
        realWeather.addStringProperty("areaid");
        realWeather.addStringProperty("areaName");
        realWeather.addStringProperty("weatherCondition");
        realWeather.addStringProperty("fx");
        realWeather.addStringProperty("fj");
        realWeather.addIntProperty("temp");
        realWeather.addIntProperty("feeltemp");
        realWeather.addIntProperty("shidu");
        realWeather.addStringProperty("sunrise");
        realWeather.addStringProperty("sundown");
        realWeather.addDateProperty("lastUpdate");
    }

    private static void addForeCast(Schema schema) {
        Entity weekForeCast = schema.addEntity("WeekForeCast");
        weekForeCast.addStringProperty("areaid");
        weekForeCast.addDateProperty("weatherDate");
        weekForeCast.addStringProperty("weatherConditionStart");
        weekForeCast.addStringProperty("weatherConditionEnd");
        weekForeCast.addIntProperty("tempH");
        weekForeCast.addIntProperty("tempL");
        weekForeCast.addStringProperty("fx");
        weekForeCast.addStringProperty("fj");
        weekForeCast.addIntProperty("rainPerCent");
    }

    private static void addHourForeCast(Schema schema) {
        Entity weekForeCast = schema.addEntity("HourForeCast");
        weekForeCast.addStringProperty("areaid");
        weekForeCast.addStringProperty("hour");
        weekForeCast.addStringProperty("weatherCondition");
        weekForeCast.addIntProperty("temp");
    }

    private static void addAqi(Schema schema) {
        Entity weekForeCast = schema.addEntity("Aqi");
        weekForeCast.addStringProperty("areaid");
        weekForeCast.addIntProperty("aqi");
        weekForeCast.addStringProperty("quality");
        weekForeCast.addIntProperty("pm2_5");
        weekForeCast.addIntProperty("pm10");
        weekForeCast.addIntProperty("so2");
        weekForeCast.addIntProperty("no2");
    }

    private static void addZhishu(Schema schema) {
        Entity weekForeCast = schema.addEntity("Zhishu");
        weekForeCast.addStringProperty("areaid");
        weekForeCast.addStringProperty("name");
        weekForeCast.addStringProperty("level");
        weekForeCast.addStringProperty("text");
        weekForeCast.addStringProperty("detail");
    }

    private static void addUseArea(Schema schema) {
        Entity weekForeCast = schema.addEntity("UseArea");
        weekForeCast.addStringProperty("areaid");
        weekForeCast.addStringProperty("areaid2345");
        weekForeCast.addStringProperty("areaName");
        weekForeCast.addBooleanProperty("main");
    }

}
