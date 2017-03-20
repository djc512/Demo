package huanxing_print.com.cn.printhome.ui.activity.login;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.TextView;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;

/**
 * Created by Administrator on 2017/3/17 0017.
 */

public class ContactActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_contact_phone;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_user_contact);
        initView();
        setListener();
    }

    private void setListener() {
        tv_contact_phone.setOnClickListener(this);
    }

    private void initView() {
        tv_contact_phone = (TextView) findViewById(R.id.tv_contact_phone);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_contact_phone:
                String number = tv_contact_phone.getText().toString().trim();
                //用intent启动拨打电话
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(intent);
                break;
        }
    }
}
