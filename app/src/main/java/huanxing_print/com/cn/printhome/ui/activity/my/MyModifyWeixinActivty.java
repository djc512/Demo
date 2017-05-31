package huanxing_print.com.cn.printhome.ui.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.simple.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.net.callback.NullCallback;
import huanxing_print.com.cn.printhome.net.request.my.UpdatePersonInfoRequest;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;

/**
 * Created by Administrator on 2017/3/22 0022.
 */

public class MyModifyWeixinActivty extends BaseActivity implements View.OnClickListener{
    private LinearLayout ll_back;
    private TextView iv_modifyName_finish;
    private EditText et_weixin;
    private ImageView iv_modify_delete;
    private String weixin;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_useinfo_midifyweixin);
        CommonUtils.initSystemBar(this);
        EventBus.getDefault().register(this);
        initView();
        initData();
        setListener();
    }

    private void initData() {
        weixin = getIntent().getStringExtra("weixin");
        if (!ObjectUtils.isNull(weixin)) {
            et_weixin.setText(weixin);
        }
    }

    private void initView() {
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        iv_modifyName_finish = (TextView) findViewById(R.id.iv_modifyName_finish);
        et_weixin = (EditText) findViewById(R.id.et_weixin);
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
                finishCurrentActivity();
                break;
            case R.id.iv_modifyName_finish:
                weixin = et_weixin.getText().toString().trim();
                if(ObjectUtils.isNull(weixin)){
                    ToastUtil.doToast(getSelfActivity(),"请输入用户名");
                    return;
                }

                Map<String, Object> params = new HashMap<String, Object>();
                params.put("weixin", weixin);
                DialogUtils.showProgressDialog(getSelfActivity(), "正在保存").show();
                UpdatePersonInfoRequest.update(getSelfActivity(),  baseApplication.getLoginToken(),params, callback);

                break;
            case R.id.iv_modify_delete:
                et_weixin.setText("");
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
    private NullCallback callback = new NullCallback() {

        @Override
        public void fail(String msg) {
            toast(msg);
            DialogUtils.closeProgressDialog();
        }

        @Override
        public void connectFail() {
            DialogUtils.closeProgressDialog();
            toastConnectFail();
        }

        @Override
        public void success(String msg) {
            DialogUtils.closeProgressDialog();
            baseApplication.setNickName(weixin);
            Intent carlist = new Intent(getSelfActivity() , MyActivity.class);
            carlist.putExtra("weixin", weixin);
            setResult(105, carlist);
            finishCurrentActivity();
            EventBus.getDefault().post(weixin, "weixin");
            //EventBus.getDefault().post(new UpdateEvent());
        }
    };
}
