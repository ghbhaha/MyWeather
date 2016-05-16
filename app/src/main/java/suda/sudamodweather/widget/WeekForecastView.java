package suda.sudamodweather.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import suda.sudamodweather.dao.greendao.WeekForeCast;
import suda.sudamodweather.util.DateTimeUtil;
import suda.sudamodweather.util.ScreenUtil;
import suda.sudamodweather.util.WeatherIconUtil;

/**
 * Created by ghbha on 2016/5/13.
 */
public class WeekForecastView extends View {


    private final static String TAG = "ForeCastView";
    /**
     * 高度
     */
    private float height, width;
    private Paint paint = new Paint();

    private Context context;

    private List<WeekForeCast> foreCasts = new ArrayList<>();

    private float maxMinDelta;

    private int tempH, tempL;


    private float radius = 0;

    private float leftRight;


    public WeekForecastView(Context context) {
        super(context);

        this.context = context;
    }

    public WeekForecastView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public WeekForecastView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = ScreenUtil.getScreenWidth(context);
        height = width - getFitSize(20);
        leftRight = getFitSize(30);
        radius = getFitSize(8);
        setMeasuredDimension((int) width, (int) height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (foreCasts.size() == 0)
            return;

        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(0);

        drawWeatherDetail(canvas);

    }

    private void drawWeatherDetail(Canvas canvas) {

        paint.setTextAlign(Paint.Align.CENTER);
        //获取每个天气所占空间
        float widthavg = (width - leftRight) / foreCasts.size();

        float paddingLeft = 0;

        float weekPaddingBottom = getFitSize(200);
        float weekInfoPaddingBottom = getFitSize(40);
        float linePaddingBottom = getFitSize(330);
        float lineHigh = getFitSize(320);
        float tempPaddingTop = getFitSize(20);
        float tempPaddingBottom = getFitSize(45);

        Matrix matrix = new Matrix();
        matrix.postScale(0.45f, 0.45f); //长和宽放大缩小的比例
        paint.setTextSize(getFitSize(35));
        int i = 1;
        Path pathTempHigh = new Path();
        Path pathTempLow = new Path();

        float lineAvg = lineHigh / maxMinDelta;

        for (WeekForeCast foreCast : foreCasts) {

            paddingLeft = leftRight / 2 + (i - 1 + 0.5f) * widthavg;
            //  Log.d(TAG, "padding" + paddingLeft + "");

            if (i == 1) {
                pathTempHigh.moveTo(paddingLeft, height - (linePaddingBottom + (foreCast.getTempH() - tempL) * lineAvg));
                pathTempLow.moveTo(paddingLeft, height - (linePaddingBottom + (foreCast.getTempL() - tempL) * lineAvg));
            } else {
                pathTempHigh.lineTo(paddingLeft, height - (linePaddingBottom + (foreCast.getTempH() - tempL) * lineAvg));
                pathTempLow.lineTo(paddingLeft, height - (linePaddingBottom + (foreCast.getTempL() - tempL) * lineAvg));
            }

            paint.setStyle(Paint.Style.FILL);
            paint.setStrokeWidth(getFitSize(2));
            canvas.drawCircle(paddingLeft, height - (linePaddingBottom + (foreCast.getTempH() - tempL) * lineAvg), radius, paint);
            canvas.drawCircle(paddingLeft, height - (linePaddingBottom + (foreCast.getTempL() - tempL) * lineAvg), radius, paint);
            paint.setStrokeWidth(0);
            paint.setStyle(Paint.Style.STROKE);

            canvas.drawText(foreCast.getTempH() + "°", paddingLeft, height - (linePaddingBottom + tempPaddingTop + (foreCast.getTempH() - tempL) * lineAvg), paint);
            canvas.drawText(foreCast.getTempL() + "°", paddingLeft, height - (linePaddingBottom - tempPaddingBottom + (foreCast.getTempL() - tempL) * lineAvg), paint);

            //星期
            canvas.drawText(DateTimeUtil.getWeekOfDate(foreCast.getWeatherDate()), paddingLeft, height - getFitSize(weekPaddingBottom), paint);
            canvas.drawText(DateTimeUtil.getWeekOfDate(foreCast.getWeatherDate()), paddingLeft, height - getFitSize(weekPaddingBottom), paint);

            //天气图标
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), WeatherIconUtil.getWeatherIconID(foreCast.getWeatherConditionStart()));
            Bitmap bitmapDisplay = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

            canvas.drawBitmap(bitmapDisplay,
                    paddingLeft - bitmapDisplay.getWidth() / 2, height - getFitSize(8) - getFitSize((weekPaddingBottom - weekInfoPaddingBottom) / 2 + weekInfoPaddingBottom) - bitmapDisplay.getHeight() / 2, paint);
            bitmap.recycle();
            bitmapDisplay.recycle();
            //canvas.drawText(DateTimeUtil.getWeekOfDate(foreCast.getWeatherDate()), paddingLeft, height - getFitSize(140), paint);
            //天气描述
            canvas.drawText(foreCast.getWeatherConditionStart(), paddingLeft, height - getFitSize(weekInfoPaddingBottom), paint);
            i++;
        }
        paint.setStrokeWidth(getFitSize(3));
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(pathTempHigh, paint);
        canvas.drawPath(pathTempLow, paint);
    }


    private int getMaxMinDelta() {
        if (foreCasts.size() > 0) {
            tempH = foreCasts.get(0).getTempH();
            tempL = foreCasts.get(0).getTempL();
            for (WeekForeCast weekForeCast : foreCasts) {
                if (weekForeCast.getTempH() > tempH) {
                    tempH = weekForeCast.getTempH();
                }
                if (weekForeCast.getTempL() < tempL) {
                    tempL = weekForeCast.getTempL();
                }
            }
            // Log.d(TAG, "max - min" + (tempH - tempL));
            return tempH - tempL;
        }
        return 0;
    }


    public void setForeCasts(List<WeekForeCast> foreCasts) {
        this.foreCasts.clear();
        this.foreCasts.addAll(foreCasts);
        maxMinDelta = getMaxMinDelta();
        this.invalidate();
    }

    private float getFitSize(float orgSize) {
        return orgSize * width * 1.0f / 1080;
    }

}
