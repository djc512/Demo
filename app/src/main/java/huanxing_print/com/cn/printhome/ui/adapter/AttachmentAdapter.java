package huanxing_print.com.cn.printhome.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.ArrayList;
import java.util.HashMap;

import huanxing_print.com.cn.printhome.R;

/**
 * 附件适配器
 * Created by wanghao on 2017/5/22.
 */

public class AttachmentAdapter extends BaseAdapter{
    private Context mContext;
    private ArrayList<String> mAttachments = new ArrayList<String>();
    private LayoutInflater layoutInflater;
    private int mMax;
    private HashMap<Integer, View> viewMap = new HashMap<Integer, View>();
    public AttachmentAdapter(Context context, ArrayList<String> attachments, int max) {
        this.mContext = context;
        mAttachments.addAll(attachments);
        this.mMax = max;
        layoutInflater = LayoutInflater.from(mContext);
        initData(mAttachments);
    }

    private void initData(ArrayList<String> attachments) {
        if(mMax > attachments.size()) {
            mAttachments.add("+");
        }
    }

    public void modifyData(ArrayList<String> attachments) {
        mAttachments.clear();
        mAttachments.addAll(attachments);
        initData(mAttachments);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mAttachments.size();
    }

    @Override
    public Object getItem(int i) {
        return mAttachments.get(i);
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
            view = layoutInflater.inflate(R.layout.item_gridview, null);
            holder.image = (ImageView) view.findViewById(R.id.imageView1);
            view.setTag(holder);
            viewMap.put(i,view);
        }else{
            view = viewMap.get(i);
            holder = (ViewHolder) view.getTag();
        }
        loadPic(holder.image, mAttachments.get(i));
        return view;
    }

    private void loadPic(final ImageView imageView, String picPath) {
        if("+".equals(picPath)){
            Glide.with(mContext).load(R.drawable.add).diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
        }else {
            Glide.with(mContext).load(picPath).placeholder(R.drawable.iv_head).into(new SimpleTarget<GlideDrawable>() {
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                    imageView.setImageDrawable(resource);
                }
            });
        }
    }

    public class ViewHolder {
        public ImageView image;
    }
}
