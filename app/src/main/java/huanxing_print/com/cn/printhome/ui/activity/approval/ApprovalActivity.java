package huanxing_print.com.cn.printhome.ui.activity.approval;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.ui.activity.fragment.ApprovalNoFragment;
import huanxing_print.com.cn.printhome.ui.activity.fragment.fragapproval.ApprovalFragment;
import huanxing_print.com.cn.printhome.ui.adapter.ViewPagerAdapter;
import huanxing_print.com.cn.printhome.util.CommonUtils;

/**
 * Created by Administrator on 2017/5/4 0004.
 */

public class ApprovalActivity extends FragmentActivity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_approval;
    private TextView tv_approval_no;
    private View view_approval;
    private ViewPager vp_approval;
    private int llWidth;
    private int tvWidth;
    private Context ctx;
    private List<Fragment> fragments;
    private ViewPagerAdapter adapter;
    private int approverNum;
    private final String BROADCAST_ACTION_APPROVALNUM_REFRESH= "approvalnum.refresh";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval);
        CommonUtils.initSystemBar(this);
        ctx = this;
        //EventBus.getDefault().register(this);
        // 注册广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BROADCAST_ACTION_APPROVALNUM_REFRESH);
        registerReceiver(mBroadcastReceiver, intentFilter);

        initView();
        initMeasureSpec();
        initData();
        initListener();
    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_approval = (TextView) findViewById(R.id.tv_approval);
        tv_approval_no = (TextView) findViewById(R.id.tv_approval_no);
        view_approval = findViewById(R.id.view_approval);
        vp_approval = (ViewPager) findViewById(R.id.vp_approval);
    }

    private void initData() {
        approverNum = getIntent().getIntExtra("approverNum", 0);
        fragments = new ArrayList<>();
        ApprovalFragment approvalFragment = new ApprovalFragment();
        ApprovalNoFragment approvalnoFragment = new ApprovalNoFragment();
        fragments.add(approvalFragment);
        fragments.add(approvalnoFragment);

        if (approverNum>0){
            tv_approval.setText("待审批("+approverNum+")");
        }else{
            tv_approval.setText("待审批");
        }
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
        vp_approval.setAdapter(adapter);
    }

    private void initListener() {
        iv_back.setOnClickListener(this);
        tv_approval.setOnClickListener(this);
        tv_approval_no.setOnClickListener(this);

        vp_approval.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(tvWidth, CommonUtils.dip2px(ctx, 3));
                if (lp != null && positionOffset != 0) {
                    lp.leftMargin = (int) ((position + positionOffset) * llWidth) + (llWidth - tvWidth) / 2;
                    view_approval.setLayoutParams(lp);
                }
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    tv_approval.setTextColor(getResources().getColor(R.color.blue));
                    tv_approval_no.setTextColor(getResources().getColor(R.color.black2));
                } else {
                    tv_approval_no.setTextColor(getResources().getColor(R.color.blue));
                    tv_approval.setTextColor(getResources().getColor(R.color.black2));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initMeasureSpec() {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        tv_approval.measure(w, h);
        tvWidth = tv_approval.getMeasuredWidth();

        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        llWidth = screenWidth / 2;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(tvWidth, CommonUtils.dip2px(ctx, 2));
        lp.leftMargin = (llWidth - tvWidth) / 2;
        Log.e("wanghao","leftMargin ---- " + lp.leftMargin);
        view_approval.setLayoutParams(lp);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_approval:
                vp_approval.setCurrentItem(0);
                break;
            case R.id.tv_approval_no:
                vp_approval.setCurrentItem(1);
                break;
        }
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BROADCAST_ACTION_APPROVALNUM_REFRESH)) {
                approverNum--;

                if (approverNum>0){
                    tv_approval.setText("待审批("+approverNum+")");
                }else{
                    tv_approval.setText("待审批");
                }
            }
        }
    };
    @Override
    public void onDestroy() {
        super.onDestroy();
        //EventBus.getDefault().unregister(this);
        unregisterReceiver(mBroadcastReceiver);
    }
}
