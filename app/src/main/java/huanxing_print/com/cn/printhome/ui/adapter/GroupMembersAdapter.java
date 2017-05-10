package huanxing_print.com.cn.printhome.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.model.contact.GroupMember;

/**
 * Created by wanghao on 2017/5/10.
 */

public class GroupMembersAdapter extends BaseAdapter{
    private Context ctx;
    private ArrayList<GroupMember> groupMembers;
    private LayoutInflater layoutInflater;
    private HashMap<Integer, View> viewMap = new HashMap<Integer, View>();
    public GroupMembersAdapter(Context context, ArrayList<GroupMember> members) {
        this.ctx = context;
        this.groupMembers = members;
        layoutInflater = LayoutInflater.from(ctx);
    }

    public void modify(ArrayList<GroupMember> members) {
        viewMap.clear();
        this.groupMembers = members;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(null != groupMembers) {
            return groupMembers.size() + 1;
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if(null != groupMembers)
            return groupMembers.get(i);
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        final ItemHolder holder;
        if(!viewMap.containsKey(position) || viewMap.get(position) == null) {
            view = layoutInflater.inflate(R.layout.item_group_member,null);
            holder = new ItemHolder();
            holder.delIcon = (CircleImageView) view.findViewById(R.id.iv_member_del);
            holder.memberIcon = (CircleImageView) view.findViewById(R.id.iv_member_icon);
            view.setTag(holder);
            viewMap.put(position,view);
        }else{
            view = viewMap.get(position);
            holder = (ItemHolder) view.getTag();
        }
        if(position == groupMembers.size()) {
            //最后一个
            holder.delIcon.setVisibility(View.GONE);
            Glide.with(ctx).load(R.drawable.ic_group_member_add).into(holder.memberIcon);
            holder.memberIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(null != memberClickListener) {
                        memberClickListener.clickAdd();
                    }
                }
            });
        }else{
            holder.delIcon.setVisibility(View.VISIBLE);
            final GroupMember member = groupMembers.get(position);

            Glide.with(ctx).load(R.drawable.ic_group_member_del).into(holder.delIcon);
            Glide.with(ctx).load(member.getMemberUrl()).placeholder(R.drawable.iv_head).into(new SimpleTarget<GlideDrawable>() {
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                    holder.memberIcon.setImageDrawable(resource);
                }
            });
            holder.memberIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(null != memberClickListener) {
                        memberClickListener.clickMember(member);
                    }
                }
            });
            holder.delIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(null != memberClickListener) {
                        memberClickListener.delMember(member);
                    }
                }
            });
        }

        return view;
    }

    class ItemHolder{
        CircleImageView memberIcon;
        CircleImageView delIcon;
    }

    private OnGroupMemberClickListener memberClickListener;
    public interface OnGroupMemberClickListener{
        void delMember(GroupMember member);
        void clickMember(GroupMember member);
        void clickAdd();
    }

    public void setOnGroupMemberClickListener(OnGroupMemberClickListener listener) {
        this.memberClickListener = listener;
    }
}
