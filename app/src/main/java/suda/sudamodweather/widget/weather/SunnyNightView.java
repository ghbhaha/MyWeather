package suda.sudamodweather.widget.weather;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.ArrayList;

import suda.sudamodweather.widget.BaseAnimView;

/**
 * Created by ghbha on 2016/5/16.
 */
public class SunnyNightView extends BaseAnimView {

    private static final int STAR_COUNT = 150; //星星数
    private Paint paint;
    private ArrayList<Star> stars;

    public SunnyNightView(Context context) {
        super(context);
    }

    @Override
    protected void init() {
        super.init();
        paint = new Paint();
        paint.setStrokeWidth(getFitSize(3));
        paint.setColor(Color.WHITE);

        stars = new ArrayList<>();
        for (int i = 0; i < STAR_COUNT; i++) {
            stars.add(new Star(windowWidth, windowHeight));
        }

    }

    @Override
    protected void drawSub(Canvas canvas) {

        for (Star star : stars) {
            paint.setAlpha(star.getCurrentAlpha());
            Float fitRadius = getFitSize(star.getRadius());
            RectF rect = new RectF(star.getX() - fitRadius, star.getY() - fitRadius,
                    star.getX() + fitRadius, star.getY() + fitRadius);
            canvas.drawArc(rect, 0, 360, false, paint);

            //虚化边缘
            paint.setAlpha(star.getCurrentAlpha() - 15);
            RectF rect2 = new RectF(star.getX() - fitRadius - 2, star.getY() - fitRadius - 2,
                    star.getX() + fitRadius + 2, star.getY() + fitRadius + 2);
            canvas.drawArc(rect2, 0, 360, false, paint);

        }
    }

    @Override
    protected void animLogic() {
        for (Star star : stars) {
            star.shine();
        }
    }

    @Override
    protected boolean needStopAnimThread() {
        return false;
    }

    @Override
    protected void onAnimEnd() {
    }

    @Override
    protected int sleepTime() {
        return 30;
    }
}
