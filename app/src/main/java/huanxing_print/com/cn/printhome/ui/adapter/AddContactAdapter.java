package huanxing_print.com.cn.printhome.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.model.contact.ContactInfo;

/**
 * Created by wanghao on 2017/5/3.
 */

public class AddContactAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ArrayList<ContactInfo> addContacts;
    private LayoutInflater mLayoutInflater;
    public AddContactAdapter(Context context, ArrayList<ContactInfo> infos) {
        this.mContext = context;
        this.addContacts = infos;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void updateContacts(ArrayList<ContactInfo> infos) {
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
            ContactInfo info = addContacts.get(position);
            if(null != info) {
                yjNumTv.setText(info.getYjNum());
                yjNameTv.setText(info.getName());
                if(info.isAddRequest() || info.isFriend()) {
                    addBtn.setEnabled(false);
                }else{
                    addBtn.setEnabled(true);
                }
            }
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
        void send(int poisition);
    }

    private OnItemSendListener itemSendListener;
    public void setOnItemSendListener(OnItemSendListener listener) {
        this.itemSendListener = listener;
    }
}