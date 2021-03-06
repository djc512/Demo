package huanxing_print.com.cn.printhome.ui.activity.chat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.model.contact.FriendInfo;
import huanxing_print.com.cn.printhome.model.contact.FriendSearchInfo;
import huanxing_print.com.cn.printhome.model.contact.GroupInfo;
import huanxing_print.com.cn.printhome.presenter.ChatPresenter;
import huanxing_print.com.cn.printhome.presenter.impl.ChatPresenterImpl;
import huanxing_print.com.cn.printhome.ui.adapter.ChatAdapter;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.util.copy.BitmapCorrectUtil;
import huanxing_print.com.cn.printhome.util.copy.PicSaveUtil;

import static huanxing_print.com.cn.printhome.util.ShowUtil.showToast;

/**
 * Created by htj on 2017/5/8.
 */

public class ChatActivity extends BaseActivity implements TextWatcher, ChatView, View.OnClickListener {

    private ChatPresenter mChatPresenter;
    private String mUsername;
    //private ChatAdapter mChatAdapter;
    private Context ctx;

    private File tempFile;
    private static final int REQUEST_CAPTURE = 100;
    private static final int REQUEST_FILE = 110;
    private static final int PICK_PHOTO = 1;
    private static final int TEXT = 0;//文本
    private static final int PIC = 1;//图片
    private  int mKind;
    private int CODE_APPROVAL_REQUEST = 0X11;//审批人请求码
    private int CODE_COPY_REQUEST = 0X12;//抄送人人请求码
    private List<Bitmap> mResults = new ArrayList<>();
    private int PICK_IMAGE_REQUEST = 1;

    private ChatAdapter mChatAdapter;
    Boolean isOpen = false;
    private int kind;
    private PicSaveUtil saveUtil;

    TextView mTvTitle;
    //标题栏没写

    //TextView mToolBar;
    RecyclerView mRecyclerView;
    EditText mEtMsg;
    Button mBtnSend;
    ImageView mAdd;
    ImageView mBack;
    LinearLayout mAddFile;
    //底部的四个图片
    LinearLayout mPhoto;
    LinearLayout mCarema;
    LinearLayout mFile;
    LinearLayout mRedPacket;
    //区分群聊和单聊
    private int type;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 改变状态栏的颜色使其与APP风格一体化
        setContentView(R.layout.activity_chat);
        CommonUtils.initSystemBar(this);
        //设置布局不被顶出去
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ctx = this;

        saveUtil = new PicSaveUtil(ctx);
        tempFile = saveUtil.createCameraTempFile(savedInstanceState);
        initVIew();
        initListener();

        Intent intent = getIntent();
        type = intent.getIntExtra("type", -1);
        mUsername = intent.getStringExtra("id");
        String name = intent.getStringExtra("name");
        mTvTitle.setText(name);

        Log.i("CMCC", "type:" + type + ",id:" + mUsername + ",name:" + name);

        GroupInfo groupInfo = intent.getParcelableExtra("GroupInfo");
        FriendInfo friendInfo = intent.getParcelableExtra("FriendInfo");
        FriendSearchInfo friendSearchInfo = intent.getParcelableExtra("FriendSearchInfo");
        if (!ObjectUtils.isNull(groupInfo)) {
            //群聊
            type = 1;
            mUsername = groupInfo.getEasemobGroupId();
            mTvTitle.setText(groupInfo.getGroupName());
            Log.i("CMCC", "type:" + type + ",mUsername:" + mUsername);
        }

        if (!ObjectUtils.isNull(friendInfo)) {
            //私聊
            type = 2;
            mUsername = friendInfo.getEasemobId();
            mTvTitle.setText(friendInfo.getMemberName());
            Log.i("CMCC", "type:" + type + ",mUsername:" + mUsername);
        }

        if (!ObjectUtils.isNull(friendSearchInfo)) {
            //私聊
            type = 2;
            mUsername = friendSearchInfo.getMemberId();
            mTvTitle.setText(friendSearchInfo.getMemberName());
            Log.i("CMCC", "type:" + type + ",mUsername:" + mUsername);
        }

        //聊天对象
        // mUsername = intent.getStringExtra("username");
        //测试，
        //mUsername = "15830317334532";

        if (TextUtils.isEmpty(mUsername)) {
            showToast("跟鬼聊呀，请携带username参数！");
            finish();
            return;
        }


        mEtMsg.addTextChangedListener(this);
        String msg = mEtMsg.getText().toString();
        if (TextUtils.isEmpty(msg)) {
            mBtnSend.setEnabled(false);
        } else {
            mBtnSend.setEnabled(true);
        }
        mChatPresenter = new ChatPresenterImpl(this);
        /**
         * 显示最多最近的20条聊天记录，然后定位RecyclerView到最后一行
         */
        mChatPresenter.initChat(mUsername,TEXT);
        EventBus.getDefault().register(this);

    }


    protected void initVIew() {
        mBack = (ImageView) findViewById(R.id.iv_back);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mEtMsg = (EditText) findViewById(R.id.et_msg);
        mBtnSend = (Button) findViewById(R.id.btn_send);
        mAdd = (ImageView) findViewById(R.id.iv_add);
        mAddFile = (LinearLayout) findViewById(R.id.ll_add_file);
        //底部的四个图片
        mPhoto = (LinearLayout) findViewById(R.id.ll_photo);
        mCarema = (LinearLayout) findViewById(R.id.ll_camera);
        mFile = (LinearLayout) findViewById(R.id.ll_file);
        mRedPacket = (LinearLayout) findViewById(R.id.ll_red_packet);


    }

    private void initListener() {
        mBtnSend.setOnClickListener(this);
        mAdd.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mEtMsg.setOnClickListener(this);
        mPhoto.setOnClickListener(this);
        mCarema.setOnClickListener(this);
        mFile.setOnClickListener(this);
        mRedPacket.setOnClickListener(this);

    }


//接收消息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void on(EMMessage message) {
        //当收到信消息的时候
        /*
         *  判断当前这个消息是不是正在聊天的用户给我发的
         *  如果是，让ChatPresenter 更新数据
         *
         */

        String from = "";
        if (type == 1) {
            from = message.getTo();
            Log.i("CMCC", "111111from:" + from + ",mUsername:" + mUsername);
        } else if (type == 2) {
            from = message.getFrom();
            Log.i("CMCC", "222222from:" + from + ",mUsername:" + mUsername);
        }
        try {
            mKind = message.getIntAttribute("kind");
//        message.getUserName();
//        message.getTo();

                if (from.equals(mUsername)) {
                    mChatPresenter.updateData(mUsername,mKind);
                }
        } catch (HyphenateException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        //EMClient.getInstance().chatManager().removeMessageListener(msgListener);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finishCurrentActivity();
                break;
            case R.id.et_msg:
                //如果打开就关闭
                if(isOpen){
                    mAddFile.setVisibility(View.GONE);
                    isOpen = false;
                }
                break;
            case R.id.iv_add:
                //点击添加的操作
                if (isOpen) {
                    mAddFile.setVisibility(View.GONE);
                    isOpen = false;
                } else {
                    mAddFile.setVisibility(View.VISIBLE);
                    InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
                    isOpen = true;
                }


                break;
            case R.id.btn_send:
                Log.i("CMCC", "发送消息:" + type + ",mUsername:" + mUsername);
                //发送消息
                String msgg = mEtMsg.getText().toString();
                if (type == 1) {
                    Log.i("CMCC", "1111");
                    mChatPresenter.sendMessage(mUsername, msgg, 1,TEXT);
                } else if (2 == type) {
                    Log.i("CMCC", "2222");
                    mChatPresenter.sendMessage(mUsername, msgg, 0,TEXT);
                }

                mEtMsg.getText().clear();

                break;
            case R.id.ll_photo:
                Intent intentPhoto = new Intent();
                intentPhoto.setType("image/*");
                intentPhoto.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intentPhoto, "Select Picture"), PICK_IMAGE_REQUEST);

                break;
            case R.id.ll_camera:
                Intent intenCamera_ = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intenCamera_.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
                startActivityForResult(intenCamera_, REQUEST_CAPTURE);

                break;
            case R.id.ll_file:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent,REQUEST_FILE);

                break;
            case R.id.ll_red_packet:
                if (type==1){
                    //群聊
                    jumpActivity(SendRedEnvelopesGroupChatActivity.class);
                }else {
                    //私聊
                    jumpActivity(SendRedEnvelopesSingleChatActivity.class);
                }

                break;
            default:
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            String path = BitmapCorrectUtil.uriTopath(getSelfActivity(),uri);
            Log.i("CMCC","图片地址--------------------------"+path);
            mChatPresenter.sendImgMessage(mUsername,path,type,PIC);
        }else if (requestCode == REQUEST_CAPTURE && resultCode == RESULT_OK) {
            Uri uri = Uri.fromFile(tempFile);
            String path = BitmapCorrectUtil.uriTopath(getSelfActivity(),uri);

            mChatPresenter.sendImgMessage(mUsername,path,type,PIC);
        }else {
            Uri uri = data.getData();
            String path = BitmapCorrectUtil.uriTopath(getSelfActivity(),uri);
            mChatPresenter.sendImgMessage(mUsername,path,type,PIC);
        }


    }

   /* protected void sendFileByUri(Uri uri){
        String filePath = null;
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = null;

            try {
                cursor = getSelfActivity().getContentResolver().query(uri, filePathColumn, null, null, null);
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
*/

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().length() == 0) {
            mBtnSend.setEnabled(false);
            mBtnSend.setVisibility(View.GONE);
            mAdd.setVisibility(View.VISIBLE);

        } else {
            mBtnSend.setEnabled(true);
            mBtnSend.setVisibility(View.VISIBLE);
            mAdd.setVisibility(View.GONE);

        }
    }

    @Override
    public void onInit(List<EMMessage> emMessageList) {
        /**
         * 初始化RecyclerView
         */
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mChatAdapter = new ChatAdapter(getSelfActivity(),emMessageList);
        mRecyclerView.setAdapter(mChatAdapter);
        if (emMessageList.size() != 0) {
            mRecyclerView.smoothScrollToPosition(emMessageList.size() - 1);
        }
    }

    @Override
    public void onUpdate(int size) {
        Log.i("CMCC", "收到消息了10100101010101010101100");
        mChatAdapter.notifyDataSetChanged();
        if (size != 0) {
            mRecyclerView.smoothScrollToPosition(size - 1);
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus()) {
            /**
             * 点击空白位置 隐藏软键盘
             */
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
            //隐藏文件部分
            //mAddFile.setVisibility(View.VISIBLE);
        }
        return super.onTouchEvent(event);
    }

}
