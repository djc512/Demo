package huanxing_print.com.cn.printhome.ui.activity.Copy;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.util.CommonUtils;

/**
 * Created by Administrator on 2017/4/27 0027.
 */

public class IDClipActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_preview;
    private ImageView iv_previewf;
    private Button btn_reset;
    private Button btn_confirm;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.activity_idclip);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        iv_preview = (ImageView) findViewById(R.id.iv_preview);
        iv_previewf = (ImageView) findViewById(R.id.iv_previewf);
        btn_reset = (Button) findViewById(R.id.btn_reset);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
    }

    private void initData() {
        Intent intent = getIntent();
        byte[] bytes = intent.getByteArrayExtra("bytes");
        byte[] bytesf = intent.getByteArrayExtra("bytesf");

        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        iv_preview.setImageBitmap(bitmap);

        Bitmap bitmapf = BitmapFactory.decodeByteArray(bytesf, 0, bytesf.length);
        iv_previewf.setImageBitmap(bitmapf);
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
                Toast.makeText(this, "去打印", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
