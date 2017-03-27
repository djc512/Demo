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
import huanxing_print.com.cn.printhome.model.my.DebitNormalBean;
import huanxing_print.com.cn.printhome.net.callback.my.DebitNormalCallBack;
import huanxing_print.com.cn.printhome.net.request.my.DebitNormalRequest;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;

/**
 * Created by Administrator on 2017/3/24 0024.
 */

public class DebitNormalFragment extends Fragment implements View.OnClickListener {
    private EditText et_debit_normal_name;
    private TextView tv_debit_normal_money;
    private EditText et_debit_normal_address;
    private EditText et_debit_normal_phone;
    private EditText et_debit_normal_location;
    private EditText et_debit_normal_dlocation;
    private ImageView iv_normal_wechat;
    private ImageView iv_normal_alipay;
    private String dlocation;
    private String location;
    private String phone;
    private String address;
    private String name;
    private String money;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activty_debit_normal, null);

        initView(view);
        initData();
        setListener();
        return view;
    }

    private void initView(View view) {

        et_debit_normal_name = (EditText) view.findViewById(R.id.et_debit_normal_name);
        tv_debit_normal_money = (TextView) view.findViewById(R.id.tv_debit_normal_money);
        et_debit_normal_address = (EditText) view.findViewById(R.id.et_debit_normal_address);
        et_debit_normal_phone = (EditText) view.findViewById(R.id.et_debit_normal_phone);
        et_debit_normal_location = (EditText) view.findViewById(R.id.et_debit_normal_location);
        et_debit_normal_dlocation = (EditText) view.findViewById(R.id.et_debit_normal_dlocation);
        iv_normal_wechat = (ImageView) view.findViewById(R.id.iv_normal_wechat);
        iv_normal_alipay = (ImageView) view.findViewById(R.id.iv_normal_alipay);
    }

    private void initData() {
        money = tv_debit_normal_money.getText().toString().trim();

        name = et_debit_normal_name.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getContext(), "填写公司名称或者个人", Toast.LENGTH_SHORT).show();
            return;
        }

        address = et_debit_normal_address.getText().toString().trim();
        if (TextUtils.isEmpty(address)) {
            Toast.makeText(getContext(), "选择地区信息", Toast.LENGTH_SHORT).show();
            return;
        }

        phone = et_debit_normal_phone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(getContext(), "填写联系方式", Toast.LENGTH_SHORT).show();
            return;
        }

        location = et_debit_normal_location.getText().toString().trim();
        if (TextUtils.isEmpty(location)) {
            Toast.makeText(getContext(), "选择地区信息", Toast.LENGTH_SHORT).show();
            return;
        }

        dlocation = et_debit_normal_dlocation.getText().toString().trim();
        if (TextUtils.isEmpty(dlocation)) {
            Toast.makeText(getContext(), "填写详细地址", Toast.LENGTH_SHORT).show();
            return;
        }
    }


    private void setListener() {
        iv_normal_wechat.setOnClickListener(this);
        iv_normal_alipay.setOnClickListener(this);
    }

    private int type;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_normal_wechat:
                iv_normal_wechat.setImageResource(R.drawable.check_2x);
                iv_normal_alipay.setImageResource(R.drawable.uncheck_2x);
                type = 0;
                break;
            case R.id.iv_normal_alipay:
                iv_normal_alipay.setImageResource(R.drawable.check_2x);
                iv_normal_wechat.setImageResource(R.drawable.uncheck_2x);
                type = 1;
                break;
            case R.id.btn_normal_submit:
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
//    orderId	订单列表	string
//    payType	支付方式0-微信 1-支付宝 2-货到付款	number
//    receiver	接收人	string
//    telPhone	联系电话

    private void senRequest() {
        DialogUtils.showProgressDialog(getActivity(),"正在提交");
        DebitNormalRequest.sendNormalBack(
                getActivity(),
                dlocation,
                money,
                null,
                address,
                name,
                null,
                null,
                null,
                type,
                null,
                phone,
                new DebitNormalCallBack() {
                    @Override
                    public void success(String msg, DebitNormalBean bean) {

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
