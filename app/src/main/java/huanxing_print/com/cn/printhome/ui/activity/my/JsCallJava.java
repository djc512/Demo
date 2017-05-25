package huanxing_print.com.cn.printhome.ui.activity.my;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseApplication;
import huanxing_print.com.cn.printhome.model.my.WeChatPayBean;
import huanxing_print.com.cn.printhome.net.callback.my.WeChatCallBack;
import huanxing_print.com.cn.printhome.net.request.my.Go2PayRequest;
import huanxing_print.com.cn.printhome.util.Pay.PayUtil;
import huanxing_print.com.cn.printhome.util.WeiXinUtils;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;

/**
 * Created by Administrator on 2017/5/19 0019.
 */

public class JsCallJava {
    private Context ctx;
    private Dialog dialog;

    public JsCallJava(Context ctx) {
        this.ctx = ctx;
    }

    @JavascriptInterface
    public void pay(String orderid) {
        Toast.makeText(ctx, "支付", Toast.LENGTH_SHORT).show();
        DialogUtils.showProgressDialog(ctx, "正在加载");
        Go2PayRequest.go2PWeChat(ctx, orderid, "JM", new WeChatCallBack() {
            @Override
            public void success(WeChatPayBean bean) {
                DialogUtils.closeProgressDialog();
                PayUtil.getInstance(ctx).weChatPay(bean);
            }

            @Override
            public void fail(String msg) {

            }

            @Override
            public void connectFail() {
                Toast.makeText(ctx, "网络连接失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @JavascriptInterface
    public void share(final String url) {
        Log.i("JsCallJava", url);
        dialog = new Dialog(ctx, R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        View inflate = LayoutInflater.from(ctx).inflate(R.layout.dialog_invitation1, null);
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
                invitationWeiXin(url);
            }
        });
        View btn_message = inflate.findViewById(R.id.btn_message);
        btn_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
               invitationWxFriend(url);
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

    private void invitationWeiXin(String shareAppUrl) {
        WeiXinUtils weiXinUtils = WeiXinUtils.getInstance();
        weiXinUtils.init(ctx, BaseApplication.getInstance().WX_APPID);
        Bitmap bmp = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.appicon_print);
        weiXinUtils.shareToWxSceneSession("印家共享打印", "大家帮我支持一下打印点投放！", shareAppUrl, bmp);
    }

    private void invitationWxFriend(String shareAppUrl) {
        WeiXinUtils weiXinUtils = WeiXinUtils.getInstance();
        weiXinUtils.init(ctx, BaseApplication.getInstance().WX_APPID);
        Bitmap bmp = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.appicon_print);
        weiXinUtils.shareToWxFriend("印家共享打印", "大家帮我支持一下打印点投放！", shareAppUrl, bmp);
    }
}
