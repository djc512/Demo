package huanxing_print.com.cn.printhome.ui.activity.chat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.model.chat.RedPackage;
import huanxing_print.com.cn.printhome.model.chat.RefreshEvent;
import huanxing_print.com.cn.printhome.model.my.MyInfoBean;
import huanxing_print.com.cn.printhome.net.callback.chat.SendPackageCallBack;
import huanxing_print.com.cn.printhome.net.callback.my.MyInfoCallBack;
import huanxing_print.com.cn.printhome.net.request.chat.ChatRequest;
import huanxing_print.com.cn.printhome.net.request.my.MyInfoRequest;
import huanxing_print.com.cn.printhome.util.CashierInputFilter;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;
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

        //设置金额填框必须只能填写金额数字 EditText要先设置
        //android:inputType="numberDecimal"或者setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL)
        InputFilter[] filters = {new CashierInputFilter()};
        edt_single_money.setFilters(filters);
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
                    str_word = "撸起袖子好好干";
                }
                //网络请求，获取用户信息
                DialogUtils.showProgressDialog(this, "装红包中").show();
                MyInfoRequest.getMyInfo(getSelfActivity(), baseApplication.getLoginToken(), new MyMyInfoCallBack());
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
                //EvenBus发个消息更新余额
                RefreshEvent event = new RefreshEvent();
                event.setCode(0x11);
                EventBus.getDefault().postSticky(event);

                String amount = redPackage.getAmount();
                String masterName = redPackage.getMasterName();
                String packetId = redPackage.getPacketId();
                String remark = redPackage.getRemark();
                Intent intent = new Intent();
                intent.putExtra("packetId", packetId);
                intent.putExtra("remark", remark);
                intent.putExtra("groupType", 1001);
                intent.putExtra("packetType", getString(R.string.single_Red_package));
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

    public class MyMyInfoCallBack extends MyInfoCallBack {

        @Override
        public void success(String msg, MyInfoBean bean) {
            DialogUtils.closeProgressDialog();

            if (!ObjectUtils.isNull(bean)) {
                String totleBalance = bean.getTotleBalance();
                String amount = edt_single_money.getText().toString();
                if (Float.parseFloat(totleBalance) >= Float.parseFloat(amount)) {
                    //发红包
                    DialogUtils.showRedPackageConfirmDialog(getSelfActivity(), "红包", "¥ " + Float.parseFloat(amount), new DialogUtils.RedPackageCallback() {
                        @Override
                        public void send() {
                            ChatRequest.sendRedPackage(getSelfActivity(), baseApplication.getLoginToken()
                                    , str_money, str_word, memberId, callBack);
                        }
                    }).show();
                } else {
                    //发红包
                    DialogUtils.showRedPackageConfirmDialog(getSelfActivity(), "红包", "余额不足", new DialogUtils.RedPackageCallback() {
                        @Override
                        public void send() {
                        }
                    }).show();
                }

            }
        }

        @Override
        public void fail(String msg) {
            DialogUtils.closeProgressDialog();
            ToastUtil.doToast(getSelfActivity(), msg);
        }

        @Override
        public void connectFail() {
            DialogUtils.closeProgressDialog();
        }
    }
}