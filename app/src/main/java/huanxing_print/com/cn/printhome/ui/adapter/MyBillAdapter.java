package huanxing_print.com.cn.printhome.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.ui.activity.my.BillDetailActivity;

/**
 * Created by Administrator on 2017/3/24 0024.
 */

public class MyBillAdapter extends RecyclerView.Adapter<MyBillAdapter.MyHolder>{

    private Context ctx;
    public MyBillAdapter(Context ctx) {
        this.ctx =ctx;
    }
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyHolder holder = new MyHolder(LayoutInflater.from(
                ctx).inflate(R.layout.activity_bill_detail, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {

        MyBillItemAdapter adapter = new MyBillItemAdapter(ctx);
        holder.rv_item_bill.setLayoutManager(new LinearLayoutManager(ctx));
        holder.rv_item_bill.setAdapter(adapter);

        adapter.setOnItemClickLitener(new MyBillItemAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(int position) {
                ctx.startActivity(new Intent(ctx, BillDetailActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class MyHolder extends RecyclerView.ViewHolder{

        RecyclerView rv_item_bill;

        public MyHolder(View view) {
            super(view);
            rv_item_bill = (RecyclerView) view.findViewById(R.id.rv_item_bill);
        }
    }
}
