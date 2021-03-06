package huanxing_print.com.cn.printhome.ui.activity.print.fragment;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.event.print.NoUsedPrinterEvent;
import huanxing_print.com.cn.printhome.model.print.UsedPrinterResp;
import huanxing_print.com.cn.printhome.net.request.print.HttpListener;
import huanxing_print.com.cn.printhome.net.request.print.PrintRequest;
import huanxing_print.com.cn.printhome.ui.activity.print.PickPrinterActivity;
import huanxing_print.com.cn.printhome.ui.adapter.UsedPrinterRcAdapter;
import huanxing_print.com.cn.printhome.util.DisplayUtil;
import huanxing_print.com.cn.printhome.util.GsonUtil;
import huanxing_print.com.cn.printhome.util.ShowUtil;
import huanxing_print.com.cn.printhome.view.RecyclerViewDivider;

/**
 * Created by LGH on 2017/5/3.
 */

public class UsedPrinterFragment extends BaseLazyFragment {

    private RecyclerView printerRcList;
    private boolean isFirst = true;
    private List<UsedPrinterResp.Printer> printerList = new ArrayList<>();
    private UsedPrinterRcAdapter usedPrinterRcAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_printer_used, container, false);
            initView(view);
            isPrepared = true;
            if (!isLoaded) {
                lazyLoad();
            }
        }
        return view;
    }

    private void initView(View view) {
        printerRcList = (RecyclerView) view.findViewById(R.id.mRecView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        printerRcList.setLayoutManager(mLayoutManager);
        printerRcList.setHasFixedSize(true);
        printerRcList.setItemAnimator(new DefaultItemAnimator());
        printerRcList.addItemDecoration(new RecyclerViewDivider(context, LinearLayoutManager.VERTICAL, DisplayUtil
                .dip2px(context, 10), ContextCompat.getColor(context, R.color.bc_gray)));
        usedPrinterRcAdapter = new UsedPrinterRcAdapter(printerList);
        usedPrinterRcAdapter.setOnItemClickListener(new UsedPrinterRcAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()) {
                    case R.id.printLyt:
                        ((PickPrinterActivity) getActivity()).turnSetting(usedPrinterRcAdapter.getPrinterList().get
                                (position).getPrinterNo());
                        break;
                    case R.id.detailTv:
                        turnDetail(usedPrinterRcAdapter.getPrinterList().get(position).getPrinterNo());
                        break;
                }
            }
        });
        printerRcList.setAdapter(usedPrinterRcAdapter);
//        printerList.add(new UsedPrinterResp.PrintUtil());
//        printerList.add(new UsedPrinterResp.PrintUtil());
//        printerList.add(new UsedPrinterResp.PrintUtil());
//        printerList.add(new UsedPrinterResp.PrintUtil());
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
        PrintRequest.queryRecentPrinters(mActivity, new HttpListener() {
            @Override
            public void onSucceed(String content) {
                UsedPrinterResp usedPrinterResp = GsonUtil.GsonToBean(content, UsedPrinterResp.class);
                if (usedPrinterResp == null) {
                    return;
                }
                if (usedPrinterResp.isSuccess()) {
                    printerList = usedPrinterResp.getData();
                    if (printerList == null || printerList.isEmpty()) {
                        if (isFirst) {
                            isFirst = false;
                            EventBus.getDefault().post(new NoUsedPrinterEvent(true));
                            ShowUtil.showToast("没有已用打印机");
                        }
                        return;
                    }
                    usedPrinterRcAdapter.setPrinterList(printerList);
                    usedPrinterRcAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailed(String exception) {
                ShowUtil.showToast(getString(R.string.net_error));
            }
        });
        isLoaded = true;
    }

    private void turnDetail(String printerNo) {
        ((PickPrinterActivity) getActivity()).requeryPrice(printerNo);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
