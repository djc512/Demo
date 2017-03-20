package huanxing_print.com.cn.printhome.ui.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;

/**
 * Created by Administrator on 2017/3/20 0020.
 */

public class NoticeActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_notice_detail;
    private ImageView iv_notice_tuikuang;
    private ImageView iv_notice_chongzhi;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_nitice);
        initView();
        setListener();
    }

    private void initView() {
        iv_notice_detail = (ImageView) findViewById(R.id.iv_notice_detail);
        iv_notice_tuikuang = (ImageView) findViewById(R.id.iv_notice_tuikuang);
        iv_notice_chongzhi = (ImageView) findViewById(R.id.iv_notice_chongzhi);

    }
    private void setListener() {
        iv_notice_chongzhi.setOnClickListener(this);
        iv_notice_tuikuang.setOnClickListener(this);
        iv_notice_detail.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_notice_detail://消息列表
                startActivity(new Intent(NoticeActivity.this,NoticeListActivity.class));

                break;
            case R.id.iv_notice_tuikuang://退款详情

                break;
            case R.id.iv_notice_chongzhi://充值消息详情

                break;
        }
    }
}
