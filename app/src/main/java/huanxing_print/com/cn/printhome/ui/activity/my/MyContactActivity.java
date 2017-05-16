package huanxing_print.com.cn.printhome.ui.activity.my;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.util.CommonUtils;

/**
 * Created by Administrator on 2017/3/17 0017.
 */

public class MyContactActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_contact_phone;
    private ImageView iv_suggest;
    private LinearLayout ll_back;
    private RelativeLayout rl_service;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.activty_user_contact);
        initView();
        setListener();
    }


    private void initView() {
        tv_contact_phone = (TextView) findViewById(R.id.tv_contact_phone);
        iv_suggest = (ImageView) findViewById(R.id.iv_suggest);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        rl_service = (RelativeLayout) findViewById(R.id.rl_service);

    }

    private void setListener() {
        tv_contact_phone.setOnClickListener(this);
        ll_back.setOnClickListener(this);
        rl_service.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_contact_phone:
                //String number = tv_contact_phone.getText().toString().trim();
                //用intent启动拨打电话
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "400-666-2060"));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(intent);
                break;
            case R.id.rl_service:
                startActivity(new Intent(MyContactActivity.this, SuggestActivity.class));
                break;
            case R.id.ll_back:
                finishCurrentActivity();
                break;
        }
    }
}
