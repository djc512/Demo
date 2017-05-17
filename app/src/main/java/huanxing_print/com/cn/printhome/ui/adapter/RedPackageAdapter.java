package huanxing_print.com.cn.printhome.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.model.chat.GroupLuckyPackageDetail;
import huanxing_print.com.cn.printhome.model.chat.LuckyPackageObject;
import huanxing_print.com.cn.printhome.model.yinxin.RedPackageBean;
import huanxing_print.com.cn.printhome.util.CircleTransform;
import huanxing_print.com.cn.printhome.util.ObjectUtils;

/**
 * Created by LGH on 2017/5/8.
 */

public class RedPackageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private final int TYPE_HEADER = 1;
    private final int TYPE_NORMAL = 2;

    private RedPackageBean date;
    private GroupLuckyPackageDetail detail;
    private List<LuckyPackageObject> list = new ArrayList<>();
    public RedPackageAdapter(RedPackageBean date) {
        this.date = date;
    }

    public RedPackageAdapter(Context context,GroupLuckyPackageDetail detail) {
        this.context = context;
        this.detail = detail;
        this.list = detail.getList();
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == TYPE_HEADER) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_redpackage_list_header,
                    viewGroup,
                    false);
            return new HeaderViewHolder(v);
        } else {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_redpackage_list, viewGroup,
                    false);
            return new NormalViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof HeaderViewHolder) {
            String snatch = detail.getSnatch();
            String amount = detail.getAmount();
            String sendNameUrl =detail.getSendMemberUrl();
            String sendName =detail.getSendMemberName();
            String remark =detail.getRemark();//
            String outTime =detail.getOutTime();//多久被抢光
            String snatchAmount=detail.getSnatchAmount();//已被抢红包金额
            String snatchNum=detail.getSnatchNum();//已被抢红包个数
            String totalNumber =detail.getTotalNumber();//红包总数
            String totalAmount =detail.getTotalAmount();//红包总额

            if (!ObjectUtils.isNull(sendNameUrl)) {
                Glide.with(context)
                        .load(sendNameUrl)
                        .centerCrop()
                        .transform(new CircleTransform(context))
                        .crossFade()
                        .placeholder(R.drawable.iv_head)
                        .error(R.drawable.iv_head)
                        .into(((HeaderViewHolder) viewHolder).circleImageView);
            }
            if (!ObjectUtils.isNull(sendName)) {
                ((HeaderViewHolder) viewHolder).nameTv.setText(sendName + "的红包");
            }

            if (!ObjectUtils.isNull(remark)) {
                ((HeaderViewHolder) viewHolder).remarkTv.setText(remark);
            }

            if ("true".equals(snatch)) {
                ((HeaderViewHolder) viewHolder).ll_money.setVisibility(View.VISIBLE);
                ((HeaderViewHolder) viewHolder).moneyTv.setText(amount);
                if(snatchNum.equals(totalNumber)){
                 ((HeaderViewHolder) viewHolder).detailTv.setText(totalNumber+"个红包，"+outTime+"被抢光");
                }else{
                 ((HeaderViewHolder) viewHolder).detailTv.setText("已领取"+snatchNum+"/"
                         +totalNumber+"个，"+"共"+snatchAmount+"/"+totalAmount);
                }

            }else{
                ((HeaderViewHolder) viewHolder).ll_money.setVisibility(View.GONE);
                ((HeaderViewHolder) viewHolder).detailTv.setText(totalNumber+"个红包，"+outTime+"被抢光");
            }

        } else if (viewHolder instanceof NormalViewHolder) {

            String listamount = list.get(i).getAmount();
            String listsendNameUrl =list.get(i).getFaceUrl();
            String listsendName =list.get(i).getName();
            String listtime =list.get(i).getAddTime();
            String listtype =list.get(i).getType();

            if (!ObjectUtils.isNull(listsendNameUrl)) {
                Glide.with(context)
                        .load(listsendNameUrl)
                        .centerCrop()
                        .transform(new CircleTransform(context))
                        .crossFade()
                        .placeholder(R.drawable.iv_head)
                        .error(R.drawable.iv_head)
                        .into(((NormalViewHolder) viewHolder).circleImageView);
            }
            if (!ObjectUtils.isNull(listsendName)) {
                ((NormalViewHolder) viewHolder).nameTv.setText(listsendName);
            }
            if (!ObjectUtils.isNull(listamount)) {
                ((NormalViewHolder) viewHolder).amountTv.setText(listamount+"元");
            }
            if (!ObjectUtils.isNull(listtime)) {
                ((NormalViewHolder) viewHolder).timeTv.setText(listtime);
            }
            if ("1".equals(listtype)) {
                ((NormalViewHolder) viewHolder).luckLyt.setVisibility(View.VISIBLE);
            }else{
                ((NormalViewHolder) viewHolder).luckLyt.setVisibility(View.GONE);
            }


        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return TYPE_HEADER;
        return TYPE_NORMAL;
    }

    public class NormalViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout luckLyt;
        public CircleImageView circleImageView;
        public TextView nameTv;
        public TextView timeTv;
        public TextView amountTv;

        public NormalViewHolder(View itemView) {
            super(itemView);
            luckLyt = (LinearLayout) itemView.findViewById(R.id.luckLyt);
            circleImageView = (CircleImageView) itemView.findViewById(R.id.circleImageView);
            nameTv = (TextView) itemView.findViewById(R.id.nameTv);
            timeTv = (TextView) itemView.findViewById(R.id.timeTv);
            amountTv = (TextView) itemView.findViewById(R.id.amountTv);
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout ll_money;
        public CircleImageView circleImageView;
        public TextView moneyTv;
        public TextView nameTv;
        public TextView remarkTv;
        public TextView detailTv;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            ll_money= (LinearLayout) itemView.findViewById(R.id.ll_money);
            circleImageView = (CircleImageView) itemView.findViewById(R.id.circleImageView);
            nameTv = (TextView) itemView.findViewById(R.id.nameTv);
            remarkTv = (TextView) itemView.findViewById(R.id.remarkTv);
            detailTv = (TextView) itemView.findViewById(R.id.detailTv);
            moneyTv = (TextView) itemView.findViewById(R.id.moneyTv);
        }
    }
}