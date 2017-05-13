package huanxing_print.com.cn.printhome.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.model.my.ChongZhiRecordBean;

import static huanxing_print.com.cn.printhome.R.id.tv_rechargerecord_money;
import static huanxing_print.com.cn.printhome.R.id.tv_rechargerecord_num;
import static huanxing_print.com.cn.printhome.R.id.tv_rechargerecord_time;

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
        return list.size() > 0 ? list.size() : 0;
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
        MyHolder holder = null;
        if (null == convertView) {
            convertView = LayoutInflater.from(ctx).inflate(R.layout.item_account_record, parent, false);
            holder = new MyHolder();
            holder.tv_rechargerecord_time = (TextView) convertView.findViewById(tv_rechargerecord_time);
            holder.tv_rechargerecord_num = (TextView) convertView.findViewById(tv_rechargerecord_num);
            holder.tv_rechargerecord_money = (TextView) convertView.findViewById(tv_rechargerecord_money);
            convertView.setTag(holder);

        } else {
            holder = (MyHolder) convertView.getTag();

        }
        List<ChongZhiRecordBean.ListBean.DetailBean> detail = list.get(position).getDetail();
        ChongZhiRecordBean.ListBean.DetailBean detailBean = detail.get(position);
        holder.tv_rechargerecord_money.setText(detailBean.getAmount());
        holder.tv_rechargerecord_num.setText("订单编号:" + detailBean.getOrderId());
        holder.tv_rechargerecord_time.setText(detailBean.getDate());
        return convertView;
    }

    public class MyHolder {
        TextView tv_rechargerecord_time;
        TextView tv_rechargerecord_num;
        TextView tv_rechargerecord_money;
    }
}
