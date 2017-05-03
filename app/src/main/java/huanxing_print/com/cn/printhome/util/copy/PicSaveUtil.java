package huanxing_print.com.cn.printhome.util.copy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.dreamlive.cn.clog.CollectLog.TAG;

/**
 * Created by Administrator on 2017/4/27 0027.
 */

public class PicSaveUtil {

    private Context ctx;

    public PicSaveUtil(Context ctx) {
        this.ctx = ctx;
    }

    /**
     * 拍照图片的临时存储位置
     *
     * @param savedInstanceState
     */
    public File createCameraTempFile(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey("tempFile")) {
            return (File) savedInstanceState.getSerializable("tempFile");
        } else {
            return new File(checkDirPath(Environment.getExternalStorageDirectory().getPath() + "/image/"),
                    System.currentTimeMillis() + ".jpg");
        }
    }

    /**
     * 检查文件是否存在
     */
    private String checkDirPath(String dirPath) {
        if (TextUtils.isEmpty(dirPath)) {
            return "";
        }
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dirPath;
    }

    /**
     * 保存处理过的图片
     */
    public void saveClipPic(Bitmap bm, String name) {
        Log.e(TAG, "保存图片");
        File f = new File(checkDirPath(Environment.getExternalStorageDirectory().getPath() + "/image/"), name);
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            // 其次把文件插入到系统图库
            try {
                MediaStore.Images.Media.insertImage(ctx.getContentResolver(),
                        f.getAbsolutePath(), System.currentTimeMillis() + ".jpg", null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // 最后通知图库更新
            String path = Environment.getExternalStorageDirectory().getPath() + "/image/" + name;
            ctx.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
