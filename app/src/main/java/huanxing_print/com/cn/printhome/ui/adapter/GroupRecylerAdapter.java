package huanxing_print.com.cn.printhome.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.model.print.GroupResp;

/**
 * Created by LGH on 2017/5/12.
 */

public class GroupRecylerAdapter extends RecyclerView.Adapter<GroupRecylerAdapter.ViewHolder> {

    private List<GroupResp.Group> groupList;

    public GroupRecylerAdapter(List<GroupResp.Group> groupList) {
        this.groupList = groupList;
    }

    public void clearData() {
        groupList.clear();
    }

    public void setFileList(List<GroupResp.Group> fileList) {
        this.groupList = fileList;
    }

    public List<GroupResp.Group> getFileList() {
        return groupList;
    }

    public void clear() {
        groupList.clear();
    }

    public OnItemClickListener itemClickListener;

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public RelativeLayout ryt;
        public TextView groupTv;
        public TextView balanceTv;
        public TextView defaultTv;


        public ViewHolder(View itemView) {
            super(itemView);
            ryt = (RelativeLayout) itemView.findViewById(R.id.ryt);
            groupTv = (TextView) itemView.findViewById(R.id.groupTv);
            balanceTv = (TextView) itemView.findViewById(R.id.balanceTv);
            defaultTv = (TextView) itemView.findViewById(R.id.defaultTv);
            ryt.setOnClickListener(this);
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
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_pay_group, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        if (i != 0) {
            viewHolder.defaultTv.setVisibility(View.GONE);
        }
        viewHolder.groupTv.setText(groupList.get(i).getGroupName());
        viewHolder.balanceTv.setText("(" + "￥" + groupList.get(i).getBalance() + ")");
    }


    @Override
    public int getItemCount() {
        return groupList.size();
    }
}