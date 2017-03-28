package huanxing_print.com.cn.printhome.ui.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;

import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.model.my.DaYinListBean;
import huanxing_print.com.cn.printhome.util.ToastUtil;

/**
 * Created by DjC512 on 2017-3-26.
 */

public class DingDanListAdapter extends BaseRecyclerAdapter<DingDanListAdapter.MyHolder> {

    private Context ctx;
    private List<DaYinListBean.ListBean> list;

    public DingDanListAdapter(Context ctx, List<DaYinListBean.ListBean> list) {
        this.ctx = ctx;
        this.list = list;
    }

    public interface OnItemClickLitener {
        void onItemClick(int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
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
    public void onBindViewHolder(MyHolder holder, final int position, boolean isItem) {
        DaYinListBean.ListBean listBean = list.get(position);
        int status = listBean.getStatus();//打印状态
        initStatus(status);
        String addTime = listBean.getAddTime();
        double totalAmount = listBean.getTotalAmount();

        holder.tv_dylist_time.setText(addTime);
        holder.tv_dylist_state.setText(statusStr);
        holder.tv_dylist_money.setText(totalAmount+"");

        DingDanItemListAdapter adapter = new DingDanItemListAdapter(ctx);

        holder.rv_dy_list.setLayoutManager(new LinearLayoutManager(ctx));
        holder.rv_dy_list.setAdapter(adapter);


        if (mOnItemClickLitener != null){
            holder.ll_dingdan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(position);
                }
            });
        }
        holder.btn_dylist_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.doToast(ctx,"去评论");
            }
        });
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
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        private final TextView tv_dylist_time;
        private final TextView tv_dylist_state;
        private final TextView tv_dylist_money;
        private final RecyclerView rv_dy_list;
        private final LinearLayout ll_dingdan;
        private final Button btn_dylist_comment;
        private final RelativeLayout rl_rv;

        public MyHolder(View view) {
            super(view);

            tv_dylist_time = (TextView) view.findViewById(R.id.tv_dylist_time);
            tv_dylist_state = (TextView) view.findViewById(R.id.tv_dylist_state);
            tv_dylist_money = (TextView) view.findViewById(R.id.tv_dylist_money);
            rv_dy_list = (RecyclerView) view.findViewById(R.id.rv_dy_list);
            ll_dingdan = (LinearLayout) view.findViewById(R.id.ll_dingdan);
            btn_dylist_comment = (Button) view.findViewById(R.id.btn_dylist_comment);
            rl_rv = (RelativeLayout) view.findViewById(R.id.rl_rv);
        }
    }
}
