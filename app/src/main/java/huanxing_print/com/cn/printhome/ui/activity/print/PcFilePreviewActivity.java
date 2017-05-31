package huanxing_print.com.cn.printhome.ui.activity.print;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.github.chrisbanes.photoview.PhotoView;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.log.Logger;
import huanxing_print.com.cn.printhome.model.print.AddFileSettingBean;
import huanxing_print.com.cn.printhome.model.print.PrintFileInfo;
import huanxing_print.com.cn.printhome.model.print.PrintListBean;
import huanxing_print.com.cn.printhome.model.print.PrintSetting;
import huanxing_print.com.cn.printhome.net.request.print.HttpListener;
import huanxing_print.com.cn.printhome.net.request.print.PrintRequest;
import huanxing_print.com.cn.printhome.util.GsonUtil;
import huanxing_print.com.cn.printhome.util.ImageUtil;
import huanxing_print.com.cn.printhome.util.PrintUtil;
import huanxing_print.com.cn.printhome.util.ShowUtil;
import huanxing_print.com.cn.printhome.util.StringUtil;

public class PcFilePreviewActivity extends BasePrintActivity implements View.OnClickListener {

    private PrintListBean.FileInfo fileInfo;

    private PhotoView photoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initView();
    }

    @Override
    protected void setContent() {
        setContentView(R.layout.activity_pc_file_preview);
    }

    private void initData() {
        fileInfo = getIntent().getExtras().getParcelable(PC_FILE);
        Logger.i(fileInfo.toString());
    }

    public static final String PC_FILE = "pcFile";

    public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent(context, PcFilePreviewActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    private void initView() {
        initTitleBar(fileInfo.getFileName());
        photoView = (PhotoView) findViewById(R.id.photoView);
        ImageUtil.showImageView(context, fileInfo.getPreviewUrl(), photoView);
        setRightTvVisible();
    }

    private void setRightTvVisible() {
        findViewById(R.id.rightTv).setVisibility(View.VISIBLE);
        findViewById(R.id.rightTv).setOnClickListener(this);
    }

    private void addFile(PrintListBean.FileInfo fileInfo) {
        PrintRequest.addFile(activity, fileInfo.getFileName(), fileInfo.getFileUrl(), PrintUtil.TYPE_PRINT, new
                HttpListener() {
                    @Override
                    public void onSucceed(String content) {
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
                        ShowUtil.showToast(getString(R.string.net_error));
                    }
                }, false);
    }

    private void turnPickPrinter(PrintSetting printSetting) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(PickPrinterActivity.SETTING, printSetting);
        bundle.putParcelable(PickPrinterActivity.FILE_INFO, new PrintFileInfo(PrintFileInfo.TYPE_FILE, StringUtil
                .stringToInt(fileInfo.getFilePage())));
        bundle.putString(PickPrinterActivity.IMAGE_PATH, fileInfo.getPreviewUrl());
        PickPrinterActivity.start(context, bundle);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        switch (id) {
            case R.id.rightTv:
                addFile(fileInfo);
                break;
        }
    }
}
