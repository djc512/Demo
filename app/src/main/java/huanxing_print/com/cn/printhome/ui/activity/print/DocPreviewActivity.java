package huanxing_print.com.cn.printhome.ui.activity.print;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.event.print.FinishEvent;
import huanxing_print.com.cn.printhome.event.print.PreviewFlagEvent;
import huanxing_print.com.cn.printhome.log.Logger;
import huanxing_print.com.cn.printhome.model.print.AddFileSettingBean;
import huanxing_print.com.cn.printhome.model.print.DocPreviewResp;
import huanxing_print.com.cn.printhome.model.print.PrintFileInfo;
import huanxing_print.com.cn.printhome.model.print.PrintSetting;
import huanxing_print.com.cn.printhome.model.print.UploadFileBean;
import huanxing_print.com.cn.printhome.net.request.print.HttpListener;
import huanxing_print.com.cn.printhome.net.request.print.PrintRequest;
import huanxing_print.com.cn.printhome.ui.adapter.DocPreViewpageAdapter;
import huanxing_print.com.cn.printhome.util.FileType;
import huanxing_print.com.cn.printhome.util.FileUtils;
import huanxing_print.com.cn.printhome.util.GsonUtil;
import huanxing_print.com.cn.printhome.util.PrintUtil;
import huanxing_print.com.cn.printhome.util.ShowUtil;
import huanxing_print.com.cn.printhome.util.ToastUtil;
import huanxing_print.com.cn.printhome.view.dialog.Alert;


public class DocPreviewActivity extends BasePrintActivity implements View.OnClickListener {

    private final int timeCount = 10 * 60 * 1000;
    private boolean isTimeout = false;
    private ViewPager viewpager;
    private boolean previewFlag;
    private List<String> fileUrlList;
    private String fileUrl;
    private File file;
    private DocPreViewpageAdapter docPreViewpageAdapter;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_preview);
        EventBus.getDefault().register(context);
        initData();
    }

    private void initView() {
        initTitleBar(file.getName());
        if (previewFlag) {
            setRightTvInvisible();
        } else {
            setRightTvVisible();
        }
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        if (fileUrlList != null) {
            docPreViewpageAdapter = new DocPreViewpageAdapter(context, fileUrlList);
            viewpager.setAdapter(docPreViewpageAdapter);
        }
    }

    private void initData() {
        uri = getIntent().getData();
        if (uri != null) {
            // file:///storage/emulated/0/tencent/MicroMsg/Download/201%E9%A1%B5%E6%96%87%E6%A1%A3.docx
            Logger.i(uri.toString());
            String filepath = Uri.decode(getIntent().getDataString()).substring(7);
            Logger.i(filepath);
            file = new File(filepath);
            if (file == null) {
                ShowUtil.showToast(getString(R.string.file_error));
                finish();
            }
            turnFile(file);
        } else {
            Bundle bundle = getIntent().getExtras();
            fileUrl = (String) bundle.getCharSequence(KEY_URL);
            fileUrlList = bundle.getStringArrayList(KEY_URL_LIST);
            file = (File) bundle.getSerializable(KEY_FILE);
            previewFlag = bundle.getBoolean(PREVIEW_FLAG, false);
            Logger.i(fileUrl);
            Logger.i(fileUrlList);
            initView();
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onMessageEventPostThread(FinishEvent event) {
        if (event != null && event.isFinishFlag()) {
            finish();
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)
    public void onMessageEventPostThread(PreviewFlagEvent previewFlagEvent) {
        Logger.i(previewFlagEvent);
        this.previewFlag = previewFlagEvent.isPreviewFlag();
    }

    private void addFile(final String fileUrl) {
        showLoading();
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
                    Logger.i("net error");
                    ToastUtil.doToast(context, getString(R.string.upload_failure));
                }
            }

            @Override
            public void onFailed(String exception) {
                dismissLoading();
                if (uri != null) {
                    finish();
                }
                ShowUtil.showToast(getString(R.string.net_error));
            }
        }, false);
    }

    private void turnPickPrinter(PrintSetting printSetting) {
        if (fileUrlList.size() == 1) {
            EventBus.getDefault().postSticky(new Integer(1));
        } else {
            EventBus.getDefault().postSticky(new Integer(2));
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable(PickPrinterActivity.SETTING, printSetting);
        bundle.putParcelable(PickPrinterActivity.FILE_INFO, new PrintFileInfo(PrintFileInfo.TYPE_FILE, fileUrlList
                .size()));
        bundle.putString(PickPrinterActivity.IMAGE_PATH, fileUrlList.get(0));
        PickPrinterActivity.start(context, bundle);
    }

    static class MyHandler extends Handler {
        WeakReference<DocPreviewActivity> mActivity;

        MyHandler(DocPreviewActivity activity) {
            mActivity = new WeakReference<DocPreviewActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            DocPreviewActivity activity = mActivity.get();
            String base = (String) msg.obj;
            if (base != null) {
                activity.uploadFile(base);
            }
        }
    }

    private MyHandler mHandler = new MyHandler(this);


    public void turnFile(final File file) {
        this.file = file;
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
            finish();
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

    private void uploadFile(String base) {
        PrintRequest.uploadFile(activity, FileType.getType(file.getPath()), base, file.getName(), new
                HttpListener() {
                    @Override
                    public void onSucceed(String content) {
                        UploadFileBean uploadFileBean = GsonUtil.GsonToBean(content, UploadFileBean.class);
                        if (uploadFileBean == null) {
                            dismissLoading();
                            finish();
                            return;
                        }
                        if (uploadFileBean.isSuccess()) {
                            if (isLoading()) {
                                fileUrl = uploadFileBean.getData().getImgUrl();
                                getPreview(fileUrl);
                            }
                        } else {
                            dismissLoading();
                            ShowUtil.showToast(getString(R.string.upload_failure));
                            finish();
                        }
                    }

                    @Override
                    public void onFailed(String exception) {
                        dismissLoading();
                        ShowUtil.showToast(getString(R.string.net_error));
                        finish();
                    }
                }, false);
    }

    private void getPreview(final String url) {
        PrintRequest.docPreview(activity, url, new HttpListener() {
            @Override
            public void onSucceed(String content) {
                dismissLoading();
                DocPreviewResp docPreviewResp = GsonUtil.GsonToBean(content, DocPreviewResp.class);
                if (docPreviewResp == null) {
                    dismissLoading();
                    finish();
                    return;
                }
                if (!docPreviewResp.isSuccess()) {
                    ShowUtil.showToast(docPreviewResp.getErrorMsg());
                    finish();
                    return;
                }
                if (docPreviewResp.getData() == null || docPreviewResp.getData().getPaperNum() > 200) {
                    ShowUtil.showToast(getString(R.string.file_outpage));
                    finish();
                    return;
                }
                fileUrlList = docPreviewResp.getData().getArryList();
                initView();
            }

            @Override
            public void onFailed(String exception) {
                dismissLoading();
                ShowUtil.showToast(getString(R.string.net_error));
                finish();
            }
        });
    }

    private void setRightTvInvisible() {
        findViewById(R.id.rightTv).setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        switch (id) {
            case R.id.rightTv:
                if (isTimeout) {
                    ShowUtil.showToast(getString(R.string.file_outtime));
                }
                addFile(fileUrl);
                break;
        }
    }

    public static final String PRINT_SETTING = "print_setting";

    private void setRightTvVisible() {
        findViewById(R.id.rightTv).setVisibility(View.VISIBLE);
        findViewById(R.id.rightTv).setOnClickListener(this);
    }

    public final static String KEY_URL_LIST = "url_list";
    public final static String KEY_URL = "file_url";
    public final static String KEY_FILE = "file";
    public static final String PREVIEW_FLAG = "previewFlag";

    public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent(context, DocPreviewActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void start(Context context, Uri uri) {
        Intent intent = new Intent(context, DocPreviewActivity.class);
        intent.setData(uri);
        context.startActivity(intent);
    }

    private CountDownTimer timer = new CountDownTimer(timeCount, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            Logger.i(millisUntilFinished / 1000);
        }

        @Override
        public void onFinish() {
            isTimeout = true;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
        Logger.i("onDestroy");
        EventBus.getDefault().removeAllStickyEvents();
        EventBus.getDefault().unregister(context);
    }
}
