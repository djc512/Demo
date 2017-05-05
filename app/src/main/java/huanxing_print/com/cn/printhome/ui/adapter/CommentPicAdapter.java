package huanxing_print.com.cn.printhome.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import huanxing_print.com.cn.printhome.R;

/**
 * Created by Administrator on 2017/5/5 0005.
 */

public class CommentPicAdapter extends RecyclerView.Adapter<CommentPicAdapter.MyViewHolder> {
    private Context ctx;

    public CommentPicAdapter(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(ctx).inflate(R.layout.item_comment_pic,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_comment;

        public MyViewHolder(View view) {
            super(view);
            iv_comment = (ImageView) view.findViewById(R.id.iv_comment);
        }
    }
}
