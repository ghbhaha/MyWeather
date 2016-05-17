package suda.sudamodweather.widget.weather;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import java.util.ArrayList;

import suda.sudamodweather.widget.BaseAnimView;

/**
 * Created by ghbha on 2016/5/16.
 */
public class RainSnowHazeView extends BaseAnimView {


    private static final int RAIN_COUNT = 100; //雨点个数
    private ArrayList<BaseLine> rainLines;
    private Paint paint;
    private Type type = Type.RAIN;

    public RainSnowHazeView(Context context, Type type) {
        super(context);
        this.type = type;
        init2();
    }

    public RainSnowHazeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 初始化
     */

    protected void init2() {
        rainLines = new ArrayList<>();
        switch (type) {
            case RAIN_SNOW:
            case SNOW:
            case RAIN:
                for (int i = 0; i < RAIN_COUNT; i++) {
                    rainLines.add(new RainOrSnowLine(windowWidth, windowHeight));
                }
                break;
            case HAZE:
                for (int i = 0; i < RAIN_COUNT; i++) {
                    rainLines.add(new HazeLine(windowWidth, windowHeight));
                }
                break;
            default:
                for (int i = 0; i < RAIN_COUNT; i++) {
                    rainLines.add(new RainOrSnowLine(windowWidth, windowHeight));
                }
                break;
        }

        paint = new Paint();
        paint.setStrokeWidth(3);
        if (paint != null) {
            paint.setColor(Color.WHITE);
        }

    }

    /**
     * 画子类
     *
     * @param canvas
     */
    @Override
    protected void drawSub(Canvas canvas) {
        boolean rain = true;
        for (BaseLine rainLine : rainLines) {

            paint.setAlpha(rainLine.getAlpha());
            if (type == Type.HAZE) {
                RectF rect1 = new RectF(rainLine.getStartX() - getFitSize(5), rainLine.getStartY() - getFitSize(5),
                        rainLine.getStartX() + getFitSize(5), rainLine.getStartY() + getFitSize(5));
                canvas.drawArc(rect1, 0, 360, false, paint);
            } else if (type == Type.SNOW) {
                RectF rect3 = new RectF(rainLine.getStartX() - getFitSize(5), rainLine.getStartY() - getFitSize(5),
                        rainLine.getStartX() + getFitSize(5), rainLine.getStartY() + getFitSize(5));
                canvas.drawArc(rect3, 0, 360, false, paint);
            } else {
                if (type == Type.RAIN_SNOW)
                    rain = !rain;
                if (rain)
                    canvas.drawLine(rainLine.getStartX(), rainLine.getStartY(), rainLine.getStopX(), rainLine.getStopY() + getFitSize(5), paint);
                else {
                    RectF rect3 = new RectF(rainLine.getStartX() - getFitSize(5), rainLine.getStartY() - getFitSize(5), rainLine.getStartX() + getFitSize(5), rainLine.getStartY() + getFitSize(5));
                    canvas.drawArc(rect3, 0, 360, false, paint);
                }
            }
        }

    }

    /**
     * 动画逻辑处理
     */
    @Override
    protected void animLogic() {
        for (BaseLine rainLine : rainLines) {
            rainLine.rain();
        }
    }

    /**
     * 里面根据当前状态判断是否需要返回停止动画
     *
     * @return 是否需要停止动画thread
     */
    @Override
    protected boolean needStopAnimThread() {
        return false;
    }

    /**
     * 动画结束后做的操作，比如回收资源
     */
    @Override
    protected void onAnimEnd() {
    }

    public enum Type {
        RAIN, SNOW, RAIN_SNOW, HAZE;
    }

    @Override
    protected int sleepTime() {
        return 10;
    }

}