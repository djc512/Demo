package huanxing_print.com.cn.printhome.util;

import android.content.Context;
import android.widget.Toast;

public class ContextUtils {
	public static void toast(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}
}
