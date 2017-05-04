package huanxing_print.com.cn.printhome.ui.activity.contact;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneNumberUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import java.util.ArrayList;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.model.contact.PhoneContactInfo;
import huanxing_print.com.cn.printhome.ui.adapter.AddressBookAdapter;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;
import huanxing_print.com.cn.printhome.util.contact.MyDecoration;
import huanxing_print.com.cn.printhome.view.IndexSideBar;

/**
 * Created by wanghao on 2017/5/3.
 */

public class AddressBookActivity extends BaseActivity implements View.OnClickListener, IndexSideBar.OnTouchLetterListener,AddressBookAdapter.OnOptionListener{
    private LinearLayout ll_back;
    private IndexSideBar indexSideBar;
    private RecyclerView contactsView;
    private LinearLayoutManager layoutManager;
    private AddressBookAdapter adapter;
    private ArrayList<PhoneContactInfo> contactInfos = new ArrayList<PhoneContactInfo>();
    private Dialog dialog;
    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.activity_address_book);

        initView();
        initData();
        setListener();
    }

    private void initData(){
        PhoneContactInfo info01 = new PhoneContactInfo();
        info01.setPhoneName("阿大");
        info01.setPhoneNum("18956567878");
        info01.setFriendState(PhoneContactInfo.STATE.FRIEND.ordinal());

        PhoneContactInfo info02 = new PhoneContactInfo();
        info02.setPhoneName("Aixy");
        info02.setPhoneNum("025-7866566");
        info02.setFriendState(PhoneContactInfo.STATE.UNREGISTED.ordinal());


        PhoneContactInfo info03 = new PhoneContactInfo();
        info03.setPhoneName("边宝玉");
        info03.setPhoneNum("13767676544");
        info03.setFriendState(PhoneContactInfo.STATE.FRIEND.ordinal());


        PhoneContactInfo info04 = new PhoneContactInfo();
        info04.setPhoneName("宝龙");
        info04.setPhoneNum("15952199676");
        info04.setFriendState(PhoneContactInfo.STATE.NOTFRIEND.ordinal());

        contactInfos.add(info01);
        contactInfos.add(info02);
        contactInfos.add(info03);
        contactInfos.add(info04);

        adapter.modifyData(contactInfos);
    }

    private void initView(){
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        indexSideBar = (IndexSideBar) findViewById(R.id.addressBook_sidebar);
        indexSideBar.setOnTouchLetterListener(this);
        contactsView = (RecyclerView) findViewById(R.id.addressBookView);

        layoutManager = new LinearLayoutManager(this);
        contactsView.setHasFixedSize(true);
        contactsView.setLayoutManager(layoutManager);
        contactsView.addItemDecoration(new MyDecoration(this, MyDecoration.VERTICAL_LIST));

        adapter = new AddressBookAdapter(this, contactInfos);
        adapter.setOnOptionListener(this);
        contactsView.setAdapter(adapter);
    }

    private void setListener() {
        ll_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_back:
                finishCurrentActivity();
                break;
        }
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
    public void invitation(PhoneContactInfo info) {
        showInvitation(info);
    }

    @Override
    public void addFriend(PhoneContactInfo info) {
        ToastUtil.doToast(this,"添加好友");
    }

    private void showInvitation(final PhoneContactInfo info) {
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

    private void invitationMessage(PhoneContactInfo info,String message) {
        if(null != info && (!info.getPhoneNum().isEmpty()) && PhoneNumberUtils.isGlobalPhoneNumber(info.getPhoneNum())){
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+info.getPhoneNum()));
            intent.putExtra("sms_body", message);
            startActivity(intent);
        }
    }

    private void invitationWeiXin(PhoneContactInfo info,String message) {
        ToastUtil.doToast(this,"微信分享，问陆成宋");
    }
}
