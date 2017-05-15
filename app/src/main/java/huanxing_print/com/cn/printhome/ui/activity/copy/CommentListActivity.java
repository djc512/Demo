package huanxing_print.com.cn.printhome.ui.activity.copy;

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
import huanxing_print.com.cn.printhome.model.comment.CommentListBean;
import huanxing_print.com.cn.printhome.net.callback.comment.CommentListCallback;
import huanxing_print.com.cn.printhome.net.request.commet.CommentListRequest;
import huanxing_print.com.cn.printhome.ui.activity.fragment.fragcomment.CommentAllFragment;
import huanxing_print.com.cn.printhome.ui.activity.fragment.fragcomment.CommentBadFragment;
import huanxing_print.com.cn.printhome.ui.activity.fragment.fragcomment.CommentGoodFragment;
import huanxing_print.com.cn.printhome.ui.activity.fragment.fragcomment.CommentMediumFragment;
import huanxing_print.com.cn.printhome.ui.adapter.ViewPagerAdapter;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;

/**
 * Created by Administrator on 2017/5/4 0004.
 */

public class CommentListActivity extends FragmentActivity implements View.OnClickListener {
    private ImageView iv_back;
    private LinearLayout ll_back;
    private TextView tv_all;
    private LinearLayout ll_all;
    private TextView tv_good;
    private LinearLayout ll_good;
    private LinearLayout ll_medium;
    private TextView tv_bad;
    private LinearLayout ll_bad;
    private View view_line;
    private Context ctx;
    private ViewPager vp_comment;
    private List<Fragment> fragments;
    private int tvWidth;
    private int llWidth;
    private TextView tv_medium;
    private List<TextView> textViews;
    private LinearLayout.LayoutParams lp;
    private int marginLeft;
    private TextView tv_address;
    private TextView tv_printno;
    private CommentAllFragment allFragment;
    private CommentGoodFragment goodFragment;
    private CommentMediumFragment mediumFragment;
    private CommentBadFragment badFragment;
    private String printno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.activity_comment_list);
        ctx = this;
        printno = getIntent().getExtras().getString("printer_id");
        initView();
        initData();
        initListener();
    }

    private void initView() {
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_printno = (TextView) findViewById(R.id.tv_printno);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        tv_all = (TextView) findViewById(R.id.tv_all);
        ll_all = (LinearLayout) findViewById(R.id.ll_all);
        tv_good = (TextView) findViewById(R.id.tv_good);
        ll_good = (LinearLayout) findViewById(R.id.ll_good);
        ll_medium = (LinearLayout) findViewById(R.id.ll_medium);
        tv_medium = (TextView) findViewById(R.id.tv_medium);
        tv_bad = (TextView) findViewById(R.id.tv_bad);
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
        llWidth = screenWidth / 4;
        lp = new LinearLayout.LayoutParams(tvWidth, CommonUtils.dip2px(ctx, 2));
        marginLeft = (llWidth - tvWidth) / 2;

        fragments = new ArrayList<>();

        allFragment = new CommentAllFragment();
        Bundle allbundle = new Bundle();
        allbundle.putString("printno", printno);
        allFragment.setArguments(allbundle);

        goodFragment = new CommentGoodFragment();
        Bundle goodbundle = new Bundle();
        goodbundle.putString("printno", printno);
        goodFragment.setArguments(goodbundle);

        mediumFragment = new CommentMediumFragment();
        Bundle mediumbundle = new Bundle();
        mediumbundle.putString("printno", printno);
        mediumFragment.setArguments(mediumbundle);

        badFragment = new CommentBadFragment();
        Bundle badbundle = new Bundle();
        badbundle.putString("printno", printno);
        badFragment.setArguments(badbundle);

        fragments.add(allFragment);
        fragments.add(goodFragment);
        fragments.add(mediumFragment);
        fragments.add(badFragment);

        textViews = new ArrayList<>();
        textViews.add(tv_all);
        textViews.add(tv_good);
        textViews.add(tv_medium);
        textViews.add(tv_bad);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
        vp_comment.setAdapter(adapter);

        getData();
    }

    /**
     * 获取数据
     */
    private void getData() {
        DialogUtils.showProgressDialog(ctx, "正在加载中...");
        CommentListRequest.request(ctx, 1, printno, 0, new CommentListCallback() {
            @Override
            public void success(CommentListBean bean) {
                DialogUtils.closeProgressDialog();
                List<CommentListBean.DetailBean> detail = bean.getDetail();
                CommentListBean.DetailBean detailBean = detail.get(0);
                tv_address.setText(bean.getAddress());
                tv_printno.setText("编号:" + printno);
            }

            @Override
            public void fail(String msg) {

            }

            @Override
            public void connectFail() {

            }
        });
    }

    private void initListener() {
        iv_back.setOnClickListener(this);
        ll_all.setOnClickListener(this);
        ll_good.setOnClickListener(this);
        ll_medium.setOnClickListener(this);
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
                        allFragment.getData(0, printno);
                        break;
                    case 1:
                        setTextView(1);
                        goodFragment.getData(1, printno);
                        break;
                    case 2:
                        setTextView(2);
                        mediumFragment.getData(2, printno);
                        break;
                    case 3:
                        setTextView(3);
                        badFragment.getData(3, printno);
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
        lp.leftMargin = marginLeft;
        view_line.setLayoutParams(lp);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_all:
                setTextView(0);
                vp_comment.setCurrentItem(0);
                break;
            case R.id.ll_good:
                setTextView(1);
                vp_comment.setCurrentItem(1);
                break;
            case R.id.ll_medium:
                setTextView(2);
                vp_comment.setCurrentItem(2);
                break;
            case R.id.ll_bad:
                setTextView(3);
                vp_comment.setCurrentItem(3);
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
