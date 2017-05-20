package huanxing_print.com.cn.printhome.ui.activity.copy;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.ui.activity.print.PickPrinterActivity;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.copy.PicSaveUtil;

import static huanxing_print.com.cn.printhome.util.copy.ClipPicUtil.ctx;

/**
 * Created by Administrator on 2017/4/27 0027.
 */

public class IDClipActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_preview;
    private TextView btn_preview;
    //    private double a4Width = 210;
//    private double a4Height = 297;
    private double a4Width = 220;
    private double a4Height = 307;
    private double idWidth = 85.5;
    private double idHeight = 54;
    private int screenWidth;
    private int screenHeight;
    private double sqrtRatio;
    private double ivSqrt;
    private Bitmap mergeBitmap;
    private Bitmap bitmap;
    private Bitmap bitmapf;
    private PicSaveUtil saveUtil;
    private Bitmap mBitmap;
    private TextView btn_reset;
    private Bitmap mergePic;

    //  1毫米 约等于 3.78像素
    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.activity_idclip);
        saveUtil = new PicSaveUtil(ctx);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        iv_preview = (ImageView) findViewById(R.id.iv_preview);
        btn_preview = (TextView) findViewById(R.id.btn_preview);
        btn_reset = (TextView) findViewById(R.id.btn_reset);
    }

    private void initData() {

        Display display = getWindowManager().getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();

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
    }

    private void initListener() {
        btn_preview.setOnClickListener(this);
        btn_reset.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onResume() {
        super.onResume();
        if (null != bitmap && null != bitmapf) {
            initMergePic();
        } else {
            scaleID(mBitmap);
        }
    }

    /**
     * 按比例缩放
     *
     * @param mBitmap
     */
    private void scaleID(Bitmap mBitmap) {
        sqrtRatio = (a4Width * a4Height) / (idWidth * idHeight);//a4纸与身份证面积比
        //计算图片在屏幕中应占的比例面积
        ivSqrt = ((screenHeight) * (screenWidth)) / sqrtRatio;
        double idRatio = idWidth / idHeight;//获取身份证的宽高比
        double ivHeight = Math.sqrt(ivSqrt / idRatio);//获取图片的高
        double ivWidth = ivHeight * idRatio;//获取图片的高
        Bitmap bitmap1 = ThumbnailUtils.extractThumbnail(mBitmap, (int) (ivWidth * 0.788 * 0.8684 * 0.6196), (int) (ivHeight * 0.7581 * 0.9101 * 0.6136));
        mergePic = mergePic(bitmap1, null, ivWidth, ivHeight);
        iv_preview.setImageBitmap(mergePic);
    }

    private String picName;

    /**
     * 处理拼接的图片
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void initMergePic() {
        sqrtRatio = (a4Width * a4Height) / (idWidth * idHeight);//a4纸与身份证面积比
        //计算图片在屏幕中应占的比例面积
        ivSqrt = ((screenHeight) * (screenWidth)) / sqrtRatio;
        double idRatio = idWidth / idHeight;//获取身份证的宽高比
        double ivHeight = Math.sqrt(ivSqrt / idRatio);//获取图片的高
        double ivWidth = ivHeight * idRatio;//获取图片的高

        Bitmap bitmap1 = ThumbnailUtils.extractThumbnail(bitmap, (int) (ivWidth * 0.788 * 0.8684 * 0.6196), (int) (ivHeight * 0.7581 * 0.9101 * 0.6136));
        Bitmap bitmap2 = ThumbnailUtils.extractThumbnail(bitmapf, (int) (ivWidth * 0.788 * 0.8684 * 0.6196), (int) (ivHeight * 0.7581 * 0.9101 * 0.6136));

        mergeBitmap = mergePic(bitmap1, bitmap2, ivWidth, ivHeight);
        iv_preview.setImageBitmap(mergeBitmap);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_reset:
                finishCurrentActivity();
                break;
            case R.id.btn_preview:
                picName = System.currentTimeMillis() + ".jpg";
                if (null != bitmap && null != bitmapf) {
                    saveUtil.saveClipPic(mergeBitmap, picName);
                    String path = Environment.getExternalStorageDirectory().getPath() + "/image/" + picName;
                    Intent printIntent = new Intent(getSelfActivity(), PickPrinterActivity.class);
                    printIntent.putExtra("imagepath", path);
                    printIntent.putExtra("copyfile", false);
                    startActivity(printIntent);
                    finishCurrentActivity();
                } else {
                    saveUtil.saveClipPic(mergePic, picName);
                    String path = Environment.getExternalStorageDirectory().getPath() + "/image/" + picName;
                    Intent printIntent = new Intent(getSelfActivity(), PickPrinterActivity.class);
                    printIntent.putExtra("imagepath", path);
                    printIntent.putExtra("copyfile", false);
                    startActivity(printIntent);
                    finishCurrentActivity();
                }
                break;
        }
    }

    /**
     * 合并图片
     *
     * @param first
     * @param second
     * @param ivWidth
     * @param ivHeight @return
     */
    private Bitmap mergePic(Bitmap first, Bitmap second, double ivWidth, double ivHeight) {
        int picwidth = (int) ivWidth;
        int picheight;
        if (null == second) {
            picheight = (int) (ivHeight);
        } else {
            picheight = (int) (ivHeight * 2);
        }
        Bitmap result = Bitmap.createBitmap(picwidth, picheight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(first, dip2px(50), dip2px(50), null);
        if (null != second) {
            canvas.drawBitmap(second, dip2px(50), first.getHeight() + dip2px(80), null);

        }
        return result;
    }

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
}
