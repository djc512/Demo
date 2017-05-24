package huanxing_print.com.cn.printhome.ui.activity.print;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseApplication;
import huanxing_print.com.cn.printhome.constant.HttpUrl;
import huanxing_print.com.cn.printhome.event.print.FinishEvent;
import huanxing_print.com.cn.printhome.event.print.PrintTypeEvent;
import huanxing_print.com.cn.printhome.log.Logger;
import huanxing_print.com.cn.printhome.model.print.OrderStatusResp;
import huanxing_print.com.cn.printhome.model.print.PrintInfoResp;
import huanxing_print.com.cn.printhome.net.request.print.HttpListener;
import huanxing_print.com.cn.printhome.net.request.print.PrintRequest;
import huanxing_print.com.cn.printhome.ui.activity.copy.CommentActivity;
import huanxing_print.com.cn.printhome.util.GsonUtil;
import huanxing_print.com.cn.printhome.util.ShowUtil;
import huanxing_print.com.cn.printhome.util.WeiXinUtils;


public class PrintStatusActivity extends BasePrintActivity implements View.OnClickListener {

    private TextView stateTv;
    private TextView stateDetailTv;
    private TextView countTv;
    private RelativeLayout successRyt;
    private RelativeLayout stateRyt;
    private LinearLayout exceptionLyt;
    private ImageView animImg;

    private OrderStatusResp orderStatusResp;
    private PrintInfoResp.PrinterPrice printerPrice;
    private long orderId;
    private int awakeAccount = 0;

    private PrintTypeEvent printTypeEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_status);
        EventBus.getDefault().register(context);
        initData();
        initView();
        setUpload();
        timer.schedule(task, 1000 * 3, 1000 * 2);
    }

    private void initData() {
        orderId = getIntent().getExtras().getLong(ORDER_ID);
        printerPrice = getIntent().getExtras().getParcelable(PRINTER_PRICE);
        Logger.i(printerPrice.toString());
        Logger.i(orderId);
    }

    private void initView() {
        initTitleBar("打印");
        successRyt = (RelativeLayout) findViewById(R.id.successRyt);
        stateRyt = (RelativeLayout) findViewById(R.id.stateRyt);
        exceptionLyt = (LinearLayout) findViewById(R.id.exceptionLyt);

        animImg = (ImageView) findViewById(R.id.animImg);
        stateTv = (TextView) findViewById(R.id.stateTv);
        stateDetailTv = (TextView) findViewById(R.id.stateDetailTv);
        countTv = (TextView) findViewById(R.id.countTv);

        findViewById(R.id.errorExitTv).setOnClickListener(this);
        findViewById(R.id.printTv).setOnClickListener(this);
        findViewById(R.id.shareRyt).setOnClickListener(this);
        findViewById(R.id.commentRyt).setOnClickListener(this);
        findViewById(R.id.backImg).setOnClickListener(this);
    }

    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)
    public void onMessageEventPostThread(PrintTypeEvent printTypeEvent) {
        this.printTypeEvent = printTypeEvent;
        Logger.i(printTypeEvent.toString());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.backImg:
                finishThis();
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
                Bundle bundle = new Bundle();
                bundle.putLong("order_id", orderId);
                bundle.putString("printNum", printerPrice.getPrinterNo());
                bundle.putString("location", printerPrice.getPrintAddress());
                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
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
        OrderStatusResp.OrderStatus orderStatus = orderStatusResp.getData();
        if (orderStatus.isNeedAwake() && awakeAccount < 3) {
            awakeAccount++;
            setAwake();
        } else if (orderStatus.getWaitingCount() > 0) {
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
        successRyt.setVisibility(View.VISIBLE);
        stateRyt.setVisibility(View.GONE);
        exceptionLyt.setVisibility(View.GONE);
    }

    private void setExceptionView() {
        findViewById(R.id.errorExitTv).setVisibility(View.INVISIBLE);
        successRyt.setVisibility(View.GONE);
        stateRyt.setVisibility(View.VISIBLE);
        exceptionLyt.setVisibility(View.VISIBLE);
        countTv.setVisibility(View.GONE);
        animImg.setImageResource(R.drawable.ic_exception);
        stateTv.setText("打印异常");
        stateDetailTv.setText("很抱歉打印机发生了故障，未能正常打印，\n     系统会在两个小时后完成自动退款");
    }

    private void setAwake() {
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
                    if (count < 0) {
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
}
