package suda.sudamodweather;

import android.app.Application;

import suda.sudamodweather.util.AssetsCopyUtil;

/**
 * Created by ghbha on 2016/5/14.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AssetsCopyUtil.copyEmbassy2Databases(this, "data/data/" + this.getPackageName() + "/databases/",
                "location.db");
    }
}
