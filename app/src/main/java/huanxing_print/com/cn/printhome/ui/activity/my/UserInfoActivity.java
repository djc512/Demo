package huanxing_print.com.cn.printhome.ui.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.util.CommonUtils;

/**
 * Created by Administrator on 2017/3/22 0022.
 */

public class UserInfoActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout ll_back;
    private CircleImageView iv_userInfo_head;
    private TextView tv_userInfo_nickname;
    private TextView tv_userInfo_wechat;
    private LinearLayout ll_userInfo_name;
    private LinearLayout ll_userInfo_wechat;


    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBarGreen(this);
        setContentView(R.layout.activity_my_useinfo);
        initView();
        setListener();
    }

    private void initView() {
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        iv_userInfo_head = (CircleImageView) findViewById(R.id.iv_userInfo_head);
        tv_userInfo_nickname = (TextView) findViewById(R.id.tv_userInfo_nickname);
        tv_userInfo_wechat = (TextView) findViewById(R.id.tv_userInfo_wechat);
        ll_userInfo_name = (LinearLayout) findViewById(R.id.ll_userInfo_name);
        ll_userInfo_wechat = (LinearLayout) findViewById(R.id.ll_userInfo_wechat);
    }

    private void setListener() {
        ll_back.setOnClickListener(this);
        iv_userInfo_head.setOnClickListener(this);
        ll_userInfo_name.setOnClickListener(this);
        ll_userInfo_wechat.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_userInfo_head:
                startActivity(new Intent(getSelfActivity(), MyActivity.class));
                break;
            case R.id.ll_userInfo_name:
                startActivity(new Intent(getSelfActivity(), MyModifyNameActivty.class));
                break;
            case R.id.ll_userInfo_wechat:
                break;
            case R.id.ll_back:
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
