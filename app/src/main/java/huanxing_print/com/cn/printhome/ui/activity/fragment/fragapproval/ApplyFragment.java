package huanxing_print.com.cn.printhome.ui.activity.fragment.fragapproval;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseFragment;
import huanxing_print.com.cn.printhome.ui.activity.approval.ApprovalHomeActivity;
import huanxing_print.com.cn.printhome.ui.activity.copy.CopyActivity;

public class ApplyFragment extends BaseFragment implements OnClickListener {
	private Context mContext;


	@Override
	protected void init() {

		mContext = getActivity();
		initViews();
		// initData();
		setListener();
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	protected int getContextView() {
		return R.layout.frag_apply;
	}


	private void initViews() {


	}

	private void setListener() {
		findViewById(R.id.ll_copy).setOnClickListener(this);
		findViewById(R.id.ll_approve).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.ll_copy:
				startActivity(new Intent(getActivity(), CopyActivity.class));
				break;
			case R.id.ll_approve:
				startActivity(new Intent(getActivity(), ApprovalHomeActivity.class));
				break;
			default:
				break;
		}
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
