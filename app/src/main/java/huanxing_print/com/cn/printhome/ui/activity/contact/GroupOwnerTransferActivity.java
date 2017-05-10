package huanxing_print.com.cn.printhome.ui.activity.contact;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.constant.ConFig;
import huanxing_print.com.cn.printhome.model.contact.FriendInfo;
import huanxing_print.com.cn.printhome.net.callback.contact.MyFriendListCallback;
import huanxing_print.com.cn.printhome.net.request.contact.FriendManagerRequest;
import huanxing_print.com.cn.printhome.ui.adapter.ContactsItemAdapter;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.SharedPreferencesUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;
import huanxing_print.com.cn.printhome.util.contact.MyDecoration;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;

/**
 * Created by Administrator on 2017/5/10 0010.
 */

public class GroupOwnerTransferActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout ll_back;
    private RecyclerView recyclerView;
    private ArrayList<FriendInfo> friends;
    private ContactsItemAdapter adapter;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.activity_groupowner_transfer);
        EventBus.getDefault().register(getSelfActivity());
        initView();
        initData();
        setListener();
    }

    private void initView() {
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        recyclerView = (RecyclerView) findViewById(R.id.groupMemberView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new MyDecoration(this, MyDecoration.HORIZONTAL_LIST));

        adapter = new ContactsItemAdapter(getSelfActivity(), friends);
        recyclerView.setAdapter(adapter);
    }

    private void initData() {
        String token = SharedPreferencesUtils.getShareString(this, ConFig.SHAREDPREFERENCES_NAME,
                "loginToken");
        DialogUtils.showProgressDialog(this, "加载中").show();
        FriendManagerRequest.queryFriendList(this, token, myFriendListCallback);
    }

    private void setListener() {
        ll_back.setOnClickListener(this);
        adapter.setTypeItemClickerListener(new ContactsItemAdapter.OnTypeItemClickerListener() {
            @Override
            public void newFriendLister() {

            }

            @Override
            public void addressBookListener() {

            }

            @Override
            public void groupListener() {

            }

            @Override
            public void contactClick(FriendInfo info) {
                showTransferDialog(info.getMemberName());
            }
        });
    }

    /**
     * 提示
     * @param
     */
    private void showTransferDialog(final String memberName) {
        DialogUtils.showQunTransferDialog(getSelfActivity(), memberName, new DialogUtils.QunOwnerTransferDialogCallBack() {
            @Override
            public void transfer() {
                EventBus.getDefault().post("qunowner",memberName);//群主页面接受
                finishCurrentActivity();
            }
        }).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finishCurrentActivity();
                break;
        }
    }

    MyFriendListCallback myFriendListCallback = new MyFriendListCallback() {

        @Override
        public void success(String msg, ArrayList<FriendInfo> friendInfos) {
            DialogUtils.closeProgressDialog();
            if (null != friendInfos) {
                for (FriendInfo info : friendInfos) {
                    if (null == info.getMemberName()) {
                        info.setMemberName("Null");
                    }
                }
                friends = friendInfos;
                adapter.modify(friendInfos);
            }
        }

        @Override
        public void fail(String msg) {
            DialogUtils.closeProgressDialog();
            ToastUtil.doToast(getSelfActivity(), msg);
        }

        @Override
        public void connectFail() {
            DialogUtils.closeProgressDialog();
            toastConnectFail();
        }
    };
}
