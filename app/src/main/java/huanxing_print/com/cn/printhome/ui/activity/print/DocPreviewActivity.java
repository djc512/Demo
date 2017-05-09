package huanxing_print.com.cn.printhome.ui.activity.print;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.io.File;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.model.print.AddFileSettingBean;
import huanxing_print.com.cn.printhome.model.print.PrintSetting;
import huanxing_print.com.cn.printhome.model.print.UploadFileBean;
import huanxing_print.com.cn.printhome.net.request.print.HttpListener;
import huanxing_print.com.cn.printhome.net.request.print.PrintRequest;
import huanxing_print.com.cn.printhome.ui.adapter.DocPreViewpageAdapter;
import huanxing_print.com.cn.printhome.util.FileType;
import huanxing_print.com.cn.printhome.util.FileUtils;
import huanxing_print.com.cn.printhome.util.GsonUtil;
import huanxing_print.com.cn.printhome.util.ShowUtil;


public class DocPreviewActivity extends BasePrintActivity implements View.OnClickListener {

    private ViewPager viewpager;

    private String url;
    private File file;
    private DocPreViewpageAdapter docPreViewpageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_preview);
        initData();
        initView();

//        upload(file);
    }

    private void initView() {
        initTitleBar("文件预览");
        setRightTvVisible();
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        docPreViewpageAdapter = new DocPreViewpageAdapter(context);
        viewpager.setAdapter(docPreViewpageAdapter);
    }


    private void initData() {
        Bundle bundle = getIntent().getExtras();
        file = (File) bundle.getSerializable(KEY_FILE);
    }

    private void upload(final File file) {
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
//                    turnPreView(url, file);
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

    private void preview(String fileUrl) {
        PrintRequest.docPreview(activity, fileUrl, new HttpListener() {
            @Override
            public void onSucceed(String content) {
                UploadFileBean uploadFileBean = GsonUtil.GsonToBean(content, UploadFileBean.class);
                if (uploadFileBean == null) {
                    return;
                }
                if (uploadFileBean.isSuccess()) {

                }
            }

            @Override
            public void onFailed(String exception) {
                ShowUtil.showToast(getString(R.string.net_error));
            }
        });
    }

    private void add() {
        PrintRequest.addFile(activity, "1", file.getName(), url, new HttpListener() {
            @Override
            public void onSucceed(String content) {
                AddFileSettingBean addFileSettingBean = GsonUtil.GsonToBean(content, AddFileSettingBean.class);
                if (addFileSettingBean == null) {
                    return;
                }
                if (addFileSettingBean.isSuccess()) {
                    setRightTvVisible();
//                    turnPrintSetting(addFileSettingBean.getData());
                } else {
                    ShowUtil.showToast(getString(R.string.upload_failure));
                }
            }

            @Override
            public void onFailed(String exception) {
                ShowUtil.showToast(getString(R.string.net_error));
            }
        }, true);
    }

    public static final String PRINT_SETTING = "print_setting";

    private void turnPrintSetting(PrintSetting printSetting) {
        Intent intent = new Intent(context, DocPrintActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(PRINT_SETTING, printSetting);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        switch (id) {
            case R.id.rightTv:
                PickPrinterActivity.start(context, null);
                finish();
//                add();
                break;
        }
    }

    private void setRightTvVisible() {
        findViewById(R.id.rightTv).setVisibility(View.VISIBLE);
        findViewById(R.id.rightTv).setOnClickListener(this);
    }

    public final static String KEY_URL = "url";
    public final static String KEY_FILE = "file";

    public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent(context, DocPreviewActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
