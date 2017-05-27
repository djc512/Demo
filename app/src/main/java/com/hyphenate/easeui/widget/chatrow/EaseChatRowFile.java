package com.hyphenate.easeui.widget.chatrow;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMFileMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessage.ChatType;
import com.hyphenate.chat.EMNormalFileMessageBody;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.TextFormater;

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
import huanxing_print.com.cn.printhome.util.FileType;
import huanxing_print.com.cn.printhome.util.FileUtils;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.util.PreViewUtil;
import huanxing_print.com.cn.printhome.util.SharedPreferencesUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;
import huanxing_print.com.cn.printhome.util.ToastUtils;
import huanxing_print.com.cn.printhome.view.popupwindow.PopupList;

import static huanxing_print.com.cn.printhome.util.webserver.ChatFileType.getExtensionName;

public class EaseChatRowFile extends EaseChatRow {

    protected TextView fileNameView;
    protected TextView fileSizeView;
    protected TextView fileStateView;
    private ImageView iv_userhead;
    private ImageView iv_file_type;
    private TextView tv_userid;
    private ArrayList<String> popupMenuItemList;
    private String localFilePath;
    protected EMCallBack sendfileCallBack;
    private LinearLayout bubble;
    protected boolean isNotifyProcessed;
    private EMNormalFileMessageBody fileMessageBody;
    private File file;
    private PopupList popupList;
    private float mRawX;
    private float mRawY;
    private String fileReName;//修改之后的文件名称
    private String fileName;//文件名字
    private ExecutorService service = Executors.newSingleThreadExecutor();
    private ProgressDialog pd;

    public EaseChatRowFile(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflateView() {
        inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ?
                R.layout.ease_row_received_file : R.layout.ease_row_sent_file, this);
    }

    @Override
    protected void onFindViewById() {
        fileNameView = (TextView) findViewById(R.id.tv_file_name);
        fileSizeView = (TextView) findViewById(R.id.tv_file_size);
        fileStateView = (TextView) findViewById(R.id.tv_file_state);
        percentageView = (TextView) findViewById(R.id.percentage);
        iv_userhead = (ImageView) findViewById(R.id.iv_userhead);
        iv_file_type = (ImageView) findViewById(R.id.iv_file_type);
        tv_userid = (TextView) findViewById(R.id.tv_userid);
        bubble = (LinearLayout) findViewById(R.id.bubble);
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

        fileMessageBody = (EMNormalFileMessageBody) message.getBody();
        localFilePath = fileMessageBody.getLocalUrl();
        file = new File(localFilePath);
        fileName = fileMessageBody.getFileName();
        fileNameView.setText(fileMessageBody.getFileName());

        if (message.getType() == EMMessage.Type.FILE) {
            //设置不同文件类型的图标
            String fileType = getExtensionName(localFilePath);
            if (!ObjectUtils.isNull(fileType)) {
                switch (fileType) {
                    case "pdf":
                        iv_file_type.setImageResource(R.drawable.file_pdf);
                        break;
                    case "doc":
                        iv_file_type.setImageResource(R.drawable.file_doc);
                        break;
                    case "docx":
                        iv_file_type.setImageResource(R.drawable.file_docx);
                        break;
                    case "ppt":
                        iv_file_type.setImageResource(R.drawable.file_ppt);
                        break;
                    case "pptx":
                        iv_file_type.setImageResource(R.drawable.file_pptx);
                        break;
                    default:
                        iv_file_type.setImageResource(R.drawable.iv_apply_copy);
                        break;
                }
            }
        }
        fileSizeView.setText(TextFormater.getDataSize(fileMessageBody.getFileSize()));
        if (message.direct() == EMMessage.Direct.RECEIVE) {
            File file = new File(localFilePath);
            if (file.exists()) {
                fileStateView.setText(R.string.Have_downloaded);
            } else {
                fileStateView.setText(R.string.Did_not_download);
            }
            return;
        }
        String iconUrl = message.getStringAttribute("iconUrl", "");
        String nickName = message.getStringAttribute("nickName", "");
//        Log.i("CCCC", "iconUrl=============================================" + iconUrl);
//        Log.i("PPPP", "iconUrl=============================================" + iconUrl);
        //头像
        if (ObjectUtils.isNull(iconUrl)) {
//            Log.i("PPPP", "=============================================我走了为空的");
//            Log.i("CCCC", "=============================================我走了为空的");
            Glide.with(getContext())
                    .load(R.drawable.iv_head)
                    .transform(new CircleTransform(getContext()))
                    .into(iv_userhead);
        } else {
//            Log.i("PPPP", "=============================================我走了不为空，网络的");
//            Log.i("CCCC", "=============================================我走了不为空，网络的");
            Glide.with(getContext())
                    .load(iconUrl)
                    .transform(new CircleTransform(getContext()))
                    .into(iv_userhead);
        }
        //昵称
        if (ObjectUtils.isNull(nickName)) {
            tv_userid.setText(message.getFrom());
//            Log.i("DDDD", "=============================================我走了空");
        } else {
            tv_userid.setText(nickName);
//            Log.i("DDDD", "=============================================我走了不为空，网络的");
        }
//        Log.i("DDDD", "message.getFrom()=============================================" + message.getFrom());
//        Log.i("DDDD", "nickName=============================================" + nickName);
        //隐藏发送文件的用户名
        if (message.getChatType().equals(ChatType.Chat)) {
            tv_userid.setVisibility(GONE);
        } else {
            tv_userid.setVisibility(VISIBLE);
        }

        // until here, to sending message
        handleSendMessage();
    }

    /**
     * handle sending message
     */
    protected void handleSendMessage() {
        setMessageSendCallback();
        switch (message.status()) {
            case SUCCESS:
                progressBar.setVisibility(View.INVISIBLE);
                if (percentageView != null)
                    percentageView.setVisibility(View.INVISIBLE);
                statusView.setVisibility(View.INVISIBLE);
                break;
            case FAIL:
                progressBar.setVisibility(View.INVISIBLE);
                if (percentageView != null)
                    percentageView.setVisibility(View.INVISIBLE);
                statusView.setVisibility(View.VISIBLE);
                break;
            case INPROGRESS:
                progressBar.setVisibility(View.VISIBLE);
                if (percentageView != null) {
                    percentageView.setVisibility(View.VISIBLE);
                    percentageView.setText(message.progress() + "%");
                }
                statusView.setVisibility(View.INVISIBLE);
                break;
            default:
                progressBar.setVisibility(View.INVISIBLE);
                if (percentageView != null)
                    percentageView.setVisibility(View.INVISIBLE);
                statusView.setVisibility(View.VISIBLE);
                break;
        }
    }


    @Override
    protected void onUpdateView() {
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onBubbleClick() {
        String filePath = fileMessageBody.getLocalUrl();
        File file = new File(filePath);
        if (file.exists()) {
            //文件预览页
            // open files if it exist
//            FileUtils.openFile(file, (Activity) context);
//            Intent intent = FileUtil.openFile(file.getAbsolutePath());
//            context.startActivity(intent);
            if (!ObjectUtils.isNull(filePath)) {
                if (FileType.isPrintType(filePath)) {
                    PreViewUtil.preview(context, filePath, true);
                } else {
                    ToastUtil.doToast(context, "不支持此类文件的预览!");
                }
            }
        } else {
            // download the file
            //context.startActivity(new Intent(context, EaseShowNormalFileActivity.class).putExtra("msg", message));
            downLoadFile(message);
        }
        if (message.direct() == EMMessage.Direct.RECEIVE && !message.isAcked() && message.getChatType() == ChatType.Chat) {
            try {
                EMClient.getInstance().chatManager().ackMessageRead(message.getFrom(), message.getMsgId());
            } catch (HyphenateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void onBubbleLongClick() {
//        Log.d("CMCC", "onBubbleLongClick触发了");
        file = new File(localFilePath);

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
                        if (!ObjectUtils.isNull(file) && file.exists()) {
                            //文件预览页
                            PreViewUtil.preview(context, localFilePath, false);
                        } else {
                            // download the file
                            //ToastUtils.showToast(context, "下载中...");
                            //context.startActivity(new Intent(context, EaseShowNormalFileActivity.class).putExtra("msg", message));
                            downLoadFile(message);
                        }
                        if (message.direct() == EMMessage.Direct.RECEIVE && !message.isAcked() && message.getChatType() == ChatType.Chat) {
                            try {
                                EMClient.getInstance().chatManager().ackMessageRead(message.getFrom(), message.getMsgId());
                            } catch (HyphenateException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                        break;
                    case 1:
                        //转发
                        EMFileMessageBody body = (EMFileMessageBody) message.getBody();
                        if (!ObjectUtils.isNull(file) && file.exists()) {
                            //跳转到选择联系人界面只能单选
                            Intent intent = new Intent(context, CreateGroupChatActivity.class);
                            intent.putExtra("fileUrl", localFilePath);
                            intent.putExtra("fileName", body.getFileName());
                            context.startActivity(intent);
                        } else {
                            //context.startActivity(new Intent(context, EaseShowNormalFileActivity.class).putExtra("msg", message));
                            downLoadFile(message);
                            if (message.direct() == EMMessage.Direct.RECEIVE && !message.isAcked() && message.getChatType() == ChatType.Chat) {
                                try {
                                    EMClient.getInstance().chatManager().ackMessageRead(message.getFrom(), message.getMsgId());
                                } catch (HyphenateException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;
                    case 2:
                        //保存
                        if (file.exists()) {
                            //文件预览页
                            ToastUtils.showToast(context, "保存成功!");
                        } else {
                            // download the file
                            //context.startActivity(new Intent(context, EaseShowNormalFileActivity.class).putExtra("msg", message));
                            downLoadFile(message);
                        }
                        if (message.direct() == EMMessage.Direct.RECEIVE && !message.isAcked() && message.getChatType() == ChatType.Chat) {
                            try {
                                EMClient.getInstance().chatManager().ackMessageRead(message.getFrom(), message.getMsgId());
                            } catch (HyphenateException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                        break;
                    case 3:
                        //删除本地消息
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
            //生成新的文件名称
            String lastPath = FileUtils.getFileName(destFileName);
            destFile = new File(lastPath);
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


    /**
     * description: 下载文件
     * author LSW
     * date 2017/5/26 0:27
     * update 2017/5/26
     */
    public void downLoadFile(final EMMessage emMessage) {

        String str1 = "下载文件:0%";
        pd = new ProgressDialog(context);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage(str1);
        pd.show();

        if (!(message.getBody() instanceof EMFileMessageBody)) {
            ToastUtil.doToast(context, "不支持的消息类型");
            return;
        }
        final File file = new File(((EMFileMessageBody) message.getBody())
                .getLocalUrl());

        message.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {
                //TODO 已经下载了文件
                //自己拼凑新的文件名称

                if (emMessage.getChatType() == ChatType.GroupChat ||
                        emMessage.getChatType() == ChatType.ChatRoom) {
                    if (!ObjectUtils.isNull(file) && file.exists()) {
                        //文件路径 个人环信号/群环信号/
                        String easemobIdPersonal = SharedPreferencesUtils
                                .getShareString(context, "easemobId");
                        fileReName = ConFig.FILE_SAVE + File.separator + easemobIdPersonal +
                                File.separator + emMessage.getTo() + File.separator + fileName;
                        Log.d("CMCC", "fileReName:" + fileReName);
                        //复制文件
                        service.submit(new Runnable() {
                            @Override
                            public void run() {
                                copyFile(localFilePath, fileReName, false);
                            }
                        });
                    }
                }

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (pd != null) {
                            pd.dismiss();
                        }
                    }
                });
            }

            @Override
            public void onError(int code, String error) {
                if (file != null && file.exists() && file.isFile()) {
                    file.delete();
                }
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        pd.dismiss();
                        ToastUtil.doToast(context, "下载失败,请重试!");
                    }
                });
            }

            @Override
            public void onProgress(final int progress, String status) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        final String str2 = "下载文件:";
                        pd.setMessage(str2 + progress + "%");
                    }
                });
            }
        });
        EMClient.getInstance().chatManager().downloadAttachment(message);
    }
}
