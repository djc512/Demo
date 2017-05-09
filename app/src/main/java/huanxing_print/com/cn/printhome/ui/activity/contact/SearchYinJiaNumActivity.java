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
                if(searchEt.getText().length() >= 6 && searchEt.getText().length() <= 14) {
                    String token = SharedPreferencesUtils.getShareString(this, ConFig.SHAREDPREFERENCES_NAME,
                            "loginToken");
                    DialogUtils.showProgressDialog(this, "加载中").show();
                    FriendManagerRequest.friendSearch(this, token, searchEt.getText().toString(), friendSearchCallback);
                }else {
                    ToastUtil.doToast(this, "内容不能小于6");
                }
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
                infos.add(friendSearchInfo);
                startActivity(infos);
            }
        }

        @Override
        public void fail(String msg) {
            DialogUtils.closeProgressDialog();
            ToastUtil.doToast(SearchYinJiaNumActivity.this, msg + " -- 假数据");
        }

        @Override
        public void connectFail() {
            DialogUtils.closeProgressDialog();
            ToastUtil.doToast(SearchYinJiaNumActivity.this, "connectFail -- 假数据");
        }
    };

    private void startActivity(ArrayList<FriendSearchInfo> infos) {
        Intent intent = new Intent(SearchYinJiaNumActivity.this, SearchAddResultActivity.class);
        intent.putParcelableArrayListExtra("search result", infos);
        startActivity(intent);
    }
}
