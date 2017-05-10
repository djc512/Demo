package huanxing_print.com.cn.printhome.ui.activity.fragment;


import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;

import java.util.ArrayList;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseFragment;
import huanxing_print.com.cn.printhome.constant.ConFig;
import huanxing_print.com.cn.printhome.model.contact.FriendInfo;
import huanxing_print.com.cn.printhome.net.callback.contact.MyFriendListCallback;
import huanxing_print.com.cn.printhome.net.request.contact.FriendManagerRequest;
import huanxing_print.com.cn.printhome.ui.activity.contact.AddByAddressBookActivity;
import huanxing_print.com.cn.printhome.ui.activity.contact.AddContactActivity;
import huanxing_print.com.cn.printhome.ui.activity.contact.GroupActivity;
import huanxing_print.com.cn.printhome.ui.activity.contact.GroupSettingActivity;
import huanxing_print.com.cn.printhome.ui.activity.contact.NewFriendActivity;
import huanxing_print.com.cn.printhome.ui.adapter.ContactsItemAdapter;
import huanxing_print.com.cn.printhome.util.SharedPreferencesUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;
import huanxing_print.com.cn.printhome.util.contact.MyDecoration;
import huanxing_print.com.cn.printhome.view.IndexSideBar;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;

public class ContantsFragment extends BaseFragment implements
        OnClickListener, IndexSideBar.OnTouchLetterListener,ContactsItemAdapter.OnTypeItemClickerListener {

    private IndexSideBar indexSideBar;
    private RecyclerView contactsView;
    private ArrayList<FriendInfo> friends = new ArrayList<FriendInfo>();
    private ContactsItemAdapter adapter;
    private LinearLayoutManager layoutManager;

    @Override
    protected void init() {
        initView();
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    protected int getContextView() {
        return R.layout.frag_find;
    }

    private void initView() {
        indexSideBar = (IndexSideBar) findViewById(R.id.contacts_sidebar);
        indexSideBar.setOnTouchLetterListener(this);
        contactsView = (RecyclerView) findViewById(R.id.contactsView);
        layoutManager = new LinearLayoutManager(getActivity());
        contactsView.setHasFixedSize(true);
        contactsView.setLayoutManager(layoutManager);
        contactsView.addItemDecoration(new MyDecoration(getActivity(),MyDecoration.HORIZONTAL_LIST));

        adapter = new ContactsItemAdapter(getActivity(), friends);
        adapter.setTypeItemClickerListener(this);
        contactsView.setAdapter(adapter);

        findViewById(R.id.iv_add_contact).setOnClickListener(this);
    }

    private void initData() {
        String token = SharedPreferencesUtils.getShareString(getActivity(), ConFig.SHAREDPREFERENCES_NAME,
                "loginToken");
        DialogUtils.showProgressDialog(getActivity(),"加载中").show();
        FriendManagerRequest.queryFriendList(getActivity(),token,myFriendListCallback);
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
        if(null != adapter) {
            int position = adapter.getScrollPosition(letter);
            layoutManager.scrollToPositionWithOffset(position, 0);
        }
    }

    @Override
    public void onTouchedLetterListener() {

    }

    @Override
    public void newFriendLister() {
        ToastUtil.doToast(getActivity(),"newFriendLister");
        startActivity(NewFriendActivity.class);
    }

    @Override
    public void addressBookListener() {
        ToastUtil.doToast(getActivity(),"addressBookListener");
        startActivity(AddByAddressBookActivity.class);
    }

    @Override
    public void groupListener() {
        startGroupActivity();
        ToastUtil.doToast(getActivity(),"groupListener");
    }

    private void startActivity(Class cls) {
        Intent intent = new Intent(getActivity(), cls);
        startActivity(intent);
    }

    private void startGroupActivity() {
        Intent intent = new Intent(getActivity(), GroupActivity.class);
        startActivity(intent);
    }

    @Override
    public void contactClick(FriendInfo info) {
        ToastUtil.doToast(getActivity(),"点击了 --- " + info.getMemberName());
    }

    MyFriendListCallback myFriendListCallback = new MyFriendListCallback() {
        @Override
        public void success(String msg, ArrayList<FriendInfo> friendInfos) {
            DialogUtils.closeProgressDialog();
            if(null !=  friendInfos) {
                for(FriendInfo info : friendInfos) {
                    if(null == info.getMemberName()) {
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
            ToastUtil.doToast(getActivity(),msg);
        }

        @Override
        public void connectFail() {
            DialogUtils.closeProgressDialog();
            ToastUtil.doToast(getActivity(),"网络连接超时");
        }
    };

}
