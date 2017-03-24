package huanxing_print.com.cn.printhome.ui.activity.fragment;


import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseFragment;
import huanxing_print.com.cn.printhome.model.my.MyInfoBean;
import huanxing_print.com.cn.printhome.net.callback.my.MyInfoCallBack;
import huanxing_print.com.cn.printhome.net.request.my.MyInfoRequest;
import huanxing_print.com.cn.printhome.ui.activity.my.AccountActivity;
import huanxing_print.com.cn.printhome.ui.activity.my.ContactActivity;
import huanxing_print.com.cn.printhome.ui.activity.my.DaYinActivity;
import huanxing_print.com.cn.printhome.ui.activity.my.MingXiActivity;
import huanxing_print.com.cn.printhome.ui.activity.my.MyActivity;
import huanxing_print.com.cn.printhome.ui.activity.my.NoticeActivity;
import huanxing_print.com.cn.printhome.ui.activity.my.SettingActivity;
import huanxing_print.com.cn.printhome.ui.activity.my.ShareActivity;
import huanxing_print.com.cn.printhome.util.CommonUtils;

public class ContactFragment extends BaseFragment implements OnClickListener{

	private ImageView iv_chongzhi;
	private ImageView iv_mingxi;
	private ImageView iv_dayin;
	private ImageView iv_contact;
	private ImageView iv_share;
	private ImageView iv_notice;
	private ImageView iv_my;
	private ImageView iv_set;

	private MyInfoBean useBean;
	private CircleImageView iv_head;
	private TextView tv_phone;
	private TextView tv_name;
    private LinearLayout ll_my_account;
    private LinearLayout ll_my_contact;
    private LinearLayout ll_my_dy;
    private LinearLayout ll_my_mx;
    private LinearLayout ll_my_set;
    private LinearLayout ll_my_share;
	private RelativeLayout rl_userInfo;

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
		// 改变状态栏的颜色使其与APP风格一体化
		CommonUtils.initSystemBarGreen(getActivity());
		return R.layout.frag_usercenter;
	}

	private void initData() {
		//网络请求，获取用户信息
		MyInfoRequest.getMyInfo(getActivity(),new MyMyInfoCallBack());
		//设置用户头像
//		Glide.with(getActivity())
//				.load(useBean.getFaceUrl())
//				.into(iv_head);

//		tv_name.setText(useBean.getName());
//		tv_phone.setText(useBean.getMobileNumber());
	}

	public class MyMyInfoCallBack extends MyInfoCallBack{
		@Override
		public void success(String msg, MyInfoBean bean) {
			showToast("获取成功");
			useBean = bean;

		}

		@Override
		public void fail(String msg) {

		}

		@Override
		public void connectFail() {

		}
	}

	private void initViews() {
		tv_phone = (TextView)findViewById(R.id.tv_phone);
		tv_name = (TextView)findViewById(R.id.tv_name);

		iv_head = (CircleImageView) findViewById(R.id.iv_head);
		iv_chongzhi = (ImageView) findViewById(R.id.iv_chongzhi);
		iv_mingxi = (ImageView) findViewById(R.id.iv_mingxi);
		iv_dayin = (ImageView) findViewById(R.id.iv_dayin);
		iv_contact = (ImageView) findViewById(R.id.iv_contact);
		iv_share = (ImageView) findViewById(R.id.iv_share);
		iv_notice = (ImageView) findViewById(R.id.iv_notice);
		iv_my = (ImageView) findViewById(R.id.iv_my);
		iv_set = (ImageView) findViewById(R.id.iv_set);

        ll_my_account = (LinearLayout) findViewById(R.id.ll_my_account);
        ll_my_contact = (LinearLayout) findViewById(R.id.ll_my_contact);
        ll_my_dy = (LinearLayout) findViewById(R.id.ll_my_dy);
        ll_my_mx = (LinearLayout) findViewById(R.id.ll_my_mx);
        ll_my_set = (LinearLayout) findViewById(R.id.ll_my_set);
        ll_my_share = (LinearLayout) findViewById(R.id.ll_my_share);

		rl_userInfo = (RelativeLayout) findViewById(R.id.rl_userMsg);
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

		ll_my_account.setOnClickListener(this);
		ll_my_contact.setOnClickListener(this);
		ll_my_dy.setOnClickListener(this);
		ll_my_mx.setOnClickListener(this);
		ll_my_set.setOnClickListener(this);
		ll_my_share.setOnClickListener(this);

		rl_userInfo.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.rl_userMsg:
				startActivity(new Intent(getActivity(), MyActivity.class));
				break;
			case R.id.iv_notice:
				startActivity(new Intent(getActivity(), NoticeActivity.class));
				break;
			case R.id.ll_my_account:
				startActivity(new Intent(getActivity(), AccountActivity.class));
				break;
			case R.id.ll_my_mx:
				startActivity(new Intent(getActivity(), MingXiActivity.class));
				break;
			case R.id.ll_my_dy:
				startActivity(new Intent(getActivity(), DaYinActivity.class));
				break;
			case R.id.ll_my_contact:
				startActivity(new Intent(getActivity(), ContactActivity.class));
				break;
            case R.id.ll_my_share:
				startActivity(new Intent(getActivity(), ShareActivity.class));
				break;
			case R.id.ll_my_set:
				startActivity(new Intent(getActivity(), SettingActivity.class));
				break;

		default:
			break;
		}
	}
}
