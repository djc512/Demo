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

import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.model.comment.CommentListBean;
import huanxing_print.com.cn.printhome.net.callback.comment.CommentListCallback;
import huanxing_print.com.cn.printhome.net.request.commet.CommentListRequest;
import huanxing_print.com.cn.printhome.ui.adapter.CommentListAdapter;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;

/**
 * Created by Administrator on 2017/5/4 0004.
 */

public class CommentBadFragment extends Fragment {

    private RecyclerView rv_comment_list;
    private Context ctx;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ctx = getActivity();
        View view = inflater.inflate(R.layout.frag_comment, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        rv_comment_list = (RecyclerView) view.findViewById(R.id.rv_comment_list);
    }

    @Override
    public void onResume() {
        super.onResume();
        getData(3);
    }

    public void getData(int index) {
        DialogUtils.showProgressDialog(ctx,"正在加载中...");
        CommentListRequest.request(ctx, 1, "zwf001", index, new CommentListCallback() {
            @Override
            public void success(CommentListBean bean) {
                DialogUtils.closeProgressDialog();
                List<CommentListBean.DetailBean> detail = bean.getDetail();
                CommentListAdapter adapter = new CommentListAdapter(ctx, detail);
                LinearLayoutManager manager = new LinearLayoutManager(ctx);
                rv_comment_list.setLayoutManager(manager);
                rv_comment_list.setAdapter(adapter);
            }

            @Override
            public void fail(String msg) {

            }

            @Override
            public void connectFail() {

            }
        });
    }
}
