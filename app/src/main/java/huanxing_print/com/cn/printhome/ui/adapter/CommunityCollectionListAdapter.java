package huanxing_print.com.cn.printhome.ui.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.util.CommonUtils;

/**
 * Created by Administrator on 2017/5/5 0005.
 */

public class CommunityCollectionListAdapter extends RecyclerView.Adapter<CommunityCollectionListAdapter.MyViewHold> {
    private Context ctx;

    public CommunityCollectionListAdapter(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public MyViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHold hold = new MyViewHold(LayoutInflater.from(ctx).inflate(R.layout.item_community_list, parent, false));
        return hold;
    }

    @Override
    public void onBindViewHolder(MyViewHold holder, int position) {

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, CommonUtils.dip2px(ctx,160));
        holder.rv_iv.setLayoutParams(lp);
        CommentPicAdapter adapter = new CommentPicAdapter(ctx, imageList);
        GridLayoutManager manager = new GridLayoutManager(ctx, 4);
        holder.rv_iv.setLayoutManager(manager);
        holder.rv_iv.setAdapter(adapter);

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    class MyViewHold extends RecyclerView.ViewHolder {
        private CircleImageView iv_head;
        private TextView tv_name;
        private TextView tv_content;
        private RecyclerView rv_iv;
        private TextView tv_time;
        private TextView tv_comment_num;
        private TextView tv_browse_num;
        public MyViewHold(View view) {
            super(view);
            iv_head = (CircleImageView) view.findViewById(R.id.iv_head);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_content= (TextView) view.findViewById(R.id.tv_content);
            rv_iv = (RecyclerView) view.findViewById(R.id.rv_iv);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            tv_comment_num = (TextView) view.findViewById(R.id.tv_comment_num);
            tv_browse_num = (TextView) view.findViewById(R.id.tv_browse_num);
        }
    }
}
