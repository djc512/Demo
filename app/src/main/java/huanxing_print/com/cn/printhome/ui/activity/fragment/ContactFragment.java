package huanxing_print.com.cn.printhome.ui.activity.fragment;


import android.view.View;
import android.view.View.OnClickListener;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseFragment;

public class ContactFragment extends BaseFragment implements OnClickListener{



	@Override
	protected void init() {
		initViews();
		initData();
		
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	protected int getContextView() {
		return R.layout.frag_contact;
	}
	
	private void initData() {

		
	}


	private void initViews() {

	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

//		case R.id.ll_add_contact:
//			startActivity(new Intent(getActivity(),
//					ContactAddActivity.class));
//			break;

		default:
			break;
		}
	}


}
