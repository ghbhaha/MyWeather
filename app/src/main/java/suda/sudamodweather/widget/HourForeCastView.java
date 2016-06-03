package suda.sudamodweather.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import suda.sudamodweather.dao.greendao.HourForeCast;
import suda.sudamodweather.util.ScreenUtil;

/**
 * Created by ghbha on 2016/5/13.
 */
public class HourForeCastView extends View {

    private final static String TAG = "HourForeCastView";
    Paint paint = new Paint();
    float widthAvg;
    private float height, width;
    private List<HourForeCast> hourForeCasts = new ArrayList<>();
    private int tempH, tempL;
    private Context context;
    private float radius = 0;
    private float leftRight = 0;

    public HourForeCastView(Context context) {
        super(context);
        this.context = context;
    }

    public HourForeCastView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public HourForeCastView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = ScreenUtil.getScreenWidth(context);
        height = width / 2 - getFitSize(20);
        widthAvg = getFitSize(200);
        radius = getFitSize(8);
        setMeasuredDimension((int) leftRight + (int) widthAvg * 25, (int) height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (hourForeCasts.size() == 0)
            return;
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(0);
        paint.setTextSize(ScreenUtil.getSp(context, 13));
        paint.setTextAlign(Paint.Align.CENTER);

        float weatherDetallPadding = getFitSize(50);
        float weatherTimePadding = getFitSize(100);
        float linePaddingBottom = getFitSize(200);
        float lineHigh = getFitSize(180);

        float lineAvg = lineHigh / getMaxMinDelta();

        float paddingLeft = 0;

        //解决path过大无法绘制，分成三段
        Path tempPath = new Path();
        Path tempPath2 = new Path();
        Path tempPath3 = new Path();

        int i = 1;
        for (HourForeCast foreCast : hourForeCasts) {
            paddingLeft = leftRight / 2 + (i - 1 + 0.5f) * widthAvg;

            if (i == 1) {
                tempPath.moveTo(paddingLeft, height - (linePaddingBottom + (foreCast.getTemp() - tempL) * lineAvg));
            } else if (i > 1 && i <= 10) {
                tempPath.lineTo(paddingLeft, height - (linePaddingBottom + (foreCast.getTemp() - tempL) * lineAvg));
                tempPath2.moveTo(paddingLeft, height - (linePaddingBottom + (foreCast.getTemp() - tempL) * lineAvg));
            } else if (i > 10 && i <= 20) {
                tempPath2.lineTo(paddingLeft, height - (linePaddingBottom + (foreCast.getTemp() - tempL) * lineAvg));
                tempPath3.moveTo(paddingLeft, height - (linePaddingBottom + (foreCast.getTemp() - tempL) * lineAvg));
            } else {
                tempPath3.lineTo(paddingLeft, height - (linePaddingBottom + (foreCast.getTemp() - tempL) * lineAvg));
            }
            paint.setStyle(Paint.Style.FILL);
            paint.setStrokeWidth(getFitSize(2));
            canvas.drawCircle(paddingLeft, height - (linePaddingBottom + (foreCast.getTemp() - tempL) * lineAvg), radius, paint);
            paint.setStrokeWidth(0);
            paint.setStyle(Paint.Style.STROKE);

            canvas.drawText(foreCast.getTemp() + "°", paddingLeft, height - (getFitSize(20) + linePaddingBottom + (foreCast.getTemp() - tempL) * lineAvg), paint);

            //文字
            canvas.drawText(foreCast.getWeatherCondition(), paddingLeft, height - weatherDetallPadding, paint);
            canvas.drawText(foreCast.getHour(), paddingLeft, height - weatherTimePadding, paint);
            i++;
        }
        paint.setStrokeWidth(getFitSize(3));
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(tempPath, paint);
        canvas.drawPath(tempPath2, paint);
        canvas.drawPath(tempPath3, paint);
    }


    private int getMaxMinDelta() {
        if (hourForeCasts.size() > 0) {
            tempL = hourForeCasts.get(0).getTemp();
            tempH = hourForeCasts.get(0).getTemp();
            for (HourForeCast hourForeCast : hourForeCasts) {
                if (hourForeCast.getTemp() > tempH) {
                    tempH = hourForeCast.getTemp();
                }
                if (hourForeCast.getTemp() < tempL) {
                    tempL = hourForeCast.getTemp();
                }
            }
            return tempH - tempL;
        }
        return 0;
    }

    public void setHourForeCasts(List<HourForeCast> hourForeCasts) {
        this.hourForeCasts.clear();
        this.hourForeCasts.addAll(hourForeCasts);
        this.invalidate();
    }

    private float getFitSize(float orgSize) {
        return orgSize * width * 1.0f / 1080;
    }

}
