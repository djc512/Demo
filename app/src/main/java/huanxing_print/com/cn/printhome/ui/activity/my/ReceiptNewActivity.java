package huanxing_print.com.cn.printhome.ui.activity.my;

import android.os.Bundle;
import android.view.View;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;

/**
 * Created by Administrator on 2017/5/8 0008.
 */

public class ReceiptNewActivity extends BaseActivity implements View.OnClickListener {


    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_debit_value);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        
    }

    private void initData() {

    }

    private void initListener() {

    }

    @Override
    public void onClick(View v) {

    }
}
