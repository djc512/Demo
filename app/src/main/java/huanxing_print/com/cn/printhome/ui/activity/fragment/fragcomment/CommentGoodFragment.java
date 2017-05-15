package huanxing_print.com.cn.printhome.ui.activity.fragment.fragcomment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;

import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.model.comment.CommentListBean;
import huanxing_print.com.cn.printhome.net.callback.comment.CommentListCallback;
import huanxing_print.com.cn.printhome.net.request.commet.CommentListRequest;
import huanxing_print.com.cn.printhome.ui.adapter.CommentListAdapter1;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;
import huanxing_print.com.cn.printhome.view.refresh.CustomerFooter;

/**
 * Created by Administrator on 2017/5/4 0004.
 */

public class CommentGoodFragment extends Fragment {

    private ListView rv_comment_list;
    private Context ctx;
    private String printno;
    private XRefreshView xrf_comment;
    private int pageNum =1;
    private boolean isLoadMore;
    private List<CommentListBean.DetailBean> detail;
    private CommentListAdapter1 adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ctx = getActivity();
        printno = getArguments().getString("printno");
        View view = inflater.inflate(R.layout.frag_comment, null);
        initView(view);
        initListener();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getData(1, printno);
    }

    private void initView(View view) {
        rv_comment_list = (ListView) view.findViewById(R.id.rv_comment_list);
        xrf_comment = (XRefreshView) view.findViewById(R.id.xrf_comment);
    }

    private int type;

    public void getData(int type, String printno) {
        this.type = type;
        DialogUtils.showProgressDialog(ctx, "正在加载中...");
        CommentListRequest.request(ctx, 1, printno, type, new MyCommentListCallback());
    }

    private void initListener() {
        xrf_comment.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh() {
                super.onRefresh();
                isLoadMore = false;
                if (null == detail) {
                    Toast.makeText(getActivity(), "没有充值记录", Toast.LENGTH_SHORT).show();
                    xrf_comment.stopRefresh();
                    return;
                }
                detail.clear();
                //获取充值记录
                CommentListRequest.request(ctx, 1, printno, type, new MyCommentListCallback());
                xrf_comment.stopRefresh();
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                super.onLoadMore(isSilence);
                isLoadMore = true;
                pageNum++;
                CommentListRequest.request(ctx, pageNum, printno, type, new MyCommentListCallback());
            }
        });
    }

    public class MyCommentListCallback extends CommentListCallback {

        @Override
        public void success(CommentListBean bean) {
            DialogUtils.closeProgressDialog();
            if (isLoadMore) {//如果是加载更多
                if (!ObjectUtils.isNull(bean)) {
                    xrf_comment.stopLoadMore();
                    if (!ObjectUtils.isNull(bean.getDetail())) {
                        detail.addAll(bean.getDetail());
                        adapter.notifyDataSetChanged();
                    } else {
                        ToastUtil.doToast(getActivity(), "没有更多数据");
                        return;
                    }
                } else {
                    ToastUtil.doToast(getActivity(), "没有更多数据");
                    xrf_comment.stopLoadMore();
                    return;
                }
            } else {
                if (bean != null) {
                    detail = bean.getDetail();
                    if (null != detail && detail.size() > 0) {
                        adapter = new CommentListAdapter1(ctx, detail);
                        rv_comment_list.setAdapter(adapter);
                    }
                } else {
                    Toast.makeText(getActivity(), "没有充值数据", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            xrf_comment.setAutoLoadMore(true);
            xrf_comment.setPullLoadEnable(true);
            xrf_comment.setAutoLoadMore(false);
            //设置在上拉加载被禁用的情况下，是否允许界面被上拉
            xrf_comment.setPinnedTime(1000);
            xrf_comment.setMoveForHorizontal(true);
            xrf_comment.setCustomFooterView(new CustomerFooter(getActivity()));
            //设置当非RecyclerView上拉加载完成以后的回弹时间
            // xrf_czrecord.setScrollBackDuration(300);
        }

        @Override
        public void fail(String msg) {

        }

        @Override
        public void connectFail() {

        }
    }
}
