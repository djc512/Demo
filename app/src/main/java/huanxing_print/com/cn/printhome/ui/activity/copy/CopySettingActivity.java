package huanxing_print.com.cn.printhome.ui.activity.copy;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.util.DensityUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.event.print.WechatPayEvent;
import huanxing_print.com.cn.printhome.log.Logger;
import huanxing_print.com.cn.printhome.model.CommonResp;
import huanxing_print.com.cn.printhome.model.my.WeChatPayBean;
import huanxing_print.com.cn.printhome.model.print.AddOrderRespBean;
import huanxing_print.com.cn.printhome.model.print.FileBean;
import huanxing_print.com.cn.printhome.model.print.GroupResp;
import huanxing_print.com.cn.printhome.model.print.PrintFileInfo;
import huanxing_print.com.cn.printhome.model.print.PrintInfoResp;
import huanxing_print.com.cn.printhome.model.print.PrintSetting;
import huanxing_print.com.cn.printhome.net.callback.my.Go2PayCallBack;
import huanxing_print.com.cn.printhome.net.callback.my.WeChatCallBack;
import huanxing_print.com.cn.printhome.net.request.my.Go2PayRequest;
import huanxing_print.com.cn.printhome.net.request.print.HttpListener;
import huanxing_print.com.cn.printhome.net.request.print.PrintRequest;
import huanxing_print.com.cn.printhome.ui.activity.my.AccountActivity;
import huanxing_print.com.cn.printhome.ui.activity.print.PrintStatusActivity;
import huanxing_print.com.cn.printhome.ui.adapter.GroupRecylerAdapter;
import huanxing_print.com.cn.printhome.util.DisplayUtil;
import huanxing_print.com.cn.printhome.util.GsonUtil;
import huanxing_print.com.cn.printhome.util.ImageUtil;
import huanxing_print.com.cn.printhome.util.Pay.PayUtil;
import huanxing_print.com.cn.printhome.util.PriceUtil;
import huanxing_print.com.cn.printhome.util.PrintUtil;
import huanxing_print.com.cn.printhome.util.SharedPreferencesUtils;
import huanxing_print.com.cn.printhome.util.ShowUtil;
import huanxing_print.com.cn.printhome.util.StatusBarCompat;
import huanxing_print.com.cn.printhome.util.StepViewUtil;
import huanxing_print.com.cn.printhome.util.StringUtil;
import huanxing_print.com.cn.printhome.view.RecyclerViewDivider;
import huanxing_print.com.cn.printhome.view.StepLineView;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;
import huanxing_print.com.cn.printhome.view.dialog.LoadingDialog;

import static huanxing_print.com.cn.printhome.R.drawable.on;


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
    private TextView groupTv;
    private ImageView iv_cz_persion;
    private TextView btn_preview;
    private LinearLayout ll_finish;
    private TextView tv_mount;
    private LinearLayout ll_cz_persion;
    private ImageView iv_cz_qun;
    private LinearLayout ll_cz_qun;
    private LinearLayout printTypeLyt;
    protected LoadingDialog loadingDialog;
    private SeekBar seekBar;
    private LinearLayout seekLyt;
    private ImageView scaleImg;
    private ImageView paperImg;
    private TextView defaultTv;
    private TextView defTv;
    private LinearLayout scaleLyt;
    private LinearLayout directionLv;
    private LinearLayout paperTypeLyt;
    private LinearLayout paperSizeLyt;
    private RelativeLayout imgBcRyt;

    private String previewPath;
    private PrintFileInfo printFileInfo;

    private List<GroupResp.Group> groupList = new ArrayList<>();
    private String printerNo;
    private PrintSetting printSetting;
    private PrintSetting newSetting;
    private int printType;
    private PrintInfoResp.PrinterPrice printerPrice;
    private long orderId;
    private GroupResp.Group group;
    private int scaleRatio = 100;
    private boolean isStandard = true;

    //    colourFlag	彩色打印0-彩色 1-黑白	number
    //    directionFlag	方向标识0-横版  1-竖版	number
    //    doubleFlag	双面打印0-是  1-否	number
    //    printCount	打印份数	number
    //    scaleRatio	缩放比例 1-100	number
    //    sizeType	   大小类型0-A4  1-A3
    private int colourFlag = 1;
    private int directionFlag = 1;
    private int doubleFlag = 1;
    private int printCount = 1;
    private int sizeType = 0;
    private int paperType = PrintUtil.SETTING_COMMON;
    private int pageCount = 2;

    private int id;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_copy_set);
        StatusBarCompat.compat(CopySettingActivity.this, ContextCompat.getColor(CopySettingActivity.this, R.color
                .statusbar_gray));
        EventBus.getDefault().register(CopySettingActivity.this);
        ctx = this;
        initData();
        log();
    }

    private void initCommonStep() {
        StepViewUtil.init(ctx, findViewById(R.id.step), StepLineView.STEP_PAY);
    }

    private void initCopyStep() {
        StepViewUtil.init(ctx, findViewById(R.id.step), StepLineView.STEP_PAY);
        TextView pickFileTv = (TextView) findViewById(R.id.pickFileTv);
        pickFileTv.setText("采集制作");
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        printFileInfo = bundle.getParcelable(FILE_INFO);
        if (printFileInfo.getFileType() == PrintFileInfo.TYPE_COPY) {
            initCopyStep();
        } else {
            initCommonStep();
        }
        Logger.i(printFileInfo.toString());
        previewPath = bundle.getString(PREVIEW_PATH);
        printType = bundle.getInt(PRINT_TYPE);
        printerNo = bundle.getString(PRINTER_NO);
        printSetting = bundle.getParcelable(PRINT_SETTING);
        newSetting = printSetting.clone();
        newSetting.setScaleRatio(100);
        newSetting.setPaperType(PrintUtil.SETTING_COMMON);
        colourFlag = newSetting.getColourFlag();
        directionFlag = newSetting.getDirectionFlag();
        doubleFlag = newSetting.getDoubleFlag();
        printCount = newSetting.getPrintCount();
        sizeType = newSetting.getSizeType();
//        Logger.i(previewPath);
//        Logger.i(printerNo);
//        Logger.i(printSetting.toString());
        requeryPrice(printerNo);
    }

    private boolean isSettingChange() {
        updateSetting();
        if (newSetting.toString().equals(printSetting.toString())) {
            return true;
        }
        return true;
    }

    private void updateSetting() {
        newSetting.setColourFlag(colourFlag);
        newSetting.setDirectionFlag(directionFlag);
        newSetting.setDoubleFlag(doubleFlag);
        newSetting.setSizeType(sizeType);
        newSetting.setPrintCount(printCount);
        newSetting.setScaleRatio(scaleRatio);
        newSetting.setPaperType(paperType);
    }

    public void requeryPrice(String printerNo) {
        PrintRequest.queryPrinterPrice(this, printerNo, new HttpListener() {
            @Override
            public void onSucceed(String content) {
                PrintInfoResp printInfoResp = GsonUtil.GsonToBean(content, PrintInfoResp.class);
                if (printInfoResp != null && printInfoResp.isSuccess()) {
                    PrintInfoResp.PrinterPrice printerPrice = printInfoResp.getData();
                    if (printerPrice != null) {
                        setPrice(printerPrice);
                        initView();
                        initListener();
                    } else {
                        ShowUtil.showToast(getString(R.string.price_error));
                    }
                } else {
                    ShowUtil.showToast(getString(R.string.price_error));
                }
            }

            @Override
            public void onFailed(String exception) {
                ShowUtil.showToast(getString(R.string.net_error));
            }
        });
        Logger.i(printerNo);
    }

    private void complete() {
        showLoading();
        if (!isSettingChange()) {
            addOrder();
        } else {
            modifySetting();
        }
    }

    private void addOrder() {
        List<FileBean> fileList = new ArrayList();
        fileList.add(new FileBean(printSetting.getId()));
        PrintRequest.addOrder(this, printerNo, fileList, new HttpListener() {
            @Override
            public void onSucceed(String content) {
                AddOrderRespBean addOrderRespBean = new Gson().fromJson(content, AddOrderRespBean.class);
                if (addOrderRespBean != null && addOrderRespBean.isSuccess() && isLoading()) {
                    AddOrderRespBean.Order order = addOrderRespBean.getData();
                    orderId = StringUtil.stringToLong(addOrderRespBean.getData().getOrderId());
                    if (isPersion) {
                        if (StringUtil.stringToFloat(order.getTotalAmount()) <= StringUtil.stringToFloat(order
                                .getTotleBalance())) {
                            balancePay();
                        } else {
                            showPayType();
                            //Logger.i("余额不足,调用第三方支付");
                        }
                    } else {
                        groupPay();
                        //Logger.i("使用群支付");
                    }
                } else {
                    dismissLoading();
                    ShowUtil.showToast(addOrderRespBean.getErrorMsg());
                }
            }

            @Override
            public void onFailed(String exception) {
                dismissLoading();
                ShowUtil.showToast(getString(R.string.net_error));
            }
        });
    }

    private void groupPay() {
        PrintRequest.groupPay(this, group.getGroupId(), orderId, new HttpListener() {
            @Override
            public void onSucceed(String content) {
                //Logger.i("groupPay onSucceed");
                CommonResp resp = new Gson().fromJson(content, CommonResp.class);
                if (resp.isSuccess() && isLoading()) {
                    //Logger.i("modifySetting onSucceed");
                    print();
                } else {
                    dismissLoading();
                    ShowUtil.showToast(resp.getErrorMsg());
                }
            }

            @Override
            public void onFailed(String exception) {
                dismissLoading();
                ShowUtil.showToast(getString(R.string.net_error));
            }
        });
    }

    private void modifySetting() {
        PrintRequest.modifySetting(this, newSetting.getColourFlag(), newSetting.getDirectionFlag(), newSetting
                        .getDoubleFlag(), newSetting.getId(), newSetting.getPrintCount(), newSetting.getSizeType(),
                newSetting.getScaleRatio(), newSetting.getPaperType(),
                new HttpListener() {
                    @Override
                    public void onSucceed(String content) {
                       // Logger.i("modifySetting onSucceed");
                        CommonResp resp = new Gson().fromJson(content, CommonResp.class);
                        if (resp.isSuccess() && isLoading()) {
                            addOrder();
                        } else {
                            dismissLoading();
                        }
                    }

                    @Override
                    public void onFailed(String exception) {
                        dismissLoading();
                        ShowUtil.showToast(getString(R.string.net_error));
                    }
                });
    }

    private void balancePay() {
        PrintRequest.balancePay(this, orderId, new HttpListener() {
            @Override
            public void onSucceed(String content) {
               // Logger.i("balancePay onSucceed");
                CommonResp resp = new Gson().fromJson(content, CommonResp.class);
                if (resp.isSuccess() && isLoading()) {
                    //Logger.i("modifySetting onSucceed");
                    print();
                } else {
                    dismissLoading();
                    ShowUtil.showToast(resp.getErrorMsg());
                }
            }

            @Override
            public void onFailed(String exception) {
                dismissLoading();
                ShowUtil.showToast(getString(R.string.net_error));
            }
        });
    }

    private void print() {
        PrintRequest.print(this, orderId, new HttpListener() {
            @Override
            public void onSucceed(String content) {
                Logger.i("print onSucceed");
                CommonResp resp = new Gson().fromJson(content, CommonResp.class);
                if (resp.isSuccess()) {
                    turnPrintState();
                } else {
                    ShowUtil.showToast(resp.getErrorMsg());
                }
                dismissLoading();
            }

            @Override
            public void onFailed(String exception) {
                dismissLoading();
                ShowUtil.showToast(getString(R.string.net_error));
            }
        });
    }

    private void queryGroup() {
        updateSetting();
        if (printerPrice == null) {
            ShowUtil.showToast(getString(R.string.price_error));
            return;
        }
        float price = PriceUtil.getPrice(newSetting, printerPrice);
        //Logger.i(price);
        showLoading();
        PrintRequest.queryGroup(this, price + "", new HttpListener() {
            @Override

            public void onSucceed(String content) {
                dismissLoading();
                Logger.i(content);
                GroupResp resp = new Gson().fromJson(content, GroupResp.class);
                if (resp.isSuccess()) {
                    if (resp.getData() == null || resp.getData().size() == 0) {
                        ShowUtil.showToast("没有可支付的群");
                        setGroupViewGone();
                    } else {
                        groupList = resp.getData();
                        setGroupViewVisible(groupList.get(0));
                    }
                } else {
                    setGroupViewGone();
                    ShowUtil.showToast(resp.getErrorMsg());
                }
            }

            @Override
            public void onFailed(String exception) {
                dismissLoading();
                ShowUtil.showToast(getString(R.string.net_error));
            }
        });
    }

    private void turnPrintState() {
        Bundle bundle = new Bundle();
        bundle.putLong(PrintStatusActivity.ORDER_ID, orderId);
        bundle.putParcelable(PrintStatusActivity.PRINTER_PRICE, printerPrice);
        PrintStatusActivity.start(this, bundle);
        finish();
    }

    private void setPrice(PrintInfoResp.PrinterPrice printerPrice) {
        this.printerPrice = printerPrice;
    }

    private void initView() {
        imgBcRyt = (RelativeLayout) findViewById(R.id.imgBcRyt);
        paperSizeLyt = (LinearLayout) findViewById(R.id.paperSizeLyt);
        paperTypeLyt = (LinearLayout) findViewById(R.id.paperTypeLyt);
        paperImg = (ImageView) findViewById(R.id.paperImg);
        directionLv = (LinearLayout) findViewById(R.id.directionLv);
        scaleLyt = (LinearLayout) findViewById(R.id.scaleLyt);
        defaultTv = (TextView) findViewById(R.id.defaultTv);
        defTv = (TextView) findViewById(R.id.defTv);
        scaleImg = (ImageView) findViewById(R.id.scaleImg);
        seekLyt = (LinearLayout) findViewById(R.id.seekLyt);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setPreviewScale(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        printTypeLyt = (LinearLayout) findViewById(R.id.printTypeLyt);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        pickFileTv = (TextView) findViewById(R.id.pickFileTv);
        pickPrinterTv = (TextView) findViewById(R.id.pickPrinterTv);
        payTv = (TextView) findViewById(R.id.payTv);
        lv1 = (LinearLayout) findViewById(R.id.lv1);
        stepView = (StepLineView) findViewById(R.id.stepView);
        lv = (LinearLayout) findViewById(R.id.lv);

        groupTv = (TextView) findViewById(R.id.groupTv);
        iv_paper = (ImageView) findViewById(R.id.iv_paper);
        ImageUtil.showImageView(ctx, previewPath, iv_paper);
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
        ll_cz_persion.setVisibility(View.VISIBLE);
        ll_cz_qun.setVisibility(View.GONE);
        //    colourFlag	彩色打印0-彩色 1-黑白	number
        //    directionFlag	方向标识0-横版  1-竖版	number
        //    doubleFlag	双面打印0-是  1-否	number
        //    printCount	打印份数	number
        //    scaleRatio	缩放比例 1-100	number
        //    sizeType	   大小类型0-A4  1-A3
        tv_mount.setText(newSetting.getPrintCount() + "");
        if (directionFlag == 0) {
            iv_orientation.setImageBitmap(BitmapFactory.decodeResource(getResources(), on));
            setPreviewHorizontal();
        } else {
            iv_orientation.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.off));
            setPreviewVertical();
        }
        if (PrintUtil.PRINTER_TYPE_BLACK.equals(printerPrice.getPrinterType())) {
            iv_color.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.off));
            colourFlag = 1;
        } else {
            if (colourFlag == 0) {
                iv_color.setImageBitmap(BitmapFactory.decodeResource(getResources(), on));
            } else {
                iv_color.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.off));
            }
        }
        if (sizeType == 0) {
            iv_a43.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.off));
        } else {
            iv_a43.setImageBitmap(BitmapFactory.decodeResource(getResources(), on));
        }
        if (doubleFlag == 0) {
            iv_print_type.setImageBitmap(BitmapFactory.decodeResource(getResources(), on));
        } else {
            iv_print_type.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.off));
        }
        if (isStandard) {
            seekLyt.setVisibility(View.GONE);
            scaleImg.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.off));
            isStandard = true;
            scaleRatio = 100;
            seekBar.setProgress(100);
        } else {
            seekLyt.setVisibility(View.VISIBLE);
            scaleImg.setImageBitmap(BitmapFactory.decodeResource(getResources(), on));
        }
        if (printType == PrintUtil.PRINT_TYPE_FILE) {
            printTypeLyt.setVisibility(View.GONE);
            scaleLyt.setVisibility(View.VISIBLE);
        }
        if (printType == PrintUtil.PRINT_TYPE_ID) {
            printTypeLyt.setVisibility(View.GONE);
            scaleLyt.setVisibility(View.GONE);
            directionLv.setVisibility(View.GONE);
            paperSizeLyt.setVisibility(View.GONE);
        }
        if (printType == PrintUtil.PRINT_TYPE_CENSUS) {
            printTypeLyt.setVisibility(View.GONE);
            scaleLyt.setVisibility(View.GONE);
            directionLv.setVisibility(View.GONE);
            paperSizeLyt.setVisibility(View.GONE);
        }
        if (printType == PrintUtil.PRINT_TYPE_PASSFORT) {
            printTypeLyt.setVisibility(View.GONE);
            scaleLyt.setVisibility(View.GONE);
            directionLv.setVisibility(View.GONE);
            paperSizeLyt.setVisibility(View.GONE);
        }
        if (printType == PrintUtil.PRINT_TYPE_PRINT) {
            printTypeLyt.setVisibility(View.VISIBLE);
            scaleLyt.setVisibility(View.GONE);
        }
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
        scaleImg.setOnClickListener(this);
        paperImg.setOnClickListener(this);
    }

    private boolean isPersion = true;

    private void setPreviewScale(int progress) {
        scaleRatio = (int) (25 + (((float) progress / 100)) * 75);
        Logger.i(scaleRatio);
        int newWidth;
        int vWidth = DensityUtil.dip2px(ctx, 43);
        int hWidth = DensityUtil.dip2px(ctx, 84);
        int height = DensityUtil.dip2px(ctx, 60);
        int newHeight = (int) (height * 0.25 + (height * 0.75 * progress / 100));
        RelativeLayout.LayoutParams layout = (RelativeLayout.LayoutParams) iv_paper.getLayoutParams();
        if (directionFlag == 0) {
            newWidth = (int) (hWidth * 0.25 + (hWidth * 0.75 * progress / 100));
        } else {
            newWidth = (int) (vWidth * 0.25 + (vWidth * 0.75 * progress / 100));
        }
        iv_paper.getLayoutParams().height = newHeight;
        iv_paper.getLayoutParams().width = newWidth;
        iv_paper.setLayoutParams(layout);
    }

    private void setGroupViewVisible(GroupResp.Group group) {
        this.group = group;
        groupTv.setText(group.getGroupName());
        iv_copy_cz.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.on));
        ll_cz_persion.setVisibility(View.GONE);
        ll_cz_qun.setVisibility(View.VISIBLE);
        isPersion = false;
    }

    private void setGroupViewGone() {
        iv_copy_cz.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.off));
        ll_cz_persion.setVisibility(View.VISIBLE);
        ll_cz_qun.setVisibility(View.GONE);
        isPersion = true;
    }

    /**
     * 预览竖版
     */
    private void setPreviewVertical() {
        directionFlag = 1;
        iv_orientation.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.off));
        int height = DisplayUtil.dip2px(ctx, 60);
        int width = DisplayUtil.dip2px(ctx, 43);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) imgBcRyt.getLayoutParams();
        imgBcRyt.getLayoutParams().height = height;
        imgBcRyt.getLayoutParams().width = width;
        imgBcRyt.setLayoutParams(layoutParams);
    }

    /**
     * 预览横版
     */
    private void setPreviewHorizontal() {
        directionFlag = 0;
        iv_orientation.setImageBitmap(BitmapFactory.decodeResource(getResources(), on));
        int height = DisplayUtil.dip2px(ctx, 60);
        int width = DisplayUtil.dip2px(ctx, 84);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) imgBcRyt.getLayoutParams();
        imgBcRyt.getLayoutParams().height = height;
        imgBcRyt.getLayoutParams().width = width;
        imgBcRyt.setLayoutParams(layoutParams);
    }

    private boolean isDoublePrint() {
        if (PrintFileInfo.TYPE_IMAGE == printFileInfo.getFileType()) {
            ShowUtil.showToast("图片只能单面打印");
            return false;
        }
        if (PrintFileInfo.TYPE_COPY == printFileInfo.getFileType()) {
            ShowUtil.showToast("图片只能单面打印");
            return false;
        }
        if (printFileInfo.getPageCount() <= 1) {
            ShowUtil.showToast("单页文件只能单面打印");
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        boolean priceFlag = false;
        switch (v.getId()) {
            case R.id.scaleImg://缩放
                if (isStandard) {
                    seekLyt.setVisibility(View.VISIBLE);
                    scaleImg.setImageBitmap(BitmapFactory.decodeResource(getResources(), on));
                    isStandard = false;
                } else {
                    seekLyt.setVisibility(View.GONE);
                    scaleImg.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.off));
                    isStandard = true;
                    scaleRatio = 100;
                    seekBar.setProgress(100);
                }
                break;
            case R.id.iv_back://返回
                finishCurrentActivity();
                break;
            case R.id.iv_minus://减
                if (printCount == 1) {
                    Toast.makeText(ctx, "数量不能小于1", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    printCount--;
                }
                tv_mount.setText(printCount + "");
                priceFlag = true;
                break;
            case R.id.iv_plus://加
                if (sizeType == 0 && StringUtil.stringToInt(printerPrice.getA4Num()) <= printCount) {
                    ShowUtil.showToast(getString(R.string.page_out));
                    return;
                }
                if (sizeType == 1 && StringUtil.stringToInt(printerPrice.getA3Num()) <= printCount) {
                    ShowUtil.showToast(getString(R.string.page_out));
                    return;
                }
                printCount++;
                tv_mount.setText(printCount + "");
                priceFlag = true;
                break;
            case R.id.iv_orientation://方向
                if (directionFlag == 1) {
                    setPreviewHorizontal();
                } else {
                    setPreviewVertical();
                }
                setPreviewScale(100);
                scaleRatio = 100;
                seekBar.setProgress(100);
                break;
            case R.id.iv_color://色彩
                if (PrintUtil.PRINTER_TYPE_BLACK.equals(printerPrice.getPrinterType())) {
                    ShowUtil.showToast("黑白机不可选彩色");
                    return;
                } else {
                    if (colourFlag == 1) {
                        iv_color.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.on));
                        colourFlag = 0;
                    } else {//黑白
                        if (paperType == PrintUtil.SETTING_PHOTO) {
                            ShowUtil.showToast("相片纸不能黑白打印");
                            return;
                        }
                        iv_color.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.off));
                        colourFlag = 1;
                    }
                }
                priceFlag = true;
                break;
            case R.id.paperImg://纸张类型
                if (PrintUtil.PRINTER_TYPE_PHOTO != printerPrice.getPaperType()) {
                    ShowUtil.showToast("打印机不支持相片纸打印");
                    return;
                }
                if (StringUtil.stringToInt(printerPrice.getPhotoNum()) == 0) {
                    ShowUtil.showToast("纸张不足");
                } else {
                    if (paperType == PrintUtil.SETTING_COMMON) {
                        paperImg.setImageBitmap(BitmapFactory.decodeResource(getResources(), on));
                        paperType = PrintUtil.SETTING_PHOTO;
                        iv_color.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.on));
                        colourFlag = 0;
                        iv_print_type.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.off));
                        doubleFlag = 1;
                        iv_a43.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.off));
                        sizeType = 0;
                    } else {
                        paperImg.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.off));
                        paperType = PrintUtil.SETTING_COMMON;
                    }
                }
                priceFlag = true;
                break;
            case R.id.iv_a43://纸张大小
                if (sizeType == 1) {
                    if (printerPrice.getA4Num() != null && printCount > StringUtil.stringToInt(printerPrice.getA4Num
                            ())) {
                        printCount = StringUtil.stringToInt(printerPrice.getA4Num());
                        tv_mount.setText(printCount + "");
                    }
                    iv_a43.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.off));
                    sizeType = 0;
                } else {
                    if (printerPrice.getA3Num() != null && printCount > StringUtil.stringToInt(printerPrice.getA3Num
                            ())) {
                        printCount = StringUtil.stringToInt(printerPrice.getA3Num());
                        tv_mount.setText(printCount + "");
                    }
                    iv_a43.setImageBitmap(BitmapFactory.decodeResource(getResources(), on));
                    sizeType = 1;
                    paperImg.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.off));
                    paperType = PrintUtil.SETTING_COMMON;
                }
                priceFlag = true;
                break;
            case R.id.iv_print_type://单双面
                if (!isDoublePrint()) {
                    return;
                }
                if (doubleFlag == 1) {
                    iv_print_type.setImageBitmap(BitmapFactory.decodeResource(getResources(), on));
                    doubleFlag = 0;
                } else {
                    iv_print_type.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.off));
                    doubleFlag = 1;
                }
                priceFlag = true;
                break;
            case R.id.iv_copy_cz://支付方式
                if (isPersion) {
                    queryGroup();
                } else {
                    setGroupViewGone();
                }
                break;
            case R.id.ll_cz_qun://群支付
                showGroupDialog();
                break;
            case R.id.ll_cz_persion:
                startActivity(new Intent(CopySettingActivity.this, AccountActivity.class));
                break;
            case R.id.btn_preview://完成
                if (printerPrice != null) {
                    complete();
                } else {
                    ShowUtil.showToast(getString(R.string.price_error));
                }
                break;
        }
        updateSetting();
        log();
        if (priceFlag && !isPersion) {
            queryGroup();
        }
    }

    private void showGroupDialog() {
        final Dialog dialog = new Dialog(this, R.style.GroupDialogStyle);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.main_menu_animstyle);
        window.setGravity(Gravity.BOTTOM);
        View view = View.inflate(this, R.layout.dialog_pay_qun1, null);
        window.setContentView(view);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();

        RecyclerView mRcList = (RecyclerView) view.findViewById(R.id.groupRecView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(CopySettingActivity.this);
        mRcList.setLayoutManager(mLayoutManager);
        mRcList.setHasFixedSize(true);
        mRcList.setItemAnimator(new DefaultItemAnimator());
        GroupRecylerAdapter mAdapter = new GroupRecylerAdapter(groupList);
        mRcList.setAdapter(mAdapter);
        mRcList.addItemDecoration(new RecyclerViewDivider(CopySettingActivity.this, LinearLayoutManager.VERTICAL, 1,
                ContextCompat.getColor(CopySettingActivity.this, R.color.devide_gray)));
        mAdapter.setOnItemClickListener(new GroupRecylerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                dialog.dismiss();
                setGroupViewVisible(groupList.get(position));
            }
        });

        TextView cancelTv = (TextView) view.findViewById(R.id.cancelTv);
        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
    }

    private void showPayType() {
        dismissLoading();
        DialogUtils.showPayChooseDialog(ctx, PriceUtil.getPrice(newSetting, printerPrice) + "", new DialogUtils
                .PayChooseDialogCallBack() {
            @Override
            public void wechat() {
                Logger.i("微信支付");
                Go2PayRequest.go2PWeChat(getSelfActivity(), orderId + "", "PT", new WeChatCallBack() {
                    @Override
                    public void success(WeChatPayBean bean) {
                        wechatPay(bean);
                    }

                    @Override
                    public void fail(String msg) {

                    }

                    @Override
                    public void connectFail() {

                    }
                });
            }

            @Override
            public void alipay() {
                Logger.i("支付宝支付");
                Go2PayRequest.go2Pay(getSelfActivity(), orderId + "", "PT", new Go2PayCallBack() {

                    @Override
                    public void success(String msg, String id) {
                        Logger.i(id);
                        aliPay(id);
                    }

                    @Override
                    public void fail(String msg) {
                        ShowUtil.showToast(msg);
                    }

                    @Override
                    public void connectFail() {
                        ShowUtil.showToast("connectFail ");
                    }
                });
            }
        });
    }

    private void wechatPay(WeChatPayBean bean) {
        SharedPreferencesUtils.putShareValue(CopySettingActivity.this, "wechatFlag", true);
        PayUtil.getInstance(getSelfActivity()).weChatPay(bean);
    }

    private void aliPay(String payInfo) {
        PayUtil.getInstance(getSelfActivity()).setCallBack(new PayUtil.PayCallBack() {
            @Override
            public void paySuccess() {
                showLoading();
                print();
            }

            @Override
            public void payFailed() {
            }
        });
        PayUtil.getInstance(getSelfActivity()).alipay(payInfo);
    }

    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)
    public void onMessageEventPostThread(Integer i) {
        this.pageCount = i;
       // Logger.i(pageCount);
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onMessageEvent(WechatPayEvent wechatPayEvent) {
        //Logger.i(wechatPayEvent.toString());
        SharedPreferencesUtils.putShareValue(CopySettingActivity.this, "wechatFlag", false);
        if (wechatPayEvent.isResult()) {
            print();
        } else {
            ShowUtil.showToast("支付失败");
        }
    }

    public static final String PRINT_SETTING = "setting";
    public static final String PRINT_TYPE = "print_type";
    public static final String PRINTER_NO = "pinter_no";
    public static final String COPY_FILE_FLAG = "copy_file_flag";
    public static final String PREVIEW_PATH = "previewPath";
    public static final String FILE_INFO = "fileInfo";

    public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent(context, CopySettingActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    //:{"colourFlag":0,"directionFlag":1,"doubleFlag":1,"fileName":"XP0F78FIBWS9YITGFXOEKDC.png","filePage":1,
    // "fileType":"0"
    //fileUrl":"http://139.196.224.235:12003/file/df_/g1/M00/00/66/Ci-4nVkUBfaAQNrmAAADstAedTk532.png","id":4284,
    // "memberId":"100000309","printCount":1,"sizeType":0,"source":"1","status":0
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

    protected void showLoading() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);
        }
        if (!loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }

    protected void dismissLoading() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    protected boolean isLoading() {
        if (loadingDialog == null || !loadingDialog.isShowing()) {
            return false;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().removeAllStickyEvents();
        EventBus.getDefault().unregister(CopySettingActivity.this);
    }
}
