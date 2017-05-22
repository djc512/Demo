package huanxing_print.com.cn.printhome.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.model.contact.GroupMember;
import huanxing_print.com.cn.printhome.util.CircleTransform;
import huanxing_print.com.cn.printhome.view.imageview.RoundImageView;

/**
 * Created by wanghao on 2017/5/22.
 */

public class ApprovalAndCopyAdapter extends BaseAdapter{
    private Context mContext;
    private ArrayList<GroupMember> groupMembers = new ArrayList<GroupMember>();
    private LayoutInflater layoutInflater;
    private HashMap<Integer, View> viewMap = new HashMap<Integer, View>();
    public ApprovalAndCopyAdapter(Context context, ArrayList<GroupMember> members) {
        this.mContext = context;
        layoutInflater = LayoutInflater.from(mContext);
        initData(members);
    }

    private void initData(ArrayList<GroupMember> members) {
        groupMembers.addAll(members);

        //加入个"+"
        GroupMember addGroupMember = new GroupMember();
        addGroupMember.setEasemobId("+");
        groupMembers.add(addGroupMember);
    }

    public void modifyData(ArrayList<GroupMember> members) {
        groupMembers.clear();
        initData(members);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return groupMembers.size();
    }

    @Override
    public Object getItem(int i) {
        return groupMembers.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if(!viewMap.containsKey(i) || viewMap.get(i) == null) {
            holder = new ViewHolder();
            view = layoutInflater.inflate(R.layout.item_grid_approval, null);
            holder.round_head_image = (RoundImageView) view.findViewById(R.id.round_head_image);
            holder.txt_name = (TextView) view.findViewById(R.id.txt_name);
            view.setTag(holder);
            viewMap.put(i,view);
        }else{
            view = viewMap.get(i);
            holder = (ViewHolder) view.getTag();
        }

        if ("+".equals(groupMembers.get(i).getEasemobId())) {
            //这里是加号
            Glide.with(mContext)
                    .load(R.drawable.add_people)
                    .centerCrop()
                    .transform(new CircleTransform(mContext))
                    .crossFade()
                    .into(holder.round_head_image);
            //添加图标
            holder.txt_name.setVisibility(View.GONE);
        } else {
            Glide.with(mContext)
                    .load(groupMembers.get(i).getMemberUrl())
                    .centerCrop()
                    .transform(new CircleTransform(mContext))
                    .crossFade()
                    .placeholder(R.drawable.iv_head)
                    .into(holder.round_head_image);
            holder.txt_name.setVisibility(View.VISIBLE);
            holder.txt_name.setText(groupMembers.get(i).getMemberName());
        }
        return view;
    }

    class ViewHolder {
        RoundImageView round_head_image;
        TextView txt_name;
    }
}
