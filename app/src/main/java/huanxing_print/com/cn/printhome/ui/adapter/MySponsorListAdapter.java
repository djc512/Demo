package huanxing_print.com.cn.printhome.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.util.OnItemClickListener;

/**
 * 我发起的列表Adapter
 * Created by dd on 2017/5/6.
 */

public class MySponsorListAdapter extends RecyclerView.Adapter<MySponsorListAdapter.DefaultViewHolder> {

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    @Override
    public DefaultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DefaultViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.my_sponsor_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(DefaultViewHolder holder, int position) {
        holder.setOnItemClickListener(onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    static class DefaultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        OnItemClickListener onItemClickListener;
        TextView txt_name_type;
        TextView txt_time;
        TextView txt_status;
        TextView txt_use_type;
        TextView txt_buy_form;

        public DefaultViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            txt_name_type = (TextView) itemView.findViewById(R.id.txt_name_type);
            txt_time = (TextView) itemView.findViewById(R.id.txt_time);
            txt_status = (TextView) itemView.findViewById(R.id.txt_status);
            txt_use_type = (TextView) itemView.findViewById(R.id.txt_use_type);
            txt_buy_form = (TextView) itemView.findViewById(R.id.txt_buy_form);
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(getAdapterPosition());
            }
        }
    }
}
