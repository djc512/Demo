/*
 * 官网地站:http://www.mob.com
 * 技术支持QQ: 4006852216
 * 官方微信:ShareSDK   （如果发布新版本的话，我们将会第一时间通过微信将版本更新内容推送给您。如果使用过程中有任何问题，也可以通过微信与我们取得联系，我们将会在24小时内给予回复）
 *
 * Copyright (c) 2013年 mob.com. All rights reserved.
 */

package huanxing_print.com.cn.printhome.wxapi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.constant.ConFig;

/** 微信客户端回调activity示例 */
public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {
	private IWXAPI api;

	@Override
	protected BaseActivity getSelfActivity() {
		return this;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//----------- 当分享成功后，返回应用结束当前也 start ---------------
		finishCurrentActivity();
		//----------- 当分享成功后，返回应用结束当前也 end ---------------

		api = WXAPIFactory.createWXAPI(this, baseApplication.WX_APPID, false);
		//将你收到的intent和实现IWXAPIEventHandler接口的对象传递给handleIntent方法
		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
		finish();
	}

	@Override
	public void onReq(BaseReq baseReq) {

	}

	@Override
	public void onResp(BaseResp baseResp) {
		String result = "";
		switch (baseResp.errCode) {
			case BaseResp.ErrCode.ERR_OK:
				String code = ((SendAuth.Resp) baseResp).code;
				SharedPreferences WxSp = getApplicationContext().getSharedPreferences(ConFig.spName, Context.MODE_PRIVATE);
				SharedPreferences.Editor WxSpEditor = WxSp.edit();
				WxSpEditor.putString(ConFig.CODE,code);
				WxSpEditor.apply();
				Intent intent = new Intent();
				intent.setAction("authlogin");
				WXEntryActivity.this.sendBroadcast(intent);
				finish();
				break;
			case BaseResp.ErrCode.ERR_USER_CANCEL:
//				result = "发送取消";
//				Toast.makeText(this, result, Toast.LENGTH_LONG).show();
				finish();
				break;
			case BaseResp.ErrCode.ERR_AUTH_DENIED:
				result = "发送被拒绝";
				Toast.makeText(this, result, Toast.LENGTH_LONG).show();
				finish();
				break;
			default:
				result = "发送返回";
				Toast.makeText(this, result, Toast.LENGTH_LONG).show();
				finish();
				break;
		}
	}

}
