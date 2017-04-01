package huanxing_print.com.cn.printhome.ui.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.util.CommonUtils;

/**
 * Created by Administrator on 2017/3/30 0030.
 */

public class DebitListActivity extends BaseActivity implements View.OnClickListener{
    private LinearLayout ll_back;
    private TextView tv_debitlist_name;
    private TextView tv_debitlist_money;
    private TextView tv_debitlist_time;
    private TextView tv_debitlist_post;
    private TextView tv_debitlist_state;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBarGreen(this);
        setContentView(R.layout.activity_debitlist);
        initView();
        setListener();
    }

    private void setListener() {

    }

    private void initView() {
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        tv_debitlist_name = (TextView) findViewById(R.id.tv_debitlist_name);
        tv_debitlist_money = (TextView) findViewById(R.id.tv_debitlist_money);
        tv_debitlist_time = (TextView) findViewById(R.id.tv_debitlist_time);
        tv_debitlist_post = (TextView) findViewById(R.id.tv_debitlist_post);
        tv_debitlist_state = (TextView) findViewById(R.id.tv_debitlist_state);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finishCurrentActivity();
                break;
            case R.id.ll_debitlist:
                startActivity(new Intent(getSelfActivity(),DebitListDetailActivity.class));
                break;
        }
    }
}
