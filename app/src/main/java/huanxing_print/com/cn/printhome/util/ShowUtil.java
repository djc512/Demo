package huanxing_print.com.cn.printhome.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import huanxing_print.com.cn.printhome.base.BaseApplication;

/**
 * Created by Liugh on 2016/9/13.
 */
public class ShowUtil {

    private static final boolean DEBUG_MODE = true;

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
