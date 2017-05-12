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
import huanxing_print.com.cn.printhome.net.callback.my.WeChatCallBack;
import huanxing_print.com.cn.printhome.net.request.my.Go2PayRequest;
import huanxing_print.com.cn.printhome.util.CommonUtils;

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
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.activity_pay);
        initView();
        initData();
        setListener();
    }

    private void initData() {
        orderid = getIntent().getStringExtra("orderid");
        rechargeAmout = getIntent().getStringExtra("rechargeAmout");
        tv_money.setText(rechargeAmout);
        tv_money1.setText(rechargeAmout);
    }

    private void setListener() {
        iv_back.setOnClickListener(this);
        et_cardnum.setOnClickListener(this);
        et_cardname.setOnClickListener(this);

        iv_wechat_check.setOnClickListener(this);
        iv_bankcard_check.setOnClickListener(this);
        tv_confirm1.setOnClickListener(this);
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

//    IWXAPI mWxApi = WXAPIFactory.createWXAPI(mContext, WX_APPID, true);
//    mWxApi.registerApp(WX_APPID);
//    /**
//     * 请求app服务器得到的回调结果
//     */
//    @Override
//    public void onGet(JSONObject jsonObject) {
//        if (mWxApi != null) {
//            PayReq req = new PayReq();
//
//            req.appId = WX_APPID;// 微信开放平台审核通过的应用APPID
//            try {
//                req.partnerId = jsonObject.getString("partnerid");// 微信支付分配的商户号
//                req.prepayId = jsonObject.getString("prepayid");// 预支付订单号，app服务器调用“统一下单”接口获取
//                req.nonceStr = jsonObject.getString("noncestr");// 随机字符串，不长于32位，服务器小哥会给咱生成
//                req.timeStamp = jsonObject.getString("timestamp");// 时间戳，app服务器小哥给出
//                req.packageValue = jsonObject.getString("package");// 固定值Sign=WXPay，可以直接写死，服务器返回的也是这个固定值
//                req.sign = jsonObject.getString("sign");// 签名，服务器小哥给出，他会根据：https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=4_3指导得到这个
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            mWxApi.sendReq(req);
//            Log.d("发起微信支付申请");
//        }
//
//    }


    /**
     * 微信支付
     */
    private void gotoWeChat() {
        Go2PayRequest.go2PWeChat(getSelfActivity(), orderid, "CZ", new WeChatCallBack() {
            @Override
            public void success(WeChatPayBean bean) {

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
