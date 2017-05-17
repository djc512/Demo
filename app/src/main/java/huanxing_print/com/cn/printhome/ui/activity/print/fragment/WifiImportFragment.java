package huanxing_print.com.cn.printhome.ui.activity.print.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.log.Logger;
import huanxing_print.com.cn.printhome.ui.activity.print.AddFileActivity;
import huanxing_print.com.cn.printhome.ui.activity.print.ImgPreviewActivity;
import huanxing_print.com.cn.printhome.ui.activity.print.PdfPreviewActivity;
import huanxing_print.com.cn.printhome.ui.adapter.FileRecyclerAdapter;
import huanxing_print.com.cn.printhome.util.FileType;
import huanxing_print.com.cn.printhome.util.FileUtils;
import huanxing_print.com.cn.printhome.util.ShowUtil;
import huanxing_print.com.cn.printhome.util.StorageUtil;
import huanxing_print.com.cn.printhome.util.WifiUtil;
import huanxing_print.com.cn.printhome.util.webserver.WebServer;
import huanxing_print.com.cn.printhome.util.webserver.WebServer1;
import huanxing_print.com.cn.printhome.view.RecyclerViewDivider;
import huanxing_print.com.cn.printhome.view.dialog.Alert;

/**
 * Created by LGH on 2017/4/27.
 */

public class WifiImportFragment extends BaseLazyFragment {

    private List<File> fileList;
    private RecyclerView fileRecView;
    private FileRecyclerAdapter mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_wifi_file, container, false);
            isPrepared = true;
            if (!isLoaded) {
                lazyLoad();
            }
        }
        return view;
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || isLoaded) {
            return;
        }
        isLoaded = true;
        TextView wifiTv = (TextView) view.findViewById(R.id.wifiTv);
        wifiTv.setText(WifiUtil.getWifiInfo(context));
        TextView sdTv = (TextView) view.findViewById(R.id.sdTv);
        sdTv.setText(StorageUtil.getSdInfo(context));
        TextView ipTv = (TextView) view.findViewById(R.id.ipTv);
        ipTv.setText("在浏览器输入以下网址:\n" + WifiUtil.getIPAddress(true, context) + ":" + WebServer.PORT);
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setProgress(StorageUtil.getSdUsablePercent(context));

        fileList = FileUtils.getFileList(FileUtils.getWifiUploadPath());
        Logger.i(FileUtils.getWifiUploadPath());
        if (fileList == null || fileList.size() == 0) {
            View devider = view.findViewById(R.id.devider);
            devider.setVisibility(View.GONE);
            return;
        }

        LinearLayout lv = (LinearLayout) view.findViewById(R.id.lv);
        lv.setVisibility(View.GONE);

        fileRecView = (RecyclerView) view.findViewById(R.id.fileRecView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        fileRecView.setLayoutManager(mLayoutManager);
        fileRecView.setHasFixedSize(true);
        fileRecView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new FileRecyclerAdapter(fileList, context);
        fileRecView.setAdapter(mAdapter);
        fileRecView.addItemDecoration(new RecyclerViewDivider(context, LinearLayoutManager.VERTICAL, 1, ContextCompat
                .getColor(context, R.color.devide_gray)));
        mAdapter.setOnItemClickListener(
                new FileRecyclerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(final View view, int position) {
                        File file = mAdapter.getFileList().get(position);
                        if (!FileType.isPrintType(file.getPath())) {
                            ShowUtil.showToast("文件不可打印");
                            return;
                        }
                        Bundle bundle = new Bundle();
                        if (FileType.getPrintType(file.getPath()) == FileType.TYPE_IMG) {
                            bundle.putCharSequence(ImgPreviewActivity.KEY_IMG_URI, file.getPath());
                            ImgPreviewActivity.start(context, bundle);
                        } else if (FileType.getPrintType(file.getPath()) == FileType.TYPE_PDF) {
                            bundle.putCharSequence(PdfPreviewActivity.KEY_PDF_PATH, file.getPath());
                            PdfPreviewActivity.start(context, bundle);
                        } else {
                            ((AddFileActivity) getActivity()).turnFile(file);
                        }
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
        startWebServer();
    }

    private void startWebServer() {
        Intent intent = new Intent(context, WebServer1.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(WebServer1.FILE_LIST, (Serializable) fileList);
        intent.putExtras(bundle);
        context.startService(intent);
    }

    @Override
    public void startIntentSenderForResult(IntentSender intent, int requestCode, @Nullable Intent fillInIntent, int
            flagsMask, int flagsValues, int extraFlags, Bundle options) throws IntentSender.SendIntentException {
        super.startIntentSenderForResult(intent, requestCode, fillInIntent, flagsMask, flagsValues, extraFlags,
                options);
    }
}