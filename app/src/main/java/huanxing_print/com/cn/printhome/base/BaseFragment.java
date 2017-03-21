package huanxing_print.com.cn.printhome.base;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


@SuppressLint("NewApi")
public abstract class BaseFragment extends Fragment {

	private View v;
	//private OnekeyShare oks;
	protected BaseApplication baseApplication;
	@Override
	public final View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		v = inflater.inflate(getContextView(), null);
	//	oks = new OnekeyShare();
		init();
		return v;
	}


	protected void init() {
		baseApplication = (BaseApplication) getActivity().getApplication();
	}

	protected View findViewById(int id) {
		if (v == null) {
			return null;
		}
		return v.findViewById(id);
	}


    protected void showToast(String msg) {

        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

	protected abstract int getContextView();

}
