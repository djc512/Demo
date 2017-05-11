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

public class CommentAllFragment extends Fragment {

    private RecyclerView rv_comment_list;
    private Context ctx;
    private int printno;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ctx = getActivity();
        printno = getArguments().getInt("printno");
        View view = inflater.inflate(R.layout.frag_comment, null);
        initView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getData(0,printno);
    }

    private void initView(View view) {
        rv_comment_list = (RecyclerView) view.findViewById(R.id.rv_comment_list);
    }

    public void getData(int type,int printno) {
        DialogUtils.showProgressDialog(ctx, "正在加载中...");
        CommentListRequest.request(ctx, 1, printno, type, new CommentListCallback() {
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
