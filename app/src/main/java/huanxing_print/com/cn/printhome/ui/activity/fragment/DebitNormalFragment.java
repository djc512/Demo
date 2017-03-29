package huanxing_print.com.cn.printhome.ui.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.net.callback.my.DebitNormalCallBack;
import huanxing_print.com.cn.printhome.net.request.my.DebitNormalRequest;
import huanxing_print.com.cn.printhome.util.RegexUtils;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;

/**
 * Created by Administrator on 2017/3/24 0024.
 */

public class DebitNormalFragment extends Fragment implements View.OnClickListener {

    private Button btn_normal_submit;
    private EditText et_debit_normal_companyName;
    private TextView tv_bill_billContext;
    private TextView tv_debit_normal_amount;
    private TextView textView;
    private EditText et_debit_normal_receiver;
    private EditText et_debit_normal_telPhone;
    private EditText et_debit_normal_city;
    private EditText et_debit_normal_address;
    private ImageView iv_normal_wechat;
    private ImageView iv_normal_alipay;

    private String companyName;
    private String receiver;
    private String telPhone;
    private String city;
    private String address;
    private String billContext;
    private String amount;

    private String billValue;
    private List<String> citys;
    private String expAmount;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activty_debit_normal, null);
        billValue = getArguments().getString("billValue");
        initView(view);
        setListener();
        return view;
    }

    private void initView(View view) {

        btn_normal_submit = (Button) view.findViewById(R.id.btn_normal_submit);
        et_debit_normal_companyName = (EditText) view.findViewById(R.id.et_debit_normal_companyName);
        tv_bill_billContext = (TextView) view.findViewById(R.id.tv_bill_billContext);
        tv_debit_normal_amount = (TextView) view.findViewById(R.id.tv_debit_normal_amount);
        textView = (TextView) view.findViewById(R.id.textView);
        et_debit_normal_receiver = (EditText) view.findViewById(R.id.et_debit_normal_receiver);
        et_debit_normal_telPhone = (EditText) view.findViewById(R.id.et_debit_normal_telPhone);
        et_debit_normal_city = (EditText) view.findViewById(R.id.et_debit_normal_city);
        et_debit_normal_address = (EditText) view.findViewById(R.id.et_debit_normal_address);
        iv_normal_wechat = (ImageView) view.findViewById(R.id.iv_normal_wechat);
        iv_normal_alipay = (ImageView) view.findViewById(R.id.iv_normal_alipay);

        tv_debit_normal_amount.setText(billValue);
    }
    private void getData() {
        citys = Arrays.asList("上海", "浙江", "江苏");
        billContext = tv_bill_billContext.getText().toString().trim();

        companyName = et_debit_normal_companyName.getText().toString().trim();
        if (TextUtils.isEmpty(companyName)) {
            Toast.makeText(getContext(), "填写公司名称或者个人", Toast.LENGTH_SHORT).show();
            return;
        }

        receiver = et_debit_normal_receiver.getText().toString().trim();
        if (TextUtils.isEmpty(receiver)) {
            Toast.makeText(getContext(), "填写收件人", Toast.LENGTH_SHORT).show();
            return;
        }

        telPhone = et_debit_normal_telPhone.getText().toString().trim();
        if (TextUtils.isEmpty(telPhone)) {
            Toast.makeText(getContext(), "填写联系方式", Toast.LENGTH_SHORT).show();
            return;
        }
        city = et_debit_normal_city.getText().toString().trim();
        if (TextUtils.isEmpty(city)) {
            Toast.makeText(getContext(), "选择地区信息", Toast.LENGTH_SHORT).show();
            return;
        }else if(citys.contains(city)){
            expAmount = "6";
        }else {
            expAmount ="15";
        }

        address = et_debit_normal_address.getText().toString().trim();
        if (TextUtils.isEmpty(address)) {
            Toast.makeText(getContext(), "填写详细地址", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private void setListener() {
        iv_normal_wechat.setOnClickListener(this);
        iv_normal_alipay.setOnClickListener(this);
        btn_normal_submit.setOnClickListener(this);

        et_debit_normal_telPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String editStr = s.toString().trim();
                if (!RegexUtils.isMobileSimple(editStr)){
                    Toast.makeText(getContext(), "手机号码格式不正确", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    private int payType;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_normal_wechat:
                iv_normal_wechat.setImageResource(R.drawable.check_2x);
                iv_normal_alipay.setImageResource(R.drawable.uncheck_2x);
                payType = 0;
                break;
            case R.id.iv_normal_alipay:
                iv_normal_alipay.setImageResource(R.drawable.check_2x);
                iv_normal_wechat.setImageResource(R.drawable.uncheck_2x);
                payType = 1;
                break;
            case R.id.btn_normal_submit:
                getData();
                senRequest();
                break;
        }
    }

//    address	收件地址	string
//    amount	发票金额	string
//    billContext	发票内容	string
//    city	城市	string
//    companyName	发票抬头	string
//    expAmount	邮费	string
//    fileSize	文件份数	string
//    payType	支付方式0-微信 1-支付宝 2-货到付款	number
//    receiver	接收人	string
//    telPhone	联系电话	string

    private void senRequest() {
        DialogUtils.showProgressDialog(getActivity(), "正在提交");
        DebitNormalRequest.sendNormalBack(
                getActivity(),
                address,//详细地址
                billValue,//发票金额
                billContext,//发票内容
                city,//所在城市
                companyName,//发票抬头
                expAmount,//邮费
                null,//文件份数
                payType,//支付方式0-微信 1-支付宝 2-货到付款	number
                receiver,//收件人
                telPhone,//联系方式
                new DebitNormalCallBack() {
                    @Override
                    public void success(String msg, String bean) {

                    }

                    @Override
                    public void fail(String msg) {

                    }

                    @Override
                    public void connectFail() {

                    }
                }
        );
    }
}
