package huanxing_print.com.cn.printhome.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
    private List<MingxiDetailBean.ListBean.DetailBean> detail;

    public MyBillItemAdapter(Context ctx, List<MingxiDetailBean.ListBean.DetailBean> detail) {
        this.ctx = ctx;
        this.detail = detail;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyHolder holder = new MyHolder(LayoutInflater.from(
                ctx).inflate(R.layout.activity_bill_detail_item1, parent,
                false));
        return holder;
    }

    private boolean isShow;

    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {

        MingxiDetailBean.ListBean.DetailBean detailBean = detail.get(position);
        String date = detailBean.getDate();
        String context = detailBean.getContext();
        String amount = detailBean.getAmount();

        holder.tv_bill_czway.setText(context);
        holder.tv_bill_time.setText(date);
        holder.tv_bill_money.setText(amount);

        holder.iv_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShow) {
                    holder.iv_down.setBackgroundResource(R.drawable.select_no);
                    holder.ll_detail.setVisibility(View.VISIBLE);
                } else {
                    holder.iv_down.setBackgroundResource(R.drawable.select);
                    holder.ll_detail.setVisibility(View.GONE);
                }
            }
        });

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
        return detail.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        RelativeLayout rv_bill_item;
        private final TextView tv_bill_czway;
        private final TextView tv_bill_money;
        private final TextView tv_bill_time;
        private final ImageView iv_down;
        private final LinearLayout ll_detail;

        public MyHolder(View itemView) {
            super(itemView);

            rv_bill_item = (RelativeLayout) itemView.findViewById(R.id.rl_bill_item);
            tv_bill_czway = (TextView) itemView.findViewById(R.id.tv_bill_czway);
            tv_bill_money = (TextView) itemView.findViewById(R.id.tv_bill_money);
            tv_bill_time = (TextView) itemView.findViewById(R.id.tv_bill_time);
            iv_down = (ImageView) itemView.findViewById(R.id.iv_down);
            ll_detail = (LinearLayout) itemView.findViewById(R.id.ll_detail);
        }
    }

    /**
     * 动画效果
     */
    private void showAnimation(LinearLayout ll_down) {
        AlphaAnimation aa = new AlphaAnimation(0, 1);
        aa.setDuration(500);
        aa.setFillAfter(true);
        ll_down.startAnimation(aa);
    }

    private void hideAnimation(LinearLayout ll_down) {
        AlphaAnimation aa = new AlphaAnimation(1, 0);
        aa.setDuration(500);
        aa.setFillAfter(true);
        ll_down.startAnimation(aa);
    }
}
