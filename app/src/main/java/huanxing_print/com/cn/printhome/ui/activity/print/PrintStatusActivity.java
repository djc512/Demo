package huanxing_print.com.cn.printhome.ui.activity.print;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseApplication;
import huanxing_print.com.cn.printhome.constant.HttpUrl;
import huanxing_print.com.cn.printhome.event.print.FinishEvent;
import huanxing_print.com.cn.printhome.event.print.PrintTypeEvent;
import huanxing_print.com.cn.printhome.log.Logger;
import huanxing_print.com.cn.printhome.model.my.PrintDetailBean;
import huanxing_print.com.cn.printhome.model.my.TotleBalanceBean;
import huanxing_print.com.cn.printhome.model.print.ADInfo;
import huanxing_print.com.cn.printhome.model.print.OrderStatusResp;
import huanxing_print.com.cn.printhome.model.print.PrintInfoResp;
import huanxing_print.com.cn.printhome.net.callback.my.ADCallBack;
import huanxing_print.com.cn.printhome.net.callback.my.PrintDetailCallBack;
import huanxing_print.com.cn.printhome.net.callback.my.TotleBalanceCallBack;
import huanxing_print.com.cn.printhome.net.request.my.ADListRequest;
import huanxing_print.com.cn.printhome.net.request.my.PrintDetailRequest;
import huanxing_print.com.cn.printhome.net.request.my.TotleBalanceRequest;
import huanxing_print.com.cn.printhome.net.request.print.HttpListener;
import huanxing_print.com.cn.printhome.net.request.print.PrintRequest;
import huanxing_print.com.cn.printhome.util.FileUtils;
import huanxing_print.com.cn.printhome.util.GsonUtil;
import huanxing_print.com.cn.printhome.util.ShowUtil;
import huanxing_print.com.cn.printhome.util.ViewFactory;
import huanxing_print.com.cn.printhome.util.WeiXinUtils;
import huanxing_print.com.cn.printhome.view.cycleviewpager.CycleViewPager;
import huanxing_print.com.cn.printhome.view.cycleviewpager.CycleViewPager.ImageCycleViewListener;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;
import huanxing_print.com.cn.printhome.view.dialog.LoadingDialog;


public class PrintStatusActivity extends BasePrintActivity implements View.OnClickListener {

    private TextView stateTv, tv_money;
    private TextView stateDetailTv;
    private TextView countTv;
    private RelativeLayout successRyt;
    private RelativeLayout stateRyt;
    private LinearLayout exceptionLyt;
    private LinearLayout ll_contact;
    private ImageView animImg;

    private OrderStatusResp orderStatusResp;
    private PrintInfoResp.PrinterPrice printerPrice;
    private long orderId;
    private boolean isAwaking;
    private boolean isAwaked = false;
    private PrintTypeEvent printTypeEvent;
    private boolean comment;
    private ReceiveBroadCast receiveBroadCast;

    private List<ImageView> views = new ArrayList<ImageView>();
    private List<ADInfo> infos = new ArrayList<ADInfo>();
    private CycleViewPager cycleViewPager;

    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(context);
        delImageView();
        initView();
        initData();
        setUpload();
        timer.schedule(task, 1000 * 3, 1000 * 2);
    }

    @Override
    protected void setContent() {
        setContentView(R.layout.activity_print_status);
    }

    private void delImageView() {
        String path = Environment.getExternalStorageDirectory().getPath() + "/image/";
        File f = new File(Environment.getExternalStorageDirectory().getPath() + "/image/");
        if (f != null) {
            FileUtils.delImageDir(f);
        }
    }

    private void initData() {
        orderId = getIntent().getExtras().getLong(ORDER_ID);
        printerPrice = getIntent().getExtras().getParcelable(PRINTER_PRICE);
        if (null != infos && infos.size() > 0) {
            infos.clear();
        }
        ADListRequest.request(context, adCallback);
//        Logger.i(printerPrice.toString());
//        Logger.i(orderId);
    }


    private ADCallBack adCallback = new ADCallBack() {


        @Override
        public void fail(String msg) {

        }

        @Override
        public void connectFail() {

        }

        @Override
        public void success(List<ADInfo> bean) {
            if (null != bean && bean.size() > 0) {
                infos.addAll(bean);
                configImageLoader();
                initialize();
            }
        }
    };


    private void initView() {
        initTitleBar("打印");
        loadingDialog = new LoadingDialog(context);
        successRyt = (RelativeLayout) findViewById(R.id.successRyt);
        stateRyt = (RelativeLayout) findViewById(R.id.stateRyt);
        exceptionLyt = (LinearLayout) findViewById(R.id.exceptionLyt);
        ll_contact = (LinearLayout) findViewById(R.id.ll_contact);

        animImg = (ImageView) findViewById(R.id.animImg);
        stateTv = (TextView) findViewById(R.id.stateTv);
        tv_money = (TextView) findViewById(R.id.tv_money);
        stateDetailTv = (TextView) findViewById(R.id.stateDetailTv);
        countTv = (TextView) findViewById(R.id.countTv);

        findViewById(R.id.errorExitTv).setOnClickListener(this);
        findViewById(R.id.printTv).setOnClickListener(this);
        findViewById(R.id.shareRyt).setOnClickListener(this);
        findViewById(R.id.commentRyt).setOnClickListener(this);
        findViewById(R.id.backImg).setOnClickListener(this);
        findViewById(R.id.rightTv).setOnClickListener(this);
        ll_contact.setOnClickListener(this);
    }

    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)
    public void onMessageEventPostThread(PrintTypeEvent printTypeEvent) {
        this.printTypeEvent = printTypeEvent;
        Logger.i(printTypeEvent.toString());
    }

    private boolean isComment = true;

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.backImg:
                finishThis();
                break;
            case R.id.rightTv:
                DialogUtils.showCallDialog(context, getResources().getString(R.string.dlg_call),
                        new DialogUtils.CallDialogCallBack() {
                            @Override
                            public void ok() {
                                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "400-666-2060"));
                                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) !=
                                        PackageManager.PERMISSION_GRANTED) {
                                    return;
                                }
                                startActivity(intent);
                            }
                        }).show();
                break;
            case R.id.errorExitTv:
                finish();
                break;
            case R.id.printTv:
                finish();
                break;
            case R.id.shareRyt:
                showInvitation();
                break;
            case R.id.commentRyt:
//                if (isComment) {
//                    Bundle bundle = new Bundle();
//                    bundle.putLong("order_id", orderId);
//                    bundle.putString("printNum", printerPrice.getPrinterNo());
//                    bundle.putString("location", printerPrice.getPrintName());
//                    Intent intent = new Intent(context, CommentActivity.class);
//                    intent.putExtras(bundle);
//                    startActivity(intent);
//                } else {
//                    Toast.makeText(context, "您已评价过啦，请不要重复评价~", Toast.LENGTH_SHORT).show();
//                }

                //DialogUtils.showPrintSuccessDetailDialog(context)
                loadingDialog.show();
                PrintDetailRequest.getPrintDetail(context, orderId + "", callback);
                break;
            case R.id.ll_contact:
                DialogUtils.showCallDialog(context, getResources().getString(R.string.dlg_call),
                        new DialogUtils.CallDialogCallBack() {
                            @Override
                            public void ok() {
                                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "400-666-2060"));
                                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) !=
                                        PackageManager.PERMISSION_GRANTED) {
                                    return;
                                }
                                startActivity(intent);
                            }
                        }).show();
                break;
            default:
                break;
        }
    }


    private void finishThis() {
        EventBus.getDefault().post(new FinishEvent(true));
//        if (printTypeEvent == null) {
//            startActivity(new Intent(context, MainActivity.class));
//            finish();
//            return;
//        }
//        if (PrintTypeEvent.TYPE_COPY == printTypeEvent.getType()) {
//            startActivity(new Intent(context, CopyActivity.class));
//        } else {
//            startActivity(new Intent(context, MainActivity.class));
//        }
        finish();
    }

    static class MyHandler extends Handler {
        WeakReference mActivity;

        MyHandler(PrintStatusActivity activity) {
            mActivity = new WeakReference(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            PrintStatusActivity theActivity = (PrintStatusActivity) mActivity.get();
            switch (msg.what) {
                case 1:
                    if (theActivity != null) {
                        theActivity.update();
                    }
                    break;
            }
        }
    }

    public void update() {
        if (isAwaking) {
            return;
        }
        OrderStatusResp.OrderStatus orderStatus = orderStatusResp.getData();
        if (orderStatus == null) {
            setUpload();
            return;
        }
        if (orderStatus.isNeedAwake()) {
            if (!isAwaked) {
                setAwake();
                return;
            }
        }
        if (orderStatus.getWaitingCount() > 0) {
            setQueueView(orderStatus.getWaitingCount());
        } else {
            switch (orderStatus.getStatus()) {
                //正在打印
                case 0:
                    setUpload();
                    break;
                //打印成功
                case 1:
                    stopTimerTask();
                    setSuccessView();
                    break;
                //打印失败
                case 2:
                    stopTimerTask();
                    setExceptionView();
                    break;
            }

        }
    }

    MyHandler handler = new MyHandler(this);
    Timer timer = new Timer();
    TimerTask task = new TimerTask() {
        public void run() {
            PrintRequest.queryOrderStatus(activity, orderId, new HttpListener() {
                @Override
                public void onSucceed(String content) {
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                    orderStatusResp = GsonUtil.GsonToBean(content, OrderStatusResp.class);
                    if (orderStatusResp != null && orderStatusResp.isSuccess()) {

                    } else {
                        Logger.i(orderStatusResp.getErrorMsg());
                    }
                }

                @Override
                public void onFailed(String exception) {
                    ShowUtil.showToast(getString(R.string.net_error));
                    stopTimerTask();
                }
            });
        }
    };

    private void stopTimerTask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void setSuccessView() {
        //查询余额接口
        TotleBalanceRequest.request(context, new MyTotleBalanceCallBack());
        initTitleRight("联系客服", true);
        successRyt.setVisibility(View.VISIBLE);
        stateRyt.setVisibility(View.GONE);
        exceptionLyt.setVisibility(View.GONE);
        ll_contact.setVisibility(View.GONE);
    }

    private void setExceptionView() {
        initTitleRight("联系客服", false);
        findViewById(R.id.errorExitTv).setVisibility(View.INVISIBLE);
        successRyt.setVisibility(View.GONE);
        stateRyt.setVisibility(View.VISIBLE);
        exceptionLyt.setVisibility(View.VISIBLE);
        countTv.setVisibility(View.GONE);
        ll_contact.setVisibility(View.VISIBLE);
        animImg.setImageResource(R.drawable.ic_exception);
        stateTv.setText("打印异常");
        stateDetailTv.setText("很抱歉打印机发生了故障，未能正常打印，\n     系统会在两个小时后完成自动退款");
    }

    private void setAwake() {
        isAwaking = true;
        animImg.setImageResource(R.drawable.anim_awake);
        AnimationDrawable queueAnum = (AnimationDrawable) animImg.getDrawable();
        queueAnum.start();
        stateTv.setText("文件发送成功，打印机预热中...");
        stateDetailTv.setText("");
        countTv.setVisibility(View.VISIBLE);
        successRyt.setVisibility(View.GONE);
        stateRyt.setVisibility(View.VISIBLE);
        exceptionLyt.setVisibility(View.GONE);
        if (count == 60) {
            countTimer.schedule(countTask, 0, 1000);
        }
    }

    private Timer countTimer = new Timer();

    private void stopCountTimer() {
        if (countTimer != null) {
            countTimer.cancel();
            countTimer = null;
        }
    }

    private int count = 60;

    TimerTask countTask = new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    count--;
                    countTv.setText("预计还有" + count + "s…");
                    if (count <= 0) {
                        isAwaked = true;
                        isAwaking = false;
                        stopCountTimer();
                        countTv.setVisibility(View.GONE);
                    }
                }
            });
        }
    };

    private void setUpload() {
        animImg.setImageResource(R.drawable.anim_upload);
        AnimationDrawable queueAnum = (AnimationDrawable) animImg.getDrawable();
        queueAnum.start();
        stateTv.setText("文件发送中");
        stateDetailTv.setText("打印机正在接收文件，请耐心等待~");
        successRyt.setVisibility(View.GONE);
        stateRyt.setVisibility(View.VISIBLE);
        exceptionLyt.setVisibility(View.GONE);
        ll_contact.setVisibility(View.GONE);
    }

    private void setQueueView(int count) {
        animImg.setImageResource(R.drawable.anim_queue);
        AnimationDrawable queueAnum = (AnimationDrawable) animImg.getDrawable();
        queueAnum.start();
        stateTv.setText("发送文件排队中");
        stateDetailTv.setText("前面有" + count + "个打印任务,请耐心等待");
        successRyt.setVisibility(View.GONE);
        stateRyt.setVisibility(View.VISIBLE);
        exceptionLyt.setVisibility(View.GONE);
        countTv.setVisibility(View.GONE);
    }

    public static final String ORDER_ID = "orderId";
    public static final String PRINTER_PRICE = "printerPrice";

    public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent(context, PrintStatusActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        stopCountTimer();
        stopTimerTask();
        super.onDestroy();
        EventBus.getDefault().removeAllStickyEvents();
        EventBus.getDefault().unregister(context);

        if (null != receiveBroadCast) {
            unregisterReceiver(receiveBroadCast);
        }
    }

    @Override
    public void onBackPressed() {
        finishThis();
    }

    private void showInvitation() {
        final Dialog dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_third_share, null);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        inflate.setLayoutParams(layoutParams);
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
                wechatShare();
            }
        });
        View friendLyt = inflate.findViewById(R.id.friendLyt);
        friendLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                shareToWxFriend();
            }
        });
        dialog.setContentView(inflate);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
        dialog.show();
    }

    private void wechatShare() {
        WeiXinUtils weiXinUtils = WeiXinUtils.getInstance();
        weiXinUtils.init(this, BaseApplication.getInstance().WX_APPID);
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.appicon_print);
        weiXinUtils.shareToWxSceneSession(String.format("%s邀请您使用印家打印", BaseApplication.getInstance()
                .getNickName()), "我在用印家打印APP,打印、办公非常方便,快来下载吧", HttpUrl.getInstance().getPostUrl() + HttpUrl
                .appDownLoad + "?memberId=" + BaseApplication.getInstance
                ().getMemberId(), bmp);
        BaseApplication.getInstance().getApkUrl();
    }

    private void shareToWxFriend() {
        WeiXinUtils weiXinUtils = WeiXinUtils.getInstance();
        weiXinUtils.init(this, BaseApplication.getInstance().WX_APPID);
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.appicon_print);
        weiXinUtils.shareToWxFriend(String.format("%s邀请您使用印家打印", BaseApplication.getInstance()
                        .getNickName()), "我在用印家打印APP,打印、办公非常方便,快来下载吧",
                HttpUrl.getInstance().getPostUrl() + HttpUrl.appDownLoad + "?memberId=" + BaseApplication.getInstance
                        ().getMemberId(), bmp);
    }

    private PrintDetailCallBack callback = new PrintDetailCallBack() {
        @Override
        public void fail(String msg) {
            loadingDialog.dismiss();
        }

        @Override
        public void connectFail() {
            loadingDialog.dismiss();
        }

        @Override
        public void success(String msg, PrintDetailBean bean) {
            loadingDialog.dismiss();
            if (null != bean) {
                DialogUtils.showPrintSuccessDetailDialog(context, bean);
            }
        }
    };


    public class MyTotleBalanceCallBack extends TotleBalanceCallBack {

        @Override
        public void fail(String msg) {

        }

        @Override
        public void connectFail() {

        }

        @Override
        public void success(String msg, TotleBalanceBean bean) {
            String totleBalance = bean.getTotleBalance();
            tv_money.setText("￥" + totleBalance);
        }
    }

    //    public void onSuccess(View view) {
//        setSuccessView();
//    }
//
//    public void onException(View view) {
//        setExceptionView();
//    }
//
//    public void onQueue(View view) {
//        setQueueView(10);
//
//    }
//
//    public void onAWake(View view) {
//        setAwake();
//    }
//
//    public void onUpload(View view) {
//        setUpload();
//    }
    public class ReceiveBroadCast extends BroadcastReceiver {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onReceive(Context context, Intent intent) {
            //得到广播中得到的数据，并显示出来
            comment = intent.getBooleanExtra("comment", false);
            isComment = comment;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("打印状态");
        receiveBroadCast = new ReceiveBroadCast();
        IntentFilter filter = new IntentFilter();
        filter.addAction("comment");    //只有持有相同的action的接受者才能接收此广播
        context.registerReceiver(receiveBroadCast, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("打印状态");
    }

    @SuppressLint("NewApi")
    private void initialize() {

        cycleViewPager = (CycleViewPager) getFragmentManager()
                .findFragmentById(R.id.fragment_cycle_viewpager_content);
//
//        for(int i = 0; i < imageUrls.length; i ++){
//            ADInfo info = new ADInfo();
////            info.setUrl(imageUrls[i]);
////            info.setContent("图片-->" + i );
//            infos.add(info);
//        }

        // 将最后一个ImageView添加进来
        views.add(ViewFactory.getImageView(this, infos.get(infos.size() - 1).getImageUrl()));
        for (int i = 0; i < infos.size(); i++) {
            views.add(ViewFactory.getImageView(this, infos.get(i).getImageUrl()));
        }
        // 将第一个ImageView添加进来
        views.add(ViewFactory.getImageView(this, infos.get(0).getImageUrl()));

        // 设置循环，在调用setData方法前调用
        cycleViewPager.setCycle(true);

        // 在加载数据前设置是否循环
        cycleViewPager.setData(views, infos, mAdCycleViewListener);
        //设置轮播
        cycleViewPager.setWheel(true);

        // 设置轮播时间，默认5000ms
        cycleViewPager.setTime(2000);
        //设置圆点指示图标组居中显示，默认靠右
        cycleViewPager.setIndicatorCenter();
    }

    private ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener() {


        public void onImageClick(ADInfo info, int position, View imageView) {
            if (cycleViewPager.isCycle()) {
                position = position - 1;
//                Toast.makeText(context,
//                        "position-->" + info.getContent(), Toast.LENGTH_SHORT)
//                        .show();
            }

        }

    };

    /**
     * 配置ImageLoder
     */
    private void configImageLoader() {
        // 初始化ImageLoader
        @SuppressWarnings("deprecation")
        DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.drawable.icon_stub) //
                // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.icon_empty) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.icon_error) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
                // .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
                .build(); // 创建配置过得DisplayImageOption对象

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(options)
                .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType
                        .LIFO).build();
        ImageLoader.getInstance().init(config);
    }
}
