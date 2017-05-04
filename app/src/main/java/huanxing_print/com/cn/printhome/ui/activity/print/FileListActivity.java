package huanxing_print.com.cn.printhome.ui.activity.print;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.log.Logger;
import huanxing_print.com.cn.printhome.model.print.UploadImgBean;
import huanxing_print.com.cn.printhome.net.request.print.HttpListener;
import huanxing_print.com.cn.printhome.net.request.print.PrintRequest;
import huanxing_print.com.cn.printhome.ui.adapter.FileRecyclerAdapter;
import huanxing_print.com.cn.printhome.util.FileType;
import huanxing_print.com.cn.printhome.util.FileUtils;
import huanxing_print.com.cn.printhome.util.GsonUtil;
import huanxing_print.com.cn.printhome.util.ShowUtil;

import static huanxing_print.com.cn.printhome.ui.activity.print.AddFileActivity.KEY_FILE;
import static huanxing_print.com.cn.printhome.ui.activity.print.DocPreviewActivity.KEY_URL;

public class FileListActivity extends BasePrintActivity {
    private static final String TAG = "FileActivity";

    private RecyclerView mRcList;
    private FileRecyclerAdapter mAdapter;

    private List<File> fileList;
    private int source;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_list);
        initData();
        initView();
    }

    private void initView() {
        mRcList = (RecyclerView) findViewById(R.id.mRecView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRcList.setLayoutManager(mLayoutManager);
        mRcList.setHasFixedSize(true);
        mRcList.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new FileRecyclerAdapter(fileList);
        mRcList.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(
                new FileRecyclerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(final View view, int position) {
                        File file = fileList.get(position);
                        int type = FileType.getPrintType(file.getPath());
                        Logger.i(file.getPath());
                        try {
                            Logger.i(FileUtils.getBase64(file));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        upload(fileList.get(position));
                    }
                });
    }

    private void upload(final File file) {
        PrintRequest.uploadFile(activity, FileType.getType(file.getPath()), FileUtils.getBase64(file), file
                .getName(), "1", new HttpListener() {
            @Override
            public void onSucceed(String content) {
                UploadImgBean uploadImgBean = GsonUtil.GsonToBean(content, UploadImgBean.class);
                if (uploadImgBean == null) {
                    return;
                }
                if (uploadImgBean.isSuccess()) {
                    String url = uploadImgBean.getData().getImgUrl();
                    turnPreView(url, file);
                } else {
                    ShowUtil.showToast(getString(R.string.upload_failure));
                }
            }

            @Override
            public void onFailed(String exception) {
                ShowUtil.showToast(getString(R.string.net_error));
            }
        }, false);
    }

    private void turnPreView(String url, File file) {
        Intent intent = new Intent(context, DocPreviewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_FILE, file);
        bundle.putString(KEY_URL, url);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void turnActivity(String typeStr, String path) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(path));
        intent.setDataAndType(uri, typeStr);
        startActivity(intent);
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        fileList = (List<File>) bundle.getSerializable(KEY_FILE);
        source = bundle.getInt(AddFileActivity.KEY_SOURCE);
        for (int i = 0; i < fileList.size(); i++) {
            Log.i(TAG, fileList.get(i).getName());
        }
    }

    public void onDoc(View view) {
        mAdapter.setFileList(FileType.getFiltFileList(fileList, FileType.TYPE_PPT));
        mAdapter.notifyDataSetChanged();
    }
}
