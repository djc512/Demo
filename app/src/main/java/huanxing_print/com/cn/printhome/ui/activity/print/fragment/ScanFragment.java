package huanxing_print.com.cn.printhome.ui.activity.print.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import huanxing_print.com.cn.printhome.R;

/**
 * Created by LGH on 2017/4/27.
 */

public class ScanFragment extends BaseLazyFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_scan, container, false);
        }
        return view;
    }


    @Override
    protected void lazyLoad() {
    }
}
