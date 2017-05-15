package huanxing_print.com.cn.printhome.ui.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xlhratingbar_lib.XLHRatingBar;

import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.model.comment.CommentListBean;
import huanxing_print.com.cn.printhome.util.CommonUtils;

import static huanxing_print.com.cn.printhome.R.id.rv_iv;
import static huanxing_print.com.cn.printhome.R.id.tv_comment_name;

/**
 * Created by Administrator on 2017/5/5 0005.
 */

public class CommentListAdapter1 extends BaseAdapter {
    private Context ctx;
    private List<CommentListBean.DetailBean> detail;

    public CommentListAdapter1(Context ctx, List<CommentListBean.DetailBean> detail) {
        this.ctx = ctx;
        this.detail = detail;
    }

    @Override
    public int getCount() {
        return detail.size() > 0 ? detail.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return detail.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(ctx).inflate(R.layout.item_comment_list, parent, false);
            holder = new MyHolder();
            holder.tv_comment_name = (TextView) convertView.findViewById(tv_comment_name);
            holder.rb_comment_list = (XLHRatingBar) convertView.findViewById(R.id.rb_comment_list);
            holder.tv_comment_list = (TextView) convertView.findViewById(R.id.tv_comment_list);
            holder.tv_comment_time = (TextView) convertView.findViewById(R.id.tv_comment_time);
            holder.rv_iv = (RecyclerView) convertView.findViewById(rv_iv);

            convertView.setTag(holder);
        } else {
            holder = (MyHolder) convertView.getTag();
        }

        CommentListBean.DetailBean bean = detail.get(position);
        holder.rb_comment_list.setCountSelected(bean.getTotalScore());
        holder.tv_comment_list.setText(bean.getRemark());
        holder.tv_comment_name.setText(bean.getNickName());
        holder.tv_comment_time.setText(bean.getDateTime());

        List<String> imageList = bean.getImageList();
        if (imageList.size() == 0) {
            holder.rv_iv.setVisibility(View.GONE);
        } else if (imageList.size() < 4) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, CommonUtils.dip2px(ctx, 100));
            holder.rv_iv.setLayoutParams(lp);
        } else if (imageList.size() < 8) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, CommonUtils.dip2px(ctx, 200));
            holder.rv_iv.setLayoutParams(lp);
        } else {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, CommonUtils.dip2px(ctx, 300));
            holder.rv_iv.setLayoutParams(lp);
        }
        CommentPicAdapter adapter = new CommentPicAdapter(ctx, imageList);
        GridLayoutManager manager = new GridLayoutManager(ctx, 3);
        holder.rv_iv.setLayoutManager(manager);
        holder.rv_iv.setAdapter(adapter);

        return convertView;
    }

    public class MyHolder {
        private TextView tv_comment_time;
        private XLHRatingBar rb_comment_list;
        private TextView tv_comment_list;
        private RecyclerView rv_iv;
        private TextView tv_comment_name;
    }
}
