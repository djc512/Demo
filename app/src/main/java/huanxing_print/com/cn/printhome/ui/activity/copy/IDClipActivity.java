package huanxing_print.com.cn.printhome.ui.activity.copy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.ui.activity.print.PickPrinterActivity;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.copy.PicSaveUtil;

import static com.hyphenate.chat.EMGCMListenerService.TAG;
import static huanxing_print.com.cn.printhome.util.copy.ClipPicUtil.ctx;

/**
 * Created by Administrator on 2017/4/27 0027.
 */

public class IDClipActivity extends BaseActivity implements View.OnClickListener {
    //    private ImageView iv_preview;
    private TextView btn_preview;
    //    private double a4Width = 210;
//    private double a4Height = 297;
    private double a4Width = 220;
    private double a4Height = 307;
    private double idWidth = 85.5;
    private double idHeight = 54;
//    private int screenWidth;
//    private int screenHeight;
    private double sqrtRatio;
    private double ivSqrt;
    private Bitmap mergeBitmap;
    private Bitmap bitmap;
    private Bitmap bitmapf;
    private PicSaveUtil saveUtil;
    private Bitmap mBitmap;
    private TextView btn_reset;
    private Bitmap mergePic;
    private LinearLayout ll;
    private int measureWidth;

    //  1毫米 约等于 3.78像素
    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idclip);
        CommonUtils.initSystemBar(this);
        saveUtil = new PicSaveUtil(ctx);
        initView();
        initData();
        initListener();
    }

    private void initView() {
//        iv_preview = (ImageView) findViewById(iv_preview);
        btn_preview = (TextView) findViewById(R.id.btn_preview);
        btn_reset = (TextView) findViewById(R.id.btn_reset);
        ll = (LinearLayout) findViewById(R.id.ll_image_container);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void initData() {

//        Display display = getWindowManager().getDefaultDisplay();
//        screenWidth = display.getWidth();
//        screenHeight = display.getHeight();

        Intent intent = getIntent();
        byte[] bytes = intent.getByteArrayExtra("bytes");
        byte[] bytesf = intent.getByteArrayExtra("bytesf");
        if (null != bytes) {
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            mBitmap = bitmap;
        }
        if (null != bytesf) {
            bitmapf = BitmapFactory.decodeByteArray(bytesf, 0, bytesf.length);
            mBitmap = bitmapf;
        }

        if (null != bitmap && null != bitmapf) {
            initMergePic();
        } else {
            scaleID(mBitmap);
        }

    }

    private void initListener() {
        btn_preview.setOnClickListener(this);
        btn_reset.setOnClickListener(this);
    }

    /**
     * 按比例缩放
     *
     * @param mBitmap
     */
    private void scaleID(Bitmap mBitmap) {

        double wImage = dip2px(261) * 0.4943;
        double hImage = wImage * 54 / 86;
        ImageView iv = new ImageView(getSelfActivity());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((int) wImage, (int) hImage);
        lp.topMargin = dip2px(50);
        iv.setLayoutParams(lp);
        iv.setImageBitmap(mBitmap);

        ll.setGravity(Gravity.CENTER_HORIZONTAL);
        ll.addView(iv);
    }

    public Bitmap createViewBitmap(LinearLayout v) {
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }

    private String picName;

    /**
     * 处理拼接的图片
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void initMergePic() {
        double wImage = dip2px(261) * 0.4943;
        double hImage = wImage * 54 / 86;
        ImageView iv = new ImageView(getSelfActivity());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((int) wImage, (int) hImage);
        lp.topMargin = dip2px(50);
        iv.setLayoutParams(lp);
        iv.setImageBitmap(bitmap);

        double wImagef = dip2px(261) * 0.4943;
        double hImagef = wImage * 54 / 86;
        ImageView ivf = new ImageView(getSelfActivity());
        LinearLayout.LayoutParams lpf = new LinearLayout.LayoutParams((int) wImagef, (int) hImagef);
        lpf.topMargin = dip2px(20);
        ivf.setLayoutParams(lp);
        ivf.setImageBitmap(bitmapf);


        ll.setGravity(Gravity.CENTER_HORIZONTAL);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.addView(iv);
        ll.addView(ivf);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_reset:
                finishCurrentActivity();
                break;
            case R.id.btn_preview:
                picName = System.currentTimeMillis() + ".jpg";
//                if (null != bitmap && null != bitmapf) {
//                    Bitmap viewBitmap = createViewBitmap(ll);
//                    saveUtil.saveClipPic(viewBitmap, picName);
//                    String path = Environment.getExternalStorageDirectory().getPath() + "/image/" + picName;
//                    Intent printIntent = new Intent(getSelfActivity(), PickPrinterActivity.class);
//                    printIntent.putExtra("imagepath", path);
//                    printIntent.putExtra("copyfile", false);
//                    startActivity(printIntent);
//                    finishCurrentActivity();
//                } else {
                    Bitmap viewBitmap = createViewBitmap(ll);
                    saveUtil.saveClipPic(viewBitmap, picName);
                    String path = Environment.getExternalStorageDirectory().getPath() + "/image/" + picName;
                    Intent printIntent = new Intent(getSelfActivity(), PickPrinterActivity.class);
                    printIntent.putExtra("imagepath", path);
                    printIntent.putExtra("copyfile", false);
                    startActivity(printIntent);
                    finishCurrentActivity();
//                }
                break;
        }
    }

//    /**
//     * 合并图片
//     *
//     * @param first
//     * @param second
//     * @param ivWidth
//     * @param ivHeight @return
//     */
//    private Bitmap mergePic(Bitmap first, Bitmap second, double ivWidth, double ivHeight) {
//        int picwidth = (int) ivWidth;
//        int picheight;
//        if (null == second) {
//            picheight = (int) (ivHeight);
//        } else {
//            picheight = (int) (ivHeight * 2);
//        }
//        Bitmap result = Bitmap.createBitmap(picwidth, picheight, Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(result);
//        canvas.drawBitmap(first, dip2px(50), dip2px(50), null);
//        if (null != second) {
//            canvas.drawBitmap(second, dip2px(50), first.getHeight() + dip2px(80), null);
//
//        }
//        return result;
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mBitmap) {
            mBitmap.recycle();
            mBitmap = null;
        }
        if (null != mergeBitmap) {
            mergeBitmap.recycle();
            mergeBitmap = null;
        }
        System.gc();
    }

//    /**
//     * 获取屏幕的实际尺寸
//     */
//    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
//    private void getScreenSizeOfDevice() {
//        Point point = new Point();
//        getWindowManager().getDefaultDisplay().getRealSize(point);
//        DisplayMetrics dm = getResources().getDisplayMetrics();
//        double x = Math.pow(point.x / dm.xdpi, 2);
//        double y = Math.pow(point.y / dm.ydpi, 2);
//        double screenInches = Math.sqrt(x + y);
//        Log.d(TAG, "Screen inches : " + screenInches);
//    }
//    /**
//     * 获取手机的屏幕状态
//     * true为打开，false为关闭
//     */
//    public boolean isScreenon() {
//        PowerManager powerManager = (PowerManager) getSelfActivity()
//                .getSystemService(Context.POWER_SERVICE);
//        boolean ifOpen = powerManager.isScreenOn();
//        return ifOpen;
//    }
}
