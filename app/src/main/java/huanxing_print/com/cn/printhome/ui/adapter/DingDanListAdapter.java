package huanxing_print.com.cn.printhome.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.model.my.DaYinListBean;
import huanxing_print.com.cn.printhome.ui.activity.my.OrderDetailActivity;

import static huanxing_print.com.cn.printhome.R.id.ll_dingdan;
import static huanxing_print.com.cn.printhome.R.id.rl_rv;
import static huanxing_print.com.cn.printhome.R.id.rv_dy_list;
import static huanxing_print.com.cn.printhome.R.id.tv_dylist_comment;
import static huanxing_print.com.cn.printhome.R.id.tv_dylist_money;
import static huanxing_print.com.cn.printhome.R.id.tv_dylist_state;
import static huanxing_print.com.cn.printhome.R.id.tv_dylist_time;

/**
 * Created by DjC512 on 2017-3-26.
 */

public class DingDanListAdapter extends BaseAdapter{

    private Context ctx;
    private List<DaYinListBean.ListBean> list;
    private String statusStr;
    private MyViewHolder holder;

    public DingDanListAdapter(Context ctx, List<DaYinListBean.ListBean> list) {
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
        if (convertView == null) {
            convertView = LayoutInflater.from(ctx).inflate(R.layout.activity_dy_item,null);
            holder = new MyViewHolder();
            holder.tv_dylist_time = (TextView) convertView.findViewById(tv_dylist_time);
            holder.tv_dylist_state = (TextView) convertView.findViewById(tv_dylist_state);
            holder.tv_dylist_money = (TextView) convertView.findViewById(tv_dylist_money);
            holder.rv_dy_list = (RecyclerView) convertView.findViewById(rv_dy_list);
            holder.ll_dingdan = (LinearLayout) convertView.findViewById(ll_dingdan);
            holder.tv_dylist_comment = (TextView) convertView.findViewById(tv_dylist_comment);
            holder.rl_rv = (RelativeLayout) convertView.findViewById(rl_rv);

            convertView.setTag(holder);
        }else {
            holder = (MyViewHolder) convertView.getTag();
        }

        DaYinListBean.ListBean listBean = list.get(position);
        int status = listBean.getStatus();//打印状态
        String addTime = listBean.getAddTime();
        double totalAmount = listBean.getTotalAmount();
        String remarkId = listBean.getRemarkId();
        initStatus(status,remarkId);

        holder.tv_dylist_time.setText(addTime);
        holder.tv_dylist_state.setText(statusStr);
        holder.tv_dylist_money.setText(totalAmount + "");
        List<DaYinListBean.ListBean.FileInfosBean> fileInfos = listBean.getFileInfos();

        DingDanItemListAdapter adapter = new DingDanItemListAdapter(ctx, fileInfos);
        holder.rv_dy_list.setLayoutManager(new LinearLayoutManager(ctx));
        holder.rv_dy_list.setAdapter(adapter);

        final int id = listBean.getId();
        adapter.setOnItemClickLitener(new DingDanItemListAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(ctx, OrderDetailActivity.class);
                intent.putExtra("id",String.valueOf(id));
                ctx.startActivity(intent);
            }
        });
        return convertView;
    }

    public class MyViewHolder{
        TextView tv_dylist_state;
        TextView tv_dylist_time;
        TextView tv_dylist_money;
        RecyclerView rv_dy_list;
        LinearLayout ll_dingdan;
        TextView tv_dylist_comment;
        RelativeLayout rl_rv;
    }
    private void initStatus(int status, String remarkId) {
        switch (status) {
            case 0:
                statusStr = "未交费";
                holder.tv_dylist_comment.setVisibility(View.INVISIBLE);
                break;
            case 1:
                statusStr = "已交费";
                holder.tv_dylist_comment.setVisibility(View.INVISIBLE);
                break;
            case 2:
                statusStr = "打印失败";
                holder.tv_dylist_comment.setVisibility(View.INVISIBLE);
                break;
            case 3:
                statusStr = "退款中";
                holder.tv_dylist_comment.setVisibility(View.INVISIBLE);
                break;
            case 4:
                statusStr = "已经退款完毕";
                holder.tv_dylist_comment.setVisibility(View.INVISIBLE);
                break;
            case 5:
                statusStr = "打印成功";
                if (remarkId == "0"){//默认0未评价，只有大于0时才有评论
                    holder.tv_dylist_comment.setVisibility(View.INVISIBLE);
                }else {
                    holder.tv_dylist_comment.setText(View.VISIBLE);
                }
                break;
        }
    }
}
