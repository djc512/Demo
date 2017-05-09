package huanxing_print.com.cn.printhome.ui.activity.approval;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.constant.ConFig;
import huanxing_print.com.cn.printhome.model.contact.FriendInfo;
import huanxing_print.com.cn.printhome.net.callback.contact.MyFriendListCallback;
import huanxing_print.com.cn.printhome.net.request.contact.FriendManagerRequest;
import huanxing_print.com.cn.printhome.ui.adapter.ChooseGroupContactAdapter;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.SharedPreferencesUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;
import huanxing_print.com.cn.printhome.util.contact.MyDecoration;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;

/**
 * description: 选择审批人和抄送人
 * author LSW
 * date 2017/5/8 20:25
 * update 2017/5/8
 */
public class ChoosePeopleOfAddressActivity extends BaseActivity implements
        View.OnClickListener,
        ChooseGroupContactAdapter.OnClickGroupInListener,
        ChooseGroupContactAdapter.OnChooseMemberListener {

    private static final int MAXCHOOSE = 4;
    private Button btn_create;
    private RecyclerView recyclerView;
    private TextView tv_hint_member;
    private ChooseGroupContactAdapter adapter;
    private ArrayList<FriendInfo> friends = new ArrayList<FriendInfo>();
    private ArrayList<FriendInfo> chooseMembers;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.activity_choose_people_of_address);

        initView();
        initData();
        setListener();
    }

    private void initView() {
        btn_create = (Button) findViewById(R.id.btn_create);
        tv_hint_member = (TextView) findViewById(R.id.hint_member);

        recyclerView = (RecyclerView) findViewById(R.id.groupMemberView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new MyDecoration(this, MyDecoration.HORIZONTAL_LIST));

        adapter = new ChooseGroupContactAdapter(this, friends, MAXCHOOSE);
        recyclerView.setAdapter(adapter);
    }

    private void initData() {
        ArrayList<FriendInfo> friendInfos = getIntent().getParcelableArrayListExtra("friends");
        friends = friendInfos;
        adapter.modify(friends);

        //请求联系人数据
        String token = SharedPreferencesUtils.getShareString(getSelfActivity(), ConFig.SHAREDPREFERENCES_NAME,
                "loginToken");
        DialogUtils.showProgressDialog(getSelfActivity(), "加载中");
        FriendManagerRequest.queryFriendList(getSelfActivity(), token, myFriendListCallback);
    }

    private void setListener() {
        findViewById(R.id.ll_back).setOnClickListener(this);
        btn_create.setOnClickListener(this);
        adapter.setOnChooseMemberListener(this);
        adapter.setOnClickGroupInListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finishCurrentActivity();
                break;
            case R.id.btn_create:
                Intent intent = new Intent();
                intent.putExtra("FriendInfo", chooseMembers);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }

    @Override
    public void clickGroup() {
        ToastUtil.doToast(this, "选择已有群组");
    }

    @Override
    public void choose(ArrayList<FriendInfo> infos) {
        chooseMembers = infos;
        if (null != infos) {
            tv_hint_member.setText(String.format(getString(R.string.hint_choose_members), infos.size()));
            btn_create.setText(String.format(getString(R.string.btn_hint_members), infos.size(), MAXCHOOSE));
            if (infos.size() > 0) {
                btn_create.setEnabled(true);
            } else {
                btn_create.setEnabled(false);
            }
        }
    }

    MyFriendListCallback myFriendListCallback = new MyFriendListCallback() {
        @Override
        public void success(String msg, ArrayList<FriendInfo> friendInfos) {
            DialogUtils.closeProgressDialog();
            if (null != friendInfos && friendInfos.size() > 0) {
//                friends = friendInfos;
//                adapter.modify(friendInfos);
                ToastUtil.doToast(getSelfActivity(), " -- 假数据");
            }
        }

        @Override
        public void fail(String msg) {
            DialogUtils.closeProgressDialog();
            ToastUtil.doToast(getSelfActivity(), msg + " -- 假数据");
        }

        @Override
        public void connectFail() {
            DialogUtils.closeProgressDialog();
            ToastUtil.doToast(getSelfActivity(), "网络连接超时");
        }
    };
}
