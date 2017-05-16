package huanxing_print.com.cn.printhome.ui.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.model.my.OrderListBean;

/**
 * Created by Administrator on 2017/3/24 0024.
 */

public class MyBillAdapter extends BaseAdapter {

    private Context ctx;
    private List<OrderListBean> list;

    public MyBillAdapter(Context ctx, List<OrderListBean> list) {
        this.ctx = ctx;
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
            convertView = LayoutInflater.from(ctx).inflate(R.layout.activity_bill_detail, null);
            holder = new MyViewHolder();
            holder.tv_bill_time = (TextView) convertView.findViewById(R.id.tv_bill_time);
            holder.rv_item_bill = (RecyclerView) convertView.findViewById(R.id.rv_item_bill);

            convertView.setTag(holder);
        } else {
            holder = (MyViewHolder) convertView.getTag();
        }

        OrderListBean dataBean = list.get(position);
        String month = dataBean.getMonth();
        holder.tv_bill_time.setText(month);

        List<OrderListBean.PrintListBean> printList = dataBean.getPrintList();
        MyBillItemAdapter adapter = new MyBillItemAdapter(ctx, printList);
        holder.rv_item_bill.setLayoutManager(new LinearLayoutManager(ctx));
        holder.rv_item_bill.setAdapter(adapter);
        holder.rv_item_bill.setNestedScrollingEnabled(false);
        return convertView;
    }

    public class MyViewHolder {
        TextView tv_bill_time;
        RecyclerView rv_item_bill;
    }
}
