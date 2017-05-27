package huanxing_print.com.cn.printhome.ui.activity.print.fragment;

import android.content.Intent;
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
import com.amap.api.maps2d.model.LatLng;
import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshView.SimpleXRefreshListener;
import com.andview.refreshview.XRefreshViewFooter;
import com.zhy.http.okhttp.utils.L;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.log.Logger;
import huanxing_print.com.cn.printhome.model.print.AroundPrinterResp;
import huanxing_print.com.cn.printhome.model.print.PrintSetting;
import huanxing_print.com.cn.printhome.net.request.print.HttpListener;
import huanxing_print.com.cn.printhome.net.request.print.PrintRequest;
import huanxing_print.com.cn.printhome.ui.activity.copy.CommentListActivity;
import huanxing_print.com.cn.printhome.ui.activity.print.PickPrinterActivity;
import huanxing_print.com.cn.printhome.ui.activity.print.PrinterLocationActivity;
import huanxing_print.com.cn.printhome.ui.adapter.DisTextArrayAdapter;
import huanxing_print.com.cn.printhome.ui.adapter.FindPrinterRcAdapter;
import huanxing_print.com.cn.printhome.util.DisplayUtil;
import huanxing_print.com.cn.printhome.util.GsonUtil;
import huanxing_print.com.cn.printhome.util.ShowUtil;
import huanxing_print.com.cn.printhome.util.StringUtil;
import huanxing_print.com.cn.printhome.view.RecyclerViewDivider;

/**
 * Created by LGH on 2017/5/3.
 */

public class FindPrinterFragment extends BaseLazyFragment implements AMapLocationListener {

    private AMapLocationClient locationClient;
    private AMapLocationClientOption locationOption;

    private RecyclerView printerRcList;

    private List<AroundPrinterResp.Printer> printerList = new ArrayList<>();
    private FindPrinterRcAdapter findPrinterRcAdapter;

    private Spinner spinner;
    private TextView addressTv;
    private ImageView refreshImg;
    private XRefreshView xRefreshView;
    private final int DELAY = 500;
    private int pageCount = 1;
    private int pageSize = 20;
    private int radius = 4 * 1000;
    private String center;
    private PrintSetting printSetting;

    private final int REFRESH = 1;
    private final int LOADMORE = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

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
        xRefreshView = (XRefreshView) view.findViewById(R.id.xRefreshView);
        xRefreshView.setPinnedTime(1000);
        xRefreshView.setMoveForHorizontal(true);
        xRefreshView.setPullLoadEnable(true);
        xRefreshView.setAutoLoadMore(false);

        xRefreshView.enableReleaseToLoadMore(true);
        xRefreshView.enableRecyclerViewPullUp(true);
        xRefreshView.enablePullUpWhenLoadCompleted(true);

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
        printerRcList = (RecyclerView) view.findViewById(R.id.mRecView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        printerRcList.setLayoutManager(mLayoutManager);
        printerRcList.setHasFixedSize(true);
        printerRcList.setItemAnimator(new DefaultItemAnimator());
        printerRcList.addItemDecoration(new RecyclerViewDivider(context, LinearLayoutManager.VERTICAL, DisplayUtil
                .dip2px(context, 10), ContextCompat.getColor(context, R.color.bc_gray)));
        findPrinterRcAdapter = new FindPrinterRcAdapter(printerList, context);
        findPrinterRcAdapter.setCustomLoadMoreView(new XRefreshViewFooter(context));
        findPrinterRcAdapter.setOnItemClickListener(new FindPrinterRcAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                AroundPrinterResp.Printer printer = findPrinterRcAdapter.getPrinterList().get(position);
                switch (view.getId()) {
                    case R.id.printerLyt:
                        ((PickPrinterActivity) getActivity()).turnSetting(printer.getPrinterNo());
                        break;
                    case R.id.detailTv:
                        turnDetail(printer.getPrinterNo());
                        break;
                    case R.id.navLty:
                        LatLng latLng = StringUtil.getLatLng(printer.getLocation());
                        Logger.i(latLng.toString());
                        if (latLng != null) {
                            Bundle bundle = new Bundle();
                            bundle.putParcelable(PrinterLocationActivity.LATLNG, latLng);
                            PrinterLocationActivity.start(context, bundle);
                        } else {
                            ShowUtil.showToast("位置错误");
                        }
                        break;
                    case R.id.commentTv:
                        if (findPrinterRcAdapter.isCommentEnpty(printer)) {
                            return;
                        }
                        Bundle bundle = new Bundle();
                        bundle.putString("printer_id", findPrinterRcAdapter.getPrinterList().get(position)
                                .getPrinterNo());
                        Intent intent = new Intent(context, CommentListActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                }
            }
        });
        printerRcList.setAdapter(findPrinterRcAdapter);

        xRefreshView.setXRefreshViewListener(new SimpleXRefreshListener() {

            @Override
            public void onRefresh() {
                Logger.i("onRefresh");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pageCount = 1;
                        findPrinterRcAdapter.clear();
                        ruqueryPrinters(REFRESH);
                    }
                }, DELAY);
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                Logger.i("onLoadMore");
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        ruqueryPrinters(LOADMORE);
                    }
                }, DELAY);
            }
        });
    }

    private void initSpinner() {
        String[] mStringArray = getResources().getStringArray(R.array.distance);
        DisTextArrayAdapter mAdapter = new DisTextArrayAdapter(context, mStringArray);
        spinner.setAdapter(mAdapter);
        spinner.setSelection(1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String[] dis = getResources().getStringArray(R.array.distance);
                Logger.i(pos);
                switch (pos) {
                    case 0:
                        radius = 1 * 1000;
                        break;
                    case 1:
                        radius = 5 * 1000;
                        break;
                    case 2:
                        radius = 10 * 1000;
                        break;
                    case 3:
                        radius = 50 * 1000;
                        break;
                }
                xRefreshView.startRefresh();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || isLoaded) {
            return;
        }
        location();
        findPrinterRcAdapter.setPrinterList(printerList);
        findPrinterRcAdapter.notifyDataSetChanged();
        isLoaded = true;
    }

    private void turnDetail(String printerNo) {
        ((PickPrinterActivity) getActivity()).requeryPrice(printerNo);
    }

    private void ruqueryPrinters(final int type) {
        PrintRequest.queryAroundPrinter(mActivity, pageCount, pageSize, radius, center, new HttpListener() {
            @Override
            public void onSucceed(String content) {
                AroundPrinterResp aroundPrinterResp = GsonUtil.GsonToBean(content, AroundPrinterResp.class);
                if (aroundPrinterResp != null && aroundPrinterResp.isSuccess()) {
                    printerList = aroundPrinterResp.getData().getList();
                    if (printerList != null && !printerList.isEmpty()) {
                        pageCount++;
                        findPrinterRcAdapter.insert(printerList);
                    } else if (type == REFRESH) {
                        ShowUtil.showToast("附近没有打印机");
                    }
                }
                findPrinterRcAdapter.notifyDataSetChanged();
                if (printerList.size() < 20) {
                    xRefreshView.setLoadComplete(true);
                } else {
                    xRefreshView.setLoadComplete(false);
                }
                if (type == REFRESH) {
                    xRefreshView.stopRefresh();
                } else {
                    xRefreshView.stopLoadMore(false);
                }
            }

            @Override
            public void onFailed(String exception) {
                ShowUtil.showToast(getString(R.string.net_error));
                if (type == REFRESH) {
                    xRefreshView.stopRefresh();
                } else {
                    xRefreshView.stopLoadMore(false);
                }
            }
        });
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


    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (null != aMapLocation) {
            if (aMapLocation.getErrorCode() == 0) {
                initSpinner();
                center = aMapLocation.getLongitude() + "," + aMapLocation.getLatitude();
                Logger.i(aMapLocation.toString());
                addressTv.setText(getAddress(aMapLocation));
                xRefreshView.setVisibility(View.VISIBLE);
                xRefreshView.startRefresh();
            } else {
                Logger.i("errorcode" + aMapLocation.getErrorCode());
                xRefreshView.setVisibility(View.INVISIBLE);
                addressTv.setText("定位失败");
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    // latitude=31.999238#longitude=118.7909#province=江苏省#city=南京市#district=秦淮区#cityCode=025#adCode=320104#address
    // =江苏省南京市秦淮区养虎仓靠近普天科技园#country=中国#road=养虎仓#poiName=普天科技园#street=养虎仓#streetNum=34号#aoiName=普天科技园#poiid=#floor
    // =#errorCode=0#errorInfo=success#locationDetail=-5 #csid:cb39c0ecc94f4c46a2c21945cdb53bbd-5
    // #csid:2516c70b42f345dd90b7fc59344d4313#locationType=4
    private String getAddress(AMapLocation aMapLocation) {
        return aMapLocation.getCity() + aMapLocation.getDistrict() + aMapLocation.getStreet() + aMapLocation
                .getStreetNum() + aMapLocation.getAoiName();
    }
}

