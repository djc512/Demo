package huanxing_print.com.cn.printhome.ui.activity.contact;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.event.contacts.GroupMessageUpdate;
import huanxing_print.com.cn.printhome.event.contacts.GroupUpdate;
import huanxing_print.com.cn.printhome.model.chat.RefreshEvent;
import huanxing_print.com.cn.printhome.model.contact.GroupMessageInfo;
import huanxing_print.com.cn.printhome.net.callback.NullCallback;
import huanxing_print.com.cn.printhome.net.request.contact.GroupManagerRequest;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;

/**
 * Created by Administrator on 2017/5/10 0010.
 */

public class ModifyQunNameActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_save;
    private EditText et_modify;
    private ImageView iv_delete;
    private String groupid;
    private String groupurl;
    private GroupMessageInfo groupMessageInfo;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_qunname);
        initView();
        initData();
        initListener();
    }

    private void initData() {
        Intent intent = getIntent();
        groupMessageInfo = intent.getParcelableExtra("groupMessageInfo");
//        groupid = intent.getStringExtra("groupid");
//        groupurl = intent.getStringExtra("groupurl");
        groupid = groupMessageInfo.getGroupId();
        groupurl = groupMessageInfo.getGroupUrl();
        et_modify.setText(groupMessageInfo.getGroupName());
    }

    private void initListener() {
        iv_back.setOnClickListener(this);
        tv_save.setOnClickListener(this);
        iv_delete.setOnClickListener(this);
    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_save = (TextView) findViewById(R.id.tv_save);
        et_modify = (EditText) findViewById(R.id.et_modify);
        iv_delete = (ImageView) findViewById(R.id.iv_delete);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finishCurrentActivity();
                break;
            case R.id.iv_delete:
                et_modify.setText("");
                break;
            case R.id.tv_save:
                String name = et_modify.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(getSelfActivity(), "请先输入群组名", Toast.LENGTH_SHORT).show();
                    return;
                }
                modifyName(name);
//                Intent saveIntent = new Intent();
//                setResult(RESULT_OK, saveIntent);
//                finishCurrentActivity();
                break;
        }
    }

    private void modifyName(final String name) {
        DialogUtils.showProgressDialog(getSelfActivity(), "努力上传中").show();
        Map<String, Object> params = new HashMap<>();
        params.put("groupName", name);
        params.put("groupId", groupid);
        params.put("groupUrl", groupurl);
        GroupManagerRequest.modify(getSelfActivity(), baseApplication.getLoginToken(), params, new NullCallback() {
            @Override
            public void success(String msg) {
                DialogUtils.closeProgressDialog();
                Toast.makeText(getSelfActivity(), "修改成功", Toast.LENGTH_SHORT).show();
                EventBus.getDefault().post(new GroupUpdate("groupUpdate"));
                groupMessageInfo.setGroupName(name);
                EventBus.getDefault().post(new GroupMessageUpdate("updateName", groupMessageInfo));
                //更新UI
                RefreshEvent event = new RefreshEvent();
                event.setCode(0x15);
                EventBus.getDefault().post(event);

                setResult(RESULT_OK);
                finishCurrentActivity();
            }

            @Override
            public void fail(String msg) {
                DialogUtils.closeProgressDialog();
            }

            @Override
            public void connectFail() {
                DialogUtils.closeProgressDialog();
            }
        });
    }
}
