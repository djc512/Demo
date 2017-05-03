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

public class PassportClipActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_preview;
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
        setContentView(R.layout.activity_passportclip);
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
        Intent intent = getIntent();
        byte[] bytes = intent.getByteArrayExtra("bytes");

        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        iv_preview.setImageBitmap(bitmap);
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
