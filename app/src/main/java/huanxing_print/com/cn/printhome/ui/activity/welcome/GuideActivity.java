package huanxing_print.com.cn.printhome.ui.activity.welcome;

import java.util.ArrayList;
import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.ui.adapter.GuideViewPageAdapter;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

public class GuideActivity extends BaseActivity {
	private List<View> pageViews;
	private ViewPager viewPager;
	private GuideViewPageAdapter guideViewPageAdapter;

	@Override
	protected BaseActivity getSelfActivity() {
		return this;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		initViews();
		initViewPager();
	}
	
	private void initViews(){
		viewPager=(ViewPager) findViewById(R.id.guide_vp);
		
	}

	private void initViewPager() {
		pageViews = new ArrayList<View>();
		LayoutInflater inflater = getLayoutInflater();
		pageViews.add(inflater.inflate(R.layout.layout_guide_viewpager1, null));
		pageViews.add(inflater.inflate(R.layout.layout_guide_viewpager2, null));
		pageViews.add(inflater.inflate(R.layout.layout_guide_viewpager3, null));
		guideViewPageAdapter=new GuideViewPageAdapter(getSelfActivity(),pageViews);
		viewPager.setAdapter(guideViewPageAdapter);

	}
}
