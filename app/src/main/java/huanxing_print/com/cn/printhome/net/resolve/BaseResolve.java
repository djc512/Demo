package huanxing_print.com.cn.printhome.net.resolve;

import com.google.gson.Gson;
import com.google.zxing.common.StringUtils;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import huanxing_print.com.cn.printhome.event.login.HasLoginEvent;
import huanxing_print.com.cn.printhome.log.Logger;
import huanxing_print.com.cn.printhome.util.JsonUtils;
import huanxing_print.com.cn.printhome.util.ObjectUtils;


public abstract class BaseResolve<T> {

	protected int resultCode;

	protected String resultMessage;

	protected String detail;

	protected final int SUCCESS_CODE = 0;

	protected final int FAIL_CODE = 1;

	protected final int FAIL_CODE_TWO = 2;

	protected final int FAIL_CODE_OTHER = -1;

	protected T bean;

	public BaseResolve(String result) {
		resolve(result);
	}

	public void resolve(String result) {

		resultCode =Integer.parseInt(JsonUtils.getValueString("result", result));
		resultMessage = JsonUtils.getValueString("message", result);
		if (FAIL_CODE_TWO==resultCode) {
			HasLoginEvent hasLoginEvent = new HasLoginEvent();
			hasLoginEvent.setResultMessage(resultMessage);
			EventBus.getDefault().post(hasLoginEvent);
		}
		detail = JsonUtils.getValueString("detail", result);
		Logger.d("resultCode:" + resultCode);
		Logger.d("resultMessage:" + resultMessage);
		Logger.d("detail:" + detail);
		Gson gson = new Gson();

		try {

			Type[] types = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments();
			bean = (T) gson.fromJson(detail, types[0]);

		} catch (Exception e) {
			e.printStackTrace();
			Logger.e(e.getMessage());
		}

		if (!ObjectUtils.isNull(bean)) {
			Logger.d("bean:" + bean.toString());
		}
	}

}
