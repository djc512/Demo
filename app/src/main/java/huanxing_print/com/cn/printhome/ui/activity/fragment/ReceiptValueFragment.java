package huanxing_print.com.cn.printhome.ui.activity.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.util.ObjectUtils;

/**
 * Created by Administrator on 2017/5/8 0008.
 */

public class ReceiptValueFragment extends Fragment implements View.OnClickListener {
    private TextView tv_comfirm;
    private Context ctx;
    private EditText et_companyName;
    private EditText et_ratepayerId;
    private EditText et_companyAddress;
    private EditText et_companyPhone;
    private EditText et_bankName;
    private EditText et_bankAccount;
    private String companyName;
    private String ratepayerId;
    private String companyAddress;
    private String companyPhone;
    private String bankName;
    private String bankAccount;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ctx = getActivity();
        View view = inflater.inflate(R.layout.activty_debit_value1, null);
        initView(view);
        initData();
        initListener();
        return view;
    }

    private void initView(View view) {

        et_companyName = (EditText) view.findViewById(R.id.et_companyName);
        et_ratepayerId = (EditText) view.findViewById(R.id.et_ratepayerId);
        et_companyAddress = (EditText) view.findViewById(R.id.et_companyAddress);
        et_companyPhone = (EditText) view.findViewById(R.id.et_companyPhone);
        et_bankName = (EditText) view.findViewById(R.id.et_bankName);
        et_bankAccount = (EditText) view.findViewById(R.id.et_bankAccount);
        tv_comfirm = (TextView) view.findViewById(R.id.tv_comfirm);
    }

    private void initData() {
    }

    private void initListener() {
        tv_comfirm.setOnClickListener(this);
        et_companyName.setOnClickListener(this);
        et_ratepayerId.setOnClickListener(this);
        et_companyAddress.setOnClickListener(this);
        et_companyPhone.setOnClickListener(this);
        et_bankName.setOnClickListener(this);
        et_bankAccount.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_comfirm:
                getData();
                break;
        }
    }

    private void getData() {
        companyName = et_companyName.getText().toString().trim();
        if (ObjectUtils.isNull(companyName)) {
            Toast.makeText(ctx, "请输入公司抬头或个人", Toast.LENGTH_SHORT).show();
            return;
        }
        ratepayerId = et_ratepayerId.getText().toString().trim();
        if (ObjectUtils.isNull(ratepayerId)) {
            Toast.makeText(ctx, "请输入公司抬头或个人", Toast.LENGTH_SHORT).show();
            return;
        }

        companyAddress = et_companyAddress.getText().toString().trim();
        if (ObjectUtils.isNull(companyAddress)) {
            Toast.makeText(ctx, "请输入公司抬头或个人", Toast.LENGTH_SHORT).show();
            return;
        }

        companyPhone = et_companyPhone.getText().toString().trim();
        if (ObjectUtils.isNull(companyPhone)) {
            Toast.makeText(ctx, "请输入公司抬头或个人", Toast.LENGTH_SHORT).show();
            return;
        }

        bankName = et_bankName.getText().toString().trim();
        if (ObjectUtils.isNull(bankName)) {
            Toast.makeText(ctx, "请输入公司抬头或个人", Toast.LENGTH_SHORT).show();
            return;
        }

        bankAccount = et_bankAccount.getText().toString().trim();
        if (ObjectUtils.isNull(bankAccount)) {
            Toast.makeText(ctx, "请输入公司抬头或个人", Toast.LENGTH_SHORT).show();
            return;
        }
    }
}
