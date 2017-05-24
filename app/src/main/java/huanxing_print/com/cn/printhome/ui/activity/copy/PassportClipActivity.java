package huanxing_print.com.cn.printhome.ui.activity.copy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
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
import huanxing_print.com.cn.printhome.util.PrintUtil;
import huanxing_print.com.cn.printhome.util.copy.PicSaveUtil;

/**
 * Created by Administrator on 2017/4/27 0027.
 */

public class PassportClipActivity extends BaseActivity implements View.OnClickListener {
    private TextView btn_reset;
    private TextView btn_preview;
    private double a4Width = 220;
    private double a4Height = 307;
    private double passportWidth = 125;
    private double passportHeight = 173;
    private int screenWidth;
    private int screenHeight;
    private double sqrtRatio;
    private double ivSqrt;
    private Bitmap thumbnail;
    private String picName;
    private PicSaveUtil saveUtil;
    private Context ctx;
    private Bitmap mergePic;
    private LinearLayout ll;

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
        btn_reset = (TextView) findViewById(R.id.btn_reset);
        btn_preview = (TextView) findViewById(R.id.btn_preview);
        ll = (LinearLayout) findViewById(R.id.ll_image_container);
    }

    private void initData() {
        Display display = getWindowManager().getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();

        Intent intent = getIntent();
        byte[] bytes = intent.getByteArrayExtra("bytes");
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        double wImage = dip2px(348) * 0.4943;
        double hImage = wImage * 173 / 125;
        ImageView iv = new ImageView(getSelfActivity());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((int) (wImage * 1.4535), (int) (hImage * 1.4914));
        lp.topMargin = dip2px(50);
        iv.setLayoutParams(lp);
        iv.setImageBitmap(bitmap);

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
                Bitmap viewBitmap = createViewBitmap(ll);
                picName = System.currentTimeMillis() + ".jpg";
                saveUtil.saveClipPic(viewBitmap, picName);
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
