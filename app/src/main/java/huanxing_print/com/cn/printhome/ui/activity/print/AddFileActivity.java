package huanxing_print.com.cn.printhome.ui.activity.print;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.log.Logger;
import huanxing_print.com.cn.printhome.model.print.PrintListBean;
import huanxing_print.com.cn.printhome.model.print.PrinterPriceBean;
import huanxing_print.com.cn.printhome.net.request.print.HttpListener;
import huanxing_print.com.cn.printhome.net.request.print.PrintRequest;
import huanxing_print.com.cn.printhome.ui.activity.print.fragment.AllFileFragment;
import huanxing_print.com.cn.printhome.ui.activity.print.fragment.PcFileFragment;
import huanxing_print.com.cn.printhome.ui.activity.print.fragment.PhotoFragment;
import huanxing_print.com.cn.printhome.ui.activity.print.fragment.QQFileFragment;
import huanxing_print.com.cn.printhome.ui.activity.print.fragment.WechatFileFragment;
import huanxing_print.com.cn.printhome.ui.activity.print.fragment.WifiImportFragment;
import huanxing_print.com.cn.printhome.ui.adapter.FinderFragmentAdapter;
import huanxing_print.com.cn.printhome.util.FileUtils;
import huanxing_print.com.cn.printhome.util.GsonUtil;
import huanxing_print.com.cn.printhome.util.ShowUtil;
import huanxing_print.com.cn.printhome.util.StepViewUtil;
import huanxing_print.com.cn.printhome.view.StepLineView;
import huanxing_print.com.cn.printhome.view.dialog.WaitDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static huanxing_print.com.cn.printhome.constant.ConFig.IMG_CACHE_PATH;
import static huanxing_print.com.cn.printhome.ui.activity.print.ImgPreviewActivity.KEY_IMG_URI;

;

public class AddFileActivity extends BasePrintActivity implements EasyPermissions.PermissionCallbacks, View
        .OnClickListener {

    private TextView titleTv;
    private Button imageBtn;
    private Button qqBtn;
    private Button wechatBtn;
    private Button pcBtn;
    private ViewPager viewpager;
    private TabLayout tabs;
    private AllFileFragment allFileFragment;
    private static final int REQUEST_CODE = 1;

    private static final int REQUEST_IMG = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickfile);
        Logger.i(IMG_CACHE_PATH);
//        setContentView(R.layout.activity_add_file);
        initStepLine();
        initView1();
//        initView();
    }

    private void initView1() {
        initTitleBar("选取文件");
        findViewById(R.id.titleTv).setOnClickListener(this);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        tabs = (TabLayout) findViewById(R.id.tabs);
        List<String> titles = new ArrayList<>();
        titles.add("全部文件");
        titles.add("手机相册");
        titles.add("微信");
        titles.add("QQ");
        titles.add("WIFI导入");
        titles.add("电脑上传");
        for (int i = 0; i < titles.size(); i++) {
            tabs.addTab(tabs.newTab().setText(titles.get(i)));
        }
        List<Fragment> fragments = new ArrayList<>();
        allFileFragment = new AllFileFragment();
        fragments.add(allFileFragment);
        fragments.add(new PhotoFragment());
        fragments.add(new WechatFileFragment());
        fragments.add(new QQFileFragment());
        fragments.add(new WifiImportFragment());
        fragments.add(new PcFileFragment());
        FinderFragmentAdapter mFragmentAdapteradapter = new FinderFragmentAdapter(getSupportFragmentManager(),
                fragments, titles);
        viewpager.setAdapter(mFragmentAdapteradapter);
        tabs.setupWithViewPager(viewpager);
    }

    private void initStepLine() {
        StepViewUtil.init(context, findViewById(R.id.step), StepLineView.STEP_PICK_FILE);
    }

    @Override
    public void onBackPressed() {
        if (viewpager.getCurrentItem() == 0) {
            if (allFileFragment != null && allFileFragment.isVisible()) {
                if (!allFileFragment.onBackPressed()) {
                    super.onBackPressed();
                }
                return;
            }
        }
        super.onBackPressed();
    }

    private boolean isPermissionsGranted() {
        boolean isPermission;
        if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            isPermission = true;
        } else {
            EasyPermissions.requestPermissions(this, "需要请求权限", REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
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


    private void initView() {
        imageBtn = (Button) findViewById(R.id.imageBtn);
        qqBtn = (Button) findViewById(R.id.qqBtn);
        wechatBtn = (Button) findViewById(R.id.wechatBtn);
        pcBtn = (Button) findViewById(R.id.pcBtn);

        imageBtn.setOnClickListener(this);
        qqBtn.setOnClickListener(this);
        wechatBtn.setOnClickListener(this);
        pcBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.imageBtn:
                if (isPermissionsGranted()) {
                    getImage();
                }
                break;
            case R.id.qqBtn:
                if (isPermissionsGranted()) {
                    getFileList(PATH_QQ_FILE, SOURCE_QQ);
                }
                break;
            case R.id.wechatBtn:
                if (isPermissionsGranted()) {
                    getFileList(PATH_WECHAT_FILE, SOURCE_WECHAT);
                }
                break;
            case R.id.titleTv:
                break;
            case R.id.pcBtn:
                break;
        }
    }

    public void onPreviewBtn(View view) {
        turnImgPreview(uri);
    }

    public void onGetPrintList(View view) {
        PrintRequest.queryPrintList(activity, 1, 100, new HttpListener() {
            @Override
            public void onSucceed(String content) {
                PrintListBean printListBean = GsonUtil.GsonToBean(content, PrintListBean.class);
                if (printListBean == null) {
                    return;
                }
                if (printListBean.isSuccess()) {
                    ShowUtil.showToast(printListBean.getData().size() + "");
                } else {
                    ShowUtil.showToast(getString(R.string.upload_failure));
                }
            }

            @Override
            public void onFailed(String exception) {
                ShowUtil.showToast(getString(R.string.net_error));
            }
        });
    }

    public void onGetPrinters(View view) {
        startActivity(new Intent(context, RecentPrintersActivity.class));
    }

    public void onGetPrinterPrice(View view) {
        PrintRequest.queryPrinterPrice(activity, "48TZ-13102-1251581193", new HttpListener() {
            @Override
            public void onSucceed(String content) {
                PrinterPriceBean printerPriceBean = GsonUtil.GsonToBean(content, PrinterPriceBean.class);
                if (printerPriceBean == null) {
                    return;
                }
                if (printerPriceBean.isSuccess()) {

                }
            }

            @Override
            public void onFailed(String exception) {
                ShowUtil.showToast(getString(R.string.net_error));
            }
        });
    }

    public void onShow(View view) {
        WaitDialog.showDialog(activity, null, null);
    }

    public void onDismiss(View view) {
        WaitDialog.dismissDialog();
    }


    private void getImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    private static final String PATH_QQ_FILE = Environment.getExternalStorageDirectory().getPath() +
            "/tencent/QQfile_recv/";
    private static final String PATH_WECHAT_FILE = Environment.getExternalStorageDirectory().getPath() +
            "/tencent/MicroMsg/Download/";
    public static final String KEY_FILE = "file";
    public static final String KEY_SOURCE = "source";
    public static final int SOURCE_QQ = 1;
    public static final int SOURCE_WECHAT = 2;

    private void getFileList(String path, int source) {
        List<File> fileList = FileUtils.getFileList(path);
        if (fileList == null) {
            ShowUtil.showToast("file error");
        } else {
            Intent intent = new Intent(AddFileActivity.this, FileListActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(KEY_FILE, (Serializable) fileList);
            bundle.putInt(KEY_SOURCE, source);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    private void turnImgPreview(Uri uri) {
        Intent intent = new Intent(context, ImgPreviewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_IMG_URI, uri);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private Uri uri;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMG:
                    Logger.i(data.toString());
                    uri = data.getData();
                    turnImgPreview(data.getData());
                    break;
            }
        }
    }


}
