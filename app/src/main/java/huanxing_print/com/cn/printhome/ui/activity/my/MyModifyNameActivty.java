package huanxing_print.com.cn.printhome.ui.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.simple.eventbus.EventBus;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.model.my.UpdateInfoBean;
import huanxing_print.com.cn.printhome.net.callback.my.UpdateInfoCallBack;
import huanxing_print.com.cn.printhome.net.request.my.UpdateIfoRequest;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;

/**
 * Created by Administrator on 2017/3/22 0022.
 */

public class MyModifyNameActivty extends BaseActivity implements View.OnClickListener{
    private LinearLayout ll_back;
    private TextView iv_modifyName_finish;
    private EditText et_modify_nickName;
    private ImageView iv_modify_delete;
    private String cropImagePath;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBarGreen(this);
        setContentView(R.layout.activity_useinfo_midifyname);
        EventBus.getDefault().register(this);
        initView();
        initData();
        setListener();
    }

    private void initData() {
        Intent intent = getIntent();
        cropImagePath = intent.getStringExtra("cropImagePath");
    }

    private void initView() {
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        iv_modifyName_finish = (TextView) findViewById(R.id.iv_modifyName_finish);
        et_modify_nickName = (EditText) findViewById(R.id.et_modify_nickName);
        iv_modify_delete = (ImageView) findViewById(R.id.iv_modify_delete);
    }

    private void setListener() {
        ll_back.setOnClickListener(this);
        iv_modifyName_finish.setOnClickListener(this);
        iv_modify_delete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_back:
                finish();
                break;
            case R.id.iv_modifyName_finish:
                String name = et_modify_nickName.getText().toString().trim();
                if(ObjectUtils.isNull(name)){
                    ToastUtil.doToast(getSelfActivity(),"请输入用户名");
                    return;
                }

                //修改上一个页面的用户名
                EventBus.getDefault().post(name,"name");

                //将用户头像上传到后台
                UpdateIfoRequest.updateInfo(getSelfActivity(),cropImagePath,
                        null,name,null,null,null,
                        new MyUpdateInfoCallBack()
                );
                ToastUtil.doToast(getSelfActivity(),"修改成功");
                finish();
                break;
            case R.id.iv_modify_delete:
                et_modify_nickName.setText("");
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private class MyUpdateInfoCallBack extends UpdateInfoCallBack {

        @Override
        public void fail(String msg) {

        }

        @Override
        public void connectFail() {

        }

        @Override
        public void success(String msg, UpdateInfoBean bean) {

        }
    }
}
