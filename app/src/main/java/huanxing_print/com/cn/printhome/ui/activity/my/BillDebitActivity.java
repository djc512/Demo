package huanxing_print.com.cn.printhome.ui.activity.my;

import android.os.Bundle;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;

/**
 * Created by Administrator on 2017/3/24 0024.
 */

public class BillDebitActivity extends BaseActivity {

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_debit_normal);
    }
}
