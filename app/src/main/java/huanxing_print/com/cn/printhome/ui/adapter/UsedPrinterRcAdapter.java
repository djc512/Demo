package huanxing_print.com.cn.printhome.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.model.print.UsedPrinterResp;

/**
 * Created by LGH on 2017/3/23.
 */

public class UsedPrinterRcAdapter extends RecyclerView.Adapter<UsedPrinterRcAdapter.ViewHolder> {

    private List<UsedPrinterResp.Printer> printerList;

    public UsedPrinterRcAdapter(List<UsedPrinterResp.Printer> printerList) {
        this.printerList = printerList;
    }

    public void setPrinterList(List<UsedPrinterResp.Printer> printerList) {
        this.printerList = printerList;
    }

    public OnItemClickListener itemClickListener;


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private LinearLayout printerLyt;
        private ImageView typeImg;
        private TextView nameTv;
        private TextView detailTv;

        public ViewHolder(View itemView) {
            super(itemView);
            printerLyt = (LinearLayout) itemView.findViewById(R.id.printerLyt);
            typeImg = (ImageView) itemView.findViewById(R.id.typeImg);
            nameTv = (TextView) itemView.findViewById(R.id.nameTv);
            detailTv = (TextView) itemView.findViewById(R.id.detailTv);
            printerLyt.setOnClickListener(this);
            typeImg.setOnClickListener(this);
            nameTv.setOnClickListener(this);
            detailTv.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(v, getLayoutPosition());
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_printer_used_list, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
//        viewHolder.nameTv.setText("aaaaa");
    }

    @Override
    public int getItemCount() {
        return printerList.size();
    }
}
