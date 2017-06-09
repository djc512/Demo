package huanxing_print.com.cn.printhome.base;

import android.app.ActivityManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.media.SoundPool;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.dreamlive.cn.clog.CollectLog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMGroupChangeListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.controller.EaseUI;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import com.zhy.http.okhttp.OkHttpUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;
import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.constant.ConFig;
import huanxing_print.com.cn.printhome.event.chat.GroupMessageUpdateInRed;
import huanxing_print.com.cn.printhome.event.contacts.GroupUpdate;
import huanxing_print.com.cn.printhome.model.chat.GroupMessageObject;
import huanxing_print.com.cn.printhome.model.chat.RefreshEvent;
import huanxing_print.com.cn.printhome.model.contact.GroupMessageInfo;
import huanxing_print.com.cn.printhome.net.callback.contact.GroupMessageCallback;
import huanxing_print.com.cn.printhome.net.request.contact.GroupManagerRequest;
import huanxing_print.com.cn.printhome.ui.activity.chat.ChatTestActivity;
import huanxing_print.com.cn.printhome.ui.activity.login.LoginActivity;
import huanxing_print.com.cn.printhome.ui.activity.main.MainActivity;
import huanxing_print.com.cn.printhome.ui.adapter.MessageListenerAdapter;
import huanxing_print.com.cn.printhome.util.Constant;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.util.SharedPreferencesUtils;
import huanxing_print.com.cn.printhome.util.ThreadUtils;
import huanxing_print.com.cn.printhome.util.ToastUtils;
import okhttp3.OkHttpClient;

//import com.dreamlive.cn.clog.CollectLog;

public class BaseApplication extends Application {
    private String loginToken;//登录校验token，需要登录的接口需要校验该token
    private String phone, passWord;
    private String easemobId;//环信聊天用户id
    private String uniqueId;//印家号
    private String memberId;//会员id
    private String nickName;
    private String wechatName;
    private String comId;
    private String uniqueModifyFlag;//能否修改印家号
    private String totleBalance;//能否修改印家号
    private SoundPool mSoundPool;
    private int mDuanSound;
    private int mYuluSound;
    private List<BaseActivity> mBaseActivityList = new ArrayList<>();
    private String sex;
    private String headImg;
    private String wechatId;
    private String address, city;
    private double lat, lon;
    public static int num = 9;
    //判断是否第一次进入
    private boolean isFirstEnter = true;
    //判断App是否为最新版本
    private boolean isNewApp = false;
    //APK下载地址
    private String ApkUrl;
    //判断是否登录
    private boolean hasLoginEvent = false;
    //微信第三方登录
    //正式
    public static final String WX_APPID = "wxb54a2ee8a63993f9";
    public static final String WX_APPSecret = "c8c5ed7d1e388e54cb5a1b4c1af35663";
    //测试
//    public static final String WX_APPID = "wx4c877768d9a9fc08";
//    public static final String WX_APPSecret = "d7ba93d327cfdd1d02b8d5a4b43b1223";

    private static final String HW_APPID = "100015649";
    private IWXAPI api;

    private static BaseApplication mInstance;
    private ArrayList<GroupMessageObject> infos;
    private Gson gson;
    private ExecutorService service = Executors.newSingleThreadExecutor();

    public synchronized static BaseApplication getInstance() {
        return mInstance;
    }

    public boolean isFirstEnter() {
        isFirstEnter = SharedPreferencesUtils.getShareBoolean(this, "isFirstEnter", true);
        return isFirstEnter;
    }

    public void setFirstEnter(boolean firstEnter) {
        SharedPreferencesUtils.putShareValue(this, "isFirstEnter", firstEnter);
        isFirstEnter = firstEnter;
    }

    public boolean isNewApp() {
        isNewApp = SharedPreferencesUtils.getShareBoolean(this, "isNewApp", true);
        return isNewApp;
    }

    public void setNewApp(boolean newApp) {
        SharedPreferencesUtils.putShareValue(this, "isNewApp", newApp);
        isNewApp = newApp;
    }

    public String getApkUrl() {
        if (ObjectUtils.isNull(ApkUrl)) {
            ApkUrl = SharedPreferencesUtils.getShareString(this, "ApkUrl");
        }
        return ApkUrl;
    }

    public void setApkUrl(String apkUrl) {
        SharedPreferencesUtils.putShareValue(this, "ApkUrl", apkUrl);
        ApkUrl = apkUrl;
    }

    public boolean isHasLoginEvent() {
        hasLoginEvent = SharedPreferencesUtils.getShareBoolean(this, "hasLoginEvent", false);
        return hasLoginEvent;
    }

    public void setHasLoginEvent(boolean hasLoginEvent) {
        SharedPreferencesUtils.putShareValue(this, "hasLoginEvent", hasLoginEvent);
        this.hasLoginEvent = hasLoginEvent;
    }

    public String getLoginToken() {

        if (ObjectUtils.isNull(loginToken)) {
            loginToken = SharedPreferencesUtils.getShareString(this, "loginToken");
        }

        return loginToken;
    }

    public void setLoginToken(String loginToken) {
        SharedPreferencesUtils.putShareValue(this, "loginToken", loginToken);
        this.loginToken = loginToken;
    }

    public String getPhone() {
        if (ObjectUtils.isNull(phone)) {
            phone = SharedPreferencesUtils.getShareString(this, "phone");
        }
        return phone;
    }

    public void setPhone(String phone) {
        SharedPreferencesUtils.putShareValue(this, "phone", phone);
        this.phone = phone;
    }

    public String getPassWord() {
        if (ObjectUtils.isNull(passWord)) {
            passWord = SharedPreferencesUtils.getShareString(this, "passWord");
        }
        return passWord;
    }

    public void setPassWord(String passWord) {
        SharedPreferencesUtils.putShareValue(this, "passWord", passWord);
        this.passWord = passWord;
    }

    public String getEasemobId() {
        if (ObjectUtils.isNull(easemobId)) {
            easemobId = SharedPreferencesUtils.getShareString(this, "easemobId");
        }
        return easemobId;
    }

    public void setEasemobId(String easemobId) {
        SharedPreferencesUtils.putShareValue(this, "easemobId", easemobId);
        this.easemobId = easemobId;
    }

    public String getUniqueId() {
        if (ObjectUtils.isNull(uniqueId)) {
            uniqueId = SharedPreferencesUtils.getShareString(this, "uniqueId");
        }
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        SharedPreferencesUtils.putShareValue(this, "uniqueId", uniqueId);
        this.uniqueId = uniqueId;
    }

    public String getUniqueModifyFlag() {
        if (ObjectUtils.isNull(uniqueModifyFlag)) {
            uniqueModifyFlag = SharedPreferencesUtils.getShareString(this, "uniqueModifyFlag");
        }
        return uniqueModifyFlag;
    }

    public void setUniqueModifyFlag(String uniqueModifyFlag) {
        SharedPreferencesUtils.putShareValue(this, "uniqueModifyFlag", uniqueModifyFlag);
        this.uniqueModifyFlag = uniqueModifyFlag;
    }

    public String getMemberId() {
        if (ObjectUtils.isNull(memberId)) {
            memberId = SharedPreferencesUtils.getShareString(this, "memberId");
        }
        return memberId;
    }

    public void setMemberId(String memberId) {
        SharedPreferencesUtils.putShareValue(this, "memberId", memberId);
        this.memberId = memberId;
    }

    public String getTotleBalance() {
        if (ObjectUtils.isNull(totleBalance)) {
            totleBalance = SharedPreferencesUtils.getShareString(this, "totleBalance");
        }
        return totleBalance;
    }

    public void setTotleBalance(String totleBalance) {
        SharedPreferencesUtils.putShareValue(this, "totleBalance", totleBalance);
        this.totleBalance = totleBalance;
    }

    public String getWechatName() {
        if (ObjectUtils.isNull(wechatName)) {
            wechatName = SharedPreferencesUtils.getShareString(this, "wechatName");
        }
        return wechatName;
    }

    public void setWechatName(String wechatName) {
        SharedPreferencesUtils.putShareValue(this, "wechatName", wechatName);
        this.wechatName = wechatName;
    }

    public String getNickName() {
        if (ObjectUtils.isNull(nickName)) {
            nickName = SharedPreferencesUtils.getShareString(this, "nickName");
        }
        return nickName;
    }

    public void setNickName(String nickName) {
        SharedPreferencesUtils.putShareValue(this, "nickName", nickName);
        this.nickName = nickName;
    }

    public String getComId() {
        if (ObjectUtils.isNull(comId)) {
            comId = SharedPreferencesUtils.getShareString(this, "comId");
        }
        return comId;
    }

    public void setComId(String comId) {
        SharedPreferencesUtils.putShareValue(this, "comId", comId);
        this.comId = comId;
    }

    public String getSex() {
        if (ObjectUtils.isNull(sex)) {
            sex = SharedPreferencesUtils.getShareString(this, "sex");
        }
        return sex;
    }

    public void setSex(String sex) {
        SharedPreferencesUtils.putShareValue(this, "sex", sex);
        this.sex = sex;
    }

    public String getHeadImg() {
        if (ObjectUtils.isNull(headImg)) {
            headImg = SharedPreferencesUtils.getShareString(this, "headImg");
        }
        return headImg;
    }

    public void setHeadImg(String headImg) {
        SharedPreferencesUtils.putShareValue(this, "headImg", headImg);
        this.headImg = headImg;
    }

    public String getWechatId() {
        if (ObjectUtils.isNull(wechatId)) {
            wechatId = SharedPreferencesUtils.getShareString(this, "wechatId");
        }
        return wechatId;
    }

    public void setWechatId(String wechatId) {
        SharedPreferencesUtils.putShareValue(this, "wechatId", wechatId);
        this.wechatId = wechatId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        // 置入一个不设防的VmPolicy（不设置的话 7.0以上一调用拍照功能就崩溃了）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
        EaseUI.getInstance().init(this, null);
        CollectLog clog = CollectLog.getInstance();
        clog.init(this, Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "YinJia");
        api = WXAPIFactory.createWXAPI(this, WX_APPID, true);
        api.registerApp(WX_APPID);
        //initJPush();
        initHttpConnection();
        ZXingLibrary.initDisplayOpinion(this);
        initHuanxin();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void initHuanxin() {
        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        options.setAutoLogin(true);
        options.setHuaweiPushAppId(HW_APPID);//设置华为手机推送服务
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
// 如果APP启用了远程的service，此application:onCreate会被调用2次
// 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
// 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回

        if (processAppName == null || !processAppName.equalsIgnoreCase(getPackageName())) {
            //Log.e(TAG, "enter the service process!");

            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }

        //初始化
        EMClient.getInstance().init(this, options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);
        //添加消息的监听
        initMessageListener();
        //监听连接状态的改变
        initConnectionListener();

    }


    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }

    private void initMessageListener() {
        EMClient.getInstance().chatManager().addMessageListener(new MessageListenerAdapter() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onMessageReceived(List<EMMessage> list) {
                super.onMessageReceived(list);
                if (list != null && list.size() > 0) {
                    /**
                     * 1. 判断当前应用是否在后台运行
                     * 2. 如果是在后台运行，则发出通知栏
                     * 3. 如果是在后台发出长声音
                     * 4. 如果在前台发出短声音
                     */
                    Log.d("CMCC", "收到消息了666666666666666666666666666666");

                    //发消息去刷新会话列表界面
                    RefreshEvent event = new RefreshEvent();
                    event.setCode(0x14);
                    EventBus.getDefault().post(event);


                    if (isRuninBackground()) {
                        sendNotification(list.get(0));
                        //发出长声音
                        //参数2/3：左右喇叭声音的大小
                        //mSoundPool.play(mYuluSound, 1, 1, 0, 0, 1);
                    } else {
                        //发出短声音
                        //mSoundPool.play(mDuanSound, 1, 1, 0, 0, 1);
                    }
                    //EventBus.getDefault().post(list.get(0));

                    //群聊才处理
                    final EMMessage message = list.get(0);
                    gson = new Gson();
                    if (message.getChatType() == EMMessage.ChatType.GroupChat ||
                            message.getChatType() == EMMessage.ChatType.ChatRoom) {
                        //放到线程里面去处理
                        service.submit(new Runnable() {
                            @Override
                            public void run() {
                                //跟本地群信息对比没有就插入
                                boolean isTwo = false;
                                String group = SharedPreferencesUtils.getShareString(getBaseContext(), "group");
                                Type type = new TypeToken<ArrayList<GroupMessageObject>>() {
                                }.getType();
                                if (!ObjectUtils.isNull(group)) {
                                    //不为空解析
                                    infos = gson.fromJson(group, type);
                                    //找到头像和昵称
                                    for (GroupMessageObject info : infos) {
                                        if (info.getGroupEaseId().equals(message.getTo())) {
                                            isTwo = true;
                                        }
                                    }
                                    //判断有没有找到群头像
                                    if (!isTwo) {
                                        //去请求群信息
                                        GroupManagerRequest.queryGroupMessage(getBaseContext(),
                                                getLoginToken(), "", message.getTo(),
                                                new GroupMessageCallback() {
                                                    @Override
                                                    public void success(String msg, GroupMessageInfo groupMessageInfo) {
                                                        Log.d("CMCC", "111" + msg);
                                                        if (!ObjectUtils.isNull(groupMessageInfo)) {
                                                            Log.d("CMCC", "groupMessageInfo");
                                                            GroupMessageObject object = new GroupMessageObject();
                                                            object.setGroupId(groupMessageInfo.getGroupId());
                                                            object.setGroupEaseId(message.getTo());
                                                            object.setGroupName(groupMessageInfo.getGroupName());
                                                            object.setGroupUrl(groupMessageInfo.getGroupUrl());

                                                            infos.add(object);
                                                            //存储
                                                            SharedPreferencesUtils.putShareValue(getBaseContext(), "group", gson.toJson(infos));
                                                            //发消息去刷新会话列表界面
                                                            RefreshEvent event = new RefreshEvent();
                                                            event.setCode(0x14);
                                                            EventBus.getDefault().post(event);
                                                        }
                                                    }

                                                    @Override
                                                    public void fail(String msg) {
                                                        Log.d("CMCC", "11fail" + msg);
                                                    }

                                                    @Override
                                                    public void connectFail() {
                                                        Log.d("CMCC", "connectFail");
                                                    }
                                                });
                                    }
                                } else {
                                    //为空
                                    //去请求群信息
                                    GroupManagerRequest.queryGroupMessage(getBaseContext(),
                                            getLoginToken(), "", message.getTo(),
                                            new GroupMessageCallback() {
                                                @Override
                                                public void success(String msg, GroupMessageInfo groupMessageInfo) {
                                                    Log.d("CMCC", "222" + msg);
                                                    if (!ObjectUtils.isNull(groupMessageInfo)) {
                                                        GroupMessageObject object = new GroupMessageObject();
                                                        object.setGroupId(groupMessageInfo.getGroupId());
                                                        object.setGroupEaseId(message.getTo());
                                                        object.setGroupName(groupMessageInfo.getGroupName());
                                                        object.setGroupUrl(groupMessageInfo.getGroupUrl());
                                                        //第一次存储
                                                        ArrayList<GroupMessageObject> objects = new ArrayList<>();
                                                        objects.add(object);
                                                        SharedPreferencesUtils.putShareValue(getBaseContext(), "group", gson.toJson(objects));

                                                        //发消息去刷新会话列表界面
                                                        RefreshEvent event = new RefreshEvent();
                                                        event.setCode(0x14);
                                                        EventBus.getDefault().post(event);
                                                    }

                                                }

                                                @Override
                                                public void fail(String msg) {
                                                    Log.d("CMCC", "222fail" + msg);
                                                }

                                                @Override
                                                public void connectFail() {
                                                    Log.d("CMCC", "connectFail");
                                                }
                                            });
                                }
                            }
                        });
                    }

                }
            }
        });

        EMClient.getInstance().groupManager().addGroupChangeListener(new EMGroupChangeListener() {
            @Override
            public void onInvitationReceived(String groupId, String groupName, String inviter, String reason) {
                //接收到群组加入邀请
            }

            @Override
            public void onRequestToJoinReceived(String groupId, String groupName, String applyer, String reason) {
                //用户申请加入群
            }

            @Override
            public void onRequestToJoinAccepted(String groupId, String groupName, String accepter) {
                //加群申请被同意
            }

            @Override
            public void onRequestToJoinDeclined(String groupId, String groupName, String decliner, String reason) {
                //加群申请被拒绝
            }

            @Override
            public void onInvitationAccepted(String groupId, String inviter, String reason) {
                //群组邀请被同意
            }

            @Override
            public void onInvitationDeclined(String groupId, String invitee, String reason) {
                //群组邀请被拒绝
            }

            @Override
            public void onUserRemoved(String s, String s1) {
                EventBus.getDefault().post(new GroupUpdate("groupUpdate"));
            }

            @Override
            public void onGroupDestroyed(String s, String s1) {

            }

            @Override
            public void onAutoAcceptInvitationFromGroup(String groupId, String inviter, String inviteMessage) {
                //接收邀请时自动加入到群组的通知
                EventBus.getDefault().post(new GroupUpdate("groupUpdate"));
            }

            @Override
            public void onMuteListAdded(String groupId, final List<String> mutes, final long muteExpire) {
                //成员禁言的通知
            }

            @Override
            public void onMuteListRemoved(String groupId, final List<String> mutes) {
                //成员从禁言列表里移除通知
            }

            @Override
            public void onAdminAdded(String groupId, String administrator) {
                //增加管理员的通知
            }

            @Override
            public void onAdminRemoved(String groupId, String administrator) {
                //管理员移除的通知
            }

            @Override
            public void onOwnerChanged(String groupId, String newOwner, String oldOwner) {
                //群所有者变动通知
            }

            @Override
            public void onMemberJoined(final String groupId, final String member) {
                //群组加入新成员通知
                GroupMessageUpdateInRed updateInRed = new GroupMessageUpdateInRed("updateMember", groupId, member, true);
                EventBus.getDefault().post(updateInRed);
            }

            @Override
            public void onMemberExited(final String groupId, final String member) {
                //群成员退出通知
                GroupMessageUpdateInRed updateInRed = new GroupMessageUpdateInRed("updateMember", groupId, member, false);
                EventBus.getDefault().post(updateInRed);
            }

        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void sendNotification(EMMessage message) {
        EMTextMessageBody messageBody = (EMTextMessageBody) message.getBody();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //延时意图
        /**
         * 参数2：请求码 大于1
         */
        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Intent chatIntent = new Intent(this, ChatTestActivity.class);
        if (message.getChatType() == EMMessage.ChatType.Chat) {
            chatIntent.putExtra(Constant.EXTRA_USER_ID, message.getFrom());
        } else {
            chatIntent.putExtra(Constant.EXTRA_USER_ID, message.getTo());
            chatIntent.putExtra(Constant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_GROUP);
        }

        chatIntent.putExtra("nickName", message.getStringAttribute("nickName", ""));
        chatIntent.putExtra("iconUrl", message.getStringAttribute("iconUrl", ""));

        Intent[] intents = {mainIntent, chatIntent};
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 1, intents, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setAutoCancel(true) //当点击后自动删除
                .setSmallIcon(R.mipmap.message) //必须设置
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.appicon_print))
                .setContentTitle("您有一条新消息")
                .setContentText(messageBody.getMessage())
                .setContentInfo(message.getFrom())
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_MAX)
                .build();
        notificationManager.notify(1, notification);
    }

    private boolean isRuninBackground() {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTasks = activityManager.getRunningTasks(100);
        ActivityManager.RunningTaskInfo runningTaskInfo = runningTasks.get(0);
        if (runningTaskInfo.topActivity.getPackageName().equals(getPackageName())) {
            return false;
        } else {
            return true;
        }
    }


    private void initConnectionListener() {
        EMClient.getInstance().addConnectionListener(new EMConnectionListener() {
            @Override
            public void onConnected() {
            }

            @Override
            public void onDisconnected(int i) {
                if (i == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                    // 显示帐号在其他设备登录
                    /**
                     *  将当前任务栈中所有的Activity给清空掉
                     *  重新打开登录界面
                     */
                    // 这里实现你的逻辑即可
                    SharedPreferencesUtils.clearAllShareValue(BaseApplication.this);
                    ActivityHelper.getInstance().finishAllActivity();
                    EMClient.getInstance().logout(true);//环信退出

                    Intent intent = new Intent(BaseApplication.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.showToast(BaseApplication.this, "您已在其他设备上登录了，请重新登录。");
                        }
                    });

                }
            }
        });
    }


    private void initHttpConnection() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                // .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(ConFig.CONNECT_TIME_OUT, TimeUnit.MILLISECONDS)
                .readTimeout(ConFig.CONNECT_TIME_OUT, TimeUnit.MILLISECONDS).build();

        OkHttpUtils.initClient(okHttpClient);

    }

    private void initJPush() {
        JPushInterface.setDebugMode(false);     // 设置开启日志,发布时请关闭日志
        JPushInterface.init(getApplicationContext());             // 初始化 JPush
    }


}
