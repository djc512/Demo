package huanxing_print.com.cn.printhome.ui.activity.print.fragment;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.PopupMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.log.Logger;
import huanxing_print.com.cn.printhome.ui.activity.print.AddFileActivity;
import huanxing_print.com.cn.printhome.ui.activity.print.ImgPreviewActivity;
import huanxing_print.com.cn.printhome.ui.activity.print.PdfPreviewActivity;
import huanxing_print.com.cn.printhome.ui.adapter.AllFileListAdapter;
import huanxing_print.com.cn.printhome.util.FileType;
import huanxing_print.com.cn.printhome.util.FileUtils;
import huanxing_print.com.cn.printhome.util.ShowUtil;
import huanxing_print.com.cn.printhome.util.file.FileComparator;
import huanxing_print.com.cn.printhome.view.ClearEditText;
import huanxing_print.com.cn.printhome.view.dialog.Alert;

import static huanxing_print.com.cn.printhome.ui.adapter.AllFileListAdapter.FILE_OBJ;
import static huanxing_print.com.cn.printhome.ui.adapter.AllFileListAdapter.FILE_TYPE_DIR;
import static huanxing_print.com.cn.printhome.ui.adapter.AllFileListAdapter.FILE_TYPE_FILE;

/**
 * Created by LGH on 2017/4/27.
 */

public class AllFileFragment extends BaseLazyFragment implements AllFileListAdapter.BackHandledInterface {

    protected AllFileListAdapter mAdapter;
    private LinkedList<String> mHistory;
    private ListView fileListView;
    private int mode = FileComparator.MODE_NAME;
    private ClearEditText searchEditText;
    private ImageView filterBtn;
    private RelativeLayout searchRyt;

    private File showFile;
    private boolean isSearch = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_all_file, container, false);
            initView(view);
            isPrepared = true;
            if (!isLoaded) {
                lazyLoad();
            }
        }
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Logger.i(isVisibleToUser);
    }

    private void initView(View view) {
        searchRyt = (RelativeLayout) view.findViewById(R.id.searchRyt);
        fileListView = (ListView) view.findViewById(R.id.fileListView);
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
                initHistory();
                File file = new File(mHistory.get(0));
                updateList(file);
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
        showFile = Environment.getExternalStorageDirectory();
        mHistory = new LinkedList<>();
        initHistory();
        initShowAdapter(showFile);
        isLoaded = true;
    }

    private void showFilter() {
        PopupMenu popup = new PopupMenu(getActivity(), filterBtn);
        popup.getMenuInflater().inflate(R.menu.filter, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.time:
                        mode = FileComparator.MODE_TIME;
                        updateList(showFile);
                        break;
                    case R.id.name:
                        mode = FileComparator.MODE_NAME;
                        updateList(showFile);
                        break;
                    case R.id.type:
                        mode = FileComparator.MODE_TYPE;
                        updateList(showFile);
                        break;
                }
                return false;
            }
        });
        popup.show();
    }

    private void initHistory() {
        mHistory.clear();
        mHistory.add(Environment.getExternalStorageDirectory().getAbsolutePath());
    }

    private void initShowAdapter(File fileToShow) {
        mAdapter = new AllFileListAdapter(context);
        fileListView.setAdapter(mAdapter);
        fileListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, Object> map = mAdapter.getItem(position);
                File file = (File) map.get(AllFileListAdapter.FILE_OBJ);
                if (file.isDirectory()) {
                    if (file.list().length == 0) {
                        ShowUtil.showToast("目录为空");
                    } else {
                        showFile = file;
                        mHistory.add(file.getAbsolutePath());
                        updateList(file);
                    }
                } else {
                    if (!FileType.isPrintType(file.getPath())) {
                        ShowUtil.showToast("文件不可打印");
                    } else {
                        Bundle bundle = new Bundle();
                        if (FileType.getPrintType(file.getPath()) == FileType.TYPE_IMG) {
                            bundle.putCharSequence(ImgPreviewActivity.KEY_IMG_URI, (String) mAdapter.getData().get
                                    (position).get(AllFileListAdapter.FILE_PATH));
                            ImgPreviewActivity.start(context, bundle);
                        } else if (FileType.getPrintType(file.getPath()) == FileType.TYPE_PDF) {
                            bundle.putCharSequence(PdfPreviewActivity.KEY_PDF_PATH, file.getPath());
                            PdfPreviewActivity.start(context, bundle);
                        } else {
                            ((AddFileActivity) getActivity()).turnFile(file);
                        }
                    }
                }
                Logger.i(mHistory);
            }
        });
        fileListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                HashMap<String, Object> map = mAdapter.getItem(position);
                final File file = (File) map.get(AllFileListAdapter.FILE_OBJ);
                if (!file.isDirectory()) {
                    Alert.show(context, "提示", "确定删除文件？", null, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (file.delete()) {
                                mAdapter.getData().remove(position);
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                }
                return true;
            }
        });
        updateList(fileToShow);
    }

    private void updateList(File fileToShow) {
        mAdapter.clearData();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        File[] files = fileToShow.listFiles();
        List<File> fileList = new ArrayList<>();
        List<String> fileNameList = new ArrayList<>();
        for (File file : files) {
            fileList.add(file);
            fileNameList.add(file.getName());
        }
        Collections.sort(fileList, new FileComparator(mode));
        for (File file : fileList) {
            HashMap<String, Object> map = new HashMap<>();
            String fileName = file.getName();
            String fileSize;
            String fileType;
            if (file.isDirectory()) {
                fileSize = file.list().length + "项";
                fileType = FILE_TYPE_DIR;
            } else {
                fileSize = FileUtils.prettySize(file.length());
                fileType = FILE_TYPE_FILE;
            }
            String fileUpdateTime = sdf.format(file.lastModified());
            map.put(AllFileListAdapter.FILE_NAME, fileName);
            map.put(AllFileListAdapter.FILE_SIZE, fileSize);
            map.put(AllFileListAdapter.FILE_UPDATE_TIME, fileUpdateTime);
            map.put(AllFileListAdapter.FILE_TYPE, fileType);
            map.put(AllFileListAdapter.FILE_PATH, file.getPath());
            map.put(AllFileListAdapter.FILE_OBJ, file);
            mAdapter.addData(map);
        }
        mAdapter.notifyDataSetChanged();
    }

    private void serchFileList(String keyword) {
        String[] keywordStr = {keyword};
        SearchFileExecutor searchFileExecutor = new SearchFileExecutor();
        searchFileExecutor.execute(keywordStr);
    }

    @Override
    public boolean onBackPressed() {
        if (isSearch) {
            isSearch = false;
            initHistory();
            File file = new File(mHistory.get(0));
            updateList(file);
            return true;
        }
        if (mHistory.size() <= 1) {
            return false;
        }
        mHistory.removeLast();
        String previousPath = mHistory.getLast();
        File file = new File(previousPath);
        updateList(file);
        Logger.i(mHistory);
        return true;
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
            FileUtils.searchFileList(params[0], fileList, new String[]{Environment.getExternalStorageDirectory()
                    .getPath()});
            HashMap<String, Object> map = new HashMap<>();
            List<HashMap<String, Object>> list = new ArrayList<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            for (File file : fileList) {
                map = new HashMap<>();
                String fileName = file.getName();
                String fileSize;
                String fileType;
                if (file.isDirectory()) {
                    fileSize = file.list().length + "项";
                    fileType = FILE_TYPE_DIR;
                } else {
                    fileSize = FileUtils.prettySize(file.length());
                    fileType = FILE_TYPE_FILE;
                }
                String fileUpdateTime = sdf.format(file.lastModified());
                map.put(AllFileListAdapter.FILE_NAME, fileName);
                map.put(AllFileListAdapter.FILE_SIZE, fileSize);
                map.put(AllFileListAdapter.FILE_UPDATE_TIME, fileUpdateTime);
                map.put(AllFileListAdapter.FILE_TYPE, fileType);
                map.put(FILE_OBJ, file);
                mAdapter.addData(map);
            }
            return null;
        }
    }
}

