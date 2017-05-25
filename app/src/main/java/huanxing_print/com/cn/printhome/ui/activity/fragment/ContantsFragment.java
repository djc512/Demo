package huanxing_print.com.cn.printhome.ui.activity.fragment;


import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.hyphenate.chat.EMClient;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.ActivityHelper;
import huanxing_print.com.cn.printhome.base.BaseFragment;
import huanxing_print.com.cn.printhome.constant.ConFig;
import huanxing_print.com.cn.printhome.event.contacts.FriendUpdate;
import huanxing_print.com.cn.printhome.model.contact.FriendInfo;
import huanxing_print.com.cn.printhome.net.callback.contact.MyFriendListCallback;
import huanxing_print.com.cn.printhome.net.request.contact.FriendManagerRequest;
import huanxing_print.com.cn.printhome.ui.activity.chat.ChatTestActivity;
import huanxing_print.com.cn.printhome.ui.activity.contact.AddByAddressBookActivity;
import huanxing_print.com.cn.printhome.ui.activity.contact.AddContactActivity;
import huanxing_print.com.cn.printhome.ui.activity.contact.GroupActivity;
import huanxing_print.com.cn.printhome.ui.activity.contact.NewFriendActivity;
import huanxing_print.com.cn.printhome.ui.activity.login.LoginActivity;
import huanxing_print.com.cn.printhome.ui.adapter.ContactsItemAdapter;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.util.SharedPreferencesUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;
import huanxing_print.com.cn.printhome.util.contact.MyDecoration;
import huanxing_print.com.cn.printhome.view.IndexSideBar;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;
import huanxing_print.com.cn.printhome.view.dialog.LoadingDialog;

public class ContantsFragment extends BaseFragment implements
        OnClickListener, IndexSideBar.OnTouchLetterListener, ContactsItemAdapter.OnTypeItemClickerListener {
    private static final int STARTNEWFRIEND = 1000;
    private IndexSideBar indexSideBar;
    private RecyclerView contactsView;
    private ArrayList<FriendInfo> friends = new ArrayList<FriendInfo>();
    private ContactsItemAdapter adapter;
    private LinearLayoutManager layoutManager;
    private String token;
    private LoadingDialog loadingDialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

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
        loadingDialog = new LoadingDialog(getActivity());
        indexSideBar = (IndexSideBar) findViewById(R.id.contacts_sidebar);
        indexSideBar.setOnTouchLetterListener(this);
        contactsView = (RecyclerView) findViewById(R.id.contactsView);
        layoutManager = new LinearLayoutManager(getActivity());
        contactsView.setHasFixedSize(true);
        contactsView.setLayoutManager(layoutManager);
        contactsView.addItemDecoration(new MyDecoration(getActivity(), MyDecoration.HORIZONTAL_LIST, 1, ContextCompat.getColor(getActivity(), R.color.recycler_divider_color)));

        adapter = new ContactsItemAdapter(getActivity(), friends);
        adapter.setTypeItemClickerListener(this);
        contactsView.setAdapter(adapter);

        findViewById(R.id.iv_add_contact).setOnClickListener(this);
    }

    private void initData() {
        token = SharedPreferencesUtils.getShareString(getActivity(), ConFig.SHAREDPREFERENCES_NAME,
                "loginToken");
        getData();
    }

    public void reload() {
        getData();
    }

    private void getData() {
        loadingDialog.show();
//        DialogUtils.showProgressDialog(getActivity(), "Loading").show();
        FriendManagerRequest.queryFriendList(getActivity(), token, myFriendListCallback);
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

    //无论产生事件的是否是UI线程，都在新的线程中执行
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onMessageEventAsync(FriendUpdate messageEvent) {
        Log.e("CMCC", messageEvent.getResultMessage());
        FriendManagerRequest.queryFriendList(getActivity(), token, myFriendListCallback);
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
        Intent intent = new Intent(getActivity(), NewFriendActivity.class);
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
        Intent intent = new Intent(getActivity(), cls);
        startActivity(intent);
    }

    private void startGroupActivity() {
        Intent intent = new Intent(getActivity(), GroupActivity.class);
        startActivity(intent);
    }

    @Override
    public void contactClick(FriendInfo info) {
        Intent intent = new Intent(getActivity(), ChatTestActivity.class);
        intent.putExtra("FriendInfo", info);
        startActivity(intent);
        //ToastUtil.doToast(getActivity(), "点击了 --- " + info.getMemberName());
    }

    MyFriendListCallback myFriendListCallback = new MyFriendListCallback() {
        @Override
        public void success(String msg, ArrayList<FriendInfo> friendInfos) {
            loadingDialog.dismiss();
//            DialogUtils.closeProgressDialog();
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
            loadingDialog.dismiss();
//            DialogUtils.closeProgressDialog();
            if (!ObjectUtils.isNull(msg)&&"用户未登录".equals(msg)){
                SharedPreferencesUtils.clearAllShareValue(getActivity());
                ActivityHelper.getInstance().finishAllActivity();
                EMClient.getInstance().logout(true);//环信退出
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }else{
                showToast(msg);
            }
        }

        @Override
        public void connectFail() {
            loadingDialog.dismiss();
//            DialogUtils.closeProgressDialog();
            ToastUtil.doToast(getActivity(), "网络连接超时");
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case STARTNEWFRIEND:
                if (resultCode == getActivity().RESULT_OK) {
                    getData();
                }
                break;
        }
    }
}
