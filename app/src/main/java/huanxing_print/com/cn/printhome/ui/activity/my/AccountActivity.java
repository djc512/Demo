package huanxing_print.com.cn.printhome.ui.activity.my;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.model.my.ChongZhiBean;
import huanxing_print.com.cn.printhome.net.callback.my.ChongzhiCallBack;
import huanxing_print.com.cn.printhome.net.request.my.ChongzhiRequest;
import huanxing_print.com.cn.printhome.ui.adapter.AccountCZAdapter;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;

/**
 * Created by Administrator on 2017/3/17 0017.
 */

public class AccountActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_money;
    private Button btn_chongzhi;
    private Dialog dialog;
    private LinearLayout ll_back;
    private TextView tv_account_record;
    private RecyclerView rv_account;
    private AccountCZAdapter adapter;

    private List<ChongZhiBean> list = new ArrayList<>();
    private ChongZhiBean czBean;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBarGreen(this);
        setContentView(R.layout.activity_user_account);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        tv_money = (TextView) findViewById(R.id.tv_money);
        btn_chongzhi = (Button) findViewById(R.id.btn_chongzhi);

        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        tv_account_record = (TextView) findViewById(R.id.tv_account_record);
        rv_account = (RecyclerView) findViewById(R.id.rv_account);

        rv_account.setLayoutManager(new GridLayoutManager(this,2));
        adapter = new AccountCZAdapter(this,list);
        rv_account.setAdapter(adapter);
    }

    private void initData() {

        //通过接口获取充值数据
        ChongzhiRequest.getChongZhi(getSelfActivity(),new MyChongzhiCallBack());
    }

    private void setListener() {
        tv_account_record.setOnClickListener(this);
        ll_back.setOnClickListener(this);
        btn_chongzhi.setOnClickListener(this);

        //条目点击事件
        adapter.setOnItemClickLitener(new AccountCZAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                adapter.setSeclection(position);
                adapter.notifyDataSetChanged();
//                czBean = list.get(position);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_chongzhi:
                if(ObjectUtils.isNull(czBean.getRechargeAmout())){//如果没有选择充值数
                    ToastUtil.doToast(getSelfActivity(),"请先选择充值金额");
                    return;
                }
                showCZDialog();
                break;
            case R.id.iv_cz_wechat:
                dialog.dismiss();
                ToastUtil.doToast(AccountActivity.this, "微信充值");
                break;
            case R.id.iv_cz_alipay:
                dialog.dismiss();
                ToastUtil.doToast(AccountActivity.this, "支付宝充值");
                break;
            case R.id.tv_account_record://充值记录
                startActivity(new Intent(getSelfActivity(),AccountRecordActivity.class));
                break;
            case R.id.ll_back://返回
                finish();
                break;

        }
    }

    /**
     * 显示充值对话框
     */
    private void showCZDialog() {
        dialog = new Dialog(this, R.style.dialog_theme);
        View view = View.inflate(this, R.layout.dialog_chongzhi, null);
        ImageView iv_cz_wechat = (ImageView) view.findViewById(R.id.iv_cz_wechat);
        ImageView iv_cz_alipay = (ImageView) view.findViewById(R.id.iv_cz_alipay);
        dialog.setContentView(view);
        dialog.show();

        iv_cz_wechat.setOnClickListener(this);
        iv_cz_alipay.setOnClickListener(this);
    }

    public class MyChongzhiCallBack extends ChongzhiCallBack{

        @Override
        public void success(String msg, ChongZhiBean bean) {
            toast("获取成功");
        }

        @Override
        public void fail(String msg) {

        }

        @Override
        public void connectFail() {

        }
    }
}
