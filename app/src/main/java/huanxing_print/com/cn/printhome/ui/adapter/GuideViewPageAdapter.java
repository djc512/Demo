package huanxing_print.com.cn.printhome.ui.adapter;

import java.util.List;

import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.ui.activity.login.LoginActivity;
import huanxing_print.com.cn.printhome.ui.activity.main.MainActivity;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.util.SharedPreferencesUtils;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;

public class GuideViewPageAdapter extends PagerAdapter {

	private List<View> pageViews;

	private BaseActivity baseActivity;

	private static final String SHAREDPREFERENCES_NAME = "my_pref";

	private static final String KEY_GUIDE_ACTIVITY = "guide_activity";

	private int lastPostion;

	public GuideViewPageAdapter(BaseActivity baseActivity, List<View> pageViews) {
		this.pageViews = pageViews;
		this.baseActivity = baseActivity;
		if (null != pageViews) {
			lastPostion = pageViews.size() - 1;
		}

	}

	@Override
	public int getCount() {
		return pageViews == null ? 0 : pageViews.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public Object instantiateItem(View v, int position) {
		((ViewPager) v).addView(pageViews.get(position));
		if (position == lastPostion) {
			pageViews.get(position).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					SharedPreferencesUtils.putShareValue(baseActivity, SHAREDPREFERENCES_NAME, KEY_GUIDE_ACTIVITY,
							"false");
					if (ObjectUtils.isNull(SharedPreferencesUtils.getShareString(baseActivity, "password"))) {
						baseActivity.jumpActivity(LoginActivity.class);
					}else{
						baseActivity.jumpActivity(MainActivity.class);
					}
					baseActivity.finishCurrentActivity();
				}
			});

		}
		return pageViews.get(position);
	}

	@Override
	public void destroyItem(View v, int position, Object arg2) {
		((ViewPager) v).removeView(pageViews.get(position));

	}

}
