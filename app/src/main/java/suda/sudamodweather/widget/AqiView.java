package suda.sudamodweather.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by ghbha on 2016/5/14.
 */
public class AqiView extends View {

    Paint paint = new Paint();
    private float height, width;
    private int progress = 0;
    private String label = "";

    public AqiView(Context context) {
        super(context);
    }

    public AqiView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AqiView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = width * 550 / 992;
        setMeasuredDimension((int) width, (int) (height + getFitSize(100)));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setAntiAlias(true);                       //设置画笔为无锯齿
        paint.setColor(Color.WHITE);                    //设置画笔颜色
        paint.setStrokeWidth(getFitSize(60));              //线宽
        paint.setStyle(Paint.Style.STROKE);

        //外围虚线
        float center = width / 2;
        float radius = height - getFitSize(100);
        RectF rect = new RectF(center - radius + getFitSize(40), center - radius, center
                + radius - getFitSize(40), center + radius - getFitSize(40));
        paint.setAlpha(100);
        canvas.drawArc(rect, 180, 180, false, paint);

        //进度实线
        paint.setAlpha(255);
        canvas.drawArc(rect, 180, (180 * progress * 1.0f / 500), false, paint);

        //文字描述
        paint.setStrokeWidth((float) 1.0);
        paint.setTextSize(getFitSize(120));
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(progress + "", center, radius + getFitSize(30), paint);
        paint.setTextSize(getFitSize(70));
        canvas.drawText(label, rect.centerX(), radius + getFitSize(180), paint);
    }

    private float getFitSize(float orgSize) {
        return orgSize * width / 992;
    }


    public void setProgressAndLabel(int progress, String label) {
        this.progress = progress;
        this.label = label;
        this.invalidate();
    }

}
