package huanxing_print.com.cn.printhome.util.image;

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

    public static void showImageRotated(Context context, String path, ImageView imageView) {
        if (ImageRolateUtil.getExifOrientation(path) == 90 || ImageRolateUtil.getExifOrientation(path) == 270) {
            Glide.with(context)
                    .load(path)
                    .transform(new RolateTransformation(context, 90f))
                    .into(imageView);
        } else {
            showImageView(context, path, imageView);
        }
    }
}
