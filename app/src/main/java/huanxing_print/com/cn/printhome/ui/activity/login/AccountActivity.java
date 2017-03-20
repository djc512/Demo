package huanxing_print.com.cn.printhome.ui.activity.login;

import android.os.Bundle;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;

/**
 * Created by Administrator on 2017/3/17 0017.
 */

public class AccountActivity extends BaseActivity {

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);
    }
}
