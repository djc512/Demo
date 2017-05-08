package huanxing_print.com.cn.printhome.ui.activity.my;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.ui.activity.fragment.ReceiptNormalFragment;
import huanxing_print.com.cn.printhome.ui.activity.fragment.ReceiptValueFragment;
import huanxing_print.com.cn.printhome.ui.adapter.ViewPagerAdapter;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.view.viewpager.MyViewPager;

/**
 * Created by Administrator on 2017/5/8 0008.
 */

public class ReceiptActivity extends FragmentActivity implements View.OnClickListener {

    private LinearLayout ll_back;
    private TextView tv_bill_normal;
    private TextView tv_bill_value;
    private View view_bill;
    private MyViewPager vp;

    private List<Fragment> fragmentList = new ArrayList<>();
    private Context ctx;
    private ImageView iv_back;
    private LinearLayout ll_all;
    private LinearLayout ll_bad;
    private View view_line;
    private ViewPager vp_comment;
    private TextView tv_all;
    private int tvWidth;
    private int llWidth;
    private LinearLayout.LayoutParams lp;
    private int marginLeft;
    private List<Fragment> fragments;
    private List<TextView> textViews;
    private TextView tv_bad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.activity_debit1);
        ctx = this;
        initView();
        initData();
        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();

        lp.leftMargin = marginLeft;
        view_line.setLayoutParams(lp);
    }
    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_all = (TextView) findViewById(R.id.tv_all);
        tv_bad = (TextView) findViewById(R.id.tv_bad);

        ll_all = (LinearLayout) findViewById(R.id.ll_all);
        ll_bad = (LinearLayout) findViewById(R.id.ll_bad);

        view_line = findViewById(R.id.view_line);
        vp_comment = (ViewPager) findViewById(R.id.vp_comment);
    }

    private void initData() {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        tv_all.measure(w, h);
        tvWidth = tv_all.getMeasuredWidth();

        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        llWidth = screenWidth / 2;
        lp = new LinearLayout.LayoutParams(tvWidth, CommonUtils.dip2px(ctx, 2));
        marginLeft = (llWidth - tvWidth) / 2;

        fragments = new ArrayList<>();
        fragments.add(new ReceiptNormalFragment());
        fragments.add(new ReceiptValueFragment());

        textViews = new ArrayList<>();
        textViews.add(tv_all);
        textViews.add(tv_bad);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
        vp_comment.setAdapter(adapter);
    }

    private void setListener() {
        iv_back.setOnClickListener(this);
        ll_all.setOnClickListener(this);
        ll_bad.setOnClickListener(this);

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
                    case 2:
                        setTextView(2);
                        break;
                    case 3:
                        setTextView(3);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_file:
                vp_comment.setCurrentItem(0);
                break;
            case R.id.ll_bad:
                vp_comment.setCurrentItem(1);
                break;
            case R.id.iv_back:
                finish();
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
