package huanxing_print.com.cn.printhome.ui.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;

import huanxing_print.com.cn.printhome.R;

/**
 * Created by DjC512 on 2017-3-26.
 */

public class DingDanListAdapter extends BaseRecyclerAdapter<DingDanListAdapter.MyHolder> {

    private Context ctx;

    public DingDanListAdapter(Context ctx) {
        this.ctx =ctx;
    }

    @Override
    public MyHolder getViewHolder(View view) {
        return new MyHolder(view);
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        MyHolder holder = new MyHolder(LayoutInflater.from(ctx).inflate(R.layout.activity_dy_item, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position, boolean isItem) {

        DingDanItemListAdapter adapter = new DingDanItemListAdapter(ctx);

        holder.rv_dy_list.setLayoutManager(new LinearLayoutManager(ctx));
        holder.rv_dy_list.setAdapter(adapter);
    }

    @Override
    public int getAdapterItemCount() {
        return 3;
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        private final TextView tv_dylist_time;
        private final TextView tv_dylist_state;
        private final TextView tv_dylist_money;
        private final RecyclerView rv_dy_list;

        public MyHolder(View view) {
            super(view);
            tv_dylist_time = (TextView) view.findViewById(R.id.tv_dylist_time);
            tv_dylist_state = (TextView) view.findViewById(R.id.tv_dylist_state);
            tv_dylist_money = (TextView) view.findViewById(R.id.tv_dylist_money);
            rv_dy_list = (RecyclerView) view.findViewById(R.id.rv_dy_list);
        }
    }
}
