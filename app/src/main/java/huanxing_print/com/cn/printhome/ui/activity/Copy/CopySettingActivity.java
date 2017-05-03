package huanxing_print.com.cn.printhome.ui.activity.Copy;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.StepViewUtil;
import huanxing_print.com.cn.printhome.view.StepLineView;

/**
 * Created by Administrator on 2017/5/3 0003.
 */

public class CopySettingActivity extends BaseActivity implements View.OnClickListener {

    private Context ctx;
    private ImageView iv_back;
    private LinearLayout ll_back;
    private TextView pickFileTv;
    private TextView pickPrinterTv;
    private TextView payTv;
    private LinearLayout lv1;
    private StepLineView stepView;
    private LinearLayout lv;
    private ImageView iv_paper;
    private ImageView iv_minus;
    private ImageView iv_plus;
    private TextView tv_vertical;
    private ImageView iv_orientation;
    private TextView tv_orientation;
    private TextView tv_black;
    private ImageView iv_color;
    private TextView tv_color;
    private TextView tv_a4;
    private ImageView iv_a43;
    private TextView tv_a3;
    private TextView tv_single;
    private ImageView iv_print_type;
    private TextView tv_double;
    private TextView tv_persion;
    private ImageView iv_copy_cz;
    private TextView tv_qun;
    private ImageView iv_cz;
    private TextView btn_preview;
    private LinearLayout ll_finish;
    private TextView tv_mount;
    private int copy_mount;
    private LinearLayout ll_cz;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_copy_set);
        ctx = this;
        CommonUtils.initSystemBar(this);
        StepViewUtil.init(ctx, findViewById(R.id.step), StepLineView.STEP_PAY);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        pickFileTv = (TextView) findViewById(R.id.pickFileTv);
        pickPrinterTv = (TextView) findViewById(R.id.pickPrinterTv);
        payTv = (TextView) findViewById(R.id.payTv);
        lv1 = (LinearLayout) findViewById(R.id.lv1);
        stepView = (StepLineView) findViewById(R.id.stepView);
        lv = (LinearLayout) findViewById(R.id.lv);

        iv_paper = (ImageView) findViewById(R.id.iv_paper);
        iv_minus = (ImageView) findViewById(R.id.iv_minus);
        tv_mount = (TextView) findViewById(R.id.tv_mount);
        iv_plus = (ImageView) findViewById(R.id.iv_plus);
        tv_vertical = (TextView) findViewById(R.id.tv_vertical);
        iv_orientation = (ImageView) findViewById(R.id.iv_orientation);
        tv_orientation = (TextView) findViewById(R.id.tv_orientation);
        tv_black = (TextView) findViewById(R.id.tv_black);
        iv_color = (ImageView) findViewById(R.id.iv_color);
        tv_color = (TextView) findViewById(R.id.tv_color);
        tv_a4 = (TextView) findViewById(R.id.tv_a4);
        iv_a43 = (ImageView) findViewById(R.id.iv_a43);
        tv_a3 = (TextView) findViewById(R.id.tv_a3);
        tv_single = (TextView) findViewById(R.id.tv_single);
        iv_print_type = (ImageView) findViewById(R.id.iv_print_type);
        tv_double = (TextView) findViewById(R.id.tv_double);
        tv_persion = (TextView) findViewById(R.id.tv_persion);
        iv_copy_cz = (ImageView) findViewById(R.id.iv_copy_cz);
        tv_qun = (TextView) findViewById(R.id.tv_qun);
        iv_cz = (ImageView) findViewById(R.id.iv_cz);
        ll_cz = (LinearLayout) findViewById(R.id.ll_cz);
        btn_preview = (TextView) findViewById(R.id.btn_preview);
        ll_finish = (LinearLayout) findViewById(R.id.ll_finish);
    }

    private void initData() {
        copy_mount = Integer.parseInt(tv_mount.getText().toString().trim());
    }

    private void initListener() {
        iv_back.setOnClickListener(this);
        iv_minus.setOnClickListener(this);
        iv_plus.setOnClickListener(this);
        iv_orientation.setOnClickListener(this);
        iv_color.setOnClickListener(this);
        iv_a43.setOnClickListener(this);
        iv_print_type.setOnClickListener(this);
        iv_copy_cz.setOnClickListener(this);
        iv_cz.setOnClickListener(this);
        ll_finish.setOnClickListener(this);
    }

    private boolean isVertical;
    private boolean isColor;
    private boolean isA4;
    private boolean isSingle;
    private boolean isPersion;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back://返回
                finishCurrentActivity();
                break;
            case R.id.iv_minus://减
                copy_mount--;
                if (copy_mount < 0) {
                    Toast.makeText(ctx, "页数不能小于0", Toast.LENGTH_SHORT).show();
                    return;
                }
                tv_mount.setText(copy_mount + "");
                break;
            case R.id.iv_plus://加
                copy_mount++;
                tv_mount.setText(copy_mount + "");
                break;
            case R.id.iv_orientation://方向
                if (isVertical) {//竖向
                    iv_paper.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.vertical));
                    tv_orientation.setTextColor(getResources().getColor(R.color.gray8));
                    tv_vertical.setTextColor(getResources().getColor(R.color.black2));
                } else {//横向
                    iv_paper.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.orientation));
                    tv_orientation.setTextColor(getResources().getColor(R.color.black2));
                    tv_vertical.setTextColor(getResources().getColor(R.color.gray8));
                }
                isVertical = !isVertical;
                break;
            case R.id.iv_color://色彩
                if (isColor) {//彩色
                    tv_black.setTextColor(getResources().getColor(R.color.gray8));
                    tv_color.setTextColor(getResources().getColor(R.color.black2));
                } else {//黑白
                    tv_black.setTextColor(getResources().getColor(R.color.black2));
                    tv_color.setTextColor(getResources().getColor(R.color.gray8));
                }
                isColor = !isColor;
                break;
            case R.id.iv_a43://纸张
                if (isA4) {
                    tv_a3.setTextColor(getResources().getColor(R.color.gray8));
                    tv_a4.setTextColor(getResources().getColor(R.color.black2));
                } else {
                    tv_a4.setTextColor(getResources().getColor(R.color.gray8));
                    tv_a3.setTextColor(getResources().getColor(R.color.black2));
                }
                isA4 = !isA4;
                break;
            case R.id.iv_print_type://单双面
                if (isSingle) {
                    tv_double.setTextColor(getResources().getColor(R.color.gray8));
                    tv_single.setTextColor(getResources().getColor(R.color.black2));
                } else {
                    tv_single.setTextColor(getResources().getColor(R.color.gray8));
                    tv_double.setTextColor(getResources().getColor(R.color.black2));
                }
                isSingle = !isSingle;
                break;
            case R.id.iv_copy_cz://支付方式
                if (isPersion) {//个人
                    tv_qun.setTextColor(getResources().getColor(R.color.gray8));
                    tv_single.setTextColor(getResources().getColor(R.color.black2));

                } else {//群
                    tv_qun.setTextColor(getResources().getColor(R.color.black2));
                    tv_single.setTextColor(getResources().getColor(R.color.gray8));
                }
                isPersion = !isPersion;
                break;
            case R.id.iv_cz://充值

                break;
            case R.id.ll_finish://完成
                break;
        }
    }
}
