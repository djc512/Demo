package huanxing_print.com.cn.printhome.ui.activity.my;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;

/**
 * Created by Administrator on 2017/3/24 0024.
 */

public class BillDebitActivity extends BaseActivity implements View.OnClickListener{

    private LinearLayout ll_back;
    private TextView tv_bill_normal;
    private TextView tv_bill_value;
    private View view_bill;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debit);
        initView();
        setListener();
    }

    private void setListener() {
        tv_bill_normal.setOnClickListener(this);
        tv_bill_value.setOnClickListener(this);
    }

    private void initView() {
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        tv_bill_normal = (TextView) findViewById(R.id.tv_bill_normal);
        tv_bill_value = (TextView) findViewById(R.id.tv_bill_value);
        view_bill = findViewById(R.id.view_bill);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_bill_normal:
                break;
            case R.id.tv_bill_value:
                break;
        }
    }
}
