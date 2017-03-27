package huanxing_print.com.cn.printhome.ui.activity.my;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.log.Logger;
import huanxing_print.com.cn.printhome.model.my.PrinterInfoBean;
import huanxing_print.com.cn.printhome.net.callback.my.SetPrinterInfoCallback;
import huanxing_print.com.cn.printhome.net.request.my.SetDefaultPrinterRequest;
import huanxing_print.com.cn.printhome.ui.adapter.SetDefaultPrinterListAdapter;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;
import static huanxing_print.com.cn.printhome.R.id.ll_back;

/**
 * Created by Administrator on 2017/3/17 0017.
 */

public class SetDefaultPrinterActivity extends BaseActivity implements View.OnClickListener {

    private ListView lv_print;
    private SetDefaultPrinterListAdapter adapter;
    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBarGreen(this);
        setContentView(R.layout.activty_set_default_printer);
        initView();
        initData();
        setListener();
    }


    private void initView() {
        lv_print = (ListView) findViewById(R.id.lv_print);

    }
    private void initData() {
        DialogUtils.showProgressDialog(getSelfActivity(), "正在加载数据").show();
        SetDefaultPrinterRequest.getPrinterList(getSelfActivity(),
                baseApplication.getLoginToken(), printCallback);

    }
    private void setListener() {
        findViewById(ll_back).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case ll_back:
                finishCurrentActivity();
                break;
        }
    }

    //登录接口返回
    private SetPrinterInfoCallback printCallback = new SetPrinterInfoCallback() {

        @Override
        public void success(List<PrinterInfoBean> list) {

            DialogUtils.closeProgressDialog();
            if (null!=list&&list.size()>0) {
                adapter = new SetDefaultPrinterListAdapter(getSelfActivity(),
                        baseApplication.getLoginToken(),list);
                lv_print.setAdapter(adapter);
            }
        }

        @Override
        public void fail(String msg) {
            DialogUtils.closeProgressDialog();
            toast(msg);

        }

        @Override
        public void connectFail() {
            Logger.d("connectFail:");
            DialogUtils.closeProgressDialog();
            toastConnectFail();
        }

    };
}
