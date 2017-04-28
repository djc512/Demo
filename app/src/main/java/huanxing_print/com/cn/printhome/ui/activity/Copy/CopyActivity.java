package huanxing_print.com.cn.printhome.ui.activity.Copy;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.ActivityHelper;
import huanxing_print.com.cn.printhome.ui.activity.fragment.FileFragment;
import huanxing_print.com.cn.printhome.ui.activity.fragment.HuKouFragment;
import huanxing_print.com.cn.printhome.ui.activity.fragment.IDFragment;
import huanxing_print.com.cn.printhome.ui.activity.fragment.PassportFragment;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.StepViewUtil;
import huanxing_print.com.cn.printhome.view.StepLineView;

/**
 * Created by Administrator on 2017/4/28 0028.
 */

public class CopyActivity extends FragmentActivity implements View.OnClickListener {
    private LinearLayout ll_back;
    private TextView tv_file;
    private View view_line;
    private LinearLayout ll_file;
    private TextView tv_id;
    private LinearLayout ll_id;
    private TextView tv_hukou;
    private LinearLayout ll_hukou;
    private TextView tv_passport;
    private LinearLayout ll_passport;
    private int llWidth;
    private int tvWidth;
    private List<TextView> tvList = new ArrayList<>();
    private List<Fragment> fragments;
    private Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_copy);
        ctx = this;
        CommonUtils.initSystemBarGreen(this);
        StepViewUtil.init(ctx, findViewById(R.id.step), StepLineView.STEP_PICK_FILE);
        initView();
        initData();
        initListener();
    }

    ViewTreeObserver.OnGlobalLayoutListener observer = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            llWidth = ll_file.getWidth();
            tvWidth = tv_file.getWidth();
            setViewWidth(tvWidth);
        }
    };

    /**
     * 动态设置指示线的宽度
     */
    private void setViewWidth(int width) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, CommonUtils.dip2px(ctx, 3));
        params.topMargin = CommonUtils.dip2px(ctx, 10);
        params.leftMargin = CommonUtils.dip2px(ctx, 25) + (llWidth - width) / 2;
        view_line.setLayoutParams(params);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_file.getViewTreeObserver().addOnGlobalLayoutListener(observer);
    }

    private void initView() {
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        tv_file = (TextView) findViewById(R.id.tv_file);
        view_line = findViewById(R.id.view_line);
        ll_file = (LinearLayout) findViewById(R.id.ll_file);
        tv_id = (TextView) findViewById(R.id.tv_id);
        ll_id = (LinearLayout) findViewById(R.id.ll_id);
        tv_hukou = (TextView) findViewById(R.id.tv_hukou);
        ll_hukou = (LinearLayout) findViewById(R.id.ll_hukou);
        tv_passport = (TextView) findViewById(R.id.tv_passport);
        ll_passport = (LinearLayout) findViewById(R.id.ll_passport);

        tvList.add(tv_file);
        tvList.add(tv_id);
        tvList.add(tv_hukou);
        tvList.add(tv_passport);
    }

    private void initData() {
        fragments = new ArrayList<>();
        fragments.add(new FileFragment());
        fragments.add(new IDFragment());
        fragments.add(new HuKouFragment());
        fragments.add(new PassportFragment());
    }

    private void initListener() {
        ll_back.setOnClickListener(this);
        ll_file.setOnClickListener(this);
        ll_id.setOnClickListener(this);
        ll_hukou.setOnClickListener(this);
        ll_passport.setOnClickListener(this);

        ll_file.performClick();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_file:
                index = 0;
                FragmentTransaction transactionfile = getSupportFragmentManager().beginTransaction();
                transactionfile.replace(R.id.ll_container, fragments.get(index));
                transactionfile.commit();
                break;
            case R.id.ll_id:
                index = 1;
                FragmentTransaction transactionid = getSupportFragmentManager().beginTransaction();
                transactionid.replace(R.id.ll_container, fragments.get(index));
                transactionid.commit();
                break;
            case R.id.ll_hukou:
                index = 2;
                FragmentTransaction transactionhukou = getSupportFragmentManager().beginTransaction();
                transactionhukou.replace(R.id.ll_container, fragments.get(index));
                transactionhukou.commit();
                break;
            case R.id.ll_passport:
                index = 3;
                FragmentTransaction transactionport = getSupportFragmentManager().beginTransaction();
                transactionport.replace(R.id.ll_container, fragments.get(index));
                transactionport.commit();
                break;
            case R.id.ll_back:
                ActivityHelper.getInstance().finishCurrentActivity();
                break;
        }
        setTextState(index);
        startAnim(index);
    }

    /**
     * 改变text的字体
     */
    private void setTextState(int index) {
        for (int i = 0; i < tvList.size(); i++) {
            if (index == i) {
                tvList.get(index).setTextColor(getResources().getColor(R.color.black1));
            } else {
                tvList.get(i).setTextColor(getResources().getColor(R.color.gray6));
            }
        }
    }

    private int index;
    private int start;

    /**
     * 指示线做动画
     *
     * @param index
     */
    private void startAnim(int index) {
        int end = llWidth * index;
        TranslateAnimation ta = new TranslateAnimation(start, llWidth * index, 0, 0);
        ta.setDuration(500);
        ta.setFillAfter(true);
        view_line.startAnimation(ta);
        start = end;
    }
}
