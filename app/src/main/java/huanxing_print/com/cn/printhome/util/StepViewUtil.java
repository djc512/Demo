package huanxing_print.com.cn.printhome.util;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.view.StepLineView;

import static huanxing_print.com.cn.printhome.R.id.lv;
import static huanxing_print.com.cn.printhome.R.id.ryt;

/**
 * Created by LGH on 2017/4/28.
 */

public class StepViewUtil {

    public static void init(Context context, final View view, int step) {
        final StepLineView stepView = (StepLineView) view.findViewById(R.id.stepView);
        final TextView pickFileTv = (TextView) view.findViewById(R.id.pickFileTv);
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
        final RelativeLayout ryt = (RelativeLayout) view.findViewById(R.id.ryt);
        ViewTreeObserver vto2 = ryt.getViewTreeObserver();
        vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ryt.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int textwidth = pickFileTv.getWidth();
                int rytPadding = ryt.getPaddingLeft();
                int padding =  rytPadding + textwidth / 2;
                stepView.setPadding(padding, 0, padding, 0);
            }
        });
        stepView.setStep(step);
        stepView.invalidate();
    }
}
