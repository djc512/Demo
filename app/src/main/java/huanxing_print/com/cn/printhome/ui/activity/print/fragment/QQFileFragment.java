package huanxing_print.com.cn.printhome.ui.activity.print.fragment;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.log.Logger;
import huanxing_print.com.cn.printhome.ui.activity.print.AddFileActivity;
import huanxing_print.com.cn.printhome.ui.adapter.FileRecyclerAdapter;
import huanxing_print.com.cn.printhome.util.FileUtils;
import huanxing_print.com.cn.printhome.util.ShowUtil;
import huanxing_print.com.cn.printhome.util.file.FileComparator;
import huanxing_print.com.cn.printhome.view.ClearEditText;
import huanxing_print.com.cn.printhome.view.FileFilterPopupMenu;
import huanxing_print.com.cn.printhome.view.RecyclerViewDivider;
import huanxing_print.com.cn.printhome.view.dialog.Alert;

/**
 * Created by LGH on 2017/4/27.
 */

public class QQFileFragment extends BaseLazyFragment {

    private final String[] PATH_QQ_FILE = {Environment.getExternalStorageDirectory().getPath() +
            "/tencent/QQfile_recv/", Environment.getExternalStorageDirectory().getPath() + "/tencent/QQ_Images/"};
    private RecyclerView mRcList;
    private FileRecyclerAdapter mAdapter;
    private ImageView filterBtn;
    private ClearEditText searchEditText;
    private RelativeLayout searchRyt;
    private boolean isSearch = false;
    private FileFilterPopupMenu popupMenu;

    private int mode = FileComparator.MODE_NAME;

    private List<File> fileList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_file_list, container, false);
            initView(view);
            isPrepared = true;
            if (!isLoaded) {
                lazyLoad();
            }
        }
        return view;
    }

    private void initView(View view) {
        popupMenu = new FileFilterPopupMenu(getActivity(),view );
        popupMenu.setOnItemClickListener(new FileFilterPopupMenu.OnItemClickListener() {
            @Override
            public void onClick(int id) {
                switch (id) {
                    case R.id.timeTv:
                        mode = FileComparator.MODE_TIME;
                        updateList(mAdapter.getFileList());
                        break;
                    case R.id.nameTv:
                        mode = FileComparator.MODE_NAME;
                        updateList(mAdapter.getFileList());
                        break;
                    case R.id.typeTv:
                        mode = FileComparator.MODE_TYPE;
                        updateList(mAdapter.getFileList());
                        break;
                }
            }
        });
        searchRyt = (RelativeLayout) view.findViewById(R.id.searchRyt);
        mRcList = (RecyclerView) view.findViewById(R.id.mRecView);
        filterBtn = (ImageView) view.findViewById(R.id.filterBtn);
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilter();
            }
        });
        searchEditText = (ClearEditText) view.findViewById(R.id.searchEditText);
        searchEditText.setOnClearListener(new ClearEditText.OnClearListener() {
            @Override
            public void onClear() {
                isSearch = false;
                fileList = FileUtils.getAllFileList(PATH_QQ_FILE);
                updateList(fileList);
            }
        });
        searchEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        isSearch = true;
                        serchFileList(v.getText().toString());
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || isLoaded) {
            return;
        }
        fileList = FileUtils.getAllFileList(PATH_QQ_FILE);
        if (fileList == null || fileList.size() == 0) {
            ShowUtil.showToast("没有相关文件");
            searchRyt.setVisibility(View.GONE);
            return;
        }
        searchRyt.setVisibility(View.VISIBLE);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        mRcList.setLayoutManager(mLayoutManager);
        mRcList.setHasFixedSize(true);
        mRcList.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new FileRecyclerAdapter(fileList, context);
        mRcList.setAdapter(mAdapter);
        mRcList.addItemDecoration(new RecyclerViewDivider(context, LinearLayoutManager.VERTICAL, 1, ContextCompat
                .getColor(context, R.color.devide_gray)));
        mAdapter.setOnItemClickListener(
                new FileRecyclerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(final View view, int position) {
                        File file = mAdapter.getFileList().get(position);
                        ((AddFileActivity) getActivity()).pickFile(file);
                    }
                });
        mAdapter.setItemLongClickListener(new FileRecyclerAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, final int position) {
                final File file = mAdapter.getFileList().get(position);
                Alert.show(context, "提示", "确定删除文件？", null, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (file.delete()) {
                            mAdapter.getFileList().remove(position);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        });
        updateList(fileList);
        isLoaded = true;
    }

    private void showFilter() {
        popupMenu.setTextColor(mode);
        popupMenu.showLocation(R.id.filterBtn);
    }

    private void serchFileList(String keyword) {
        String[] keywordStr = {keyword};
        SearchFileExecutor searchFileExecutor = new SearchFileExecutor();
        searchFileExecutor.execute(keywordStr);
    }

    private void updateList(List<File> fileList) {
        Collections.sort(fileList, new FileComparator(mode));
        mAdapter.setFileList(fileList);
        mAdapter.notifyDataSetChanged();
    }

    class SearchFileExecutor extends AsyncTask<String, Integer, List<File>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mAdapter.clearData();
            mAdapter.notifyDataSetChanged();
            ShowUtil.showToast("搜索中...");
        }

        @Override
        protected void onPostExecute(List list) {
            Logger.i("onPostExecute");
            if (isSearch) {
                mAdapter.notifyDataSetChanged();
            }
        }

        @Override
        protected List<File> doInBackground(String... params) {
            List<File> fileList = new ArrayList<>();
            FileUtils.searchFileList(params[0], fileList, PATH_QQ_FILE);
            HashMap<String, Object> map = new HashMap<>();
            List<HashMap<String, Object>> list = new ArrayList<>();
            mAdapter.setFileList(fileList);
            return null;
        }
    }
}
