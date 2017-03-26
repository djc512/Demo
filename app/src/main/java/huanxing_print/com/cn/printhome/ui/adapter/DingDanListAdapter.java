package huanxing_print.com.cn.printhome.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import huanxing_print.com.cn.printhome.R;

/**
 * Created by DjC512 on 2017-3-26.
 */

public class DingDanListAdapter extends RecyclerView.Adapter<DingDanListAdapter.MyHolder> {

    private Context ctx;

    public DingDanListAdapter(Context ctx) {
        this.ctx =ctx;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyHolder holder = new MyHolder(LayoutInflater.from(ctx).inflate(R.layout.activity_dy_item, null));

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

        public MyHolder(View itemView) {
            super(itemView);
        }
    }
}
