package huanxing_print.com.cn.printhome.ui.activity.fragment;

import android.view.View;
import android.view.View.OnClickListener;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseFragment;


public class HomeFragment extends BaseFragment implements OnClickListener{

	@Override
	protected void init() {

		
	}


	@Override
	protected int getContextView() {
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
