package huanxing_print.com.cn.printhome.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;

import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.log.Logger;
import huanxing_print.com.cn.printhome.model.print.AroundPrinterResp;
import huanxing_print.com.cn.printhome.util.PrintUtil;
import huanxing_print.com.cn.printhome.util.StringUtil;

/**
 * Created by LGH on 2017/5/3.
 */

public class FindPrinterRcAdapter extends BaseRecyclerAdapter<FindPrinterRcAdapter.ViewHolder> {

    private List<AroundPrinterResp.Printer> mPrinterList;

    public FindPrinterRcAdapter(List<AroundPrinterResp.Printer> printerList) {
        this.mPrinterList = printerList;
    }

    public void setPrinterList(List<AroundPrinterResp.Printer> printerList) {
        this.mPrinterList = printerList;
    }

    public List<AroundPrinterResp.Printer> getPrinterList() {
        return mPrinterList;
    }

    public OnItemClickListener itemClickListener;

    public void clear() {
        mPrinterList.clear();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void insert(AroundPrinterResp.Printer printer, int position) {
        insert(mPrinterList, printer, position);
    }

    public void insert(List<AroundPrinterResp.Printer> printerList) {
        for (AroundPrinterResp.Printer printer : printerList) {
            insert(mPrinterList, printer, getAdapterItemCount());
        }
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public LinearLayout printerLyt;
        public LinearLayout navLty;
        public ImageView typeImg;
        public TextView nameTv;
        public TextView addressTv;
        public TextView disTv;
        public TextView detailTv;
        public TextView commentTv;
        public TextView printCountTv;

        public ViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                printerLyt = (LinearLayout) itemView.findViewById(R.id.printerLyt);
                navLty = (LinearLayout) itemView.findViewById(R.id.navLty);
                typeImg = (ImageView) itemView.findViewById(R.id.typeImg);
                nameTv = (TextView) itemView.findViewById(R.id.nameTv);
                detailTv = (TextView) itemView.findViewById(R.id.detailTv);
                addressTv = (TextView) itemView.findViewById(R.id.addressTv);
                disTv = (TextView) itemView.findViewById(R.id.disTv);
                printCountTv = (TextView) itemView.findViewById(R.id.printCountTv);
                commentTv = (TextView) itemView.findViewById(R.id.commentTv);
                printerLyt.setOnClickListener(this);
//                navLty.setOnClickListener(this);
                nameTv.setOnClickListener(this);
                detailTv.setOnClickListener(this);
                commentTv.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(v, getLayoutPosition());
            }
        }
    }

//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
//        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_printer_find_list, viewGroup,
// false);
//        return new ViewHolder(v);
//    }

    @Override
    public ViewHolder getViewHolder(View view) {
        return new ViewHolder(view, false);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_printer_find_list, parent, false);
        return new ViewHolder(v, isItem);
    }

    //{"address":"江苏省无锡市黄家庵区东瓜匙路与明匙路交叉口西南10米","distance":"140","isOnline":"true","location":"118.79076,32.000463",
    // "name":"测试-医疗机09号","pageCount":"388","printerNo":"zwf002","printerType":"0","remarkCount":"14"}
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position, boolean isItem) {
        AroundPrinterResp.Printer printer = mPrinterList.get(position);
        viewHolder.nameTv.setText(printer.getName());
        viewHolder.addressTv.setText(printer.getAddress());
        viewHolder.disTv.setText(StringUtil.getDistance(printer.getDistance()));
        Logger.i(StringUtil.getDistance(printer.getDistance()));
        viewHolder.commentTv.setText("评论 " + printer.getRemarkCount());
        viewHolder.printCountTv.setText("已打印 " + printer.getPageCount());
        if (PrintUtil.PRINTER_TYPE_COLOR.equals(printer.getPrinterType())) {
            viewHolder.typeImg.setImageResource(R.drawable.ic_colorized);
        } else {
            viewHolder.typeImg.setImageResource(R.drawable.ic_black);
        }
    }

    @Override
    public int getAdapterItemCount() {
        return mPrinterList.size();
    }
}

