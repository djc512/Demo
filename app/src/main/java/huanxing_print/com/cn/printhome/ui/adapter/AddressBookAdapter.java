package huanxing_print.com.cn.printhome.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.model.contact.PhoneContactInfo;
import huanxing_print.com.cn.printhome.util.PinYinUtil;
import huanxing_print.com.cn.printhome.util.StringFormatUtil;
import huanxing_print.com.cn.printhome.util.contact.ContactComparator;

/**
 * Created by wanghao on 2017/5/4.
 */

public class AddressBookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private LayoutInflater layoutInflater;
    private List<String> characterList; // 字母List
    private List<String> mContactList; // 联系人名称List（转换成拼音）
    private ArrayList<PhoneContactInfo> contactInfos = new ArrayList<PhoneContactInfo>();

    public AddressBookAdapter(Context context, ArrayList<PhoneContactInfo> contacts) {
        this.mContext = context;
        layoutInflater = LayoutInflater.from(mContext);
        updateData(contacts);
    }

    public enum ITEM_TYPE {
        ITEM_TYPE_CONTACT,
        ITEM_TYPE_CHARACTER
    }

    private void updateData(ArrayList<PhoneContactInfo> contacts) {
        contactInfos.clear();
        Map<String, PhoneContactInfo> map = new HashMap<>();
        ArrayList<PhoneContactInfo> newInfos = new ArrayList<PhoneContactInfo>();
        mContactList = new ArrayList<String>();
        characterList = new ArrayList<String>();
        for (int i = 0; i < contacts.size(); i++) {
            String pinyin = PinYinUtil.getPingYin(contacts.get(i).getPhoneName());
            map.put(pinyin, contacts.get(i));
            mContactList.add(pinyin);
        }
        Collections.sort(mContactList, new ContactComparator());

        for (String pinyin : mContactList) {
            PhoneContactInfo info = map.get(pinyin);
            if (null != info) {
                String character = (pinyin.charAt(0) + "").toUpperCase(Locale.ENGLISH);
                if (!characterList.contains(character)) {
                    if (character.hashCode() >= "A".hashCode() && character.hashCode() <= "Z".hashCode()) { // 是字母
                        characterList.add(character);
                        PhoneContactInfo characterInfo = new PhoneContactInfo();
                        characterInfo.setType(ITEM_TYPE.ITEM_TYPE_CHARACTER.ordinal());
                        characterInfo.setPhoneName(character);
                        newInfos.add(characterInfo);
                    } else {
                        if (!characterList.contains("#")) {
                            characterList.add("#");
                            PhoneContactInfo characterInfo = new PhoneContactInfo();
                            characterInfo.setType(ITEM_TYPE.ITEM_TYPE_CHARACTER.ordinal());
                            characterInfo.setPhoneName("#");
                            newInfos.add(characterInfo);
                        }
                    }
                }
                info.setType(ITEM_TYPE.ITEM_TYPE_CONTACT.ordinal());
                newInfos.add(info);
            }
        }
        contactInfos.addAll(newInfos);
    }

    public void modifyData(ArrayList<PhoneContactInfo> contacts) {
        updateData(contacts);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (viewType == ITEM_TYPE.ITEM_TYPE_CHARACTER.ordinal()) {
            View view = layoutInflater.inflate(R.layout.item_contact_character, null);
            view.setLayoutParams(layoutParams);
            return new CharacterHolder(view);
        } else {
            View view = layoutInflater.inflate(R.layout.item_contact_add_by_phone, null);
            view.setLayoutParams(layoutParams);
            return new PhoneContactHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PhoneContactHolder) {
            ((PhoneContactHolder) holder).bind(position);
        } else if (holder instanceof CharacterHolder) {
            ((CharacterHolder) holder).bind(position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return contactInfos.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return contactInfos == null ? 0 : contactInfos.size();
    }

    class CharacterHolder extends RecyclerView.ViewHolder {
        TextView mTitle;

        public CharacterHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.nameTv);
        }

        public void bind(int position){
            PhoneContactInfo info = contactInfos.get(position);
            if(null != info) {
                mTitle.setText(info.getPhoneName());
            }
        }
    }

    class PhoneContactHolder extends RecyclerView.ViewHolder {
        CircleImageView icon;
        TextView tv_phoneName;
        TextView tv_yjNum;
        Button btn_add;
        TextView tv_friend_state;
        private int mPosition;

        public PhoneContactHolder(View itemView) {
            super(itemView);
            icon = (CircleImageView) itemView.findViewById(R.id.iv_head);
            tv_phoneName = (TextView) itemView.findViewById(R.id.tv_phoneName);
            tv_yjNum = (TextView) itemView.findViewById(R.id.tv_yjNum);
            btn_add = (Button) itemView.findViewById(R.id.btn_add);
            tv_friend_state = (TextView) itemView.findViewById(R.id.friend_state);
            tv_friend_state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(null != optionListener) {
                        PhoneContactInfo info = contactInfos.get(mPosition);
                        optionListener.invitation(info);
                    }
                }
            });
            btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(null != optionListener) {
                        PhoneContactInfo info = contactInfos.get(mPosition);
                        optionListener.addFriend(info);
                    }
                }
            });
        }

        public void bind(int position) {
            this.mPosition = position;
            PhoneContactInfo info = contactInfos.get(position);
            if (null != info) {
                tv_phoneName.setText(info.getPhoneName());
                tv_yjNum.setText(info.getPhoneNum());
                if(info.getFriendState() == PhoneContactInfo.STATE.FRIEND.ordinal()) {
                    btn_add.setVisibility(View.GONE);
                    tv_friend_state.setVisibility(View.GONE);
                }else if(info.getFriendState() == PhoneContactInfo.STATE.NOTFRIEND.ordinal()){
                    btn_add.setVisibility(View.VISIBLE);
                    tv_friend_state.setVisibility(View.GONE);
                }else if(info.getFriendState() == PhoneContactInfo.STATE.UNREGISTED.ordinal()) {
                    btn_add.setVisibility(View.GONE);
                    tv_friend_state.setVisibility(View.VISIBLE);
                    String wholeStr = "还不是印家用户,立即邀请";
                    StringFormatUtil spanStr = new StringFormatUtil(mContext, wholeStr,
                            "立即邀请", R.color.stepline_red).fillColor();
                    tv_friend_state.setText(spanStr.getResult());
                }
            }
        }
    }
    public int getScrollPosition(String character) {
        if (characterList.contains(character)) {
            for (int i = 0; i < contactInfos.size(); i++) {
                String name = contactInfos.get(i).getPhoneName();
                if(name !=null && name.equals(character)){
                    return i;
                }
            }
        }

        return -1; // -1不会滑动
    }

    private OnOptionListener optionListener;
    public interface OnOptionListener{
        void invitation(PhoneContactInfo info);
        void addFriend(PhoneContactInfo info);
    }

    public void setOnOptionListener(OnOptionListener listener) {
        this.optionListener = listener;
    }

}