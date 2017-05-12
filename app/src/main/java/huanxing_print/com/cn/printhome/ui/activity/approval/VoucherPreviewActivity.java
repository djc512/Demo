package huanxing_print.com.cn.printhome.ui.activity.approval;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.net.callback.approval.CheckVoucherCallBack;
import huanxing_print.com.cn.printhome.net.request.approval.ApprovalRequest;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.ObjectUtils;

/**
 * description: 凭证预览界面
 * author LSW
 * date 2017/5/8 20:08
 * update 2017/5/8
 */
public class VoucherPreviewActivity extends BaseActivity implements View.OnClickListener {

    private ImageView img_voucher;

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

        img_voucher = (ImageView) findViewById(R.id.img_voucher);
        findViewById(R.id.btn_print_proof).setOnClickListener(this);
        ApprovalRequest.checkVoucher(getSelfActivity(), baseApplication.getLoginToken(),
                "", 1, checkVoucherCallBack);
    }

    CheckVoucherCallBack checkVoucherCallBack = new CheckVoucherCallBack() {
        @Override
        public void success(String msg, String data) {
            if (!ObjectUtils.isNull(data)) {
                Glide.with(getSelfActivity())
                        .load(data)
                        .placeholder(R.drawable.iv_head)
                        .error(R.drawable.iv_head)
                        .into(img_voucher);
            }
        }

        @Override
        public void fail(String msg) {

        }

        @Override
        public void connectFail() {

        }
    };

    @Override
    public void onClick(View v) {
        if (R.id.btn_print_proof == v.getId()) {
            //打印凭证
        }
    }
}
