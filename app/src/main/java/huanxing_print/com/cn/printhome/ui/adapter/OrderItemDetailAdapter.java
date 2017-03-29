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
//        colourFlag	彩色打印0-彩色 1-黑白	number
//        directionFlag	方向标识0-横向 1-纵向	number
//        doubleFlag	双面打印0-是 1-否	number
//        fileName	文件名	string
//        fileUrl	文件url	string
//        printCount	打印份数	number
//        printNo	打印机编号	string
//        printType	打印类型	string
//        sizeType	大小类型0-A4 1-A3

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        OrderDetailBean.PrintFilesBean bean = printFiles.get(position);

        int colourFlag = bean.getColourFlag();//彩色打印0-彩色 1-黑白
        if(colourFlag == 0){
            holder.tv_orderitem_color.setText("颜色:彩色");
        }else if(colourFlag == 1){
            holder.tv_orderitem_color.setText("颜色:黑白");
        }

        int directionFlag = bean.getDirectionFlag();
        if(directionFlag == 0){
            holder.tv_orderitem_style.setText("版式:横向");
        }else if(directionFlag == 1){
            holder.tv_orderitem_color.setText("版式:纵向");
        }
        int doubleFlag = bean.getDoubleFlag();
        if (doubleFlag == 0) {
            holder.tv_orderitem_printtype.setText("页面：双面打印");
        }else if (doubleFlag == 1){
            holder.tv_orderitem_printtype.setText("页面：单面打印");
        }
        String fileName = bean.getFileName();
        holder.tv_orderitem_title.setText(fileName);
        int printCount = bean.getPrintCount();
        holder.tv_orderitem_fenshu.setText("打印份数:"+printCount);
        int sizeType = bean.getSizeType();
        if (sizeType == 0){
            holder.tv_orderitem_pagetype.setText("纸张:A4");
        }else if(sizeType == 1){
            holder.tv_orderitem_pagetype.setText("纸张:A3");
        }
    }

    @Override
    public int getItemCount() {
        return printFiles.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{

        private TextView tv_orderitem_title;
        private TextView tv_orderitem_fenshu;
        private TextView tv_orderitem_num;
        private TextView tv_orderitem_price;
        private TextView tv_orderitem_pagetype;
        private TextView tv_orderitem_color;
        private TextView tv_orderitem_style;
        private TextView tv_orderitem_printtype;

        public MyHolder(View view) {
            super(view);

            tv_orderitem_title = (TextView) view.findViewById(R.id.tv_orderitem_title);
            tv_orderitem_fenshu = (TextView) view.findViewById(R.id.tv_orderitem_fenshu);
            tv_orderitem_num = (TextView) view.findViewById(R.id.tv_orderitem_num);
            tv_orderitem_price = (TextView) view.findViewById(R.id.tv_orderitem_price);
            tv_orderitem_pagetype = (TextView) view.findViewById(R.id.tv_orderitem_pagetype);
            tv_orderitem_color = (TextView) view.findViewById(R.id.tv_orderitem_color);
            tv_orderitem_style = (TextView) view.findViewById(R.id.tv_orderitem_style);
            tv_orderitem_printtype = (TextView) view.findViewById(R.id.tv_orderitem_printtype);
        }
    }
}
