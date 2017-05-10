package huanxing_print.com.cn.printhome.ui.activity.print.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.simple.eventbus.EventBus;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.model.print.PrintInfoResp;
import huanxing_print.com.cn.printhome.model.print.PrintSetting;
import huanxing_print.com.cn.printhome.ui.activity.print.PickPrinterActivity;
import huanxing_print.com.cn.printhome.util.PrinterUtil;

/**
 * Created by LGH on 2017/5/4.
 */

public class PrinterDetailFragment extends BaseLazyFragment {

    private PrintSetting printSetting;

    private TextView switchTv;
    private TextView nextTv;
    private TextView addressTv;
    private TextView typeTv;
    private TextView backPriceTv;
    private TextView colorPriceTv;
    private TextView nameTv;
    private TextView resolutionTv;
    private TextView technicalTypeTv;
    private PrintInfoResp.Info printInfo;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_printer_detail, container, false);
            initView(view);
            isPrepared = true;
            if (!isLoaded) {
                lazyLoad();
            }
        }
        return view;
    }


    private void initView(View view) {
        technicalTypeTv = (TextView) view.findViewById(R.id.technicalTypeTv);
        resolutionTv = (TextView) view.findViewById(R.id.resolutionTv);
        nameTv = (TextView) view.findViewById(R.id.nameTv);
        backPriceTv = (TextView) view.findViewById(R.id.backPriceTv);
        colorPriceTv = (TextView) view.findViewById(R.id.colorPriceTv);
        addressTv = (TextView) view.findViewById(R.id.addressTv);
        typeTv = (TextView) view.findViewById(R.id.typeTv);
        switchTv = (TextView) view.findViewById(R.id.switchTv);
        nextTv = (TextView) view.findViewById(R.id.nextTv);
        switchTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PickPrinterActivity) getActivity()).showPick();
            }
        });
        nextTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PickPrinterActivity) getActivity()).turnSetting(printInfo.getPrinterNo());
            }
        });
    }

    public void updateView(PrintInfoResp.Info printInfo) {
        this.printInfo = printInfo;
        nameTv.setText(printInfo.getCompanyName());
        addressTv.setText("地址：" + printInfo.getPrintAddress());
        if (PrinterUtil.TYPE_COLOR.equals(printInfo.getPrinterType())) {
            colorPriceTv.setText("彩色 A4 ￥" + printInfo.getA4ColorPrice() + "   A3 ￥" + printInfo.getA3ColorPrice());
        } else {
            colorPriceTv.setVisibility(View.GONE);
        }
        backPriceTv.setText("黑色 A4 ￥" + printInfo.getA4BlackPrice() + "   A3 ￥" + printInfo.getA3BlackPrice());
        typeTv.setText(printInfo.getCapability());
        resolutionTv.setText(printInfo.getResolution());
        technicalTypeTv.setText(printInfo.getTechnicalType());
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || isLoaded) {
            return;
        }

        isLoaded = true;
    }

    public void setPrintSetting(PrintSetting printSetting) {
        this.printSetting = printSetting;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


}
