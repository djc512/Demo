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
import huanxing_print.com.cn.printhome.model.contact.FriendInfo;
import huanxing_print.com.cn.printhome.util.PinYinUtil;
import huanxing_print.com.cn.printhome.util.contact.ContactComparator;

/**
 * Created by wanghao on 2017/5/2.
 */

public class ContactsItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private ArrayList<FriendInfo> mInitInfos = new ArrayList<FriendInfo>();
    private ArrayList<FriendInfo> mInfos = new ArrayList<FriendInfo>();
    private List<String> characterList; // 字母List
    private List<String> mFriends; // 联系人名称List（转换成拼音）
    private OnTypeItemClickerListener typeItemClickerListener;
    public ContactsItemAdapter(Context context, ArrayList<FriendInfo> infos) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        initData();
        updateData(infos);
    }

    public enum ITEM_TYPE {
        ITEM_TYPE_NEWFRIEND,
        ITEM_TYPE_ADDRESSBOOK,
        ITEM_TYPE_GROUP,
        ITEM_TYPE_FRIEND,
        ITEM_TYPE_CHARACTER
    }

    private void initData() {
        FriendInfo newFriend = new FriendInfo();
        newFriend.setType(ITEM_TYPE.ITEM_TYPE_NEWFRIEND.ordinal());

        FriendInfo addressBook = new FriendInfo();
        addressBook.setType(ITEM_TYPE.ITEM_TYPE_ADDRESSBOOK.ordinal());

        FriendInfo group = new FriendInfo();
        group.setType(ITEM_TYPE.ITEM_TYPE_GROUP.ordinal());

        mInitInfos.add(newFriend);
        mInitInfos.add(addressBook);
        mInitInfos.add(group);
    }

    public void updateData(ArrayList<FriendInfo> infos) {
        mInfos.clear();
        mInfos.addAll(mInitInfos);
        addFriend(infos);
    }

    public void modify(ArrayList<FriendInfo> infos) {
        updateData(infos);
        notifyDataSetChanged();
    }

    private void addFriend(ArrayList<FriendInfo> infos) {
        Map<String, FriendInfo> map = new HashMap<String, FriendInfo>();
        ArrayList<FriendInfo> newInfos = new ArrayList<FriendInfo>();
        mFriends = new ArrayList<String>();
        characterList = new ArrayList<String>();
        for (int i = 0; i < infos.size(); i++) {
            String pinyin = PinYinUtil.getPingYin(infos.get(i).getMemberName());
            map.put(pinyin, infos.get(i));
            mFriends.add(pinyin);
        }
        Collections.sort(mFriends, new ContactComparator());

        for (String pinyin : mFriends) {
            FriendInfo info = map.get(pinyin);
            if (null != info) {
                String character = (pinyin.charAt(0) + "").toUpperCase(Locale.ENGLISH);
                if (!characterList.contains(character)) {
                    if (character.hashCode() >= "A".hashCode() && character.hashCode() <= "Z".hashCode()) { // 是字母
                        characterList.add(character);
                        FriendInfo characterInfo = new FriendInfo();
                        characterInfo.setType(ITEM_TYPE.ITEM_TYPE_CHARACTER.ordinal());
                        characterInfo.setMemberName(character);
                        newInfos.add(characterInfo);
                    } else {
                        if (!characterList.contains("#")) {
                            characterList.add("#");
                            FriendInfo characterInfo = new FriendInfo();
                            characterInfo.setType(ITEM_TYPE.ITEM_TYPE_CHARACTER.ordinal());
                            characterInfo.setMemberName("#");
                            newInfos.add(characterInfo);
                        }
                    }
                }
                info.setType(ITEM_TYPE.ITEM_TYPE_FRIEND.ordinal());
                newInfos.add(info);
            }

        }
        mInfos.addAll(newInfos);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (viewType == ITEM_TYPE.ITEM_TYPE_NEWFRIEND.ordinal()) {
            View view = mLayoutInflater.inflate(R.layout.item_contact_newfriend, null);
            view.setLayoutParams(layoutParams);
            return new NewFriendHolder(view);
        } else if (viewType == ITEM_TYPE.ITEM_TYPE_ADDRESSBOOK.ordinal()) {
            View view = mLayoutInflater.inflate(R.layout.item_contact_addressbook, null);
            view.setLayoutParams(layoutParams);
            return new AddressBookHolder(view);
        } else if (viewType == ITEM_TYPE.ITEM_TYPE_GROUP.ordinal()) {
            View view = mLayoutInflater.inflate(R.layout.item_contact_group, null);
            view.setLayoutParams(layoutParams);
            return new GroupHolder(view);
        } else if (viewType == ITEM_TYPE.ITEM_TYPE_CHARACTER.ordinal()) {
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
        if (holder instanceof NewFriendHolder) {
//            ((NewFriendHolder) holder).mTitle.setText(mInfos.get(position).getName());
        } else if (holder instanceof AddressBookHolder) {
//            ((AddressBookHolder) holder).mTitle.setText(mInfos.get(position).getName());
        } else if (holder instanceof GroupHolder) {
//            ((GroupHolder) holder).mTitle.setText(mInfos.get(position).getName());
        } else if(holder instanceof CharacterHolder){
            ((CharacterHolder) holder).mTitle.setText(mInfos.get(position).getMemberName());
        }else{
            ((FriendHolder) holder).bind(mInfos.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mInfos == null ? 0 : mInfos.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mInfos.get(position).getType();
    }

    class NewFriendHolder extends RecyclerView.ViewHolder {
        TextView mTitle;

        public NewFriendHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.nameTv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != typeItemClickerListener) {
                       typeItemClickerListener.newFriendLister();
                    }
                }
            });
        }
    }

    class AddressBookHolder extends RecyclerView.ViewHolder {
        TextView mTitle;

        public AddressBookHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.nameTv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != typeItemClickerListener) {
                        typeItemClickerListener.addressBookListener();
                    }
                }
            });
        }
    }

    class GroupHolder extends RecyclerView.ViewHolder {
        TextView mTitle;

        public GroupHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.nameTv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != typeItemClickerListener) {
                        typeItemClickerListener.groupListener();
                    }
                }
            });
        }
    }

    class FriendHolder extends RecyclerView.ViewHolder {
        TextView mTitle;
        CircleImageView iv_head;
        private FriendInfo friendInfo;

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

        public void bind(FriendInfo info) {
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

    public int getScrollPosition(String character) {
        if (characterList.contains(character)) {
            for (int i = 0; i < mInfos.size(); i++) {
                String name = mInfos.get(i).getMemberName();
                if(name !=null && name.equals(character)){
                    return i;
                }
            }
        }

        return -1; // -1不会滑动
    }


    public interface OnTypeItemClickerListener {
        void newFriendLister();
        void addressBookListener();
        void groupListener();
        void contactClick(FriendInfo info);
    }

    public void setTypeItemClickerListener(OnTypeItemClickerListener listener) {
        this.typeItemClickerListener = listener;
    }

}
