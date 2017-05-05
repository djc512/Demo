package huanxing_print.com.cn.printhome.ui.activity.fragment;


import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;

import java.util.ArrayList;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseFragment;
import huanxing_print.com.cn.printhome.model.contact.ContactInfo;
import huanxing_print.com.cn.printhome.ui.activity.contact.AddContactActivity;
import huanxing_print.com.cn.printhome.ui.activity.contact.AddressBookActivity;
import huanxing_print.com.cn.printhome.ui.activity.contact.GroupActivity;
import huanxing_print.com.cn.printhome.ui.activity.contact.NewFriendActivity;
import huanxing_print.com.cn.printhome.ui.adapter.ContactsItemAdapter;
import huanxing_print.com.cn.printhome.util.ToastUtil;
import huanxing_print.com.cn.printhome.util.contact.MyDecoration;
import huanxing_print.com.cn.printhome.view.IndexSideBar;

public class ContantsFragment extends BaseFragment implements
        OnClickListener, IndexSideBar.OnTouchLetterListener,ContactsItemAdapter.OnTypeItemClickerListener {

    private IndexSideBar indexSideBar;
    private RecyclerView contactsView;
    private ArrayList<ContactInfo> contactInfos = new ArrayList<ContactInfo>();
    private ContactsItemAdapter adapter;
    private LinearLayoutManager layoutManager;

    @Override
    protected void init() {
        initData();
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    protected int getContextView() {
        return R.layout.frag_find;
    }

    private void initView() {
        indexSideBar = (IndexSideBar) findViewById(R.id.contacts_sidebar);
        indexSideBar.setOnTouchLetterListener(this);
        contactsView = (RecyclerView) findViewById(R.id.contactsView);
        layoutManager = new LinearLayoutManager(getActivity());
        contactsView.setHasFixedSize(true);
        contactsView.setLayoutManager(layoutManager);
        contactsView.addItemDecoration(new MyDecoration(getActivity(),MyDecoration.HORIZONTAL_LIST));

        adapter = new ContactsItemAdapter(getActivity(), contactInfos);
        adapter.setTypeItemClickerListener(this);
        contactsView.setAdapter(adapter);

        findViewById(R.id.iv_add_contact).setOnClickListener(this);
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
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
/*        case R.id.ll_communication:

             break;*/
            case R.id.iv_add_contact:
                startActivity(AddContactActivity.class);
                break;
            default:
                break;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onTouchingLetterListener(String letter) {
        if(null != adapter) {
            int position = adapter.getScrollPosition(letter);
            layoutManager.scrollToPositionWithOffset(position, 0);
        }
    }

    @Override
    public void onTouchedLetterListener() {

    }

    @Override
    public void newFriendLister() {
        ToastUtil.doToast(getActivity(),"newFriendLister");
        startActivity(NewFriendActivity.class);
    }

    @Override
    public void addressBookListener() {
        ToastUtil.doToast(getActivity(),"addressBookListener");
        startActivity(AddressBookActivity.class);
    }

    @Override
    public void groupListener() {
        startActivity(GroupActivity.class);
        ToastUtil.doToast(getActivity(),"groupListener");
    }

    private void startActivity(Class cls) {
        Intent intent = new Intent(getActivity(), cls);
        startActivity(intent);
    }
}
