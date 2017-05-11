package huanxing_print.com.cn.printhome.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import huanxing_print.com.cn.printhome.R;

/**
 * Created by Administrator on 2017/3/23 0023.
 */

public class AccountRecordAdapter extends RecyclerView.Adapter<AccountRecordAdapter.MyViewHolder> {

    private Context ctx;

    public AccountRecordAdapter(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(ctx).inflate(R.layout.item_account_record1, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tv_rechargerecord_money.setText("100");
        holder.tv_rechargerecord_num.setText("订单编号:" + 123456);
        holder.tv_rechargerecord_time.setText("2017-02-02");
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_rechargerecord_time;
        private final TextView tv_rechargerecord_num;
        private final TextView tv_rechargerecord_money;

        public MyViewHolder(View view) {
            super(view);
            tv_rechargerecord_time = (TextView) view.findViewById(R.id.tv_rechargerecord_time);
            tv_rechargerecord_num = (TextView) view.findViewById(R.id.tv_rechargerecord_num);
            tv_rechargerecord_money = (TextView) view.findViewById(R.id.tv_rechargerecord_money);
        }
    }
}
