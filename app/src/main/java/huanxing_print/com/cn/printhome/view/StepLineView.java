package huanxing_print.com.cn.printhome.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.util.DisplayUtil;

/**
 * Created by LGH on 2017/4/26.
 */

public class StepLineView extends View {

    private int step = 0;

    public static final int STEP_DEFAULT = 0;
    public static final int STEP_PICK_FILE = 1;
    public static final int STEP_PICK_PRINTER = 2;
    public static final int STEP_PAY = 3;

    private final int RADIUS = 10;
    private final int STROKE_WHITH = 2;
    private Context context;

    public StepLineView(Context context, int step) {
        super(context);
        this.context = context;
        this.step = step;
    }

    public StepLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public StepLineView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        // 在wrap_content的情况下默认长度为20dp
        int minSize = DisplayUtil.dip2px(getContext(), 15);
        // wrap_content的specMode是AT_MOST模式，这种情况下宽/高等同于specSize
        // 查表得这种情况下specSize等同于parentSize，也就是父容器当前剩余的大小
        // 在wrap_content的情况下如果特殊处理，效果等同martch_parent
        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(minSize, minSize);
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(minSize, heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, minSize);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        final int paddingLeft = getPaddingLeft();
        final int paddingTop = getPaddingTop();
        final int paddingRight = getPaddingRight();
        final int paddingBottom = getPaddingBottom();

        final int width = getMeasuredWidth();
        final int height = getMeasuredHeight();

        final int dis = (width - paddingLeft - paddingRight) / 4;

        final int pointX1 = 0 + paddingLeft;
        final int pointY1 = height / 2;
        final int pointX2 = pointX1 + dis;
        final int pointY2 = height / 2;
        final int pointX3 = pointX2 + dis;
        final int pointY3 = height / 2;
        final int pointX4 = pointX3 + dis;
        final int pointY4 = height / 2;
        final int pointX5 = pointX4 + dis;
        final int pointY5 = height / 2;

        Paint p = new Paint();
        p.setStrokeWidth(STROKE_WHITH);
        if (step == STEP_DEFAULT) {
            p.setColor(Color.BLACK);
            p.setStyle(Paint.Style.FILL);
            p.setAntiAlias(true);
            canvas.drawLine(pointX1, pointY1, pointX5, pointY5, p);
            canvas.drawCircle(pointX1, pointY1, RADIUS, p);
            canvas.drawCircle(pointX3, pointY3, RADIUS, p);
            canvas.drawCircle(pointX5, pointY5, RADIUS, p);
        }
        if (step == STEP_PICK_FILE) {
            p.setColor(ContextCompat.getColor(context, R.color.stepline_red));
            p.setStyle(Paint.Style.FILL);
            p.setAntiAlias(true);
            canvas.drawLine(pointX1, pointY1, pointX2, pointY2, p);
            canvas.drawCircle(pointX1, pointY1, RADIUS, p);

            p.setStyle(Paint.Style.STROKE);
            p.setPathEffect(new DashPathEffect(new float[]{10, 10}, 1));
            p.setColor(Color.BLACK);
            Path path = new Path();
            path.moveTo(pointX2, pointY2);
            path.lineTo(pointX3, pointY3);
            canvas.drawPath(path, p);

            p.setStyle(Paint.Style.FILL);
            canvas.drawLine(pointX3, pointY3, pointX5, pointY5, p);
            canvas.drawCircle(pointX3, pointY3, RADIUS, p);
            canvas.drawCircle(pointX5, pointY5, RADIUS, p);
        }
        if (step == STEP_PICK_PRINTER) {
            p.setColor(ContextCompat.getColor(context, R.color.stepline_red));
            p.setStyle(Paint.Style.FILL);
            p.setAntiAlias(true);
            canvas.drawLine(pointX1, pointY1, pointX4, pointY4, p);
            canvas.drawCircle(pointX1, pointY1, RADIUS, p);
            canvas.drawCircle(pointX3, pointY3, RADIUS, p);

            p.setStyle(Paint.Style.STROKE);
            p.setPathEffect(new DashPathEffect(new float[]{10, 10}, 1));
            p.setColor(Color.BLACK);
            Path path = new Path();
            path.moveTo(pointX4, pointY4);
            path.lineTo(pointX5, pointY5);
            canvas.drawPath(path, p);
            p.setStyle(Paint.Style.FILL);
            canvas.drawCircle(pointX5, pointY5, RADIUS, p);
        }
        if (step == STEP_PAY) {
            p.setColor(ContextCompat.getColor(context, R.color.stepline_red));
            p.setStyle(Paint.Style.FILL);
            p.setAntiAlias(true);
            canvas.drawLine(pointX1, pointY1, pointX5, pointY5, p);
            canvas.drawCircle(pointX1, pointY1, RADIUS, p);
            canvas.drawCircle(pointX3, pointY3, RADIUS, p);
            canvas.drawCircle(pointX5, pointY5, RADIUS, p);
        }
    }
}
