package huanxing_print.com.cn.printhome.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.model.my.OrderListBean;

/**
 * Created by Administrator on 2017/3/24 0024.
 */

public class MyBillItemAdapter extends RecyclerView.Adapter<MyBillItemAdapter.MyHolder> {

    private Context ctx;
    private List<OrderListBean.PrintListBean> detail;

    public MyBillItemAdapter(Context ctx, List<OrderListBean.PrintListBean> detail) {
        this.ctx = ctx;
        this.detail = detail;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyHolder holder = new MyHolder(LayoutInflater.from(
                ctx).inflate(R.layout.activity_bill_detail_item1, parent,
                false));
        return holder;
    }

    private boolean isShow = false;

    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {

        OrderListBean.PrintListBean bean = detail.get(position);
        int orderStatus = bean.getOrderStatus();
//        3-退款中 4-已经退款完毕 5-打印成功
        if (orderStatus == 3) {
            holder.tv_state.setText("正在退款");
            holder.tv_state.setBackgroundResource(R.color.orange);
        } else if (orderStatus == 4) {
            holder.tv_state.setText("已退款");
            holder.tv_state.setBackgroundResource(R.color.gray4);
        } else if (orderStatus == 5) {
            holder.tv_state.setText("打印成功");
            holder.tv_state.setBackgroundResource(R.color.green2);
        }
        String date = bean.getAddTime();//日期
        holder.tv_bill_time.setText(date);
        String amount = bean.getAmount();//金额
        holder.tv_bill_money.setText("￥" + amount);
        String orderId = bean.getOrderId();//订单号
        holder.tv_bill_orderid.setText("订单编号:" + orderId);
        String printAddress = bean.getPrintAddress();//打印店
        holder.tv_location.setText(printAddress);
        String fileType = bean.getFileType();//文件类型
        holder.tv_filetype.setText(fileType);
        int printPage = bean.getPrintPage();//打印页数
        holder.tv_printCount.setText(printPage + "页");
        String printPrice = bean.getPrintPrice();//单价
        holder.tv_price.setText(printPrice + "元/张");
        int color = bean.getColor();//色彩
        if (color == 0) {
            holder.tv_color.setText("彩色");
        } else {
            holder.tv_color.setText("黑白");
        }
        int printSize = bean.getPrintSize();//纸张
        if (printSize == 0) {
            holder.tv_papertype.setText("A4");
        } else {
            holder.tv_papertype.setText("A3");
        }
        String payType = bean.getPayType();//支付类型

        if ("balance".equals(payType)) {
            holder.tv_pay.setText("余额支付");
        } else if ("wxpay".equals(payType)) {
            holder.tv_pay.setText("微信支付");
        } else if ("zfb".equals(payType)){
            holder.tv_pay.setText("支付宝支付");
        }

        holder.iv_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShow) {
                    Bitmap upBitmap = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.up);
                    holder.iv_down.setImageBitmap(upBitmap);
                    holder.ll_detail.setVisibility(View.VISIBLE);
                } else {
                    Bitmap downBitmap = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.down);
                    holder.iv_down.setImageBitmap(downBitmap);
                    holder.ll_detail.setVisibility(View.GONE);

                }
                isShow = !isShow;
            }
        });
    }

    @Override
    public int getItemCount() {
        return detail.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {


        private final TextView tv_bill_time;
        private final TextView tv_bill_orderid;
        private final TextView tv_bill_money;
        private final ImageView iv_down;
        private final TextView tv_location;
        private final TextView tv_filetype;
        private final TextView tv_printCount;
        private final TextView tv_price;
        private final TextView tv_color;
        private final TextView tv_papertype;
        private final TextView tv_pay;
        private final LinearLayout ll_detail;
        private final TextView tv_state;

        public MyHolder(View view) {
            super(view);
            ll_detail = (LinearLayout) view.findViewById(R.id.ll_detail);
            tv_bill_time = (TextView) view.findViewById(R.id.tv_bill_time);
            tv_bill_orderid = (TextView) view.findViewById(R.id.tv_bill_orderid);
            tv_bill_money = (TextView) view.findViewById(R.id.tv_bill_money);
            iv_down = (ImageView) view.findViewById(R.id.iv_down);
            tv_location = (TextView) view.findViewById(R.id.tv_location);
            tv_filetype = (TextView) view.findViewById(R.id.tv_filetype);
            tv_printCount = (TextView) view.findViewById(R.id.tv_printCount);

            tv_price = (TextView) view.findViewById(R.id.tv_price);
            tv_color = (TextView) view.findViewById(R.id.tv_color);
            tv_papertype = (TextView) view.findViewById(R.id.tv_papertype);
            tv_pay = (TextView) view.findViewById(R.id.tv_pay);
            tv_state = (TextView) view.findViewById(R.id.tv_state);
        }
    }

    /**
     * 动画效果
     */
    private void showAnimation(LinearLayout ll_down) {
        AlphaAnimation aa = new AlphaAnimation(0, 1);
        aa.setDuration(500);
        aa.setFillAfter(true);
        ll_down.startAnimation(aa);
    }

    private void hideAnimation(LinearLayout ll_down) {
        AlphaAnimation aa = new AlphaAnimation(1, 0);
        aa.setDuration(500);
        aa.setFillAfter(true);
        ll_down.startAnimation(aa);
    }
}
