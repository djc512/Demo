package huanxing_print.com.cn.printhome.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.model.approval.ApprovalObject;

/**
 * 抄送我的的列表Adapter
 * Created by dd on 2017/5/6.
 */

public class CopyToMeAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ApprovalObject> objects;

    public CopyToMeAdapter(Context context, ArrayList<ApprovalObject> objects) {
        this.context = context;
        this.objects = objects;
    }

    //    @Override
//    public int getCount() {
//        return objects.size();
//    }
    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DefaultViewHolder holder;
        if (convertView == null) {
            holder = new DefaultViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.my_sponsor_list_item, null);
            holder.txt_name_type = (TextView) convertView.findViewById(R.id.txt_name_type);
            holder.txt_time = (TextView) convertView.findViewById(R.id.txt_time);
            holder.txt_status = (TextView) convertView.findViewById(R.id.txt_status);
            holder.txt_use_type = (TextView) convertView.findViewById(R.id.txt_use_type);
            holder.txt_buy_form = (TextView) convertView.findViewById(R.id.txt_buy_form);
            convertView.setTag(holder);
        } else {
            holder = (DefaultViewHolder) convertView.getTag();
        }
//        holder.txt_name_type.setText(objects.get(position).getJobNumber());
//        holder.txt_time.setText(objects.get(position).getAddTime());
//        //根据状态码来显示不同的状态
//        //holder.txt_status.setText(objects.get(position).getStatus()+"");
//        holder.txt_use_type.setText(objects.get(position).getTitle());
//        holder.txt_name_type.setText(objects.get(position).getPurchaseList());
        return convertView;
    }

    static class DefaultViewHolder {
        TextView txt_name_type;
        TextView txt_time;
        TextView txt_status;
        TextView txt_use_type;
        TextView txt_buy_form;
    }
}
