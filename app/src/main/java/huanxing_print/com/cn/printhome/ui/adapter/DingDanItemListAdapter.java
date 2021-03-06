package huanxing_print.com.cn.printhome.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.model.my.DaYinListBean;

/**
 * Created by Administrator on 2017/3/27 0027.
 */

public class DingDanItemListAdapter extends RecyclerView.Adapter<DingDanItemListAdapter.MyHolder> {

    public interface OnItemClickLitener {
        void onItemClick(int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    private Context ctx;
    private List<DaYinListBean.ListBean.FileInfosBean> fileInfos;

    public DingDanItemListAdapter(Context ctx, List<DaYinListBean.ListBean.FileInfosBean> fileInfos) {
        this.ctx = ctx;
        this.fileInfos = fileInfos;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        MyHolder holder = new MyHolder(LayoutInflater.from(ctx).inflate(R.layout.activity_dylist_item, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {

        DaYinListBean.ListBean.FileInfosBean fileInfosBean = fileInfos.get(position);
        String fileName = fileInfosBean.getFileName();
        int printCount = fileInfosBean.getPrintCount();

        holder.tv_list_name.setText(fileName);
        holder.tv_list_num.setText("X"+printCount);

        holder.rl_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickLitener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return fileInfos.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        private final TextView tv_list_name;
        private final TextView tv_list_num;
        private final RelativeLayout rl_item;

        public MyHolder(View view) {
            super(view);
            tv_list_name = (TextView) view.findViewById(R.id.tv_list_name);
            tv_list_num = (TextView) view.findViewById(R.id.tv_list_num);
            rl_item = (RelativeLayout) view.findViewById(R.id.rl_item);
        }
    }
}
