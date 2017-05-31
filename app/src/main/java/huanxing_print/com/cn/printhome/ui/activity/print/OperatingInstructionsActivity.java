package huanxing_print.com.cn.printhome.ui.activity.print;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.util.CommonUtils;

/**
 * Created by Administrator on 2017/3/22 0022.
 */

public class OperatingInstructionsActivity extends BaseActivity implements View.OnClickListener{
    private LinearLayout ll_back;
    private WebView wv_xieyi;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_instructions);
        CommonUtils.initSystemBar(this);
        initView();
        initData();
        setListener();
    }
    private void initView() {
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        wv_xieyi = (WebView) findViewById(R.id.wv_xieyi);
    }
    private void initData() {
        String printUrl = "file:///android_asset/print.html";
        wv_xieyi.loadUrl(printUrl);
    }
    private void setListener() {
        ll_back.setOnClickListener(this);
        wv_xieyi.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_back:
                finishCurrentActivity();
                break;
        }
    }
}
