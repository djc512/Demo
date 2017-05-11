package huanxing_print.com.cn.printhome.ui.activity.contact;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.model.contact.GroupMember;
import huanxing_print.com.cn.printhome.model.contact.GroupMessageInfo;
import huanxing_print.com.cn.printhome.net.callback.NullCallback;
import huanxing_print.com.cn.printhome.net.request.contact.GroupManagerRequest;
import huanxing_print.com.cn.printhome.ui.adapter.QunMemberListAdapter;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.contact.MyDecoration;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;

/**
 * Created by Administrator on 2017/5/10 0010.
 */

public class GroupOwnerTransferActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout ll_back;
    private RecyclerView recyclerView;
    private QunMemberListAdapter adapter;
    private ArrayList<GroupMember> groupMembers;
    private GroupMessageInfo messageInfo;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.activity_groupowner_transfer);
        EventBus.getDefault().register(getSelfActivity());
        initData();
        initView();
        setListener();
    }

    private void initView() {
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        recyclerView = (RecyclerView) findViewById(R.id.groupMemberView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new MyDecoration(this, MyDecoration.HORIZONTAL_LIST));

        adapter = new QunMemberListAdapter(getSelfActivity(), groupMembers);
        recyclerView.setAdapter(adapter);
    }

    private void initData() {
        messageInfo = getIntent().getParcelableExtra("qunlist");
        groupMembers = messageInfo.getGroupMembers();
    }

    private void setListener() {
        ll_back.setOnClickListener(this);
        adapter.setTypeItemClickerListener(new QunMemberListAdapter.OnTypeItemClickerListener() {
            @Override
            public void contactClick(final GroupMember info) {
                DialogUtils.showQunTransferDialog(getSelfActivity(), "确定选择"+info.getMemberName()+"为新群主，您将主动放弃群主身份",
                        new DialogUtils.QunOwnerTransferDialogCallBack() {
                    @Override
                 public void transfer() {
                        Map<String, Object> params = new HashMap<String, Object>();
                        params.put("groupId",messageInfo.getGroupId());
                        params.put("memberId",info.getMemberId());
                        GroupManagerRequest.transfer(getSelfActivity(), baseApplication.getLoginToken(), params, new NullCallback() {
                            @Override
                            public void success(String msg) {
                                toast("发送成功:"+msg);
                                setResult(RESULT_OK);
                                finishCurrentActivity();
                            }

                            @Override
                            public void fail(String msg) {

                            }

                            @Override
                            public void connectFail() {

                            }
                        });
                    }
                }).show();
            }
        });
    }

    /**
     * 提示
     * @param
     */
    private void showTransferDialog(final String memberName) {
        DialogUtils.showQunTransferDialog(getSelfActivity(), memberName, new DialogUtils.QunOwnerTransferDialogCallBack() {
            @Override
            public void transfer() {
                Intent intent = new Intent();
                //TODO 发送后台修改的群主
                intent.putExtra("qunowner",memberName);
                setResult(RESULT_OK,intent);
                finishCurrentActivity();
            }
        }).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finishCurrentActivity();
                break;
        }
    }


}
