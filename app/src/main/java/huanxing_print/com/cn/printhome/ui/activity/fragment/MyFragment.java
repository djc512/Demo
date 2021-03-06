package huanxing_print.com.cn.printhome.ui.activity.fragment;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import de.hdodenhof.circleimageview.CircleImageView;
import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.ActivityHelper;
import huanxing_print.com.cn.printhome.base.BaseFragment;
import huanxing_print.com.cn.printhome.constant.ConFig;
import huanxing_print.com.cn.printhome.constant.HttpUrl;
import huanxing_print.com.cn.printhome.event.print.PrintPaySuccessEvent;
import huanxing_print.com.cn.printhome.model.chat.RefreshEvent;
import huanxing_print.com.cn.printhome.model.my.MyInfoBean;
import huanxing_print.com.cn.printhome.net.callback.my.MyInfoCallBack;
import huanxing_print.com.cn.printhome.net.request.my.MyInfoRequest;
import huanxing_print.com.cn.printhome.ui.activity.login.LoginActivity;
import huanxing_print.com.cn.printhome.ui.activity.my.AccountActivity;
import huanxing_print.com.cn.printhome.ui.activity.my.MingXiActivity;
import huanxing_print.com.cn.printhome.ui.activity.my.MyActivity;
import huanxing_print.com.cn.printhome.ui.activity.my.WebViewCommunityActivity;
import huanxing_print.com.cn.printhome.ui.activity.my.WebViewSuggestActivity;
import huanxing_print.com.cn.printhome.util.BitmapUtils;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.util.SharedPreferencesUtils;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;
import huanxing_print.com.cn.printhome.view.dialog.LoadingDialog;

public class MyFragment extends BaseFragment implements OnClickListener {

    private CircleImageView iv_head;
    private TextView tv_uniqueid, tv_name, tv_print_count, tv_totle_balance;
    private String token;
    private String headUrl;
    private String nickName, wechatName;
    private String phone;
    private String totleBalance;
    private String printCount;
    private String uniqueId;//印家号
    private String wechatId;
    private String uniqueModifyFlag;//能否修改印家号

    private LoadingDialog loadingDialog;

    @Override
    protected void init() {
        EventBus.getDefault().register(this);
        org.greenrobot.eventbus.EventBus.getDefault().register(this);
        initViews();
        initData();
        setListener();
    }

    private Bitmap bitMap;

    @Override
    public void onResume() {
        super.onResume();
        if (null != bitMap) {
            iv_head.setImageBitmap(bitMap);
        }
        if (!ObjectUtils.isNull(name)) {
            tv_name.setText(name);
        }
        if (!ObjectUtils.isNull(uniqueId)) {
            tv_uniqueid.setText(uniqueId);
        }
    }

    @Subscriber(tag = "head")
    private void getHead(Bitmap bitMap) {
        // 这里实现你的逻辑即可
        this.bitMap = bitMap;
    }

    private String name;

    @Subscriber(tag = "name")
    private void getname(String name) {
        this.name = name;
    }

    @Subscriber(tag = "uniqueId")
    private void getuniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onMessageEvent(PrintPaySuccessEvent printPaySuccessEvent) {
        getData();
    }
    @Override
    protected int getContextView() {
        return R.layout.frag_usercenter;

    }
    public void reload() {
        getData();
    }
    private void initData() {
        token = SharedPreferencesUtils.getShareString(getActivity(), ConFig.SHAREDPREFERENCES_NAME,
                "loginToken");
        uniqueId = SharedPreferencesUtils.getShareString(getActivity(), ConFig.SHAREDPREFERENCES_NAME,
                "uniqueId");
        getData();
    }
    private void getData() {
        loadingDialog.show();
        // DialogUtils.showProgressDialog(getActivity(), "加载中").show();
        //网络请求，获取用户信息
        MyInfoRequest.getMyInfo(getActivity(), token, new MyMyInfoCallBack());
    }

    public class MyMyInfoCallBack extends MyInfoCallBack {

        @Override
        public void success(String msg, MyInfoBean bean) {
            loadingDialog.dismiss();

            if (!ObjectUtils.isNull(bean)) {
                headUrl = bean.getFaceUrl();
                nickName = bean.getNickName();
                wechatName = bean.getWechatName();
                phone = bean.getMobileNumber();
                printCount = bean.getTotlePrintCount();
                totleBalance = bean.getTotleBalance();
                uniqueModifyFlag = bean.getUniqueModifyFlag();
                // monthConsume = bean.getMonthConsume();
                wechatId = bean.getWechatId();
                if (!ObjectUtils.isNull(wechatName)) {
                    SharedPreferencesUtils.putShareValue(getActivity(), "wechatName", wechatName);
                }
                if (!ObjectUtils.isNull(uniqueModifyFlag)) {
                    SharedPreferencesUtils.putShareValue(getActivity(),
                            "uniqueModifyFlag", uniqueModifyFlag);
                }
                SharedPreferencesUtils.putShareValue(getActivity(),
                        "totleBalance", totleBalance);
                SharedPreferencesUtils.putShareValue(getActivity(),"nickName", nickName);
                tv_name.setText(nickName);
                tv_uniqueid.setText("印家号:" + uniqueId);
                tv_print_count.setText(printCount);
                tv_totle_balance.setText(totleBalance);
                //设置用户头像
                BitmapUtils.displayImage(getActivity(), headUrl, R.drawable.iv_head, iv_head);
            }
        }

        @Override
        public void fail(String msg) {
            loadingDialog.dismiss();
            if (!ObjectUtils.isNull(msg)&&"用户未登录".equals(msg)){
                // 这里实现你的逻辑即可
                SharedPreferencesUtils.clearAllShareValue(getActivity());
                ActivityHelper.getInstance().finishAllActivity();
                EMClient.getInstance().logout(true);//环信退出
               // activityExitAnim();
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }else{
                showToast(msg);
            }
        }

        @Override
        public void connectFail() {
            loadingDialog.dismiss();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void refreshMyInfo(RefreshEvent event) {
        if (0x11 == event.getCode()) {
            //网络请求，获取用户信息  更新UI
            //Log.d("CMCC", "接收到了消息!");
            MyInfoRequest.getMyInfo(getActivity(), token, new MyMyInfoCallBack());
        }
    }

    private void initViews() {
        loadingDialog = new LoadingDialog(getActivity());
        tv_uniqueid = (TextView) findViewById(R.id.tv_uniqueid);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_print_count = (TextView) findViewById(R.id.tv_print_count);
        tv_totle_balance = (TextView) findViewById(R.id.tv_totle_balance);
        iv_head = (CircleImageView) findViewById(R.id.iv_head);

    }

    private void setListener() {

        findViewById(R.id.ll_my_account).setOnClickListener(this);
        findViewById(R.id.ll_my_contact).setOnClickListener(this);
        findViewById(R.id.ll_station).setOnClickListener(this);
        findViewById(R.id.ll_join).setOnClickListener(this);
        findViewById(R.id.ll_my_community).setOnClickListener(this);
        findViewById(R.id.rl_userMsg).setOnClickListener(this);
        findViewById(R.id.ll_printnum).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_userMsg:
                startActivity(new Intent(getActivity(), MyActivity.class));
                break;
            case R.id.ll_my_account:
                Intent accIntent = new Intent(getActivity(), AccountActivity.class);
                accIntent.putExtra("totleBalance", totleBalance);
                startActivity(accIntent);
                break;
            case R.id.ll_station://布点建议
                Intent stationIntent = new Intent(getActivity(), WebViewSuggestActivity.class);
                stationIntent.putExtra("titleName", "布点建议");
                stationIntent.putExtra("webUrl", HttpUrl.getInstance().getHtmUrl()+HttpUrl.myLay);
                startActivity(stationIntent);
                break;
            case R.id.ll_join://打印点加盟
                Intent joinIntent = new Intent(getActivity(), WebViewCommunityActivity.class);
                joinIntent.putExtra("titleName", "打印点加盟");
                joinIntent.putExtra("webUrl", HttpUrl.getInstance().getHtmUrl()+HttpUrl.myEarn);
                startActivity(joinIntent);
                break;
            case R.id.ll_my_contact:
                DialogUtils.showCallDialog(getActivity(), getResources().getString(R.string.dlg_call),
                        new DialogUtils.CallDialogCallBack() {
                            @Override
                            public void ok() {
                                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "400-666-2060"));
                                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    return;
                                }
                                startActivity(intent);
                            }
                        }).show();

                //startActivity(new Intent(getActivity(), MyContactActivity.class));
                break;
            case R.id.ll_my_community:
                //startActivity(new Intent(getActivity(), CommunityListActivity.class));
                Intent communityIntent = new Intent(getActivity(), WebViewCommunityActivity.class);
                communityIntent.putExtra("webUrl", HttpUrl.getInstance().getHtmUrl()+HttpUrl.community);
                startActivity(communityIntent);
                break;
//            case R.id.ll_my_set:
////                startActivity(new Intent(getActivity(), SettingActivity.class));
////                startActivity(new Intent(getActivity(), ApprovalActivity.class));
//                startActivity(new Intent(getActivity(), CommentActivity.class));
//                break;
            case R.id.ll_printnum:
                startActivity(new Intent(getActivity(), MingXiActivity.class));
                break;

            default:
                break;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        org.greenrobot.eventbus.EventBus.getDefault().unregister(this);
    }
}
