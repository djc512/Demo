package com.hyphenate.easeui.widget.chatrow;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMFileMessageBody;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessage.ChatType;
import com.hyphenate.easeui.model.EaseImageCache;
import com.hyphenate.easeui.ui.EaseShowBigImageActivity;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.utils.EaseImageUtils;

import java.io.File;
import java.util.ArrayList;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.util.CircleTransform;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.view.popupwindow.PopupList;

public class EaseChatRowImage extends EaseChatRowFile {

    protected ImageView imageView;
    private EMImageMessageBody imgBody;
    private ImageView iv_userhead;
    private TextView tv_userid;
    private RelativeLayout bubble;
    private LinearLayout lin_group;
    private ArrayList<String> popupMenuItemList;

    public EaseChatRowImage(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflateView() {
        inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ?
                R.layout.ease_row_received_picture : R.layout.ease_row_sent_picture, this);
    }

    @Override
    protected void onFindViewById() {
        percentageView = (TextView) findViewById(R.id.percentage);
        imageView = (ImageView) findViewById(R.id.image);
        iv_userhead = (ImageView) findViewById(R.id.iv_userhead);
        tv_userid = (TextView) findViewById(R.id.tv_userid);
        bubble = (RelativeLayout) findViewById(R.id.bubble);
        lin_group = (LinearLayout) findViewById(R.id.lin_group);
    }


    @Override
    protected void onSetUpView() {
        String iconUrl = message.getStringAttribute("iconUrl", "");
        String nickName = message.getStringAttribute("nickName", "");
        //头像
        if (ObjectUtils.isNull(iconUrl)) {
            Glide.with(getContext())
                    .load(R.drawable.iv_head)
                    .transform(new CircleTransform(getContext()))
                    .into(iv_userhead);
        } else {
            Glide.with(getContext())
                    .load(iconUrl)
                    .transform(new CircleTransform(getContext()))
                    .into(iv_userhead);
        }
        //昵称
        if (ObjectUtils.isNull(nickName)) {
            tv_userid.setText(message.getFrom());
        } else {
            tv_userid.setText(nickName);
        }

        imgBody = (EMImageMessageBody) message.getBody();
        // received messages
        if (message.direct() == EMMessage.Direct.RECEIVE) {
            if (imgBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.DOWNLOADING ||
                    imgBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.PENDING) {
                imageView.setImageResource(R.drawable.ease_default_image);
                setMessageReceiveCallback();
            } else {
                progressBar.setVisibility(View.GONE);
                percentageView.setVisibility(View.GONE);
                imageView.setImageResource(R.drawable.ease_default_image);
                String thumbPath = imgBody.thumbnailLocalPath();
                if (!new File(thumbPath).exists()) {
                    // to make it compatible with thumbnail received in previous version
                    thumbPath = EaseImageUtils.getThumbnailImagePath(imgBody.getLocalUrl());
                }
                showImageView(thumbPath, imageView, imgBody.getLocalUrl(), message);
            }
            return;
        }

        String filePath = imgBody.getLocalUrl();
        String thumbPath = EaseImageUtils.getThumbnailImagePath(imgBody.getLocalUrl());
        showImageView(thumbPath, imageView, filePath, message);
//        //给图片设置长按事件
//        bubble.setOnLongClickListener(new OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//
//
//                return true;
//            }
//        });

        handleSendMessage();
    }

    @Override
    protected void onUpdateView() {
        super.onUpdateView();
    }

    @Override
    protected void onBubbleClick() {
        Intent intent = new Intent(context, EaseShowBigImageActivity.class);
        File file = new File(imgBody.getLocalUrl());
        if (file.exists()) {
            Uri uri = Uri.fromFile(file);
            intent.putExtra("uri", uri);
        } else {
            // The local full size pic does not exist yet.
            // ShowBigImage needs to download it from the server
            // first
            String msgId = message.getMsgId();
            intent.putExtra("messageId", msgId);
            intent.putExtra("localUrl", imgBody.getLocalUrl());
        }
        if (message != null && message.direct() == EMMessage.Direct.RECEIVE && !message.isAcked()
                && message.getChatType() == ChatType.Chat) {
            try {
                EMClient.getInstance().chatManager().ackMessageRead(message.getFrom(), message.getMsgId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        context.startActivity(intent);
    }

    /**
     * load image into image view
     *
     * @param thumbernailPath
     * @param iv
     * @return the image exists or not
     */
    private boolean showImageView(final String thumbernailPath, final ImageView iv, final String localFullSizePath, final EMMessage message) {
        // first check if the thumbnail image already loaded into cache
        Bitmap bitmap = EaseImageCache.getInstance().get(thumbernailPath);
        if (bitmap != null) {
            // thumbnail image is already loaded, reuse the drawable
            iv.setImageBitmap(bitmap);
            return true;
        } else {
            new AsyncTask<Object, Void, Bitmap>() {

                @Override
                protected Bitmap doInBackground(Object... args) {
                    File file = new File(thumbernailPath);
                    if (file.exists()) {
                        return EaseImageUtils.decodeScaleImage(thumbernailPath, 265, 265);
                    } else if (new File(imgBody.thumbnailLocalPath()).exists()) {
                        return EaseImageUtils.decodeScaleImage(imgBody.thumbnailLocalPath(), 265, 265);
                    } else {
                        if (message.direct() == EMMessage.Direct.SEND) {
                            if (localFullSizePath != null && new File(localFullSizePath).exists()) {
                                return EaseImageUtils.decodeScaleImage(localFullSizePath, 265, 265);
                            } else {
                                return null;
                            }
                        } else {
                            return null;
                        }
                    }
                }

                protected void onPostExecute(Bitmap image) {
                    if (image != null) {
                        iv.setImageBitmap(image);
                        EaseImageCache.getInstance().put(thumbernailPath, image);
                    } else {
                        if (message.status() == EMMessage.Status.FAIL) {
                            if (EaseCommonUtils.isNetWorkConnected(activity)) {
                                new Thread(new Runnable() {

                                    @Override
                                    public void run() {
                                        EMClient.getInstance().chatManager().downloadThumbnail(message);
                                    }
                                }).start();
                            }
                        }

                    }
                }
            }.execute();

            return true;
        }
    }

    @Override
    protected void onBubbleLongClick() {
        Log.d("CMCC", "onBubbleLongClick触发了");
        popupMenuItemList = new ArrayList<>();
        popupMenuItemList.add("打印");
        popupMenuItemList.add("转发");
        popupMenuItemList.add("保存");
        popupMenuItemList.add("删除");
        PopupList popupList = new PopupList(context);
        popupList.bind(bubble, popupMenuItemList, new PopupList.PopupListListener() {
            @Override
            public boolean showPopupList(View adapterView, View contextView, int contextPosition) {
                return true;
            }

            @Override
            public void onPopupListClick(View contextView, int contextPosition, int position) {
                switch (position) {
                    case 0:
                        //打印
                        Log.d("CMCC", "打印");
                        break;
                    case 1:
                        //转发
                        Log.d("CMCC", "转发");
                        break;
                    case 2:
                        //保存
                        Log.d("CMCC", "保存");
                        final String imgUrl = message.getStringAttribute("imagePath", "");
                        Log.d("CMCC", "imgUrl:" + imgUrl);
                        Bitmap bitmap = BitmapFactory.decodeFile(imgUrl);
                        MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, System.currentTimeMillis() + "", "description");
                        //刷新相册
                        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(imgUrl))));
                        break;
                    case 3:
                        //删除记录
                        Log.d("CMCC", "删除");
                        //发送透传消息代码如下：
                        String action = context.getString(R.string.REMOVE);
                        EMMessage cmdMessage = EMMessage.createSendMessage(EMMessage.Type.CMD);

                        EMCmdMessageBody cmdBody = new EMCmdMessageBody(action);
                        Log.d("CMCC", "111111");
                        if (message.getChatType() == ChatType.GroupChat ||
                                message.getChatType() == ChatType.ChatRoom) {
                            cmdMessage.setChatType(ChatType.GroupChat);
                            String toChatUserName = message.getTo();
                            Log.d("CMCC", "toChatUserName------>" + toChatUserName);
                            cmdMessage.setTo(toChatUserName);
                            //删除掉本地消息
                            EMClient.getInstance().chatManager()
                                    .getConversation(toChatUserName).removeMessage(message.getMsgId());
                            //刷新一下
                            updateView();
                        } else {
                            String toChatUserName = message.getFrom();
                            Log.d("CMCC", "toChatUserName------>" + toChatUserName);
                            cmdMessage.setFrom(toChatUserName);
                            //删除掉本地消息
                            EMClient.getInstance().chatManager()
                                    .getConversation(toChatUserName).removeMessage(message.getMsgId());
                            //刷新一下
                            updateView();
                        }
                        String msgId = message.getMsgId();
                        Log.d("CMCC", "msgIdsend-------->" + msgId);
                        cmdMessage.setAttribute("msgid", msgId);
                        cmdMessage.addBody(cmdBody);
                        EMClient.getInstance().chatManager().sendMessage(cmdMessage);
                        Log.d("CMCC", "发送透传success");
                        break;
                }
            }
        });
    }
}
