package huanxing_print.com.cn.printhome.ui.activity.copy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.copy.PicSaveUtil;

/**
 * Created by Administrator on 2017/4/27 0027.
 */

public class PassportClipActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_preview;
    private Button btn_reset;
    private Button btn_confirm;
    private double a4Width = 210;
    private double a4Height = 297;
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
        btn_reset = (Button) findViewById(R.id.btn_reset);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
    }

    private void initData() {
        Display display = getWindowManager().getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();

        //a4纸与身份证面积比
        sqrtRatio = (a4Width * a4Height) / (passportWidth * passportHeight);
        //计算图片在屏幕中应占的比例面积
        ivSqrt = (screenHeight * screenWidth) / sqrtRatio;
        double idRatio = passportWidth / passportHeight;//获取身份证的宽高比
        double ivHeight = Math.sqrt(ivSqrt / idRatio);//获取图片的高
        double ivWidth = ivHeight * idRatio;//获取图片的高

        Intent intent = getIntent();
        byte[] bytes = intent.getByteArrayExtra("bytes");
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        thumbnail = ThumbnailUtils.extractThumbnail(bitmap, (int) ivWidth, (int) ivHeight);

        iv_preview.setImageBitmap(thumbnail);
    }

    private void initListener() {
        btn_reset.setOnClickListener(this);
        btn_confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_reset:
                finish();
                break;
            case R.id.btn_confirm:
                picName = System.currentTimeMillis() + ".jpg";
                saveUtil.saveClipPic(thumbnail, picName);
                break;
        }
    }
}
