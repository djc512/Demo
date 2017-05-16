package huanxing_print.com.cn.printhome.ui.adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.DateUtils;

import java.io.File;
import java.util.Date;
import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.constant.ConFig;

/**
 * 作者： itheima
 * 时间：2016-10-19 09:37
 * 网址：http://www.itheima.com
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private static final int TEXT = 0;//文本
    private static final int PIC = 1;//图片
    private List<EMMessage> mEMMessageList;
    private int mKind;
    private Context ctx;

    public ChatAdapter(Context ctx,List<EMMessage> EMMessageList) {
        mEMMessageList = EMMessageList;
        this.ctx= ctx;
    }

    @Override
    public int getItemCount() {
        return mEMMessageList == null ? 0 : mEMMessageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        EMMessage emMessage = mEMMessageList.get(position);
        return emMessage.direct() == EMMessage.Direct.RECEIVE ? 0 : 1;
    }

    /**
     * @param parent
     * @param viewType 在调用该方法之前系统会先调用getItemViewType方法
     * @return
     */
    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_receiver, parent, false);
        } else if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_send, parent, false);
        }
        ChatViewHolder chatViewHolder = new ChatViewHolder(view);
        return chatViewHolder;
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        EMMessage emMessage = mEMMessageList.get(position);
        long msgTime = emMessage.getMsgTime();

        try {
            mKind = emMessage.getIntAttribute("kind");
            Log.i("CMCC", "kind=========="+mKind);
            if (mKind==TEXT){
                //需要将消息body转换为EMTextMessageBody
                Log.i("CMCC", "kind==========111111111111111111111");
                EMTextMessageBody body = (EMTextMessageBody) emMessage.getBody();
                String message = body.getMessage();
                holder.mTvMsg.setText(message);
                Log.i("CMCC", "44444444444444444444"+message);
                holder.mPic.setVisibility(View.GONE);
                holder.mTvMsg.setVisibility(View.VISIBLE);
            }else if(mKind==PIC){
                Log.i("CMCC", "kind==========2222222222222222222222");

                holder.mPic.setVisibility(View.VISIBLE);
                holder.mTvMsg.setVisibility(View.GONE);
                EMImageMessageBody body = (EMImageMessageBody) emMessage.getBody();
                String url = body.getFileName();
                File file = new File(ConFig.IMG_SAVE);
                if(!file.exists()){
                    file.mkdirs();
                }
                body.setThumbnailUrl(ConFig.IMG_SAVE);
                body.getThumbnailUrl();
                Log.i("CMCC", "3333333333333333333333333"+url);
                /*Glide.with(ctx).load(url)
                        .error(R.drawable.error_photo).into(holder.mPic);*/
                Glide.with(ctx).load(new File(ConFig.IMG_SAVE,url)).error(R.drawable.error_photo).into(holder.mPic);

                // 压缩图片
                /*Bitmap bitmap = BitmapLoadUtils.decodeSampledBitmapFromFd(url, 400, 500);
                holder.mPic.setImageBitmap(Bimp.tempSelectBitmap.get(position).getBitmap());
                holder.mTvMsg.setVisibility(View.GONE);*/
                //BitmapFactory.decodeResource(getResources(), R.drawable.add);
            }else{

            }

        } catch (HyphenateException e) {
            e.printStackTrace();
        }


        holder.mTvTime.setText(DateUtils.getTimestampString(new Date(msgTime)));
        if (position==0){
            holder.mTvTime.setVisibility(View.VISIBLE);
        }else{
            EMMessage preMessage = mEMMessageList.get(position - 1);
            long preMsgTime = preMessage.getMsgTime();
            if (DateUtils.isCloseEnough(msgTime,preMsgTime)){
                holder.mTvTime.setVisibility(View.GONE);
            }else{
                holder.mTvTime.setVisibility(View.VISIBLE);
            }
        }
        if (emMessage.direct()== EMMessage.Direct.SEND){
            switch (emMessage.status()) {
                case INPROGRESS:
                    holder.mIvState.setVisibility(View.VISIBLE);
                    holder.mIvState.setImageResource(R.drawable.msg_state_animation);
                    AnimationDrawable drawable = (AnimationDrawable) holder.mIvState.getDrawable();
                    if (drawable.isRunning()){
                        drawable.stop();
                    }
                    drawable.start();
                    break;
                case SUCCESS:
                    holder.mIvState.setVisibility(View.GONE);
                    break;
                case FAIL:
                    holder.mIvState.setVisibility(View.VISIBLE);
                    holder.mIvState.setImageResource(R.mipmap.msg_error);
                    break;
            }
        }
    }


    class ChatViewHolder extends RecyclerView.ViewHolder {

        TextView mTvTime;
        TextView mTvMsg;
        ImageView mIvState;
        ImageView mPic;

        public ChatViewHolder(View itemView) {
            super(itemView);
            mTvTime = (TextView) itemView.findViewById(R.id.tv_time);
            mTvMsg = (TextView) itemView.findViewById(R.id.tv_msg);
            mIvState = (ImageView) itemView.findViewById(R.id.iv_state);
            mPic = (ImageView) itemView.findViewById(R.id.iv_pic);
        }
    }
    private void loadPic(final ImageView iv_user_head, String picPath) {
        Glide.with(ctx).load(picPath).placeholder(R.drawable.iv_head).into(new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                iv_user_head.setImageDrawable(resource);
            }
        });
    }

}
