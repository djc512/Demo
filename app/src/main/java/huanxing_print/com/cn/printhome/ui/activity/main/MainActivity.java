package huanxing_print.com.cn.printhome.ui.activity.main;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.ActivityHelper;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.base.BaseFragment;
import huanxing_print.com.cn.printhome.model.chat.RefreshEvent;
import huanxing_print.com.cn.printhome.model.welcome.GetVersionBean;
import huanxing_print.com.cn.printhome.net.callback.welcome.VersionCallback;
import huanxing_print.com.cn.printhome.net.request.welcome.VersionRequset;
import huanxing_print.com.cn.printhome.ui.activity.fragment.ChatFragment;
import huanxing_print.com.cn.printhome.ui.activity.fragment.ContantsFragment;
import huanxing_print.com.cn.printhome.ui.activity.fragment.MyFragment;
import huanxing_print.com.cn.printhome.ui.activity.fragment.PrintFragment;
import huanxing_print.com.cn.printhome.ui.activity.fragment.fragapproval.ApplyFragment;
import huanxing_print.com.cn.printhome.util.AppUtils;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 主界面
 *
 * @author Administrator
 */
public class MainActivity extends BaseActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks {
    private Context mContext;
    private LinearLayout ll_bg;
    private RadioGroup rgp;

    private PrintFragment fragPrint;

    private MyFragment fragMy;

    private ContantsFragment fragContants;

    private ChatFragment fragChat;

    private ApplyFragment fragApply;

    private BaseFragment fragTemp;

    private FragmentManager fragMananger;

    private NotificationManager manager;

    private long exitTime;

    private TextView tv_count;
    private ImageView iv_chat;
    private TextView tv_chat;
    private ImageView iv_apply;
    private TextView tv_apply;
    private ImageView iv_print;
    private TextView tv_print;
    private ImageView iv_contacts;
    private TextView tv_contacts;
    private ImageView iv_my;
    private TextView tv_my;
    private List<ImageView> imageViewList;
    private List<TextView> textViewList;
    private String version;
    private static final int delayMillis = 2000;
    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 改变状态栏的颜色使其与APP风格一体化
        setContentView(R.layout.activity_main);
        CommonUtils.initSystemBar(this);
        mContext = MainActivity.this;
        version = AppUtils.getVersionName(getSelfActivity());
        EventBus.getDefault().register(mContext);
        initView();
        initListener();
        inData();
        initPermission();
        if (!ObjectUtils.isNull(baseApplication.getMemberId())) {
            MobclickAgent.onProfileSignIn(baseApplication.getMemberId());
        }
    }

    private void initView() {
        iv_chat = (ImageView) findViewById(R.id.iv_chat);
        tv_chat = (TextView) findViewById(R.id.tv_chat);
        iv_apply = (ImageView) findViewById(R.id.iv_apply);
        tv_apply = (TextView) findViewById(R.id.tv_apply);
        iv_print = (ImageView) findViewById(R.id.iv_print);
        tv_print = (TextView) findViewById(R.id.tv_print);
        iv_contacts = (ImageView) findViewById(R.id.iv_contacts);
        tv_contacts = (TextView) findViewById(R.id.tv_contacts);
        iv_my = (ImageView) findViewById(R.id.iv_my);
        tv_my = (TextView) findViewById(R.id.tv_my);
        tv_count = (TextView) findViewById(R.id.tv_count);

        textViewList = new ArrayList<>();
        textViewList.add(tv_chat);
        textViewList.add(tv_apply);
        textViewList.add(tv_print);
        textViewList.add(tv_contacts);
        textViewList.add(tv_my);
    }

    private void initListener() {

        findViewById(R.id.ll_chat).setOnClickListener(this);
        findViewById(R.id.ll_apply).setOnClickListener(this);
        findViewById(R.id.ll_print).setOnClickListener(this);
        findViewById(R.id.ll_contacts).setOnClickListener(this);
        findViewById(R.id.ll_my).setOnClickListener(this);
    }

    private void inData() {
        showUnreadMsgCount();
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        ll_bg = (LinearLayout) findViewById(R.id.ll_bg);

        fragMananger = getFragmentManager();

        fragPrint = new PrintFragment();
        fragMy = new MyFragment();
        fragContants = new ContantsFragment();
        fragChat = new ChatFragment();
        fragApply = new ApplyFragment();
        ll_bg.setBackgroundResource(R.color.white);

        findViewById(R.id.ll_print).performClick();


        // 判断是否有网络
        if (CommonUtils.isNetWorkConnected(this)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // autoLogin();
                    VersionRequset.updateVersion(getSelfActivity(), version,callback);
                }
            }, delayMillis);
        }else{
            toast("没有可用的网络连接，请打开蜂窝数据或者wifi");
        }
    }

    public void onClick(View v) {
        FragmentTransaction tran = fragMananger.beginTransaction();
        if (fragTemp != null && !fragTemp.isHidden()) {
            tran.hide(fragTemp);
        }
        switch (v.getId()) {
            case R.id.ll_chat:
                ll_bg.setBackgroundResource(R.color.gray5);
                iv_chat.setImageDrawable(getResources().getDrawable(R.drawable.main_tab_chat_on));
                iv_apply.setImageDrawable(getResources().getDrawable(R.drawable.main_tab_apply_off));
                iv_print.setImageDrawable(getResources().getDrawable(R.drawable.main_tab_print_off));
                iv_contacts.setImageDrawable(getResources().getDrawable(R.drawable.main_tab_contacts_off));
                iv_my.setImageDrawable(getResources().getDrawable(R.drawable.main_tab_my_off));
                setColor(0);//yixin
                fragTemp = fragChat;
                MobclickAgent.onEvent(this, "YinXin_Tab");
                break;
            case R.id.ll_apply:
                ll_bg.setBackgroundResource(R.color.gray5);
                iv_chat.setImageDrawable(getResources().getDrawable(R.drawable.main_tab_chat_off));
                iv_apply.setImageDrawable(getResources().getDrawable(R.drawable.main_tab_apply_on));
                iv_print.setImageDrawable(getResources().getDrawable(R.drawable.main_tab_print_off));
                iv_contacts.setImageDrawable(getResources().getDrawable(R.drawable.main_tab_contacts_off));
                iv_my.setImageDrawable(getResources().getDrawable(R.drawable.main_tab_my_off));
                setColor(1);//yixin
                fragTemp = fragApply;
                MobclickAgent.onEvent(this, "Application_Tab");
                break;
            case R.id.ll_print:
                ll_bg.setBackgroundResource(R.color.white);
                iv_chat.setImageDrawable(getResources().getDrawable(R.drawable.main_tab_chat_off));
                iv_apply.setImageDrawable(getResources().getDrawable(R.drawable.main_tab_apply_off));
                iv_print.setImageDrawable(getResources().getDrawable(R.drawable.main_tab_print_on));
                iv_contacts.setImageDrawable(getResources().getDrawable(R.drawable.main_tab_contacts_off));
                iv_my.setImageDrawable(getResources().getDrawable(R.drawable.main_tab_my_off));
                setColor(2);//yixin
                fragTemp = fragPrint;
                MobclickAgent.onEvent(this, "Print_Tab");
                break;
            case R.id.ll_contacts:
                ll_bg.setBackgroundResource(R.color.gray5);
                iv_chat.setImageDrawable(getResources().getDrawable(R.drawable.main_tab_chat_off));
                iv_apply.setImageDrawable(getResources().getDrawable(R.drawable.main_tab_apply_off));
                iv_print.setImageDrawable(getResources().getDrawable(R.drawable.main_tab_print_off));
                iv_contacts.setImageDrawable(getResources().getDrawable(R.drawable.main_tab_contacts_on));
                iv_my.setImageDrawable(getResources().getDrawable(R.drawable.main_tab_my_off));
                setColor(3);//yixin
                fragTemp = fragContants;
                MobclickAgent.onEvent(this, "Contact_Tab");
                break;
            case R.id.ll_my:
                ll_bg.setBackgroundResource(R.color.gray5);
                iv_chat.setImageDrawable(getResources().getDrawable(R.drawable.main_tab_chat_off));
                iv_apply.setImageDrawable(getResources().getDrawable(R.drawable.main_tab_apply_off));
                iv_print.setImageDrawable(getResources().getDrawable(R.drawable.main_tab_print_off));
                iv_contacts.setImageDrawable(getResources().getDrawable(R.drawable.main_tab_contacts_off));
                iv_my.setImageDrawable(getResources().getDrawable(R.drawable.main_tab_my_on));
                setColor(4);//yixin
                fragTemp = fragMy;
                MobclickAgent.onEvent(this, "Mine_Tab");
                break;
            default:
                break;

        }
        if (fragTemp.isAdded()) {
           if(fragTemp instanceof ApplyFragment){
                ((ApplyFragment) fragTemp).reload();
            }else if (fragTemp instanceof ContantsFragment) {
                ((ContantsFragment) fragTemp).reload();
            }else if(fragTemp instanceof MyFragment){
                ((MyFragment) fragTemp).reload();
            }
            tran.show(fragTemp);
        }else {
            tran.add(R.id.fl_main_context, fragTemp);
        }
        tran.commitAllowingStateLoss();
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        if (fragTemp instanceof PrintFragment) {
            fragPrint.onActivityResult(arg0, arg1, arg2);
        } else if (fragTemp instanceof ContantsFragment) {
            fragContants.onActivityResult(arg0, arg1, arg2);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                ActivityHelper.getInstance().finishAllActivity();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void msgNum(RefreshEvent event) {
        //显示印信聊天未读消息数
        if (event.getCode() == 0x14) {
            showUnreadMsgCount();
        }
    }

    @Override
    protected void onResume() {
        showUnreadMsgCount();
        super.onResume();
        MobclickAgent.onPageStart("打印首页");
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("打印首页");
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(mContext);
        super.onDestroy();
    }

    private void initPermission() {
        if (!isPermissionsGranted()) {
            getPermissions();
        }
    }

    final String[] permissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_PHONE_STATE
    };

    private boolean isPermissionsGranted() {
        boolean isPermission;
        if (EasyPermissions.hasPermissions(this, permissions)) {
            isPermission = true;
        } else {
            isPermission = false;
        }
        return isPermission;
    }

    private void getPermissions() {
        EasyPermissions.requestPermissions(this, "请允许必要的权限", 1, permissions);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    /**
     * gaibianzhaungtai
     */
    private void setColor(int index) {
        for (int i = 0; i < textViewList.size(); i++) {
            if (index == i) {
                //imageViewList.get(index).setImageBitmap(bitmap);
                textViewList.get(index).setTextColor(getResources().getColor(R.color.text_color));
            } else {
                //imageViewList.get(i).setImageBitmap(bitmap1);
                textViewList.get(i).setTextColor(getResources().getColor(R.color.gray2));
            }
        }
    }


    private VersionCallback callback = new VersionCallback() {

        @Override
        public void success(GetVersionBean bean) {
            baseApplication.setHasLoginEvent(false);
            if (!ObjectUtils.isNull(bean)) {
                //String deployTime = bean.getDeployTime(); // 发布日期
                final String downloadUrl = bean.getDownloadUrl(); // 下载地址
                String isForceUpdate = bean.getIsForceUpdate(); // 是否要强制更新
                String isNew = bean.getIsNew(); // 是否是最新版本0 否 1 是最新版本
                String versionCode = bean.getVersionCode(); // 版本号
                //String versionDetail = bean.getVersionDetail(); // 版本更新细节
                baseApplication.setApkUrl(downloadUrl);
                if ("0".equals(isNew)) {
                    baseApplication.setNewApp(false);
                    if(!ObjectUtils.isNull(isForceUpdate)&&"1".equals(isForceUpdate)){
                        DialogUtils.showVersionDialog(getSelfActivity(),new DialogUtils.VersionDialogCallBack() {

                            public void update() {
                                if (!ObjectUtils.isNull(downloadUrl)) {
                                    Uri uri = Uri.parse(downloadUrl);
                                    Intent it = new Intent(Intent.ACTION_VIEW, uri);
                                    startActivity(it);
                                }
                            }
                        }).show();

                    }
                }else{
                    baseApplication.setNewApp(true);
                }
            }

        }
        @Override
        public void fail(String msg) {
            ToastUtil.doToast(getSelfActivity(),"服务器连接失败，请检查网络！");
        }

        @Override
        public void connectFail() {
            ToastUtil.doToast(getSelfActivity(),"网络连接失败，请检查网络！");
            //autoLogin();
        }

    };

    int unReadNum = 0;
    /**
     * 显示印信聊天未读消息数
     */
    public void showUnreadMsgCount() {
        unReadNum = EMClient.getInstance().chatManager().getUnreadMessageCount();
        if (unReadNum > 0) {
            tv_count.setVisibility(View.VISIBLE);
            tv_count.setText(unReadNum + "");
        } else {
            tv_count.setVisibility(View.GONE);
        }
    }
}
