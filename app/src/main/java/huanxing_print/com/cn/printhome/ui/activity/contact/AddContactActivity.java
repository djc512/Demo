package huanxing_print.com.cn.printhome.ui.activity.contact;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.util.CommonUtils;

/**
 * Created by wanghao on 2017/5/3.
 */

public class AddContactActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout ll_back;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.activity_add_contact);
        initView();
        setListener();
    }

    private void initView() {
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
    }

    private void setListener() {
        ll_back.setOnClickListener(this);
        findViewById(R.id.input_yinJia_number_space).setOnClickListener(this);
        findViewById(R.id.add_by_addressbook).setOnClickListener(this);
        findViewById(R.id.add_by_qr).setOnClickListener(this);
        findViewById(R.id.my_qr).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finishCurrentActivity();
                break;
            case R.id.input_yinJia_number_space:
                startActivity(SearchYinJiaNumActivity.class);
                break;
            case R.id.add_by_addressbook:
                startActivity(AddByAddressBookActivity.class);
                break;
            case R.id.add_by_qr:
                break;
            case R.id.my_qr:
                startActivity(MyQRCodeActivity.class);
                break;
        }
    }

    private void startActivity(Class cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }
}
