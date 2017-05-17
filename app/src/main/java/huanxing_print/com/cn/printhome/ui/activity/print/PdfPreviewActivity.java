package huanxing_print.com.cn.printhome.ui.activity.print;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.lang.ref.WeakReference;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.log.Logger;
import huanxing_print.com.cn.printhome.model.print.AddFileSettingBean;
import huanxing_print.com.cn.printhome.model.print.PrintSetting;
import huanxing_print.com.cn.printhome.model.print.UploadFileBean;
import huanxing_print.com.cn.printhome.net.request.print.HttpListener;
import huanxing_print.com.cn.printhome.net.request.print.PrintRequest;
import huanxing_print.com.cn.printhome.util.FileType;
import huanxing_print.com.cn.printhome.util.FileUtils;
import huanxing_print.com.cn.printhome.util.GsonUtil;
import huanxing_print.com.cn.printhome.util.PrintUtil;
import huanxing_print.com.cn.printhome.util.ShowUtil;
import huanxing_print.com.cn.printhome.view.dialog.Alert;

public class PdfPreviewActivity extends BasePrintActivity implements View.OnClickListener, OnLoadCompleteListener {

    private PDFView pdfView;
    private String pdfPath;
    private File file;
    private int page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdt_preview);
        initData();
        initTitleBar(file.getName());
        initView();
    }

    private void initData() {
        pdfPath = (String) getIntent().getExtras().get(KEY_PDF_PATH);
        file = new File(pdfPath);
    }

    private void initView() {
        pdfView = (PDFView) findViewById(R.id.pdfView);
        pdfView.fromFile(new File(pdfPath))
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        switch (id) {
            case R.id.rightTv:
                if (page > 200) {
                    ShowUtil.showToast(getString(R.string.file_outpage));
                    return;
                }
                turnFile();
                break;
        }
    }

    @Override
    public void loadComplete(int nbPages) {
        Logger.i(nbPages);
        page = nbPages;
        setRightTvVisible();
    }

    static class MyHandler extends Handler {
        WeakReference<PdfPreviewActivity> mActivity;

        MyHandler(PdfPreviewActivity activity) {
            mActivity = new WeakReference<PdfPreviewActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            PdfPreviewActivity activity = mActivity.get();
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
                    turnPrintSetting(addFileSettingBean.getData());
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

    private void turnPrintSetting(PrintSetting printSetting) {
        if (page == 1) {
            EventBus.getDefault().postSticky(new Integer(1));
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable(PickPrinterActivity.SETTING, printSetting);
        PickPrinterActivity.start(context, bundle);
    }

    private void setRightTvVisible() {
        findViewById(R.id.rightTv).setVisibility(View.VISIBLE);
        findViewById(R.id.rightTv).setOnClickListener(this);
    }

    public static final String KEY_PDF_PATH = "pdf_path";

    public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent(context, PdfPreviewActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
