package huanxing_print.com.cn.printhome.ui.activity.my;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.ui.activity.fragment.fragcommunity.CommunityCollecteFragment;
import huanxing_print.com.cn.printhome.ui.activity.fragment.fragcommunity.CommunityNewestFragment;
import huanxing_print.com.cn.printhome.ui.adapter.ViewPagerAdapter;
import huanxing_print.com.cn.printhome.util.CommonUtils;

/**
 * Created by Administrator on 2017/5/4 0004.
 */

public class CommunityListActivity extends FragmentActivity implements View.OnClickListener {
    private TextView tv_newest;
    private LinearLayout ll_newest;
    private TextView tv_collection;
    private LinearLayout ll_collection;

    private View view_line;
    private Context ctx;
    private ViewPager vp_comment;
    private List<Fragment> fragments;
    private int tvWidth;
    private int llWidth;
    private List<TextView> textViews;
    private LinearLayout.LayoutParams lp;
    private int marginLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);
        CommonUtils.initSystemBar(this);
        ctx = this;
        initView();
        initData();
        initListener();
    }

    private void initView() {
        tv_newest = (TextView) findViewById(R.id.tv_newest);
        ll_newest = (LinearLayout) findViewById(R.id.ll_newest);
        tv_collection = (TextView) findViewById(R.id.tv_collection);
        ll_collection = (LinearLayout) findViewById(R.id.ll_collection);

        view_line = findViewById(R.id.view_line);
        vp_comment = (ViewPager) findViewById(R.id.vp_comment);
    }


    private void initData() {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        tv_newest .measure(w, h);
        tvWidth = tv_newest.getMeasuredWidth();

        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        llWidth = screenWidth / 2;
        lp = new LinearLayout.LayoutParams(tvWidth, CommonUtils.dip2px(ctx, 2));
        marginLeft = (llWidth - tvWidth) / 2;

        fragments = new ArrayList<>();
        CommunityNewestFragment newestFragment = new CommunityNewestFragment();
        CommunityCollecteFragment collecteFragment = new CommunityCollecteFragment();

        fragments.add(newestFragment);
        fragments.add(collecteFragment);

        textViews = new ArrayList<>();
        textViews.add(tv_newest);
        textViews.add(tv_collection);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
        vp_comment.setAdapter(adapter);
    }

    private void initListener() {
        findViewById(R.id.iv_back).setOnClickListener(this);
        ll_newest.setOnClickListener(this);
        ll_collection.setOnClickListener(this);

        vp_comment.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
                        setTextView(0);
                        break;
                    case 1:
                        setTextView(1);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        lp.leftMargin = marginLeft;
        view_line.setLayoutParams(lp);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_newest:
                setTextView(0);
                vp_comment.setCurrentItem(0);
                break;
            case R.id.ll_collection:
                setTextView(1);
                vp_comment.setCurrentItem(1);
                break;
            default:
                break;
        }
    }

    /**
     * 设置textView的状态
     */
    private void setTextView(int index) {
        for (int i = 0; i < textViews.size(); i++) {
            if (index == i) {
                textViews.get(index).setTextColor(getResources().getColor(R.color.black2));
            } else {
                textViews.get(i).setTextColor(getResources().getColor(R.color.gray8));
            }
        }
    }
}
