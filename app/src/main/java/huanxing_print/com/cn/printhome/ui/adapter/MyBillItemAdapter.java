package huanxing_print.com.cn.printhome.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.model.my.MingxiDetailBean;

/**
 * Created by Administrator on 2017/3/24 0024.
 */

public class MyBillItemAdapter extends RecyclerView.Adapter<MyBillItemAdapter.MyHolder> {


    public interface OnItemClickLitener {
        void onItemClick(int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    private Context ctx;
    private List<MingxiDetailBean.DataBean.ListBean.DetailBean> detail = new ArrayList<>();

    public MyBillItemAdapter(Context ctx, List<MingxiDetailBean.DataBean.ListBean.DetailBean> detail) {
        this.ctx = ctx;
        this.detail = detail;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyHolder holder = new MyHolder(LayoutInflater.from(
                ctx).inflate(R.layout.activity_bill_detail_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {

//        MingxiDetailBean.DataBean.ListBean.DetailBean detailBean = detail.get(position);
//        String date = detailBean.getDate();
//        String amount = detailBean.getAmount();
//        int type = detailBean.getType();
//        String context = detailBean.getContext();
//
//        holder.tv_bill_czway.setText(context);
//        holder.tv_bill_time.setText(date);
//        holder.tv_bill_money.setText(amount);

        if (mOnItemClickLitener != null) {
            holder.rv_bill_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        RelativeLayout rv_bill_item;
        private final TextView tv_bill_czway;
        private final TextView tv_bill_money;
        private final TextView tv_bill_time;

        public MyHolder(View itemView) {
            super(itemView);

            rv_bill_item = (RelativeLayout) itemView.findViewById(R.id.rl_bill_item);
            tv_bill_czway = (TextView) itemView.findViewById(R.id.tv_bill_czway);
            tv_bill_money = (TextView) itemView.findViewById(R.id.tv_bill_money);
            tv_bill_time = (TextView) itemView.findViewById(R.id.tv_bill_time);
        }
    }
}
