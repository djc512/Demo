package huanxing_print.com.cn.printhome.ui.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import huanxing_print.com.cn.printhome.R;

/**
 * Created by Administrator on 2017/3/24 0024.
 */

public class DebitValueFragment extends Fragment implements View.OnClickListener {
    private EditText et_debit_value_name;
    private EditText et_debit_value_shui;
    private EditText et_debit_value_address;
    private EditText et_debit_value_phone;
    private EditText et_debit_value_blank;
    private EditText et_debit_value_blanknum;
    private TextView tv_debit_value_money;
    private EditText et_debit_value_location;
    private EditText et_debit_value_num;
    private EditText et_debit_value_location1;
    private EditText et_debit_value_dlocation;
    private String name;
    private String shui;
    private String address;
    private String phone;
    private String blank;
    private String blanknum;
    private String location;
    private String num;
    private String location1;
    private String dlocation;
    private ImageView iv_value_wechat;
    private ImageView iv_value_alipay;
    private String money;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activty_debit_value, null);
        initView(view);
        initData();
        setListener();
        return view;
    }

    private void setListener() {
        iv_value_wechat.setOnClickListener(this);
        iv_value_alipay.setOnClickListener(this);
    }

    private void initView(View view) {

        et_debit_value_name = (EditText) view.findViewById(R.id.et_debit_value_name);
        et_debit_value_shui = (EditText) view.findViewById(R.id.et_debit_value_shui);
        et_debit_value_address = (EditText) view.findViewById(R.id.et_debit_value_address);
        et_debit_value_phone = (EditText) view.findViewById(R.id.et_debit_value_phone);
        et_debit_value_blank = (EditText) view.findViewById(R.id.et_debit_value_blank);
        et_debit_value_blanknum = (EditText) view.findViewById(R.id.et_debit_value_blanknum);
        tv_debit_value_money = (TextView) view.findViewById(R.id.tv_debit_value_money);
        et_debit_value_location = (EditText) view.findViewById(R.id.et_debit_value_location);
        et_debit_value_num = (EditText) view.findViewById(R.id.et_debit_value_num);
        et_debit_value_location1 = (EditText) view.findViewById(R.id.et_debit_value_location1);
        et_debit_value_dlocation = (EditText) view.findViewById(R.id.et_debit_value_dlocation);
        iv_value_wechat = (ImageView) view.findViewById(R.id.iv_value_wechat);
        iv_value_alipay = (ImageView) view.findViewById(R.id.iv_value_alipay);

    }

    private void initData() {
        money = tv_debit_value_money.getText().toString().trim();
        name = et_debit_value_name.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getContext(), "填写公司名称", Toast.LENGTH_SHORT).show();
            return;
        }

        shui = et_debit_value_shui.getText().toString().trim();
        if (TextUtils.isEmpty(shui)) {
            Toast.makeText(getContext(), "填写纳税人识别码", Toast.LENGTH_SHORT).show();
            return;
        }

        address = et_debit_value_address.getText().toString().trim();
        if (TextUtils.isEmpty(address)) {
            Toast.makeText(getContext(), "填写收票单位注册地址", Toast.LENGTH_SHORT).show();
            return;
        }

        phone = et_debit_value_phone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(getContext(), "填写公司电话号码", Toast.LENGTH_SHORT).show();
            return;
        }

        blank = et_debit_value_blank.getText().toString().trim();
        if (TextUtils.isEmpty(blank)) {
            Toast.makeText(getContext(), "填写收票单位开户银行", Toast.LENGTH_SHORT).show();
            return;
        }

        blanknum = et_debit_value_blanknum.getText().toString().trim();
        if (TextUtils.isEmpty(blanknum)) {
            Toast.makeText(getContext(), "填写收票单位银行账户", Toast.LENGTH_SHORT).show();
            return;
        }

        location = et_debit_value_location.getText().toString().trim();
        if (TextUtils.isEmpty(location)) {
            Toast.makeText(getContext(), "选择地区信息", Toast.LENGTH_SHORT).show();
            return;
        }

        num = et_debit_value_num.getText().toString().trim();
        if (TextUtils.isEmpty(num)) {
            Toast.makeText(getContext(), "填写联系方式", Toast.LENGTH_SHORT).show();
            return;
        }

        location1 = et_debit_value_location1.getText().toString().trim();
        if (TextUtils.isEmpty(location1)) {
            Toast.makeText(getContext(), "填写收票单位注册地址", Toast.LENGTH_SHORT).show();
            return;
        }

        dlocation = et_debit_value_dlocation.getText().toString().trim();
        if (TextUtils.isEmpty(dlocation)) {
            Toast.makeText(getContext(), "填写详细地址", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private int type;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_value_alipay:
                type = 2;
                break;
            case R.id.iv_value_wechat:
                type = 1;
                break;
            case R.id.btn_value_submit:
                sendValue();
                break;
        }
    }

//    address	收件人地址	string
//    amount	发票金额	string
//    bankAccount	开户行账号	string
//    bankName	开户行	string
//    billContext	发票内容0-打印费	number
//    city	城市	string
//    companyAddress	发票公司地址	string
//    companyName	发票公司名称	string
//    companyPhone	发票公司电话	string
//    expAmount	邮费	string
//    fileSize	文件份数	string
//    orderId	订单ID列表	string
//    payType	支付方式0-微信 1-支付宝 2-货到付款	number
//    ratepayerId	纳税人识别码	string
//    receiver	快递接收人	string
//    telPhone	联系人号码

    private void sendValue() {

    }
}
