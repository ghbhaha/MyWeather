package suda.sudamodweather.widget.weather;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public abstract class BaseAnimView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    protected Thread thread;
    protected int backColor = 0;
    protected int windowWidth; //屏幕宽
    protected int windowHeight; //屏幕高
    protected int sleepTime = 30;
    protected SurfaceHolder holder;

    public BaseAnimView(Context context) {
        super(context);
        holder = getHolder();
        holder.addCallback(this);
        setZOrderOnTop(true);
        holder.setFormat(PixelFormat.TRANSLUCENT); // 顶层绘制SurfaceView设成透明
        init();
    }

    /**
     * 初始化
     */
    protected void init() {
        Rect rect = new Rect();
        getWindowVisibleDisplayFrame(rect);
        windowWidth = rect.width();
        windowHeight = rect.height();
    }

    /**
     * 画子类
     */
    protected abstract void drawSub(Canvas canvas);

    /**
     * 动画逻辑处理
     */
    protected abstract void animLogic();

    /**
     * @return 线程睡眠时间，值越大，动画越慢，值越小，动画越快
     */
    protected int sleepTime() {
        return sleepTime;
    }


    protected float getFitSize(float size) {
        return size * windowWidth / 1080;
    }

    protected void startAnim() {
        if (thread != null) {
            thread = null;
        }
        thread = new Thread(this);
        thread.start();
    }

    protected void doLogic() {
        while (true) {
            Canvas canvas = null;
            synchronized (this) {
                canvas = holder.lockCanvas();
                if (canvas != null) {
                    canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                    drawSub(canvas);
                    animLogic();
                    holder.unlockCanvasAndPost(canvas);
                }

                try {
                    Thread.sleep(sleepTime());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}