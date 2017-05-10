package huanxing_print.com.cn.printhome.ui.activity.print;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.model.print.UsedPrinterResp;
import huanxing_print.com.cn.printhome.net.request.print.HttpListener;
import huanxing_print.com.cn.printhome.net.request.print.PrintRequest;
import huanxing_print.com.cn.printhome.ui.adapter.UsedPrinterRcAdapter;
import huanxing_print.com.cn.printhome.util.GsonUtil;
import huanxing_print.com.cn.printhome.util.ShowUtil;

public class RecentPrintersActivity extends BasePrintActivity {

    private RecyclerView printerRcList;
    private List<UsedPrinterResp.Printer> printerList;
    private UsedPrinterRcAdapter usedPrinterRcAdapter;

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
        usedPrinterRcAdapter = new UsedPrinterRcAdapter(printerList);
        printerRcList.setAdapter(usedPrinterRcAdapter);
        usedPrinterRcAdapter.setOnItemClickListener(
                new UsedPrinterRcAdapter.OnItemClickListener() {
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
                UsedPrinterResp usedPrinterResp = GsonUtil.GsonToBean(content, UsedPrinterResp.class);
                if (usedPrinterResp == null) {
                    return;
                }
                if (usedPrinterResp.isSuccess()) {
                    printerList = usedPrinterResp.getData();
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
