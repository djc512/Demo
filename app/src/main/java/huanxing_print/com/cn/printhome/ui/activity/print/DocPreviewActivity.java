package huanxing_print.com.cn.printhome.ui.activity.print;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.ViewPager;
import android.view.View;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.log.Logger;
import huanxing_print.com.cn.printhome.model.print.AddFileSettingBean;
import huanxing_print.com.cn.printhome.model.print.PrintSetting;
import huanxing_print.com.cn.printhome.net.request.print.HttpListener;
import huanxing_print.com.cn.printhome.net.request.print.PrintRequest;
import huanxing_print.com.cn.printhome.ui.adapter.DocPreViewpageAdapter;
import huanxing_print.com.cn.printhome.util.GsonUtil;
import huanxing_print.com.cn.printhome.util.PrintUtil;
import huanxing_print.com.cn.printhome.util.ShowUtil;
import huanxing_print.com.cn.printhome.util.ToastUtil;


public class DocPreviewActivity extends BasePrintActivity implements View.OnClickListener {

    private final int timeCount = 10 * 60 * 1000;
    private boolean isTimeout = false;
    private ViewPager viewpager;

    private List<String> fileUrlList;
    private String fileUrl;
    private File file;
    private DocPreViewpageAdapter docPreViewpageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_preview);
        initData();
        initView();
    }

    private void initView() {
        initTitleBar(file.getName());
        setRightTvVisible();
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        if (fileUrlList != null) {
            docPreViewpageAdapter = new DocPreViewpageAdapter(context, fileUrlList);
        }
        viewpager.setAdapter(docPreViewpageAdapter);
    }


    private void initData() {
        Bundle bundle = getIntent().getExtras();
        fileUrl = (String) bundle.getCharSequence(KEY_URL);
        fileUrlList = bundle.getStringArrayList(KEY_URL_LIST);
        file = (File) bundle.getSerializable(KEY_FILE);
        Logger.i(fileUrl);
        Logger.i(fileUrlList);
    }

    private void addFile(String fileUrl) {
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
                    turnPrintSetting(addFileSettingBean.getData());
                } else {
                    Logger.i("net error");
                    ToastUtil.doToast(context, getString(R.string.upload_failure));
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
        if (fileUrlList.size() == 1) {
            EventBus.getDefault().postSticky(new Integer(1));
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable(PickPrinterActivity.SETTING, printSetting);
        PickPrinterActivity.start(context, bundle);
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

    public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent(context, DocPreviewActivity.class);
        intent.putExtras(bundle);
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
    }
}
