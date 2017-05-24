package huanxing_print.com.cn.printhome.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.model.approval.ApprovalObject;

/**
 * 我发起的列表Adapter
 * Created by dd on 2017/5/6.
 */

public class MySponsorListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ApprovalObject> objects;

    public MySponsorListAdapter(Context context, ArrayList<ApprovalObject> objects) {
        this.context = context;
        this.objects = objects;
    }

    @Override
    public int getCount() {
        return objects.size();
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
            holder.txt_name = (TextView) convertView.findViewById(R.id.txt_name);
            holder.lin_middle = (LinearLayout) convertView.findViewById(R.id.lin_middle);
            holder.lin_bottom = (LinearLayout) convertView.findViewById(R.id.lin_bottom);
            convertView.setTag(holder);
        } else {
            holder = (DefaultViewHolder) convertView.getTag();
        }
        int type = objects.get(position).getType();
        if (1 == type) {
            //采购
            holder.lin_middle.setVisibility(View.VISIBLE);
            holder.lin_bottom.setVisibility(View.VISIBLE);
            holder.txt_name_type.setText(objects.get(position).getApproveTitle());
            holder.txt_name.setText("用途说明:");
            holder.txt_use_type.setText(objects.get(position).getTitle());
        } else if (2 == type) {
            //报销
            holder.lin_middle.setVisibility(View.VISIBLE);
            holder.lin_bottom.setVisibility(View.INVISIBLE);
            holder.txt_name_type.setText(objects.get(position).getApproveTitle());
            holder.txt_name.setText("报销金额总计:");
            holder.txt_use_type.setText(objects.get(position).getAmountMonney());
        }
        holder.txt_time.setText(objects.get(position).getAddTime());
        holder.txt_buy_form.setText(objects.get(position).getPurchaseList());
        //根据状态码来显示不同的状态
        int status = objects.get(position).getStatus();
        if (0 == status) {
            //审批中
            holder.txt_status.setText(R.string.status_zero);
            holder.txt_status.setTextColor(context.getResources().getColor(R.color.color_status_zero));
        } else if (2 == status) {
            //审批完成
            holder.txt_status.setText(R.string.status_two);
            holder.txt_status.setTextColor(context.getResources().getColor(R.color.color_status_two));
        } else if (3 == status) {
            //已驳回
            holder.txt_status.setText(R.string.status_three);
            holder.txt_status.setTextColor(context.getResources().getColor(R.color.color_status_three));
        } else if (4 == status) {
            //已撤销
            holder.txt_status.setText(R.string.status_four);
            holder.txt_status.setTextColor(context.getResources().getColor(R.color.color_status_four));
        } else if (5 == status) {
            //打印凭证
            holder.txt_status.setText(R.string.status_two);
            holder.txt_status.setTextColor(context.getResources().getColor(R.color.color_status_two));
        } else if (6 == status) {
            //已打印
            holder.txt_status.setText(R.string.status_six);
            holder.txt_status.setTextColor(context.getResources().getColor(R.color.color_status_six));
        }
        return convertView;
    }

    static class DefaultViewHolder {
        TextView txt_name_type;
        TextView txt_time;
        TextView txt_status;
        TextView txt_use_type;
        TextView txt_buy_form;
        TextView txt_name;
        LinearLayout lin_middle;
        LinearLayout lin_bottom;
    }
}
