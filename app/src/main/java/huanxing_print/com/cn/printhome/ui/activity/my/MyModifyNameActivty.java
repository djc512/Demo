package huanxing_print.com.cn.printhome.ui.activity.my;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
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

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBarGreen(this);
        setContentView(R.layout.activity_useinfo_midifyname);
//        EventBus.getDefault().register(this);
        initView();
        setListener();
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
                //发送一个消息，用户名已经改变
//                EventBus.TAG = "name";
//                EventBus.getDefault().post(name);
                break;
            case R.id.iv_modify_delete:
                et_modify_nickName.setText("");
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
    }
}
