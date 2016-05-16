package suda.sudamodweather.widget.weather;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by AZZ on 15/10/20 21:20.
 */
public abstract class BaseView extends View {

    protected AnimThread animThread;
    protected int windowWidth; //屏幕宽
    protected int windowHeight; //屏幕高
    protected int sleepTime = 30;
    private OnAnimEndListener mOnAnimEndListener;

    public BaseView(Context context) {
        super(context);
        init();
    }

    public BaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    //----------------------------------------------------    画布操作

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
    //----------------------------------------------------    动画操作

    @Override
    protected void onDraw(Canvas canvas) {
        drawSub(canvas);
        if (animThread == null) {
            animThread = new AnimThread();
            animThread.start();
        }
    }

    /**
     * 动画逻辑处理
     */
    protected abstract void animLogic();

    /**
     * 里面根据当前状态判断是否需要返回停止动画
     *
     * @return 是否需要停止动画thread
     */
    protected abstract boolean needStopAnimThread();

    /**
     * @return 线程睡眠时间，值越大，动画越慢，值越小，动画越快
     */
    protected int sleepTime() {
        return sleepTime;
    }

    /**
     * 动画结束后做的操作，比如回收资源
     */
    protected abstract void onAnimEnd();

    //----------------------------------------------------    外部监听器，监听动画结束

    /**
     * @param onAnimEndListener 设置滚动结束监听器
     */
    public void setOnRollEndListener(OnAnimEndListener onAnimEndListener) {
        this.mOnAnimEndListener = onAnimEndListener;
    }

    protected float getFitSize(float size) {
        return size * windowWidth / 1080;
    }

    /**
     * 滚动结束接听器
     */
    interface OnAnimEndListener {
        void onAnimEnd();
    }

    class AnimThread extends Thread {
        @Override
        public void run() {
            while (true) {
                //1.动画逻辑
                animLogic();
                //2.绘制图像
                postInvalidate();
                //3.延迟，不然会造成执行太快动画一闪而过
                try {
                    Thread.sleep(sleepTime());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //关闭线程逻辑判断
                if (needStopAnimThread()) {
                    Log.i("BaseView", "   -线程停止！");

                    if (mOnAnimEndListener != null) {
                        mOnAnimEndListener.onAnimEnd();
                    }
                    onAnimEnd();
                    break;
                }
            }
        }
    }
}