package huanxing_print.com.cn.printhome.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.util.Info;

/**
 * Created by Administrator on 2017/3/23 0023.
 */

public class ApprovalPersonAdapter extends BaseAdapter {
    private Context ctx;
    private List<Info> list;

    public ApprovalPersonAdapter(Context ctx, List<Info> list) {
        this.ctx = ctx;
        this.list = list;
    }

    @Override
    public int getCount() {
        return 3;
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
            convertView = LayoutInflater.from(ctx).inflate(R.layout.item_approval_person,null);
            holder = new MyViewHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.iv_user_head = (ImageView) convertView.findViewById(R.id.iv_user_head);
            holder.iv_isapproval = (ImageView) convertView.findViewById(R.id.iv_isapproval);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_detail = (TextView) convertView.findViewById(R.id.tv_detail);

            convertView.setTag(holder);
        }else {
           holder = (MyViewHolder) convertView.getTag();
        }

        //Info info = list.get(position);

        /*BitmapUtils.displayImage(ctx,info.getUsePic(),holder.iv_user_head);
        //holder.iv_user_head.setImageResource();
        holder.tv_name.setText(info.getName());
        //holder.iv_isapproval
        holder.tv_time.setText(info.getTime());
        holder.tv_detail.setText(info.getDetail());*/

        return convertView;
    }
    public class MyViewHolder{
        TextView tv_name;
        TextView tv_time;
        ImageView iv_user_head;
        ImageView iv_isapproval;
        TextView tv_detail;//发起申请，审批中，审批完成
    }
}
