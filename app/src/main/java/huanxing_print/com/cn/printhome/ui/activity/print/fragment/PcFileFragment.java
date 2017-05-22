package huanxing_print.com.cn.printhome.ui.activity.print.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.log.Logger;
import huanxing_print.com.cn.printhome.model.print.DelPcFileResp;
import huanxing_print.com.cn.printhome.model.print.PrintListBean;
import huanxing_print.com.cn.printhome.net.request.print.HttpListener;
import huanxing_print.com.cn.printhome.net.request.print.PrintRequest;
import huanxing_print.com.cn.printhome.ui.activity.print.PcFilePreviewActivity;
import huanxing_print.com.cn.printhome.ui.adapter.PcFileRecylerAdapter;
import huanxing_print.com.cn.printhome.util.FileUtils;
import huanxing_print.com.cn.printhome.util.GsonUtil;
import huanxing_print.com.cn.printhome.util.ShowUtil;
import huanxing_print.com.cn.printhome.util.StringUtil;
import huanxing_print.com.cn.printhome.view.RecyclerViewDivider;
import huanxing_print.com.cn.printhome.view.dialog.Alert;

/**
 * Created by LGH on 2017/4/27.
 */

public class PcFileFragment extends BaseLazyFragment {

    private RecyclerView mRcList;
    private PcFileRecylerAdapter mAdapter;
    private List<PrintListBean.FileInfo> fileList = new ArrayList<>();
    private Timer timer = new Timer();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_pc_file, container, false);
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
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        mRcList.setLayoutManager(mLayoutManager);
        mRcList.setHasFixedSize(true);
        mRcList.setItemAnimator(new DefaultItemAnimator());
        mRcList.addItemDecoration(new RecyclerViewDivider(context, LinearLayoutManager.VERTICAL, 1, ContextCompat
                .getColor(context, R.color.devide_gray)));
        mAdapter = new PcFileRecylerAdapter(fileList, context);
        mRcList.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new PcFileRecylerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(PcFilePreviewActivity.PC_FILE, mAdapter.getFileList().get(position));
                PcFilePreviewActivity.start(mActivity, bundle);
//                addFile(mAdapter.getFileList().get(position));
            }
        });
        mAdapter.setItemLongClickListener(new PcFileRecylerAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, final int position) {
                final PrintListBean.FileInfo file = mAdapter.getFileList().get(position);
                Alert.show(context, "提示", "确定删除文件？", null, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteFile(mAdapter.getFileList().get(position));
                        mAdapter.getFileList().remove(position);
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || isLoaded) {
            return;
        }
        if (timer == null) {
            timer = new Timer();
        }
        timer.schedule(task, 1000 * 1, 1000 * 2);
        isLoaded = true;
    }

    private void updateView(List<PrintListBean.FileInfo> fileList) {
        mAdapter.setFileList(fileList);
        mAdapter.notifyDataSetChanged();
    }

    TimerTask task = new TimerTask() {
        public void run() {
            Logger.i("TimerTask");
            queryFile();
        }
    };

    private void deleteFile(PrintListBean.FileInfo fileInfo) {
        PrintRequest.delFile(mActivity, StringUtil.stringToLong(fileInfo.getId()), new HttpListener() {
            @Override
            public void onSucceed(String content) {
                DelPcFileResp delPcFileResp = GsonUtil.GsonToBean(content, DelPcFileResp.class);
                if (delPcFileResp != null && delPcFileResp.isSuccess()) {
                    if (delPcFileResp.isSuccess()) {
                        Logger.i("删除成功");
                    }
                }
                if (delPcFileResp != null && !delPcFileResp.isSuccess()) {
                    ShowUtil.showToast(delPcFileResp.getErrorMsg());
                }
            }

            @Override
            public void onFailed(String exception) {
                ShowUtil.showToast(getString(R.string.net_error));
            }
        });
    }

    private void queryFile() {
        PrintRequest.queryPrintList(mActivity, new HttpListener() {
            @Override
            public void onSucceed(String content) {
                PrintListBean printListBean = GsonUtil.GsonToBean(content, PrintListBean.class);
                if (printListBean == null) {
                    return;
                }
                if (printListBean.isSuccess()) {
                    updateView(printListBean.getData());
                } else {
                    ShowUtil.showToast(printListBean.getErrorMsg());
                }
            }

            @Override
            public void onFailed(String exception) {
                ShowUtil.showToast(getString(R.string.net_error));
            }
        });
    }

    private void stopTimerTask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopTimerTask();
    }
}
