package huanxing_print.com.cn.printhome.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.model.approval.Attachment;
import huanxing_print.com.cn.printhome.util.BitmapUtils;

/**
 * Created by wanghao on 2017/5/14.
 */

public class AttachmentAdatper extends BaseAdapter{
    private Context mContext;
    private ArrayList<Attachment> attachmentInfos = new ArrayList<Attachment>();
    private LayoutInflater layoutInflater;
    public AttachmentAdatper(Context context, ArrayList<Attachment> attachments) {
        this.mContext = context;
        this.attachmentInfos = attachments;
        layoutInflater = LayoutInflater.from(mContext);
    }

    public void modifyData(ArrayList<Attachment> attachments) {
        this.attachmentInfos = attachments;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return attachmentInfos.size();
    }

    @Override
    public Object getItem(int i) {
        return attachmentInfos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(null  == view) {
            holder = new ViewHolder();
            view = layoutInflater.inflate(R.layout.item_attachment, null);
            holder.im_attachment = (ImageView) view.findViewById(R.id.attachement_pic);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        loadPic(holder.im_attachment, attachmentInfos.get(i).getFileUrl());
        return view;
    }

    private void loadPic(final ImageView iv_user_head, String picPath) {
//        Glide.with(mContext).load(picPath).placeholder(R.drawable.iv_head).into(new SimpleTarget<GlideDrawable>() {
//            @Override
//            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
//                iv_user_head.setImageDrawable(resource);
//            }
//        });
        BitmapUtils.displayImage(mContext, picPath,R.drawable.icon_error, iv_user_head);
    }

    class ViewHolder{
        ImageView im_attachment;
    }
}
