package huanxing_print.com.cn.printhome.ui.activity.print.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.log.Logger;
import huanxing_print.com.cn.printhome.model.print.PrintInfoResp;
import huanxing_print.com.cn.printhome.ui.activity.copy.CommentListActivity;
import huanxing_print.com.cn.printhome.ui.activity.print.PickPrinterActivity;
import huanxing_print.com.cn.printhome.util.PriceUtil;
import huanxing_print.com.cn.printhome.util.PrintUtil;

/**
 * Created by LGH on 2017/5/4.
 */

public class PrinterDetailFragment extends Fragment {

    protected View view;
    private ImageView typeImg;
    private TextView switchTv;
    private TextView nextTv;
    private TextView addressTv;
    private TextView typeTv;
    private TextView backPriceTv;
    private TextView colorPriceTv;
    private TextView nameTv;
    private TextView resolutionTv;
    private TextView technicalTypeTv;
    private TextView remainTv;
    private TextView photoRemainTv;
    private LinearLayout colorLyt;
    private TextView photoPriceTv;

    public PrintInfoResp.PrinterPrice getPrintPrinterPrice() {
        return printPrinterPrice;
    }

    public void setPrintPrinterPrice(PrintInfoResp.PrinterPrice printPrinterPrice) {
        this.printPrinterPrice = printPrinterPrice;
    }

    private PrintInfoResp.PrinterPrice printPrinterPrice;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Logger.i("onCreateView");
        view = inflater.inflate(R.layout.fragment_printer_detail, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        photoPriceTv = (TextView) view.findViewById(R.id.photoPriceTv);
        colorLyt = (LinearLayout) view.findViewById(R.id.colorLyt);
        photoRemainTv = (TextView) view.findViewById(R.id.photoRemainTv);
        remainTv = (TextView) view.findViewById(R.id.remainTv);
        typeImg = (ImageView) view.findViewById(R.id.typeImg);
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
                ((PickPrinterActivity) getActivity()).turnSetting(printPrinterPrice.getPrinterNo());
            }
        });
        view.findViewById(R.id.commentRyt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("printer_id", printPrinterPrice.getPrinterNo());
                Intent intent = new Intent(getActivity(), CommentListActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        if (printPrinterPrice != null) {
            updateView(printPrinterPrice);
        }
    }

    public void updateView(PrintInfoResp.PrinterPrice printerPrice) {
        Logger.i("updateView");
        this.printPrinterPrice = printerPrice;
        nameTv.setText(printerPrice.getPrintName());
        addressTv.setText("地址：" + printerPrice.getPrintAddress());
        if (PrintUtil.PRINTER_TYPE_COLOR.equals(printerPrice.getPrinterType())) {
            typeImg.setImageResource(R.drawable.ic_colorized);
            colorLyt.setVisibility(View.VISIBLE);
            colorPriceTv.setText("彩色 A4 ￥" + printerPrice.getA4ColorPrice() + "   A3 ￥" + printerPrice
                    .getA3ColorPrice());
            photoRemainTv.setVisibility(View.VISIBLE);
            photoRemainTv.setText("相片纸 " + printerPrice.getPhotoNum() + "张");
            photoPriceTv.setVisibility(View.VISIBLE);
            photoPriceTv.setText("相片纸 ￥"+ PriceUtil.getPhotoPriceStr(printerPrice));
        } else {
            typeImg.setImageResource(R.drawable.ic_black);
            photoPriceTv.setVisibility(View.GONE);
            colorLyt.setVisibility(View.GONE);
            photoRemainTv.setVisibility(View.GONE);
        }
        backPriceTv.setText("黑色 A4 ￥" + printerPrice.getA4BlackPrice() + "   A3 ￥" + printerPrice
                .getA3BlackPrice());
        typeTv.setText(printerPrice.getCapability());
        resolutionTv.setText(printerPrice.getResolution());
        technicalTypeTv.setText(printerPrice.getTechnicalType());
        remainTv.setText("A4 " + printerPrice.getA4Num() + "张 A3 " + printerPrice.getA3Num() + "张");
    }
}
