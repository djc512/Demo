package huanxing_print.com.cn.printhome.ui.activity.contact;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.ui.adapter.ChatFilesRecylerAdapter;
import huanxing_print.com.cn.printhome.util.FileUtils;
import huanxing_print.com.cn.printhome.util.ShowUtil;

public class ChatFilesActivity extends BaseActivity {


    private RecyclerView mRcList;
    private ChatFilesRecylerAdapter mAdapter;

    @Override
    protected BaseActivity getSelfActivity() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_files);
        initView();

    }

    private void initView() {
        final List<String> photoList = FileUtils.getImgList(ChatFilesActivity.this);
        if (photoList == null || photoList.size() == 0) {
            ShowUtil.showToast("没有相关文件");
            return;
        }
        mRcList = (RecyclerView) findViewById(R.id.mRecView);
        mRcList.setLayoutManager(new GridLayoutManager(ChatFilesActivity.this, 3));
        mRcList.setHasFixedSize(true);
        mRcList.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new ChatFilesRecylerAdapter(ChatFilesActivity.this, photoList);
        mRcList.setAdapter(mAdapter);
    }



}
