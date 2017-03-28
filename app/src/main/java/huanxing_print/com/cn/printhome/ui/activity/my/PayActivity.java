package huanxing_print.com.cn.printhome.ui.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.model.my.Go2PayBean;
import huanxing_print.com.cn.printhome.net.callback.my.Go2PayCallBack;
import huanxing_print.com.cn.printhome.net.request.my.Go2PayRequest;
import huanxing_print.com.cn.printhome.util.CommonUtils;

/**
 * Created by Administrator on 2017/3/27 0027.
 */

public class PayActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout ll_back;
    private TextView tv_pay_money;
    private ImageView iv_pay_wechat;
    private ImageView iv_pay_alipay;
    private Button btn_pay;
    private String orderId;
    private String rechargeAmout;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBarGreen(this);
        setContentView(R.layout.activity_pay);
        initView();
        initData();
        setListener();
    }

    private void initData() {
        Intent intent = getIntent();
        orderId = intent.getStringExtra("orderId");
        rechargeAmout = intent.getStringExtra("rechargeAmout");

        tv_pay_money.setText("￥"+rechargeAmout);
    }

    private void setListener() {
        btn_pay.setOnClickListener(this);
        ll_back.setOnClickListener(this);
        iv_pay_alipay.setOnClickListener(this);
        iv_pay_wechat.setOnClickListener(this);
    }

    private void initView() {
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        tv_pay_money = (TextView) findViewById(R.id.tv_pay_money);
        iv_pay_wechat = (ImageView) findViewById(R.id.iv_pay_wechat);
        iv_pay_alipay = (ImageView) findViewById(R.id.iv_pay_alipay);
        btn_pay = (Button) findViewById(R.id.btn_pay);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pay:
                go2Pay();
                break;
            case R.id.ll_back:
                finish();
                break;
            case R.id.iv_pay_alipay:
                iv_pay_alipay.setBackgroundResource(R.drawable.check_2x);
                iv_pay_wechat.setBackgroundResource(R.drawable.uncheck_2x);
                break;
            case R.id.iv_pay_wechat:
                iv_pay_wechat.setBackgroundResource(R.drawable.check_2x);
                iv_pay_alipay.setBackgroundResource(R.drawable.uncheck_2x);
                break;
        }
    }

    /**
     *去支付
     */
    private void go2Pay() {
        Go2PayRequest.go2Pay(getSelfActivity(), orderId, "CZ", new Go2PayCallBack() {
            @Override
            public void success(String msg, Go2PayBean bean) {

            }

            @Override
            public void fail(String msg) {

            }

            @Override
            public void connectFail() {

            }
        });
    }
}
