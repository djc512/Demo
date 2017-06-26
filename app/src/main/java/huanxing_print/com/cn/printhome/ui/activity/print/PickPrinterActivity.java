package huanxing_print.com.cn.printhome.ui.activity.print;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.event.print.FinishEvent;
import huanxing_print.com.cn.printhome.log.Logger;
import huanxing_print.com.cn.printhome.model.print.AddFileSettingBean;
import huanxing_print.com.cn.printhome.model.print.IsOnlineResp;
import huanxing_print.com.cn.printhome.model.print.PrintFileInfo;
import huanxing_print.com.cn.printhome.model.print.PrintInfoResp;
import huanxing_print.com.cn.printhome.model.print.PrintSetting;
import huanxing_print.com.cn.printhome.model.print.UploadFileBean;
import huanxing_print.com.cn.printhome.net.request.print.HttpListener;
import huanxing_print.com.cn.printhome.net.request.print.PrintRequest;
import huanxing_print.com.cn.printhome.ui.activity.copy.CopySettingActivity;
import huanxing_print.com.cn.printhome.ui.activity.print.fragment.PickPrinterFragment;
import huanxing_print.com.cn.printhome.ui.activity.print.fragment.PrinterDetailFragment;
import huanxing_print.com.cn.printhome.util.FileType;
import huanxing_print.com.cn.printhome.util.FileUtils;
import huanxing_print.com.cn.printhome.util.GsonUtil;
import huanxing_print.com.cn.printhome.util.PrintUtil;
import huanxing_print.com.cn.printhome.util.ShowUtil;
import huanxing_print.com.cn.printhome.util.StepViewUtil;
import huanxing_print.com.cn.printhome.view.StepLineView;
import huanxing_print.com.cn.printhome.view.dialog.Alert;
import pub.devrel.easypermissions.EasyPermissions;

public class PickPrinterActivity extends BasePrintActivity implements EasyPermissions.PermissionCallbacks {

    private PickPrinterFragment pickPrinterFragment;
    private PrinterDetailFragment printerDetailFragment;
    private PrintInfoResp.PrinterPrice printerPrice;
    private static final int REQUEST_CODE = 1;
    private static final int REQUEST_IMG = 1;
    private PrintSetting printSetting;
    private String imagePath;
    private File file;
    private int printType;
    private boolean isFileCopy = false;
    private PrintFileInfo printFileInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(context);
        initData();
        initView();
        isPermissionsGranted();
    }

    @Override
    protected void setContent() {
        setContentView(R.layout.activity_pick_printer);
    }

    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)
    public void onMessageEventPostThread(PrintInfoResp.PrinterPrice printerPrice) {
        Logger.i(printerPrice.toString());
        this.printerPrice = printerPrice;
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onMessageEventPostThread(FinishEvent event) {
        if (event != null && event.isFinishFlag()) {
            finish();
        }
    }

    private void initData() {
        printSetting = getIntent().getExtras().getParcelable(SETTING);
        if (printSetting == null) {
            initStepView();
            printType = getIntent().getIntExtra("print_type", 0);
            imagePath = getIntent().getStringExtra(IMAGE_PATH);
            isFileCopy = getIntent().getBooleanExtra(COPY_FLAG, false);
            Logger.i(imagePath);
            Logger.i(isFileCopy);
            if (imagePath == null) {
                ShowUtil.showToast(getString(R.string.file_error));
                finish();
            }
            printFileInfo = new PrintFileInfo(PrintFileInfo.TYPE_COPY, 1);
            turnFile();
        } else {
            initStepLine();
            printFileInfo = getIntent().getExtras().getParcelable(FILE_INFO);
            imagePath = getIntent().getStringExtra(IMAGE_PATH);
            printType = PrintUtil.PRINT_TYPE_PRINT;
            isFileCopy = false;
            Logger.i(printSetting.toString());
            Logger.i(printFileInfo.toString());
            initFragment();
        }
    }

    private void initStepView() {
        StepViewUtil.init(context, findViewById(R.id.step), StepLineView.STEP_PICK_FILE);
        TextView pickFileTv = (TextView) findViewById(R.id.pickFileTv);
        pickFileTv.setText("采集制作");
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("选打印机");
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("选打印机");
    }

    static class MyHandler extends Handler {
        WeakReference<PickPrinterActivity> mActivity;

        MyHandler(PickPrinterActivity activity) {
            mActivity = new WeakReference<PickPrinterActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            PickPrinterActivity activity = mActivity.get();
            String base = (String) msg.obj;
            if (base != null) {
                activity.uploadFile(base);
            }
        }
    }

    private MyHandler mHandler = new MyHandler(this);

    private void uploadFile(String base) {
        PrintRequest.uploadFile(activity, FileType.getType(file.getPath()), base, file.getName(), new
                HttpListener() {
                    @Override
                    public void onSucceed(String content) {

                        UploadFileBean uploadFileBean = GsonUtil.GsonToBean(content, UploadFileBean.class);
                        if (uploadFileBean == null) {
                            return;
                        }
                        if (uploadFileBean.isSuccess()) {
                            if (isLoading()) {
                                String url = uploadFileBean.getData().getImgUrl();
                                addFile(url);
                            }
                        } else {
                            ShowUtil.showToast(getString(R.string.upload_failure));
                        }
                    }

                    @Override
                    public void onFailed(String exception) {
                        dismissLoading();
                        ShowUtil.showToast(getString(R.string.net_error));
                    }
                }, false);
    }

    private void turnFile() {
        file = new File(imagePath);
        if (file == null || !file.exists()) {
            ShowUtil.showToast(getString(R.string.file_error));
            finish();
        }
        if (FileUtils.isOutOfSize(file)) {
            Alert.show(context, "提示", getString(R.string.size_out), null, new
                    DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
            return;
        }
        showLoading();
        new Thread() {
            @Override
            public void run() {
                super.run();
                Message msg = new Message();
                msg.obj = FileUtils.getBase64(file);
                mHandler.sendMessage(msg);
            }
        }.start();
    }

    private void addFile(String fileUrl) {
        PrintRequest.addFile(activity, file.getName(), fileUrl, PrintUtil.TYPE_COPY, new HttpListener() {
            @Override
            public void onSucceed(String content) {
                dismissLoading();
                AddFileSettingBean addFileSettingBean = GsonUtil.GsonToBean(content, AddFileSettingBean.class);
                if (addFileSettingBean == null) {
                    return;
                }
                if (addFileSettingBean.isSuccess()) {
                    printSetting = addFileSettingBean.getData();
                    Logger.i(printSetting.toString());
                    initFragment();
                } else {
                    ShowUtil.showToast(addFileSettingBean.getErrorMsg());
                }
            }

            @Override
            public void onFailed(String exception) {
                dismissLoading();
                ShowUtil.showToast(getString(R.string.net_error));
            }
        }, false);
    }


    private void initView() {
        initTitleBar("选打印机");
    }

    private void initStepLine() {
        StepViewUtil.init(context, findViewById(R.id.step), StepLineView.STEP_PICK_PRINTER);
    }

    private void initFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        printerDetailFragment = new PrinterDetailFragment();
        pickPrinterFragment = new PickPrinterFragment();
        fragmentTransaction.add(R.id.content, pickPrinterFragment);
        fragmentTransaction.add(R.id.content, printerDetailFragment);
        fragmentTransaction.commit();
        if (printerPrice != null) {
            showScanPrinterDetail(printerPrice);
        } else {
            showPick();
        }
    }

    private void showScanPrinterDetail(PrintInfoResp.PrinterPrice printPrinterPrice) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(pickPrinterFragment)
                .show(printerDetailFragment)
                .commit();
        printerDetailFragment.setPrintPrinterPrice(printPrinterPrice);
    }

    private void showPrinterDetail(PrintInfoResp.PrinterPrice printPrinterPrice) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(pickPrinterFragment)
                .show(printerDetailFragment)
                .commit();
        printerDetailFragment.updateView(printPrinterPrice);
    }

    public void showPick() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(printerDetailFragment)
                .show(pickPrinterFragment)
                .commit();
    }

    public void requeryIsOnline(final String printerNo) {
        PrintRequest.queryIsOnline(activity, printerNo, new HttpListener() {
            @Override
            public void onSucceed(String content) {
                IsOnlineResp isOnlineResp = GsonUtil.GsonToBean(content, IsOnlineResp.class);
                if (isOnlineResp != null) {
                    if (isOnlineResp.isSuccess()) {
                        if (isOnlineResp.getData() == true) {
                            requeryPrice(printerNo);
                        } else {
                            ShowUtil.showToast(getString(R.string.printer_error));
                        }
                    } else {
                        ShowUtil.showToast(getString(R.string.printer_error));
                    }
                }
            }

            @Override
            public void onFailed(String exception) {
                ShowUtil.showToast(getString(R.string.net_error));
            }
        });
    }

    public void requeryPrice(String printerNo) {
        showLoading();
        PrintRequest.queryPrinterPrice(activity, printerNo, new HttpListener() {
            @Override
            public void onSucceed(String content) {
                PrintInfoResp printInfoResp = GsonUtil.GsonToBean(content, PrintInfoResp.class);
                if (printInfoResp != null && printInfoResp.isSuccess()) {
                    PrintInfoResp.PrinterPrice printPrinterPrice = printInfoResp.getData();
                    if (printPrinterPrice != null && isLoading()) {
                        showPrinterDetail(printPrinterPrice);
                    }
                }
                if (printInfoResp != null && !printInfoResp.isSuccess()) {
                    ShowUtil.showToast(printInfoResp.getErrorMsg());
                }
                dismissLoading();
            }

            @Override
            public void onFailed(String exception) {
                ShowUtil.showToast(getString(R.string.net_error));
                dismissLoading();
            }
        });
        Logger.i(printerNo);
    }

    public void turnSetting(String printerNo) {
        Bundle bundle = new Bundle();
        bundle.putString(CopySettingActivity.PRINTER_NO, printerNo);
        bundle.putInt(CopySettingActivity.PRINT_TYPE, printType);
        bundle.putParcelable(CopySettingActivity.PRINT_SETTING, printSetting);
        bundle.putBoolean(CopySettingActivity.COPY_FILE_FLAG, isFileCopy);
        bundle.putString(CopySettingActivity.PREVIEW_PATH, imagePath);
        bundle.putParcelable(CopySettingActivity.FILE_INFO, printFileInfo);
        CopySettingActivity.start(context, bundle);
    }

    public static final String SETTING = "setting";
    public static final String IMAGE_PATH = "imagepath";
    public static final String COPY_FLAG = "copyfile";
    public static final String FILE_INFO = "fileInfo";

    public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent(context, PickPrinterActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Logger.i("onBackPressed");
        finish();
    }

    private boolean isPermissionsGranted() {
        boolean isPermission;
        if (EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA, Manifest.permission
                .ACCESS_COARSE_LOCATION)) {
            isPermission = true;
        } else {
            EasyPermissions.requestPermissions(this, "需要请求权限", REQUEST_CODE, Manifest.permission.CAMERA, Manifest
                    .permission.ACCESS_COARSE_LOCATION);
            isPermission = false;
        }
        return isPermission;
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().removeAllStickyEvents();
        EventBus.getDefault().unregister(context);
    }
}
