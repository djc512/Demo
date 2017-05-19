package huanxing_print.com.cn.printhome.ui.activity.print;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.StatusBarUtil;
import huanxing_print.com.cn.printhome.view.dialog.LoadingDialog;

/**
 * Created by LGH on 2017/3/21.
 */

public abstract class BasePrintActivity extends AppCompatActivity implements View.OnClickListener {

    public Context context;
    public Activity activity;

    protected LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        activity = this;
        initStatusBar();
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

    protected void initTitleBar(String title) {
        TextView titleTv = (TextView) findViewById(R.id.titleTv);
        titleTv.setText(title);
        if (findViewById(R.id.backImg) != null) {
            findViewById(R.id.backImg).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.backImg:
                finish();
                break;
        }
    }

    protected void showLoading() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(context);
        }
        if (!loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }

    protected void dismissLoading() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    protected boolean isLoading() {
        if (loadingDialog == null || !loadingDialog.isShowing()) {
            return false;
        }
        return true;
    }

    public LoadingDialog getLoadingDialog() {
        return loadingDialog;
    }
}
