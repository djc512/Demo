package huanxing_print.com.cn.printhome.ui.activity.contact;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.ui.adapter.ContactFileAdapter;

/**
 * Created by Administrator on 2017/5/10 0010.
 */

public class ContactFileActivity extends BaseActivity implements View.OnClickListener {

    private ImageView iv_back;
    private LinearLayout ll_back;
    private RecyclerView rv_file;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_file);
        initView();
        initData();
        setListener();
    }

    private void initData() {
        ContactFileAdapter adapter = new ContactFileAdapter(getSelfActivity());
        rv_file.setLayoutManager(new GridLayoutManager(getSelfActivity(),3));
        rv_file.setAdapter(adapter);
    }

    private void setListener() {
        iv_back.setOnClickListener(this);
    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        rv_file = (RecyclerView) findViewById(R.id.rv_file);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finishCurrentActivity();
                break;
        }
    }
}
