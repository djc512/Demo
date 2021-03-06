package huanxing_print.com.cn.printhome.ui.activity.contact;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.ArrayList;

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
import huanxing_print.com.cn.printhome.util.UrlUtil;
import huanxing_print.com.cn.printhome.view.dialog.LoadingDialog;

/**
 * Created by wanghao on 2017/5/3.
 */

public class AddContactActivity extends BaseActivity implements View.OnClickListener {
    private static final int SCANQR = 1000;
    private LinearLayout ll_back;
    private LoadingDialog loadingDialog;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        CommonUtils.initSystemBar(this);
        initView();
        setListener();
    }

    private void initView() {
        loadingDialog = new LoadingDialog(this);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
    }

    private void setListener() {
        ll_back.setOnClickListener(this);
        findViewById(R.id.input_yinJia_number_space).setOnClickListener(this);
        findViewById(R.id.add_by_addressbook).setOnClickListener(this);
        findViewById(R.id.add_by_qr).setOnClickListener(this);
        findViewById(R.id.my_qr).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finishCurrentActivity();
                break;
            case R.id.input_yinJia_number_space:
                startActivity(SearchYinJiaNumActivity.class);
                break;
            case R.id.add_by_addressbook:
                startActivity(AddByAddressBookActivity.class);
                break;
            case R.id.add_by_qr:
                startActivityForResult(new Intent(this, CaptureActivity.class), SCANQR);
                break;
            case R.id.my_qr:
                startActivity(MyQRCodeActivity.class);
                break;
        }
    }

    private void startActivity(Class cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANQR:
                if(resultCode == RESULT_OK) {
                    String resultString = data.getStringExtra(CodeUtils.RESULT_STRING);
                    String id = UrlUtil.getValueByName(resultString, "id");
                    if (id != null) {
                        search(id);
                    }
//                    if(resultString.startsWith("MINE:")) {
//                        String subResultString = resultString.replace("MINE:","");
//                        search(subResultString);
//                    }
                }
                break;
        }
    }

    private void search(String searchContent) {
        String token = SharedPreferencesUtils.getShareString(this, ConFig.SHAREDPREFERENCES_NAME,
                "loginToken");
//        DialogUtils.showProgressDialog(this, "加载中").show();
        loadingDialog.show();
        FriendManagerRequest.friendSearch(this, token, searchContent, friendSearchCallback);
    }

    FriendSearchCallback friendSearchCallback = new FriendSearchCallback() {
        @Override
        public void success(String msg, FriendSearchInfo friendSearchInfo) {
//            DialogUtils.closeProgressDialog();
            loadingDialog.dismiss();
            if (null != friendSearchInfo) {
                if (1 == friendSearchInfo.getIsFriend()) {
                    Intent intent = new Intent(AddContactActivity.this, ChatTestActivity.class);
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
            ToastUtil.doToast(AddContactActivity.this, msg);
        }

        @Override
        public void connectFail() {
//            DialogUtils.closeProgressDialog();
            loadingDialog.dismiss();
            toastConnectFail();
        }
    };

    private void startActivity(ArrayList<FriendSearchInfo> infos) {
        Intent intent = new Intent(AddContactActivity.this, SearchAddResultActivity.class);
        intent.putParcelableArrayListExtra("search result", infos);
        startActivity(intent);
    }
}
