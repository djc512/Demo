package huanxing_print.com.cn.printhome.ui.activity.copy;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.copy.PicSaveUtil;

import static huanxing_print.com.cn.printhome.util.copy.ClipPicUtil.ctx;

/**
 * Created by Administrator on 2017/4/27 0027.
 */

public class HuKouClipActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_preview;
    private Button btn_reset;
    private Button btn_confirm;
    private double a4Width = 210;
    private double a4Height = 297;
    private double idWidth = 105;
    private double idHeight = 143;
    private int screenWidth;
    private int screenHeight;
    private double sqrtRatio;
    private double ivSqrt;
    private Bitmap mergeBitmap;
    private Bitmap bitmap;
    private Bitmap bitmapf;
    private PicSaveUtil saveUtil;
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
        btn_reset = (Button) findViewById(R.id.btn_reset);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
    }

    private void initData() {

        Display display = getWindowManager().getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();

        Intent intent = getIntent();

        byte[] bytes = intent.getByteArrayExtra("bytes");
        byte[] bytesf = intent.getByteArrayExtra("bytesf");

        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        bitmapf = BitmapFactory.decodeByteArray(bytesf, 0, bytesf.length);

    }

    private void initListener() {
        btn_reset.setOnClickListener(this);
        btn_confirm.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onResume() {
        super.onResume();
        initMergePic();
    }

    private String picName;

    /**
     * 处理拼接的图片
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void initMergePic() {
        sqrtRatio = (a4Width * a4Height) / (idWidth * idHeight);//a4纸与身份证面积比
        //计算图片在屏幕中应占的比例面积
        ivSqrt = (screenHeight * screenWidth) / sqrtRatio;
        double idRatio = idWidth / idHeight;//获取身份证的宽高比
        double ivHeight = Math.sqrt(ivSqrt / idRatio);//获取图片的高
        double ivWidth = ivHeight * idRatio;//获取图片的高

        Bitmap bitmap1 = ThumbnailUtils.extractThumbnail(bitmap, (int) ivWidth, (int) ivHeight);
        Bitmap bitmap2 = ThumbnailUtils.extractThumbnail(bitmapf, (int) ivWidth, (int) ivHeight);

        mergeBitmap = mergePic(bitmap1, bitmap2, ivWidth, ivHeight);
        iv_preview.setImageBitmap(mergeBitmap);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_reset:
                finish();
                break;
            case R.id.btn_confirm:
                picName = System.currentTimeMillis() + ".jpg";
                saveUtil.saveClipPic(mergeBitmap, picName);
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
        int picheight = (int) (ivHeight * 2);
        Bitmap result = Bitmap.createBitmap(picwidth, picheight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(first, 0, 0, null);

        canvas.drawBitmap(second, 0, first.getHeight() + 50, null);
        return result;
    }
}
