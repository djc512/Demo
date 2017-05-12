package huanxing_print.com.cn.printhome.ui.activity.print;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.log.Logger;
import huanxing_print.com.cn.printhome.model.print.DocPreviewResp;
import huanxing_print.com.cn.printhome.model.print.PrintListBean;
import huanxing_print.com.cn.printhome.model.print.UploadFileBean;
import huanxing_print.com.cn.printhome.net.request.print.HttpListener;
import huanxing_print.com.cn.printhome.net.request.print.PrintRequest;
import huanxing_print.com.cn.printhome.ui.activity.print.fragment.AllFileFragment;
import huanxing_print.com.cn.printhome.ui.activity.print.fragment.PcFileFragment;
import huanxing_print.com.cn.printhome.ui.activity.print.fragment.PhotoFragment;
import huanxing_print.com.cn.printhome.ui.activity.print.fragment.QQFileFragment;
import huanxing_print.com.cn.printhome.ui.activity.print.fragment.WechatFileFragment;
import huanxing_print.com.cn.printhome.ui.activity.print.fragment.WifiImportFragment;
import huanxing_print.com.cn.printhome.ui.adapter.FinderFragmentAdapter;
import huanxing_print.com.cn.printhome.util.AlertUtil;
import huanxing_print.com.cn.printhome.util.FileType;
import huanxing_print.com.cn.printhome.util.FileUtils;
import huanxing_print.com.cn.printhome.util.GsonUtil;
import huanxing_print.com.cn.printhome.util.ShowUtil;
import huanxing_print.com.cn.printhome.util.StepViewUtil;
import huanxing_print.com.cn.printhome.view.StepLineView;
import huanxing_print.com.cn.printhome.view.dialog.WaitDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static huanxing_print.com.cn.printhome.constant.ConFig.IMG_CACHE_PATH;
import static huanxing_print.com.cn.printhome.ui.activity.print.ImgPreviewActivity.KEY_IMG_URI;

public class AddFileActivity extends BasePrintActivity implements EasyPermissions.PermissionCallbacks, View
        .OnClickListener {

    private TextView titleTv;
    private Button imageBtn;
    private Button qqBtn;
    private Button wechatBtn;
    private Button pcBtn;
    private ViewPager viewpager;
    private TabLayout tabLayout;
    private AllFileFragment allFileFragment;
    private int index = 0;

    private static final int REQUEST_CODE = 1;
    private static final int REQUEST_IMG = 1;

    private final String[] titles = {"全部文件", "微信", "QQ", "手机相册", "电脑上传", "WIFI导入"};
    private final int[] tabIcons = {
            R.drawable.ic_tab_file,
            R.drawable.ic_tab_wechat,
            R.drawable.ic_tab_qq,
            R.drawable.ic_tab_photo,
            R.drawable.ic_tab_pc,
            R.drawable.ic_tab_wifi,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickfile);
        Logger.i(IMG_CACHE_PATH);
//        setContentView(R.layout.activity_add_file);
        initStepLine();
        initData();
        initView1();
//        initView();
    }

    private void initData() {
        index = getIntent().getExtras().getInt(INDEX);
    }

    public static final String INDEX = "index";

    public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent(context, AddFileActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    private void initView1() {
        initTitleBar("选取文件");
        findViewById(R.id.titleTv).setOnClickListener(this);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        List<String> titleList = new ArrayList<>();
//        for (int i = 0; i < titles.length; i++) {
//            titleList.add(titles[i]);
//        }
        for (String title : titles) {
            titleList.add(title);
        }
        List<Fragment> fragments = new ArrayList<>();
        allFileFragment = new AllFileFragment();
        fragments.add(allFileFragment);
        fragments.add(new WechatFileFragment());
        fragments.add(new QQFileFragment());
        fragments.add(new PhotoFragment());
        fragments.add(new PcFileFragment());
        fragments.add(new WifiImportFragment());
        FinderFragmentAdapter mFragmentAdapteradapter = new FinderFragmentAdapter(getSupportFragmentManager(),
                fragments, titleList);
        viewpager.setAdapter(mFragmentAdapteradapter);
        tabLayout.setupWithViewPager(viewpager);
        for (int i = 0; i < titles.length; i++) {
            tabLayout.getTabAt(i).setCustomView(getTabView(i));
        }
        tabLayout.setTabTextColors(ContextCompat.getColor(context, R.color.text_black), ContextCompat.getColor
                (context, R.color.text_gray));
        setTabIndex(index);
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

    private void setTabIndex(int index) {
        viewpager.setCurrentItem(index);
        tabLayout.getTabAt(index).select();
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

    public View getTabView(int position) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_pickfile_tab, null);
        TextView txt_title = (TextView) view.findViewById(R.id.titleTv);
        txt_title.setText(titles[position]);
        ImageView img_title = (ImageView) view.findViewById(R.id.img);
        img_title.setImageResource(tabIcons[position]);
        return view;
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

    public void turnPreview(ArrayList<String> fileUrlList, String fileUrl) {
        Bundle bundle = new Bundle();
        bundle.putCharSequence(DocPreviewActivity.KEY_URL, fileUrl);
        bundle.putStringArrayList(DocPreviewActivity.KEY_URL_LIST, fileUrlList);
        bundle.putSerializable(DocPreviewActivity.KEY_FILE, file);
        DocPreviewActivity.start(context, bundle);
    }

    static class MyHandler extends Handler {
        WeakReference<AddFileActivity> mActivity;

        MyHandler(AddFileActivity activity) {
            mActivity = new WeakReference<AddFileActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            AddFileActivity activity = mActivity.get();
            String base = (String) msg.obj;
            if (base != null) {
                activity.uploadFile(base);
            }
        }
    }

    private MyHandler mHandler = new MyHandler(this);

    private File file;

    public void turnFile(final File file) {
        this.file = file;
        if (file == null || !file.exists()) {
            return;
        }
        if (FileUtils.isOutOfSize(file)) {
            AlertUtil.show(context, "提示", getString(R.string.size_out), null, new
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

    private void uploadFile(String base) {
        PrintRequest.uploadFile(activity, FileType.getType(file.getPath()), base, file.getName(),  new
                HttpListener() {
                    @Override
                    public void onSucceed(String content) {
                        UploadFileBean uploadFileBean = GsonUtil.GsonToBean(content, UploadFileBean.class);
                        if (uploadFileBean == null) {
                            dismissLoading();
                            return;
                        }
                        if (uploadFileBean.isSuccess()) {
                            if (isLoading()) {
                                String url = uploadFileBean.getData().getImgUrl();
                                getPreview(url);
                                Logger.i(url);
                            }
                        } else {
                            dismissLoading();
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

    private void getPreview(final String url) {
        PrintRequest.docPreview(activity, url, new HttpListener() {
            @Override
            public void onSucceed(String content) {
                DocPreviewResp docPreviewResp = GsonUtil.GsonToBean(content, DocPreviewResp.class);
                if (docPreviewResp == null) {
                    dismissLoading();
                    return;
                }
                if (docPreviewResp.isSuccess()) {
                    turnPreview(docPreviewResp.getData().getArryList(), url);
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
