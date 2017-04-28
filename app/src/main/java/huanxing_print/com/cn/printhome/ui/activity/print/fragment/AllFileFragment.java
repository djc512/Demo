package huanxing_print.com.cn.printhome.ui.activity.print.fragment;

import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.log.Logger;
import huanxing_print.com.cn.printhome.ui.adapter.AllFileListAdapter;
import huanxing_print.com.cn.printhome.util.FileUtils;

import static huanxing_print.com.cn.printhome.ui.adapter.AllFileListAdapter.FILE_OBJ;
import static huanxing_print.com.cn.printhome.ui.adapter.AllFileListAdapter.FILE_TYPE_DIR;
import static huanxing_print.com.cn.printhome.ui.adapter.AllFileListAdapter.FILE_TYPE_FILE;

/**
 * Created by LGH on 2017/4/27.
 */

public class AllFileFragment extends BaseLazyFragment implements AllFileListAdapter.BackHandledInterface {

    protected AllFileListAdapter mAdapter;
    private LinkedList<String> mHistory;//访问的文件夹历史记录
    private ListView fileListView;

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
    public void onStop() {
        super.onStop();
        Logger.i("onStop");
    }

    @Override
    public void onPause() {
        super.onPause();
        Logger.i("onPause");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Logger.i("onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.i("onDestroy");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Logger.i(isVisibleToUser);
    }

    private void initView(View view) {
        fileListView = (ListView) view.findViewById(R.id.fileListView);
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || isLoaded) {
            return;
        }
        File file = Environment.getExternalStorageDirectory();
        mHistory = new LinkedList<>();
        mHistory.add(file.getAbsolutePath());
        initShowAdapter(file);
        isLoaded = true;
    }

    private void initShowAdapter(File fileToShow) {
        String path = fileToShow.getAbsolutePath();
        mAdapter = new AllFileListAdapter(context);
        File[] files = fileToShow.listFiles();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        for (File file : files) {
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
            map.put(FILE_OBJ, file);
            mAdapter.addData(map);
        }
        fileListView.setAdapter(mAdapter);
        fileListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, Object> map = mAdapter.getItem(position);
                File file = (File) map.get(AllFileListAdapter.FILE_OBJ);
                if (file.isDirectory()) {
                    mHistory.add(file.getAbsolutePath());
                    initShowAdapter(file);
                }
            }
        });
    }

    @Override
    public boolean onBackPressed() {
        if (mHistory.size() <= 1) {
            return false;
        }
        mHistory.removeLast();
        String previousPath = mHistory.getLast();
        File file = new File(previousPath);
        initShowAdapter(file);
        return true;
    }


}

