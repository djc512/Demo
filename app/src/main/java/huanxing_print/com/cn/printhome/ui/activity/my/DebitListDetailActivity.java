package huanxing_print.com.cn.printhome.ui.activity.my;

import android.os.Bundle;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.util.CommonUtils;

/**
 * Created by Administrator on 2017/3/30 0030.
 */

public class DebitListDetailActivity extends BaseActivity {
    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debitdetail);
        CommonUtils.initSystemBar(this);
    }
}
