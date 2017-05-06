package huanxing_print.com.cn.printhome.ui.activity.main;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.ActivityHelper;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.base.BaseFragment;
import huanxing_print.com.cn.printhome.ui.activity.fragment.ChatFragment;
import huanxing_print.com.cn.printhome.ui.activity.fragment.ContantsFragment;
import huanxing_print.com.cn.printhome.ui.activity.fragment.MyFragment;
import huanxing_print.com.cn.printhome.ui.activity.fragment.PrintFragment;
import huanxing_print.com.cn.printhome.ui.activity.fragment.fragapproval.ApplyFragment;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 主界面
 *
 * @author Administrator
 */
public class MainActivity extends BaseActivity implements OnCheckedChangeListener, EasyPermissions.PermissionCallbacks {
    private Context mContext;
    private LinearLayout ll_bg;
    private RadioGroup rgp;

    private PrintFragment fragPrint;

    private MyFragment fragMy;

    private ContantsFragment fragContants;

    private ChatFragment fragChat;

    private ApplyFragment fragApply;

    private BaseFragment fragTemp;

    private FragmentManager fragMananger;

    private NotificationManager manager;

    private long exitTime;

    private String url;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 改变状态栏的颜色使其与APP风格一体化
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.activity_main);
        mContext = MainActivity.this;

        init();
        initPermission();
    }

    private void init() {
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        ll_bg = (LinearLayout) findViewById(R.id.ll_bg);
        rgp = (RadioGroup) findViewById(R.id.rgp_option);
        fragMananger = getFragmentManager();

        fragPrint = new PrintFragment();
        fragMy = new MyFragment();
        fragContants = new ContantsFragment();
        fragChat = new ChatFragment();
        fragApply = new ApplyFragment();

        ll_bg.setBackgroundResource(R.color.yellow);
        rgp.setOnCheckedChangeListener(this);
        onCheckedChanged(rgp, R.id.rgb_print);

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        FragmentTransaction tran = fragMananger.beginTransaction();
        if (fragTemp != null && !fragTemp.isHidden()) {
            tran.hide(fragTemp);
        }
        switch (checkedId) {
            case R.id.rgb_chat:
                ll_bg.setBackgroundResource(R.color.gray5);
                fragTemp = fragChat;
                break;
            case R.id.rgb_apply:
                ll_bg.setBackgroundResource(R.color.gray5);
                fragTemp = fragApply;
                break;
            case R.id.rgb_print:
                ll_bg.setBackgroundResource(R.color.yellow);
                fragTemp = fragPrint;
                break;
            case R.id.rgb_contacts:
                ll_bg.setBackgroundResource(R.color.transparent_full);
                fragTemp = fragContants;
                break;
            case R.id.rgb_my:
                ll_bg.setBackgroundResource(R.color.gray5);
                fragTemp = fragMy;
                break;


        }
        if (fragTemp.isAdded()) {
            tran.show(fragTemp);
        } else {
            tran.add(R.id.fl_main_context, fragTemp);
        }
        tran.commitAllowingStateLoss();
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        fragPrint.onActivityResult(arg0, arg1, arg2);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                ActivityHelper.getInstance().finishAllActivity();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initPermission() {
        if (!isPermissionsGranted()) {
            getPermissions();
        }
    }

    final String[] permissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION};

    private boolean isPermissionsGranted() {
        boolean isPermission;
        if (EasyPermissions.hasPermissions(this, permissions)) {
            isPermission = true;
        } else {
            isPermission = false;
        }
        return isPermission;
    }

    private void getPermissions() {
        EasyPermissions.requestPermissions(this, "请允许必要的权限", 1, permissions);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
