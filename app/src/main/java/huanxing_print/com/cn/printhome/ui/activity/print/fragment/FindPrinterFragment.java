package huanxing_print.com.cn.printhome.ui.activity.print.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshView.SimpleXRefreshListener;
import com.andview.refreshview.XRefreshViewFooter;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.log.Logger;
import huanxing_print.com.cn.printhome.model.print.Printer;
import huanxing_print.com.cn.printhome.ui.activity.copy.CopySettingActivity;
import huanxing_print.com.cn.printhome.ui.activity.print.PickPrinterActivity;
import huanxing_print.com.cn.printhome.ui.activity.print.PrinterLocationActivity;
import huanxing_print.com.cn.printhome.ui.adapter.FindPrinterRcAdapter;
import huanxing_print.com.cn.printhome.util.DisplayUtil;
import huanxing_print.com.cn.printhome.util.ShowUtil;
import huanxing_print.com.cn.printhome.view.RecyclerViewDivider;

/**
 * Created by LGH on 2017/5/3.
 */

public class FindPrinterFragment extends BaseLazyFragment implements AMapLocationListener {

    private AMapLocationClient locationClient;
    private AMapLocationClientOption locationOption;

    private RecyclerView printerRcList;

    private List<Printer> printerList = new ArrayList<>();
    private FindPrinterRcAdapter findPrinterRcAdapter;

    private Spinner spinner;
    private TextView addressTv;
    private ImageView refreshImg;
    private XRefreshView xRefreshView;
    private int pageCount = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        location();
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
        xRefreshView = (XRefreshView) view.findViewById(R.id.xRefreshView);
        addressTv = (TextView) view.findViewById(R.id.addressTv);
        refreshImg = (ImageView) view.findViewById(R.id.refreshImg);
        refreshImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressTv.setText("正在定位...");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        location();
                    }
                }, 2000);
            }
        });
        spinner = (Spinner) view.findViewById(R.id.disSpinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String[] dis = getResources().getStringArray(R.array.distance);
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
                        CopySettingActivity.start(context, null);
                        break;
                    case R.id.detailTv:
                        Printer printer = new Printer();
                        printer.setId(10);
                        EventBus.getDefault().post(printer, PickPrinterActivity.TAG_EVENT_PRINTER);
                        break;
                    case R.id.navImg:
                        PrinterLocationActivity.start(context, null);
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

        xRefreshView.setPinnedTime(1000);
        xRefreshView.setMoveForHorizontal(true);
        xRefreshView.setPullLoadEnable(true);
        xRefreshView.setAutoLoadMore(false);
        findPrinterRcAdapter.setCustomLoadMoreView(new XRefreshViewFooter(context));
        xRefreshView.enableReleaseToLoadMore(true);
        xRefreshView.enableRecyclerViewPullUp(true);
        xRefreshView.enablePullUpWhenLoadCompleted(true);

        xRefreshView.setXRefreshViewListener(new SimpleXRefreshListener() {

            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pageCount = 1;
                        findPrinterRcAdapter.clear();
                        for (int i = 0; i < 6; i++) {
                            findPrinterRcAdapter.insert(new Printer(), findPrinterRcAdapter.getAdapterItemCount());
                        }
                        findPrinterRcAdapter.notifyDataSetChanged();
                        xRefreshView.stopRefresh();
                    }
                }, 500);
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        for (int i = 0; i < 6; i++) {
                            findPrinterRcAdapter.insert(new Printer(), findPrinterRcAdapter.getAdapterItemCount());
                        }
                        pageCount++;
                        if (pageCount >= 3) {//模拟没有更多数据的情况
                            xRefreshView.setLoadComplete(true);
                        } else {
                            // 刷新完成必须调用此方法停止加载
                            xRefreshView.stopLoadMore(false);
                            //当数据加载失败 不需要隐藏footerview时，可以调用以下方法，传入false，不传默认为true
                            // 同时在Footerview的onStateFinish(boolean hideFooter)，可以在hideFooter为false时，显示数据加载失败的ui
//                            xRefreshView1.stopLoadMore(false);
                        }
                    }
                }, 500);
            }
        });
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

    public void location() {
        try {
            locationClient = new AMapLocationClient(context);
            locationOption = new AMapLocationClientOption();
            locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
            locationClient.setLocationListener(this);
            locationOption.setOnceLocation(true);
            locationClient.setLocationOption(locationOption);
            locationClient.startLocation();
        } catch (Exception e) {
            e.printStackTrace();
            addressTv.setText("定位失败");
        }
    }

    //province=江苏省#city=南京市#district=秦淮区#cityCode=025#adCode=320104#address=江苏省南京市秦淮区养虎仓靠近普天科技园#country=中国#road
    // =养虎仓#poiName=普天科技园#street=养虎仓#streetNum=34号#aoiName=普天科技园#poiid=#floor=#errorCode=0#errorInfo=success
    // #locationDetail=-5 #csid:94fcc35f079b4035af001dbc30ebd05f#locationType=5
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (null != aMapLocation) {
            if (aMapLocation.getErrorCode() == 0) {
                addressTv.setText(aMapLocation.getAddress());
            } else {
                Logger.i("errorcode" + aMapLocation.getErrorCode());
                addressTv.setText("定位失败");
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}

