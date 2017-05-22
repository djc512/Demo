package huanxing_print.com.cn.printhome.ui.activity.contact;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hyphenate.util.PathUtil;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseApplication;
import huanxing_print.com.cn.printhome.log.Logger;
import huanxing_print.com.cn.printhome.model.print.DocPreviewResp;
import huanxing_print.com.cn.printhome.model.print.UploadFileBean;
import huanxing_print.com.cn.printhome.net.request.print.HttpListener;
import huanxing_print.com.cn.printhome.net.request.print.PrintRequest;
import huanxing_print.com.cn.printhome.ui.activity.print.BasePrintActivity;
import huanxing_print.com.cn.printhome.ui.activity.print.DocPreviewActivity;
import huanxing_print.com.cn.printhome.ui.activity.print.ImgPreviewActivity;
import huanxing_print.com.cn.printhome.ui.activity.print.PdfPreviewActivity;
import huanxing_print.com.cn.printhome.ui.adapter.ChatFilesRecylerAdapter;
import huanxing_print.com.cn.printhome.util.FileType;
import huanxing_print.com.cn.printhome.util.FileUtils;
import huanxing_print.com.cn.printhome.util.GsonUtil;
import huanxing_print.com.cn.printhome.util.ShowUtil;

public class ChatFilesActivity extends BasePrintActivity implements View.OnClickListener{

    private String path;
    private RecyclerView mRcList;
    private ChatFilesRecylerAdapter mAdapter;
    private String groupId;
    private String easemobId = BaseApplication.getInstance().getEasemobId();

    ///storage/emulated/0/Android/data/huanxing_print.com.cn
    /// .printhome/1196170420115735#ecostardev/files/100000309/16386101411841/da032000-3c69-11e7-86ed-f796dbacccc3.pdf
    ///storage/emulated/0/Android/data/huanxing_print.com.cn
    /// .printhome/1196170420115735#ecostardev/files/100000309/16555006033921/db3b52d0-3d22-11e7-9aa5-3b108232d526.doc

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_files);
        initData();
        initView();
    }

    private void initData() {
        groupId = getIntent().getExtras().getString(GROUP_ID);
        path = PathUtil.getInstance().getFilePath().toString().replace(easemobId + "/file", "files/" + easemobId +
                "/" + groupId + "/");
        Logger.i(groupId);
        Logger.i(path);
    }

    public static final String GROUP_ID = "groupId";

    public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent(context, ChatFilesActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    private void initView() {
        findViewById(R.id.backImg).setOnClickListener(this);
        final List<File> fileList = FileUtils.getAllFileList(new String[]{path});
        if (fileList == null || fileList.size() == 0) {
            ShowUtil.showToast("没有相关文件");
            return;
        }
        for (File file : fileList) {
            Logger.i(file.getPath());
        }
        mRcList = (RecyclerView) findViewById(R.id.mRecView);
        mRcList.setLayoutManager(new GridLayoutManager(ChatFilesActivity.this, 3));
        mRcList.setHasFixedSize(true);
        mRcList.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new ChatFilesRecylerAdapter(ChatFilesActivity.this, fileList);
        mRcList.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new ChatFilesRecylerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final View view, int position) {
                File file = mAdapter.getFileList().get(position);
                if (!FileType.isPrintType(file.getPath())) {
                    ShowUtil.showToast("不可打开类型");
                    return;
                }
                Bundle bundle = new Bundle();
                if (FileType.getPrintType(file.getPath()) == FileType.TYPE_IMG) {
                    bundle.putBoolean(ImgPreviewActivity.PREVIEW_FLAG, true);
                    bundle.putCharSequence(ImgPreviewActivity.KEY_IMG_URI, file.getPath());
                    ImgPreviewActivity.start(ChatFilesActivity.this, bundle);
                } else if (FileType.getPrintType(file.getPath()) == FileType.TYPE_PDF) {
                    bundle.putBoolean(PdfPreviewActivity.PREVIEW_FLAG, true);
                    bundle.putCharSequence(PdfPreviewActivity.KEY_PDF_PATH, file.getPath());
                    PdfPreviewActivity.start(ChatFilesActivity.this, bundle);
                } else {
                    turnFile(file);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        switch (id) {
            case R.id.backImg:
                finish();
                break;
        }
    }

    static class MyHandler extends Handler {
        WeakReference<ChatFilesActivity> mActivity;

        MyHandler(ChatFilesActivity activity) {
            mActivity = new WeakReference<ChatFilesActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ChatFilesActivity activity = mActivity.get();
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
        PrintRequest.uploadFile(ChatFilesActivity.this, FileType.getType(file.getPath()), base, file.getName(), new
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
        PrintRequest.docPreview(ChatFilesActivity.this, url, new HttpListener() {
            @Override
            public void onSucceed(String content) {
                DocPreviewResp docPreviewResp = GsonUtil.GsonToBean(content, DocPreviewResp.class);
                if (docPreviewResp == null) {
                    dismissLoading();
                    return;
                }
                if (!docPreviewResp.isSuccess()) {
                    ShowUtil.showToast(docPreviewResp.getErrorMsg());
                    dismissLoading();
                    return;
                }
                if (isLoading()) {
                    dismissLoading();
                    turnPreview(docPreviewResp.getData().getArryList(), url);
                }
            }

            @Override
            public void onFailed(String exception) {
                dismissLoading();
                ShowUtil.showToast(getString(R.string.net_error));
            }
        });
    }

    public void turnPreview(ArrayList<String> fileUrlList, String fileUrl) {
        Bundle bundle = new Bundle();
        bundle.putCharSequence(DocPreviewActivity.KEY_URL, fileUrl);
        bundle.putStringArrayList(DocPreviewActivity.KEY_URL_LIST, fileUrlList);
        bundle.putSerializable(DocPreviewActivity.KEY_FILE, file);
        bundle.putBoolean(DocPreviewActivity.PREVIEW_FLAG, true);
        DocPreviewActivity.start(ChatFilesActivity.this, bundle);
    }


//     Logger.i(PathUtil.getInstance().getFilePath());
//        Logger.i(PathUtil.getInstance().getHistoryPath());
//        Logger.i(PathUtil.getInstance().getImagePath());
//        Logger.i(PathUtil.getInstance().getVideoPath());
//        Logger.i(PathUtil.getInstance().getVoicePath());
    ///storage/emulated/0/Android/data/huanxing_print.com.cn.printhome/1196170420115735#ecostardev/100000309/file
//        /storage/emulated/0/Android/data/huanxing_print.com.cn.printhome/1196170420115735#ecostardev/100000309/chat
//        /storage/emulated/0/Android/data/huanxing_print.com.cn.printhome/1196170420115735#ecostardev/100000309/image
//        /storage/emulated/0/Android/data/huanxing_print.com.cn.printhome/1196170420115735#ecostardev/100000309/video
//        /storage/emulated/0/Android/data/huanxing_print.com.cn.printhome/1196170420115735#ecostardev/100000309/voice


}
