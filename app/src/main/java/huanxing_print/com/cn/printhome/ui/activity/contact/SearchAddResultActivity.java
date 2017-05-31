package huanxing_print.com.cn.printhome.ui.activity.contact;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.model.contact.FriendSearchInfo;
import huanxing_print.com.cn.printhome.ui.adapter.AddContactAdapter;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.contact.MyDecoration;

/**
 * Created by wanghao on 2017/5/3.
 * desc  : 印家号／手机号 查询结果
 */

public class SearchAddResultActivity extends BaseActivity implements View.OnClickListener,AddContactAdapter.OnItemSendListener {
    private ArrayList<FriendSearchInfo> mResults = new ArrayList<FriendSearchInfo>();
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
        setContentView(R.layout.activity_search_add_result);
        CommonUtils.initSystemBar(this);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        resultRecylerView = (RecyclerView) findViewById(R.id.search_result_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        resultRecylerView.setHasFixedSize(true);
        resultRecylerView.setLayoutManager(layoutManager);
        resultRecylerView.addItemDecoration(new MyDecoration(this,MyDecoration.HORIZONTAL_LIST));

        addContactAdapter = new AddContactAdapter(this,mResults);
        addContactAdapter.setOnItemSendListener(this);
        resultRecylerView.setAdapter(addContactAdapter);
    }

    private void initData() {
        ArrayList<FriendSearchInfo> results = getIntent().getParcelableArrayListExtra("search result");
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
    public void send(int position) {
        sendPostion = position;
        FriendSearchInfo info = mResults.get(sendPostion);
        Intent intent = new Intent(this, AddVerificationActivity.class);
        intent.putExtra("verification", info);
        startActivity(intent);
    }
}
