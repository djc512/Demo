package huanxing_print.com.cn.printhome.ui.activity.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.util.CommonUtils;

/**
 * Created by Administrator on 2017/4/28 0028.
 */

public class HuKouFragment extends Fragment {
    private Context ctx;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ctx = getActivity();
        CommonUtils.initSystemBar(getActivity());
        View view = inflater.inflate(R.layout.frag_hukou, null);
        return view;
    }
}
