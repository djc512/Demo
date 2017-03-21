package huanxing_print.com.cn.printhome.ui.activity.print;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import huanxing_print.com.cn.printhome.base.BaseActivity;

/**
 * Created by LGH on 2017/3/21.
 */

public abstract class BasePrintActivity extends BaseActivity {

    public Context context;
    public Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        activity = this;
    }
}
