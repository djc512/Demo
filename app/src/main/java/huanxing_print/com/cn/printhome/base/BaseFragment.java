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

	private View mView;
	//private OnekeyShare oks;
	protected BaseApplication baseApplication;
	@Override
	public final View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		if (mView != null) {
			ViewGroup parent = (ViewGroup) mView.getParent();
			if (parent != null) {
				parent.removeView(mView);
			}
			return mView;
		}
		View rootView = null;
		rootView = inflater.inflate(getContextView(), container, false);
		mView = rootView;
		init();
		return rootView;
	}


	protected void init() {

		baseApplication = (BaseApplication) getActivity().getApplication();
	}

	protected View findViewById(int id) {
		if (mView == null) {
			return null;
		}
		return mView.findViewById(id);
	}


    protected void showToast(String msg) {

        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

	protected abstract int getContextView();

}
