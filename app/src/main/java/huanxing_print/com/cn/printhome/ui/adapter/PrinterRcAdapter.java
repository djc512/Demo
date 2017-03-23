package huanxing_print.com.cn.printhome.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.model.print.Printer;

/**
 * Created by LGH on 2017/3/23.
 */

public class PrinterRcAdapter extends RecyclerView.Adapter<PrinterRcAdapter.ViewHolder> {

    private List<Printer> printerList;

    public PrinterRcAdapter(List<Printer> printerList) {
        this.printerList = printerList;
    }

    public void setPrinterList(List<Printer> printerList) {
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

        public TextView textView;
        public RelativeLayout rlyt;
        public ImageView icImg;


        public ViewHolder(View itemView) {
            super(itemView);
            rlyt = (RelativeLayout) itemView.findViewById(R.id.rlayt);
            textView = (TextView) itemView.findViewById(R.id.nameTv);
            icImg = (ImageView) itemView.findViewById(R.id.icImg);
            textView.setOnClickListener(this);
            icImg.setOnClickListener(this);
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
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_filelist, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.textView.setText("aaaaa");
    }

    @Override
    public int getItemCount() {
        return printerList.size();
    }
}
