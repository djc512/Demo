package huanxing_print.com.cn.printhome.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.model.my.PrinterInfoBean;
import huanxing_print.com.cn.printhome.net.callback.NullCallback;
import huanxing_print.com.cn.printhome.net.request.my.SetDefaultPrinterRequest;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;

import static huanxing_print.com.cn.printhome.R.id.tv_set;

/**
 * Created by DjC512 on 2017-3-26.
 */

public class SetDefaultPrinterListAdapter extends BaseAdapter {

    private Context context;
    private  List<PrinterInfoBean> list;
    private  String token;
    private final String BROADCAST_ACTION_REFRESH= "SetDefaultPrinterActivity.refresh";
    public SetDefaultPrinterListAdapter(Context context,String token,List<PrinterInfoBean> list) {
        this.context =context;
        this.token =token;
        this.list =list;
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        // NewsItem info = list.get(position);
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_listview_default_printer, parent, false);

            holder.tv_printer_name = (TextView) convertView
                    .findViewById(R.id.tv_printer_name);
            holder.tv_printer_location = (TextView) convertView
                    .findViewById(R.id.tv_printer_location);
            holder.tv_set = (TextView) convertView
                    .findViewById(tv_set);
            holder.tv_print_total = (TextView) convertView
                    .findViewById(R.id.tv_print_total);
            holder.tv_comments = (TextView) convertView
                    .findViewById(R.id.tv_comments);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String printerName = list.get(position).getRemark();
        String location= list.get(position).getPrinterAddress();
        String printTotal= list.get(position).getPageCount();
        int printerDef = list.get(position).getPrinterDef();//默认打印机0否 1是
        String comments= list.get(position).getRemarkCount();
        //String printerNo= list.get(position).getPrinterNo();//打印机id

        if (!ObjectUtils.isNull(printerName)) {
            holder.tv_printer_name.setText(printerName);
        }
        if (!ObjectUtils.isNull(location)) {
            holder.tv_printer_location.setText(location);
        }
        if (1==printerDef){
            holder.tv_set.setText("已默认");
            holder.tv_set.setTextColor(context.getResources().getColor(R.color.white));
            holder.tv_set.setBackground(context.getResources()
                    .getDrawable(R.drawable.broder_green_full));

        }else{
            holder.tv_set.setText("设为默认");
            holder.tv_set.setTextColor(context.getResources().getColor(R.color.green));
            holder.tv_set.setBackground(context.getResources()
                    .getDrawable(R.drawable.broder_green));
        }
        holder.tv_print_total.setText("("+printTotal+")");
        holder.tv_comments.setText("("+comments+")");

        holder.tv_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String printerNo= list.get(position).getPrinterNo();//打印机id
                DialogUtils.showProgressDialog(context, "正在设置中").show();
                SetDefaultPrinterRequest.setDefaultPrinter(context,printerNo, token, new NullCallback() {
                    @Override
                    public void fail(String msg) {
                        DialogUtils.closeProgressDialog();
                    }

                    @Override
                    public void connectFail() {
                        DialogUtils.closeProgressDialog();

                    }

                    @Override
                    public void success(String msg) {
                        DialogUtils.closeProgressDialog();
                        //发送一个广播更新页面
                        Intent intent = new Intent();
                        intent.setAction(BROADCAST_ACTION_REFRESH);
                        context.sendBroadcast(intent);
                    }
                });
            }
        });
        return convertView;
    }


    class ViewHolder {
        TextView tv_printer_name;
        TextView tv_printer_location;
        TextView tv_set;
        TextView tv_print_total;
        TextView tv_comments;

    }

}
