package huanxing_print.com.cn.printhome.ui.activity.chat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.EMMessageListener;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMChatRoom;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.domain.EaseEmojicon;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.model.EaseAtMessageHelper;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.ui.EaseChatRoomListener;
import com.hyphenate.easeui.ui.EaseGroupListener;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.hyphenate.easeui.widget.EaseAlertDialog;
import com.hyphenate.easeui.widget.EaseChatExtendMenu;
import com.hyphenate.easeui.widget.EaseChatInputMenu;
import com.hyphenate.easeui.widget.EaseChatMessageList;
import com.hyphenate.easeui.widget.EaseVoiceRecorderView;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.hyphenate.util.EMLog;
import com.hyphenate.util.PathUtil;

import java.io.File;
import java.util.List;

import huanxing_print.com.cn.printhome.BuildConfig;
import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.model.chat.GroupHint;
import huanxing_print.com.cn.printhome.model.chat.MessageTypeObject;
import huanxing_print.com.cn.printhome.model.chat.RedPacketHint;
import huanxing_print.com.cn.printhome.model.contact.FriendInfo;
import huanxing_print.com.cn.printhome.model.contact.FriendSearchInfo;
import huanxing_print.com.cn.printhome.model.contact.GroupInfo;
import huanxing_print.com.cn.printhome.model.contact.GroupMessageInfo;
import huanxing_print.com.cn.printhome.model.contact.NewFriendInfo;
import huanxing_print.com.cn.printhome.net.callback.contact.GroupMessageCallback;
import huanxing_print.com.cn.printhome.net.request.contact.GroupManagerRequest;
import huanxing_print.com.cn.printhome.ui.activity.contact.GroupSettingActivity;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.Constant;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;
import huanxing_print.com.cn.printhome.util.copy.PicSaveUtil;

/**
 * 聊天测试activity
 */
public class ChatTestActivity extends BaseActivity implements EMMessageListener {
    protected static final String TAG = "EaseChatFragment";
    protected static final int REQUEST_CODE_MAP = 1;
    protected static final int REQUEST_CODE_CAMERA = 2;
    protected static final int REQUEST_CODE_LOCAL = 3;
    private static final int REQUEST_CODE_SELECT_FILE = 12;

    private static final int ITEM_TAKE_PICTURE = 4;
    private static final int ITEM_PICTURE = 5;
    private static final int ITEM_FILE = 6;
    private static final int ITEM_RED_PACKET = 7;
    private static final int ITEM_VIDEO = 8;
    private static final int ITEM_LOCATION = 9;
    private static final int ITEM_TAKE_PICTURE_CALL = 10;
    private static final int ITEM_PICTURE_CALL = 11;
    private static final int ITEM_FILE_CALL = 12;
    private static final int ITEM_RED_PACKET_CALL = 13;


    protected int chatType;
    protected String toChatUsername;
    protected EaseChatMessageList messageList;
    protected EaseChatInputMenu inputMenu;

    protected EMConversation conversation;

    protected InputMethodManager inputManager;
    protected ClipboardManager clipboard;

    protected Handler handler = new Handler();
    protected File cameraFile;
    protected EaseVoiceRecorderView voiceRecorderView;
    protected SwipeRefreshLayout swipeRefreshLayout;
    protected ListView listView;

    protected boolean isloading;
    protected boolean haveMoreData = true;
    protected int pagesize = 20;
    protected GroupListener groupListener;
    protected ChatRoomListener chatRoomListener;

    protected EMMessage contextMenuMessage;
    private GroupMessageInfo groupInfo;
    private boolean isMessageListInited;
    protected MyItemClickListener extendMenuItemClickListener;
    //protected EaseTitleBar titleBar;
    private PicSaveUtil saveUtil;
    private String nickName;
    private TextView tv_title;
    private ImageView addImg;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 改变状态栏的颜色使其与APP风格一体化
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.activity_chat_test);

        EMClient.getInstance().chatManager().addMessageListener(msgListener);
        //聊天类型
        chatType = getIntent().getIntExtra(Constant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
        toChatUsername = getIntent().getStringExtra(Constant.EXTRA_USER_ID);
        nickName = getIntent().getStringExtra("name");
        Log.i("CMCC", "chatType:" + chatType + ",toChatUsername:" + toChatUsername);
        saveUtil = new PicSaveUtil(getSelfActivity());
        cameraFile = saveUtil.createCameraTempFile(savedInstanceState);
        // userId you are chat with or group id
        initView();

        //群则去请求群信息
        if (chatType == EaseConstant.CHATTYPE_GROUP) {
            GroupManagerRequest.queryGroupMessage(getSelfActivity(), baseApplication.getLoginToken(),
                    "", toChatUsername, callback);
        } else {
            tv_title.setText(toChatUsername);
        }

        setUpView();
        Log.i("CCCC", "nickName=============================================" + nickName);
        Log.i("CCCC", "toChatUsername=============================================" + toChatUsername);
        if (toChatUsername.equals("secretary")) {
            tv_title.setText("印家小秘书");

        }
    }

    GroupMessageCallback callback = new GroupMessageCallback() {
        @Override
        public void success(String msg, GroupMessageInfo groupMessageInfo) {
            if (!ObjectUtils.isNull(groupMessageInfo)) {
                groupInfo = groupMessageInfo;
                nickName = groupInfo.getGroupName();
                tv_title.setText(groupInfo.getGroupName() + "(" + groupInfo.getGroupMembers().size() + ")");
            }
        }

        @Override
        public void fail(String msg) {

        }

        @Override
        public void connectFail() {

        }
    };

    /**
     * init view
     */
    protected void initView() {
        // hold to record voice
        //noinspection ConstantConditions
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(toChatUsername);


        addImg = (ImageView) findViewById(R.id.addImg);
        voiceRecorderView = (EaseVoiceRecorderView) findViewById(R.id.voice_recorder);
        View view = findViewById(R.id.back);
        view.findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishCurrentActivity();
            }
        });

        GroupInfo groupInfo = getIntent().getParcelableExtra("GroupInfo");
        FriendInfo friendInfo = getIntent().getParcelableExtra("FriendInfo");
        FriendSearchInfo friendSearchInfo = getIntent().getParcelableExtra("FriendSearchInfo");
        NewFriendInfo newFriendInfo = getIntent().getParcelableExtra("NewFriendInfo");
        if (!ObjectUtils.isNull(groupInfo)) {
            //群聊
            chatType = EaseConstant.CHATTYPE_GROUP;
            toChatUsername = groupInfo.getEasemobGroupId();
            nickName = groupInfo.getGroupName();
            Log.i("CMCC", "type:" + chatType + ",mUsername:" + toChatUsername +
                    ",nickName:" + nickName);
        }

        if (!ObjectUtils.isNull(friendInfo)) {
            //私聊
            chatType = EaseConstant.CHATTYPE_SINGLE;
            toChatUsername = friendInfo.getEasemobId();
            nickName = friendInfo.getMemberName();
            Log.i("CMCC", "type:" + chatType + ",mUsername:" + toChatUsername +
                    ",nickName:" + nickName);
        }

        if (!ObjectUtils.isNull(friendSearchInfo)) {
            //私聊
            chatType = EaseConstant.CHATTYPE_SINGLE;
            toChatUsername = friendSearchInfo.getMemberId();
            nickName = friendSearchInfo.getMemberName();
            Log.i("CMCC", "type:" + chatType + ",mUsername:" + toChatUsername +
                    ",nickName:" + nickName);
        }

        if (!ObjectUtils.isNull(newFriendInfo)) {
            //私聊
            chatType = EaseConstant.CHATTYPE_SINGLE;
            toChatUsername = newFriendInfo.getMemberId();
            nickName = newFriendInfo.getMemberName();
            Log.i("CMCC", "type:" + chatType + ",mUsername:" + toChatUsername +
                    ",nickName:" + nickName);
        }

        tv_title.setText(nickName);
        //取消声音按钮
        voiceRecorderView.setVisibility(View.GONE);

        // message list layout
        messageList = (EaseChatMessageList) findViewById(R.id.message_list);
        if (chatType != EaseConstant.CHATTYPE_SINGLE)
            messageList.setShowUserNick(true);
        listView = messageList.getListView();

        extendMenuItemClickListener = new MyItemClickListener();
        inputMenu = (EaseChatInputMenu) findViewById(R.id.input_menu);
        registerExtendMenuItem();
        // init input menu
        inputMenu.init(null);
        inputMenu.setChatInputMenuListener(new EaseChatInputMenu.ChatInputMenuListener() {

            @Override
            public void onSendMessage(String content) {
                sendTextMessage(content);
            }

            @Override
            public boolean onPressToSpeakBtnTouch(View v, MotionEvent event) {
                return voiceRecorderView.onPressToSpeakBtnTouch(v, event, new EaseVoiceRecorderView.EaseVoiceRecorderCallback() {

                    @Override
                    public void onVoiceRecordComplete(String voiceFilePath, int voiceTimeLength) {
                        sendVoiceMessage(voiceFilePath, voiceTimeLength);
                    }
                });
            }

            @Override
            public void onBigExpressionClicked(EaseEmojicon emojicon) {
                sendBigExpressionMessage(emojicon.getName(), emojicon.getIdentityCode());
            }
        });

        swipeRefreshLayout = messageList.getSwipeRefreshLayout();
        swipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);

        inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    protected void setUpView() {
        //titleBar.setTitle(toChatUsername);
//        titleBar.setLeftImageResource(R.drawable.ic_back);
        if (chatType == EaseConstant.CHATTYPE_SINGLE) {
            // set title
            addImg.setVisibility(View.GONE);
            if (EaseUserUtils.getUserInfo(toChatUsername) != null) {
                EaseUser user = EaseUserUtils.getUserInfo(toChatUsername);
                if (user != null) {
                    tv_title.setText(user.getNick());
                }
            }
            //titleBar.setRightImageResource(R.drawable.ease_mm_title_remove);
        } else {
            //titleBar.setRightImageResource(R.drawable.group_setting);
            addImg.setVisibility(View.VISIBLE);
            if (chatType == EaseConstant.CHATTYPE_GROUP) {
                //group chat
                EMGroup group = EMClient.getInstance().groupManager().getGroup(toChatUsername);
                if (group != null)
                    tv_title.setText(toChatUsername );
                // listen the event that user moved out group or group is dismissed
                groupListener = new GroupListener();
                EMClient.getInstance().groupManager().addGroupChangeListener(groupListener);
            } else {
                chatRoomListener = new ChatRoomListener();
                EMClient.getInstance().chatroomManager().addChatRoomChangeListener(chatRoomListener);
                onChatRoomViewCreation();
            }

        }
        if (chatType != EaseConstant.CHATTYPE_CHATROOM) {
            onConversationInit();
            onMessageListInit();
        }

//        titleBar.setLeftLayoutClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
//        titleBar.setRightLayoutClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if (chatType == EaseConstant.CHATTYPE_SINGLE) {
//                    //emptyHistory();
//                } else {
//                    toGroupDetails();
//                }
//            }
//        });

        addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toGroupDetails();
            }
        });

        setRefreshLayoutListener();

        // show forward message if the message is not null
        String forward_msg_id = getIntent().getStringExtra("forward_msg_id");
        if (forward_msg_id != null) {
            forwardMessage(forward_msg_id);
        }
    }

    /**
     * register extend menu, item id need > 3 if you override this method and keep exist item
     */
    protected void registerExtendMenuItem() {
        inputMenu.registerExtendMenuItem(R.string.attach_take_pic, R.drawable.camera_chat, ITEM_TAKE_PICTURE, extendMenuItemClickListener);
        inputMenu.registerExtendMenuItem(R.string.attach_picture, R.drawable.photo_chat, ITEM_PICTURE, extendMenuItemClickListener);
        inputMenu.registerExtendMenuItem(R.string.attach_file, R.drawable.file_chat, ITEM_FILE, extendMenuItemClickListener);
        inputMenu.registerExtendMenuItem(R.string.attach_red_packet, R.drawable.hongbao_chat, ITEM_RED_PACKET, extendMenuItemClickListener);
//        if (chatType == Constant.CHATTYPE_SINGLE) {
//            inputMenu.registerExtendMenuItem(R.string.attach_voice_call, R.drawable.em_chat_voice_call_selector, ITEM_VOICE_CALL, extendMenuItemClickListener);
//            inputMenu.registerExtendMenuItem(R.string.attach_video_call, R.drawable.em_chat_video_call_selector, ITEM_VIDEO_CALL, extendMenuItemClickListener);
//        }
        //聊天室暂时不支持红包功能
        //red packet code : 注册红包菜单选项
//        if (chatType != Constant.CHATTYPE_CHATROOM) {
//            inputMenu.registerExtendMenuItem(R.string.attach_red_packet, R.drawable.em_chat_red_packet_selector, ITEM_RED_PACKET, extendMenuItemClickListener);
//        }
//        for (int i = 0; i < itemStrings.length; i++) {
//            inputMenu.registerExtendMenuItem(itemStrings[i], itemdrawables[i], itemIds[i], extendMenuItemClickListener);
//        }
    }


    protected void onConversationInit() {
        conversation = EMClient.getInstance().chatManager().getConversation(toChatUsername, EaseCommonUtils.getConversationType(chatType), true);
        conversation.markAllMessagesAsRead();
        // the number of messages loaded into conversation is getChatOptions().getNumberOfMessagesLoaded
        // you can change this number
        final List<EMMessage> msgs = conversation.getAllMessages();
        int msgCount = msgs != null ? msgs.size() : 0;
        if (msgCount < conversation.getAllMsgCount() && msgCount < pagesize) {
            String msgId = null;
            if (msgs != null && msgs.size() > 0) {
                msgId = msgs.get(0).getMsgId();
            }
            conversation.loadMoreMsgFromDB(msgId, pagesize - msgCount);
        }

    }

    protected void onMessageListInit() {
        messageList.init(toChatUsername, chatType, chatFragmentHelper != null ?
                chatFragmentHelper.onSetCustomChatRowProvider() : null);
        setListItemClickListener();

        messageList.getListView().setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                inputMenu.hideExtendMenuContainer();
                return false;
            }
        });

        isMessageListInited = true;
    }

    protected void setListItemClickListener() {
        messageList.setItemClickListener(new EaseChatMessageList.MessageListItemClickListener() {

            @Override
            public void onUserAvatarClick(String username) {
                if (chatFragmentHelper != null) {
                    chatFragmentHelper.onAvatarClick(username);
                }
            }

            @Override
            public void onUserAvatarLongClick(String username) {
                if (chatFragmentHelper != null) {
                    chatFragmentHelper.onAvatarLongClick(username);
                }
            }

            @Override
            public void onResendClick(final EMMessage message) {
                new EaseAlertDialog(getSelfActivity(), R.string.resend, R.string.confirm_resend, null, new EaseAlertDialog.AlertDialogUser() {
                    @Override
                    public void onResult(boolean confirmed, Bundle bundle) {
                        if (!confirmed) {
                            return;
                        }
                        resendMessage(message);
                    }
                }, true).show();
            }

            @Override
            public boolean onBubbleLongClick(EMMessage message) {
                Log.d("CMCC", "chattestactivity:");
                return false;
            }

            @Override
            public boolean onBubbleClick(EMMessage message) {
                if (chatFragmentHelper == null) {
                    return false;
                }
                if (!ObjectUtils.isNull(message.getStringAttribute("packetId", ""))) {
                    ToastUtil.doToast(getSelfActivity(), "点击了红包!");
                }
                return chatFragmentHelper.onMessageBubbleClick(message);
            }

        });
    }

    protected void setRefreshLayoutListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        if (listView.getFirstVisiblePosition() == 0 && !isloading && haveMoreData) {
                            List<EMMessage> messages;
                            try {
                                if (chatType == EaseConstant.CHATTYPE_SINGLE) {
                                    messages = conversation.loadMoreMsgFromDB(messageList.getItem(0).getMsgId(),
                                            pagesize);
                                } else {
                                    messages = conversation.loadMoreMsgFromDB(messageList.getItem(0).getMsgId(),
                                            pagesize);
                                }
                            } catch (Exception e1) {
                                swipeRefreshLayout.setRefreshing(false);
                                return;
                            }
                            if (messages.size() > 0) {
                                messageList.refreshSeekTo(messages.size() - 1);
                                if (messages.size() != pagesize) {
                                    haveMoreData = false;
                                }
                            } else {
                                haveMoreData = false;
                            }

                            isloading = false;

                        } else {
                            Toast.makeText(getSelfActivity(), getResources().getString(R.string.no_more_messages),
                                    Toast.LENGTH_SHORT).show();
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 600);
            }
        });
    }

    //uri转换为String地址
    public static String uriToRealPath(Context context, Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri,
                new String[]{MediaStore.Images.Media.DATA},
                null,
                null,
                null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
        return path;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //拍照
            if (requestCode == REQUEST_CODE_CAMERA) {
                if (cameraFile != null && cameraFile.exists()) {
                    sendImageMessage(cameraFile.getAbsolutePath());
                }

            } else if (requestCode == REQUEST_CODE_LOCAL) { // send local image
                //图片
                if (data != null) {
                    Uri selectedImage = data.getData();
                    if (selectedImage != null) {
                        sendPicByUri(selectedImage);
                    }
                }
            } else if (requestCode == REQUEST_CODE_MAP) { // location
                double latitude = data.getDoubleExtra("latitude", 0);
                double longitude = data.getDoubleExtra("longitude", 0);
                String locationAddress = data.getStringExtra("address");
                if (locationAddress != null && !locationAddress.equals("")) {
                    sendLocationMessage(latitude, longitude, locationAddress);
                } else {
                    Toast.makeText(getSelfActivity(), R.string.unable_to_get_loaction, Toast.LENGTH_SHORT).show();
                }

            } else if (requestCode == REQUEST_CODE_SELECT_FILE) {
                //send the file 文件
                if (data != null) {
                    Uri uri = data.getData();
                    if (uri != null) {
                        sendFileByUri(uri);
                    }
                }
            } else if (requestCode == ITEM_RED_PACKET_CALL) {
                //红包
                String packetId = data.getStringExtra("packetId");
                String remark = data.getStringExtra("remark");
                String packetType = data.getStringExtra("packetType");
                int groupType = data.getIntExtra("groupType", -1);
                Log.d("CMCC", "setResult-红包-groupType--》" + groupType + ",packetId:" + packetId + ",packetType:" + packetType);
                sendRedPackageMessage(remark, packetId, groupType, packetType);
            }
        }
        //拿到结果返回时关闭底部布局
        inputMenu.hideExtendMenuContainer();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (isMessageListInited)
            messageList.refresh();
        EaseUI.getInstance().pushActivity(getSelfActivity());
        // register the event listener when enter the foreground
        EMClient.getInstance().chatManager().addMessageListener(this);

        if (chatType == EaseConstant.CHATTYPE_GROUP) {
            EaseAtMessageHelper.get().removeAtMeGroup(toChatUsername);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        // unregister this event listener when this activity enters the
        // background
        EMClient.getInstance().chatManager().removeMessageListener(this);

        // remove activity from foreground activity list
        EaseUI.getInstance().popActivity(getSelfActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (groupListener != null) {
            EMClient.getInstance().groupManager().removeGroupChangeListener(groupListener);
        }

        if (chatRoomListener != null) {
            EMClient.getInstance().chatroomManager().removeChatRoomListener(chatRoomListener);
        }

        if (chatType == EaseConstant.CHATTYPE_CHATROOM) {
            EMClient.getInstance().chatroomManager().leaveChatRoom(toChatUsername);
        }

        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
    }

    public void onBackPressed() {
        if (inputMenu.onBackPressed()) {
            finish();
            if (chatType == EaseConstant.CHATTYPE_GROUP) {
                EaseAtMessageHelper.get().removeAtMeGroup(toChatUsername);
                EaseAtMessageHelper.get().cleanToAtUserList();
            }
            if (chatType == EaseConstant.CHATTYPE_CHATROOM) {
                EMClient.getInstance().chatroomManager().leaveChatRoom(toChatUsername);
            }
        }
    }

    protected void onChatRoomViewCreation() {
        final ProgressDialog pd = ProgressDialog.show(getSelfActivity(), "", "Joining......");
        EMClient.getInstance().chatroomManager().joinChatRoom(toChatUsername, new EMValueCallBack<EMChatRoom>() {

            @Override
            public void onSuccess(final EMChatRoom value) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (getSelfActivity().isFinishing() || !toChatUsername.equals(value.getId()))
                            return;
                        pd.dismiss();
                        EMChatRoom room = EMClient.getInstance().chatroomManager().getChatRoom(toChatUsername);
                        if (room != null) {
                            tv_title.setText(room.getName());
                            EMLog.d(TAG, "join room success : " + room.getName());
                        } else {
                            tv_title.setText(toChatUsername);
                        }
                        addChatRoomChangeListenr();
                        onConversationInit();
                        onMessageListInit();
                    }
                });
            }

            @Override
            public void onError(final int error, String errorMsg) {
                // TODO Auto-generated method stub
                EMLog.d(TAG, "join room failure : " + error);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pd.dismiss();
                    }
                });
                finish();
            }
        });
    }

    protected void addChatRoomChangeListenr() {
        /*
        chatRoomChangeListener = new EMChatRoomChangeListener() {

            @Override
            public void onChatRoomDestroyed(String roomId, String roomName) {
                if (roomId.equals(toChatUsername)) {
                    showChatroomToast(" room : " + roomId + " with room name : " + roomName + " was destroyed");
                    getActivity().finish();
                }
            }

            @Override
            public void onMemberJoined(String roomId, String participant) {
                showChatroomToast("member : " + participant + " join the room : " + roomId);
            }

            @Override
            public void onMemberExited(String roomId, String roomName, String participant) {
                showChatroomToast("member : " + participant + " leave the room : " + roomId + " room name : " + roomName);
            }

            @Override
            public void onRemovedFromChatRoom(String roomId, String roomName, String participant) {
                if (roomId.equals(toChatUsername)) {
                    String curUser = EMClient.getInstance().getCurrentUser();
                    if (curUser.equals(participant)) {
                    	EMClient.getInstance().chatroomManager().leaveChatRoom(toChatUsername);
                        getActivity().finish();
                    }else{
                        showChatroomToast("member : " + participant + " was kicked from the room : " + roomId + " room name : " + roomName);
                    }
                }
            }


            // ============================= group_reform new add api begin
            @Override
            public void onMuteListAdded(String chatRoomId, Map<String, Long> mutes) {}

            @Override
            public void onMuteListRemoved(String chatRoomId, List<String> mutes) {}

            @Override
            public void onAdminAdded(String chatRoomId, String admin) {}

            @Override
            public void onAdminRemoved(String chatRoomId, String admin) {}

            @Override
            public void onOwnerChanged(String chatRoomId, String newOwner, String oldOwner) {}

            // ============================= group_reform new add api end

        };

        EMClient.getInstance().chatroomManager().addChatRoomChangeListener(chatRoomChangeListener);
        */
    }

    protected void showChatroomToast(final String toastContent) {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getSelfActivity(), toastContent, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // implement methods in EMMessageListener
    @Override
    public void onMessageReceived(List<EMMessage> messages) {
        for (EMMessage message : messages) {
            String username = null;
            // group message
            if (message.getChatType() == EMMessage.ChatType.GroupChat || message.getChatType() == EMMessage.ChatType.ChatRoom) {
                username = message.getTo();
            } else {
                // single chat message
                username = message.getFrom();
            }

            // if the message is for current conversation
            if (username.equals(toChatUsername) || message.getTo().equals(toChatUsername)) {
                messageList.refreshSelectLast();
                EaseUI.getInstance().getNotifier().vibrateAndPlayTone(message);
                conversation.markMessageAsRead(message.getMsgId());
            } else {
                EaseUI.getInstance().getNotifier().onNewMsg(message);
            }
        }
    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> messages) {

    }

    @Override
    public void onMessageRead(List<EMMessage> messages) {
        if (isMessageListInited) {
            messageList.refresh();
        }
    }

    @Override
    public void onMessageDelivered(List<EMMessage> messages) {
        if (isMessageListInited) {
            messageList.refresh();
        }
    }

    @Override
    public void onMessageChanged(EMMessage emMessage, Object change) {
        if (isMessageListInited) {
            messageList.refresh();
        }
    }


    /**
     * handle the click event for extend menu
     */
    class MyItemClickListener implements EaseChatExtendMenu.EaseChatExtendMenuItemClickListener {

        @Override
        public void onClick(int itemId, View view) {
            if (chatFragmentHelper != null) {
                if (chatFragmentHelper.onExtendMenuItemClick(itemId, view)) {
                    return;
                }
            }
            switch (itemId) {
                case ITEM_TAKE_PICTURE:
                    selectPicFromCamera();
                    break;
                case ITEM_PICTURE:
                    selectPicFromLocal();
                    break;
                case ITEM_FILE:
                    selectFileFromLocal();
                    break;
                case ITEM_RED_PACKET:
                    Intent intent = new Intent();
                    if (chatType == Constant.CHATTYPE_GROUP) {
                        intent.setClass(getSelfActivity(), SendRedEnvelopesGroupChatActivity.class);
                        intent.putExtra("groupInfo", groupInfo);
                    } else if (chatType == Constant.CHATTYPE_CHATROOM) {
                        intent.setClass(getSelfActivity(), SendRedEnvelopesGroupChatActivity.class);
                        intent.putExtra("groupInfo", groupInfo);
                    } else {
                        intent.setClass(getSelfActivity(), SendRedEnvelopesSingleChatActivity.class);
                        intent.putExtra("memberId", toChatUsername);
                    }
                    startActivityForResult(intent, ITEM_RED_PACKET_CALL);
                    break;

                default:
                    break;
            }
        }

    }

    /**
     * input @
     *
     * @param username
     */
    protected void inputAtUsername(String username, boolean autoAddAtSymbol) {
        if (EMClient.getInstance().getCurrentUser().equals(username) ||
                chatType != EaseConstant.CHATTYPE_GROUP) {
            return;
        }
        EaseAtMessageHelper.get().addAtUser(username);
        EaseUser user = EaseUserUtils.getUserInfo(username);
        if (user != null) {
            username = user.getNick();
        }
        if (autoAddAtSymbol)
            inputMenu.insertText("@" + username + " ");
        else
            inputMenu.insertText(username + " ");
    }


    /**
     * input @
     *
     * @param username
     */
    protected void inputAtUsername(String username) {
        inputAtUsername(username, true);
    }


    protected void sendRedPackageMessage(String remark, String packetId, int groupType, String packetType) {
        if (EaseAtMessageHelper.get().containsAtUsername(remark)) {
            sendAtMessage(remark);
        } else {
            EMMessage emMessage = EaseCommonUtils.createRedPackageMessage(remark, toChatUsername, packetId);
            //emMessage.setAttribute("packetId", packetId);
            emMessage.setAttribute("userId", baseApplication.getMemberId());
            emMessage.setAttribute("iconUrl", baseApplication.getHeadImg());
            emMessage.setAttribute("nickName", baseApplication.getNickName());
            emMessage.setAttribute("groupType", groupType);
            emMessage.setAttribute("packetType", packetType);
            if (chatType == EaseConstant.CHATTYPE_GROUP ||
                    chatType == EaseConstant.CHATTYPE_CHATROOM) {
                emMessage.setAttribute("groupUrl", groupInfo.getGroupUrl());
                emMessage.setAttribute("groupName", groupInfo.getGroupName());
            }
            sendMessage(emMessage);
        }
    }

    //send message
    protected void sendTextMessage(String content) {
        if (EaseAtMessageHelper.get().containsAtUsername(content)) {
            sendAtMessage(content);
        } else {
            EMMessage message = EMMessage.createTxtSendMessage(content, toChatUsername);
            message.setAttribute("userId", baseApplication.getMemberId());
            message.setAttribute("iconUrl", baseApplication.getHeadImg());
            message.setAttribute("nickName", baseApplication.getNickName());
            if (chatType == EaseConstant.CHATTYPE_GROUP ||
                    chatType == EaseConstant.CHATTYPE_CHATROOM) {
                message.setAttribute("groupUrl", groupInfo.getGroupUrl());
                message.setAttribute("groupName", groupInfo.getGroupName());
                Log.d("CMCC", baseApplication.getMemberId() + "," + baseApplication.getHeadImg() +
                        baseApplication.getNickName() + "," + groupInfo.getGroupUrl() + "," +
                        groupInfo.getGroupName());
            }
            Log.d("CMCC", baseApplication.getMemberId() + "," + baseApplication.getHeadImg() +
                    baseApplication.getNickName());
            // TODO: 2017/5/17
            sendMessage(message);
        }
    }

    /**
     * send @ message, only support group chat message
     *
     * @param content
     */
    @SuppressWarnings("ConstantConditions")
    private void sendAtMessage(String content) {
        if (chatType != EaseConstant.CHATTYPE_GROUP) {
            EMLog.e(TAG, "only support group chat message");
            return;
        }
        EMMessage message = EMMessage.createTxtSendMessage(content, toChatUsername);
        EMGroup group = EMClient.getInstance().groupManager().getGroup(toChatUsername);
        if (EMClient.getInstance().getCurrentUser().equals(group.getOwner()) && EaseAtMessageHelper.get().containsAtAll(content)) {
            message.setAttribute(EaseConstant.MESSAGE_ATTR_AT_MSG, EaseConstant.MESSAGE_ATTR_VALUE_AT_MSG_ALL);
        } else {
            message.setAttribute(EaseConstant.MESSAGE_ATTR_AT_MSG,
                    EaseAtMessageHelper.get().atListToJsonArray(EaseAtMessageHelper.get().getAtMessageUsernames(content)));
        }
        message.setAttribute("userId", baseApplication.getMemberId());
        message.setAttribute("iconUrl", baseApplication.getHeadImg());
        message.setAttribute("nickName", baseApplication.getNickName());
        if (chatType == EaseConstant.CHATTYPE_GROUP ||
                chatType == EaseConstant.CHATTYPE_CHATROOM) {
            message.setAttribute("groupUrl", groupInfo.getGroupUrl());
            message.setAttribute("groupName", groupInfo.getGroupName());
            Log.d("CMCC", baseApplication.getMemberId() + "," + baseApplication.getHeadImg() +
                    "," + baseApplication.getNickName() + "," + groupInfo.getGroupUrl() + "," +
                    groupInfo.getGroupName());
        }
        Log.d("CMCC", baseApplication.getMemberId() + "," + baseApplication.getHeadImg() +
                baseApplication.getNickName());
        sendMessage(message);

    }


    protected void sendBigExpressionMessage(String name, String identityCode) {
        EMMessage emMessage = EaseCommonUtils.createExpressionMessage(toChatUsername, name, identityCode);
        emMessage.setAttribute("userId", baseApplication.getMemberId());
        emMessage.setAttribute("iconUrl", baseApplication.getHeadImg());
        emMessage.setAttribute("nickName", baseApplication.getNickName());
        if (chatType == EaseConstant.CHATTYPE_GROUP ||
                chatType == EaseConstant.CHATTYPE_CHATROOM) {
            emMessage.setAttribute("groupUrl", groupInfo.getGroupUrl());
            emMessage.setAttribute("groupName", groupInfo.getGroupName());
        }
        sendMessage(emMessage);
    }

    protected void sendVoiceMessage(String filePath, int length) {
        EMMessage emMessage = EMMessage.createVoiceSendMessage(filePath, length, toChatUsername);
        emMessage.setAttribute("userId", baseApplication.getMemberId());
        emMessage.setAttribute("iconUrl", baseApplication.getHeadImg());
        emMessage.setAttribute("nickName", baseApplication.getNickName());
        if (chatType == EaseConstant.CHATTYPE_GROUP ||
                chatType == EaseConstant.CHATTYPE_CHATROOM) {
            emMessage.setAttribute("groupUrl", groupInfo.getGroupUrl());
            emMessage.setAttribute("groupName", groupInfo.getGroupName());
        }
        sendMessage(emMessage);
    }

    protected void sendImageMessage(String imagePath) {
        EMMessage emMessage = EMMessage.createImageSendMessage(imagePath, false, toChatUsername);
        emMessage.setAttribute("userId", baseApplication.getMemberId());
        emMessage.setAttribute("iconUrl", baseApplication.getHeadImg());
        emMessage.setAttribute("nickName", baseApplication.getNickName());
        if (chatType == EaseConstant.CHATTYPE_GROUP ||
                chatType == EaseConstant.CHATTYPE_CHATROOM) {
            emMessage.setAttribute("groupUrl", groupInfo.getGroupUrl());
            emMessage.setAttribute("groupName", groupInfo.getGroupName());
        }
        sendMessage(emMessage);
    }

    protected void sendLocationMessage(double latitude, double longitude, String locationAddress) {
        EMMessage emMessage = EMMessage.createLocationSendMessage(latitude, longitude, locationAddress, toChatUsername);
        emMessage.setAttribute("userId", baseApplication.getMemberId());
        emMessage.setAttribute("iconUrl", baseApplication.getHeadImg());
        emMessage.setAttribute("nickName", baseApplication.getNickName());
        if (chatType == EaseConstant.CHATTYPE_GROUP ||
                chatType == EaseConstant.CHATTYPE_CHATROOM) {
            emMessage.setAttribute("groupUrl", groupInfo.getGroupUrl());
            emMessage.setAttribute("groupName", groupInfo.getGroupName());
        }
        sendMessage(emMessage);
    }

    protected void sendVideoMessage(String videoPath, String thumbPath, int videoLength) {
        EMMessage emMessage = EMMessage.createVideoSendMessage(videoPath, thumbPath, videoLength, toChatUsername);
        emMessage.setAttribute("userId", baseApplication.getMemberId());
        emMessage.setAttribute("iconUrl", baseApplication.getHeadImg());
        emMessage.setAttribute("nickName", baseApplication.getNickName());
        if (chatType == EaseConstant.CHATTYPE_GROUP ||
                chatType == EaseConstant.CHATTYPE_CHATROOM) {
            emMessage.setAttribute("groupUrl", groupInfo.getGroupUrl());
            emMessage.setAttribute("groupName", groupInfo.getGroupName());
        }
        sendMessage(emMessage);
    }

    protected void sendFileMessage(String filePath) {
        EMMessage emMessage = EMMessage.createFileSendMessage(filePath, toChatUsername);
        emMessage.setAttribute("userId", baseApplication.getMemberId());
        emMessage.setAttribute("iconUrl", baseApplication.getHeadImg());
        emMessage.setAttribute("nickName", baseApplication.getNickName());
        if (chatType == EaseConstant.CHATTYPE_GROUP ||
                chatType == EaseConstant.CHATTYPE_CHATROOM) {
            emMessage.setAttribute("groupUrl", groupInfo.getGroupUrl());
            emMessage.setAttribute("groupName", groupInfo.getGroupName());
        }
        sendMessage(emMessage);
    }


    protected void sendMessage(EMMessage message) {
        if (message == null) {
            return;
        }
        if (chatFragmentHelper != null) {
            //set extension
            chatFragmentHelper.onSetMessageAttributes(message);
        }
        if (chatType == EaseConstant.CHATTYPE_GROUP) {
            message.setChatType(EMMessage.ChatType.GroupChat);
        } else if (chatType == EaseConstant.CHATTYPE_CHATROOM) {
            message.setChatType(EMMessage.ChatType.ChatRoom);
        }
        //send message
        EMClient.getInstance().chatManager().sendMessage(message);
        //refresh ui
        if (isMessageListInited) {
            messageList.refreshSelectLast();
        }
    }


    public void resendMessage(EMMessage message) {
        message.setStatus(EMMessage.Status.CREATE);
        EMClient.getInstance().chatManager().sendMessage(message);
        messageList.refresh();
    }

    //===================================================================================


    /**
     * send image
     *
     * @param selectedImage
     */
    protected void sendPicByUri(Uri selectedImage) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            cursor = null;

            if (picturePath == null || picturePath.equals("null")) {
                Toast toast = Toast.makeText(getSelfActivity(), R.string.cant_find_pictures, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;
            }
            sendImageMessage(picturePath);
        } else {
            File file = new File(selectedImage.getPath());
            if (!file.exists()) {
                Toast toast = Toast.makeText(getSelfActivity(), R.string.cant_find_pictures, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;

            }
            sendImageMessage(file.getAbsolutePath());
        }

    }

    /**
     * send file
     *
     * @param uri
     */
    protected void sendFileByUri(Uri uri) {
        String filePath = null;
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = null;

            try {
                cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    filePath = cursor.getString(column_index);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            filePath = uri.getPath();
        }
        if (filePath == null) {
            return;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            Toast.makeText(getSelfActivity(), R.string.File_does_not_exist, Toast.LENGTH_SHORT).show();
            return;
        }
        //limit the size < 10M
        if (file.length() > 10 * 1024 * 1024) {
            Toast.makeText(getSelfActivity(), R.string.The_file_is_not_greater_than_10_m, Toast.LENGTH_SHORT).show();
            return;
        }
        sendFileMessage(filePath);
    }

    /**
     * capture new image
     */
    protected void selectPicFromCamera() {
        if (!EaseCommonUtils.isSdcardExist()) {
            Toast.makeText(getSelfActivity(), R.string.sd_card_does_not_exist, Toast.LENGTH_SHORT).show();
            return;
        }

        cameraFile = new File(PathUtil.getInstance().getImagePath(), EMClient.getInstance().getCurrentUser()
                + System.currentTimeMillis() + ".jpg");
        //noinspection ResultOfMethodCallIgnored
        cameraFile.getParentFile().mkdirs();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(getSelfActivity(), BuildConfig.APPLICATION_ID + ".fileProvider", cameraFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile));
        }

        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    /**
     * select local image
     */
    protected void selectPicFromLocal() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");

        } else {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_LOCAL);
    }

    /**
     * select file
     */
    protected void selectFileFromLocal() {
        Intent intent = null;
//        if (Build.VERSION.SDK_INT < 19) { //api 19 and later, we can't use this way, demo just select from images
//            intent = new Intent(Intent.ACTION_GET_CONTENT);
//            intent.setType("*/*");
//            intent.addCategory(Intent.CATEGORY_OPENABLE);
//
//        } else {
//            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        }
        intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_CODE_SELECT_FILE);
    }


    /**
     * clear the conversation history
     */
    protected void emptyHistory() {
        String msg = getResources().getString(R.string.Whether_to_empty_all_chats);
        new EaseAlertDialog(getSelfActivity(), null, msg, null, new EaseAlertDialog.AlertDialogUser() {

            @Override
            public void onResult(boolean confirmed, Bundle bundle) {
                if (confirmed) {
                    if (conversation != null) {
                        conversation.clearAllMessages();
                    }
                    messageList.refresh();
                }
            }
        }, true).show();
    }

    /**
     * open group detail
     */
    protected void toGroupDetails() {
        if (chatType == EaseConstant.CHATTYPE_GROUP) {
//            EMGroup group = EMClient.getInstance().groupManager().getGroup(toChatUsername);
//            if (group == null) {
//                Toast.makeText(getSelfActivity(), R.string.gorup_not_found, Toast.LENGTH_SHORT).show();
//                return;
//            }
            Intent intent = new Intent(getSelfActivity(), GroupSettingActivity.class);
            intent.putExtra("groupId", "");
            intent.putExtra("easemobGroupId", toChatUsername);
            startActivity(intent);
//            if (chatFragmentHelper != null) {
//                chatFragmentHelper.onEnterToChatDetails();
//            }
        } else if (chatType == EaseConstant.CHATTYPE_CHATROOM) {
            if (chatFragmentHelper != null) {
                chatFragmentHelper.onEnterToChatDetails();
            }
        }
    }

    /**
     * hide
     */
    protected void hideKeyboard() {
        if (getSelfActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getSelfActivity().getCurrentFocus() != null)
                inputManager.hideSoftInputFromWindow(getSelfActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * forward message
     *
     * @param forward_msg_id
     */
    protected void forwardMessage(String forward_msg_id) {
        final EMMessage forward_msg = EMClient.getInstance().chatManager().getMessage(forward_msg_id);
        EMMessage.Type type = forward_msg.getType();
        switch (type) {
            case TXT:
                if (forward_msg.getBooleanAttribute(EaseConstant.MESSAGE_ATTR_IS_BIG_EXPRESSION, false)) {
                    sendBigExpressionMessage(((EMTextMessageBody) forward_msg.getBody()).getMessage(),
                            forward_msg.getStringAttribute(EaseConstant.MESSAGE_ATTR_EXPRESSION_ID, null));
                } else {
                    // get the content and send it
                    String content = ((EMTextMessageBody) forward_msg.getBody()).getMessage();
                    sendTextMessage(content);
                }
                break;
            case IMAGE:
                // send image
                String filePath = ((EMImageMessageBody) forward_msg.getBody()).getLocalUrl();
                if (filePath != null) {
                    File file = new File(filePath);
                    if (!file.exists()) {
                        // send thumb nail if original image does not exist
                        filePath = ((EMImageMessageBody) forward_msg.getBody()).thumbnailLocalPath();
                    }
                    sendImageMessage(filePath);
                }
                break;
            default:
                break;
        }

        if (forward_msg.getChatType() == EMMessage.ChatType.ChatRoom) {
            EMClient.getInstance().chatroomManager().leaveChatRoom(forward_msg.getTo());
        }
    }

    /**
     * listen the group event
     */
    class GroupListener extends EaseGroupListener {

        @Override
        public void onUserRemoved(final String groupId, String groupName) {
            runOnUiThread(new Runnable() {

                public void run() {
                    if (toChatUsername.equals(groupId)) {
                        Toast.makeText(getSelfActivity(), R.string.you_are_group, Toast.LENGTH_LONG).show();
                        Activity activity = getSelfActivity();
                        if (activity != null && !activity.isFinishing()) {
                            activity.finish();
                        }
                    }
                }
            });
        }

        @Override
        public void onGroupDestroyed(final String groupId, String groupName) {
            // prompt group is dismissed and finish this activity
            runOnUiThread(new Runnable() {
                public void run() {
                    if (toChatUsername.equals(groupId)) {
                        Toast.makeText(getSelfActivity(), R.string.the_current_group_destroyed, Toast.LENGTH_LONG).show();
                        Activity activity = getSelfActivity();
                        if (activity != null && !activity.isFinishing()) {
                            activity.finish();
                        }
                    }
                }
            });
        }
    }

    /**
     * listen chat room event
     */
    class ChatRoomListener extends EaseChatRoomListener {

        @Override
        public void onChatRoomDestroyed(final String roomId, final String roomName) {
            runOnUiThread(new Runnable() {
                public void run() {
                    if (roomId.equals(toChatUsername)) {
                        Toast.makeText(getSelfActivity(), R.string.the_current_chat_room_destroyed, Toast.LENGTH_LONG).show();
                        Activity activity = getSelfActivity();
                        if (activity != null && !activity.isFinishing()) {
                            activity.finish();
                        }
                    }
                }
            });
        }

        @Override
        public void onRemovedFromChatRoom(final String roomId, final String roomName, final String participant) {
            runOnUiThread(new Runnable() {
                public void run() {
                    if (roomId.equals(toChatUsername)) {
                        Toast.makeText(getSelfActivity(), R.string.quiting_the_chat_room, Toast.LENGTH_LONG).show();
                        Activity activity = getSelfActivity();
                        if (activity != null && !activity.isFinishing()) {
                            activity.finish();
                        }
                    }
                }
            });
        }

        @Override
        public void onMemberJoined(final String roomId, final String participant) {
            if (roomId.equals(toChatUsername)) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getSelfActivity(), "member join:" + participant, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }

        @Override
        public void onMemberExited(final String roomId, final String roomName, final String participant) {
            if (roomId.equals(toChatUsername)) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getSelfActivity(), "member exit:" + participant, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }


    }

    protected EaseChatFragment.EaseChatFragmentHelper chatFragmentHelper;

    public void setChatFragmentListener(EaseChatFragment.EaseChatFragmentHelper chatFragmentHelper) {
        this.chatFragmentHelper = chatFragmentHelper;
    }

    public interface EaseChatFragmentHelper {
        /**
         * set message attribute
         */
        void onSetMessageAttributes(EMMessage message);

        /**
         * enter to chat detail
         */
        void onEnterToChatDetails();

        /**
         * on avatar clicked
         *
         * @param username
         */
        void onAvatarClick(String username);

        /**
         * on avatar long pressed
         *
         * @param username
         */
        void onAvatarLongClick(String username);

        /**
         * on message bubble clicked
         */
        boolean onMessageBubbleClick(EMMessage message);

        /**
         * on message bubble long pressed
         */
        void onMessageBubbleLongClick(EMMessage message);

        /**
         * on extend menu item clicked, return true if you want to override
         *
         * @param view
         * @param itemId
         * @return
         */
        boolean onExtendMenuItemClick(int itemId, View view);

        /**
         * on set custom chat row provider
         *
         * @return
         */
        EaseCustomChatRowProvider onSetCustomChatRowProvider();
    }


    EMMessageListener msgListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            //收到消息
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            //收到透传消息
            Log.i("CMCC", "收到透传消息!" + messages.size());
            for (EMMessage emMessage : messages) {
                EMCmdMessageBody body = (EMCmdMessageBody) emMessage.getBody();
                Log.d("CMCC", "action:" + body.action());
                //先用基类解析出type
                Gson gson = new Gson();
                MessageTypeObject typeObject = gson.fromJson(body.action(), MessageTypeObject.class);
                //想聊天里面插入消息
                if ("401".equals(typeObject.getType()) ||
                        "402".equals(typeObject.getType())) {
                    //解析
                    RedPacketHint hint = gson.fromJson(body.action(), RedPacketHint.class);
                    Log.d("CMCC", "1111111");
                    //群红包和普通红包
                    if (baseApplication.getMemberId().equals(hint.getMemberId())) {
                        //让你看到
                        Log.d("CMCC", "2222222");
                        createHintMessage(hint);
                    }

                }

                if ("501".equals(typeObject.getType()) ||
                        "502".equals(typeObject.getType()) ||
                        "503".equals(typeObject.getType()) ||
                        "504".equals(typeObject.getType())) {
                    //501 加群审核   502 进群通知   503 退群通知   504  群解散
                    Log.d("CMCC", "群聊type:" + emMessage.getStringAttribute("type", ""));
                    if (chatType == EaseConstant.CHATTYPE_GROUP) {
                        GroupHint groupHint = gson.fromJson(body.action(), GroupHint.class);
                        //501 特殊处理有点击事件
                        if ("501".equals(groupHint.getType())) {
                            Log.d("CMCC", "501:加群审核");
                            //判断是否相等
                            if (baseApplication.getMemberId().equals(groupHint.getMemberId())) {
                                Log.d("CMCC", "333333");
                                createGroupHintMessage(groupHint);
                            }
                        } else {
                            Log.d("CMCC", "44444444 groupHint:" + groupHint.getMessage());
                            createGroupHintMessage(groupHint);
                        }
                    }
                }

            }
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
            //收到已读回执
        }

        @Override
        public void onMessageDelivered(List<EMMessage> message) {
            //收到已送达回执
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
            //消息状态变动
        }
    };

    /**
     * 创建群通知
     *
     * @param groupHint
     */
    private void createGroupHintMessage(GroupHint groupHint) {
        EMMessage message = EaseCommonUtils.createGroupHintMessage(toChatUsername, groupHint.getMessage(),
                System.currentTimeMillis() + "", groupHint);
        message.setChatType(EMMessage.ChatType.GroupChat);
        EMClient.getInstance().chatManager()
                .getConversation(toChatUsername)
                .appendMessage(message);
        //刷新
        messageList.refresh();
    }


    /**
     * 向本地会话列表插入消息红包消息提示
     */
    public void createHintMessage(RedPacketHint hint) {
        Log.d("CMCC", "message:" + hint.getMessage() + ",groupId:" + hint.getGroupId() +
                ",packetId:" + hint.getPacketId() + ",type:" + hint.getType() +
                ",easemobGroupId:" + toChatUsername);
        EMMessage message = EaseCommonUtils.createHintMessage(toChatUsername, hint.getMessage(), System.currentTimeMillis() + "", hint);
        if ("402".equals(hint.getType())) {
            message.setChatType(EMMessage.ChatType.GroupChat);
        }
        EMClient.getInstance().chatManager()
                .getConversation(toChatUsername)
                .appendMessage(message);
        //刷新
        messageList.refresh();
    }
}
