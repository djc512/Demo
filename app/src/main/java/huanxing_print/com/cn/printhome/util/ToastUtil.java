package huanxing_print.com.cn.printhome.util;

import android.content.Context;
import android.widget.Toast;

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

}
