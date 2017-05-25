package huanxing_print.com.cn.printhome.ui.activity.contact;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.constant.ConFig;
import huanxing_print.com.cn.printhome.model.contact.NewFriendInfo;
import huanxing_print.com.cn.printhome.net.callback.NullCallback;
import huanxing_print.com.cn.printhome.net.callback.contact.NewFriendCallback;
import huanxing_print.com.cn.printhome.net.request.contact.FriendManagerRequest;
import huanxing_print.com.cn.printhome.ui.activity.chat.ChatTestActivity;
import huanxing_print.com.cn.printhome.ui.adapter.NewFriendRecycelAdapter;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.SharedPreferencesUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;
import huanxing_print.com.cn.printhome.util.contact.MyDecoration;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;
import huanxing_print.com.cn.printhome.view.dialog.LoadingDialog;

/**
 * Created by wanghao on 2017/5/4.
 */

public class NewFriendActivity extends BaseActivity implements View.OnClickListener,NewFriendRecycelAdapter.OnAddItemClickListener{
    private RecyclerView friendRecycler;
    private NewFriendRecycelAdapter adapter;
    private ArrayList<NewFriendInfo> friendInfos = new ArrayList<NewFriendInfo>();
    private String token;
    private NewFriendInfo clickOperationInfo;
    private boolean isAgreeFriend = false;
    private LoadingDialog loadingDialog;
    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.activity_contact_new_friend);
        token = SharedPreferencesUtils.getShareString(this, ConFig.SHAREDPREFERENCES_NAME,
                "loginToken");
        initView();
        initData();
        setListener();
    }

    private void initView() {
        loadingDialog = new LoadingDialog(this);
        friendRecycler = (RecyclerView) findViewById(R.id.friend_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        friendRecycler.setHasFixedSize(true);
        friendRecycler.setLayoutManager(layoutManager);
        friendRecycler.addItemDecoration(new MyDecoration(this,MyDecoration.HORIZONTAL_LIST));

        adapter = new NewFriendRecycelAdapter(this,friendInfos);
        adapter.setOnAddItemClickListener(this);
        friendRecycler.setAdapter(adapter);
    }

    private void initData() {
        loadingDialog.show();
//        DialogUtils.showProgressDialog(this,"加载中").show();
        FriendManagerRequest.queryNewFriendList(this, token, newFriendCallback);
    }

    private void setListener() {
        findViewById(R.id.ll_back).setOnClickListener(this);
        findViewById(R.id.add_friend).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                if(isAgreeFriend) {
                    setResult(RESULT_OK);
                }
                finishCurrentActivity();
                break;
            case R.id.add_friend:
                startActivity(SearchYinJiaNumActivity.class);
                break;
        }
    }

    private void startActivity(Class cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    @Override
    public void onAddressBookClick() {
        startActivity(AddByAddressBookActivity.class);
    }

    @Override
    public void onItemFriendClick(NewFriendInfo newFriendInfo) {

        if("1".equals(newFriendInfo.getType())) {
            Intent intent = new Intent(NewFriendActivity.this, ChatTestActivity.class);
            intent.putExtra("NewFriendInfo", newFriendInfo);
            startActivity(intent);
        }
    }

    @Override
    public void onItemNewFriendPassClick(NewFriendInfo newFriendInfo) {
        clickOperationInfo = null;
        clickOperationInfo = newFriendInfo;
//        DialogUtils.showProgressDialog(this, "操作中").show();
        loadingDialog.show();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("isPass", 1);
        params.put("memberId", newFriendInfo.getMemberId());
        FriendManagerRequest.operationNewFriend(this, token, params, nullCallback);
    }

    NewFriendCallback newFriendCallback = new NewFriendCallback() {
        @Override
        public void success(String msg, ArrayList<NewFriendInfo> newFriendInfos) {
//            DialogUtils.closeProgressDialog();
            loadingDialog.dismiss();
            if(null != newFriendInfos) {
                friendInfos = newFriendInfos;
                for(NewFriendInfo info : newFriendInfos) {
                    if(null == info.getMemberName()) {
                        info.setMemberName("Null");
                    }
                }
                adapter.updateData(friendInfos);
            }
        }

        @Override
        public void fail(String msg) {
//            DialogUtils.closeProgressDialog();
            loadingDialog.dismiss();
            ToastUtil.doToast(NewFriendActivity.this,msg);
        }

        @Override
        public void connectFail() {
//            DialogUtils.closeProgressDialog();
            loadingDialog.dismiss();
            toastConnectFail();
        }
    };

    NullCallback nullCallback = new NullCallback() {
        @Override
        public void success(String msg) {
//            DialogUtils.closeProgressDialog();
            loadingDialog.dismiss();
            /*//发送第一句话
            EMMessage message = EMMessage.createTxtSendMessage("我们已经是好友了", clickOperationInfo.getMemberId());
            message.setAttribute("userId",baseApplication.getMemberId());
            message.setAttribute("iconUrl",baseApplication.getHeadImg());
            message.setAttribute("nickName",baseApplication.getNickName());
            EMClient.getInstance().chatManager().sendMessage(message);
            ToastUtil.doToast(getSelfActivity(), "添加成功");*/

            clickOperationInfo.setType("1");
            adapter.updateData(friendInfos);
//            clickOperationInfo = null;
            isAgreeFriend = true;
            // TODO: 2017/5/18  跳转聊天
            Intent intent = new Intent(NewFriendActivity.this, ChatTestActivity.class);
            intent.putExtra("NewFriendInfo", clickOperationInfo);
            startActivity(intent);
        }

        @Override
        public void fail(String msg) {
//            DialogUtils.closeProgressDialog();
            loadingDialog.dismiss();
            ToastUtil.doToast(NewFriendActivity.this, msg);
        }

        @Override
        public void connectFail() {
//            DialogUtils.closeProgressDialog();
            loadingDialog.dismiss();
            toastConnectFail();
        }
    };
}
