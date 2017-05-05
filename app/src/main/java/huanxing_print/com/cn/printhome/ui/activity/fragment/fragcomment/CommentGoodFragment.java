package huanxing_print.com.cn.printhome.ui.activity.fragment.fragcomment;

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
import huanxing_print.com.cn.printhome.ui.adapter.CommentListAdapter;

/**
 * Created by Administrator on 2017/5/4 0004.
 */

public class CommentGoodFragment extends Fragment {

    private RecyclerView rv_comment_list;
    private Context ctx;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ctx = getActivity();
        View view = inflater.inflate(R.layout.frag_comment, null);
        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        rv_comment_list = (RecyclerView) view.findViewById(R.id.rv_comment_list);
    }

    private void initData() {
        CommentListAdapter adapter = new CommentListAdapter(ctx);
        LinearLayoutManager manager = new LinearLayoutManager(ctx);
        rv_comment_list.setLayoutManager(manager);
        rv_comment_list.setAdapter(adapter);
    }
}
