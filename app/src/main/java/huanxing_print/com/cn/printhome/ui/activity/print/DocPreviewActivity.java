package huanxing_print.com.cn.printhome.ui.activity.print;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.io.File;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.model.print.AddFileSettingBean;
import huanxing_print.com.cn.printhome.model.print.PrintSetting;
import huanxing_print.com.cn.printhome.net.request.print.HttpListener;
import huanxing_print.com.cn.printhome.net.request.print.PrintRequest;
import huanxing_print.com.cn.printhome.util.GsonUtil;
import huanxing_print.com.cn.printhome.util.ShowUtil;


public class DocPreviewActivity extends BasePrintActivity {

    private String url;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_preview);
        initData();
    }

    public final static String KEY_URL = "url";
    public final static String KEY_FILE = "file";
    private void initData() {
        Bundle bundle = getIntent().getExtras();
        url = bundle.getString(KEY_URL);
        file = (File) bundle.getSerializable(KEY_FILE);
    }

    public void onAdd(View view) {
        PrintRequest.addFile(activity, "1", file.getName(), url, new HttpListener() {
            @Override
            public void onSucceed(String content) {
                AddFileSettingBean addFileSettingBean = GsonUtil.GsonToBean(content, AddFileSettingBean.class);
                if (addFileSettingBean == null) {
                    return;
                }
                if (addFileSettingBean.isSuccess()) {
                    turnPrintSetting(addFileSettingBean.getData());
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
}
