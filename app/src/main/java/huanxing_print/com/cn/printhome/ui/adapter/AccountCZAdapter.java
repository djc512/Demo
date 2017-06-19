package huanxing_print.com.cn.printhome.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.model.my.ChongZhiBean;
import huanxing_print.com.cn.printhome.util.ObjectUtils;

import static huanxing_print.com.cn.printhome.R.id.tv_account_song;

/**
 * Created by Administrator on 2017/3/23 0023.
 */

public class AccountCZAdapter extends RecyclerView.Adapter<AccountCZAdapter.MyViewHolder> {

    private Context ctx;
    private int clickTemp = -1;
    private List<ChongZhiBean> data;

    public interface OnItemClickLitener {
        void onItemClick(ImageView view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    //标识选择的Item
    public void setSeclection(int position) {
        clickTemp = position;
    }

    public AccountCZAdapter(Context ctx, List<ChongZhiBean> data) {
        this.ctx = ctx;
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                ctx).inflate(R.layout.item_account_money, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        ChongZhiBean bean = data.get(position);
        holder.tv_chong.setText("充￥" + bean.getRechargeAmout());
        holder.tv_song.setText("送￥" + bean.getSendAmount());
        String blackPrice = bean.getBlackPrice();
        String colorPrice = bean.getColorPrice();
        if(!ObjectUtils.isNull(blackPrice)) {
            holder.tv_blackprice.setText("黑白:￥" + blackPrice + "/页");
        }
        if(!ObjectUtils.isNull(colorPrice)) {
            holder.tv_colorprice.setText("彩色:￥" +colorPrice + "/页");
        }
        if (clickTemp == position) {
            holder.rl_cz.setBackgroundResource(R.drawable.broder_line_yellow);
            holder.iv_check.setVisibility(View.VISIBLE);
        } else {
            holder.rl_cz.setBackgroundResource(R.drawable.broder_line);
            holder.iv_check.setVisibility(View.GONE);
        }

        if (mOnItemClickLitener != null) {
            holder.rl_cz.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(holder.iv_check, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_chong;
        TextView tv_song;
        RelativeLayout rl_cz;
        TextView tv_blackprice;
        TextView tv_colorprice;
        ImageView iv_check;
        public MyViewHolder(View view) {
            super(view);
            rl_cz = (RelativeLayout) view.findViewById(R.id.rl_cz);
            tv_chong = (TextView) view.findViewById(R.id.tv_account_chong);
            tv_song = (TextView) view.findViewById(tv_account_song);
            tv_blackprice = (TextView) view.findViewById(R.id.tv_blackprice);
            tv_colorprice = (TextView) view.findViewById(R.id.tv_colorprice);
            iv_check = (ImageView) view.findViewById(R.id.iv_check);
        }
    }
}
