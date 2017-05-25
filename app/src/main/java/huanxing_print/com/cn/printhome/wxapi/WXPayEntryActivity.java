package huanxing_print.com.cn.printhome.wxapi;

import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.event.print.WechatPayEvent;
import huanxing_print.com.cn.printhome.log.Logger;
import huanxing_print.com.cn.printhome.util.SharedPreferencesUtils;

import static huanxing_print.com.cn.printhome.base.BaseApplication.WX_APPID;


public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    private static final String TAG = "WXPayEntryActivity";

    private IWXAPI api;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, WX_APPID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (SharedPreferencesUtils.getShareBoolean(WXPayEntryActivity.this, "wechatFlag", false)) {
                Logger.i("打印支付成功");
                EventBus.getDefault().post(new WechatPayEvent(true));
            } else {
                switch (resp.errCode) {
                    case 0:
                        String successUrl = "http://print.inkin.cc/src/success.html";
                        send(successUrl);
                        break;
                    case -1:
                        String faileUrl = "http://print.inkin.cc/src/payFailed.html";
                        send(faileUrl);
                        break;
                    case -2:
                        String faileUrl1 = "http://print.inkin.cc/src/payFailed.html";
                        send(faileUrl1);
                        break;
                }
            }
        }
        finishCurrentActivity();
    }

    /**
     * 发消息通知页面跳转
     *
     * @param
     */
    private void send(String url) {
        Intent intentsave = new Intent();
        intentsave.setAction("url");
        intentsave.putExtra("url", url);
        sendBroadcast(intentsave);
    }
}