package huanxing_print.com.cn.printhome.ui.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

public class MyModifyUniqueIdActivty extends BaseActivity implements View.OnClickListener{
    private LinearLayout ll_back;
    private TextView iv_modifyName_finish;
    private EditText et_modify_nickName;
    private ImageView iv_modify_delete;
    private String uniqueId;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.activity_useinfo_midifyuniqueid);
        EventBus.getDefault().register(this);
        initView();
        initData();
        setListener();
    }

    private void initData() {
        String uniqueId = getIntent().getStringExtra("uniqueId");
        if (!ObjectUtils.isNull(uniqueId)) {
            et_modify_nickName.setText(uniqueId);
        }

        et_modify_nickName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (start > 0) {
                    iv_modify_delete.setVisibility(View.VISIBLE);
                } else {
                    iv_modify_delete.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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
                finishCurrentActivity();
                break;
            case R.id.iv_modifyName_finish:
                uniqueId = et_modify_nickName.getText().toString().trim();
                if(ObjectUtils.isNull(uniqueId)){
                    ToastUtil.doToast(getSelfActivity(),"请输入用户名");
                    return;
                }

                Map<String, Object> params = new HashMap<String, Object>();
                params.put("uniqueId", uniqueId);
                DialogUtils.showProgressDialog(getSelfActivity(), "正在保存").show();
                UpdatePersonInfoRequest.update(getSelfActivity(),  baseApplication.getLoginToken(),params, callback);

                break;
            case R.id.iv_modify_delete:
                et_modify_nickName.setText("");
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
            baseApplication.setUniqueId(uniqueId);
            Intent carlist = new Intent(getSelfActivity() , MyActivity.class);
            carlist.putExtra("uniqueId", uniqueId);
            setResult(105, carlist);
            finishCurrentActivity();
            EventBus.getDefault().post(uniqueId, "uniqueId");
            //EventBus.getDefault().post(new UpdateEvent());
        }
    };
}
