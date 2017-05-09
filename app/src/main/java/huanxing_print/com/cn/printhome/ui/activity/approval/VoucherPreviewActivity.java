package huanxing_print.com.cn.printhome.ui.activity.approval;

import android.os.Bundle;
import android.view.View;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.util.CommonUtils;

/**
 * description: 凭证预览界面
 * author LSW
 * date 2017/5/8 20:08
 * update 2017/5/8
 */
public class VoucherPreviewActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 改变状态栏的颜色使其与APP风格一体化
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.activity_voucher_preview);

        init();
    }

    private void init() {
        findViewById(R.id.btn_print_proof).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (R.id.btn_print_proof == v.getId()) {
            //打印凭证
        }
    }
}
