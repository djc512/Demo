package huanxing_print.com.cn.printhome.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import de.hdodenhof.circleimageview.CircleImageView;
import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.model.chat.RedPackageDetail;
import huanxing_print.com.cn.printhome.util.CircleTransform;
import huanxing_print.com.cn.printhome.util.ObjectUtils;

/**
 * Created by LGH on 2017/5/8.
 */

public class SingleRedPackageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private final int TYPE_HEADER = 1;
    private final int TYPE_NORMAL = 2;
    private RedPackageDetail detail;

    public SingleRedPackageAdapter(Context context, RedPackageDetail detail) {
        this.context = context;
        this.detail = detail;
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
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int i) {
        String snatch = detail.getSnatch();
        if (viewHolder instanceof HeaderViewHolder) {
            String amount = detail.getAmount();
            String sendNameUrl = detail.getMasterFaceUrl();
            String sendName = detail.getMasterName();
            String remark = detail.getRemark();//
            String outTime = detail.getTime();//多久被抢光
            String snatchAmount = detail.getAmount();//已被抢红包金额

            if (!ObjectUtils.isNull(sendNameUrl)) {
                setImg(sendNameUrl, ((HeaderViewHolder) viewHolder).circleImageView);
            }
            if (!ObjectUtils.isNull(sendName)) {
                ((HeaderViewHolder) viewHolder).nameTv.setText(sendName + "的红包");
            }

            if (!ObjectUtils.isNull(remark)) {
                ((HeaderViewHolder) viewHolder).remarkTv.setText(remark);
            }

            if ("false".equals(detail.getSnatch())) {
                ((HeaderViewHolder) viewHolder).moneyTv.setText(amount);
                ((HeaderViewHolder) viewHolder).ll_money.setVisibility(View.GONE);
                ((HeaderViewHolder) viewHolder).detailTv.setText("红包金额" + amount + "元等待对方领取");

            } else {
                ((HeaderViewHolder) viewHolder).ll_money.setVisibility(View.GONE);
                ((HeaderViewHolder) viewHolder).detailTv.setText("1个红包共" + amount + "元");
            }

        } else if (viewHolder instanceof NormalViewHolder) {
            if ("true".equals(detail.getSnatch())) {
                ((NormalViewHolder) viewHolder).ryt.setVisibility(View.VISIBLE);
                String listamount = detail.getAmount();
                String listsendNameUrl = detail.getFaceUrl();
                String listsendName = detail.getName();
                String listtime = detail.getTime();

                if (!ObjectUtils.isNull(listsendNameUrl)) {
                    setImg(listsendNameUrl, ((NormalViewHolder) viewHolder).circleImageView);
                }
                if (!ObjectUtils.isNull(listsendName)) {
                    ((NormalViewHolder) viewHolder).nameTv.setText(listsendName);
                }
                if (!ObjectUtils.isNull(listamount)) {
                    ((NormalViewHolder) viewHolder).amountTv.setText(listamount + "元");
                }
                if (!ObjectUtils.isNull(listtime)) {
                    ((NormalViewHolder) viewHolder).timeTv.setText(listtime);
                }

            } else {
                ((NormalViewHolder) viewHolder).ryt.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return TYPE_HEADER;
        return TYPE_NORMAL;
    }

    public class NormalViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout ryt;
        public LinearLayout luckLyt;
        public CircleImageView circleImageView;
        public TextView nameTv;
        public TextView timeTv;
        public TextView amountTv;

        public NormalViewHolder(View itemView) {
            super(itemView);
            ryt = (RelativeLayout) itemView.findViewById(R.id.ryt);
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
            ll_money = (LinearLayout) itemView.findViewById(R.id.ll_money);
            circleImageView = (CircleImageView) itemView.findViewById(R.id.circleImageView);
            nameTv = (TextView) itemView.findViewById(R.id.nameTv);
            remarkTv = (TextView) itemView.findViewById(R.id.remarkTv);
            detailTv = (TextView) itemView.findViewById(R.id.detailTv);
            moneyTv = (TextView) itemView.findViewById(R.id.moneyTv);
        }
    }

    public void setImg(String imgUrl, final ImageView imageView) {
        Glide.with(context)
                .load(imgUrl)
                .centerCrop()
                .transform(new CircleTransform(context))
                .crossFade()
                .placeholder(R.drawable.iv_head)
                .error(R.drawable.iv_head)
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        imageView.setImageDrawable(resource);
                    }
                });
    }
}