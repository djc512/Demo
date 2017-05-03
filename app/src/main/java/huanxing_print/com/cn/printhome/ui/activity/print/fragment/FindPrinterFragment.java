package huanxing_print.com.cn.printhome.ui.activity.print.fragment;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.model.print.Printer;
import huanxing_print.com.cn.printhome.ui.adapter.FindPrinterRcAdapter;
import huanxing_print.com.cn.printhome.util.DisplayUtil;
import huanxing_print.com.cn.printhome.util.ShowUtil;
import huanxing_print.com.cn.printhome.view.RecyclerViewDivider;

/**
 * Created by LGH on 2017/5/3.
 */

public class FindPrinterFragment extends BaseLazyFragment {

    private RecyclerView printerRcList;

    private List<Printer> printerList = new ArrayList<>();
    private FindPrinterRcAdapter findPrinterRcAdapter;

    private Spinner spinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_printer_find, container, false);
            initView(view);
            isPrepared = true;
            if (!isLoaded) {
                lazyLoad();
            }
        }
        return view;
    }

    private void initView(View view) {
        spinner = (Spinner) view.findViewById(R.id.disSpinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String[] dis = getResources().getStringArray(R.array.distance);
                ShowUtil.showToast(dis[pos]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
        printerRcList = (RecyclerView) view.findViewById(R.id.mRecView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        printerRcList.setLayoutManager(mLayoutManager);
        printerRcList.setHasFixedSize(true);
        printerRcList.setItemAnimator(new DefaultItemAnimator());
        printerRcList.addItemDecoration(new RecyclerViewDivider(context, LinearLayoutManager.VERTICAL, DisplayUtil
                .dip2px(context, 10), ContextCompat.getColor(context, R.color.bc_gray)));
        findPrinterRcAdapter = new FindPrinterRcAdapter(printerList);
        findPrinterRcAdapter.setOnItemClickListener(new FindPrinterRcAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()) {
                    case R.id.printerLyt:
                        ShowUtil.showToast(position + " printerLyt");
                        break;
                    case R.id.detailTv:
                        ShowUtil.showToast(position + " detailTv");
                        break;
                    case R.id.navImg:
                        ShowUtil.showToast(position + " navImg");
                        break;
                    case R.id.commentTv:
                        ShowUtil.showToast(position + " commentTv");
                        break;
                    case R.id.printCountTv:
                        ShowUtil.showToast(position + " printCountTv");
                        break;
                }
            }
        });
        printerRcList.setAdapter(findPrinterRcAdapter);
        printerList.add(new Printer());
        printerList.add(new Printer());
        printerList.add(new Printer());
        printerList.add(new Printer());
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || isLoaded) {
            return;
        }
        findPrinterRcAdapter.setPrinterList(printerList);
        findPrinterRcAdapter.notifyDataSetChanged();
        isLoaded = true;
    }

}

