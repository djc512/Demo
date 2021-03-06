package huanxing_print.com.cn.printhome.ui.activity.fragment.fragapproval;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.ActivityHelper;
import huanxing_print.com.cn.printhome.base.BaseFragment;
import huanxing_print.com.cn.printhome.constant.ConFig;
import huanxing_print.com.cn.printhome.model.approval.UnreadMessage;
import huanxing_print.com.cn.printhome.net.callback.approval.QueryMessageCallBack;
import huanxing_print.com.cn.printhome.net.request.approval.ApprovalRequest;
import huanxing_print.com.cn.printhome.ui.activity.approval.ApprovalHomeActivity;
import huanxing_print.com.cn.printhome.ui.activity.copy.CopyActivity;
import huanxing_print.com.cn.printhome.ui.activity.login.LoginActivity;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.util.SharedPreferencesUtils;
import huanxing_print.com.cn.printhome.view.dialog.LoadingDialog;

public class ApplyFragment extends BaseFragment implements OnClickListener {
	private Context mContext;
	private TextView tv_approve_count;
	private String token;
	private int approverNum;//待我审批
	private int copyerNum;//抄送我的
	private int initiatorNum;//我发起的
	private int total;
	private final String BROADCAST_ACTION_APPROVALNUM_REFRESH= "approvalnum.refresh";
	private LoadingDialog loadingDialog;
	@Override
	protected void init() {
		mContext = getActivity();
		//EventBus.getDefault().register(this);
		// 注册广播
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BROADCAST_ACTION_APPROVALNUM_REFRESH);
		getActivity().registerReceiver(mBroadcastReceiver, intentFilter);

		initViews();
		setListener();
		initData();
	}


	@Override
	public void onResume() {
		super.onResume();

	}
	public void reload() {
		initData();
	}


	@Override
	protected int getContextView() {
		return R.layout.frag_apply;
	}


	private void initViews() {
		loadingDialog = new LoadingDialog(getActivity());
		tv_approve_count = (TextView) findViewById(R.id.tv_approve_count);

	}


	private void setListener() {
		findViewById(R.id.ll_copy).setOnClickListener(this);
		findViewById(R.id.ll_approve).setOnClickListener(this);
	}

	private void initData() {
		token = SharedPreferencesUtils.getShareString(getActivity(), ConFig.SHAREDPREFERENCES_NAME,
				"loginToken");
		loadingDialog.show();
		ApprovalRequest.queryUnreadMessage(getActivity(), token, queryMessageCallBack);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.ll_copy:
				startActivity(new Intent(getActivity(), CopyActivity.class));
				break;
			case R.id.ll_approve:
				Intent approvalIntent = new Intent(getActivity(), ApprovalHomeActivity.class);
				approvalIntent.putExtra("approverNum",approverNum);
				approvalIntent.putExtra("copyerNum",copyerNum);
				approvalIntent.putExtra("initiatorNum",initiatorNum);
				startActivity(approvalIntent);
				break;
			default:
				break;
		}
	}

	private QueryMessageCallBack queryMessageCallBack = new QueryMessageCallBack() {

		@Override
		public void success(String msg, UnreadMessage bean) {
			loadingDialog.dismiss();
			if (!ObjectUtils.isNull(bean)) {
				approverNum = bean.getApproverNum();
				copyerNum= bean.getCopyerNum();
				initiatorNum= bean.getInitiatorNum();
				total= bean.getTotal();
				if (approverNum>0){
					tv_approve_count.setVisibility(View.VISIBLE);
					tv_approve_count.setText(approverNum+"");
				}else{
					tv_approve_count.setVisibility(View.GONE);
				}
			}
		}

		@Override
		public void fail(String msg) {
			loadingDialog.dismiss();
			if (!ObjectUtils.isNull(msg)&&"用户未登录".equals(msg)){
				SharedPreferencesUtils.clearAllShareValue(getActivity());
				ActivityHelper.getInstance().finishAllActivity();
				EMClient.getInstance().logout(true);//环信退出
				startActivity(new Intent(getActivity(), LoginActivity.class));
			}else{
				showToast(msg);
			}
		}

		@Override
		public void connectFail() {
			loadingDialog.dismiss();
		}
	};

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			if (action.equals(BROADCAST_ACTION_APPROVALNUM_REFRESH)) {
//				approverNum--;
//				if (approverNum>0){
//					tv_approve_count.setVisibility(View.VISIBLE);
//					tv_approve_count.setText(approverNum+"");
//				}else{
//					tv_approve_count.setVisibility(View.GONE);
//				}
				//网络请求，获取用户信息
				ApprovalRequest.queryUnreadMessage(getActivity(), token, queryMessageCallBack);
			}
		}
	};
	@Override
	public void onDestroy() {
		super.onDestroy();
		//EventBus.getDefault().unregister(this);
		getActivity().unregisterReceiver(mBroadcastReceiver);
	}

}
