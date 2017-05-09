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

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.model.contact.FriendSearchInfo;

/**
 * Created by wanghao on 2017/5/3.
 */

public class AddContactAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ArrayList<FriendSearchInfo> addContacts;
    private LayoutInflater mLayoutInflater;
    public AddContactAdapter(Context context, ArrayList<FriendSearchInfo> infos) {
        this.mContext = context;
        this.addContacts = infos;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void updateContacts(ArrayList<FriendSearchInfo> infos) {
        this.addContacts = infos;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_contact_add, null);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        return new AddContactHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((AddContactHolder)holder).bind(position);
    }

    @Override
    public int getItemCount() {
        return addContacts == null ? 0 : addContacts.size();
    }

    class AddContactHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView yjNameTv;
        TextView yjNumTv;
        Button addBtn;
        CircleImageView iconImg;
        int mPosition;
        public AddContactHolder(View itemView) {
            super(itemView);
            yjNameTv = (TextView) itemView.findViewById(R.id.tv_yjName);
            yjNumTv = (TextView) itemView.findViewById(R.id.tv_yjNum);
            addBtn = (Button) itemView.findViewById(R.id.btn_add_contact);
            iconImg = (CircleImageView) itemView.findViewById(R.id.iv_head);
            addBtn.setOnClickListener(this);
        }

        public void bind(int position) {
            this.mPosition = position;
            FriendSearchInfo info = addContacts.get(position);
            if(null != info) {

                yjNumTv.setText(info.getUniqueId());
                yjNameTv.setText(info.getMemberName());
                loadPic(info);
            }
        }

        private void loadPic(FriendSearchInfo info) {
            Glide.with(mContext).load(info.getMemberUrl()).placeholder(R.drawable.iv_head).into(new SimpleTarget<GlideDrawable>() {
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                    iconImg.setImageDrawable(resource);
                }
            });
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_add_contact:
                    if(null != itemSendListener) {
                        itemSendListener.send(mPosition);
                    }
                    break;
            }
        }
    }

    public interface OnItemSendListener{
        void send(int position);
    }

    private OnItemSendListener itemSendListener;
    public void setOnItemSendListener(OnItemSendListener listener) {
        this.itemSendListener = listener;
    }
}
