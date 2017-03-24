package huanxing_print.com.cn.printhome.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import huanxing_print.com.cn.printhome.R;

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

    public MyBillItemAdapter(Context ctx) {
        this.ctx = ctx;
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

        public MyHolder(View itemView) {
            super(itemView);

            rv_bill_item = (RelativeLayout) itemView.findViewById(R.id.rl_bill_item);

        }
    }
}
