package huanxing_print.com.cn.printhome.ui.activity.print;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.constant.ConFig;
import huanxing_print.com.cn.printhome.log.Logger;
import huanxing_print.com.cn.printhome.model.print.AddFileSettingBean;
import huanxing_print.com.cn.printhome.model.print.PrintSetting;
import huanxing_print.com.cn.printhome.model.print.UploadImgBean;
import huanxing_print.com.cn.printhome.net.request.print.HttpListener;
import huanxing_print.com.cn.printhome.net.request.print.PrintRequest;
import huanxing_print.com.cn.printhome.util.AlertUtil;
import huanxing_print.com.cn.printhome.util.FileType;
import huanxing_print.com.cn.printhome.util.FileUtils;
import huanxing_print.com.cn.printhome.util.GsonUtil;
import huanxing_print.com.cn.printhome.util.ShowUtil;
import huanxing_print.com.cn.printhome.util.ToastUtil;

public class ImgPreviewActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String KEY_IMG_URI = "image";
    private ImageView imageView;
    private String imgPath;
    private File file;

    private Context context;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams
                .FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_img_priview);
        initData();
        initView();
    }

    private void initData() {
        context = this;
        activity = this;
        imgPath = (String) getIntent().getExtras().get(KEY_IMG_URI);
    }

    private void initView() {
        imageView = (ImageView) findViewById(R.id.imageView);
        Glide.with(ImgPreviewActivity.this)
                .load(imgPath)
                .into(imageView);
        findViewById(R.id.upImgView).setOnClickListener(this);
        findViewById(R.id.closeImgView).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.upImgView:
                uploadFile();
                break;
            case R.id.closeImgView:
                finish();
                break;
        }
    }

    private void uploadFile() {
        file = new File(imgPath);
        if (file == null || !file.exists()) {
            return;
        }
        if (FileUtils.getFileSize(file) > ConFig.FILE_UPLOAD_MAX) {
            AlertUtil.show(context, "提示", "文件超限", null, new
                    DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
            return;
        }
        PrintRequest.uploadFile(activity, FileType.getType(file.getPath()), FileUtils.getBase64(file), file
                .getName(), "1", new HttpListener() {
            @Override
            public void onSucceed(String content) {
                UploadImgBean uploadImgBean = GsonUtil.GsonToBean(content, UploadImgBean.class);
                if (uploadImgBean == null) {
                    return;
                }
                if (uploadImgBean.isSuccess()) {
                    String url = uploadImgBean.getData().getImgUrl();
                    addFile(url);
                } else {
                    ShowUtil.showToast(getString(R.string.upload_failure));
                }
            }

            @Override
            public void onFailed(String exception) {
                ShowUtil.showToast(getString(R.string.net_error));
            }
        }, false);
    }

    private void addFile(String fileUrl) {
        PrintRequest.addFile(activity, "1", file.getName(), fileUrl, new HttpListener() {
            @Override
            public void onSucceed(String content) {
                AddFileSettingBean addFileSettingBean = GsonUtil.GsonToBean(content, AddFileSettingBean.class);
                if (addFileSettingBean == null) {
                    return;
                }
                if (addFileSettingBean.isSuccess()) {
                    turnPrintSetting(addFileSettingBean.getData());
                } else {
                    Logger.i("net error");
                    ToastUtil.doToast(context, getString(R.string.upload_failure));
                }
            }

            @Override
            public void onFailed(String exception) {
                Logger.i("网络错误");
                ShowUtil.showToast(getString(R.string.net_error));
            }
        }, false);
    }

;

    private void turnPrintSetting(PrintSetting printSetting) {
        Intent intent = new Intent(ImgPreviewActivity.this, ImgPrintSettingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(ImgPrintSettingActivity.PRINT_SETTING, printSetting);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
}
