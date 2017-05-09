package huanxing_print.com.cn.printhome.ui.activity.fragment;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;

import java.util.ArrayList;
import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseFragment;
import huanxing_print.com.cn.printhome.log.Logger;
import huanxing_print.com.cn.printhome.ui.activity.yinxin.ApprovalInformActivity;
import huanxing_print.com.cn.printhome.ui.adapter.ChatRecylerAdapter;
import huanxing_print.com.cn.printhome.view.RecyclerViewDivider;

public class ChatFragment extends BaseFragment implements OnClickListener {

    private Context mContext;
    private RecyclerView chatRecView;

    private ChatRecylerAdapter usedPrinterRcAdapter;

    @Override
    protected void init() {
        Logger.i("init");
        mContext = getActivity();
        chatRecView = (RecyclerView) findViewById(R.id.chatRecView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        chatRecView.setLayoutManager(mLayoutManager);
        chatRecView.setHasFixedSize(true);
        chatRecView.setItemAnimator(new DefaultItemAnimator());
        chatRecView.addItemDecoration(new RecyclerViewDivider(mContext, LinearLayoutManager.VERTICAL, 1,
                ContextCompat.getColor(mContext, R.color.bc_gray)));
        List<ChatRecylerAdapter.ChatInfo> chatInfoList = new ArrayList<ChatRecylerAdapter.ChatInfo>();
        chatInfoList.add(new ChatRecylerAdapter.ChatInfo());
        chatInfoList.add(new ChatRecylerAdapter.ChatInfo());
        chatInfoList.add(new ChatRecylerAdapter.ChatInfo());
        chatInfoList.add(new ChatRecylerAdapter.ChatInfo());
        usedPrinterRcAdapter = new ChatRecylerAdapter(chatInfoList);
        usedPrinterRcAdapter.setOnItemClickListener(new ChatRecylerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ApprovalInformActivity.start(mContext, null);
            }
        });
        chatRecView.setAdapter(usedPrinterRcAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected int getContextView() {
        return R.layout.frag_chat;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addImg:

                break;
            default:
                break;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
