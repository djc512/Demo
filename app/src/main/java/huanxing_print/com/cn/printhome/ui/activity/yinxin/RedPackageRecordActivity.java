package huanxing_print.com.cn.printhome.ui.activity.yinxin;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.view.WindowManager;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.model.yinxin.RedPackageBean;
import huanxing_print.com.cn.printhome.ui.adapter.RedPackageAdapter;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.view.RecyclerViewDivider;

public class RedPackageRecordActivity extends BaseActivity {

    private RecyclerView recordRecView;

    private RedPackageAdapter redPackageAdapter;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStatusBar();
        CommonUtils.initSystemBar(this, R.color.red_package_red);
        setContentView(R.layout.activity_red_package_record);
        initView();
    }


    protected void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams
                    .FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams
                    .FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    private void initView() {
        recordRecView = (RecyclerView) findViewById(R.id.recordRecView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recordRecView.setLayoutManager(mLayoutManager);
        recordRecView.setHasFixedSize(true);
        recordRecView.setItemAnimator(new DefaultItemAnimator());
        recordRecView.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.VERTICAL, 1, ContextCompat.getColor(this, R.color.devide_gray)));
        RedPackageBean date = new RedPackageBean();
        redPackageAdapter = new RedPackageAdapter();
        recordRecView.setAdapter(redPackageAdapter);
    }
}
