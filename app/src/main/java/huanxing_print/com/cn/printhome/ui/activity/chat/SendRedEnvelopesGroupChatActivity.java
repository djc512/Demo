package huanxing_print.com.cn.printhome.ui.activity.chat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.model.chat.LuckyPackage;
import huanxing_print.com.cn.printhome.model.chat.RedPackageObject;
import huanxing_print.com.cn.printhome.model.contact.GroupMessageInfo;
import huanxing_print.com.cn.printhome.model.my.MyInfoBean;
import huanxing_print.com.cn.printhome.net.callback.chat.SendCommonPackageCallBack;
import huanxing_print.com.cn.printhome.net.callback.chat.SendLuckyPackageCallBack;
import huanxing_print.com.cn.printhome.net.callback.my.MyInfoCallBack;
import huanxing_print.com.cn.printhome.net.request.chat.ChatRequest;
import huanxing_print.com.cn.printhome.net.request.my.MyInfoRequest;
import huanxing_print.com.cn.printhome.util.CashierInputFilter;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;

/**
 * description: 群聊的红包
 * author LSW
 * date 2017/5/9 18:52
 * update 2017/5/9
 */
public class SendRedEnvelopesGroupChatActivity extends BaseActivity implements View.OnClickListener {

    private EditText edt_single_money;//单个金额
    private EditText edt_red_package_num;//红包个数
    private EditText edt_leave_word;//留言
    private RelativeLayout rel_red_package_num;//红包个数行
    private RelativeLayout rel_group_description;//群人数描述行
    private TextView txt_num;//跟随着你的输入额变化
    private TextView txt_action_change;//改变红包类型
    private TextView txt_left;//当前红包描述
    private TextView txt_group_bottom;//群红包不可领取说明
    private Button btn_plug_money;
    private boolean isLuck = true;//拼手气
    private GroupMessageInfo groupInfo;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 改变状态栏的颜色使其与APP风格一体化
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.activity_send_red_envelopes_group_chat);
        groupInfo = getIntent().getParcelableExtra("groupInfo");
        init();
    }

    private void init() {
        edt_single_money = (EditText) findViewById(R.id.edt_single_money);
        edt_red_package_num = (EditText) findViewById(R.id.edt_red_package_num);
        edt_leave_word = (EditText) findViewById(R.id.edt_leave_word);
        txt_num = (TextView) findViewById(R.id.txt_num);
        txt_left = (TextView) findViewById(R.id.txt_left);
        txt_group_bottom = (TextView) findViewById(R.id.txt_group_bottom);
        txt_action_change = (TextView) findViewById(R.id.txt_action_change);
        btn_plug_money = (Button) findViewById(R.id.btn_plug_money);
        rel_red_package_num = (RelativeLayout) findViewById(R.id.rel_red_package_num);
        rel_group_description = (RelativeLayout) findViewById(R.id.rel_group_description);
        btn_plug_money.setOnClickListener(this);
        txt_action_change.setOnClickListener(this);
        //设置金额填框必须只能填写金额数字 EditText要先设置
        //android:inputType="numberDecimal"或者setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL)
        InputFilter[] filters={new CashierInputFilter()};
        edt_single_money.setFilters(filters);
        //输入的监听
        edt_single_money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //变化的值
                String change = String.valueOf(s);
                if (!ObjectUtils.isNull(change)) {
                    btn_plug_money.setBackgroundResource(R.drawable.broder_red_package_null);
                    //改变下面的金额 以及按钮的颜色
                    if (isLuck) {
                        if (edt_red_package_num.getText().length() > 0) {
                            btn_plug_money.setBackgroundResource(R.drawable.broder_red_package_red);
                        }
                    } else {
                        btn_plug_money.setBackgroundResource(R.drawable.broder_red_package_red);
                    }
                    txt_num.setText(s);

                } else {
                    txt_num.setText("0.00");
                    btn_plug_money.setBackgroundResource(R.drawable.broder_red_package_null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_red_package_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                //变化的值
                String change = String.valueOf(s);
                if (!ObjectUtils.isNull(change)) {
                    btn_plug_money.setBackgroundResource(R.drawable.broder_red_package_null);
                    //改变下面的金额 以及按钮的颜色
                    if (isLuck) {
                        if (edt_single_money.getText().length() > 0) {
                            btn_plug_money.setBackgroundResource(R.drawable.broder_red_package_red);
                        }
                    } else {
                        btn_plug_money.setBackgroundResource(R.drawable.broder_red_package_red);
                    }

                } else {
                    btn_plug_money.setBackgroundResource(R.drawable.broder_red_package_null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

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
                //网络请求，获取用户信息
                DialogUtils.showProgressDialog(this, "装红包中").show();
                MyInfoRequest.getMyInfo(SendRedEnvelopesGroupChatActivity.this, baseApplication.getLoginToken(), new MyMyInfoCallBack());

//                plugMoney();
                break;
            case R.id.txt_action_change:
                //改变红包类型
                isLuck = !isLuck;
                if (isLuck) {
                    //显示红包个数和群描述 隐藏掉群红包不可领取说明
                    rel_group_description.setVisibility(View.VISIBLE);
                    rel_red_package_num.setVisibility(View.VISIBLE);
                    txt_group_bottom.setVisibility(View.INVISIBLE);
                    //切换类别说明
                    txt_left.setText(getString(R.string.red_package_type_description_luck));
                    txt_action_change.setText(getString(R.string.red_package_other_name_group));
                } else {
                    //隐藏红包个数和群描述 显示群红包不可领取说明
                    rel_group_description.setVisibility(View.GONE);
                    rel_red_package_num.setVisibility(View.GONE);
                    txt_group_bottom.setVisibility(View.VISIBLE);
                    //切换类别说明
                    txt_left.setText(getString(R.string.red_package_type_description_group));
                    txt_action_change.setText(getString(R.string.red_package_other_name_luck));
                }
                break;
        }
    }

    private void plugMoney() {
        if (isLuck) {
            //Todo 手气红包
            String amount = edt_single_money.getText().toString();
            String remark = edt_leave_word.getText().toString().isEmpty() ? "恭喜发财,大吉大利" : edt_leave_word.getText().toString();
            int number = Integer.parseInt(edt_red_package_num.getText().toString());
            String easemobGroupId = "";
            //String easemobGroupId = null == groupInfo.getEasemobGroupId() ? "" : groupInfo.getEasemobGroupId();
            String groupId = null == groupInfo.getGroupId() ? "" : groupInfo.getGroupId();
            if (!easemobGroupId.isEmpty() || !groupId.isEmpty()) {
                //Todo 执行发红包操作
                luckRedPackage(amount, remark, easemobGroupId, groupId, number);
            } else {
                ToastUtil.doToast(SendRedEnvelopesGroupChatActivity.this, "发送失败，请退出重新进入");
            }
        } else {
            //Todo 群红包
            String amount = edt_single_money.getText().toString();
            String remark = edt_leave_word.getText().toString().isEmpty() ? "恭喜发财,大吉大利" : edt_leave_word.getText().toString();
            String easemobGroupId = "";
            //String easemobGroupId = null == groupInfo.getEasemobGroupId() ? "" : groupInfo.getEasemobGroupId();
            String groupId = null == groupInfo.getGroupId() ? "" : groupInfo.getGroupId();
            if (!easemobGroupId.isEmpty() || !groupId.isEmpty()) {
                //Todo 执行发红包操作
                groupRedPackage(amount, remark, easemobGroupId, groupId);
            } else {
                ToastUtil.doToast(SendRedEnvelopesGroupChatActivity.this, "发送失败，请退出重新进入");
            }
        }
    }

    private void groupRedPackage(String amount, String remark, String easemobGroupId, String groupId) {
        Log.e("CMCC", "amount : " + amount + "\n remark : " + remark + "\n easemobGroupId : " + easemobGroupId + "\n groupId : " + groupId);
        DialogUtils.showProgressDialog(this, "处理中").show();
        ChatRequest.sendCommonPackage(SendRedEnvelopesGroupChatActivity.this, baseApplication.getLoginToken(), amount, easemobGroupId, groupId, remark, groupRedPackageCB);
    }

    private void luckRedPackage(String amount, String remark, String easemobGroupId, String groupId, int number) {
        Log.e("CMCC", "amount : " + amount + "\n remark : " + remark + "\n easemobGroupId : " + easemobGroupId + "\n groupId : " + groupId + "\n number : " + number);
        DialogUtils.showProgressDialog(this, "处理中").show();
        ChatRequest.sendLuckyPackage(SendRedEnvelopesGroupChatActivity.this, baseApplication.getLoginToken(), amount, easemobGroupId, groupId, number, remark, luckyPackageCallBack);
    }

    SendCommonPackageCallBack groupRedPackageCB = new SendCommonPackageCallBack() {
        @Override
        public void success(String msg, RedPackageObject id) {
            DialogUtils.closeProgressDialog();
            String remark = edt_leave_word.getText().toString().isEmpty() ? "恭喜发财,大吉大利" : edt_leave_word.getText().toString();
            if (null != id) {
                Intent intent = new Intent();
                intent.putExtra("packetId", id.getPacketId());
                intent.putExtra("remark", remark);
                if (isLuck) {
                    //手气
                    intent.putExtra("groupType", 2);
                    intent.putExtra("packetType", R.string.group_lucky_Red_package);
                } else {
                    intent.putExtra("groupType", 1);
                    intent.putExtra("packetType", R.string.group_common_Red_package);
                }
                Log.d("CMCC", "setResult--groupType--》" + intent.getIntExtra("groupType", -1) + ",packetId:" + id.getPacketId());
                setResult(RESULT_OK, intent);
                finishCurrentActivity();
            }
        }

        @Override
        public void fail(String msg) {
            DialogUtils.closeProgressDialog();
            ToastUtil.doToast(SendRedEnvelopesGroupChatActivity.this, msg);
        }

        @Override
        public void connectFail() {
            DialogUtils.closeProgressDialog();
            connectFail();
        }
    };

    SendLuckyPackageCallBack luckyPackageCallBack = new SendLuckyPackageCallBack() {
        @Override
        public void success(String msg, LuckyPackage luckyPackage) {
            DialogUtils.closeProgressDialog();
            if (null != luckyPackage) {
                Intent intent = new Intent();
                intent.putExtra("packetId", luckyPackage.getPacketId());
                intent.putExtra("remark", luckyPackage.getRemark());
                if (isLuck) {
                    //手气
                    intent.putExtra("groupType", 2);
                } else {
                    intent.putExtra("groupType", 1);
                }
                setResult(RESULT_OK, intent);
                finishCurrentActivity();
            }
        }

        @Override
        public void fail(String msg) {
            DialogUtils.closeProgressDialog();
            ToastUtil.doToast(SendRedEnvelopesGroupChatActivity.this, msg);
        }

        @Override
        public void connectFail() {
            DialogUtils.closeProgressDialog();
            connectFail();
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
                    DialogUtils.showRedPackageConfirmDialog(SendRedEnvelopesGroupChatActivity.this, "红包", "¥ " + Float.parseFloat(amount), new DialogUtils.RedPackageCallback() {
                        @Override
                        public void send() {
                            plugMoney();
                        }
                    }).show();
                } else {
                    //发红包
                    DialogUtils.showRedPackageConfirmDialog(SendRedEnvelopesGroupChatActivity.this, "红包", "余额不足", new DialogUtils.RedPackageCallback() {
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
            ToastUtil.doToast(SendRedEnvelopesGroupChatActivity.this, msg);
        }

        @Override
        public void connectFail() {
            DialogUtils.closeProgressDialog();
        }
    }
}
