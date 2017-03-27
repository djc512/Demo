package huanxing_print.com.cn.printhome.ui.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.model.my.ChongZhiRecordBean;

/**
 * Created by Administrator on 2017/3/23 0023.
 */

public class AccountRecordAdapter extends BaseRecyclerAdapter<AccountRecordAdapter.MyViewHolder> {
    private Context ctx;
    private ChongZhiRecordBean.DataBean dataBean;
    private List<ChongZhiRecordBean.DataBean.ListBean> list = new ArrayList<>();

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public AccountRecordAdapter(Context ctx, List<ChongZhiRecordBean.DataBean.ListBean> list) {
        this.ctx = ctx;
        this.list = list;
    }

    @Override
    public MyViewHolder getViewHolder(View view) {
        return new MyViewHolder(view);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                ctx).inflate(R.layout.item_account_record, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position, boolean isItem) {
        ChongZhiRecordBean.DataBean.ListBean listBean = list.get(position);
        String date = listBean.getDate();//充值月份
        holder.tv_time.setText(date);
        List<ChongZhiRecordBean.DataBean.ListBean.DetailBean> detail = listBean.getDetail();

        holder.rv_item_record.setLayoutManager(new LinearLayoutManager(ctx));
        holder.rv_item_record.setAdapter(new AccountRecordItemAdapter(ctx,detail));
        holder.ll_account_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickLitener != null){
                    mOnItemClickLitener.onItemClick(holder.ll_account_record,position);
                }
            }
        });
    }

    @Override
    public int getAdapterItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_time;
        RecyclerView rv_item_record;
        private final LinearLayout ll_account_record;

        public MyViewHolder(View view) {
            super(view);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            rv_item_record = (RecyclerView) view.findViewById(R.id.rv_item_record);
            ll_account_record = (LinearLayout) view.findViewById(R.id.ll_account_record);
        }
    }
}
