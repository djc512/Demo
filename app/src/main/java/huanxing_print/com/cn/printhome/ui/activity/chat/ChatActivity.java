package huanxing_print.com.cn.printhome.ui.activity.chat;

import android.content.Context;
import android.os.Bundle;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.presenter.ChatPresenter;
import huanxing_print.com.cn.printhome.presenter.impl.ChatPresenterImpl;
import huanxing_print.com.cn.printhome.ui.adapter.ChatAdapter;
import huanxing_print.com.cn.printhome.util.CommonUtils;

import static huanxing_print.com.cn.printhome.util.ShowUtil.showToast;

/**
 * Created by htj on 2017/5/8.
 */

public class ChatActivity extends BaseActivity implements TextWatcher,ChatView, View.OnClickListener{

    private ChatPresenter mChatPresenter;
    private String mUsername;
    //private ChatAdapter mChatAdapter;
    private Context ctx;

    private ChatAdapter mChatAdapter;
    Boolean isOpen = false;

    TextView mTvTitle;
    //标题栏没写

    //TextView mToolBar;
    RecyclerView mRecyclerView;
    EditText mEtMsg;
    Button mBtnSend;
    ImageView mAdd;
    LinearLayout mAddFile;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 改变状态栏的颜色使其与APP风格一体化
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.activity_chat);
        //设置布局不被顶出去
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ctx = this;

        initVIew();
        initListener();

        /*Intent intent = getIntent();
        //聊天对象
        mUsername = intent.getStringExtra("username");*/
        //测试，
        mUsername = "11111111111" +
                "";

        if (TextUtils.isEmpty(mUsername)){
            showToast("跟鬼聊呀，请携带username参数！");
            finish();
            return;
        }
        mTvTitle.setText("与"+ mUsername +"聊天中");

        mEtMsg.addTextChangedListener(this);
        String msg = mEtMsg.getText().toString();
        if (TextUtils.isEmpty(msg)){
            mBtnSend.setEnabled(false);
        }else {
            mBtnSend.setEnabled(true);
        }
        mChatPresenter = new ChatPresenterImpl(this);
        /**
         * 显示最多最近的20条聊天记录，然后定位RecyclerView到最后一行
         */
        mChatPresenter.initChat(mUsername);
        EventBus.getDefault().register(this);

    }


    protected void initVIew(){
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mEtMsg = (EditText) findViewById(R.id.et_msg);
        mBtnSend = (Button) findViewById(R.id.btn_send);
        mAdd = (ImageView) findViewById(R.id.iv_add);
        mAddFile = (LinearLayout) findViewById(R.id.ll_add_file);

    }

    private void initListener() {
        mBtnSend.setOnClickListener(this);
        mAdd.setOnClickListener(this);
    }




    @Subscribe(threadMode = ThreadMode.MAIN)
    public void on(EMMessage message){
        //当收到信消息的时候
        /*
         *  判断当前这个消息是不是正在聊天的用户给我发的
         *  如果是，让ChatPresenter 更新数据
         *
         */
        Log.i("CMCC","事件接收到");
        String from = message.getFrom();
        if (from.equals(mUsername)){
            mChatPresenter.updateData(mUsername);
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
        switch (view.getId()){
            case R.id.et_msg:
                String msg = mEtMsg.getText().toString();
                mChatPresenter.sendMessage(mUsername,msg);
                mEtMsg.getText().clear();
                break;
            case R.id.iv_add:
                //点击添加的操作
                if(isOpen){
                    mAddFile.setVisibility(View.GONE);
                    isOpen = false;
                }else{
                    mAddFile.setVisibility(View.VISIBLE);
                    isOpen = true;
                }


                break;
            case R.id.btn_send:
                //发送消息
                String msgg = mEtMsg.getText().toString();
                mChatPresenter.sendMessage(mUsername,msgg);
                mEtMsg.getText().clear();

                break;
            default:
                break;
        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().length()==0){
            mBtnSend.setEnabled(false);
            mBtnSend.setVisibility(View.GONE);
            mAdd.setVisibility(View.VISIBLE);

        }else{
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
        mChatAdapter = new ChatAdapter(emMessageList);
        mRecyclerView.setAdapter(mChatAdapter);
        if (emMessageList.size()!=0){
            mRecyclerView.smoothScrollToPosition(emMessageList.size()-1);
        }
    }

    @Override
    public void onUpdate(int size) {
        mChatAdapter.notifyDataSetChanged();
        if (size!=0){
            mRecyclerView.smoothScrollToPosition(size-1);
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        if(null != this.getCurrentFocus()){
            /**
             * 点击空白位置 隐藏软键盘
             */
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
            //隐藏文件部分
            //mAddFile.setVisibility(View.VISIBLE);
        }
        return super .onTouchEvent(event);
    }

}
