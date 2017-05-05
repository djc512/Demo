package huanxing_print.com.cn.printhome.ui.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xlhratingbar_lib.XLHRatingBar;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.util.CommonUtils;

/**
 * Created by Administrator on 2017/5/5 0005.
 */

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.MyViewHold> {
    private Context ctx;

    public CommentListAdapter(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public MyViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHold hold = new MyViewHold(LayoutInflater.from(ctx).inflate(R.layout.item_comment_list, parent, false));
        return hold;
    }

    @Override
    public void onBindViewHolder(MyViewHold holder, int position) {
        holder.rb_comment_list.setCountSelected(4);
        holder.tv_comment_list.setText("操作便捷，价格合理");
        holder.tv_comment_name.setText("小强");
        holder.tv_comment_time.setText("2015-03-21");

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, CommonUtils.dip2px(ctx,160));
        holder.rv_iv.setLayoutParams(lp);
        CommentPicAdapter adapter = new CommentPicAdapter(ctx);
        GridLayoutManager manager = new GridLayoutManager(ctx, 4);
        holder.rv_iv.setLayoutManager(manager);
        holder.rv_iv.setAdapter(adapter);

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    class MyViewHold extends RecyclerView.ViewHolder {
        private TextView tv_comment_time;
        private XLHRatingBar rb_comment_list;
        private TextView tv_comment_list;
        private RecyclerView rv_iv;
        private TextView tv_comment_name;

        public MyViewHold(View view) {
            super(view);
            tv_comment_name = (TextView) view.findViewById(R.id.tv_comment_name);
            rb_comment_list = (XLHRatingBar) view.findViewById(R.id.rb_comment_list);
            tv_comment_list = (TextView) view.findViewById(R.id.tv_comment_list);
            tv_comment_time = (TextView) view.findViewById(R.id.tv_comment_time);
            rv_iv = (RecyclerView) view.findViewById(R.id.rv_iv);
        }
    }
}
