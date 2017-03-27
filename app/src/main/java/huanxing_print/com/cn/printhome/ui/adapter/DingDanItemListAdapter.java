package huanxing_print.com.cn.printhome.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import huanxing_print.com.cn.printhome.R;

/**
 * Created by Administrator on 2017/3/27 0027.
 */

public class DingDanItemListAdapter extends RecyclerView.Adapter<DingDanItemListAdapter.MyHolder> {

    private Context ctx;

    public DingDanItemListAdapter(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        MyHolder holder = new MyHolder(LayoutInflater.from(ctx).inflate(R.layout.activity_dylist_item, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        private final TextView tv_list_name;
        private final TextView tv_list_num;

        public MyHolder(View view) {
            super(view);
            tv_list_name = (TextView) view.findViewById(R.id.tv_list_name);
            tv_list_num = (TextView) view.findViewById(R.id.tv_list_num);
        }
    }
}
