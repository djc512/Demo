package huanxing_print.com.cn.printhome.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.model.approval.SubFormItem;

/**
 * Created by wanghao on 2017/5/14.
 */

public class SubFormAdatper extends BaseAdapter{
    private Context mContext;
    private ArrayList<SubFormItem> subFormItems = new ArrayList<SubFormItem>();
    private LayoutInflater layoutInflater;
    public SubFormAdatper(Context context, ArrayList<SubFormItem> items) {
        this.mContext = context;
        this.subFormItems = items;
        layoutInflater = LayoutInflater.from(mContext);
    }

    public void modifyData(ArrayList<SubFormItem> items) {
        this.subFormItems = items;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return subFormItems.size();
    }

    @Override
    public Object getItem(int i) {
        return subFormItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(null == view) {
            holder = new ViewHolder();
            view = layoutInflater.inflate(R.layout.item_apply_detail, null);
            holder.subform_title = (TextView) view.findViewById(R.id.subform_title);
            holder.tv_money = (TextView) view.findViewById(R.id.tv_money);
            holder.tv_kind = (TextView) view.findViewById(R.id.tv_kind);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        holder.subform_title.setText(String.format("报销明细%d",(i + 1)));
        holder.tv_money.setText(subFormItems.get(i).getAmount() + "元");
        holder.tv_kind.setText(subFormItems.get(i).getType());
        return view;
    }

    class ViewHolder {
        TextView subform_title;
        TextView tv_money;
        TextView tv_kind;
    }
}
