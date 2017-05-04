package huanxing_print.com.cn.printhome.ui.activity.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseFragment;

/**
 * Created by Administrator on 2017/5/4 0004.
 */

public class ApprovalFragment extends BaseFragment {

    private Context mContext;
    ListView lv;
    @Override
    protected void init() {
        mContext = getActivity();
        initView();


    }

    private void initView() {
        lv = (ListView) findViewById(R.id.lv_frag_approval);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected int getContextView() {
        return R.layout.frag_approval;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
