package huanxing_print.com.cn.printhome.ui.activity.approval;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.ui.adapter.MySponsorListAdapter;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.OnItemClickListener;

/**
 * description: 我发起的列表
 * author LSW
 * date 2017/5/6 11:21
 * update 2017/5/6
 */
public class MySponsorListActivity extends BaseActivity {

    private RecyclerView recycle_my_list;
    private MySponsorListAdapter listAdapter;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 改变状态栏的颜色使其与APP风格一体化
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.activity_my_sponsor_list);

        init();
        functionModule();
    }


    private void init() {
        recycle_my_list = (RecyclerView) findViewById(R.id.recycle_my_list);
        recycle_my_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        listAdapter = new MySponsorListAdapter();
        recycle_my_list.setAdapter(listAdapter);


    }

    private void functionModule() {
        listAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //查看我的列表详情
               // ToastUtil.doToast(MySponsorListActivity.this, "查看列表详情" + position);
                Intent intent = new Intent(MySponsorListActivity.this,ApprovalBuyAddOrRemoveActivity.class);
                intent.putExtra("what","3");
                startActivity(intent);

            }
        });

        //返回
        View view = findViewById(R.id.back);
        view.findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
