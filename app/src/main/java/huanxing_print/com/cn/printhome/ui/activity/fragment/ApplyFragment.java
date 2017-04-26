package huanxing_print.com.cn.printhome.ui.activity.fragment;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseFragment;

public class ApplyFragment extends BaseFragment implements OnClickListener {
	private Context mContext;


	@Override
	protected void init() {
		mContext = getActivity();
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	protected int getContextView() {
		return R.layout.frag_center;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
/*		case R.id.rl_invite:
			//startActivity(new Intent(getActivity(), SetActivity.class));		
			break;*/
		default:
			break;
		}
	}



	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
