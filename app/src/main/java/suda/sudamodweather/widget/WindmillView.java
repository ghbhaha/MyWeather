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

    private Paint paint;
    private Thread animThread;
    private float height, width;
    private float degree = 0;
    private float windSpeedDegree = 2f;

    //圆心
    private float centerX, centerY;
    //构成最下面扇子的4个点
    float x1, y1, x2, y2, x3, y3, x4, y4;

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

        measure();
        setMeasuredDimension((int) width, (int) height);
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(4);
    }

    private void measure() {
        x1 = width / 2 - getFitSize(20);
        y1 = height / 2 - getFitSize(15);
        x2 = width / 2;
        y2 = height / 2 - getFitSize(50);
        x3 = width / 2 + getFitSize(20);
        y3 = y1;
        x4 = x2;
        y4 = height / 2 + getFitSize(400);
        centerX = width / 2;
        centerY = height / 2 - getFitSize(50);
    }


    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);

        drawHolder(canvas);
        drawFan(canvas);

        degree += windSpeedDegree * 1.5f;
        if (degree >= 360)
            degree = degree - 360;
    }

    /**
     * 绘制支架
     *
     * @param canvas
     */
    private void drawHolder(Canvas canvas) {
        Path path = new Path();
        paint.setStyle(Paint.Style.STROKE);
        path.moveTo(getFitSize(180), height);
        path.lineTo(centerX, centerY);
        path.lineTo(width - getFitSize(180), height);
        canvas.drawPath(path, paint);
    }

    /**
     * 绘制风车扇子
     */
    private void drawFan(Canvas canvas) {
        final Path path = new Path();
        paint.setStyle(Paint.Style.FILL);

        path.moveTo(x1, y1);
        path.cubicTo(x1, y1, x2, y2, x3, y3);
        path.cubicTo(x3, y3, x4, y4, x1, y1);
        path.close();

        canvas.rotate(degree, centerX, centerY);
        canvas.drawPath(path, paint);
        canvas.rotate(120, centerX, centerY);
        canvas.drawPath(path, paint);
        canvas.rotate(120, centerX, centerY);
        canvas.drawPath(path, paint);
    }


    public void startAnim() {
        if (animThread == null) {
            animThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        refreshView();
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
