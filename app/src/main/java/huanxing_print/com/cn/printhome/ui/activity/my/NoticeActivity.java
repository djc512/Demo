package huanxing_print.com.cn.printhome.ui.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.util.CommonUtils;

/**
 * Created by Administrator on 2017/3/20 0020.
 */

public class NoticeActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_notice_detail;
    private ImageView iv_notice_tuikuang;
    private ImageView iv_notice_chongzhi;
    private LinearLayout ll_back;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_nitice);
        CommonUtils.initSystemBar(this);
        initView();
        setListener();
    }

    private void initView() {
        iv_notice_detail = (ImageView) findViewById(R.id.iv_notice_detail);
        iv_notice_tuikuang = (ImageView) findViewById(R.id.iv_notice_tuikuang);
        iv_notice_chongzhi = (ImageView) findViewById(R.id.iv_notice_chongzhi);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
    }
    private void setListener() {
        iv_notice_chongzhi.setOnClickListener(this);
        iv_notice_tuikuang.setOnClickListener(this);
        iv_notice_detail.setOnClickListener(this);
        ll_back.setOnClickListener(this);
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
            case R.id.ll_back://充值消息详情
                   finishCurrentActivity();
                break;
        }
    }
}
