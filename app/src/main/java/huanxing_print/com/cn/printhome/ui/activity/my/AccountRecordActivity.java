package huanxing_print.com.cn.printhome.ui.activity.my;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.model.my.ChongZhiRecordBean;
import huanxing_print.com.cn.printhome.net.callback.my.ChongZhiRecordCallBack;
import huanxing_print.com.cn.printhome.net.request.my.ChongZhiRecordRequest;
import huanxing_print.com.cn.printhome.ui.adapter.AccountRecordAdapter;
import huanxing_print.com.cn.printhome.util.CommonUtils;

/**
 * Created by Administrator on 2017/3/23 0023.
 */

public class AccountRecordActivity extends BaseActivity implements View.OnClickListener{

    private LinearLayout ll_back;
    private RecyclerView rv_account_record;
    private int pageNum;
    private AccountRecordAdapter adapter;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBarGreen(this);
        setContentView(R.layout.activity_account_record);
        initView();
        initData();
        setListener();
    }
    private void initData() {
        //获取充值记录
        ChongZhiRecordRequest.getCzRecord(getSelfActivity(),1,new MyChongzhiRecordCallBack());

        rv_account_record.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AccountRecordAdapter(getSelfActivity(),null);
        rv_account_record.setAdapter(adapter);
    }

    private void initView() {
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        rv_account_record = (RecyclerView) findViewById(R.id.rv_account_record);
    }
    private void setListener() {
        ll_back.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_back:
                finish();
                break;
        }
    }

    private class MyChongzhiRecordCallBack extends ChongZhiRecordCallBack{

        @Override
        public void success(String msg, ChongZhiRecordBean bean) {
            toast("请求成功");
        }

        @Override
        public void fail(String msg) {

        }

        @Override
        public void connectFail() {

        }
    }
}
