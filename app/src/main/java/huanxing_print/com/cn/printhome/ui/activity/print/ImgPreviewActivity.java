package huanxing_print.com.cn.printhome.ui.activity.print;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.gson.Gson;

import java.io.File;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.constant.ConFig;
import huanxing_print.com.cn.printhome.log.Logger;
import huanxing_print.com.cn.printhome.model.print.AddFileSettingBean;
import huanxing_print.com.cn.printhome.model.print.PrintSetting;
import huanxing_print.com.cn.printhome.model.print.UploadImgBean;
import huanxing_print.com.cn.printhome.net.HttpCallBack;
import huanxing_print.com.cn.printhome.net.request.print.PrintRequest;
import huanxing_print.com.cn.printhome.util.AlertUtil;
import huanxing_print.com.cn.printhome.util.FileType;
import huanxing_print.com.cn.printhome.util.FileUtils;
import huanxing_print.com.cn.printhome.util.ShowUtil;
import huanxing_print.com.cn.printhome.util.ToastUtil;
import huanxing_print.com.cn.printhome.util.UriUtil;

public class ImgPreviewActivity extends BasePrintActivity implements View.OnClickListener {

    private ImageView imageView;
    private Uri imgUri;
    private File file;
    private ImageView upImgView;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

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
                uploadFile();
                break;
            case R.id.closeImgView:
                finish();
                break;
        }
    }

    private void uploadFile() {
        file = UriUtil.getFile(ImgPreviewActivity.this, imgUri);
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
        PrintRequest.uploadFile(context, FileType.getType(file.getPath()), FileUtils.getBase64(file), file
                .getName(), "1", new HttpCallBack() {
            @Override
            public void success(String content) {
                Logger.i(content);
                UploadImgBean uploadImgBean = new Gson().fromJson(content, UploadImgBean.class);
                if (uploadImgBean.isSuccess()) {
                    String url = uploadImgBean.getData().getUrl();
                    addFile(url);
                } else {
                    ShowUtil.showToast(getString(R.string.upload_failure));
                }
            }

            @Override
            public void fail(String exception) {
                ShowUtil.showToast(getString(R.string.net_error));
            }
        });
    }

    private void addFile(String fileUrl) {
        PrintRequest.addFile(ImgPreviewActivity.this, "1", file.getName(), fileUrl, new HttpCallBack() {
            @Override
            public void success(String content) {
                Logger.i(content);
                AddFileSettingBean addFileSettingBean = new Gson().fromJson(content, AddFileSettingBean.class);
                if (addFileSettingBean.isSuccess()) {
                    turnPrintSetting(addFileSettingBean.getData().get(0));
                } else {
                    ToastUtil.doToast(getSelfActivity(), getString(R.string.upload_failure));
                }
            }

            @Override
            public void fail(String exception) {
                ShowUtil.showToast(getString(R.string.net_error));
            }
        });
    }

    public static final String PRINT_SETTING = "print_setting";

    private void turnPrintSetting(PrintSetting printSetting) {
        Intent intent = new Intent(ImgPreviewActivity.this, ImgPrintSettingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(PRINT_SETTING, printSetting);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
}
