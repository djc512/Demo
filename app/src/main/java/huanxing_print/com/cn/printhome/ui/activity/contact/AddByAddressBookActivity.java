package huanxing_print.com.cn.printhome.ui.activity.contact;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneNumberUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.constant.ConFig;
import huanxing_print.com.cn.printhome.model.contact.FriendSearchInfo;
import huanxing_print.com.cn.printhome.model.contact.PhoneContactInfo;
import huanxing_print.com.cn.printhome.net.callback.contact.PhoneContactCallback;
import huanxing_print.com.cn.printhome.net.request.contact.FriendManagerRequest;
import huanxing_print.com.cn.printhome.ui.adapter.AddAddressBookAdapter;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.SharedPreferencesUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;
import huanxing_print.com.cn.printhome.util.contact.GetContactsUtils;
import huanxing_print.com.cn.printhome.util.contact.MyDecoration;
import huanxing_print.com.cn.printhome.view.IndexSideBar;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;

/**
 * Created by wanghao on 2017/5/4.
 */

public class AddByAddressBookActivity extends BaseActivity implements View.OnClickListener, IndexSideBar.OnTouchLetterListener,AddAddressBookAdapter.OnItemBtnListener {
    private IndexSideBar indexSideBar;
    private RecyclerView contactsView;
    private ArrayList<PhoneContactInfo> contactInfos = new ArrayList<PhoneContactInfo>();
    private AddAddressBookAdapter adapter;
    private LinearLayoutManager layoutManager;
    private PhoneContactInfo currentClickPhoneContact;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.activity_add_by_address_book);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        indexSideBar = (IndexSideBar) findViewById(R.id.contacts_sidebar);
        indexSideBar.setOnTouchLetterListener(this);
        contactsView = (RecyclerView) findViewById(R.id.contactsView);

        layoutManager = new LinearLayoutManager(this);
        contactsView.setHasFixedSize(true);
        contactsView.setLayoutManager(layoutManager);
        contactsView.addItemDecoration(new MyDecoration(this, MyDecoration.HORIZONTAL_LIST));

        adapter = new AddAddressBookAdapter(this, contactInfos);
        adapter.setOnItemBtnListener(this);
        contactsView.setAdapter(adapter);
    }

    private void initData() {
        HandlerThread queryThread = new HandlerThread("query_contact");
        queryThread.start();
        Handler handler = new Handler(queryThread.getLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                GetContactsUtils contactsUtils = new GetContactsUtils(AddByAddressBookActivity.this);
                contactInfos = (ArrayList<PhoneContactInfo>) contactsUtils.getSystemContactInfos();

                adapter.modifyData(contactInfos);
                return false;
            }
        });
        handler.sendEmptyMessage(0);
    }

    private void setListener() {
        findViewById(R.id.ll_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finishCurrentActivity();
                break;
        }
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
    public void itemBtn(PhoneContactInfo contactInfo) {
        this.currentClickPhoneContact = contactInfo;
        checkPhone();
    }

    private void checkPhone() {
        String token = SharedPreferencesUtils.getShareString(this, ConFig.SHAREDPREFERENCES_NAME,
                "loginToken");
        DialogUtils.showProgressDialog(this, "验证中");
        ArrayList<PhoneContactInfo> infos = new ArrayList<PhoneContactInfo>();
        infos.add(currentClickPhoneContact);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("telList", infos);
        FriendManagerRequest.checkTelNo(this, token, params, phoneContactCallback);
    }

    PhoneContactCallback phoneContactCallback = new PhoneContactCallback() {
        @Override
        public void success(String msg, ArrayList<FriendSearchInfo> searchInfos) {
            DialogUtils.closeProgressDialog();
            if(null != searchInfos) {
//                FriendSearchInfo friendSearchInfo = getClickPhoneFriend(searchInfos);
                //下列是假数据
                FriendSearchInfo friendSearchInfo = getClickPhoneFriend(data());
                checkNextStep(friendSearchInfo);
            }
        }

        @Override
        public void fail(String msg) {
            DialogUtils.closeProgressDialog();
            ToastUtil.doToast(AddByAddressBookActivity.this, msg + " -- 假数据");
            //下列是假数据
            FriendSearchInfo friendSearchInfo = getClickPhoneFriend(data());
            checkNextStep(friendSearchInfo);
        }

        @Override
        public void connectFail() {
            DialogUtils.closeProgressDialog();
            toastConnectFail();
            //下列是假数据
            FriendSearchInfo friendSearchInfo = getClickPhoneFriend(data());
            checkNextStep(friendSearchInfo);
        }
    };

    private void checkNextStep(FriendSearchInfo info) {
        if(null != info) {
            if (!info.isMember()) {
                ToastUtil.doToast(this, "不是印家用户");
                showInvitation(info);
            } else {
                if (info.isFriend()) {
                    ToastUtil.doToast(this, "是好友");
                } else {
                    ToastUtil.doToast(this, "不是好友");
                    ArrayList<FriendSearchInfo> infos = new ArrayList<FriendSearchInfo>();
                    infos.add(info);
                    startActivity(infos);
                }
            }
        }
    }

    private FriendSearchInfo getClickPhoneFriend(ArrayList<FriendSearchInfo> searchInfos) {
        for (FriendSearchInfo info : searchInfos) {
            if(currentClickPhoneContact.getTelNo().equals(info.getTelNo())) {
                return info;
            }
        }
        return null;
    }

    private Dialog dialog;
    private void showInvitation(final FriendSearchInfo info) {
        dialog = new Dialog(this,R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_invitation, null);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        inflate.setLayoutParams(layoutParams);

//        //初始化控件
        View btn_cancel = inflate.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        View btn_weiXin = inflate.findViewById(R.id.btn_weiXin);
        btn_weiXin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                invitationWeiXin(info,"印家邀请");
            }
        });
        View btn_message = inflate.findViewById(R.id.btn_message);
        btn_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                invitationMessage(info,"印家邀请");
            }
        });
        //将布局设置给Dialog
        dialog.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        lp.y = 20;//设置Dialog距离底部的距离
        //设置窗口宽度为充满全屏
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //设置窗口高度为包裹内容
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//       将属性设置给窗体
        dialogWindow.setAttributes(lp);
        dialog.show();//显示对话框
    }

    private void invitationMessage(FriendSearchInfo info,String message) {
        if(null != info && (!info.getTelNo().isEmpty()) && PhoneNumberUtils.isGlobalPhoneNumber(info.getTelNo())){
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+info.getTelNo()));
            intent.putExtra("sms_body", message);
            startActivity(intent);
        }
    }

    private void invitationWeiXin(FriendSearchInfo info,String message) {
        ToastUtil.doToast(this,"微信分享，问陆成宋");
    }

    /**
     * 假数据
     * @return
     */
    private ArrayList<FriendSearchInfo> data() {
        ArrayList<FriendSearchInfo> searchInfos = new ArrayList<FriendSearchInfo>();
        FriendSearchInfo info = new FriendSearchInfo();
        info.setMember(true);
        info.setFriend(false);
        info.setTelNo("15850793218");
        info.setMemberName("陆成宋");
        info.setUniqueId("1867989");
        info.setMemberId("123456");
        info.setMemberUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494660151&di=fc28cd4cd681bb1d70df6ff6654791ff&imgtype=jpg&er=1&src=http%3A%2F%2Fimgsrc.baidu.com%2Fforum%2Fw%253D580%2Fsign%3D8c03c118ca8065387beaa41ba7dda115%2Fc17fc0bf6c81800a06c8cd58b13533fa828b4759.jpg");
        searchInfos.add(info);
        return searchInfos;
    }

    private void startActivity(ArrayList<FriendSearchInfo> infos) {
        Intent intent = new Intent(AddByAddressBookActivity.this, SearchAddResultActivity.class);
        intent.putParcelableArrayListExtra("search result", infos);
        startActivity(intent);
    }
}
