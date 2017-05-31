package huanxing_print.com.cn.printhome.ui.activity.my;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.model.my.WeChatPayBean;
import huanxing_print.com.cn.printhome.net.callback.my.OrderIdCallBack;
import huanxing_print.com.cn.printhome.net.callback.my.WeChatCallBack;
import huanxing_print.com.cn.printhome.net.request.my.Go2PayRequest;
import huanxing_print.com.cn.printhome.net.request.my.OrderIdRequest;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.Pay.PayUtil;

/**
 * Created by Administrator on 2017/3/27 0027.
 */

public class PayActivity extends BaseActivity implements View.OnClickListener {

    private ImageView iv_back;
    private EditText et_cardnum;
    private EditText et_cardname;
    private TextView tv_yinjia_account;
    private ImageView iv_wechat_check;
    private ImageView iv_bankcard_check;
    private TextView tv_confirm;
    private LinearLayout ll_bank;
    private String cardnum;
    private String cardname;
    private TextView tv_money;
    private TextView tv_money1;
    private String orderid;
    private LinearLayout ll_confirm;
    private LinearLayout ll_confirm1;
    private TextView tv_confirm1;
    private String rechargeAmout;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        CommonUtils.initSystemBar(this);
        initView();
        initData();
        setListener();
    }

    private void initData() {
        orderid = getIntent().getStringExtra("orderid");
        rechargeAmout = getIntent().getStringExtra("rechargeAmout");
        tv_money.setText(rechargeAmout);
        tv_money1.setText("总价:" + rechargeAmout);

    }

    private void setListener() {
        iv_back.setOnClickListener(this);
        et_cardnum.setOnClickListener(this);
        et_cardname.setOnClickListener(this);

        iv_wechat_check.setOnClickListener(this);
        iv_bankcard_check.setOnClickListener(this);
        tv_confirm1.setOnClickListener(this);
        tv_confirm.setOnClickListener(this);
    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        et_cardnum = (EditText) findViewById(R.id.et_cardnum);
        et_cardname = (EditText) findViewById(R.id.et_cardname);
        tv_yinjia_account = (TextView) findViewById(R.id.tv_yinjia_account);
        iv_wechat_check = (ImageView) findViewById(R.id.iv_wechat_check);
        iv_bankcard_check = (ImageView) findViewById(R.id.iv_bankcard_check);
        tv_confirm = (TextView) findViewById(R.id.tv_confirm);
        ll_bank = (LinearLayout) findViewById(R.id.ll_bank);
        tv_money = (TextView) findViewById(R.id.tv_money);
        tv_money1 = (TextView) findViewById(R.id.tv_money1);
        tv_confirm1 = (TextView) findViewById(R.id.tv_confirm1);
        ll_confirm = (LinearLayout) findViewById(R.id.ll_confirm);
        ll_confirm1 = (LinearLayout) findViewById(R.id.ll_confirm1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finishCurrentActivity();
                break;
            case R.id.iv_bankcard_check:
                iv_bankcard_check.setBackgroundResource(R.drawable.select);
                iv_wechat_check.setBackgroundResource(R.drawable.select_no);
                ll_bank.setVisibility(View.VISIBLE);
                ll_confirm1.setVisibility(View.VISIBLE);
                ll_confirm.setVisibility(View.GONE);
                break;
            case R.id.iv_wechat_check:
                iv_wechat_check.setBackgroundResource(R.drawable.select);
                iv_bankcard_check.setBackgroundResource(R.drawable.select_no);
                ll_bank.setVisibility(View.GONE);
                ll_confirm.setVisibility(View.VISIBLE);
                ll_confirm1.setVisibility(View.GONE);
                break;
            case R.id.tv_confirm:
                gotoWeChat();
                Toast.makeText(getSelfActivity(), "请先选择充值方式", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_confirm1:
                break;
        }
    }

    /**
     * 微信支付
     */
    private void gotoWeChat() {
        OrderIdRequest.getOrderId(getSelfActivity(), rechargeAmout, new OrderIdCallBack() {
            @Override
            public void success(String msg, String data) {
                String czOrderid = data;
                go2WeChat(czOrderid, "CZ");
            }

            @Override
            public void fail(String msg) {

            }

            @Override
            public void connectFail() {

            }
        });
    }
    /**
     * 微信支付
     */
    private void go2WeChat(String orderid, String type) {

        Go2PayRequest.go2PWeChat(getSelfActivity(), orderid, type, new WeChatCallBack() {
            @Override
            public void success(WeChatPayBean bean) {
                wechatpay(bean);
            }

            @Override
            public void fail(String msg) {

            }

            @Override
            public void connectFail() {

            }
        });
    }

    /**
     * 调微信支付api
     * @param bean
     */
    private void wechatpay(WeChatPayBean bean) {
        PayUtil.getInstance(getSelfActivity()).weChatPay(bean);
    }

    /**
     * 银行卡帐号密码
     */
    private void submit() {
        cardnum = et_cardnum.getText().toString().trim();
        if (TextUtils.isEmpty(cardnum)) {
            Toast.makeText(this, "输入您汇款的银行卡号", Toast.LENGTH_SHORT).show();
            return;
        }
        cardname = et_cardname.getText().toString().trim();
        if (TextUtils.isEmpty(cardname)) {
            Toast.makeText(this, "输入您汇款的银行开户名", Toast.LENGTH_SHORT).show();
            return;
        }
    }
}
