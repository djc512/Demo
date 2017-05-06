package huanxing_print.com.cn.printhome.util.picuplload;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;
import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.model.picupload.ImageItem;
import huanxing_print.com.cn.printhome.ui.adapter.UpLoadPicAdapter;

/**
 * Created by Administrator on 2017/5/5 0005.
 */

public class UpLoadPicUtil {
    private List<Bitmap> mResults;
    private Context ctx;
    private UpLoadPicAdapter adapter;
    public UpLoadPicUtil(Context ctx, List<Bitmap> mResults,UpLoadPicAdapter adapter) {
        this.ctx = ctx;
        this.adapter = adapter;
        this.mResults = mResults;
    }

    public void showResult(ArrayList<String> paths) {
        if (mResults == null) {
            mResults = new ArrayList<>();
        }
        if (paths.size() != 0) {
            mResults.remove(mResults.size() - 1);
        }
        for (int i = 0; i < paths.size(); i++) {
            // 压缩图片
            Bitmap bitmap = BitmapLoadUtils.decodeSampledBitmapFromFd(paths.get(i), 400, 500);
            // 针对小图也可以不压缩
//            Bitmap bitmap = BitmapFactory.decodeFile(paths.get(i));
            mResults.add(bitmap);

            ImageItem takePhoto = new ImageItem();
            takePhoto.setBitmap(bitmap);
            Bimp.tempSelectBitmap.add(takePhoto);
        }
        mResults.add(BitmapFactory.decodeResource(ctx.getResources(), R.drawable.add));
        adapter.notifyDataSetChanged();
    }
}
