package huanxing_print.com.cn.printhome.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.model.my.ChongZhiRecordBean;

/**
 * Created by Administrator on 2017/3/23 0023.
 */

public class AccountRecordItemAdapter extends RecyclerView.Adapter<AccountRecordItemAdapter.MyViewHolder> {
    private Context ctx;
    private List<ChongZhiRecordBean.DataBean.ListBean.DetailBean> list = new ArrayList<>();

    public AccountRecordItemAdapter(Context ctx, List<ChongZhiRecordBean.DataBean.ListBean.DetailBean> list) {
        this.ctx = ctx;
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(ctx).inflate(R.layout.activity_account_record_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
//        ChongZhiRecordBean.DataBean.ListBean.DetailBean detailBean = list.get(position);
//        String date = detailBean.getDate();//充值时间
//        int type = detailBean.getType();//充值类型
//        String amount = detailBean.getAmount();//充值金额
//
//        holder.tv_record_time.setText(date);//设置充值时间
//        if (type == 0) {//微信充值
//            holder.tv_record_czway.setText("充值—微信支付");
//        } else {//支付宝充值
//            holder.tv_record_czway.setText("充值-支付宝支付");
//        }
//        holder.tv_record_money.setText("+"+amount);
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_record_time;
        TextView tv_record_money;
        TextView tv_record_czway;

        public MyViewHolder(View view) {
            super(view);

            tv_record_czway = (TextView) view.findViewById(R.id.tv_record_czway);
            tv_record_time = (TextView) view.findViewById(R.id.tv_record_time);
            tv_record_money = (TextView) view.findViewById(R.id.tv_record_money);
        }
    }
}
