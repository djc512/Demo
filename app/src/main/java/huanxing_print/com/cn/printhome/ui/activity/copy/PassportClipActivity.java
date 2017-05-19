package huanxing_print.com.cn.printhome.ui.activity.copy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.ui.activity.print.PickPrinterActivity;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.PrintUtil;
import huanxing_print.com.cn.printhome.util.copy.PicSaveUtil;

/**
 * Created by Administrator on 2017/4/27 0027.
 */

public class PassportClipActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_preview;
    private TextView btn_reset;
    private TextView btn_preview;
    private double a4Width = 220;
    private double a4Height = 307;
    private double passportWidth = 125;
    private double passportHeight = 88;
    private int screenWidth;
    private int screenHeight;
    private double sqrtRatio;
    private double ivSqrt;
    private Bitmap thumbnail;
    private String picName;
    private PicSaveUtil saveUtil;
    private Context ctx;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.activity_passportclip);
        ctx = this;
        saveUtil = new PicSaveUtil(ctx);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        iv_preview = (ImageView) findViewById(R.id.iv_preview);
        btn_reset = (TextView) findViewById(R.id.btn_reset);
        btn_preview = (TextView) findViewById(R.id.btn_preview);
    }
//    Bitmap result = Bitmap.createBitmap(picwidth, picheight, Bitmap.Config.ARGB_8888);
//    Canvas canvas = new Canvas(result);
//    canvas.drawBitmap(first, 0, 0, null);

    private void initData() {
        Display display = getWindowManager().getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();

        Intent intent = getIntent();
        byte[] bytes = intent.getByteArrayExtra("bytes");
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        //a4纸与身份证面积比
        sqrtRatio = (a4Width * a4Height) / (passportWidth * passportHeight);
        //计算图片在屏幕中应占的比例面积
        ivSqrt = (screenHeight * screenWidth) / sqrtRatio;
        double idRatio = passportWidth / passportHeight;//获取身份证的宽高比
        double ivHeight = Math.sqrt(ivSqrt / idRatio);//获取图片的高
        double ivWidth = ivHeight * idRatio;//获取图片的高

        thumbnail = ThumbnailUtils.extractThumbnail(bitmap, (int) (ivWidth * 0.788*0.8684), (int) (ivHeight * 0.7851* 0.9101));
        Bitmap mergePic = mergePic(thumbnail, ivWidth, ivHeight);
        iv_preview.setImageBitmap(mergePic);
    }

    /**
     * 合并图片
     */
    private Bitmap mergePic(Bitmap first, double ivWidth, double ivHeight) {
        int picwidth = (int) ivWidth;
        int picheight = (int) (ivHeight);
        Bitmap result = Bitmap.createBitmap(picwidth, picheight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(first, 0, 0, null);
        return result;
    }
    private void initListener() {
        btn_reset.setOnClickListener(this);
        btn_preview.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_reset:
                finishCurrentActivity();
                break;
            case R.id.btn_preview:
                picName = System.currentTimeMillis() + ".jpg";
                saveUtil.saveClipPic(thumbnail, picName);
                String path = Environment.getExternalStorageDirectory().getPath() + "/image/" + picName;
                Intent printIntent = new Intent(getSelfActivity(), PickPrinterActivity.class);
                printIntent.putExtra("imagepath", path);
                printIntent.putExtra("print_type", PrintUtil.PRINT_TYPE_PASSFORT);
                startActivity(printIntent);
                finishCurrentActivity();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != thumbnail) {
            thumbnail.recycle();
            thumbnail = null;
        }
        System.gc();
    }
}
