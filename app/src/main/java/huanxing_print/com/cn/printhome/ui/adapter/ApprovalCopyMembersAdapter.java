package huanxing_print.com.cn.printhome.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.model.approval.ApprovalPeopleItem;

/**
 * Created by wanghao on 2017/5/13.
 */

public class ApprovalCopyMembersAdapter extends BaseAdapter{
    private Context mContext;
    private ArrayList<ApprovalPeopleItem> mCopys = new ArrayList<ApprovalPeopleItem>();
    private LayoutInflater layoutInflater;
    public ApprovalCopyMembersAdapter(Context context, ArrayList<ApprovalPeopleItem> copys) {
        this.mContext = context;
        this.mCopys = copys;
        layoutInflater = LayoutInflater.from(mContext);
    }

    public void modifyData(ArrayList<ApprovalPeopleItem> copys) {
        this.mCopys = copys;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mCopys.size();
    }

    @Override
    public Object getItem(int i) {
        return mCopys.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(null == view) {
            holder = new ViewHolder();
            view = layoutInflater.inflate(R.layout.item_approval_copy_member, null);
            holder.iv_member_icon = (CircleImageView) view.findViewById(R.id.iv_copy_icon);
            holder.tv_memberName = (TextView) view.findViewById(R.id.tv_copy_name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        ApprovalPeopleItem copy = mCopys.get(i);
        holder.tv_memberName.setText(copy.getName());
        loadPic(holder.iv_member_icon, copy.getFaceUrl());
        return view;
    }
    private void loadPic(final ImageView iv_user_head, String picPath) {
        Glide.with(mContext).load(picPath).placeholder(R.drawable.iv_head).into(new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                iv_user_head.setImageDrawable(resource);
            }
        });
    }

    class ViewHolder{
        CircleImageView iv_member_icon;
        TextView tv_memberName;
    }
}
