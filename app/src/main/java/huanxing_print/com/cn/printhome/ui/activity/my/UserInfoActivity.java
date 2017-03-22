package huanxing_print.com.cn.printhome.ui.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;

/**
 * Created by Administrator on 2017/3/22 0022.
 */

public class UserInfoActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout ll_back;
    private CircleImageView iv_userInfo_head;
    private TextView tv_userInfo_nickname;
    private TextView tv_userInfo_wechat;
    private ImageView iv_userinfo_nickname;
    private ImageView iv_userInfo_wechat;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_useinfo);
        initView();
        setListener();
    }

    private void initView() {
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        iv_userInfo_head = (CircleImageView) findViewById(R.id.iv_userInfo_head);
        tv_userInfo_nickname = (TextView) findViewById(R.id.tv_userInfo_nickname);
        tv_userInfo_wechat = (TextView) findViewById(R.id.tv_userInfo_wechat);
        iv_userinfo_nickname = (ImageView) findViewById(R.id.iv_userinfo_nickname);
        iv_userInfo_wechat = (ImageView) findViewById(R.id.iv_userInfo_wechat);
    }

    private void setListener() {
        ll_back.setOnClickListener(this);
        iv_userInfo_head.setOnClickListener(this);
        iv_userinfo_nickname.setOnClickListener(this);
        iv_userInfo_wechat.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_userInfo_head:
                startActivity(new Intent(getSelfActivity(),MyActivity.class));
                break;
            case R.id.iv_userinfo_nickname:
                startActivity(new Intent(getSelfActivity(),MyModifyNameActivty.class));
                break;
            case R.id.iv_userInfo_wechat:
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
