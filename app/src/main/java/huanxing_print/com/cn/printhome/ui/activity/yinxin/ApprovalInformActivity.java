package huanxing_print.com.cn.printhome.ui.activity.yinxin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.ui.activity.print.BasePrintActivity;
import huanxing_print.com.cn.printhome.ui.adapter.ApprovalInfoRecylerAdapter;


public class ApprovalInformActivity extends BasePrintActivity {

    private RecyclerView infoRecView;
    private ApprovalInfoRecylerAdapter approvalInfoRecylerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    protected void setContent() {
        setContentView(R.layout.activity_approval_inform);
    }

    private void initView() {
        initTitleBar("审批通知");
        infoRecView = (RecyclerView) findViewById(R.id.infoRecView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        infoRecView.setLayoutManager(mLayoutManager);
        infoRecView.setHasFixedSize(true);
        infoRecView.setItemAnimator(new DefaultItemAnimator());
        List<ApprovalInfoRecylerAdapter.ApprovalInfo> approvalInfos = new ArrayList<ApprovalInfoRecylerAdapter.ApprovalInfo>();
        approvalInfos.add(new ApprovalInfoRecylerAdapter.ApprovalInfo());
        approvalInfos.add(new ApprovalInfoRecylerAdapter.ApprovalInfo());
        approvalInfos.add(new ApprovalInfoRecylerAdapter.ApprovalInfo());
        approvalInfos.add(new ApprovalInfoRecylerAdapter.ApprovalInfo());
        approvalInfoRecylerAdapter = new ApprovalInfoRecylerAdapter(approvalInfos);
        approvalInfoRecylerAdapter.setOnItemClickListener(new ApprovalInfoRecylerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
            }
        });
        infoRecView.setAdapter(approvalInfoRecylerAdapter);
    }

    public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent(context, ApprovalInformActivity.class);
        context.startActivity(intent);
    }
}
