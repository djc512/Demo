package huanxing_print.com.cn.printhome.ui.activity.print.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import huanxing_print.com.cn.printhome.R;

/**
 * Created by LGH on 2017/4/27.
 */

public class PcFileFragment extends BaseLazyFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_pc_file, container, false);
            isPrepared = true;
            if (!isLoaded) {
                lazyLoad();
            }
        }
        return view;
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
    }

}
