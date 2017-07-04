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
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.lang.ref.WeakReference;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.event.print.FinishEvent;
import huanxing_print.com.cn.printhome.model.print.AddFileSettingBean;
import huanxing_print.com.cn.printhome.model.print.PrintFileInfo;
import huanxing_print.com.cn.printhome.model.print.PrintSetting;
import huanxing_print.com.cn.printhome.model.print.UploadFileBean;
import huanxing_print.com.cn.printhome.net.request.print.HttpListener;
import huanxing_print.com.cn.printhome.net.request.print.PrintRequest;
import huanxing_print.com.cn.printhome.util.FileType;
import huanxing_print.com.cn.printhome.util.FileUtils;
import huanxing_print.com.cn.printhome.util.GsonUtil;
import huanxing_print.com.cn.printhome.util.image.ImageUtil;
import huanxing_print.com.cn.printhome.util.PrintUtil;
import huanxing_print.com.cn.printhome.util.ShowUtil;
import huanxing_print.com.cn.printhome.view.dialog.Alert;

public class ImgPreviewActivity extends BasePrintActivity implements View.OnClickListener {

    private PhotoView photoView;

    private String imgPath;
    private File file;
    private boolean previewFlag;//是否只预览

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(context);
        initData();
        initView();
    }

    @Override
    protected void setContent() {
        setContentView(R.layout.activity_img_priview);
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        previewFlag = bundle.getBoolean(PREVIEW_FLAG, false);
        imgPath = (String) bundle.get(KEY_IMG_URI);
        file = new File(imgPath);
    }

    private void initView() {
        if (previewFlag) {
            setRightTvInvisible();
            initTitleBar(file.getName());
        } else {
            setRightTvVisible();
            initTitleBar("图片预览");
        }
        photoView = (PhotoView) findViewById(R.id.photoView);
        ImageUtil.showImageView(context, imgPath, photoView);
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onMessageEventPostThread(FinishEvent event) {
        if (event != null && event.isFinishFlag()) {
            finish();
        }
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
                            dismissLoading();
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
        bundle.putParcelable(PickPrinterActivity.FILE_INFO, new PrintFileInfo(PrintFileInfo.TYPE_IMAGE, 1));
        bundle.putString(PickPrinterActivity.IMAGE_PATH, imgPath);
        PickPrinterActivity.start(context, bundle);
    }

    private void setRightTvVisible() {
        findViewById(R.id.rightTv).setVisibility(View.VISIBLE);
        findViewById(R.id.rightTv).setOnClickListener(this);
    }

    private void setRightTvInvisible() {
        findViewById(R.id.rightTv).setVisibility(View.INVISIBLE);
    }

    public static final String KEY_IMG_URI = "image_path";
    public static final String PREVIEW_FLAG = "previewFlag";

    public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent(context, ImgPreviewActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(context);
    }
}
