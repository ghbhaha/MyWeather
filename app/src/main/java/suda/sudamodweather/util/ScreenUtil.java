package suda.sudamodweather.util;

import android.content.Context;
import android.util.TypedValue;
import android.view.WindowManager;


public final class ScreenUtil {

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

}
