package huanxing_print.com.cn.printhome.ui.activity.my;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.model.my.FeedBackBean;
import huanxing_print.com.cn.printhome.net.callback.my.FeedBackCallBack;
import huanxing_print.com.cn.printhome.net.request.my.FeedBackRequest;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;

/**
 * Created by Administrator on 2017/3/20 0020.
 */

public class SuggestActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout ll_back;
    private EditText et_my_feedBaxk;
    private Button btn_submit_feedback;
    private String feedBack;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBarGreen(this);
        setContentView(R.layout.activity_user_suggest);
        initView();
        setListener();
    }

    private void initView() {
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        et_my_feedBaxk = (EditText) findViewById(R.id.et_my_feedBaxk);
        btn_submit_feedback = (Button) findViewById(R.id.btn_submit_feedback);
    }
    private void setListener() {
        btn_submit_feedback.setOnClickListener(this);
        ll_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit_feedback://提交反馈
                feedBack = et_my_feedBaxk.getText().toString().trim();
                if (ObjectUtils.isNull(feedBack)) {
                    ToastUtil.doToast(getSelfActivity(),"请填写反馈内容");
                    return;
                }

                sendFeedBack();
                break;
            case R.id.ll_back:
               finishCurrentActivity();
                break;
        }
    }

    /**
     * 提交反馈
     */
    private void sendFeedBack() {

        DialogUtils.showProgressDialog(getSelfActivity(),"提交中");
        FeedBackRequest.sendFeedBack(getSelfActivity(), feedBack, new FeedBackCallBack() {
            @Override
            public void success(String msg, FeedBackBean bean) {
                DialogUtils.closeProgressDialog();
                toast("反馈成功");
            }

            @Override
            public void fail(String msg) {

            }

            @Override
            public void connectFail() {

            }
        });
    }
}
