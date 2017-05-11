package huanxing_print.com.cn.printhome.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.model.contact.GroupMember;
import huanxing_print.com.cn.printhome.util.PinYinUtil;
import huanxing_print.com.cn.printhome.util.contact.ContactComparator;

/**
 * Created by Administrator on 2017/5/10 0010.
 */

public class QunMemberListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<GroupMember> mInfos = new ArrayList<>();
    private List<String> characterList; // 字母List
    private List<String> mFriends; // 联系人名称List（转换成拼音）

    public enum ITEM_TYPE {
        ITEM_TYPE_FRIEND,
        ITEM_TYPE_CHARACTER
    }

    public QunMemberListAdapter(Context context, List<GroupMember> infos) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        updateData(infos);
    }

    public void updateData(List<GroupMember> infos) {
        mInfos.clear();
        addFriend(infos);
    }

    private void addFriend(List<GroupMember> infos) {
        Map<String, GroupMember> map = new HashMap<String, GroupMember>();
        ArrayList<GroupMember> newInfos = new ArrayList<GroupMember>();
        mFriends = new ArrayList<String>();
        characterList = new ArrayList<String>();
        for (int i = 0; i < infos.size(); i++) {
            String pinyin = PinYinUtil.getPingYin(infos.get(i).getMemberName());
            map.put(pinyin, infos.get(i));
            mFriends.add(pinyin);
        }
        Collections.sort(mFriends, new ContactComparator());

        for (String pinyin : mFriends) {
            GroupMember info = map.get(pinyin);
            if (null != info) {
                String character = (pinyin.charAt(0) + "").toUpperCase(Locale.ENGLISH);
                if (!characterList.contains(character)) {
                    if (character.hashCode() >= "A".hashCode() && character.hashCode() <= "Z".hashCode()) { // 是字母
                        characterList.add(character);
                        GroupMember characterInfo = new GroupMember();
                        characterInfo.setShowtype(ITEM_TYPE.ITEM_TYPE_CHARACTER.ordinal());
                        characterInfo.setMemberName(character);
                        newInfos.add(characterInfo);
                    } else {
                        if (!characterList.contains("#")) {
                            characterList.add("#");
                            GroupMember characterInfo = new GroupMember();
                            characterInfo.setShowtype(ITEM_TYPE.ITEM_TYPE_CHARACTER.ordinal());
                            characterInfo.setMemberName("#");
                            newInfos.add(characterInfo);
                        }
                    }
                }
                info.setShowtype(ITEM_TYPE.ITEM_TYPE_FRIEND.ordinal());
                newInfos.add(info);
            }

        }
        mInfos.addAll(newInfos);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (viewType == ITEM_TYPE.ITEM_TYPE_CHARACTER.ordinal()) {
            View view = mLayoutInflater.inflate(R.layout.item_contact_character, null);
            view.setLayoutParams(layoutParams);
            return new CharacterHolder(view);
        } else {
            View view = mLayoutInflater.inflate(R.layout.item_contact_friend, null);
            view.setLayoutParams(layoutParams);
            return new FriendHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CharacterHolder) {
            ((CharacterHolder) holder).mTitle.setText(mInfos.get(position).getMemberName());
        } else {
            ((FriendHolder) holder).bind(mInfos.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mInfos.get(position).getShowtype();
    }

    @Override
    public int getItemCount() {
        return mInfos.size();
    }

    class FriendHolder extends RecyclerView.ViewHolder {
        TextView mTitle;
        CircleImageView iv_head;
        private GroupMember friendInfo;

        public FriendHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.nameTv);
            iv_head = (CircleImageView) itemView.findViewById(R.id.iv_head);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(null != typeItemClickerListener && null != friendInfo) {
                        typeItemClickerListener.contactClick(friendInfo);
                    }
                }
            });
        }

        public void bind(GroupMember info) {
            this.friendInfo = info;
            if(null != friendInfo) {
                mTitle.setText(friendInfo.getMemberName());
                loadPic();
            }
        }

        private void loadPic() {
            Glide.with(mContext).load(friendInfo.getMemberUrl()).placeholder(R.drawable.iv_head).into(new SimpleTarget<GlideDrawable>() {
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                    iv_head.setImageDrawable(resource);
                }
            });
        }
    }

    class CharacterHolder extends RecyclerView.ViewHolder {
        TextView mTitle;

        public CharacterHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.nameTv);
        }
    }

    public interface OnTypeItemClickerListener {
        void contactClick(GroupMember info);
    }

    private OnTypeItemClickerListener typeItemClickerListener;

    public void setTypeItemClickerListener(OnTypeItemClickerListener listener) {
        this.typeItemClickerListener = listener;
    }
}
