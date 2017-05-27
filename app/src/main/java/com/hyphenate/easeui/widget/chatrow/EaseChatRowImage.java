package com.hyphenate.easeui.widget.chatrow;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMFileMessageBody;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessage.ChatType;
import com.hyphenate.easeui.model.EaseImageCache;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.utils.EaseImageUtils;
import com.hyphenate.util.EMLog;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.constant.ConFig;
import huanxing_print.com.cn.printhome.model.chat.RefreshEvent;
import huanxing_print.com.cn.printhome.ui.activity.chat.CreateGroupChatActivity;
import huanxing_print.com.cn.printhome.util.CircleTransform;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.util.PreViewUtil;
import huanxing_print.com.cn.printhome.util.SharedPreferencesUtils;
import huanxing_print.com.cn.printhome.util.ToastUtils;
import huanxing_print.com.cn.printhome.util.webserver.ChatFileType;
import huanxing_print.com.cn.printhome.view.popupwindow.PopupList;

public class EaseChatRowImage extends EaseChatRowFile {

    protected ImageView imageView;
    private EMImageMessageBody imgBody;
    private ImageView iv_userhead;
    private TextView tv_userid;
    private RelativeLayout bubble;
    private LinearLayout lin_group;
    private ArrayList<String> popupMenuItemList;
    private PopupList popupList;
    private String localFilePath;
    private ExecutorService service = Executors.newSingleThreadExecutor();
    private float mRawX;
    private float mRawY;
    private ProgressDialog pd;
    private String tempPath;
    private String iosFilePath;
    private String fileReName;//修改之后的文件名称
    private String fileName;//文件名字

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

        bubble.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mRawX = event.getRawX();
                mRawY = event.getRawY();
                return false;
            }
        });

        popupMenuItemList = new ArrayList<>();
        popupMenuItemList.add("打印");
        popupMenuItemList.add("转发");
        popupMenuItemList.add("保存");
        popupMenuItemList.add("删除");

        popupList = new PopupList(context);

        EMImageMessageBody body = (EMImageMessageBody) message.getBody();
        localFilePath = body.getLocalUrl();
        fileName = body.getFileName();
        File temp = new File(localFilePath);
        iosFilePath = temp.getParent() + "/temp_" + temp.getName() + ".jpg";
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
        Log.d("CMCC", "图片地址:" + localFilePath);
        downloadImage(message.getMsgId());

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

        popupList.showPopupListWindow(bubble, position, mRawX, mRawY, popupMenuItemList, new PopupList.PopupListListener() {
            @Override
            public boolean showPopupList(View adapterView, View contextView, int contextPosition) {
                return true;
            }

            @Override
            public void onPopupListClick(View contextView, int contextPosition, int position) {
                switch (position) {
                    case 0:
                        //打印
                        downloadlongImage(message.getMsgId(), 0);
                        break;
                    case 1:
                        //转发
                        downloadlongImage(message.getMsgId(), 1);
                        break;
                    case 2:
                        //保存
                        downloadlongImage(message.getMsgId(), 2);
                        break;
                    case 3:
                        //删除本地
                        if (message.getChatType() == ChatType.GroupChat ||
                                message.getChatType() == ChatType.ChatRoom) {
                            String toChatUserName = message.getTo();
                            //删除掉本地消息
                            EMClient.getInstance().chatManager()
                                    .getConversation(toChatUserName).removeMessage(message.getMsgId());
                        } else {
                            if (message.direct() == EMMessage.Direct.SEND) {
                                //自己发的
                                String toChatUserName = message.getTo();
                                //删除掉本地消息
                                EMClient.getInstance().chatManager()
                                        .getConversation(toChatUserName).removeMessage(message.getMsgId());
                            } else if (message.direct() == EMMessage.Direct.RECEIVE) {
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
        final File temp = new File(localFilePath);
        if (temp != null && temp.exists()) {
            //存在就不要下载
            if (ChatFileType.isImage(context, Uri.fromFile(temp))) {
                PreViewUtil.preview(context, localFilePath, true);
            } else {
                PreViewUtil.preview(context, iosFilePath, true);
            }
            return;
        }

        String str1 = getResources().getString(R.string.Download_the_pictures);
        pd = new ProgressDialog(context);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage(str1);
        pd.show();

        tempPath = temp.getParent() + "/temp_" + temp.getName();
        Log.d("CMCC", "tempPath:" + tempPath);
        Log.d("CMCC", "imgName:" + temp.getName());
        Log.d("CMCC", "localFilePath:" + localFilePath);
        final EMCallBack callback = new EMCallBack() {
            public void onSuccess() {
                Log.d("CMCC", "onSuccess");
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        new File(tempPath).renameTo(new File(localFilePath));

                        if (ChatFileType.isImage(context, Uri.fromFile(temp))) {
                            //不理会
                            if (message.getChatType() == ChatType.GroupChat ||
                                    message.getChatType() == ChatType.ChatRoom) {
                                //文件路径 个人环信号/群环信号/
                                String easemobIdPersonal = SharedPreferencesUtils
                                        .getShareString(context, "easemobId");
                                fileReName = ConFig.FILE_SAVE + File.separator + easemobIdPersonal +
                                        File.separator + message.getTo() + File.separator + fileName;
                                Log.d("CMCC", "fileReName:" + fileReName);
                                service.submit(new Runnable() {
                                    @Override
                                    public void run() {
                                        copyFile(localFilePath, fileReName, false);
                                    }
                                });
                            }
                        } else {
                            iosFilePath = temp.getParent() + "/temp_" + temp.getName() + ".jpg";
                            //复制文件
                            service.submit(new Runnable() {
                                @Override
                                public void run() {
                                    copyFile(localFilePath, iosFilePath, true);
                                    if (message.getChatType() == ChatType.GroupChat ||
                                            message.getChatType() == ChatType.ChatRoom) {
                                        //文件路径 个人环信号/群环信号/
                                        String easemobIdPersonal = SharedPreferencesUtils
                                                .getShareString(context, "easemobId");
                                        fileReName = ConFig.FILE_SAVE + File.separator + easemobIdPersonal +
                                                File.separator + message.getTo() + File.separator + temp.getName() + ".jpg";
                                        Log.d("CMCC", "fileReName:" + fileReName);
                                        copyFile(iosFilePath, fileReName, false);
                                    }
                                }
                            });
                        }
                        if (pd != null) {
                            pd.dismiss();
                        }
                    }
                });
                //预览
                if (ChatFileType.isImage(context, Uri.fromFile(temp))) {
                    Log.d("CMCC", "iosFilePath:" + iosFilePath);
                    PreViewUtil.preview(context, localFilePath, true);
                } else {
                    PreViewUtil.preview(context, iosFilePath, true);
                }


            }

            public void onError(int error, String msg) {
                Log.d("CMCC", "offline file transfer error:" + msg);
                File file = new File(tempPath);
                if (file.exists() && file.isFile()) {
                    file.delete();
                }
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        pd.dismiss();
                    }
                });
            }

            public void onProgress(final int progress, String status) {
                Log.d("CMCC", "Progress: " + progress);
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        final String str2 = getResources().getString(R.string.Download_the_pictures_new);
                        pd.setMessage(str2 + progress + "%");
                    }
                });

            }
        };

        EMMessage msg = EMClient.getInstance().chatManager().getMessage(msgId);
        msg.setMessageStatusCallback(callback);

        EMLog.e(TAG, "downloadAttachement");
        EMClient.getInstance().chatManager().downloadAttachment(msg);
    }

    /**
     * download image
     */
    @SuppressLint("NewApi")
    private void downloadlongImage(final String msgId, final int code) {
        Log.d("CMCC", "download with messageId: " + msgId);
        final File temp = new File(localFilePath);
        if (temp != null && temp.exists()) {
            //存在就不要下载
            switch (code) {
                case 0:
                    if (ChatFileType.isImage(context, Uri.fromFile(temp))) {
                        PreViewUtil.preview(context, localFilePath, false);
                    } else {
                        PreViewUtil.preview(context, iosFilePath, false);
                    }
                    break;
                case 1:
                    //跳转到选择联系人界面只能单选
                    Intent intent = new Intent(context, CreateGroupChatActivity.class);
                    if (ChatFileType.isImage(context, Uri.fromFile(temp))) {
                        intent.putExtra("imgUrl", localFilePath);
                    } else {
                        intent.putExtra("imgUrl", iosFilePath);
                    }
                    context.startActivity(intent);
                    break;
                case 2:
                    service.submit(new Runnable() {
                        @Override
                        public void run() {
                            EMImageMessageBody body = (EMImageMessageBody) message.getBody();
                            // 其次把文件插入到系统图库
                            try {
                                if (ChatFileType.isImage(context, Uri.fromFile(temp))) {
                                    String url = MediaStore.Images.Media.insertImage(context.getContentResolver(),
                                            localFilePath, body.getFileName(), null);
                                    // 最后通知图库更新
                                    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(url)));
                                } else {
                                    String url = MediaStore.Images.Media.insertImage(context.getContentResolver(),
                                            iosFilePath, body.getFileName(), null);
                                    // 最后通知图库更新
                                    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(url)));
                                }
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    ToastUtils.showToast(context, "保存成功");
                    break;
            }
            return;
        }

        String str1 = getResources().getString(R.string.Download_the_pictures);
        pd = new ProgressDialog(context);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage(str1);
        pd.show();

        tempPath = temp.getParent() + "/temp_" + temp.getName();
        final EMCallBack callback = new EMCallBack() {
            public void onSuccess() {
                Log.d("CMCC", "onSuccess");
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        new File(tempPath).renameTo(new File(localFilePath));

                        if (ChatFileType.isImage(context, Uri.fromFile(temp))) {
                            //不理会
                            if (message.getChatType() == ChatType.GroupChat ||
                                    message.getChatType() == ChatType.ChatRoom) {
                                //文件路径 个人环信号/群环信号/
                                String easemobIdPersonal = SharedPreferencesUtils
                                        .getShareString(context, "easemobId");
                                fileReName = ConFig.FILE_SAVE + File.separator + easemobIdPersonal +
                                        File.separator + message.getTo() + File.separator + fileName;
                                service.submit(new Runnable() {
                                    @Override
                                    public void run() {
                                        copyFile(localFilePath, fileReName, false);
                                    }
                                });
                            }
                        } else {
                            iosFilePath = temp.getParent() + "/temp_" + temp.getName() + ".jpg";
                            //复制文件
                            service.submit(new Runnable() {
                                @Override
                                public void run() {
                                    copyFile(localFilePath, iosFilePath, true);
                                    if (message.getChatType() == ChatType.GroupChat ||
                                            message.getChatType() == ChatType.ChatRoom) {
                                        //文件路径 个人环信号/群环信号/
                                        String easemobIdPersonal = SharedPreferencesUtils
                                                .getShareString(context, "easemobId");
                                        fileReName = ConFig.FILE_SAVE + File.separator + easemobIdPersonal +
                                                File.separator + message.getTo() + File.separator + temp.getName() + ".jpg";
                                        Log.d("CMCC", "fileReName:" + fileReName);
                                        copyFile(iosFilePath, fileReName, false);
                                    }
                                }
                            });
                        }
                        if (pd != null) {
                            pd.dismiss();
                        }
                    }
                });
                //预览
                switch (code) {
                    case 0:
                        if (ChatFileType.isImage(context, Uri.fromFile(temp))) {
                            PreViewUtil.preview(context, localFilePath, false);
                        } else {
                            PreViewUtil.preview(context, iosFilePath, false);
                        }
                        break;
                    case 1:
                        //跳转到选择联系人界面只能单选
                        Intent intent = new Intent(context, CreateGroupChatActivity.class);
                        if (ChatFileType.isImage(context, Uri.fromFile(temp))) {
                            intent.putExtra("imgUrl", localFilePath);
                        } else {
                            intent.putExtra("imgUrl", iosFilePath);
                        }
                        context.startActivity(intent);
                        break;
                    case 2:
                        service.submit(new Runnable() {
                            @Override
                            public void run() {
                                EMImageMessageBody body = (EMImageMessageBody) message.getBody();
                                // 其次把文件插入到系统图库
                                try {
                                    if (ChatFileType.isImage(context, Uri.fromFile(temp))) {
                                        String url = MediaStore.Images.Media.insertImage(context.getContentResolver(),
                                                localFilePath, body.getFileName(), null);
                                        // 最后通知图库更新
                                        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(url)));
                                    } else {
                                        String url = MediaStore.Images.Media.insertImage(context.getContentResolver(),
                                                iosFilePath, body.getFileName(), null);
                                        // 最后通知图库更新
                                        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(url)));
                                    }
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        ToastUtils.showToast(context, "保存成功");
                        break;
                }
            }

            public void onError(int error, String msg) {
                Log.d("CMCC", "offline file transfer error:" + msg);
                File file = new File(tempPath);
                if (file.exists() && file.isFile()) {
                    file.delete();
                }
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        pd.dismiss();
                    }
                });
            }

            public void onProgress(final int progress, String status) {
                Log.d("CMCC", "Progress: " + progress);
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        final String str2 = getResources().getString(R.string.Download_the_pictures_new);
                        pd.setMessage(str2 + progress + "%");
                    }
                });
            }
        };

        EMMessage msg = EMClient.getInstance().chatManager().getMessage(msgId);
        msg.setMessageStatusCallback(callback);

        Log.d("CMCC", "downloadAttachement");
        EMClient.getInstance().chatManager().downloadAttachment(msg);
    }


    /*
    *缓冲输入输出流方式复制文件
    */
    public boolean copyFile(String srcFileName, String destFileName, boolean overlay) {
        File srcFile = new File(srcFileName); //根据一个路径得到File对象
        //判断源文件是否存在
        if (!srcFile.exists()) {
            try {
                throw new Exception("源文件：" + srcFileName + "不存在！");
            } catch (Exception e) {
                e.printStackTrace();//将异常内容存到日志文件中
            }
            return false;
        } else if (!srcFile.isFile()) {//判断是不是一个文件
            try {
                throw new Exception("复制文件失败，源文件：" + srcFileName + "不是一个文件！");
            } catch (Exception e) {

                e.printStackTrace();
            }
            return false;

        }
        //判断目标文件是否存在
        File destFile = new File(destFileName);//目标文件对象destFile
        if (destFile.exists()) {
            //如果目标文件存在并允许覆盖
            if (overlay) {
                //删除已经存在的目标文件
                new File(destFileName).delete();

            }
        } else {
            //如果目标文件所在目录不存在，则创建 目录
            if (!destFile.getParentFile().exists()) {
                //目标文件所在目录不存在
                //mkdirs()：创建此抽象路径名指定的目录，包括所有必需但不存在的父目录
                if (!destFile.getParentFile().mkdirs()) {
                    //复制文件失败：创建目标文件所在目录失败
                    return false;
                }
            }
        }

        //复制文件
        int byteread = 0;//读取的字节数
        InputStream in = null;
        OutputStream out = null;

        try {
            in = new FileInputStream(srcFile);
            out = new FileOutputStream(destFile);
            byte[] buffer = new byte[1024];
                /*
                 * 方法说明：
                 * ①：将指定 byte 数组中从偏移量 off 开始的 len 个字节写入此输出流。
                 *      write(byte[] b, int off, int len)
                 *          b - 数据
                 *          off - 数据中的起始偏移量。
                 *          len - 要写入的字节数。
                 * ②：in.read(buffer))!=-1,是从流buffer中读取一个字节，当流结束的时候read返回-1
                 */

            while ((byteread = in.read(buffer)) != -1) {
                out.write(buffer, 0, byteread);
            }
            return true;
        } catch (FileNotFoundException e) {

            return false;
        } catch (IOException e) {
            return false;
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }
}
