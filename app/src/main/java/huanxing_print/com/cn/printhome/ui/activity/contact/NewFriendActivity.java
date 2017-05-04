package huanxing_print.com.cn.printhome.ui.activity.contact;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.model.contact.NewFriendInfo;
import huanxing_print.com.cn.printhome.ui.adapter.NewFriendRecycelAdapter;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;
import huanxing_print.com.cn.printhome.util.contact.MyDecoration;

/**
 * Created by wanghao on 2017/5/4.
 */

public class NewFriendActivity extends BaseActivity implements View.OnClickListener,NewFriendRecycelAdapter.AddByAddressListener{
    private RecyclerView friendRecycler;
    private NewFriendRecycelAdapter adapter;
    private ArrayList<NewFriendInfo> friendInfos = new ArrayList<NewFriendInfo>();

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.activity_contact_new_friend);

        initView();
        initData();
        setListener();
    }

    private void initView() {
        friendRecycler = (RecyclerView) findViewById(R.id.friend_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        friendRecycler.setHasFixedSize(true);
        friendRecycler.setLayoutManager(layoutManager);
        friendRecycler.addItemDecoration(new MyDecoration(this,MyDecoration.VERTICAL_LIST));

        adapter = new NewFriendRecycelAdapter(this,friendInfos);
        adapter.setAddByAddressListener(this);
        friendRecycler.setAdapter(adapter);
    }

    private void initData() {
        NewFriendInfo info_01 = new NewFriendInfo();
        info_01.setName("汪浩");
        info_01.setVerification("添加好友");
        info_01.setFriendState(NewFriendInfo.STATE.NORMAL.ordinal());

        NewFriendInfo info_02 = new NewFriendInfo();
        info_02.setName("汪浩01");
        info_02.setVerification("添加好友01");
        info_02.setFriendState(NewFriendInfo.STATE.AGREE.ordinal());

        NewFriendInfo info_03 = new NewFriendInfo();
        info_03.setName("汪浩01");
        info_03.setVerification("添加好友01");
        info_03.setFriendState(NewFriendInfo.STATE.WAIT.ordinal());
        friendInfos.add(info_01);
        friendInfos.add(info_02);
        friendInfos.add(info_03);

        adapter.updateData(friendInfos);
    }

    private void setListener() {
        findViewById(R.id.ll_back).setOnClickListener(this);
        findViewById(R.id.add_friend).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finishCurrentActivity();
                break;
            case R.id.add_friend:
                startActivity(SearchYinJiaNumActivity.class);
                break;
        }
    }

    private void startActivity(Class cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    @Override
    public void onClick() {
        ToastUtil.doToast(this,"从手机联系人添加");
        startActivity(AddByAddressBookActivity.class);
    }
}
