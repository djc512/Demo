package huanxing_print.com.cn.printhome.ui.activity.chat;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.ObjectUtils;

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
                    //改变下面的金额 以及按钮的颜色
                    txt_num.setText(s);
                    btn_plug_money.setBackgroundResource(R.drawable.broder_red_package_red);
                } else {
                    txt_num.setText("0.00");
                    btn_plug_money.setBackgroundResource(R.drawable.broder_red_package_null);
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
                //发红包
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
}
