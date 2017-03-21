package huanxing_print.com.cn.printhome.ui.activity.my;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.util.ToastUtil;

/**
 * Created by Administrator on 2017/3/17 0017.
 */

public class AccountActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_money;
    private Button btn_chongzhi;
    private Dialog dialog;
    private ImageView iv_account_back;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);
        initView();
        setListener();
    }

    private void initView() {
        tv_money = (TextView) findViewById(R.id.tv_money);
        btn_chongzhi = (Button) findViewById(R.id.btn_chongzhi);
        iv_account_back = (ImageView) findViewById(R.id.iv_account_back);

    }
    private void setListener() {
        btn_chongzhi.setOnClickListener(this);
        iv_account_back.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_chongzhi:
                showCZDialog();
                break;
            case R.id.iv_account_back:
                finish();
                break;
            case R.id.iv_cz_wechat:
                dialog.dismiss();
                ToastUtil.doToast(AccountActivity.this,"微信充值");
                break;
            case R.id.iv_cz_alipay:
                dialog.dismiss();
                ToastUtil.doToast(AccountActivity.this,"支付宝充值");
                break;
        }
    }

    /**
     * 显示充值对话框
     */
    private void showCZDialog() {
        dialog = new Dialog(this, R.style.dialog_theme);
        View view =  View.inflate(this,R.layout.dialog_chongzhi,null);
        ImageView iv_cz_wechat = (ImageView) view.findViewById(R.id.iv_cz_wechat);
        ImageView iv_cz_alipay = (ImageView) view.findViewById(R.id.iv_cz_alipay);
        dialog.setContentView(view);
        dialog.show();

        iv_cz_wechat.setOnClickListener(this);
        iv_cz_alipay.setOnClickListener(this);
    }
}
