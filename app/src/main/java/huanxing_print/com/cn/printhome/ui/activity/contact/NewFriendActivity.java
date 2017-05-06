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
        friendRecycler.addItemDecoration(new MyDecoration(this,MyDecoration.HORIZONTAL_LIST));

        adapter = new NewFriendRecycelAdapter(this,friendInfos);
        adapter.setAddByAddressListener(this);
        friendRecycler.setAdapter(adapter);
    }

    private void initData() {
        NewFriendInfo info_01 = new NewFriendInfo();
        info_01.setName("汪浩");
        info_01.setVerification("添加好友");
        info_01.setIconPath("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494660151&di=fc28cd4cd681bb1d70df6ff6654791ff&imgtype=jpg&er=1&src=http%3A%2F%2Fimgsrc.baidu.com%2Fforum%2Fw%253D580%2Fsign%3D8c03c118ca8065387beaa41ba7dda115%2Fc17fc0bf6c81800a06c8cd58b13533fa828b4759.jpg");
        info_01.setFriendState(NewFriendInfo.STATE.NORMAL.ordinal());

        NewFriendInfo info_02 = new NewFriendInfo();
        info_02.setName("汪浩01");
        info_02.setVerification("添加好友01");
        info_02.setIconPath("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494065434200&di=7c53b18639aa82a8a58a296b9502d4ee&imgtype=0&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fzhidao%2Fwh%253D450%252C600%2Fsign%3D7048a12f9e16fdfad839ceea81bfa062%2F6a63f6246b600c3350e384cc194c510fd9f9a118.jpg");
        info_02.setFriendState(NewFriendInfo.STATE.AGREE.ordinal());

        NewFriendInfo info_03 = new NewFriendInfo();
        info_03.setName("汪浩01");
        info_03.setVerification("添加好友01");
        info_03.setIconPath("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494065546496&di=a861d2debdefd088f50efa05393043dc&imgtype=jpg&src=http%3A%2F%2Fimg3.imgtn.bdimg.com%2Fit%2Fu%3D893187487%2C386198762%26fm%3D214%26gp%3D0.jpg");
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
