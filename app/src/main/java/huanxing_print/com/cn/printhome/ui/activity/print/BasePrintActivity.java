package huanxing_print.com.cn.printhome.ui.activity.print;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.StatusBarUtil;

/**
 * Created by LGH on 2017/3/21.
 */

public abstract class BasePrintActivity extends AppCompatActivity {

    public Context context;
    public Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        activity = this;
        initStatusBar();
//        setStatusBarColor();
        CommonUtils.initSystemBar(this);
    }

    protected void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams
                    .FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams
                    .FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    protected void setStatusBarColor() {
        StatusBarUtil.setColor(activity, ContextCompat.getColor(context, R.color.green));
    }
}
