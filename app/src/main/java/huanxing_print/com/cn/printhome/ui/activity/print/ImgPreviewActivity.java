package huanxing_print.com.cn.printhome.ui.activity.print;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.File;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.log.Logger;
import huanxing_print.com.cn.printhome.net.HttpCallBack;
import huanxing_print.com.cn.printhome.net.request.print.PrintRequest;
import huanxing_print.com.cn.printhome.util.ToastUtil;
import huanxing_print.com.cn.printhome.util.UriUtil;

public class ImgPreviewActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imageView;
    private Uri imgUri;
    private ImageView upImgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_img_priview);
        Logger.i("onCreate");
        initData();
        initView();
    }

    private void initData() {
        imgUri = (Uri) getIntent().getExtras().get(AddFileActivity.KEY_IMG_URI);
    }

    private void initView() {
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageURI(imgUri);
        findViewById(R.id.upImgView).setOnClickListener(this);
        findViewById(R.id.closeImgView).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.upImgView:
                ToastUtil.showToast("upload");
                uploadFile();
//                turnPreview();
                finish();
                break;
            case R.id.closeImgView:
                finish();
                break;
        }
    }

    private void uploadFile() {
        File file = UriUtil.getFile(ImgPreviewActivity.this, imgUri);
        if (file == null) {
            return;
        }
        PrintRequest.uploadFile(ImgPreviewActivity.this, "", "", "", "", new HttpCallBack() {
            @Override
            public void success(String content) {
                Logger.i(content);
            }

            @Override
            public void fail(String exception) {
                Logger.i(exception);
            }
        });
    }

    private void turnPreview() {
        Intent intent = new Intent(ImgPreviewActivity.this, ImgPrintActivity.class);
        startActivity(intent);
    }
}
