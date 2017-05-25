package huanxing_print.com.cn.printhome.ui.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.model.my.ChongZhiRecordListBean;
import huanxing_print.com.cn.printhome.model.my.ChongZhiRecordListDetailBean;

/**
 * Created by Administrator on 2017/3/23 0023.
 */

public class AccountRecordAdapter extends BaseAdapter {
    private Context ctx;
    private List<ChongZhiRecordListBean> list;
    public AccountRecordAdapter(Context ctx, List<ChongZhiRecordListBean> list) {
        this.ctx = ctx;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size() > 0 ? list.size() : 0;
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
        MyHolder holder = null;
        if (null == convertView) {
            convertView = LayoutInflater.from(ctx).inflate(R.layout.item_account_record, parent, false);
            holder = new MyHolder();
            holder.rv_item_bill = (RecyclerView) convertView.findViewById(R.id.rv_item_bill);
            convertView.setTag(holder);

        } else {
            holder = (MyHolder) convertView.getTag();

        }

        List<ChongZhiRecordListDetailBean> printList = list.get(position).getDetail();
        AccountRecordItemAdapter adapter = new AccountRecordItemAdapter(ctx, printList);
        holder.rv_item_bill.setLayoutManager(new LinearLayoutManager(ctx));
        holder.rv_item_bill.setAdapter(adapter);
        holder.rv_item_bill.setNestedScrollingEnabled(false);
//        ChongZhiRecordListDetailBean detail = list.get(position).getList();
//        ChongZhiRecordBean.ListBean.DetailBean detailBean = detail.get(position);
//        holder.tv_rechargerecord_money.setText(detailBean.getAmount());
//        holder.tv_rechargerecord_num.setText("订单编号:" + detailBean.getOrderId());
//        holder.tv_rechargerecord_time.setText(detailBean.getDate());
        return convertView;
    }

    public class MyHolder {
        RecyclerView rv_item_bill;
    }
}
