package huanxing_print.com.cn.printhome.ui.activity.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.View;
import android.view.View.OnClickListener;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseFragment;
import huanxing_print.com.cn.printhome.log.Logger;
import huanxing_print.com.cn.printhome.ui.activity.chat.ChatActivity;
import huanxing_print.com.cn.printhome.ui.adapter.ChatRecylerAdapter;
import huanxing_print.com.cn.printhome.view.RecyclerViewDivider;

public class ChatFragment extends BaseFragment implements OnClickListener {

    private Context mContext;
    private RecyclerView chatRecView;
    protected List<EMConversation> conversationList = new ArrayList<EMConversation>();

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
        ChatRecylerAdapter usedPrinterRcAdapter = new ChatRecylerAdapter(chatInfoList);
        usedPrinterRcAdapter.setOnItemClickListener(new ChatRecylerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent in = new Intent(getActivity(), ChatActivity.class);
                if (position == 0) {
                    //单聊
                    in.putExtra("type", 2);
                } else {
                    //群聊
                    in.putExtra("type", 1);
                }
                startActivity(in);
            }
        });
        chatRecView.setAdapter(usedPrinterRcAdapter);

        conversationList = loadConversationList();

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

    protected List<EMConversation> loadConversationList() {
        // get all conversations
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
        /**
         * lastMsgTime will change if there is new message during sorting
         * so use synchronized to make sure timestamp of last message won't change.
         */
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
                }
            }
        }
        try {
            // Internal is TimSort algorithm, has bug
            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<EMConversation> list = new ArrayList<EMConversation>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }
        return list;
    }

    /**
     * sort conversations according time stamp of last message
     *
     * @param conversationList
     */
    private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
            @Override
            public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

                if (con1.first.equals(con2.first)) {
                    return 0;
                } else if (con2.first.longValue() > con1.first.longValue()) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
    }

}
