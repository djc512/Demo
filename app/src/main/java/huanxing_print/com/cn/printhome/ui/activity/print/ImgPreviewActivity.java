package huanxing_print.com.cn.printhome.ui.activity.print;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.github.chrisbanes.photoview.PhotoView;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.lang.ref.WeakReference;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.model.print.AddFileSettingBean;
import huanxing_print.com.cn.printhome.model.print.PrintSetting;
import huanxing_print.com.cn.printhome.model.print.UploadFileBean;
import huanxing_print.com.cn.printhome.net.request.print.HttpListener;
import huanxing_print.com.cn.printhome.net.request.print.PrintRequest;
import huanxing_print.com.cn.printhome.util.FileType;
import huanxing_print.com.cn.printhome.util.FileUtils;
import huanxing_print.com.cn.printhome.util.GsonUtil;
import huanxing_print.com.cn.printhome.util.ImageUtil;
import huanxing_print.com.cn.printhome.util.PrintUtil;
import huanxing_print.com.cn.printhome.util.ShowUtil;
import huanxing_print.com.cn.printhome.view.dialog.Alert;

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
                turnFile();
                break;
        }
    }

    static class MyHandler extends Handler {
        WeakReference<ImgPreviewActivity> mActivity;

        MyHandler(ImgPreviewActivity activity) {
            mActivity = new WeakReference<ImgPreviewActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ImgPreviewActivity activity = mActivity.get();
            String base = (String) msg.obj;
            if (base != null) {
                activity.uploadFile(base);
            }
        }
    }

    private MyHandler mHandler = new MyHandler(this);

    private void uploadFile(String base) {
        PrintRequest.uploadFile(activity, FileType.getType(file.getPath()), base, file.getName(), new
                HttpListener() {
                    @Override
                    public void onSucceed(String content) {
                        UploadFileBean uploadFileBean = GsonUtil.GsonToBean(content, UploadFileBean.class);
                        if (uploadFileBean == null) {
                            return;
                        }
                        if (uploadFileBean.isSuccess()) {
                            if (isLoading()) {
                                String url = uploadFileBean.getData().getImgUrl();
                                addFile(url);
                            }
                        } else {
                            ShowUtil.showToast(getString(R.string.upload_failure));
                        }
                    }

                    @Override
                    public void onFailed(String exception) {
                        dismissLoading();
                        ShowUtil.showToast(getString(R.string.net_error));
                    }
                }, false);
    }

    private void turnFile() {
        file = new File(imgPath);
        if (file == null || !file.exists()) {
            return;
        }
        if (FileUtils.isOutOfSize(file)) {
            Alert.show(context, "提示", getString(R.string.size_out), null, new
                    DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
            return;
        }
        showLoading();
        new Thread() {
            @Override
            public void run() {
                super.run();
                Message msg = new Message();
                msg.obj = FileUtils.getBase64(file);
                mHandler.sendMessage(msg);
            }
        }.start();
    }

    private void addFile(String fileUrl) {
        PrintRequest.addFile(activity, file.getName(), fileUrl, PrintUtil.TYPE_PRINT, new HttpListener() {
            @Override
            public void onSucceed(String content) {
                dismissLoading();
                AddFileSettingBean addFileSettingBean = GsonUtil.GsonToBean(content, AddFileSettingBean.class);
                if (addFileSettingBean == null) {
                    return;
                }
                if (addFileSettingBean.isSuccess()) {
                    turnPickPrinter(addFileSettingBean.getData());
                } else {
                    ShowUtil.showToast(addFileSettingBean.getErrorMsg());
                }
            }

            @Override
            public void onFailed(String exception) {
                dismissLoading();
                ShowUtil.showToast(getString(R.string.net_error));
            }
        }, false);
    }

    private void turnPickPrinter(PrintSetting printSetting) {
        EventBus.getDefault().postSticky(new Integer(1));
        Bundle bundle = new Bundle();
        bundle.putParcelable(PickPrinterActivity.SETTING, printSetting);
        PickPrinterActivity.start(context, bundle);
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
