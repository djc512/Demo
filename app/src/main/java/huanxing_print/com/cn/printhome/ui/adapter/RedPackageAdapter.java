package huanxing_print.com.cn.printhome.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.model.yinxin.RedPackageBean;

/**
 * Created by LGH on 2017/5/8.
 */

public class RedPackageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int TYPE_HEADER = 1;
    private final int TYPE_NORMAL = 2;

    private RedPackageBean date;

    public RedPackageAdapter(RedPackageBean date) {
        this.date = date;
    }

    public RedPackageAdapter() {
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == TYPE_HEADER) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_redpackage_list_header,
                    viewGroup,
                    false);
            return new HeaderViewHolder(v);
        } else {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_redpackage_list, viewGroup,
                    false);
            return new NormalViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof HeaderViewHolder) {
//            ((HeaderViewHolder) viewHolder).nameTv.setText("");
        } else if (viewHolder instanceof NormalViewHolder) {
//            ((NormalViewHolder) viewHolder).nameTv.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return 20;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return TYPE_HEADER;
        return TYPE_NORMAL;
    }

    public class NormalViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout luckLyt;
        public CircleImageView circleImageView;
        public TextView nameTv;
        public TextView timeTv;
        public TextView amountTv;

        public NormalViewHolder(View itemView) {
            super(itemView);
            luckLyt = (LinearLayout) itemView.findViewById(R.id.luckLyt);
            circleImageView = (CircleImageView) itemView.findViewById(R.id.circleImageView);
            nameTv = (TextView) itemView.findViewById(R.id.nameTv);
            timeTv = (TextView) itemView.findViewById(R.id.timeTv);
            amountTv = (TextView) itemView.findViewById(R.id.amountTv);
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView circleImageView;
        public TextView nameTv;
        public TextView remarkTv;
        public TextView detailTv;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            circleImageView = (CircleImageView) itemView.findViewById(R.id.circleImageView);
            nameTv = (TextView) itemView.findViewById(R.id.nameTv);
            remarkTv = (TextView) itemView.findViewById(R.id.remarkTv);
            detailTv = (TextView) itemView.findViewById(R.id.detailTv);
        }
    }
}