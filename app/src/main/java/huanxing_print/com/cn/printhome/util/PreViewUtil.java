package huanxing_print.com.cn.printhome.util;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import huanxing_print.com.cn.printhome.ui.activity.print.DocPreviewActivity;
import huanxing_print.com.cn.printhome.ui.activity.print.ImgPreviewActivity;

/**
 * Created by LGH on 2017/5/23.
 */

public class PreViewUtil {

    public static void preview(Context context, String path, boolean previewFlag) {
        if (path == null) {
            ShowUtil.showToast("文件错误");
            return;
        }
        if (!FileType.isPrintType(path)) {
            ShowUtil.showToast("文件不可打印");
            return;
        }
        if (FileType.getPrintType(path) == FileType.TYPE_IMG) {
            Bundle bundle = new Bundle();
            bundle.putCharSequence(ImgPreviewActivity.KEY_IMG_URI, path);
            bundle.putBoolean(ImgPreviewActivity.PREVIEW_FLAG, previewFlag);
            ImgPreviewActivity.start(context, bundle);
        } else {
            Uri uri = Uri.parse(Uri.encode("file://" + path));
            DocPreviewActivity.start(context, uri);
        }
    }
}
