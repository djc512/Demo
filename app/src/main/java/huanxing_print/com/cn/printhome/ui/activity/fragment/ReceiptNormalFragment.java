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

public class ReceiptNormalFragment extends Fragment implements View.OnClickListener {
    private EditText et_name;
    private TextView tv_comfirm;
    private Context ctx;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ctx = getActivity();
        View view = inflater.inflate(R.layout.activty_debit_normal1, null);
        initView(view);
        initData();
        initListener();
        return view;
    }

    private void initView(View view) {
        et_name = (EditText) view.findViewById(R.id.et_name);
        tv_comfirm = (TextView) view.findViewById(R.id.tv_comfirm);
    }

    private void initData() {
    }

    private void initListener() {
        tv_comfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_comfirm:
                String name = et_name.getText().toString().trim();
                if (ObjectUtils.isNull(name)) {
                    Toast.makeText(ctx, "请输入公司抬头或个人", Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
        }
    }
}
