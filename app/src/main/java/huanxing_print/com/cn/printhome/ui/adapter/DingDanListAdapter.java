package huanxing_print.com.cn.printhome.ui.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.model.my.DaYinListBean;

/**
 * Created by DjC512 on 2017-3-26.
 */

public class DingDanListAdapter extends BaseRecyclerAdapter<DingDanListAdapter.MyHolder> {

    private Context ctx;
    private List<DaYinListBean.DataBean.ListBean> list = new ArrayList<>();

    public DingDanListAdapter(Context ctx, List<DaYinListBean.DataBean.ListBean> list) {
        this.ctx = ctx;
        this.list = list;
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

    private String statusStr;

    @Override
    public void onBindViewHolder(MyHolder holder, int position, boolean isItem) {
//        DaYinListBean.DataBean.ListBean listBean = list.get(position);
//        int status = listBean.getStatus();
//        List<DaYinListBean.DataBean.ListBean.FileInfosBean> fileInfos = listBean.getFileInfos();
//
//        initStatus(status);
//
//        holder.tv_dylist_state.setText(statusStr);

        DingDanItemListAdapter adapter = new DingDanItemListAdapter(ctx,null);

        holder.rv_dy_list.setLayoutManager(new LinearLayoutManager(ctx));
        holder.rv_dy_list.setAdapter(adapter);
    }

    private void initStatus(int status) {
        switch (status) {
            case 0:
                statusStr = "未交费";
                break;
            case 1:
                statusStr = "已交费";
                break;
            case 2:
                statusStr = "打印失败";
                break;
            case 3:
                statusStr = "退款中";
                break;
            case 4:
                statusStr = "已经退款完毕";
                break;
            case 5:
                statusStr = "打印成功";
                break;

        }

    }

    @Override
    public int getAdapterItemCount() {
        return 3;
//        return list.size();
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
