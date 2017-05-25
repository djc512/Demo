package huanxing_print.com.cn.printhome.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.ArrayList;
import java.util.HashMap;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.model.approval.ApprovalPeopleItem;

/**
 * Created by Administrator on 2017/3/23 0023.
 */

public class ApprovalPersonAdapter extends BaseAdapter {
    private Context ctx;
    private ArrayList<ApprovalPeopleItem> list;
    private HashMap<Integer, View> viewMap = new HashMap<Integer, View>();
    private boolean isRevok = false;

    public ApprovalPersonAdapter(Context ctx, ArrayList<ApprovalPeopleItem> list) {
        this.ctx = ctx;
        this.list = list;
    }

    public void modifyApprovalPersons(ArrayList<ApprovalPeopleItem> ls, boolean isRevok) {
        this.list = ls;
        this.isRevok = isRevok;
        notifyDataSetChanged();
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
        if(!viewMap.containsKey(position) || viewMap.get(position) == null) {
            convertView = LayoutInflater.from(ctx).inflate(R.layout.item_approval_person,null);
            holder = new MyViewHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.iv_user_head = (ImageView) convertView.findViewById(R.id.iv_user_head);
            holder.iv_isapproval = (ImageView) convertView.findViewById(R.id.iv_isapproval);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_detail = (TextView) convertView.findViewById(R.id.tv_detail);
            holder.line_top = convertView.findViewById(R.id.vertical_line_top);
            holder.line_bottom = convertView.findViewById(R.id.vertical_line);
            convertView.setTag(holder);
            viewMap.put(position,convertView);
        }else {
            convertView = viewMap.get(position);
           holder = (MyViewHolder) convertView.getTag();
        }

        holder.line_top.setVisibility(View.VISIBLE);
        holder.line_bottom.setVisibility(View.VISIBLE);
        if(0 == position) {
            holder.line_top.setVisibility(View.INVISIBLE);
            if(position == (list.size()-1)) {
                holder.line_bottom.setVisibility(View.INVISIBLE);
            }else{
                holder.line_bottom.setVisibility(View.VISIBLE);
            }
        }else if(position == (list.size()-1)) {
            holder.line_top.setVisibility(View.VISIBLE);
            holder.line_bottom.setVisibility(View.INVISIBLE);
        }

        holder.iv_isapproval.setBackgroundResource(R.drawable.approval_finish);
        //Info info = list.get(position);
        ApprovalPeopleItem  info = list.get(position);
        loadPic(holder.iv_user_head, info.getFaceUrl());
        holder.tv_name.setText(info.getName());
        holder.tv_time.setText(info.getUpdateTime());
        holder.tv_time.setVisibility(View.VISIBLE);
        holder.tv_detail.setVisibility(View.VISIBLE);
        String type = info.getStatus();
        if ("-2".equals(type)) {
            holder.tv_detail.setText("发起申请");
            holder.tv_detail.setTextColor(ctx.getResources().getColor(R.color.green));
        } else if("-1".equals(type)) {
            holder.tv_detail.setText("未开始");
            holder.tv_detail.setTextColor(ctx.getResources().getColor(R.color.text_yellow));
            holder.iv_isapproval.setBackgroundResource(R.drawable.approval_ing);
            holder.tv_time.setVisibility(View.GONE);
            if (isRevok) {
                holder.tv_detail.setVisibility(View.INVISIBLE);
            }else{
                holder.tv_detail.setVisibility(View.VISIBLE);
            }
        }else if("0".equals(type)) {
            holder.tv_detail.setText("审批中");
            holder.tv_detail.setTextColor(ctx.getResources().getColor(R.color.text_yellow));
            holder.iv_isapproval.setBackgroundResource(R.drawable.approval_ing);
            holder.tv_time.setVisibility(View.GONE);
            if (isRevok) {
                Log.e("wanghao","isRevok");
                holder.tv_detail.setVisibility(View.INVISIBLE);
            }else{
                holder.tv_detail.setVisibility(View.VISIBLE);
            }
        }else if("2".equals(type)) {
            holder.tv_detail.setText("已同意");
            holder.tv_detail.setTextColor(ctx.getResources().getColor(R.color.green));
        }else if("3".equals(type)) {
            holder.tv_detail.setText("已拒绝");
            holder.tv_detail.setTextColor(ctx.getResources().getColor(R.color.color_status_three));
            holder.iv_isapproval.setBackgroundResource(R.drawable.approval_bohui);
        }else if("4".equals(type)) {
            holder.tv_detail.setText("申请人撤销");
            holder.tv_detail.setTextColor(ctx.getResources().getColor(R.color.green));
            if (isRevok) {
                holder.tv_detail.setVisibility(View.INVISIBLE);
            }else{
                holder.tv_detail.setVisibility(View.VISIBLE);
            }
        }else if("5".equals(type)) {
            holder.tv_detail.setText("完成-未读");
            holder.tv_detail.setTextColor(ctx.getResources().getColor(R.color.green));
            if (isRevok) {
                holder.tv_detail.setVisibility(View.INVISIBLE);
            }else{
                holder.tv_detail.setVisibility(View.VISIBLE);
            }
        }else if("6".equals(type)) {
            holder.tv_detail.setText("完成");
            holder.tv_detail.setTextColor(ctx.getResources().getColor(R.color.green));
        }

        /*BitmapUtils.displayImage(ctx,info.getUsePic(),holder.iv_user_head);
        //holder.iv_user_head.setImageResource();
        holder.tv_name.setText(info.getName());
        //holder.iv_isapproval
        holder.tv_time.setText(info.getTime());
        holder.tv_detail.setText(info.getDetail());*/

        return convertView;
    }

    private void loadPic(final ImageView iv_user_head, String picPath) {
        Glide.with(ctx).load(picPath).placeholder(R.drawable.iv_head).into(new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                iv_user_head.setImageDrawable(resource);
            }
        });
    }
    public class MyViewHolder{
        View line_bottom;
        View line_top;
        TextView tv_name;
        TextView tv_time;
        ImageView iv_user_head;
        ImageView iv_isapproval;
        TextView tv_detail;//发起申请，审批中，审批完成
    }
}
