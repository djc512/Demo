package huanxing_print.com.cn.printhome.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.model.contact.GroupInfo;

/**
 * Created by wanghao on 2017/5/5.
 */

public class GroupAdatper extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context mContext;
    private ArrayList<GroupInfo> mGroups;
    private LayoutInflater mInflater;
    public GroupAdatper(Context context, ArrayList<GroupInfo> groups) {
        this.mContext = context;
        this.mGroups = groups;
        mInflater = LayoutInflater.from(mContext);
    }

    public void modifyData(ArrayList<GroupInfo> groups) {
        this.mGroups = groups;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        View view = mInflater.inflate(R.layout.item_group,null);
        view.setLayoutParams(layoutParams);
        return new GroupHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        GroupInfo info = mGroups.get(position);
        ((GroupHolder)holder).bind(info);
    }

    @Override
    public int getItemCount() {
        return mGroups == null ? 0 : mGroups.size();
    }

    class GroupHolder extends RecyclerView.ViewHolder{
        private CircleImageView icon;
        private TextView tv_groupName;
        private TextView tv_groupMember;
        public GroupHolder(View itemView) {
            super(itemView);
            icon = (CircleImageView) itemView.findViewById(R.id.iv_head);
            tv_groupName = (TextView) itemView.findViewById(R.id.groupName);
            tv_groupMember = (TextView) itemView.findViewById(R.id.groupMembers);
        }

        public void bind(GroupInfo info) {
            if(null != info) {
                tv_groupName.setText(info.getGroupName());
                tv_groupMember.setText(String.format("(%d人)", info.getMembers().size()));
            }
        }
    }
}
