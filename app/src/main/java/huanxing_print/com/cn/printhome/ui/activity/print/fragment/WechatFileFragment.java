package huanxing_print.com.cn.printhome.ui.activity.print.fragment;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.ui.adapter.FileRecyclerAdapter;
import huanxing_print.com.cn.printhome.util.FileUtils;
import huanxing_print.com.cn.printhome.view.RecyclerViewDivider;

/**
 * Created by LGH on 2017/4/27.
 */

public class WechatFileFragment extends BaseLazyFragment {

    private static final String PATH_WECHAT_FILE = Environment.getExternalStorageDirectory().getPath() +
            "/tencent/MicroMsg/Download/";
    private RecyclerView mRcList;
    private FileRecyclerAdapter mAdapter;

    private List<File> fileList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_wechat_file, container, false);
            initView(view);
            isPrepared = true;
            if (!isLoaded) {
                lazyLoad();
            }
        }
        return view;
    }

    private void initView(View view) {
        mRcList = (RecyclerView) view.findViewById(R.id.mRecView);

    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || isLoaded) {
            return;
        }
        List<File> fileList = FileUtils.getFileList(PATH_WECHAT_FILE);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        mRcList.setLayoutManager(mLayoutManager);
        mRcList.setHasFixedSize(true);
        mRcList.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new FileRecyclerAdapter(fileList);
        mRcList.setAdapter(mAdapter);
        mRcList.addItemDecoration(new RecyclerViewDivider(context, LinearLayoutManager.VERTICAL, 1, ContextCompat
                .getColor(context, R.color.devide_gray)));
        mAdapter.setOnItemClickListener(
                new FileRecyclerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(final View view, int position) {
                    }
                });
    }
}
