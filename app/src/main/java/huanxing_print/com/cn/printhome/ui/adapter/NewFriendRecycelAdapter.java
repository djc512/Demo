package huanxing_print.com.cn.printhome.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.lang.reflect.Array;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.model.contact.NewFriendInfo;
import huanxing_print.com.cn.printhome.ui.activity.contact.NewFriendActivity;

/**
 * Created by wanghao on 2017/5/4.
 */

public class NewFriendRecycelAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ArrayList<NewFriendInfo> initInfos = new ArrayList<NewFriendInfo>();
    private ArrayList<NewFriendInfo> friendInfos = new ArrayList<NewFriendInfo>();
    private LayoutInflater layoutInflater;

    public NewFriendRecycelAdapter(Context context, ArrayList<NewFriendInfo> infos) {
        this.mContext = context;

        layoutInflater = LayoutInflater.from(mContext);
        initInfos();
        modifyData(infos);
    }

    public enum ITEM_TYPE {
        ITEM_ADDRESS_BOOK,
        ITEM_HIGH_DIVIDER,
        ITEM_NEW_FIREND
    }

    private void initInfos() {
        NewFriendInfo info01 = new NewFriendInfo();
        info01.setShowType(ITEM_TYPE.ITEM_HIGH_DIVIDER.ordinal());
        NewFriendInfo info02 = new NewFriendInfo();
        info02.setShowType(ITEM_TYPE.ITEM_ADDRESS_BOOK.ordinal());
        NewFriendInfo info03 = new NewFriendInfo();
        info03.setShowType(ITEM_TYPE.ITEM_HIGH_DIVIDER.ordinal());
        initInfos.add(info01);
        initInfos.add(info02);
        initInfos.add(info03);
    }

    private void modifyData(ArrayList<NewFriendInfo> infos) {
        friendInfos.clear();
        friendInfos.addAll(initInfos);

        for (NewFriendInfo info : infos) {
            info.setShowType(ITEM_TYPE.ITEM_NEW_FIREND.ordinal());
            friendInfos.add(info);
        }
    }

    public void updateData(ArrayList<NewFriendInfo> infos) {
        modifyData(infos);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (viewType == ITEM_TYPE.ITEM_NEW_FIREND.ordinal()) {
            View view = layoutInflater.inflate(R.layout.item_new_friend, null);
            view.setLayoutParams(layoutParams);
            return new NewFriendHolder(view);
        } else if (viewType == ITEM_TYPE.ITEM_ADDRESS_BOOK.ordinal()) {
            View view = layoutInflater.inflate(R.layout.item_add_address_book, null);
            view.setLayoutParams(layoutParams);
            return new AddressBookHolder(view);
        } else {
            View view = layoutInflater.inflate(R.layout.item_new_friend_divider, null);
            view.setLayoutParams(layoutParams);
            return new DividerHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NewFriendHolder) {
            ((NewFriendHolder) holder).bind(position);
        } else if (holder instanceof DividerHolder) {

        } else {

        }
    }

    @Override
    public int getItemViewType(int position) {
        return friendInfos.get(position).getShowType();
    }

    @Override
    public int getItemCount() {
        return friendInfos.size();
    }

    class NewFriendHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CircleImageView icon;
        TextView nameTv;
        TextView verificationTv;
        Button agreeBtn;
        TextView stateTv;
        NewFriendInfo info;
        public NewFriendHolder(View itemView) {
            super(itemView);
            icon = (CircleImageView) itemView.findViewById(R.id.iv_head);
            nameTv = (TextView) itemView.findViewById(R.id.tv_yjName);
            verificationTv = (TextView) itemView.findViewById(R.id.tv_verification);
            agreeBtn = (Button) itemView.findViewById(R.id.btn_agree);
            stateTv = (TextView) itemView.findViewById(R.id.friend_state);
            agreeBtn.setOnClickListener(this);
        }

        public void bind(int position) {
            info = friendInfos.get(position);
            if (info != null) {
                nameTv.setText(info.getMemberName());
                verificationTv.setText(info.getNote());
                loadPic(info);
                if("0".equals(info.getType())) {
                    agreeBtn.setVisibility(View.VISIBLE);
                    stateTv.setVisibility(View.GONE);
                }else if("1".equals(info.getType())) {
                    agreeBtn.setVisibility(View.GONE);
                    stateTv.setVisibility(View.VISIBLE);
                    stateTv.setText("已添加");
                }else if("2".equals(info.getType())) {
                    agreeBtn.setVisibility(View.GONE);
                    stateTv.setVisibility(View.VISIBLE);
                    stateTv.setText("等待验证");
                }
            }
        }

        private void loadPic(NewFriendInfo info) {
            Glide.with(mContext).load(info.getMemberUrl()).placeholder(R.drawable.iv_head).into(new SimpleTarget<GlideDrawable>() {
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                    icon.setImageDrawable(resource);
                }
            });
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_agree:
                    if(null != onAddItemClickListener) {
                        onAddItemClickListener.onItemNewFriendPassClick(info);
                    }
                    break;
            }
        }
    }

    class DividerHolder extends RecyclerView.ViewHolder {

        public DividerHolder(View itemView) {
            super(itemView);
        }
    }

    class AddressBookHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public AddressBookHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(null != onAddItemClickListener) {
                onAddItemClickListener.onAddressBookClick();
            }
        }
    }

    public interface OnAddItemClickListener{
        void onAddressBookClick();
        void onItemNewFriendPassClick(NewFriendInfo newFriendInfo);
    }

    private OnAddItemClickListener onAddItemClickListener;
    public void setOnAddItemClickListener(OnAddItemClickListener listener){
        this.onAddItemClickListener = listener;
    }
}
