package huanxing_print.com.cn.printhome.ui.activity.contact;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.constant.ConFig;
import huanxing_print.com.cn.printhome.model.contact.FriendSearchInfo;
import huanxing_print.com.cn.printhome.net.callback.contact.FriendSearchCallback;
import huanxing_print.com.cn.printhome.net.request.contact.FriendManagerRequest;
import huanxing_print.com.cn.printhome.ui.activity.chat.ChatTestActivity;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.SharedPreferencesUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;
import huanxing_print.com.cn.printhome.view.dialog.LoadingDialog;

/**
 * Created by wanghao on 2017/5/3.
 */

public class SearchYinJiaNumActivity extends BaseActivity implements View.OnClickListener, TextWatcher {
    private EditText searchEt;
    private View show_search_content;
    private TextView hint_content;
    private View del_icon;
    private LoadingDialog loadingDialog;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_yinjia_num);
        CommonUtils.initSystemBar(this);
        initView();
        setListener();
    }

    private void initView() {
        loadingDialog = new LoadingDialog(this);
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
        switch (view.getId()) {
            case R.id.exit_search:
                finishCurrentActivity();
                break;
            case R.id.show_search_content:

                String searchStr = searchEt.getText().toString();
                if (baseApplication.getPhone().equals(searchStr) || baseApplication.getUniqueId().equals(searchStr)) {
                    ToastUtil.doToast(SearchYinJiaNumActivity.this, "不能添加自己为联系人");
                    return;
                }
                if (searchStr.length() >= 6 && searchStr.length() <= 14 && (isStartLetter(searchStr) || isStartNum(searchStr))) {
                    String token = SharedPreferencesUtils.getShareString(this, ConFig.SHAREDPREFERENCES_NAME,
                            "loginToken");
//                    DialogUtils.showProgressDialog(this, "加载中").show();
                    loadingDialog.show();
                    FriendManagerRequest.friendSearch(this, token, searchStr, friendSearchCallback);
                } else {
                    ToastUtil.doToast(this, "内容不能小于6,以数字字母开头");
                }
                break;
            case R.id.del_content:
                searchEt.setText(null);
                break;
        }
    }

    private boolean isStartNum(String str) {
        Pattern pattern = Pattern.compile("^[0-9]*");
        Matcher isNum = pattern.matcher(str.charAt(0) + "");
        if (isNum.matches())
            return true;
        return false;
    }

    private boolean isStartLetter(String str) {
        Pattern pattern = Pattern.compile("^[A-Za-z]*");
        Matcher isNum = pattern.matcher(str.charAt(0) + "");
        if (isNum.matches())
            return true;
        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence.length() > 0) {
            show_search_content.setVisibility(View.VISIBLE);
            hint_content.setText(charSequence);
            del_icon.setVisibility(View.VISIBLE);
        } else {
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
//            DialogUtils.closeProgressDialog();
            loadingDialog.dismiss();
            if (null != friendSearchInfo) {
                if (1 == friendSearchInfo.getIsFriend()) {
                    Intent intent = new Intent(SearchYinJiaNumActivity.this, ChatTestActivity.class);
                    intent.putExtra("FriendSearchInfo", friendSearchInfo);
                    startActivity(intent);
                } else {
                    ArrayList<FriendSearchInfo> infos = new ArrayList<FriendSearchInfo>();
                    infos.add(friendSearchInfo);
                    startActivity(infos);
                }
            }
        }

        @Override
        public void fail(String msg) {
//            DialogUtils.closeProgressDialog();
            loadingDialog.dismiss();
            ToastUtil.doToast(SearchYinJiaNumActivity.this, msg);
        }

        @Override
        public void connectFail() {
//            DialogUtils.closeProgressDialog();
            loadingDialog.dismiss();
            toastConnectFail();
        }
    };

    private void startActivity(ArrayList<FriendSearchInfo> infos) {
        Intent intent = new Intent(SearchYinJiaNumActivity.this, SearchAddResultActivity.class);
        intent.putParcelableArrayListExtra("search result", infos);
        startActivity(intent);
    }
}
