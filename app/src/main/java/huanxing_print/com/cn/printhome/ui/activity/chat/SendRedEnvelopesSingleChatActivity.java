package huanxing_print.com.cn.printhome.ui.activity.chat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.model.chat.RedPackage;
import huanxing_print.com.cn.printhome.net.callback.chat.SendPackageCallBack;
import huanxing_print.com.cn.printhome.net.request.chat.ChatRequest;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;

/**
 * description: 单对单的发红包
 * author LSW
 * date 2017/5/9 18:52
 * update 2017/5/9
 */
public class SendRedEnvelopesSingleChatActivity extends BaseActivity implements View.OnClickListener {

    private EditText edt_single_money;//单个金额
    private EditText edt_leave_word;//留言
    private TextView txt_num;//跟随着你的输入额变化
    private Button btn_plug_money;
    private String str_money, str_word;
    private String memberId;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 改变状态栏的颜色使其与APP风格一体化
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.activity_send_red_envelopes_single_chat);

        init();
    }

    private void init() {
        memberId = getIntent().getStringExtra("memberId");
        edt_single_money = (EditText) findViewById(R.id.edt_single_money);
        edt_leave_word = (EditText) findViewById(R.id.edt_leave_word);
        txt_num = (TextView) findViewById(R.id.txt_num);
        btn_plug_money = (Button) findViewById(R.id.btn_plug_money);
        btn_plug_money.setOnClickListener(this);
        edt_single_money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //变化的值
                String change = String.valueOf(s);
                if (!ObjectUtils.isNull(change)) {
                    //改变下面的金额 以及按钮的颜色
                    txt_num.setText(s);
                    btn_plug_money.setBackgroundResource(R.drawable.broder_red_package_red);
                    btn_plug_money.setEnabled(true);
                } else {
                    txt_num.setText("0.00");
                    btn_plug_money.setBackgroundResource(R.drawable.broder_red_package_null);
                    btn_plug_money.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //结束
        View view = findViewById(R.id.back);
        view.findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishCurrentActivity();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_plug_money:
                str_money = edt_single_money.getText().toString().trim();
                str_word = edt_leave_word.getText().toString().trim();
                if (ObjectUtils.isNull(str_word)) {
                    str_word = "恭喜发财，大吉大利";
                }
                ChatRequest.sendRedPackage(getSelfActivity(), baseApplication.getLoginToken()
                        , str_money, str_word, memberId, callBack);
                //发红包
                //展示假的红包
//                Dialog dialog = new SingleRedEnvelopesDialog(getSelfActivity(),
//                        R.style.MyDialog);
//                dialog.show();
                break;
        }
    }


    SendPackageCallBack callBack = new SendPackageCallBack() {

        @Override
        public void success(String msg, RedPackage redPackage) {
            DialogUtils.closeProgressDialog();
            if (null != redPackage) {
                String amount = redPackage.getAmount();
                String masterName = redPackage.getMasterName();
                String packetId = redPackage.getPacketId();
                String remark = redPackage.getRemark();
                Intent intent = new Intent();
                intent.putExtra("packetId", packetId);
                setResult(RESULT_OK, intent);
                finishCurrentActivity();
            }

        }

        @Override
        public void fail(String msg) {
            DialogUtils.closeProgressDialog();
            toast(msg);
        }

        @Override
        public void connectFail() {
            DialogUtils.closeProgressDialog();
            toastConnectFail();
        }

    };
}