package huanxing_print.com.cn.printhome.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by LGH on 2017/3/22.
 */

public class ImageUtil {

    public static void showImageView(Context context, String path, ImageView imageView) {
        Glide.with(context)
                .load(path)
                .into(imageView);
    }
}
