package huanxing_print.com.cn.printhome.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import huanxing_print.com.cn.printhome.base.BaseApplication;

public class ToastUtil {

	public static void doToast(Context context, int stringId) {
		doToast(context, stringId, false);
	}

	public static void doToast(Context context, String content) {
		doToast(context, content, false);
	}

	public static void doToast(Context context, String content,
			boolean longOrShort) {
		Toast.makeText(context, content,
				longOrShort ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
	}

	public static void doToast(Context context, int stringId,
			boolean longOrShort) {
		Toast.makeText(context, stringId,
				longOrShort ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
	}

	public static void showToast(String str) {
		if (!TextUtils.isEmpty(str)) {
			showToast(BaseApplication.getInstance(), str);
		}
	}

	/**
	 * 弹出的Toast
	 *
	 * @param context
	 * @param str
	 */
	public static void showToast(Context context, String str) {
		Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
	}

}
