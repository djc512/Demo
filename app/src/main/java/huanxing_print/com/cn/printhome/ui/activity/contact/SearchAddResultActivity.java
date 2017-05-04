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
import huanxing_print.com.cn.printhome.ui.adapter.AddContactAdapter;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.contact.MyDecoration;

/**
 * Created by wanghao on 2017/5/3.
 * desc  : 印家号／手机号 查询结果
 */

public class SearchAddResultActivity extends BaseActivity implements View.OnClickListener,AddContactAdapter.OnItemSendListener {
    public static final int SEND = 1000;
    private ArrayList<ContactInfo> mResults = new ArrayList<ContactInfo>();
    private RecyclerView resultRecylerView;
    private AddContactAdapter addContactAdapter;
    private int sendPostion = -1;
    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CommonUtils.initSystemBar(this);
        setContentView(R.layout.activity_search_add_result);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        resultRecylerView = (RecyclerView) findViewById(R.id.search_result_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        resultRecylerView.setHasFixedSize(true);
        resultRecylerView.setLayoutManager(layoutManager);
        resultRecylerView.addItemDecoration(new MyDecoration(this,MyDecoration.VERTICAL_LIST));

        addContactAdapter = new AddContactAdapter(this,mResults);
        addContactAdapter.setOnItemSendListener(this);
        resultRecylerView.setAdapter(addContactAdapter);
    }

    private void initData() {
        ArrayList<ContactInfo> results = getIntent().getParcelableArrayListExtra("search result");
        mResults.addAll(results);
        addContactAdapter.updateContacts(mResults);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SEND && resultCode == RESULT_OK) {
            modifyData();
        }
    }

    @Override
    public void send(int poisition) {
        sendPostion = poisition;
        Intent intent = new Intent(this, AddVerificationActivity.class);
        startActivityForResult(intent, SEND);
    }

    private void modifyData() {
        ContactInfo contactInfo = mResults.get(sendPostion);
        contactInfo.setAddRequest(true);
        addContactAdapter.updateContacts(mResults);
    }
}
