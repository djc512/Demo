package huanxing_print.com.cn.printhome.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import huanxing_print.com.cn.printhome.R;

/**
 * Created by LGH on 2017/5/8.
 */

public class ApprovalInfoRecylerAdapter extends RecyclerView.Adapter<ApprovalInfoRecylerAdapter.ViewHolder> {

    private List<ApprovalInfo> chatList;

    public ApprovalInfoRecylerAdapter(List<ApprovalInfo> chatList) {
        this.chatList = chatList;
    }

    public void setChatList(List<ApprovalInfo> chatList) {
        this.chatList = chatList;
    }

    public List getChatList() {
        return chatList;
    }

    public OnItemClickListener itemClickListener;


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private LinearLayout lyt;

        public ViewHolder(View itemView) {
            super(itemView);
            lyt = (LinearLayout) itemView.findViewById(R.id.lyt);
            lyt.setOnClickListener(this);
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
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_approvalinfo_list, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
//        viewHolder.nameTv.setText("aaaaa");
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public static class ApprovalInfo {
        private String title;
        private String date;
        private String detail;
        private String time;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }

}
