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
        DialogUtils.showProgressDialog(getActivity(),"加载中");
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
        intent.putExtra("friends", friends);
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
            if(null !=  friendInfos && friendInfos.size() > 0) {
//                friends = friendInfos;
//                adapter.modify(friendInfos);
                //假数据
                friends.addAll(data());
                adapter.modify(friendInfos);
            }
        }

        @Override
        public void fail(String msg) {
            DialogUtils.closeProgressDialog();
            ToastUtil.doToast(getActivity(),msg + " -- 假数据");
            //假数据
            friends = data();
            adapter.modify(friends);
        }

        @Override
        public void connectFail() {
            DialogUtils.closeProgressDialog();
            ToastUtil.doToast(getActivity(),"网络连接超时");
            //假数据
            friends = data();
            adapter.modify(friends);
        }
    };

    /**
     * 假数据，后面要删除
     */
    private ArrayList<FriendInfo> data(){
        FriendInfo data01 = new FriendInfo();
        data01.setMemberName("汪浩");
        data01.setMemberUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494660151&di=fc28cd4cd681bb1d70df6ff6654791ff&imgtype=jpg&er=1&src=http%3A%2F%2Fimgsrc.baidu.com%2Fforum%2Fw%253D580%2Fsign%3D8c03c118ca8065387beaa41ba7dda115%2Fc17fc0bf6c81800a06c8cd58b13533fa828b4759.jpg");

        FriendInfo data02 = new FriendInfo();
        data02.setMemberName("陆成宋");
        data02.setMemberUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494065434200&di=7c53b18639aa82a8a58a296b9502d4ee&imgtype=0&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fzhidao%2Fwh%253D450%252C600%2Fsign%3D7048a12f9e16fdfad839ceea81bfa062%2F6a63f6246b600c3350e384cc194c510fd9f9a118.jpg");

        FriendInfo data03 = new FriendInfo();
        data03.setMemberName("123");
        data03.setMemberUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494065546496&di=a861d2debdefd088f50efa05393043dc&imgtype=jpg&src=http%3A%2F%2Fimg3.imgtn.bdimg.com%2Fit%2Fu%3D893187487%2C386198762%26fm%3D214%26gp%3D0.jpg");

        FriendInfo data04 = new FriendInfo();
        data04.setMemberName("汪浩01");
        data04.setMemberUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494065434199&di=85b82a89a5b9cb3403033e90dc2dc2a1&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fforum%2Fw%253D580%2Fsign%3Dddf0103f252dd42a5f0901a3333a5b2f%2Fa4a8805494eef01f30f35d93e0fe9925bd317da3.jpg");

        FriendInfo data05 = new FriendInfo();
        data05.setMemberName("陆成宋01");
        data05.setMemberUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494065434198&di=83e45ffe0f07e6336bbbd1cdc284e9a5&imgtype=0&src=http%3A%2F%2Fwenwen.soso.com%2Fp%2F20140404%2F20140404111309-1793362574.jpg");

        FriendInfo data06 = new FriendInfo();
        data06.setMemberName("陆成宋02");
        data06.setMemberUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494065730069&di=12540a26599230b583e0bf4f477cc8d7&imgtype=0&src=http%3A%2F%2Fwww.qxjlm.com%2Ftupians%2Fbd16072941.jpg");

        FriendInfo data07 = new FriendInfo();
        data07.setMemberName("陆成宋03");
        data07.setMemberUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494065434198&di=2271e06f1c39be89ce18f3ab9746c7d1&imgtype=0&src=http%3A%2F%2Fwww.lsswgs.com%2Fqqwebhimgs%2Fuploads%2Fbd24449651.jpg");

        FriendInfo data08 = new FriendInfo();
        data08.setMemberName("陆成宋04");
        data08.setMemberUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494065662993&di=1d4fabe377e894277d005e17818ea64b&imgtype=jpg&src=http%3A%2F%2Fimg3.imgtn.bdimg.com%2Fit%2Fu%3D575752541%2C1102211525%26fm%3D214%26gp%3D0.jpg");

        FriendInfo data09 = new FriendInfo();
        data09.setMemberName("陆成宋05");
        data09.setMemberUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494065434197&di=9043dfda20c52ecfa1676e3999658669&imgtype=0&src=http%3A%2F%2Fwenwen.soso.com%2Fp%2F20111030%2F20111030173922-1579981974.jpg");

        FriendInfo data10 = new FriendInfo();
        data10.setMemberName("陆成宋06");
        data10.setMemberUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494065434197&di=af5f1a36863ac751b71b1e167e57a8e7&imgtype=0&src=http%3A%2F%2Fwenwen.soso.com%2Fp%2F20131201%2F20131201114242-1190841548.jpg");

        FriendInfo data11 = new FriendInfo();
        data11.setMemberName("陆成宋07");
        data11.setMemberUrl("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3609186921,1453144627&fm=23&gp=0.jpg");

        FriendInfo data12 = new FriendInfo();
        data12.setMemberName("陆成宋08");
        data12.setMemberUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494065730070&di=b4ae9186fcb1795ee5bec334ec893997&imgtype=0&src=http%3A%2F%2Fwww.qxjlm.com%2Ftupians%2Fbd13706313.jpg");
        friends.add(data01);
        friends.add(data02);
        friends.add(data03);
        friends.add(data04);
        friends.add(data05);
        friends.add(data06);
        friends.add(data07);
        friends.add(data08);
        friends.add(data09);
        friends.add(data10);
        friends.add(data11);
        friends.add(data12);
        return friends;
    }
}
