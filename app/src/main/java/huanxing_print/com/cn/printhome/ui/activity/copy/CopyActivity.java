package huanxing_print.com.cn.printhome.ui.activity.copy;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.event.print.PrintTypeEvent;
import huanxing_print.com.cn.printhome.ui.activity.fragment.fragcopy.FileFragment;
import huanxing_print.com.cn.printhome.ui.activity.fragment.fragcopy.HuKouFragment;
import huanxing_print.com.cn.printhome.ui.activity.fragment.fragcopy.IDFragment;
import huanxing_print.com.cn.printhome.ui.activity.fragment.fragcopy.PassportFragment;
import huanxing_print.com.cn.printhome.ui.adapter.ViewPagerAdapter;
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
    private ViewPager vp_copy;
    private int marginLeft;
    private LinearLayout.LayoutParams lp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_copy);
        CommonUtils.initSystemBar(this);
        ctx = this;
        initStepView();
        initView();
        initData();
        initListener();
    }

    private void initStepView() {
        StepViewUtil.init(ctx, findViewById(R.id.step), StepLineView.STEP_PICK_FILE);
        TextView pickFileTv = (TextView) findViewById(R.id.pickFileTv);
        pickFileTv.setText("采集制作");
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("复印");
        MobclickAgent.onResume(this);
        lp.leftMargin = marginLeft;
        view_line.setLayoutParams(lp);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("复印");
        MobclickAgent.onPause(this);
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
        vp_copy = (ViewPager) findViewById(R.id.vp_copy);

        tvList.add(tv_file);
        tvList.add(tv_id);
        tvList.add(tv_hukou);
        tvList.add(tv_passport);
    }

    private void initData() {
        EventBus.getDefault().postSticky(new PrintTypeEvent(PrintTypeEvent.TYPE_COPY));
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        tv_file.measure(w, h);
        tvWidth = tv_file.getMeasuredWidth();

        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        llWidth = screenWidth / 4;

        lp = new LinearLayout.LayoutParams(tvWidth, CommonUtils.dip2px(ctx, 2));
        marginLeft = (llWidth - tvWidth) / 2;

        fragments = new ArrayList<>();
        fragments.add(new FileFragment());
        fragments.add(new IDFragment());
        fragments.add(new HuKouFragment());
        fragments.add(new PassportFragment());

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
        vp_copy.setAdapter(adapter);
    }

    private void initListener() {
        ll_back.setOnClickListener(this);
        ll_file.setOnClickListener(this);
        ll_id.setOnClickListener(this);
        ll_hukou.setOnClickListener(this);
        ll_passport.setOnClickListener(this);

        vp_copy.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(tvWidth, CommonUtils.dip2px(ctx, 3));
                if (lp != null && positionOffset != 0) {
                    marginLeft = (int) ((position + positionOffset) * llWidth) + (llWidth - tvWidth) / 2;
                    lp.leftMargin = marginLeft;
                    view_line.setLayoutParams(lp);
                }
            }

            @Override
            public void onPageSelected(int position) {

                switch (position) {
                    case 0:
                        setTextState(0);
                        MobclickAgent.onEvent(CopyActivity.this,"File_Type");
                        break;
                    case 1:
                        setTextState(1);
                        MobclickAgent.onEvent(CopyActivity.this,"ID_Card_Type");
                        break;
                    case 2:
                        setTextState(2);
                        MobclickAgent.onEvent(CopyActivity.this,"Residence_booklet_Type");
                        break;
                    case 3:
                        setTextState(3);
                        MobclickAgent.onEvent(CopyActivity.this,"Passport_Type");
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private int index;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_file:
                index = 0;
                vp_copy.setCurrentItem(index);
                break;
            case R.id.ll_id:
                index = 1;
                vp_copy.setCurrentItem(index);
                break;
            case R.id.ll_hukou:
                index = 2;
                vp_copy.setCurrentItem(index);
                break;
            case R.id.ll_passport:
                index = 3;
                vp_copy.setCurrentItem(index);
                break;
            case R.id.ll_back:
                finish();
                break;
        }
        setTextState(index);
    }

    /**
     * 改变text的字体
     */
    private void setTextState(int index) {
        for (int i = 0; i < tvList.size(); i++) {
            if (index == i) {
                tvList.get(index).setTextColor(getResources().getColor(R.color.black1));
            } else {
                tvList.get(i).setTextColor(getResources().getColor(R.color.gray8));
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != fragments && fragments.size() > 0) {
            fragments.clear();
        }
        if (null != tvList && tvList.size() > 0) {
            tvList.clear();
        }
        System.gc();
    }
}
