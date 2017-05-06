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
        data01.setIconPath("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494660151&di=fc28cd4cd681bb1d70df6ff6654791ff&imgtype=jpg&er=1&src=http%3A%2F%2Fimgsrc.baidu.com%2Fforum%2Fw%253D580%2Fsign%3D8c03c118ca8065387beaa41ba7dda115%2Fc17fc0bf6c81800a06c8cd58b13533fa828b4759.jpg");

        ContactInfo data02 = new ContactInfo();
        data02.setName("陆成宋");
        data02.setIconPath("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494065434200&di=7c53b18639aa82a8a58a296b9502d4ee&imgtype=0&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fzhidao%2Fwh%253D450%252C600%2Fsign%3D7048a12f9e16fdfad839ceea81bfa062%2F6a63f6246b600c3350e384cc194c510fd9f9a118.jpg");

        ContactInfo data03 = new ContactInfo();
        data03.setName("123");
        data03.setIconPath("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494065546496&di=a861d2debdefd088f50efa05393043dc&imgtype=jpg&src=http%3A%2F%2Fimg3.imgtn.bdimg.com%2Fit%2Fu%3D893187487%2C386198762%26fm%3D214%26gp%3D0.jpg");

        ContactInfo data04 = new ContactInfo();
        data04.setName("汪浩01");
        data04.setIconPath("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494065434199&di=85b82a89a5b9cb3403033e90dc2dc2a1&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fforum%2Fw%253D580%2Fsign%3Dddf0103f252dd42a5f0901a3333a5b2f%2Fa4a8805494eef01f30f35d93e0fe9925bd317da3.jpg");

        ContactInfo data05 = new ContactInfo();
        data05.setName("陆成宋01");
        data05.setIconPath("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494065434198&di=83e45ffe0f07e6336bbbd1cdc284e9a5&imgtype=0&src=http%3A%2F%2Fwenwen.soso.com%2Fp%2F20140404%2F20140404111309-1793362574.jpg");

        ContactInfo data06 = new ContactInfo();
        data06.setName("陆成宋02");
        data06.setIconPath("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494065730069&di=12540a26599230b583e0bf4f477cc8d7&imgtype=0&src=http%3A%2F%2Fwww.qxjlm.com%2Ftupians%2Fbd16072941.jpg");

        ContactInfo data07 = new ContactInfo();
        data07.setName("陆成宋03");
        data07.setIconPath("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494065434198&di=2271e06f1c39be89ce18f3ab9746c7d1&imgtype=0&src=http%3A%2F%2Fwww.lsswgs.com%2Fqqwebhimgs%2Fuploads%2Fbd24449651.jpg");

        ContactInfo data08 = new ContactInfo();
        data08.setName("陆成宋04");
        data08.setIconPath("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494065662993&di=1d4fabe377e894277d005e17818ea64b&imgtype=jpg&src=http%3A%2F%2Fimg3.imgtn.bdimg.com%2Fit%2Fu%3D575752541%2C1102211525%26fm%3D214%26gp%3D0.jpg");

        ContactInfo data09 = new ContactInfo();
        data09.setName("陆成宋05");
        data09.setIconPath("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494065434197&di=9043dfda20c52ecfa1676e3999658669&imgtype=0&src=http%3A%2F%2Fwenwen.soso.com%2Fp%2F20111030%2F20111030173922-1579981974.jpg");

        ContactInfo data10 = new ContactInfo();
        data10.setName("陆成宋06");
        data10.setIconPath("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494065434197&di=af5f1a36863ac751b71b1e167e57a8e7&imgtype=0&src=http%3A%2F%2Fwenwen.soso.com%2Fp%2F20131201%2F20131201114242-1190841548.jpg");

        ContactInfo data11 = new ContactInfo();
        data11.setName("陆成宋07");
        data11.setIconPath("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3609186921,1453144627&fm=23&gp=0.jpg");

        ContactInfo data12 = new ContactInfo();
        data12.setName("陆成宋08");
        data12.setIconPath("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494065730070&di=b4ae9186fcb1795ee5bec334ec893997&imgtype=0&src=http%3A%2F%2Fwww.qxjlm.com%2Ftupians%2Fbd13706313.jpg");
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
            tv_hint_member.setText(String.format(getString(R.string.hint_choose_members), infos.size()));
            btn_create.setText(String.format(getString(R.string.btn_hint_members), infos.size(), MAXCHOOSE));
            if (infos.size() > 0) {
                btn_create.setEnabled(true);
            } else {
                btn_create.setEnabled(false);
            }
        }
    }
}
