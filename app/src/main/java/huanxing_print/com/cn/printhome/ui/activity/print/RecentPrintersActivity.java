package huanxing_print.com.cn.printhome.ui.activity.print;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.model.print.Printer;
import huanxing_print.com.cn.printhome.model.print.PrinterListBean;
import huanxing_print.com.cn.printhome.net.request.print.HttpListener;
import huanxing_print.com.cn.printhome.net.request.print.PrintRequest;
import huanxing_print.com.cn.printhome.ui.adapter.PrinterRcAdapter;
import huanxing_print.com.cn.printhome.util.GsonUtil;
import huanxing_print.com.cn.printhome.util.ShowUtil;

public class RecentPrintersActivity extends BasePrintActivity {

    private RecyclerView printerRcList;
    private List<Printer> printerList;
    private PrinterRcAdapter printerRcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_printers);
        queryPrinters();
    }

    private void initView() {
        printerRcList = (RecyclerView) findViewById(R.id.mRecView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        printerRcList.setLayoutManager(mLayoutManager);
        printerRcList.setHasFixedSize(true);
        printerRcList.setItemAnimator(new DefaultItemAnimator());
        printerRcAdapter = new PrinterRcAdapter(printerList);
        printerRcList.setAdapter(printerRcAdapter);
        printerRcAdapter.setOnItemClickListener(
                new PrinterRcAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(final View view, int position) {
                        int id = view.getId();
                        switch (view.getId()) {
                            case R.id.nameTv:
                                ShowUtil.showToast("nameTv");
                                break;
                            case R.id.icImg:
                                ShowUtil.showToast("icImg");
                                break;
                        }
                    }
                });
    }

    private void queryPrinters() {
        PrintRequest.queryRecentPrinters(activity, new HttpListener() {
            @Override
            public void onSucceed(String content) {
                PrinterListBean printerListBean = GsonUtil.GsonToBean(content, PrinterListBean.class);
                if (printerListBean == null) {
                    return;
                }
                if (printerListBean.isSuccess()) {
                    printerList = printerListBean.getData();
                    initView();
                }
            }

            @Override
            public void onFailed(String exception) {
                ShowUtil.showToast(getString(R.string.net_error));
            }
        });
    }
}
