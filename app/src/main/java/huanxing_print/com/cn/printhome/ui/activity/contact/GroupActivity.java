package huanxing_print.com.cn.printhome.ui.activity.contact;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.constant.ConFig;
import huanxing_print.com.cn.printhome.event.contacts.GroupUpdate;
import huanxing_print.com.cn.printhome.model.contact.GroupInfo;
import huanxing_print.com.cn.printhome.net.callback.contact.GroupListCallback;
import huanxing_print.com.cn.printhome.net.request.contact.GroupManagerRequest;
import huanxing_print.com.cn.printhome.ui.activity.chat.ChatTestActivity;
import huanxing_print.com.cn.printhome.ui.adapter.GroupAdatper;
import huanxing_print.com.cn.printhome.util.SharedPreferencesUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;
import huanxing_print.com.cn.printhome.util.contact.MyDecoration;
import huanxing_print.com.cn.printhome.view.dialog.LoadingDialog;

/**
 * Created by wanghao on 2017/5/5.
 */

public class GroupActivity extends BaseActivity implements View.OnClickListener, GroupAdatper.OnItemGroupClickListener {
    private static final int CREATE_GROUP = 1000;
    private RecyclerView recyclerView;
    private ArrayList<GroupInfo> groups = new ArrayList<GroupInfo>();
    private GroupAdatper adatper;
    private String token;
    private LoadingDialog loadingDialog;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_group);
        CommonUtils.initSystemBar(this);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        loadingDialog = new LoadingDialog(this);
        recyclerView = (RecyclerView) findViewById(R.id.groupView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new MyDecoration(this, MyDecoration.HORIZONTAL_LIST, 1, ContextCompat.getColor(this, R.color.recycler_divider_color)));

        adatper = new GroupAdatper(this, groups);
        adatper.setOnItemGroupClickListener(this);
        recyclerView.setAdapter(adatper);
    }

    private void initData() {
        token = SharedPreferencesUtils.getShareString(this, ConFig.SHAREDPREFERENCES_NAME,
                "loginToken");
//        DialogUtils.showProgressDialog(this, "加载中").show();
        loadingDialog.show();
        GroupManagerRequest.queryGroupList(this, token, groupListCallback);
    }

    private void setListener() {
        findViewById(R.id.ll_back).setOnClickListener(this);
        findViewById(R.id.create_group).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finishCurrentActivity();
                break;
            case R.id.create_group:
                Intent intent = new Intent(this, CreateGroup.class);
                startActivityForResult(intent, CREATE_GROUP);
                break;
        }
    }

    //无论产生事件的是否是UI线程，都在新的线程中执行
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onMessageEventAsync(GroupUpdate messageEvent) {
        Log.e("CMCC", messageEvent.getResultMessage());
        GroupManagerRequest.queryGroupList(this, token, groupListCallback);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CREATE_GROUP:
                if (resultCode == RESULT_OK) {
                    GroupInfo info = data.getParcelableExtra("created");

                    groups.add(info);
                    adatper.modifyData(groups);
                }
                break;
        }
    }

    @Override
    public void clickGroup(GroupInfo info) {
        if (null != info) {
            //假设跳入到群设置详情，后续删除
            Intent intent = new Intent(GroupActivity.this, ChatTestActivity.class);
            intent.putExtra("GroupInfo", info);
            startActivity(intent);

        }
    }

    GroupListCallback groupListCallback = new GroupListCallback() {
        @Override
        public void success(String msg, ArrayList<GroupInfo> groupInfos) {
//            DialogUtils.closeProgressDialog();
            loadingDialog.dismiss();
            if (null != groupInfos) {
                groups = groupInfos;
                adatper.modifyData(groups);
            }
        }

        @Override
        public void fail(String msg) {
//            DialogUtils.closeProgressDialog();
            loadingDialog.dismiss();
            ToastUtil.doToast(GroupActivity.this, msg);
        }

        @Override
        public void connectFail() {
//            DialogUtils.closeProgressDialog();
            loadingDialog.dismiss();
            toastConnectFail();
        }
    };

}
