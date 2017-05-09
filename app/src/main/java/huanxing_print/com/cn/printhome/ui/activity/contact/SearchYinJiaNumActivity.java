package huanxing_print.com.cn.printhome.ui.activity.contact;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.constant.ConFig;
import huanxing_print.com.cn.printhome.model.contact.FriendSearchInfo;
import huanxing_print.com.cn.printhome.net.callback.contact.FriendSearchCallback;
import huanxing_print.com.cn.printhome.net.request.contact.FriendManagerRequest;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.SharedPreferencesUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;

/**
 * Created by wanghao on 2017/5/3.
 */

public class SearchYinJiaNumActivity extends BaseActivity implements View.OnClickListener,TextWatcher{
    private EditText searchEt;
    private View show_search_content;
    private TextView hint_content;
    private View del_icon;
    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.activity_search_yinjia_num);

        initView();
        setListener();
    }

    private void initView() {
        searchEt = (EditText) findViewById(R.id.search_et);
        show_search_content = findViewById(R.id.show_search_content);
        hint_content = (TextView) findViewById(R.id.hint_content);
        del_icon = findViewById(R.id.del_content);
    }

    private void setListener() {
        findViewById(R.id.exit_search).setOnClickListener(this);
        show_search_content.setOnClickListener(this);
        searchEt.addTextChangedListener(this);
        del_icon.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.exit_search:
                finishCurrentActivity();
                break;
            case R.id.show_search_content:
                String token = SharedPreferencesUtils.getShareString(this, ConFig.SHAREDPREFERENCES_NAME,
                        "loginToken");
                DialogUtils.showProgressDialog(this, "加载中");
                FriendManagerRequest.friendSearch(this, token, searchEt.getText().toString(), friendSearchCallback);
                break;
            case R.id.del_content:
                searchEt.setText(null);
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if(charSequence.length() > 0) {
            show_search_content.setVisibility(View.VISIBLE);
            hint_content.setText(charSequence);
            del_icon.setVisibility(View.VISIBLE);
        }else{
            show_search_content.setVisibility(View.GONE);
            hint_content.setText(null);
            del_icon.setVisibility(View.GONE);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }

    FriendSearchCallback friendSearchCallback = new FriendSearchCallback() {
        @Override
        public void success(String msg, FriendSearchInfo friendSearchInfo) {
            DialogUtils.closeProgressDialog();
            if(null != friendSearchInfo) {
                ArrayList<FriendSearchInfo> infos = new ArrayList<FriendSearchInfo>();
//                infos.add(friendSearchInfo);
                infos.add(data());
                startActivity(infos);
            }
        }

        @Override
        public void fail(String msg) {
            DialogUtils.closeProgressDialog();
            ToastUtil.doToast(SearchYinJiaNumActivity.this, msg + " -- 假数据");
            //真实测试完，需要删除
            ArrayList<FriendSearchInfo> infos = new ArrayList<FriendSearchInfo>();
            infos.add(data());
            startActivity(infos);
        }

        @Override
        public void connectFail() {
            DialogUtils.closeProgressDialog();
            ToastUtil.doToast(SearchYinJiaNumActivity.this, "connectFail -- 假数据");
            //真实测试完，需要删除
            ArrayList<FriendSearchInfo> infos = new ArrayList<FriendSearchInfo>();
            infos.add(data());
            startActivity(infos);
        }
    };

    private void startActivity(ArrayList<FriendSearchInfo> infos) {
        Intent intent = new Intent(SearchYinJiaNumActivity.this, SearchAddResultActivity.class);
        intent.putParcelableArrayListExtra("search result", infos);
        startActivity(intent);
    }

    /**
     * 假数据
     */
    private FriendSearchInfo data() {
        FriendSearchInfo info = new FriendSearchInfo();
        info.setNickName("陆成宋");
        info.setMemberName("陆成宋");
        info.setUniqueId("1867989");
        info.setMemberId("123456");
        info.setMemberUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494660151&di=fc28cd4cd681bb1d70df6ff6654791ff&imgtype=jpg&er=1&src=http%3A%2F%2Fimgsrc.baidu.com%2Fforum%2Fw%253D580%2Fsign%3D8c03c118ca8065387beaa41ba7dda115%2Fc17fc0bf6c81800a06c8cd58b13533fa828b4759.jpg");
        return info;
    }
}
