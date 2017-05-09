package huanxing_print.com.cn.printhome.ui.activity.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hyphenate.chat.EMMessage;

import org.simple.eventbus.EventBus;

import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.presenter.ChatPresenter;
import huanxing_print.com.cn.printhome.presenter.impl.ChatPresenterImpl;
import huanxing_print.com.cn.printhome.ui.adapter.ChatAdapter;
import huanxing_print.com.cn.printhome.util.CommonUtils;

import static huanxing_print.com.cn.printhome.util.ShowUtil.showToast;

//import huanxing_print.com.cn.printhome.ui.adapter.ChatAdapter;

/**
 * Created by htj on 2017/5/8.
 */

public class ChatActivity extends BaseActivity implements TextWatcher,ChatView{

    private ChatPresenter mChatPresenter;
    private String mUsername;
    //private ChatAdapter mChatAdapter;
    private Context ctx;

    private ChatAdapter mChatAdapter;

    TextView mTvTitle;
    //标题栏没写

    //TextView mToolBar;
    RecyclerView mRecyclerView;
    EditText mEtMsg;
    Button mBtnSend;

    @Override
    protected BaseActivity getSelfActivity() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 改变状态栏的颜色使其与APP风格一体化
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.activity_chat);
        ctx = this;

        initVIew();

        Intent intent = getIntent();
        //聊天对象
        mUsername = intent.getStringExtra("username");
        //测试，
        mUsername = "齐天大圣";
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

    }

    //@Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EMMessage message){
        //当收到信消息的时候
        /*
         *  判断当前这个消息是不是正在聊天的用户给我发的
         *  如果是，让ChatPresenter 更新数据
         *
         */
        String from = message.getFrom();
        if (from.equals(mUsername)){
            mChatPresenter.updateData(mUsername);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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

    //@OnClick(R.id.btn_send)
    public void onClick() {
        String msg = mEtMsg.getText().toString();
        mChatPresenter.sendMessage(mUsername,msg);
        mEtMsg.getText().clear();
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
        }else{
            mBtnSend.setEnabled(true);
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
            mRecyclerView.scrollToPosition(emMessageList.size()-1);
        }
    }

    @Override
    public void onUpdate(int size) {
        mChatAdapter.notifyDataSetChanged();
        if (size!=0){
            mRecyclerView.smoothScrollToPosition(size-1);
        }
    }
}