package huanxing_print.com.cn.printhome.ui.activity.approval;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.util.CommonUtils;

/**
 * Created by Administrator on 2017/5/5 0005.
 */
public class ApprovalBuyActivity extends Activity implements View.OnClickListener{

    public Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval_buy);
        context = this;
        initView();
        initData();
        initListener();
    }

    private void initListener() {
    }

    private void initData() {
    }

    private void initView() {

    }


    @Override
    public void onClick(View v) {

    }
}
