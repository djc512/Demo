package huanxing_print.com.cn.printhome.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.model.my.OrderDetailBean;

/**
 * Created by Administrator on 2017/3/28 0028.
 */

public class OrderItemDetailAdapter extends RecyclerView.Adapter<OrderItemDetailAdapter.MyHolder> {

    private Context ctx;
    private List<OrderDetailBean.PrintFilesBean> printFiles;

    public OrderItemDetailAdapter(Context ctx, List<OrderDetailBean.PrintFilesBean> printFiles) {
        this.ctx = ctx;
        this.printFiles = printFiles;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyHolder holder = new MyHolder(LayoutInflater.from(ctx).inflate(R.layout.activity_order_item_detail,null));

        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        OrderDetailBean.PrintFilesBean printFilesBean = printFiles.get(position);
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class MyHolder extends RecyclerView.ViewHolder{

        private TextView tv_orderitem_title;
        private TextView tv_orderitem_fenshu;
        private TextView tv_orderitem_num;
        private TextView tv_orderitem_price;
        private TextView tv_orderitem_type;
        private TextView tv_orderitem_color;
        private TextView tv_orderitem_style;
        private TextView tv_orderitem_printtype;

        public MyHolder(View view) {
            super(view);

            tv_orderitem_title = (TextView) view.findViewById(R.id.tv_orderitem_title);
            tv_orderitem_fenshu = (TextView) view.findViewById(R.id.tv_orderitem_fenshu);
            tv_orderitem_num = (TextView) view.findViewById(R.id.tv_orderitem_num);
            tv_orderitem_price = (TextView) view.findViewById(R.id.tv_orderitem_price);
            tv_orderitem_type = (TextView) view.findViewById(R.id.tv_orderitem_type);
            tv_orderitem_color = (TextView) view.findViewById(R.id.tv_orderitem_color);
            tv_orderitem_style = (TextView) view.findViewById(R.id.tv_orderitem_style);
            tv_orderitem_printtype = (TextView) view.findViewById(R.id.tv_orderitem_printtype);
        }
    }
}
