package huanxing_print.com.cn.printhome.ui.activity.print;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.log.Logger;
import huanxing_print.com.cn.printhome.ui.adapter.FileRecyclerAdapter;
import huanxing_print.com.cn.printhome.util.DocTypeUtil;
import huanxing_print.com.cn.printhome.util.FileType;

public class FileListActivity extends AppCompatActivity {
    private static final String TAG = "FileActivity";

    private RecyclerView mRcList;
    private RecyclerView.LayoutManager mLayoutManager;
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
        mLayoutManager = new LinearLayoutManager(this);
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
                        try {
                            Logger.i(encodeBase64File(file.getPath()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
//                        String typeStr = null;
//                        switch (type) {
//                            case FileType.TYPE_DOC:
//                                typeStr = "application/msword";
//                                break;
//                            case FileType.TYPE_DOCX:
//                                typeStr = "application/msword";
//                                break;
//                            case FileType.TYPE_PDF:
//                                typeStr = "application/pdf";
//                                break;
//                            case FileType.TYPE_PPT:
//                                typeStr = "application/vnd.ms-powerpoint";
//                                break;
//                            case FileType.TYPE_PPTX:
//                                typeStr = "application/vnd.ms-powerpoint";
//                                break;
//                        }
//                        if (typeStr != null) {
//                            turnActivity(typeStr, file.getPath());
//                        }

                    }
                });
    }

    public static String encodeBase64File(String path) throws Exception {
        File file = new File(path);
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int) file.length()];
        inputFile.read(buffer);
        inputFile.close();
        return Base64.encodeToString(buffer, Base64.DEFAULT);
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
        fileList = (List<File>) bundle.getSerializable(AddFileActivity.KEY_FILE);
        source = bundle.getInt(AddFileActivity.KEY_SOURCE);
        for (int i = 0; i < fileList.size(); i++) {
            Log.i(TAG, fileList.get(i).getName());
        }
    }

    public void onDoc(View view) {
        mAdapter.setFileList(DocTypeUtil.getFiltFileList(fileList, FileType.TYPE_PPT));
        mAdapter.notifyDataSetChanged();
    }
}
