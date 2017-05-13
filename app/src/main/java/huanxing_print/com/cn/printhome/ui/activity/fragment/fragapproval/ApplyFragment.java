package huanxing_print.com.cn.printhome.ui.activity.fragment.fragapproval;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseFragment;
import huanxing_print.com.cn.printhome.constant.ConFig;
import huanxing_print.com.cn.printhome.model.approval.UnreadMessage;
import huanxing_print.com.cn.printhome.net.callback.approval.QueryMessageCallBack;
import huanxing_print.com.cn.printhome.net.request.approval.ApprovalRequest;
import huanxing_print.com.cn.printhome.ui.activity.approval.ApprovalHomeActivity;
import huanxing_print.com.cn.printhome.ui.activity.copy.CopyActivity;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.util.SharedPreferencesUtils;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;

public class ApplyFragment extends BaseFragment implements OnClickListener {
	private Context mContext;
	private TextView tv_approve_count;
	private String token;
	private int approverNum;//待我审批
	private int copyerNum;//抄送我的
	private int initiatorNum;//我发起的
	private int total;
	@Override
	protected void init() {

		mContext = getActivity();
		initViews();
		setListener();
		initData();
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
		tv_approve_count = (TextView) findViewById(R.id.tv_approve_count);

	}

	private void setListener() {
		findViewById(R.id.ll_copy).setOnClickListener(this);
		findViewById(R.id.ll_approve).setOnClickListener(this);
	}

	private void initData() {
		token = SharedPreferencesUtils.getShareString(getActivity(), ConFig.SHAREDPREFERENCES_NAME,
				"loginToken");
		DialogUtils.showProgressDialog(getActivity(), "加载中");
		//网络请求，获取用户信息
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
			DialogUtils.closeProgressDialog();
			if (!ObjectUtils.isNull(bean)) {
				approverNum = bean.getApproverNum();
				copyerNum= bean.getCopyerNum();
				initiatorNum= bean.getInitiatorNum();
				total= bean.getTotal();
				if (total>0){
					tv_approve_count.setVisibility(View.VISIBLE);
					tv_approve_count.setText(total+"");
				}else{
					tv_approve_count.setVisibility(View.GONE);
				}
			}
		}

		@Override
		public void fail(String msg) {
			DialogUtils.closeProgressDialog();
			showToast(msg);
		}

		@Override
		public void connectFail() {
			DialogUtils.closeProgressDialog();
		}
	};
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
