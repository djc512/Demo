package huanxing_print.com.cn.printhome.view.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by Administrator on 2016/12/20.
 */

public class Alert {

    public static final void show(Context context, String title, String message, DialogInterface.OnClickListener
            nOnClickListener, DialogInterface.OnClickListener pOnClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNegativeButton("取消", nOnClickListener);
        builder.setPositiveButton("确定", pOnClickListener);
        builder.show();
    }
}
