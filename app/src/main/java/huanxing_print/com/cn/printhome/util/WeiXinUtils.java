package huanxing_print.com.cn.printhome.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;

/**
 * Created by wanghao on 2017/5/12.
 */

public class WeiXinUtils {
    private static WeiXinUtils instance;
    public IWXAPI api;
    private Context context;
    private static final int THUMB_SIZE = 150;
    public static WeiXinUtils getInstance(){
        synchronized (WeiXinUtils.class) {
            if(instance == null){
                instance = new WeiXinUtils();
            }
        }
        return instance;
    }

    /**
     * 微信注册此app
     * @param ctx
     * @param wx_appid
     */
    public void init(Context ctx, String wx_appid) {
        this.context = ctx;
        api = WXAPIFactory.createWXAPI(context, wx_appid);
        api.registerApp(wx_appid);
//        SendAuth.Req req = new SendAuth.Req();
//        req.scope = "snsapi_userinfo";
//        req.state = "wx_login_duzun";
//        api.sendReq(req);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    /**
     * 文本分享好友列表
     * @param shareMessage
     */
    public void shareToWXSceneSession(String shareMessage) {
        WXTextObject textObj = new WXTextObject();
        textObj.text = shareMessage;

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        msg.description = shareMessage;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("text");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;

        api.sendReq(req);
    }

    /**
     * 分享图片到好友列表
     * @param bmp
     */
    public void shareToWXSceneSession(Bitmap bmp) {
        WXImageObject imgObj = new WXImageObject(bmp);

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
        bmp.recycle();
        msg.thumbData = bmpToByteArray(thumbBmp, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        api.sendReq(req);
    }

    /**
     * 分享图文
     * @param title
     * @param content
     * @param webUrl
     * @param bitmap
     */
    public void shareToWxSceneSession(String title,String content,String webUrl,Bitmap bitmap) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = webUrl;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = content;
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, THUMB_SIZE, THUMB_SIZE, true);
        bitmap.recycle();
        msg.thumbData = bmpToByteArray(thumbBmp, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        api.sendReq(req);
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
