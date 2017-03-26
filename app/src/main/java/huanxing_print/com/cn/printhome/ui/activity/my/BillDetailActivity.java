package huanxing_print.com.cn.printhome.ui.activity.my;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.util.CommonUtils;

/**
 * Created by Administrator on 2017/3/24 0024.
 */

public class BillDetailActivity extends BaseActivity implements View.OnClickListener{

    private LinearLayout ll_back;
    private TextView tv_billdetail_type;
    private TextView tv_billdetail_num;
    private TextView tv_billdetail_time;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBarGreen(this);
        setContentView(R.layout.activty_billitem_detail);
        initView();
        initData();
        setListener();
    }

    private void initData() {
//        tv_billdetail_num.setText();
//        tv_billdetail_time.setText();
//        tv_billdetail_type.setText();
    }

    private void setListener() {
        ll_back.setOnClickListener(this);
    }

    private void initView() {
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        tv_billdetail_type = (TextView) findViewById(R.id.tv_billdetail_type);
        tv_billdetail_num = (TextView) findViewById(R.id.tv_billdetail_num);
        tv_billdetail_time = (TextView) findViewById(R.id.tv_billdetail_time);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_back:
                finish();
                break;
        }
    }
}
