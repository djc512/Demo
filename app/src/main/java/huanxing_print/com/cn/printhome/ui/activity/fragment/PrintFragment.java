package huanxing_print.com.cn.printhome.ui.activity.fragment;

import android.view.View;
import android.view.View.OnClickListener;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseFragment;
import huanxing_print.com.cn.printhome.util.CommonUtils;


public class PrintFragment extends BaseFragment implements OnClickListener{

	@Override
	protected void init() {

		
	}


	@Override
	protected int getContextView() {
		CommonUtils.initSystemBar(getActivity());
		return R.layout.frag_home;
	}


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
//        case R.id.tv_user:
//     	    startActivity(new Intent(getActivity(), PersonInfoActivity.class));
//             break;

		default:
			break;

        }
    }


	@Override
	public void onDestroy() {
		super.onDestroy();
	}
    
}
