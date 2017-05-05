package huanxing_print.com.cn.printhome.ui.activity.print;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.WindowManager;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.log.Logger;
import huanxing_print.com.cn.printhome.model.print.Printer;
import huanxing_print.com.cn.printhome.ui.activity.print.fragment.PickPrinterFragment;
import huanxing_print.com.cn.printhome.ui.activity.print.fragment.PrinterDetailFragment;
import huanxing_print.com.cn.printhome.util.StepViewUtil;
import huanxing_print.com.cn.printhome.view.StepLineView;
import pub.devrel.easypermissions.EasyPermissions;

public class PickPrinterActivity extends BasePrintActivity implements EasyPermissions.PermissionCallbacks {

    private PickPrinterFragment pickPrinterFragment;
    private PrinterDetailFragment printerDetailFragment;

    private static final int REQUEST_CODE = 1;
    private static final int REQUEST_IMG = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_printer);
        initStepLine();
        initView();
        initFragment();
        isPermissionsGranted();
        EventBus.getDefault().register(this);
    }

    private void initView() {
        initTitleBar("打印");
    }

    private void initStepLine() {
        StepViewUtil.init(context, findViewById(R.id.step), StepLineView.STEP_PICK_PRINTER);
    }

    private void initFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        WindowManager wm = getWindowManager();
        printerDetailFragment = new PrinterDetailFragment();
        pickPrinterFragment = new PickPrinterFragment();
        fragmentTransaction.add(R.id.content, pickPrinterFragment);
        fragmentTransaction.add(R.id.content, printerDetailFragment);
        fragmentTransaction.hide(printerDetailFragment)
                .show(pickPrinterFragment)
                .commit();
    }

    private void showPrintDetail(Printer printer) {
        printerDetailFragment.updateView();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(pickPrinterFragment)
                .show(printerDetailFragment)
                .commit();
    }

    public static final String TAG_EVENT_SWITCH = "switch_printer";

    @Subscriber(tag = TAG_EVENT_SWITCH)
    private void showPick(Object object) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(printerDetailFragment)
                .show(pickPrinterFragment)
                .commit();
    }

    public static final String TAG_EVENT_PRINTER = "printer";

    @Subscriber(tag = TAG_EVENT_PRINTER)
    private void getPrinter(Printer printer) {
        showPrintDetail(printer);
        Logger.i(printer.toString());
    }

    public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent(context, PickPrinterActivity.class);
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
        EventBus.getDefault().unregister(this);
    }
}
