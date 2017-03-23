package huanxing_print.com.cn.printhome.ui.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.model.my.ChongZhiRecordBean;

/**
 * Created by Administrator on 2017/3/23 0023.
 */

public class AccountRecordAdapter extends RecyclerView.Adapter<AccountRecordAdapter.MyViewHolder>{

    private Context ctx;
//    private List<ChongZhiRecordBean.DataBean> list = new ArrayList<>();
    private ChongZhiRecordBean.DataBean dataBean;

    public AccountRecordAdapter(Context ctx, List<ChongZhiRecordBean.DataBean> list) {
        this.ctx = ctx;
//        this.list = list;
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                ctx).inflate(R.layout.item_account_record, parent,
                false));

        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
//        List<ChongZhiRecordBean.DataBean.ListBean> listBean = dataBean.getList();
//        ChongZhiRecordBean.DataBean.ListBean listBean1 = listBean.get(position);
//        holder.tv_time.setText(listBean.get(position).getDate());
//        List<ChongZhiRecordBean.DataBean.ListBean.DetailBean> details = listBean1.getDetail();

        holder.rv_item_record.setLayoutManager(new LinearLayoutManager(ctx));
        holder.rv_item_record.setAdapter(new AccountRecordItemAdapter(ctx,null));
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
    public int getItemCount() {
        return 3;
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
