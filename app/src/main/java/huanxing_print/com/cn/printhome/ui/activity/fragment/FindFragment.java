package huanxing_print.com.cn.printhome.ui.activity.fragment;


import android.view.View;
import android.view.View.OnClickListener;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseFragment;

public class FindFragment extends BaseFragment implements 
     OnClickListener
{


    
    @Override
    protected void init()
    {
        initView();
        initData();
    }
	@Override
    public void onResume()
    {
        super.onResume();

    }
    
    @Override
    protected int getContextView()
    {
        return R.layout.frag_find;
    }

	private void initView() {

	}

    private void initData() {


	}

	

    
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
/*        case R.id.ll_communication:

             break;*/

		default:
			break;
        }
    }
    

    @Override
	public void onDestroy()
    {
        super.onDestroy();
    }
    
}
