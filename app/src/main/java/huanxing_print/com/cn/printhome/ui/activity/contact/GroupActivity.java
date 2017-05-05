package huanxing_print.com.cn.printhome.ui.activity.contact;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.model.contact.ContactInfo;
import huanxing_print.com.cn.printhome.model.contact.GroupInfo;
import huanxing_print.com.cn.printhome.ui.adapter.GroupAdatper;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;
import huanxing_print.com.cn.printhome.util.contact.MyDecoration;

/**
 * Created by wanghao on 2017/5/5.
 */

public class GroupActivity extends BaseActivity implements View.OnClickListener {
    private static final int CREATE_GROUP = 1000;
    private RecyclerView recyclerView;
    private ArrayList<GroupInfo> groups = new ArrayList<GroupInfo>();
    private GroupAdatper adatper;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.activity_group);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.groupView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new MyDecoration(this, MyDecoration.HORIZONTAL_LIST));

        adatper = new GroupAdatper(this, groups);
        recyclerView.setAdapter(adatper);
    }

    private void initData() {
        GroupInfo group01 = new GroupInfo();
        group01.setGroupName("印家群");
        ArrayList<ContactInfo> members01 = new ArrayList<ContactInfo>();
        ContactInfo contact01 = new ContactInfo();
        contact01.setName("汪浩");
        ContactInfo contact02 = new ContactInfo();
        contact02.setName("陆成宋");
        members01.add(contact01);
        members01.add(contact02);
        group01.setMembers(members01);

        GroupInfo group02 = new GroupInfo();
        group02.setGroupName("途牛旅游");
        ArrayList<ContactInfo> members02 = new ArrayList<ContactInfo>();
        ContactInfo contact11 = new ContactInfo();
        contact11.setName("汪浩");
        ContactInfo contact12 = new ContactInfo();
        contact12.setName("陆成宋");
        ContactInfo contact13 = new ContactInfo();
        contact13.setName("杨健");
        members02.add(contact11);
        members02.add(contact12);
        members02.add(contact13);
        group02.setMembers(members02);

        groups.add(group01);
        groups.add(group02);

        adatper.modifyData(groups);
    }

    private void setListener() {
        findViewById(R.id.ll_back).setOnClickListener(this);
        findViewById(R.id.create_group).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finishCurrentActivity();
                break;
            case R.id.create_group:
                ToastUtil.doToast(this, "发起群组");
                Intent intent = new Intent(this, CreateGroup.class);
                startActivityForResult(intent, CREATE_GROUP);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CREATE_GROUP:
                if(resultCode == RESULT_OK) {
//                    GroupInfo info = data.getParcelableExtra("created");
                    GroupInfo info = new GroupInfo();
                    //假数据
                    info.setGroupName("途牛旅游01");
                    ArrayList<ContactInfo> members = new ArrayList<ContactInfo>();
                    ContactInfo contact11 = new ContactInfo();
                    contact11.setName("汪浩");
                    ContactInfo contact12 = new ContactInfo();
                    contact12.setName("陆成宋");
                    ContactInfo contact13 = new ContactInfo();
                    contact13.setName("杨健");
                    members.add(contact11);
                    members.add(contact12);
                    members.add(contact13);
                    info.setMembers(members);

                    groups.add(info);
                    adatper.modifyData(groups);
                }
                break;
        }
    }
}
