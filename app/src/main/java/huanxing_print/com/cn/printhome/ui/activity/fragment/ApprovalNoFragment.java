package huanxing_print.com.cn.printhome.ui.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.ui.activity.approval.ApprovalBuyAddOrRemoveActivity;
import huanxing_print.com.cn.printhome.ui.adapter.ApprovalListAdapter;
import huanxing_print.com.cn.printhome.util.Info;

/**
 * Created by Administrator on 2017/5/4 0004.
 */

public class ApprovalNoFragment extends Fragment implements ListView.OnItemClickListener{
    ListView lv;
    private List<Info> infos = new ArrayList<Info>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_approval, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        lv = (ListView) view.findViewById(R.id.lv_frag_approval);
        lv.setAdapter(new ApprovalListAdapter(getActivity(),infos));
        lv.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(),ApprovalBuyAddOrRemoveActivity.class);
        intent.putExtra("what","2");
        startActivity(intent);
    }

}
