package huanxing_print.com.cn.printhome.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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
 * Created by wanghao on 2017/5/5.
 */

public class ChooseGroupMemberAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<GroupMember> mInfos = new ArrayList<GroupMember>();
    private Context mContext;
    private LayoutInflater mInflater;
    private List<String> characterList; // 字母List
    private List<String> mContactList; // 联系人名称List（转换成拼音）
    private ArrayList<GroupMember> chooseInfos = new ArrayList<GroupMember>();

    public ChooseGroupMemberAdapter(Context context, ArrayList<GroupMember> contactInfos) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
//        initData();
        modifyData(contactInfos);
    }

    public enum ITEM_TYPE {
        ITEM_CONTACT,
        ITEM_GROUP_IN,
        ITEM_CHARACTER
    }


    private void modifyData(ArrayList<GroupMember> contactInfos) {
        mInfos.clear();
        addGroup(contactInfos);
    }

    public void modify(ArrayList<GroupMember> contactInfos) {
        modifyData(contactInfos);
        notifyDataSetChanged();
    }

    private void addGroup(ArrayList<GroupMember> infos) {
        Map<String, GroupMember> map = new HashMap<>();
        ArrayList<GroupMember> newInfos = new ArrayList<GroupMember>();
        mContactList = new ArrayList<String>();
        characterList = new ArrayList<String>();
        for (int i = 0; i < infos.size(); i++) {
            String pinyin = PinYinUtil.getPingYin(infos.get(i).getMemberName());
            map.put(pinyin, infos.get(i));
            mContactList.add(pinyin);
        }
        Collections.sort(mContactList, new ContactComparator());

        for (String pinyin : mContactList) {
            GroupMember info = map.get(pinyin);
            if (null != info) {
                String character = (pinyin.charAt(0) + "").toUpperCase(Locale.ENGLISH);
                if (!characterList.contains(character)) {
                    if (character.hashCode() >= "A".hashCode() && character.hashCode() <= "Z".hashCode()) { // 是字母
                        characterList.add(character);
                        GroupMember characterInfo = new GroupMember();
                        characterInfo.setmType(ITEM_TYPE.ITEM_CHARACTER.ordinal());
                        characterInfo.setMemberName(character);
                        newInfos.add(characterInfo);
                    } else {
                        if (!characterList.contains("#")) {
                            characterList.add("#");
                            GroupMember characterInfo = new GroupMember();
                            characterInfo.setmType(ITEM_TYPE.ITEM_CHARACTER.ordinal());
                            characterInfo.setMemberName("#");
                            newInfos.add(characterInfo);
                        }
                    }
                }
                info.setmType(ITEM_TYPE.ITEM_CONTACT.ordinal());
                newInfos.add(info);
            }

        }
        mInfos.addAll(newInfos);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (viewType == ITEM_TYPE.ITEM_GROUP_IN.ordinal()) {
            View view = mInflater.inflate(R.layout.item_add_from_group, null);
            view.setLayoutParams(layoutParams);
            return new ChooseGroupHolder(view);
        } else if (viewType == ITEM_TYPE.ITEM_CHARACTER.ordinal()) {
            View view = mInflater.inflate(R.layout.item_contact_character, null);
            view.setLayoutParams(layoutParams);
            return new CharacterHolder(view);
        } else {
            View view = mInflater.inflate(R.layout.item_group_choose_member, null);
            view.setLayoutParams(layoutParams);
            return new MemberHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MemberHolder) {
            ((MemberHolder) holder).bind(position);
        } else if (holder instanceof CharacterHolder) {
            ((CharacterHolder) holder).bind(position);
        }
    }

    @Override
    public int getItemCount() {
        return mInfos == null ? 0 : mInfos.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mInfos.get(position).getmType();
    }


    class CharacterHolder extends RecyclerView.ViewHolder {
        TextView mTitle;

        public CharacterHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.nameTv);
        }

        public void bind(int position) {
            GroupMember info = mInfos.get(position);
            mTitle.setText(info.getMemberName());
        }
    }

    class ChooseGroupHolder extends RecyclerView.ViewHolder {

        public ChooseGroupHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != clickGroupInListener) {
                        clickGroupInListener.clickGroup();
                    }
                }
            });
        }
    }

    class MemberHolder extends RecyclerView.ViewHolder {
        private TextView tv_contactName;
        private CircleImageView im_contactIcon;
        private CheckBox cb_choose;
        private GroupMember contactInfo;

        public MemberHolder(View itemView) {
            super(itemView);
            tv_contactName = (TextView) itemView.findViewById(R.id.contactName);
            im_contactIcon = (CircleImageView) itemView.findViewById(R.id.contactIcon);
            cb_choose = (CheckBox) itemView.findViewById(R.id.contactCheck);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (contactInfo != null && chooseInfos.size() <= mInfos.size()) {
                        if (cb_choose.isChecked()) {
                            cb_choose.setChecked(false);
                            chooseInfos.remove(contactInfo);
                            callbackChoose();
                        } else {
                            if (chooseInfos.size() != mInfos.size()) {
                                cb_choose.setChecked(true);
                                chooseInfos.add(contactInfo);
                                callbackChoose();
                            }
                        }
                    }

                }
            });

        }

        public void bind(int position) {
            contactInfo = mInfos.get(position);
            if (null != contactInfo) {
                tv_contactName.setText(contactInfo.getMemberName());
                loadPic();
            }

        }

        private void loadPic() {
            Glide.with(mContext).load(contactInfo.getMemberUrl()).placeholder(R.drawable.iv_head).into(new SimpleTarget<GlideDrawable>() {
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                    im_contactIcon.setImageDrawable(resource);
                }
            });
        }
    }

    private void callbackChoose() {
        if (null != chooseMemberListener) {
            chooseMemberListener.choose(chooseInfos);
        }
    }

    public interface OnClickGroupInListener {
        void clickGroup();
    }

    private OnClickGroupInListener clickGroupInListener;

    public void setOnClickGroupInListener(OnClickGroupInListener listener) {
        this.clickGroupInListener = listener;
    }

    public interface OnChooseMemberListener {
        void choose(ArrayList<GroupMember> infos);
    }

    private OnChooseMemberListener chooseMemberListener;

    public void setOnChooseMemberListener(OnChooseMemberListener listener) {
        this.chooseMemberListener = listener;
    }

}
