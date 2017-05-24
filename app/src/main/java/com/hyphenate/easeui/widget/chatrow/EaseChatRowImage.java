package com.hyphenate.easeui.widget.chatrow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMFileMessageBody;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessage.ChatType;
import com.hyphenate.easeui.model.EaseImageCache;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.utils.EaseImageUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.model.chat.RefreshEvent;
import huanxing_print.com.cn.printhome.ui.activity.chat.CreateGroupChatActivity;
import huanxing_print.com.cn.printhome.util.CircleTransform;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.util.PreViewUtil;
import huanxing_print.com.cn.printhome.util.ToastUtil;
import huanxing_print.com.cn.printhome.util.ToastUtils;
import huanxing_print.com.cn.printhome.view.popupwindow.PopupList;

public class EaseChatRowImage extends EaseChatRowFile {

    protected ImageView imageView;
    private EMImageMessageBody imgBody;
    private ImageView iv_userhead;
    private TextView tv_userid;
    private RelativeLayout bubble;
    private LinearLayout lin_group;
    private ArrayList<String> popupMenuItemList;
    private String localFilePath;

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
        EMImageMessageBody body = (EMImageMessageBody) message.getBody();
        localFilePath = body.getLocalUrl();
        Log.d("CMCC", "localFilePath:" + localFilePath);
        //设置图片的宽高
        setImgSize();

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
        handleSendMessage();
    }

    private void setImgSize() {
        EMImageMessageBody em = (EMImageMessageBody) message.getBody();
        float dimensWidth, dimensHeight;
        float density = activity.getResources().getDisplayMetrics().density;

        /*if (em.getWidth() > em.getHeight()) {
            dimensWidth = 140f;
            dimensHeight = dimensWidth * (em.getHeight() / em.getWidth());
            int finalDimensWidth = (int) (dimensWidth * density + 0.5f);
            int finalDimensHeight = (int) (dimensHeight * density + 0.5f);
            LinearLayout.LayoutParams imgvwDimens =
                    new LinearLayout.LayoutParams(finalDimensWidth, finalDimensWidth);
            imageView.setLayoutParams(imgvwDimens);
        } else {
            dimensHeight = 140f;
            dimensWidth = dimensHeight * (em.getWidth() / em.getHeight());
            int finalDimensWidth = (int) (dimensWidth * density + 0.5f);
            int finalDimensHeight = (int) (dimensHeight * density + 0.5f);
            LinearLayout.LayoutParams imgvwDimens =
                    new LinearLayout.LayoutParams(finalDimensHeight, finalDimensHeight);
            imageView.setLayoutParams(imgvwDimens);
        }*/
        // SET SCALETYPE
        dimensWidth = 140f;
        int finalDimensWidth = (int) (dimensWidth * density + 0.5f);
        LinearLayout.LayoutParams imgvwDimens =
                new LinearLayout.LayoutParams(finalDimensWidth, finalDimensWidth);
        imageView.setLayoutParams(imgvwDimens);

        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
    }

    @Override
    protected void onUpdateView() {
        super.onUpdateView();
    }

    @Override
    protected void onBubbleClick() {
        File file = new File(localFilePath);
        if (!ObjectUtils.isNull(file) && file.exists()) {
            //预览
            PreViewUtil.preview(context, localFilePath);
        } else {
            downloadImage(message.getMsgId());
        }
//        Intent intent = new Intent(context, EaseShowBigImageActivity.class);
//        File file = new File(imgBody.getLocalUrl());
//        if (file.exists()) {
//            Uri uri = Uri.fromFile(file);
//            intent.putExtra("uri", uri);
//        } else {
//            // The local full size pic does not exist yet.
//            // ShowBigImage needs to download it from the server
//            // first
//            String msgId = message.getMsgId();
//            intent.putExtra("messageId", msgId);
//            intent.putExtra("localUrl", imgBody.getLocalUrl());
//        }
//        if (message != null && message.direct() == EMMessage.Direct.RECEIVE && !message.isAcked()
//                && message.getChatType() == ChatType.Chat) {
//            try {
//                EMClient.getInstance().chatManager().ackMessageRead(message.getFrom(), message.getMsgId());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        context.startActivity(intent);
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

        File file = new File(localFilePath);
        if (!file.exists()) {
            //不存在就去下载
            ToastUtil.doToast(context, "正在下载...");
            downloadlongImage(message.getMsgId());
            return;
        }

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
//                        Log.d("CMCC", "打印");
                        PreViewUtil.preview(context, localFilePath);
                        break;
                    case 1:
                        //转发
//                        Log.d("CMCC", "转发");
//                        Log.d("CMCC", "localFilePath:" + localFilePath);
                        //跳转到选择联系人界面只能单选
                        Intent intent = new Intent(context, CreateGroupChatActivity.class);
                        intent.putExtra("imgUrl", localFilePath);
                        context.startActivity(intent);
                        break;
                    case 2:
                        EMImageMessageBody body = (EMImageMessageBody) message.getBody();
                        //保存
                        Log.d("CMCC", "保存");
                        // 其次把文件插入到系统图库
                        try {
                            String url = MediaStore.Images.Media.insertImage(context.getContentResolver(),
                                    localFilePath, body.getFileName(), null);
                            Log.d("CMCC", "url:" + url);
                            // 最后通知图库更新
                            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(url)));
                            ToastUtils.showToast(context, "保存成功了...");
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                        break;
                    case 3:
                        //删除记录
                        Log.d("CMCC", "删除");
                        if (message.direct() == EMMessage.Direct.SEND) {
                            //只能删除本人发出的消息
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
                            } else {
                                String toChatUserName = message.getFrom();
                                Log.d("CMCC", "toChatUserName------>" + toChatUserName);
                                cmdMessage.setFrom(toChatUserName);
                                //删除掉本地消息
                                EMClient.getInstance().chatManager()
                                        .getConversation(toChatUserName).removeMessage(message.getMsgId());
                            }

                            String msgId = message.getMsgId();
                            Log.d("CMCC", "msgIdsend-------->" + msgId);
                            cmdMessage.setAttribute("msgid", msgId);
                            cmdMessage.addBody(cmdBody);
                            EMClient.getInstance().chatManager().sendMessage(cmdMessage);
                            Log.d("CMCC", "发送透传success");
                        } else {
                            //删除本地
                            if (message.getChatType() == ChatType.GroupChat ||
                                    message.getChatType() == ChatType.ChatRoom) {
                                String toChatUserName = message.getTo();
                                //删除掉本地消息
                                EMClient.getInstance().chatManager()
                                        .getConversation(toChatUserName).removeMessage(message.getMsgId());
                            } else {
                                String toChatUserName = message.getFrom();
                                //删除掉本地消息
                                EMClient.getInstance().chatManager()
                                        .getConversation(toChatUserName).removeMessage(message.getMsgId());
                            }
                        }
                        //发消息刷新
                        RefreshEvent event = new RefreshEvent();
                        event.setCode(0x13);
                        EventBus.getDefault().post(event);
                        break;
                }
            }
        });
    }

    /**
     * download image
     */
    @SuppressLint("NewApi")
    private void downloadImage(final String msgId) {
        Log.d("CMCC", "download with messageId: " + msgId);
        File temp = new File(localFilePath);
        if (temp != null && temp.exists()) {
            //存在就不要下载
            Log.d("CMCC", "存在就不要下载");
            return;
        }
        final String tempPath = temp.getParent() + "/temp_" + temp.getName();
        final EMCallBack callback = new EMCallBack() {
            public void onSuccess() {
                Log.d("CMCC", "onSuccess");
                //预览
                PreViewUtil.preview(context, localFilePath);
            }

            public void onError(int error, String msg) {
                Log.d("CMCC", "offline file transfer error:" + msg);
                File file = new File(tempPath);
                if (file.exists() && file.isFile()) {
                    file.delete();
                }
            }

            public void onProgress(final int progress, String status) {
                Log.d("CMCC", "Progress: " + progress);
            }
        };

        EMMessage msg = EMClient.getInstance().chatManager().getMessage(msgId);
        msg.setMessageStatusCallback(callback);

        Log.d("CMCC", "downloadAttachement");
        EMClient.getInstance().chatManager().downloadAttachment(msg);
    }

    /**
     * download image
     */
    @SuppressLint("NewApi")
    private void downloadlongImage(final String msgId) {
        Log.d("CMCC", "download with messageId: " + msgId);
        File temp = new File(localFilePath);
        if (temp != null && temp.exists()) {
            //存在就不要下载
            Log.d("CMCC", "存在就不要下载");
            return;
        }
        final String tempPath = temp.getParent() + "/temp_" + temp.getName();
        final EMCallBack callback = new EMCallBack() {
            public void onSuccess() {
                Log.d("CMCC", "onSuccess");
                //预览
                ToastUtils.showToast(context, "下载完成!");
            }

            public void onError(int error, String msg) {
                Log.d("CMCC", "offline file transfer error:" + msg);
                File file = new File(tempPath);
                if (file.exists() && file.isFile()) {
                    file.delete();
                }
            }

            public void onProgress(final int progress, String status) {
                Log.d("CMCC", "Progress: " + progress);
            }
        };

        EMMessage msg = EMClient.getInstance().chatManager().getMessage(msgId);
        msg.setMessageStatusCallback(callback);

        Log.d("CMCC", "downloadAttachement");
        EMClient.getInstance().chatManager().downloadAttachment(msg);
    }
}
