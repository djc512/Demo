package huanxing_print.com.cn.printhome.ui.activity.contact;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.constant.ConFig;
import huanxing_print.com.cn.printhome.model.contact.FriendInfo;
import huanxing_print.com.cn.printhome.net.callback.contact.MyFriendListCallback;
import huanxing_print.com.cn.printhome.net.request.contact.FriendManagerRequest;
import huanxing_print.com.cn.printhome.ui.activity.chat.ChatActivity;
import huanxing_print.com.cn.printhome.ui.adapter.ContactsItemAdapter;
import huanxing_print.com.cn.printhome.util.SharedPreferencesUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;
import huanxing_print.com.cn.printhome.util.contact.MyDecoration;
import huanxing_print.com.cn.printhome.view.IndexSideBar;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;

/**
 * description: 从隐形跳过来选择联系人界面
 * author LSW
 * date 2017/5/16 13:42
 * update 2017/5/16
 */
public class ListAddContactActivity extends BaseActivity implements
        View.OnClickListener, IndexSideBar.OnTouchLetterListener,
        ContactsItemAdapter.OnTypeItemClickerListener {

    private static final int STARTNEWFRIEND = 1000;
    private IndexSideBar indexSideBar;
    private RecyclerView contactsView;
    private ArrayList<FriendInfo> friends = new ArrayList<FriendInfo>();
    private ContactsItemAdapter adapter;
    private LinearLayoutManager layoutManager;
    private String token;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_add_contact);

        initView();
        initData();
    }

    private void initView() {
        indexSideBar = (IndexSideBar) findViewById(R.id.contacts_sidebar);
        indexSideBar.setOnTouchLetterListener(this);
        contactsView = (RecyclerView) findViewById(R.id.contactsView);
        layoutManager = new LinearLayoutManager(this);
        contactsView.setHasFixedSize(true);
        contactsView.setLayoutManager(layoutManager);
        contactsView.addItemDecoration(new MyDecoration(this, MyDecoration.HORIZONTAL_LIST));

        adapter = new ContactsItemAdapter(getSelfActivity(), friends);
        adapter.setTypeItemClickerListener(this);
        contactsView.setAdapter(adapter);

        findViewById(R.id.iv_add_contact).setOnClickListener(this);
    }

    private void initData() {
        token = SharedPreferencesUtils.getShareString(getSelfActivity(), ConFig.SHAREDPREFERENCES_NAME,
                "loginToken");
        getData();
    }

    private void getData() {
        DialogUtils.showProgressDialog(getSelfActivity(), "加载中").show();
        FriendManagerRequest.queryFriendList(getSelfActivity(), token, myFriendListCallback);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
/*        case R.id.ll_communication:

             break;*/
            case R.id.iv_add_contact:
                startActivity(AddContactActivity.class);
                break;
            default:
                break;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onTouchingLetterListener(String letter) {
        if (null != adapter) {
            int position = adapter.getScrollPosition(letter);
            layoutManager.scrollToPositionWithOffset(position, 0);
        }
    }

    @Override
    public void onTouchedLetterListener() {

    }

    @Override
    public void newFriendLister() {
//        startActivity(NewFriendActivity.class);
        Intent intent = new Intent(getSelfActivity(), NewFriendActivity.class);
        startActivityForResult(intent, STARTNEWFRIEND);
    }

    @Override
    public void addressBookListener() {
        startActivity(AddByAddressBookActivity.class);
    }

    @Override
    public void groupListener() {
        startGroupActivity();
    }

    private void startActivity(Class cls) {
        Intent intent = new Intent(getSelfActivity(), cls);
        startActivity(intent);
    }

    private void startGroupActivity() {
        Intent intent = new Intent(getSelfActivity(), GroupActivity.class);
        startActivity(intent);
    }

    @Override
    public void contactClick(FriendInfo info) {
        Intent intent = new Intent(getSelfActivity(), ChatActivity.class);
        intent.putExtra("FriendInfo", info);
        startActivity(intent);
        //ToastUtil.doToast(getActivity(), "点击了 --- " + info.getMemberName());
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
            ToastUtil.doToast(getSelfActivity(), "网络连接超时");
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case STARTNEWFRIEND:
                if (resultCode == getSelfActivity().RESULT_OK) {
                    getData();
                }
                break;
        }
    }
}
