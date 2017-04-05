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
import huanxing_print.com.cn.printhome.model.my.ChongZhiRecordBean;

/**
 * Created by Administrator on 2017/3/23 0023.
 */

public class AccountRecordAdapter extends BaseAdapter {
    private Context ctx;
    private List<ChongZhiRecordBean.ListBean> list;

    public AccountRecordAdapter(Context ctx, List<ChongZhiRecordBean.ListBean> list) {
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
            convertView = LayoutInflater.from(ctx).inflate(R.layout.item_account_record,null);
            holder = new MyViewHolder();
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.rv_item_record = (RecyclerView) convertView.findViewById(R.id.rv_item_record);
            convertView.setTag(holder);
        }else {
           holder = (MyViewHolder) convertView.getTag();
        }
        ChongZhiRecordBean.ListBean listBean = list.get(position);
        String date = listBean.getDate();
        holder.tv_time.setText(date);
        AccountRecordItemAdapter adapter =new AccountRecordItemAdapter(ctx,listBean.getDetail());
        holder.rv_item_record.setLayoutManager(new LinearLayoutManager(ctx));
        holder.rv_item_record.setNestedScrollingEnabled(false);
        holder.rv_item_record.setAdapter(adapter);
//        recyclerView.setNestedScrollingEnabled(false

        return convertView;
    }
    public class MyViewHolder{
        TextView tv_time;
        RecyclerView rv_item_record;
    }
}
