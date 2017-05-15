package huanxing_print.com.cn.printhome.net.resolve;

import com.google.gson.Gson;

import org.simple.eventbus.EventBus;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import huanxing_print.com.cn.printhome.log.Logger;
import huanxing_print.com.cn.printhome.util.JsonUtils;
import huanxing_print.com.cn.printhome.util.ObjectUtils;


public abstract class BaseResolve<T> {

	protected boolean success;
    protected int code;
	protected String errorCode;

	protected String errorMsg;
    protected String successMsg;
    protected String data;

	protected final int SUCCESS_CODE = 1;

	protected final int FAIL_CODE = 0;

	protected final int FAIL_CODE_TWO = 2;

	protected final int FAIL_CODE_OTHER = -1;
    private  final String FAIL_CODE_TOKEN_FAIL= "10001";

	protected  T bean;

	public BaseResolve(String result) {
		resolve(result);
	}

	public void resolve(String result) {

		success = Boolean.parseBoolean(JsonUtils.getValueString("success", result));
		errorMsg = JsonUtils.getValueString("errorMsg", result);
        successMsg= JsonUtils.getValueString("message", result);
		errorCode = JsonUtils.getValueString("errorCode", result);
        if (success){
            code =1;

        }else {
            code =0;
            if (FAIL_CODE_TOKEN_FAIL.equals(errorCode)) {
//                HasLoginEvent hasLoginEvent = new HasLoginEvent();
//                hasLoginEvent.setResultMessage(errorMsg);
                EventBus.getDefault().post("hasLoginEvent");
            }
        }

        data = JsonUtils.getValueString("data", result);
        Logger.d("resultCode:" + errorCode);
        Logger.d("resultMessage:" + errorMsg);
        Logger.d("data:" + data);
        Gson gson = new Gson();

        try {
            Type[] types = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments();
            bean = (T) gson.fromJson(data, types[0]);

        } catch (Exception e) {
            e.printStackTrace();
            Logger.e(e.getMessage());
        }

        if (!ObjectUtils.isNull(bean)) {
            Logger.d("bean:" + bean.toString());
        }
    }

	}


