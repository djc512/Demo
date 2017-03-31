package huanxing_print.com.cn.printhome.view.viewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2017/3/31 0031.
 */

public class MyViewPager extends ViewPager {

    private float downX;
    private float upX;
    private float moveX;

    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                moveX = ev.getX();
                if (Math.abs(moveX - downX) > 10) {
                    downX = moveX;
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                upX = ev.getX();
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }
}
