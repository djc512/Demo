package huanxing_print.com.cn.printhome.ui.activity.fragment;

import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseFragment;


public class PrintFragment extends BaseFragment implements OnClickListener{


	private ViewPager mViewPaper;
	private List<ImageView> images;
	private List<View> dots;
	private int currentItem;
	//记录上一次点的位置
	private int oldPosition = 0;
	//存放图片的id
	private int[] imageIds = new int[]{
			R.drawable.a,
			R.drawable.b,
			R.drawable.c,
			R.drawable.d,
			R.drawable.e
	};

	private ViewPagerAdapter adapter;
	private ScheduledExecutorService scheduledExecutorService;
	@Override
	protected void init() {

		mViewPaper = (ViewPager) findViewById(R.id.vp);
		findViewById(R.id.tv_copy).setOnClickListener(this);
		findViewById(R.id.tv_print).setOnClickListener(this);
		findViewById(R.id.tv_instructions).setOnClickListener(this);

		//显示的图片
		images = new ArrayList<ImageView>();
		for(int i = 0; i < imageIds.length; i++){
			ImageView imageView = new ImageView(getActivity());
			imageView.setBackgroundResource(imageIds[i]);
			images.add(imageView);
		}
		//显示的小点
		dots = new ArrayList<View>();
		dots.add(findViewById(R.id.dot_0));
		dots.add(findViewById(R.id.dot_1));
		dots.add(findViewById(R.id.dot_2));
		dots.add(findViewById(R.id.dot_3));
		dots.add(findViewById(R.id.dot_4));

		adapter = new ViewPagerAdapter();
		mViewPaper.setAdapter(adapter);

		mViewPaper.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {


			@Override
			public void onPageSelected(int position) {
				//title.setText(titles[position]);
				dots.get(position).setBackgroundResource(R.drawable.dot_focused);
				dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);

				oldPosition = position;
				currentItem = position;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	/**
	 * 利用线程池定时执行动画轮播
	 */
	@Override
	public void onResume() {
		super.onResume();
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		scheduledExecutorService.scheduleWithFixedDelay(
				new ViewPageTask(),2,2,TimeUnit.SECONDS);
	}
	@Override
	protected int getContextView() {
		return R.layout.frag_print;
	}


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
        case R.id.tv_copy:
     	    //startActivity(new Intent(getActivity(), PersonInfoActivity.class));
             break;
		case R.id.tv_print:
			//startActivity(new Intent(getActivity(), PersonInfoActivity.class));
			 break;
		case R.id.tv_instructions:
			//startActivity(new Intent(getActivity(), PersonInfoActivity.class));
			 break;
		default:
			break;

        }
    }


	/**
	 * 自定义Adapter
	 * @author liuyazhuang
	 *
	 */
	private class ViewPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return images.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup view, int position, Object object) {
			// TODO Auto-generated method stub
//			super.destroyItem(container, position, object);
//			view.removeView(view.getChildAt(position));
//			view.removeViewAt(position);
			view.removeView(images.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup view, int position) {
			// TODO Auto-generated method stub
			view.addView(images.get(position));
			return images.get(position);
		}

	}



	private class ViewPageTask implements Runnable{

		@Override
		public void run() {
			currentItem = (currentItem + 1) % imageIds.length;
			mHandler.sendEmptyMessage(0);
		}
	}

	/**
	 * 接收子线程传递过来的数据
	 */
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			mViewPaper.setCurrentItem(currentItem);
		};
	};
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
    
}
