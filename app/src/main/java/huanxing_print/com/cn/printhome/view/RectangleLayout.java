package huanxing_print.com.cn.printhome.view;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by LGH on 2017/4/27.
 */

public class RectangleLayout extends android.widget.FrameLayout {
    public RectangleLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public RectangleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RectangleLayout(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
        int childWidthSize = getMeasuredWidth();
        heightMeasureSpec = widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}