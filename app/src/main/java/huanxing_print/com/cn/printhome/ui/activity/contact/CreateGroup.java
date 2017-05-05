package huanxing_print.com.cn.printhome.ui.activity.contact;

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
import huanxing_print.com.cn.printhome.model.contact.ContactInfo;
import huanxing_print.com.cn.printhome.model.contact.GroupInfo;
import huanxing_print.com.cn.printhome.ui.adapter.ChooseGroupContactAdapter;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;
import huanxing_print.com.cn.printhome.util.contact.MyDecoration;

/**
 * Created by wanghao on 2017/5/5.
 */

public class CreateGroup extends BaseActivity implements View.OnClickListener, ChooseGroupContactAdapter.OnClickGroupInListener, ChooseGroupContactAdapter.OnChooseMemberListener {
    private static final int MAXCHOOSE = 4;
    private ArrayList<ContactInfo> contactInfos = new ArrayList<ContactInfo>();
    private Button btn_create;
    private RecyclerView recyclerView;
    private TextView tv_hint_member;
    private ChooseGroupContactAdapter adapter;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.activity_group_create);
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

        adapter = new ChooseGroupContactAdapter(this, contactInfos, MAXCHOOSE);
        recyclerView.setAdapter(adapter);
    }

    private void initData() {
        ContactInfo data01 = new ContactInfo();
        data01.setName("汪浩");

        ContactInfo data02 = new ContactInfo();
        data02.setName("陆成宋");

        ContactInfo data03 = new ContactInfo();
        data03.setName("123");

        ContactInfo data04 = new ContactInfo();
        data04.setName("汪浩01");

        ContactInfo data05 = new ContactInfo();
        data05.setName("陆成宋01");
        ContactInfo data06 = new ContactInfo();
        data06.setName("陆成宋02");
        ContactInfo data07 = new ContactInfo();
        data07.setName("陆成宋03");
        ContactInfo data08 = new ContactInfo();
        data08.setName("陆成宋04");
        ContactInfo data09 = new ContactInfo();
        data09.setName("陆成宋05");
        ContactInfo data10 = new ContactInfo();
        data10.setName("陆成宋06");
        ContactInfo data11 = new ContactInfo();
        data11.setName("陆成宋07");
        ContactInfo data12 = new ContactInfo();
        data12.setName("陆成宋08");
        contactInfos.add(data01);
        contactInfos.add(data02);
        contactInfos.add(data03);
        contactInfos.add(data04);
        contactInfos.add(data05);
        contactInfos.add(data06);
        contactInfos.add(data07);
        contactInfos.add(data08);
        contactInfos.add(data09);
        contactInfos.add(data10);
        contactInfos.add(data11);
        contactInfos.add(data12);

        adapter.modifyData(contactInfos);
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
                GroupInfo info = new GroupInfo();
                intent.putExtra("created", info);
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
    public void choose(ArrayList<ContactInfo> infos) {
        if (null != infos) {
            tv_hint_member.setText(String.format("已选择:%d人", infos.size()));
            btn_create.setText(String.format("确定(%d/%d)", infos.size(), MAXCHOOSE));
            if (infos.size() > 0) {
                btn_create.setEnabled(true);
            } else {
                btn_create.setEnabled(false);
            }
        }
    }
}
