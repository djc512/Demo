package huanxing_print.com.cn.printhome.ui.activity.fragment.fragcommunity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.ui.adapter.CommunityNewestListAdapter;

/**
 * Created by Administrator on 2017/5/4 0004.
 */

public class CommunityNewestFragment extends Fragment {

    private RecyclerView rv_community_list;
    private Context ctx;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ctx = getActivity();
        View view = inflater.inflate(R.layout.frag_community_newest, null);
        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        rv_community_list = (RecyclerView) view.findViewById(R.id.rv_community_list);
    }

    private void initData() {
        CommunityNewestListAdapter adapter = new CommunityNewestListAdapter(ctx);
        LinearLayoutManager manager = new LinearLayoutManager(ctx);
        rv_community_list.setLayoutManager(manager);
        rv_community_list.setAdapter(adapter);
    }
}
