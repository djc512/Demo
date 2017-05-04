package huanxing_print.com.cn.printhome.ui.activity.contact;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.util.CommonUtils;

/**
 * Created by wanghao on 2017/5/3.
 */

public class AddressBookActivity extends BaseActivity implements View.OnClickListener{
    private LinearLayout ll_back;
    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.activity_address_book);

        initView();
        initData();
        setListener();
    }

    private void initData(){

    }

    private void initView(){
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
    }

    private void setListener() {
        ll_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_back:
                finishCurrentActivity();
                break;
        }
    }
}
