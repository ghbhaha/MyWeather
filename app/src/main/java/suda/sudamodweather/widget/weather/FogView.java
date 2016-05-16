package suda.sudamodweather.widget.weather;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by ghbha on 2016/5/16.
 */
public class FogView extends BaseView {


    Paint paint;

    //圆最小半径
    private float MIN = getFitSize(1080);
    //圆最大半径
    private float MAX = MIN + getFitSize(80);

    //圆半径
    private float radius = MIN;

    private int deltaRadius = 1;


    public FogView(Context context) {
        super(context);
    }

    @Override
    protected void init() {
        super.init();
        paint = new Paint();
        paint.setStrokeWidth(getFitSize(3));
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);

        sleepTime = 50;
        paint.setAlpha(70);
    }

    @Override
    protected void drawSub(Canvas canvas) {
        RectF rect1 = new RectF(-radius, -radius, radius, radius);
        RectF rect2 = new RectF(windowWidth - radius, windowHeight - radius, windowWidth + radius, windowHeight + radius);

        canvas.drawArc(rect1, 0, 360, false, paint);
        canvas.drawArc(rect2, 0, 360, false, paint);
    }

    @Override
    protected void animLogic() {
        radius += deltaRadius;
    }

    @Override
    protected boolean needStopAnimThread() {
        if (radius > MAX) {
            deltaRadius = -deltaRadius;
        }
        if (radius < MIN) {
            deltaRadius = -deltaRadius;
        }

        return false;
    }

    @Override
    protected void onAnimEnd() {

    }
}
