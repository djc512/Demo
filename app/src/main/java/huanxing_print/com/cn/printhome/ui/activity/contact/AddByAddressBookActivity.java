package huanxing_print.com.cn.printhome.ui.activity.contact;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.model.contact.PhoneContactInfo;
import huanxing_print.com.cn.printhome.ui.adapter.AddAddressBookAdapter;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.contact.MyDecoration;
import huanxing_print.com.cn.printhome.view.IndexSideBar;

/**
 * Created by wanghao on 2017/5/4.
 */

public class AddByAddressBookActivity extends BaseActivity implements View.OnClickListener, IndexSideBar.OnTouchLetterListener {
    private IndexSideBar indexSideBar;
    private RecyclerView contactsView;
    private ArrayList<PhoneContactInfo> contactInfos = new ArrayList<PhoneContactInfo>();
    private AddAddressBookAdapter adapter;
    private LinearLayoutManager layoutManager;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.activity_add_by_address_book);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        indexSideBar = (IndexSideBar) findViewById(R.id.contacts_sidebar);
        indexSideBar.setOnTouchLetterListener(this);
        contactsView = (RecyclerView) findViewById(R.id.contactsView);

        layoutManager = new LinearLayoutManager(this);
        contactsView.setHasFixedSize(true);
        contactsView.setLayoutManager(layoutManager);
        contactsView.addItemDecoration(new MyDecoration(this, MyDecoration.VERTICAL_LIST));

        adapter = new AddAddressBookAdapter(this, contactInfos);
        contactsView.setAdapter(adapter);
    }

    private void initData() {
        PhoneContactInfo info01 = new PhoneContactInfo();
        info01.setPhoneName("阿大");
        info01.setYjNum("6726376");
        info01.setFriendState(PhoneContactInfo.STATE.NOTFRIEND.ordinal());

        PhoneContactInfo info02 = new PhoneContactInfo();
        info02.setPhoneName("Aixy");
        info02.setYjNum("565778");
        info02.setFriendState(PhoneContactInfo.STATE.NOTFRIEND.ordinal());


        PhoneContactInfo info03 = new PhoneContactInfo();
        info03.setPhoneName("边宝玉");
        info03.setYjNum("00067676");
        info03.setFriendState(PhoneContactInfo.STATE.FRIEND.ordinal());


        PhoneContactInfo info04 = new PhoneContactInfo();
        info04.setPhoneName("宝龙");
        info04.setYjNum("8787878");
        info04.setFriendState(PhoneContactInfo.STATE.FRIEND.ordinal());

        contactInfos.add(info01);
        contactInfos.add(info02);
        contactInfos.add(info03);
        contactInfos.add(info04);

        adapter.modifyData(contactInfos);
    }

    private void setListener() {
        findViewById(R.id.ll_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finishCurrentActivity();
                break;
        }
    }

    @Override
    public void onTouchingLetterListener(String letter) {

    }

    @Override
    public void onTouchedLetterListener() {

    }
}
