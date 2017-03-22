package huanxing_print.com.cn.printhome.ui.activity.fragment;


import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseFragment;
import huanxing_print.com.cn.printhome.model.my.UserInfoBean;
import huanxing_print.com.cn.printhome.net.callback.my.UserInfoCallBack;
import huanxing_print.com.cn.printhome.net.request.my.UserInfoRequest;
import huanxing_print.com.cn.printhome.ui.activity.my.AccountActivity;
import huanxing_print.com.cn.printhome.ui.activity.my.ContactActivity;
import huanxing_print.com.cn.printhome.ui.activity.my.DaYinActivity;
import huanxing_print.com.cn.printhome.ui.activity.my.MingXiActivity;
import huanxing_print.com.cn.printhome.ui.activity.my.MyActivity;
import huanxing_print.com.cn.printhome.ui.activity.my.NoticeActivity;
import huanxing_print.com.cn.printhome.ui.activity.my.SettingActivity;
import huanxing_print.com.cn.printhome.ui.activity.my.ShareActivity;

public class ContactFragment extends BaseFragment implements OnClickListener{

	private ImageView iv_chongzhi;
	private ImageView iv_mingxi;
	private ImageView iv_dayin;
	private ImageView iv_contact;
	private ImageView iv_share;
	private ImageView iv_notice;
	private ImageView iv_my;
	private ImageView iv_set;


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
		//网络请求，获取用户信息
		UserInfoRequest.getUserInfo(getActivity(), new UserInfoCallBack() {
			@Override
			public void success(String msg, UserInfoBean bean) {

			}

			@Override
			public void fail(String msg) {

			}

			@Override
			public void connectFail() {

			}
		});
	}

	private void initViews() {
		iv_chongzhi = (ImageView) findViewById(R.id.iv_chongzhi);
		iv_mingxi = (ImageView) findViewById(R.id.iv_mingxi);
		iv_dayin = (ImageView) findViewById(R.id.iv_dayin);
		iv_contact = (ImageView) findViewById(R.id.iv_contact);
		iv_share = (ImageView) findViewById(R.id.iv_share);
		iv_notice = (ImageView) findViewById(R.id.iv_notice);
		iv_my = (ImageView) findViewById(R.id.iv_my);
		iv_set = (ImageView) findViewById(R.id.iv_set);
	}
	private void setListener() {
		iv_chongzhi.setOnClickListener(this);
		iv_mingxi.setOnClickListener(this);
		iv_dayin.setOnClickListener(this);
		iv_contact.setOnClickListener(this);
		iv_contact.setOnClickListener(this);
		iv_notice.setOnClickListener(this);
		iv_share.setOnClickListener(this);
		iv_my.setOnClickListener(this);
		iv_set.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.iv_my:
				startActivity(new Intent(getActivity(), MyActivity.class));
				break;
			case R.id.iv_notice:
				startActivity(new Intent(getActivity(), NoticeActivity.class));
				break;
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
            case R.id.iv_share:
				startActivity(new Intent(getActivity(), ShareActivity.class));
				break;
			case R.id.iv_set:
				startActivity(new Intent(getActivity(), SettingActivity.class));
				break;

		default:
			break;
		}
	}
}
