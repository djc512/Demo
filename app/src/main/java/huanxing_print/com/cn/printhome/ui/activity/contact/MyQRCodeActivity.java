package huanxing_print.com.cn.printhome.ui.activity.contact;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.QRUtil;

/**
 * Created by wanghao on 2017/5/4.
 */

public class MyQRCodeActivity extends BaseActivity implements View.OnClickListener{
    private CircleImageView my_icon;
    private TextView tv_my_name,tv_my_yjNum;
    private ImageView im_qr;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.activity_my_qrcode);
        initView();
        setListener();
    }

    private void initView() {
        my_icon = (CircleImageView) findViewById(R.id.iv_my_icon);
        tv_my_name = (TextView) findViewById(R.id.tv_my_name);
        tv_my_yjNum = (TextView) findViewById(R.id.tv_yjNum);
        im_qr = (ImageView) findViewById(R.id.iv_my_qr);

        Bitmap bitmap = QRUtil.createQRImage(this, im_qr, "这只是一个测试");
        if (bitmap != null && !bitmap.isRecycled()) {
            im_qr.setImageBitmap(bitmap);
        }
        tv_my_name.setText("汪浩");
        tv_my_yjNum.setText(String.format("印家号:%s","123456"));
    }

    private void setListener() {
        findViewById(R.id.ll_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finishCurrentActivity();
                break;
        }
    }
}
