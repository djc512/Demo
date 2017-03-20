package huanxing_print.com.cn.printhome.ui.activity.fragment;


import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseFragment;
import huanxing_print.com.cn.printhome.ui.activity.login.AccountActivity;
import huanxing_print.com.cn.printhome.ui.activity.login.ContactActivity;
import huanxing_print.com.cn.printhome.ui.activity.login.DaYinActivity;
import huanxing_print.com.cn.printhome.ui.activity.login.MingXiActivity;

public class ContactFragment extends BaseFragment implements OnClickListener{


	private ImageView iv_chongzhi;
	private ImageView iv_mingxi;
	private ImageView iv_dayin;
	private ImageView iv_contact;

	@Override
	protected void init() {
		initViews();
		initData();
		setListener();
		
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	protected int getContextView() {
		return R.layout.frag_usercenter;
	}
	
	private void initData() {

	}


	private void initViews() {
		iv_chongzhi = (ImageView) findViewById(R.id.iv_chongzhi);
		iv_mingxi = (ImageView) findViewById(R.id.iv_mingxi);
		iv_dayin = (ImageView) findViewById(R.id.iv_dayin);
		iv_contact = (ImageView) findViewById(R.id.iv_contact);

	}
	private void setListener() {
		iv_chongzhi.setOnClickListener(this);
		iv_mingxi.setOnClickListener(this);
		iv_dayin.setOnClickListener(this);
		iv_contact.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

//		case R.id.ll_add_contact:
//			startActivity(new Intent(getActivity(),
//					ContactAddActivity.class));
//			break;

			case R.id.iv_chongzhi:
				startActivity(new Intent(getActivity(), AccountActivity.class));
				break;
			case R.id.iv_mingxi:
				startActivity(new Intent(getActivity(), MingXiActivity.class));
				break;
			case R.id.iv_dayin:
				startActivity(new Intent(getActivity(), DaYinActivity.class));
				break;
			case R.id.iv_contact:
				startActivity(new Intent(getActivity(), ContactActivity.class));
				break;

		default:
			break;
		}
	}


}
