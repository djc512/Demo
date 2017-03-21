package huanxing_print.com.cn.printhome.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;

/**
 * Created by LGH on 2017/3/20.
 */

public class UriUtil {

    public static final File getFile(Context context, Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
        cursor.moveToFirst();
        int actual_image_column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        String path = cursor.getString(actual_image_column_index);
        File file = new File(path);
        return file;
    }
}
