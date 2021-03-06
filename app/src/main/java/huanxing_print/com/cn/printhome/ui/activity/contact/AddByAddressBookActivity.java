package huanxing_print.com.cn.printhome.ui.activity.contact;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
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
import huanxing_print.com.cn.printhome.constant.HttpUrl;
import huanxing_print.com.cn.printhome.model.contact.FriendSearchInfo;
import huanxing_print.com.cn.printhome.model.contact.PhoneContactInfo;
import huanxing_print.com.cn.printhome.net.callback.contact.PhoneContactCallback;
import huanxing_print.com.cn.printhome.net.request.contact.FriendManagerRequest;
import huanxing_print.com.cn.printhome.ui.activity.chat.ChatTestActivity;
import huanxing_print.com.cn.printhome.ui.adapter.AddAddressBookAdapter;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.SharedPreferencesUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;
import huanxing_print.com.cn.printhome.util.WeiXinUtils;
import huanxing_print.com.cn.printhome.util.contact.GetContactsUtils;
import huanxing_print.com.cn.printhome.util.contact.MyDecoration;
import huanxing_print.com.cn.printhome.view.IndexSideBar;
import huanxing_print.com.cn.printhome.view.dialog.LoadingDialog;

/**
 * Created by wanghao on 2017/5/4.
 */

public class AddByAddressBookActivity extends BaseActivity implements View.OnClickListener, IndexSideBar.OnTouchLetterListener, AddAddressBookAdapter.OnItemBtnListener {
    private IndexSideBar indexSideBar;
    private RecyclerView contactsView;
    private ArrayList<PhoneContactInfo> contactInfos = new ArrayList<PhoneContactInfo>();
    private AddAddressBookAdapter adapter;
    private LinearLayoutManager layoutManager;
    private PhoneContactInfo currentClickPhoneContact;
    private String shareAppUrl;
    private LoadingDialog loadingDialog;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_by_address_book);
        CommonUtils.initSystemBar(this);
        initView();
        initData();
        setListener();

    }

    private void initView() {
        loadingDialog = new LoadingDialog(this);
        indexSideBar = (IndexSideBar) findViewById(R.id.contacts_sidebar);
        indexSideBar.setOnTouchLetterListener(this);
        contactsView = (RecyclerView) findViewById(R.id.contactsView);

        layoutManager = new LinearLayoutManager(this);
        contactsView.setHasFixedSize(true);
        contactsView.setLayoutManager(layoutManager);
        contactsView.addItemDecoration(new MyDecoration(this, MyDecoration.HORIZONTAL_LIST, 1, ContextCompat.getColor(this, R.color.recycler_divider_color)));

        adapter = new AddAddressBookAdapter(this, contactInfos);
        adapter.setOnItemBtnListener(this);
        contactsView.setAdapter(adapter);
    }

    private void initData() {
        shareAppUrl = String.format(HttpUrl.getInstance().getPostUrl()+HttpUrl.appDownLoad+
                "?memberId="+baseApplication.getMemberId()+"&platform="+ConFig.PHONE_TYPE);
//        DialogUtils.showProgressDialog(this, "加载中").show();
        loadingDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                GetContactsUtils contactsUtils = new GetContactsUtils(AddByAddressBookActivity.this);
                contactInfos = (ArrayList<PhoneContactInfo>) contactsUtils.getSystemContactInfos();
                handler.post(updateThread);
            }
        }).start();
    }

    private Handler handler = new Handler();
    Runnable updateThread = new Runnable() {

        @Override
        public void run() {
//            DialogUtils.closeProgressDialog();
            loadingDialog.dismiss();
            //更新UI
            adapter.modifyData(contactInfos);
        }

    };


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
        if (baseApplication.getPhone().equals(contactInfo.getTelNo())) {
            ToastUtil.doToast(AddByAddressBookActivity.this, "不能添加自己为联系人");
            return;
        }
        this.currentClickPhoneContact = contactInfo;
        checkPhone();
    }

    private void checkPhone() {
        String token = SharedPreferencesUtils.getShareString(this, ConFig.SHAREDPREFERENCES_NAME,
                "loginToken");
//        DialogUtils.showProgressDialog(this, "验证中").show();
        loadingDialog.show();
        ArrayList<PhoneContactInfo> infos = new ArrayList<PhoneContactInfo>();
        infos.add(currentClickPhoneContact);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("telList", infos);
        FriendManagerRequest.checkTelNo(this, token, params, phoneContactCallback);
    }

    PhoneContactCallback phoneContactCallback = new PhoneContactCallback() {
        @Override
        public void success(String msg, ArrayList<FriendSearchInfo> searchInfos) {
//            DialogUtils.closeProgressDialog();
            loadingDialog.dismiss();
            if (null != searchInfos) {
                FriendSearchInfo friendSearchInfo = getClickPhoneFriend(searchInfos);
                checkNextStep(friendSearchInfo);
            }
        }

        @Override
        public void fail(String msg) {
//            DialogUtils.closeProgressDialog();
            loadingDialog.dismiss();
            ToastUtil.doToast(AddByAddressBookActivity.this, msg);
        }

        @Override
        public void connectFail() {
//            DialogUtils.closeProgressDialog();
            loadingDialog.dismiss();
            toastConnectFail();
        }
    };

    private void checkNextStep(FriendSearchInfo info) {
        if (null != info) {
            if (0 == info.getIsMember()) {
                showInvitation(info);
            } else {
                if (1 == info.getIsFriend()) {
                    Intent intent = new Intent(AddByAddressBookActivity.this, ChatTestActivity.class);
                    intent.putExtra("FriendSearchInfo", info);
                    startActivity(intent);
                } else {
                    ArrayList<FriendSearchInfo> infos = new ArrayList<FriendSearchInfo>();
                    infos.add(info);
                    startActivity(infos);
                }
            }
        }
    }

    private FriendSearchInfo getClickPhoneFriend(ArrayList<FriendSearchInfo> searchInfos) {
        for (FriendSearchInfo info : searchInfos) {
            if (currentClickPhoneContact.getTelNo().equals(info.getTelNo())) {
                return info;
            }
        }
        return null;
    }

    private Dialog dialog;

    private void showInvitation(final FriendSearchInfo info) {
        dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
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
                invitationWeiXin(info, "印家邀请");
            }
        });
        View btn_message = inflate.findViewById(R.id.btn_message);
        btn_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                invitationMessage(info, "印家邀请");
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

    private void invitationMessage(FriendSearchInfo info, String message) {
        if (null != info && (!info.getTelNo().isEmpty()) && PhoneNumberUtils.isGlobalPhoneNumber(info.getTelNo())) {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + info.getTelNo()));
            intent.putExtra("sms_body", String.format("%1$s邀请您使用印家打印App %2$s", baseApplication.getNickName(), shareAppUrl));
            startActivity(intent);
        }
    }

    private void invitationWeiXin(FriendSearchInfo info, String message) {
        WeiXinUtils weiXinUtils = WeiXinUtils.getInstance();
        weiXinUtils.init(this, baseApplication.WX_APPID);
//        weiXinUtils.shareToWXSceneSession(message);
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.appicon_print);
        weiXinUtils.shareToWxSceneSession(String.format("%s邀请您使用印家打印", baseApplication.getNickName()), "我在用印家打印APP,打印、办公非常方便,快来下载吧", shareAppUrl, bmp);
    }

    private void startActivity(ArrayList<FriendSearchInfo> infos) {
        Intent intent = new Intent(AddByAddressBookActivity.this, SearchAddResultActivity.class);
        intent.putParcelableArrayListExtra("search result", infos);
        startActivity(intent);
    }
}
