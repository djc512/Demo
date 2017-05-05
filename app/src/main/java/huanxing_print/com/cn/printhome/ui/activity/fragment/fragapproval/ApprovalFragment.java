package huanxing_print.com.cn.printhome.ui.activity.fragment.fragapproval;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import huanxing_print.com.cn.printhome.R;

/**
 * Created by Administrator on 2017/5/4 0004.
 */

public class ApprovalFragment extends Fragment {

    private ListView lv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_approval, null);
        initView(view);

        return view;
    }

    private void initView(View view) {
        lv = (ListView) view.findViewById(R.id.lv_frag_approval);
    }


    @Override
    public void onResume() {
        super.onResume();
    }
}
