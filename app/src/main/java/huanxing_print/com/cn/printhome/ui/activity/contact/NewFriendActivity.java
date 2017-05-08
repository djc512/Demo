package huanxing_print.com.cn.printhome.ui.activity.contact;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.constant.ConFig;
import huanxing_print.com.cn.printhome.model.contact.NewFriendInfo;
import huanxing_print.com.cn.printhome.net.callback.NullCallback;
import huanxing_print.com.cn.printhome.net.callback.contact.NewFriendCallback;
import huanxing_print.com.cn.printhome.net.request.contact.FriendManagerRequest;
import huanxing_print.com.cn.printhome.ui.adapter.NewFriendRecycelAdapter;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.SharedPreferencesUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;
import huanxing_print.com.cn.printhome.util.contact.MyDecoration;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;

/**
 * Created by wanghao on 2017/5/4.
 */

public class NewFriendActivity extends BaseActivity implements View.OnClickListener,NewFriendRecycelAdapter.OnAddItemClickListener{
    private RecyclerView friendRecycler;
    private NewFriendRecycelAdapter adapter;
    private ArrayList<NewFriendInfo> friendInfos = new ArrayList<NewFriendInfo>();
    private String token;
    private NewFriendInfo clickOperationInfo;
    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.activity_contact_new_friend);
        token = SharedPreferencesUtils.getShareString(this, ConFig.SHAREDPREFERENCES_NAME,
                "loginToken");
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
        adapter.setOnAddItemClickListener(this);
        friendRecycler.setAdapter(adapter);
    }

    private void initData() {
        DialogUtils.showProgressDialog(this,"加载中");
        FriendManagerRequest.queryNewFriendList(this, token, newFriendCallback);
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
    public void onAddressBookClick() {
        ToastUtil.doToast(this,"从手机联系人添加");
        startActivity(AddByAddressBookActivity.class);
    }

    @Override
    public void onItemNewFriendPassClick(NewFriendInfo newFriendInfo) {
        clickOperationInfo = newFriendInfo;
        DialogUtils.showProgressDialog(this, "操作中");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("isPass", 1);
        params.put("memberId", newFriendInfo.getMemberId());
        FriendManagerRequest.operationNewFriend(this, token, params, nullCallback);
    }

    NewFriendCallback newFriendCallback = new NewFriendCallback() {
        @Override
        public void success(String msg, ArrayList<NewFriendInfo> newFriendInfos) {
            DialogUtils.closeProgressDialog();
            if(null != newFriendInfos && newFriendInfos.size() > 0) {
//                adapter.updateData(newFriendInfos);
                //假数据
                friendInfos.addAll(data());
                adapter.updateData(friendInfos);
            }
        }

        @Override
        public void fail(String msg) {
            DialogUtils.closeProgressDialog();
            ToastUtil.doToast(NewFriendActivity.this,msg + " -- 假数据");
            //假数据
            friendInfos.addAll(data());
            adapter.updateData(friendInfos);
        }

        @Override
        public void connectFail() {
            DialogUtils.closeProgressDialog();
            toastConnectFail();
            //假数据
            friendInfos.addAll(data());
            adapter.updateData(friendInfos);
        }
    };

    NullCallback nullCallback = new NullCallback() {
        @Override
        public void success(String msg) {
            DialogUtils.closeProgressDialog();
            clickOperationInfo.setType("1");
            adapter.updateData(friendInfos);
        }

        @Override
        public void fail(String msg) {
            DialogUtils.closeProgressDialog();
            ToastUtil.doToast(NewFriendActivity.this, msg + " -- 假数据");
            //假的
            clickOperationInfo.setType("1");
            adapter.updateData(friendInfos);
        }

        @Override
        public void connectFail() {
            DialogUtils.closeProgressDialog();
            toastConnectFail();
            //假的
            clickOperationInfo.setType("1");
            adapter.updateData(friendInfos);
        }
    };

    /**
     * 假数据
     */
    private ArrayList<NewFriendInfo> data() {
        ArrayList<NewFriendInfo> infos = new ArrayList<NewFriendInfo>();
        NewFriendInfo info_01 = new NewFriendInfo();
        info_01.setMemberName("汪浩");
        info_01.setNote("添加好友");
        info_01.setMemberId("123");
        info_01.setMemebrUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494660151&di=fc28cd4cd681bb1d70df6ff6654791ff&imgtype=jpg&er=1&src=http%3A%2F%2Fimgsrc.baidu.com%2Fforum%2Fw%253D580%2Fsign%3D8c03c118ca8065387beaa41ba7dda115%2Fc17fc0bf6c81800a06c8cd58b13533fa828b4759.jpg");
        info_01.setType("0");

        NewFriendInfo info_02 = new NewFriendInfo();
        info_02.setMemberName("汪浩01");
        info_02.setNote("添加好友01");
        info_02.setMemberId("234");
        info_02.setMemebrUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494065434200&di=7c53b18639aa82a8a58a296b9502d4ee&imgtype=0&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fzhidao%2Fwh%253D450%252C600%2Fsign%3D7048a12f9e16fdfad839ceea81bfa062%2F6a63f6246b600c3350e384cc194c510fd9f9a118.jpg");
        info_02.setType("1");

        NewFriendInfo info_03 = new NewFriendInfo();
        info_03.setMemberName("汪浩01");
        info_03.setNote("添加好友01");
        info_03.setMemebrUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494065546496&di=a861d2debdefd088f50efa05393043dc&imgtype=jpg&src=http%3A%2F%2Fimg3.imgtn.bdimg.com%2Fit%2Fu%3D893187487%2C386198762%26fm%3D214%26gp%3D0.jpg");
        info_03.setType("2");
        infos.add(info_01);
        infos.add(info_02);
        infos.add(info_03);

        return infos;
    }
}
