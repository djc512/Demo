package huanxing_print.com.cn.printhome.ui.activity.fragment;

import android.view.View;
import android.view.View.OnClickListener;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseFragment;
import huanxing_print.com.cn.printhome.util.StepViewUtil;
import huanxing_print.com.cn.printhome.view.StepLineView;


public class PrintFragment extends BaseFragment implements OnClickListener{



	@Override
	protected void init() {
        StepViewUtil.init(getActivity(), findViewById(R.id.step), StepLineView.STEP_DEFAULT);

	}


	@Override
	public void onResume() {
		super.onResume();


	}
	@Override
	protected int getContextView() {
		return R.layout.frag_print;

	}


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
        case R.id.ll_file:
     	    //startActivity(new Intent(getActivity(), CopyActivity.class));
             break;
		case R.id.ll_weixin:
			//startActivity(new Intent(getActivity(), AddFileActivity.class));
			 break;
		case R.id.ll_qq:
			//startActivity(new Intent(getActivity(), OperatingInstructionsActivity.class));
			 break;
		case R.id.ll_photo:
			//startActivity(new Intent(getActivity(), CopyActivity.class));
			break;
		case R.id.ll_computer:
			//startActivity(new Intent(getActivity(), AddFileActivity.class));
			break;
		case R.id.ll_wifi:
			//startActivity(new Intent(getActivity(), OperatingInstructionsActivity.class));
			break;
		default:
			break;

        }
    }



    
}
