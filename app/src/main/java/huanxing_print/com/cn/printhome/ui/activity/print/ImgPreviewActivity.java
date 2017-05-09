package huanxing_print.com.cn.printhome.ui.activity.print;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.constant.ConFig;
import huanxing_print.com.cn.printhome.log.Logger;
import huanxing_print.com.cn.printhome.model.print.AddFileSettingBean;
import huanxing_print.com.cn.printhome.model.print.PrintSetting;
import huanxing_print.com.cn.printhome.model.print.UploadFileBean;
import huanxing_print.com.cn.printhome.net.request.print.HttpListener;
import huanxing_print.com.cn.printhome.net.request.print.PrintRequest;
import huanxing_print.com.cn.printhome.util.AlertUtil;
import huanxing_print.com.cn.printhome.util.FileType;
import huanxing_print.com.cn.printhome.util.FileUtils;
import huanxing_print.com.cn.printhome.util.GsonUtil;
import huanxing_print.com.cn.printhome.util.ImageUtil;
import huanxing_print.com.cn.printhome.util.ShowUtil;
import huanxing_print.com.cn.printhome.util.ToastUtil;

public class ImgPreviewActivity extends BasePrintActivity implements View.OnClickListener {

    private PhotoView photoView;

    private String imgPath;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_priview);
        initData();
        initTitleBar("图片预览");
        initView();
        setRightTvVisible();
    }

    private void initData() {
        imgPath = (String) getIntent().getExtras().get(KEY_IMG_URI);
    }

    private void initView() {
        photoView = (PhotoView) findViewById(R.id.photoView);
        ImageUtil.showImageView(context, imgPath, photoView);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        switch (id) {
            case R.id.rightTv:
                PickPrinterActivity.start(context, null);
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
                UploadFileBean uploadFileBean = GsonUtil.GsonToBean(content, UploadFileBean.class);
                if (uploadFileBean == null) {
                    return;
                }
                if (uploadFileBean.isSuccess()) {
                    String url = uploadFileBean.getData().getImgUrl();
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

    private void turnPrintSetting(PrintSetting printSetting) {
        Intent intent = new Intent(ImgPreviewActivity.this, ImgPrintSettingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(ImgPrintSettingActivity.PRINT_SETTING, printSetting);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    private void setRightTvVisible() {
        findViewById(R.id.rightTv).setVisibility(View.VISIBLE);
        findViewById(R.id.rightTv).setOnClickListener(this);
    }

    public static final String KEY_IMG_URI = "image_path";

    public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent(context, ImgPreviewActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
