package huanxing_print.com.cn.printhome.ui.activity.print;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.event.print.FinishEvent;
import huanxing_print.com.cn.printhome.event.print.PrintTypeEvent;
import huanxing_print.com.cn.printhome.log.Logger;
import huanxing_print.com.cn.printhome.model.print.DocPreviewResp;
import huanxing_print.com.cn.printhome.model.print.PrintListBean;
import huanxing_print.com.cn.printhome.model.print.UploadFileBean;
import huanxing_print.com.cn.printhome.net.request.print.DownloadListener;
import huanxing_print.com.cn.printhome.net.request.print.Http;
import huanxing_print.com.cn.printhome.net.request.print.HttpListener;
import huanxing_print.com.cn.printhome.net.request.print.PrintRequest;
import huanxing_print.com.cn.printhome.ui.activity.print.fragment.AllFileFragment;
import huanxing_print.com.cn.printhome.ui.activity.print.fragment.PcFileFragment;
import huanxing_print.com.cn.printhome.ui.activity.print.fragment.PhotoFragment;
import huanxing_print.com.cn.printhome.ui.activity.print.fragment.QQFileFragment;
import huanxing_print.com.cn.printhome.ui.activity.print.fragment.WechatFileFragment;
import huanxing_print.com.cn.printhome.ui.activity.print.fragment.WifiImportFragment;
import huanxing_print.com.cn.printhome.ui.adapter.FinderFragmentAdapter;
import huanxing_print.com.cn.printhome.util.FileType;
import huanxing_print.com.cn.printhome.util.FileUtils;
import huanxing_print.com.cn.printhome.util.GsonUtil;
import huanxing_print.com.cn.printhome.util.ShowUtil;
import huanxing_print.com.cn.printhome.util.StatusBarCompat;
import huanxing_print.com.cn.printhome.util.StepViewUtil;
import huanxing_print.com.cn.printhome.view.StepLineView;
import huanxing_print.com.cn.printhome.view.dialog.Alert;

import static huanxing_print.com.cn.printhome.constant.ConFig.IMG_CACHE_PATH;

public class AddFileActivity extends BasePrintActivity implements View.OnClickListener {

    public static final int TYPE_PRINT = 1;
    public static final int TYPE_CHAT = 2;
    private int pickType;

    private ViewPager viewpager;
    private TabLayout tabLayout;
    private AllFileFragment allFileFragment;
    private int index = 0;

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

//        setStatusBarColor();
        EventBus.getDefault().register(context);
        Logger.i(IMG_CACHE_PATH);
        initData();
        initView();
    }

    @Override
    protected void setContent() {
        setContentView(R.layout.activity_pickfile);
    }

    private void initData() {
        EventBus.getDefault().postSticky(new PrintTypeEvent(PrintTypeEvent.TYPE_PRINT));
        Bundle bundle = getIntent().getExtras();
        index = bundle.getInt(INDEX);
        pickType = bundle.getInt(PICK_TYPE);
        Logger.i(index);
        Logger.i(pickType);
    }

    public static final String INDEX = "index";
    public static final String PICK_TYPE = "pickType";

    public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent(context, AddFileActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public int getPickType() {
        return pickType;
    }

    public void pickFile(String path) {
        if (pickType == TYPE_CHAT) {
            setResult(path);
            return;
        }
        if (!FileType.isPrintType(path)) {
            ShowUtil.showToast("文件不可打印");
        } else {
            Bundle bundle = new Bundle();
            if (FileType.getPrintType(path) == FileType.TYPE_IMG) {
                bundle.putCharSequence(ImgPreviewActivity.KEY_IMG_URI, path);
                ImgPreviewActivity.start(context, bundle);
            } else {
                turnFile(new File(path));
            }
        }
    }

    public void pickFile(File file) {
        if (pickType == TYPE_CHAT) {
            setResult(file.getPath());
            return;
        }
        if (!FileType.isPrintType(file.getPath())) {
            ShowUtil.showToast("文件不可打印");
        } else {
            Bundle bundle = new Bundle();
            if (FileType.getPrintType(file.getPath()) == FileType.TYPE_IMG) {
                bundle.putCharSequence(ImgPreviewActivity.KEY_IMG_URI, file.getPath());
                ImgPreviewActivity.start(context, bundle);
            } else {
                turnFile(file);
            }
        }
    }

    private void initView() {
        initTitleBar("选取文件");
        initStepLine();
        if (pickType == TYPE_CHAT) {
            findViewById(R.id.step).setVisibility(View.GONE);
        }
        findViewById(R.id.titleTv).setOnClickListener(this);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        List<String> titleList = new ArrayList<>();
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

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onMessageEventPostThread(FinishEvent event) {
        if (event != null && event.isFinishFlag()) {
            finish();
        }
    }

    private void setTabIndex(int index) {
        viewpager.setCurrentItem(index);
        tabLayout.getTabAt(index).select();
    }

    public View getTabView(int position) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_pickfile_tab, null);
        TextView txt_title = (TextView) view.findViewById(R.id.titleTv);
        txt_title.setText(titles[position]);
        ImageView img_title = (ImageView) view.findViewById(R.id.img);
        img_title.setImageResource(tabIcons[position]);
        return view;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.titleTv:
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

    public void setResult(String path) {
        Logger.i(path);
        Intent intent = new Intent();
        intent.putExtra("path", path);
        setResult(RESULT_OK, intent);
        finish();
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

    private void uploadFile(String base) {
        PrintRequest.uploadFile(activity, FileType.getType(file.getPath()), base, file.getName(), new
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
                dismissLoading();
                DocPreviewResp docPreviewResp = GsonUtil.GsonToBean(content, DocPreviewResp.class);
                if (docPreviewResp == null) {
                    dismissLoading();
                    return;
                }
                if (!docPreviewResp.isSuccess()) {
                    ShowUtil.showToast(docPreviewResp.getErrorMsg());
                    return;
                }
                if (docPreviewResp.getData().getPaperNum() > 200) {
                    ShowUtil.showToast(getString(R.string.file_outpage));
                    return;
                }
                turnPreview(docPreviewResp.getData().getArryList(), url);
            }

            @Override
            public void onFailed(String exception) {
                dismissLoading();
                ShowUtil.showToast(getString(R.string.net_error));
            }
        });
    }

    public void downloadFile(final PrintListBean.FileInfo fileInfo) {
        showLoading();
        Http.download(FileUtils.getDownloadPath() + fileInfo.getFileName(), fileInfo.getFileUrl(), new
                DownloadListener() {
                    @Override
                    public void onSucceed(String content) {
                        if (isLoading()) {
                            setResult(FileUtils.getDownloadPath() + fileInfo.getFileName());
                        }
                        dismissLoading();
                    }

                    @Override
                    public void onFailed(String exception) {
                        dismissLoading();
                        ShowUtil.showToast("文件下载失败");
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(context);
    }
}
