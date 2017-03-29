package huanxing_print.com.cn.printhome.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;

import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.model.my.MingxiDetailBean;
import huanxing_print.com.cn.printhome.ui.activity.my.BillDetailActivity;

/**
 * Created by Administrator on 2017/3/24 0024.
 */

public class MyBillAdapter extends BaseRecyclerAdapter<MyBillAdapter.MyHolder> {

    private Context ctx;
    private  List<MingxiDetailBean.ListBean> list;

    public MyBillAdapter(Context ctx, List<MingxiDetailBean.ListBean> list) {
        this.ctx =ctx;
        this.list = list;
    }

    @Override
    public MyHolder getViewHolder(View view) {
        return new MyHolder(view);
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        MyHolder holder = new MyHolder(LayoutInflater.from(
                ctx).inflate(R.layout.activity_bill_detail, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position, boolean isItem) {

        MingxiDetailBean.ListBean listBean = list.get(position);
        List<MingxiDetailBean.ListBean.DetailBean> detail = listBean.getDetail();
        String monthAmount = listBean.getMonthAmount();
        String date = listBean.getDate();

        holder.tv_bill_time.setText(date);
        holder.tv_bill_consume.setText("累计消费"+monthAmount+"元");

        MyBillItemAdapter adapter = new MyBillItemAdapter(ctx,detail);
        holder.rv_item_bill.setLayoutManager(new LinearLayoutManager(ctx));
        holder.rv_item_bill.setAdapter(adapter);

        adapter.setOnItemClickLitener(new MyBillItemAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(int position) {

                Intent intent = new Intent(ctx,BillDetailActivity.class);
                ctx.startActivity(intent);
            }
        });
    }

    @Override
    public int getAdapterItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{

        RecyclerView rv_item_bill;
        private final TextView tv_bill_time;
        private final TextView tv_bill_consume;

        public MyHolder(View view) {
            super(view);
            rv_item_bill = (RecyclerView) view.findViewById(R.id.rv_item_bill);
            tv_bill_time = (TextView) view.findViewById(R.id.tv_bill_time);
            tv_bill_consume = (TextView) view.findViewById(R.id.tv_bill_consume);
        }
    }
}
