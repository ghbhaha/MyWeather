package suda.sudamodweather.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by ghbha on 2016/5/14.
 */
public class WindmillView extends View {

    Paint paint = new Paint();
    float radius;
    private Thread animThread;
    private float height, width;
    private float degree = 0;
    private float windSpeedDegree = 2f;

    public WindmillView(Context context) {
        super(context);
        init();
    }

    public WindmillView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WindmillView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        height = MeasureSpec.getSize(heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        if (width > height) {
            height = width;
        } else {
            width = height;
        }

        width = width / 2;
        setMeasuredDimension((int) width, (int) height);
        radius = getFitSize(45);
    }

    private void init() {
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(4);
    }


    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        //绘制支架
        Path path = new Path();
        paint.setStyle(Paint.Style.STROKE);
        path.moveTo(getFitSize(180), height);
        path.lineTo(width / 2, height / 2 - getFitSize(45));
        path.lineTo(width - getFitSize(180), height);
        canvas.drawPath(path, paint);

        //绘制风车最下面的叶子
        final Path path1 = new Path();
        paint.setStyle(Paint.Style.FILL);


        path1.moveTo(width / 2 - getFitSize(radius), height / 2 - getFitSize(15));
        path1.cubicTo(width / 2 - getFitSize(radius), height / 2 - getFitSize(15),
                width / 2, height / 2 - getFitSize(40), width / 2 + getFitSize(radius), height / 2 - getFitSize(15));

        path1.cubicTo(width / 2 + getFitSize(radius), height / 2 - getFitSize(15),
                width / 2, height / 2 + getFitSize(400), width / 2 - getFitSize(radius), height / 2 - getFitSize(10));

        path1.close();

        canvas.rotate(degree, width / 2, height / 2 - radius);
        canvas.drawPath(path1, paint);

        //绘制剩余叶子
        canvas.rotate(120, width / 2, height / 2 - radius);
        canvas.drawPath(path1, paint);
        canvas.rotate(120, width / 2, height / 2 - radius);
        canvas.drawPath(path1, paint);
        degree += windSpeedDegree * 1.5f;
        if (degree >= 360)
            degree = degree - 360;
    }

    public void startAnim() {
        if (animThread == null) {
            animThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        postInvalidate();
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
        animThread.start();
    }

    public void refreshView() {
        postInvalidate();
    }

    public void setWindSpeedDegree(float windSpeedDegree) {
        if (windSpeedDegree == 0)
            windSpeedDegree = 1;
        this.windSpeedDegree = windSpeedDegree;
    }

    private float getFitSize(float orgSize) {
        return orgSize * width / 496;
    }
}
