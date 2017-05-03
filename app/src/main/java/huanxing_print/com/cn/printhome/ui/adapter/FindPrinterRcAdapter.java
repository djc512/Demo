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
import huanxing_print.com.cn.printhome.model.print.Printer;

/**
 * Created by LGH on 2017/5/3.
 */

public class FindPrinterRcAdapter extends RecyclerView.Adapter<FindPrinterRcAdapter.ViewHolder> {

    private List<Printer> printerList;

    public FindPrinterRcAdapter(List<Printer> printerList) {
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

        private LinearLayout printerLyt;
        private ImageView navImg;
        private ImageView typeImg;
        private TextView nameTv;
        private TextView detailTv;
        private TextView commentTv;
        private TextView printCountTv;

        public ViewHolder(View itemView) {
            super(itemView);
            printerLyt = (LinearLayout) itemView.findViewById(R.id.printerLyt);
            typeImg = (ImageView) itemView.findViewById(R.id.typeImg);
            navImg = (ImageView) itemView.findViewById(R.id.navImg);
            nameTv = (TextView) itemView.findViewById(R.id.nameTv);
            detailTv = (TextView) itemView.findViewById(R.id.detailTv);
            printCountTv = (TextView) itemView.findViewById(R.id.printCountTv);
            commentTv = (TextView) itemView.findViewById(R.id.commentTv);
            printerLyt.setOnClickListener(this);
            nameTv.setOnClickListener(this);
            detailTv.setOnClickListener(this);
            printCountTv.setOnClickListener(this);
            commentTv.setOnClickListener(this);
            navImg.setOnClickListener(this);
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
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_printer_find_list, viewGroup, false);
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

