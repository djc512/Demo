package huanxing_print.com.cn.printhome.util;

import android.content.Context;
import android.os.Environment;
import android.text.format.Formatter;

import java.io.File;

/**
 * Created by LGH on 2017/4/28.
 */

public class StorageUtil {

    public static String getTotalSpaceStr(Context context) {
        File root = Environment.getExternalStorageDirectory();
        if (root == null) {
            return null;
        }
        long totalSpace = root.getTotalSpace();
        String totalSpaceStr = Formatter.formatFileSize(context, totalSpace);
        return totalSpaceStr;
    }

    public static String getUsableSpaceStr(Context context) {
        File root = Environment.getExternalStorageDirectory();
        if (root == null) {
            return null;
        }
        long usableSpace = root.getUsableSpace();
        String usableSpaceStr = Formatter.formatFileSize(context, usableSpace);
        return usableSpaceStr;
    }

    public static long getTotalSpace(Context context) {
        File root = Environment.getExternalStorageDirectory();
        long totalSpace = -1;
        if (root == null) {
            return totalSpace;
        }
        totalSpace = root.getTotalSpace();
        String totalSpaceStr = Formatter.formatFileSize(context, totalSpace);
        return totalSpace;
    }

    public static long getUsableSpace(Context context) {
        File root = Environment.getExternalStorageDirectory();
        long usableSpace = -1;
        if (root == null) {
            return usableSpace;
        }
        usableSpace = root.getUsableSpace();
        String usableSpaceStr = Formatter.formatFileSize(context, usableSpace);
        return usableSpace;
    }

    public static String getSdInfo(Context context) {
        String sdInfo = "存储不可用";
        if (getUsableSpace(context) > 0) {
            sdInfo = "存储空间:" + getUsableSpaceStr(context) + "/" + getTotalSpaceStr(context);
        }
        return sdInfo;
    }

    public static int getSdUsablePercent(Context context) {
        int percent = 0;
        if (getUsableSpace(context) > 0) {
            percent = (int) (100 * getUsableSpace(context) / getTotalSpace(context));
            percent = 100 - percent;
        }
        return percent;
    }

}
