package huanxing_print.com.cn.printhome.ui.activity.contact;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;

/**
 * Created by Administrator on 2017/5/10 0010.
 */

public class ModifyQunNameActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_save;
    private EditText et_modify;
    private ImageView iv_delete;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_qunname);
        initView();
        initData();
        initListener();
    }

    private void initData() {

    }

    private void initListener() {
        iv_back.setOnClickListener(this);
        tv_save.setOnClickListener(this);
        iv_delete.setOnClickListener(this);
    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_save = (TextView) findViewById(R.id.tv_save);
        et_modify = (EditText) findViewById(R.id.et_modify);
        iv_delete = (ImageView) findViewById(R.id.iv_delete);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finishCurrentActivity();
                break;
            case R.id.iv_delete:
                et_modify.setText("");
                break;
            case R.id.tv_save:
                String name = et_modify.getText().toString().trim();
                if (null == name) {
                    Toast.makeText(getSelfActivity(), "请先输入群组名", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent saveIntent = new Intent();
                setResult(RESULT_OK, saveIntent);
                finishCurrentActivity();
                break;
        }
    }
}
