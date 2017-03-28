package huanxing_print.com.cn.printhome.ui.activity.my;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.ui.activity.fragment.DebitNormalFragment;
import huanxing_print.com.cn.printhome.ui.activity.fragment.DebitValueFragment;
import huanxing_print.com.cn.printhome.ui.adapter.ViewPagerAdapter;
import huanxing_print.com.cn.printhome.util.CommonUtils;

/**
 * Created by Administrator on 2017/3/24 0024.
 */

public class BillDebitActivity extends FragmentActivity implements View.OnClickListener {

    private LinearLayout ll_back;
    private TextView tv_bill_normal;
    private TextView tv_bill_value;
    private View view_bill;
    private ViewPager vp;
    private int width;

    private List<Fragment> fragmentList = new ArrayList<>();
    private String billValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBarGreen(this);
        setContentView(R.layout.activity_debit);
        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth()/2;
        initView();
        initData();
        setListener();
    }

    private void initData() {
        billValue = getIntent().getStringExtra("billValue");

        fragmentList.clear();

        DebitNormalFragment debitNormalFragment = new DebitNormalFragment();
        Bundle norBundle = new Bundle();
        norBundle.putString("billValue",billValue);
        debitNormalFragment.setArguments(norBundle);

        DebitValueFragment debitValueFragment =new DebitValueFragment();
        Bundle valBundle = new Bundle();
        valBundle.putString("billValue",billValue);
        debitValueFragment.setArguments(valBundle);

        fragmentList.add(debitNormalFragment);
        fragmentList.add(debitValueFragment);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),fragmentList);
        adapter.notifyDataSetChanged();
        vp.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, dip2px(this,2));
        view_bill.setLayoutParams(lp);
    }

    private void setListener() {
        tv_bill_normal.setOnClickListener(this);
        tv_bill_value.setOnClickListener(this);
        ll_back.setOnClickListener(this);

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0){
                    tv_bill_normal.setTextColor(getResources().getColor(R.color.green));
                    tv_bill_value.setTextColor(Color.parseColor("#999999"));
                    position =0;
                    viewAnimation(position);
                }else {
                    tv_bill_value.setTextColor(getResources().getColor(R.color.green));
                    tv_bill_normal.setTextColor(Color.parseColor("#999999"));
                    position = 1;
                    viewAnimation(position);
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void initView() {
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        tv_bill_normal = (TextView) findViewById(R.id.tv_bill_normal);
        tv_bill_value = (TextView) findViewById(R.id.tv_bill_value);
        view_bill = findViewById(R.id.view_bill);
        vp = (ViewPager) findViewById(R.id.vp);
    }
    private int start;
    private int position;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_bill_normal:
                tv_bill_normal.setTextColor(getResources().getColor(R.color.green));
                tv_bill_value.setTextColor(Color.parseColor("#999999"));
                position =0;
                viewAnimation(position);
                vp.setCurrentItem(0);
                break;
            case R.id.tv_bill_value:
                tv_bill_value.setTextColor(getResources().getColor(R.color.green));
                tv_bill_normal.setTextColor(Color.parseColor("#999999"));
                position = 1;
                viewAnimation(position);
                vp.setCurrentItem(1);
                break;
            case R.id.ll_back:
                finish();
                break;
        }
    }

    private void viewAnimation(int i) {
        int end = width*i;
        TranslateAnimation ta = new TranslateAnimation(start,width*i,0,0);
        ta.setDuration(500);
        ta.setFillAfter(true);
        view_bill.startAnimation(ta);

        start = end;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
