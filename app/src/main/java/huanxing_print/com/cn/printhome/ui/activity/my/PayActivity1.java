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
import huanxing_print.com.cn.printhome.util.CommonUtils;

/**
 * Created by Administrator on 2017/3/27 0027.
 */

public class PayActivity1 extends BaseActivity implements View.OnClickListener {

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

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.activity_pay1);
        initView();
        initData();
        setListener();
    }

    private void initData() {
    }

    private void setListener() {
        iv_back.setOnClickListener(this);
        et_cardnum.setOnClickListener(this);
        et_cardname.setOnClickListener(this);

        iv_wechat_check.setOnClickListener(this);
        iv_bankcard_check.setOnClickListener(this);
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
                break;
            case R.id.iv_wechat_check:
                iv_wechat_check.setBackgroundResource(R.drawable.select);
                iv_bankcard_check.setBackgroundResource(R.drawable.select_no);
                ll_bank.setVisibility(View.GONE);
                break;
            case R.id.tv_confirm:
                submit();
                Toast.makeText(getSelfActivity(), "请先选择充值方式", Toast.LENGTH_SHORT).show();
                break;
        }
    }

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
