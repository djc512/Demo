//package huanxing_print.com.cn.printhome.util;
//
//import android.content.Context;
//import android.net.Uri;
//
//import com.facebook.drawee.backends.pipeline.Fresco;
//import com.facebook.drawee.interfaces.DraweeController;
//import com.facebook.drawee.view.SimpleDraweeView;
//
///**
// * Created by LGH on 2017/5/5.
// */
//
//public class GifUtil {
//
//    public static void initDraweeView(Context context, SimpleDraweeView simpleDraweeView, String res) {
//        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
//                .setAutoPlayAnimations(true)
//                .setUri(getUri(context, res))
//                .build();
//        simpleDraweeView.setController(draweeController);
//    }
//
//    public static Uri getUri(Context context, String res) {
//        String pkName = null;
//        try {
//            pkName = context.getPackageName();
//        } catch (Exception e) {
//        }
//        return Uri.parse("asset://" + pkName + "/" + res);
//    }
//}
