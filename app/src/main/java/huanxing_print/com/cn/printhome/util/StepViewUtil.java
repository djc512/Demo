package huanxing_print.com.cn.printhome.util;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.view.StepLineView;

/**
 * Created by LGH on 2017/4/28.
 */

public class StepViewUtil {

    public static void init(Context context,final View view, int step) {
        final StepLineView stepView = (StepLineView) view.findViewById(R.id.stepView);
        TextView pickFileTv = (TextView) view.findViewById(R.id.pickFileTv);
        TextView pickPrinterTv = (TextView) view.findViewById(R.id.pickPrinterTv);
        TextView payTv = (TextView) view.findViewById(R.id.payTv);
        if (step == StepLineView.STEP_PICK_FILE) {
            pickFileTv.setTextColor(ContextCompat.getColor(context, R.color.stepline_red));
        }
        if (step == StepLineView.STEP_PICK_PRINTER) {
            pickFileTv.setTextColor(ContextCompat.getColor(context, R.color.stepline_red));
            pickPrinterTv.setTextColor(ContextCompat.getColor(context, R.color.stepline_red));
        }
        if (step == StepLineView.STEP_PAY) {
            pickFileTv.setTextColor(ContextCompat.getColor(context, R.color.stepline_red));
            pickPrinterTv.setTextColor(ContextCompat.getColor(context, R.color.stepline_red));
            payTv.setTextColor(ContextCompat.getColor(context, R.color.stepline_red));
        }
        final LinearLayout lv = (LinearLayout) view.findViewById(R.id.lv1);
        ViewTreeObserver vto = view.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int width = lv.getWidth();
                int padding = lv.getPaddingLeft() + lv.getPaddingRight();
                stepView.setPadding((width - padding) / 6, 0, (width - padding) / 6, 0);
            }
        });
        stepView.setStep(step);
        stepView.invalidate();
    }
}
