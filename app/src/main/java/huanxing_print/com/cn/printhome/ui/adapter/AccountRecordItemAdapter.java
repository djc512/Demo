package huanxing_print.com.cn.printhome.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.model.my.ChongZhiRecordListDetailBean;

/**
 * Created by Administrator on 2017/3/23 0023.
 */

public class AccountRecordItemAdapter extends RecyclerView.Adapter<AccountRecordItemAdapter.MyHolder> {

    private Context ctx;
    private List<ChongZhiRecordListDetailBean> list;

    public AccountRecordItemAdapter(Context ctx, List<ChongZhiRecordListDetailBean> list) {
        this.ctx = ctx;
        this.list = list;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyHolder holder = new MyHolder(LayoutInflater.from(
                ctx).inflate(R.layout.activity_account_record_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        ChongZhiRecordListDetailBean detailBean = list.get(position);

        holder.tv_rechargerecord_money.setText(detailBean.getAmount()+"");
        holder.tv_rechargerecord_num.setText("订单编号:" + detailBean.getOrderId());
        holder.tv_rechargerecord_time.setText(detailBean.getDate()+"");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MyHolder extends RecyclerView.ViewHolder {
        private TextView tv_rechargerecord_money;
        private  TextView tv_rechargerecord_num;
        private TextView tv_rechargerecord_time;

        public MyHolder(View convertView) {
            super(convertView);
            tv_rechargerecord_time = (TextView) convertView.findViewById(R.id.tv_rechargerecord_time);
            tv_rechargerecord_num = (TextView) convertView.findViewById(R.id.tv_rechargerecord_num);
            tv_rechargerecord_money = (TextView) convertView.findViewById(R.id.tv_rechargerecord_money);

        }
    }
}
