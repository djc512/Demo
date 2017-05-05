package huanxing_print.com.cn.printhome.ui.activity.contact;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.model.contact.PhoneContactInfo;
import huanxing_print.com.cn.printhome.ui.adapter.AddAddressBookAdapter;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.contact.GetContactsUtils;
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
        contactsView.addItemDecoration(new MyDecoration(this, MyDecoration.HORIZONTAL_LIST));

        adapter = new AddAddressBookAdapter(this, contactInfos);
        contactsView.setAdapter(adapter);
    }

    private void initData() {
        HandlerThread queryThread = new HandlerThread("query_contact");
        queryThread.start();
        Handler handler = new Handler(queryThread.getLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                GetContactsUtils contactsUtils = new GetContactsUtils(AddByAddressBookActivity.this);
                contactInfos = (ArrayList<PhoneContactInfo>) contactsUtils.getSystemContactInfos();

                adapter.modifyData(contactInfos);
                return false;
            }
        });
        handler.sendEmptyMessage(0);
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
        if (null != adapter) {
            int position = adapter.getScrollPosition(letter);
            layoutManager.scrollToPositionWithOffset(position, 0);
        }
    }

    @Override
    public void onTouchedLetterListener() {

    }
}
