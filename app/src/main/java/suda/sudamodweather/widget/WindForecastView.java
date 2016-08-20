package suda.sudamodweather.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import suda.sudamodweather.dao.greendao.WeekForeCast;
import suda.sudamodweather.util.ScreenUtil;

/**
 * Created by ghbha on 2016/5/13.
 */
public class WindForecastView extends View {



    public WindForecastView(Context context) {
        super(context);
        this.context = context;
    }

    public WindForecastView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public WindForecastView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = ScreenUtil.getScreenWidth(context);
        height = width / 12;
        leftRight = getFitSize(30);
        setMeasuredDimension((int) width, (int) height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (foreCasts.size() == 0)
            return;

        float paddingLeft = 0;
        float widthAvg = (width - leftRight) / foreCasts.size();
        paint.setTextAlign(Paint.Align.CENTER);

        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setTextSize(ScreenUtil.getSp(context, 13));
        paint.setStrokeWidth(0);

        int i = 1;
        for (WeekForeCast weekForeCast : foreCasts) {
            paddingLeft = leftRight / 2 + (i - 1 + 0.5f) * widthAvg;
            canvas.drawText(weekForeCast.getFj(), paddingLeft, height / 2, paint);
            i++;
        }
    }

    public void setForeCasts(List<WeekForeCast> foreCasts) {
        this.foreCasts.clear();
        this.foreCasts.addAll(foreCasts);
        this.invalidate();
    }

    private float getFitSize(float orgSize) {
        return orgSize * width * 1.0f / 1080;
    }


    private final static String TAG = "WindForecastView";
    /**
     * 高度
     */
    private float height, width;
    private Paint paint = new Paint();
    private Context context;
    private List<WeekForeCast> foreCasts = new ArrayList<>();
    private float leftRight;
}
