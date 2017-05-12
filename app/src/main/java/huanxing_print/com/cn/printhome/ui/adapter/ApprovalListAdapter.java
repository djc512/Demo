package huanxing_print.com.cn.printhome.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.model.approval.ApprovalObject;

/**
 * Created by Administrator on 2017/3/23 0023.
 */

public class ApprovalListAdapter extends BaseAdapter {
    private Context ctx;
    private List<ApprovalObject> list;

    public ApprovalListAdapter(Context ctx, List<ApprovalObject> list) {
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
            convertView = LayoutInflater.from(ctx).inflate(R.layout.item_approval, null);
            holder = new MyViewHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_use = (TextView) convertView.findViewById(R.id.tv_use);
            holder.tv_detail = (TextView) convertView.findViewById(R.id.tv_detail);
            holder.tv_state = (TextView) convertView.findViewById(R.id.tv_state);

            convertView.setTag(holder);
        } else {
            holder = (MyViewHolder) convertView.getTag();
        }
        ApprovalObject listBean = list.get(position);
        holder.tv_name.setText(listBean.getJobNumber());
        holder.tv_time.setText(listBean.getAddTime());
        holder.tv_use.setText(listBean.getTitle());
        holder.tv_detail.setText(listBean.getPurchaseList());
        return convertView;
    }

    public class MyViewHolder {
        TextView tv_name;
        TextView tv_time;
        TextView tv_use;
        TextView tv_detail;
        TextView tv_state;
    }
}
