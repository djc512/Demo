package huanxing_print.com.cn.printhome.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.model.my.MingxiDetailBean;
import huanxing_print.com.cn.printhome.ui.activity.my.BillDetailActivity;

/**
 * Created by Administrator on 2017/3/24 0024.
 */

public class MyBillAdapter extends BaseAdapter{

    private Context ctx;
    private  List<MingxiDetailBean.ListBean> list;

    public MyBillAdapter(Context ctx, List<MingxiDetailBean.ListBean> list) {
        this.ctx =ctx;
        this.list = list;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(ctx).inflate(R.layout.activity_bill_detail,null);
            holder = new MyViewHolder();
            holder.tv_bill_time = (TextView) convertView.findViewById(R.id.tv_bill_time);
            holder.tv_bill_consume = (TextView) convertView.findViewById(R.id.tv_bill_consume);
            holder.rv_item_bill = (RecyclerView) convertView.findViewById(R.id.rv_item_bill);

            convertView.setTag(holder);
        }else {
            holder = (MyViewHolder) convertView.getTag();
        }
        MingxiDetailBean.ListBean listBean = list.get(position);
        String date = listBean.getDate();
        holder.tv_bill_time.setText(date);
        String monthAmount = listBean.getMonthAmount();
        holder.tv_bill_consume.setText(monthAmount);
        List<MingxiDetailBean.ListBean.DetailBean> detail = listBean.getDetail();

        MyBillItemAdapter adapter = new MyBillItemAdapter(ctx,detail);
        holder.rv_item_bill.setLayoutManager(new LinearLayoutManager(ctx));
        holder.rv_item_bill.setAdapter(adapter);
        holder.rv_item_bill.setNestedScrollingEnabled(false);
        adapter.setOnItemClickLitener(new MyBillItemAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(int position) {

                Intent intent = new Intent(ctx,BillDetailActivity.class);
                ctx.startActivity(intent);
            }
        });

        return convertView;
    }
    public class MyViewHolder {
        TextView tv_bill_time;
        TextView tv_bill_consume;
        RecyclerView rv_item_bill;
    }
}
