package huanxing_print.com.cn.printhome.ui.activity.copy;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.log.Logger;
import huanxing_print.com.cn.printhome.model.print.AddOrderRespBean;
import huanxing_print.com.cn.printhome.model.print.FileBean;
import huanxing_print.com.cn.printhome.model.print.PrintSetting;
import huanxing_print.com.cn.printhome.net.request.print.HttpListener;
import huanxing_print.com.cn.printhome.net.request.print.PrintRequest;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.ShowUtil;
import huanxing_print.com.cn.printhome.util.StepViewUtil;
import huanxing_print.com.cn.printhome.view.StepLineView;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;

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
    private ImageView iv_cz_persion;
    private TextView btn_preview;
    private LinearLayout ll_finish;
    private TextView tv_mount;
    private LinearLayout ll_cz_persion;
    private ImageView iv_cz_qun;
    private LinearLayout ll_cz_qun;

    private String printerNo;
    private PrintSetting printSetting;

    //    colourFlag	彩色打印0-彩色 1-黑白	number
//    directionFlag	方向标识0-横版 1-竖版	number
//    doubleFlag	双面打印0-是 1-否	number
//    id	文件id	number
//    printCount	打印份数	number
//    scaleRatio	缩放比例 1-100	number
//    sizeType	大小类型0-A4 1-A3
    private int colourFlag = 1;
    private int directionFlag = 1;
    private int doubleFlag = 1;
    private int id;
    private int printCount = 1;
    private int sizeType = 0;

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
        initData();
        initView();
        initListener();
        log();
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        printerNo = bundle.getString(PRINTER_NO, "");
        printSetting = bundle.getParcelable(PRINT_SETTING);
        Logger.i(printerNo);
        Logger.i(printSetting);
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
        iv_cz_persion = (ImageView) findViewById(R.id.iv_cz_persion);
        ll_cz_persion = (LinearLayout) findViewById(R.id.ll_cz_persion);
        iv_cz_qun = (ImageView) findViewById(R.id.iv_cz_qun);
        ll_cz_qun = (LinearLayout) findViewById(R.id.ll_cz_qun);
        btn_preview = (TextView) findViewById(R.id.btn_preview);
        ll_finish = (LinearLayout) findViewById(R.id.ll_finish);
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
        iv_cz_persion.setOnClickListener(this);
        iv_cz_qun.setOnClickListener(this);
        ll_finish.setOnClickListener(this);
        ll_cz_persion.setOnClickListener(this);
        ll_cz_qun.setOnClickListener(this);
        btn_preview.setOnClickListener(this);
    }

    private boolean isPersion = true;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back://返回
                finishCurrentActivity();
                break;
            case R.id.iv_minus://减
                if (printCount == 1) {
                    Toast.makeText(ctx, "页数不能小于1", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    printCount--;
                }
                tv_mount.setText(printCount + "");
                break;
            case R.id.iv_plus://加
                printCount++;
                tv_mount.setText(printCount + "");
                break;
            case R.id.iv_orientation://方向
                if (directionFlag == 1) {
                    iv_orientation.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.on));
                    iv_paper.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.orientation));
                    tv_orientation.setTextColor(getResources().getColor(R.color.black2));
                    tv_vertical.setTextColor(getResources().getColor(R.color.gray8));
                    directionFlag = 0;

                } else {
                    iv_paper.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.vertical));
                    tv_orientation.setTextColor(getResources().getColor(R.color.gray8));
                    tv_vertical.setTextColor(getResources().getColor(R.color.black2));
                    iv_orientation.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.off));
                    directionFlag = 1;
                }
                break;
            case R.id.iv_color://色彩
                if (colourFlag == 1) {
                    tv_black.setTextColor(getResources().getColor(R.color.gray8));
                    tv_color.setTextColor(getResources().getColor(R.color.black2));
                    iv_color.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.on));
                    colourFlag = 0;

                } else {//黑白
                    tv_black.setTextColor(getResources().getColor(R.color.black2));
                    tv_color.setTextColor(getResources().getColor(R.color.gray8));
                    iv_color.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.off));
                    colourFlag = 1;
                }
                break;
            case R.id.iv_a43://纸张
                if (sizeType == 1) {
                    iv_a43.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.off));
                    tv_a3.setTextColor(getResources().getColor(R.color.black2));
                    tv_a4.setTextColor(getResources().getColor(R.color.gray8));
                    sizeType = 0;
                } else {
                    tv_a4.setTextColor(getResources().getColor(R.color.black2));
                    tv_a3.setTextColor(getResources().getColor(R.color.gray8));
                    iv_a43.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.on));
                    sizeType = 1;
                }
                break;
            case R.id.iv_print_type://单双面
                if (doubleFlag == 1) {
                    iv_print_type.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.on));
                    tv_double.setTextColor(getResources().getColor(R.color.black2));
                    tv_single.setTextColor(getResources().getColor(R.color.gray8));
                    doubleFlag = 0;
                } else {
                    iv_print_type.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.off));
                    tv_single.setTextColor(getResources().getColor(R.color.black2));
                    tv_double.setTextColor(getResources().getColor(R.color.gray8));
                    doubleFlag = 1;
                }
                break;
            case R.id.iv_copy_cz://支付方式
                if (isPersion) {
                    iv_copy_cz.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.on));
                    tv_qun.setTextColor(getResources().getColor(R.color.black2));
                    tv_persion.setTextColor(getResources().getColor(R.color.gray8));
                    ll_cz_persion.setVisibility(View.GONE);
                    ll_cz_qun.setVisibility(View.VISIBLE);
                    isPersion = false;
                } else {
                    iv_copy_cz.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.off));
                    tv_qun.setTextColor(getResources().getColor(R.color.gray8));
                    tv_persion.setTextColor(getResources().getColor(R.color.black2));
                    ll_cz_persion.setVisibility(View.VISIBLE);
                    ll_cz_qun.setVisibility(View.GONE);
                    isPersion = true;
                }
                break;
            case R.id.ll_cz_persion://第三方支付
                DialogUtils.showPayChooseDialog(ctx, new DialogUtils.PayChooseDialogCallBack() {
                    @Override
                    public void wechat() {
                        Toast.makeText(ctx, "微信支付", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void alipay() {
                        Toast.makeText(ctx, "支付宝支付", Toast.LENGTH_SHORT).show();
                    }
                });

                break;
            case R.id.ll_cz_qun://群支付
                DialogUtils.showQunChooseDialog(ctx, new DialogUtils.PayQunChooseDialogCallBack() {
                    @Override
                    public void tuniu() {
                        Toast.makeText(ctx, "图牛群支付", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void yinjia() {
                        Toast.makeText(ctx, "印加群支付", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.ll_finish://完成
                break;
            case R.id.btn_preview://完成
                complete();
                break;
        }
        log();
    }

    private void complete() {
        List<FileBean> fileList = new ArrayList();
        FileBean file = new FileBean();
        file.setId(printSetting.getId());
        fileList.add(file);

//        PrintStatusActivity.start(CopySettingActivity.this, null);
        PrintRequest.addOrder(this, printerNo, fileList, new HttpListener() {
            @Override
            public void onSucceed(String content) {
                AddOrderRespBean addOrderRespBean = new Gson().fromJson(content, AddOrderRespBean.class);
                if (addOrderRespBean.isSuccess()) {
                    ShowUtil.showToast(getString(R.string.add_order_success));
                    String orderId = addOrderRespBean.getData().getOrderId();
                } else {
                    ShowUtil.showToast(addOrderRespBean.getErrorMsg());
                }
            }

            @Override
            public void onFailed(String exception) {
                ShowUtil.showToast(getString(R.string.net_error));
            }
        });
    }

    public static final String PRINT_SETTING = "setting";
    public static final String PRINTER_NO = "pinter_no";

    public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent(context, CopySettingActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    private void log() {
        if (colourFlag == 0) {
            Logger.i("彩色");
        } else {
            Logger.i("黑白");
        }
        if (directionFlag == 0) {
            Logger.i("横版");
        } else {
            Logger.i("竖版");
        }
        if (doubleFlag == 0) {
            Logger.i("双面");
        } else {
            Logger.i("单面");
        }
        Logger.i(printCount + "张");
        if (sizeType == 0) {
            Logger.i("A4");
        } else {
            Logger.i("A3");
        }
    }
}
