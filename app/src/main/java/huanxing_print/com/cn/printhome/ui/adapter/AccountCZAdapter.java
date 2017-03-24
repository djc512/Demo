package huanxing_print.com.cn.printhome.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.model.my.ChongZhiBean;

/**
 * Created by Administrator on 2017/3/23 0023.
 */

public class AccountCZAdapter extends RecyclerView.Adapter<AccountCZAdapter.MyViewHolder> {

    private Context ctx;
    private List<ChongZhiBean> list = new ArrayList<>();
    private int clickTemp = -1;

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
    //标识选择的Item
    public void setSeclection(int position) {
        clickTemp = position;
    }

    public AccountCZAdapter(Context ctx, List<ChongZhiBean> list) {
        this.ctx = ctx;
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                ctx).inflate(R.layout.item_cz_money, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
//        ChongZhiBean bean = list.get(position);
//
//        holder.tv_chong.setText(bean.getRechargeAmout());
//        holder.tv_song.setText(bean.getSendAmount());

        if (clickTemp == position) {
            holder.ll_cz.setBackgroundResource(R.drawable.shape_cz_money_green);
            holder.tv_chong.setTextColor(ctx.getResources().getColor(R.color.green));
            holder.tv_song.setTextColor(ctx.getResources().getColor(R.color.green));
        } else {
            holder.ll_cz.setBackgroundResource(R.drawable.shape_cz_money_bg);
            holder.tv_chong.setTextColor(Color.parseColor("#000000"));
            holder.tv_song.setTextColor(Color.parseColor("#000000"));
        }

        if (mOnItemClickLitener != null) {
            holder.ll_cz.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(holder.ll_cz, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_chong;
        TextView tv_song;
        LinearLayout ll_cz;

        public MyViewHolder(View view) {
            super(view);
            ll_cz = (LinearLayout) view.findViewById(R.id.ll_account_cz);
            tv_chong = (TextView) view.findViewById(R.id.tv_account_chong);
            tv_song = (TextView) view.findViewById(R.id.tv_account_song);
        }
    }
}
