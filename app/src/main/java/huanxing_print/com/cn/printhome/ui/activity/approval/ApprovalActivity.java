package huanxing_print.com.cn.printhome.ui.activity.approval;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.util.CommonUtils;

/**
 * Created by Administrator on 2017/5/4 0004.
 */

public class ApprovalActivity extends BaseActivity {
    private ImageView iv_back;
    private LinearLayout ll_back;
    private TextView tv_approval;
    private LinearLayout ll_approval;
    private TextView tv_approval_no;
    private LinearLayout ll_approval_no;
    private View view_approval;
    private LinearLayout ll_container;
    private int llWidth;
    private int tvWidth;
    private Context ctx;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.activity_approval);
        ctx = this;
        initView();
        initData();
        initListener();
    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        tv_approval = (TextView) findViewById(R.id.tv_approval);
        ll_approval = (LinearLayout) findViewById(R.id.ll_approval);
        tv_approval_no = (TextView) findViewById(R.id.tv_approval_no);
        ll_approval_no = (LinearLayout) findViewById(R.id.ll_approval_no);
        view_approval = findViewById(R.id.view_approval);
        ll_container = (LinearLayout) findViewById(R.id.ll_container);
    }


    ViewTreeObserver.OnGlobalLayoutListener observer = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            llWidth = ll_approval.getWidth();
            tvWidth = tv_approval.getWidth();
            setViewWidth(tvWidth);
        }
    };

    private void setViewWidth(int width) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, CommonUtils.dip2px(ctx, 3));
        params.leftMargin = (llWidth - width) / 2;
        view_approval.setLayoutParams(params);
    }

    private void initData() {
    }

    private void initListener() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_approval.getViewTreeObserver().addOnGlobalLayoutListener(observer);
    }
}
