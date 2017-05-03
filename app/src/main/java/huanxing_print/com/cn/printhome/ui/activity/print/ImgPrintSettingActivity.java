package huanxing_print.com.cn.printhome.ui.activity.print;

import android.os.Bundle;
import android.view.View;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.model.print.PrintSetting;
import huanxing_print.com.cn.printhome.net.request.print.HttpListener;
import huanxing_print.com.cn.printhome.net.request.print.PrintRequest;
import huanxing_print.com.cn.printhome.util.ShowUtil;

public class ImgPrintSettingActivity extends BasePrintActivity {

    public static final String PRINT_SETTING = "print_setting";

    private PrintSetting printSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_print);
        initData();
    }

    private void initData() {
        printSetting = getIntent().getExtras().getParcelable(PRINT_SETTING);
    }

    private void delFile() {
        PrintRequest.delFile(activity, printSetting.getId(), new HttpListener() {
            @Override
            public void onSucceed(String content) {
                finish();
            }

            @Override
            public void onFailed(String exception) {
                ShowUtil.showToast(getString(R.string.net_error));
            }
        });
    }

    public void onDel(View view) {
        delFile();
    }
}
