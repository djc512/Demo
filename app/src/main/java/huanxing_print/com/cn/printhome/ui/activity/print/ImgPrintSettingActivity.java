package huanxing_print.com.cn.printhome.ui.activity.print;

import android.os.Bundle;
import android.os.Parcelable;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;

public class ImgPrintSettingActivity extends BasePrintActivity {

    private Parcelable printSetting;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_print);
    }

    private void initData() {
        printSetting  = getIntent().getExtras().getParcelable(ImgPreviewActivity.PRINT_SETTING);
    }

    private void delFile() {

    }
}
