package huanxing_print.com.cn.printhome.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.WindowManager;

import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.log.Logger;
import huanxing_print.com.cn.printhome.model.print.GroupResp;
import huanxing_print.com.cn.printhome.ui.adapter.GroupRecylerAdapter;

/**
 * Created by LGH on 2017/5/12.
 */

public class GroupDialog extends Dialog {

    private Context context;
    private RecyclerView mRcList;
    private GroupRecylerAdapter.OnItemClickListener onItemClickListener;

    private GroupRecylerAdapter mAdapter;
    private List<GroupResp.Group> groupList;

    public GroupDialog(Context context, int themeResId) {
        super(context);
        this.context = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.i("onCreateonCreate");

        getWindow().setContentView(R.layout.dialog_pay_qun1);
//        setContentView(R.layout.dialog_pay_qun1);

        mRcList = (RecyclerView) findViewById(R.id.groupRecView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        mRcList.setLayoutManager(mLayoutManager);
        mRcList.setHasFixedSize(true);
        mRcList.setItemAnimator(new DefaultItemAnimator());
        GroupRecylerAdapter mAdapter = new GroupRecylerAdapter(groupList);
//        mRcList.setAdapter(mAdapter);
//        mRcList.addItemDecoration(new RecyclerViewDivider(context, LinearLayoutManager.VERTICAL, 1, ContextCompat
//                .getColor(context, R.color.devide_gray)));
//        mAdapter.setOnItemClickListener(onItemClickListener);
//
//        TextView cancelTv = (TextView) findViewById(R.id.cancelTv);
//        cancelTv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                dismiss();
//            }
//        });
    }

    @Override
    public void show() {
        super.show();
        /**
         * 设置宽度全屏，要设置在show的后面
         */
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        getWindow().getDecorView().setPadding(0, 0, 0, 0);

        getWindow().setAttributes(layoutParams);

    }


    public void initView(List<GroupResp.Group> groupList, GroupRecylerAdapter.OnItemClickListener onItemClickListener) {
        this.groupList = groupList;
        this.onItemClickListener = onItemClickListener;
    }
}
