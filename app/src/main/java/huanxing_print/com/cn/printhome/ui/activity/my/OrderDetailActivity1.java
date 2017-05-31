package huanxing_print.com.cn.printhome.ui.activity.my;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.util.CommonUtils;

public class OrderDetailActivity1 extends BaseActivity implements View.OnClickListener {

    private LinearLayout ll_back;
    private RecyclerView rv_order_item;
    private TextView tv_order_id;
    private TextView tv_order_channel;
    private TextView tv_order_time;
    private TextView tv_order_address;
    private TextView tv_order_totalprice;
    private ImageView iv_back;
    private ImageView iv_location;
    private TextView tv_location;
    private LinearLayout ll_container;
    private ImageView iv_go;
    private ImageView iv_print;
    private TextView tv_print_name;
    private TextView tv_print_price;
    private TextView tv_print_type;
    private TextView tv_print_time;
    private TextView tv_print_money;
    private TextView tv_print_num;
    private ImageView iv_print1;
    private TextView tv_print_name1;
    private TextView tv_print_price1;
    private TextView tv_print_type1;
    private TextView tv_print_time1;
    private TextView tv_print_money1;
    private TextView tv_print_mun1;
    private TextView tv_send;
    private TextView tv_receipt;
    private ImageView iv_receipt_go;
    private TextView tv_note;
    private EditText et_note;
    private TextView tv_submit;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail1);
        CommonUtils.initSystemBar(this);
        initView();
        initData();
        setListener();
    }

    private void initData() {
    }

    private void setListener() {
        ll_back.setOnClickListener(this);
        iv_go.setOnClickListener(this);
        iv_receipt_go.setOnClickListener(this);
        tv_submit.setOnClickListener(this);
    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_location = (ImageView) findViewById(R.id.iv_location);
        tv_location = (TextView) findViewById(R.id.tv_location);
        ll_container = (LinearLayout) findViewById(R.id.ll_container);
        iv_go = (ImageView) findViewById(R.id.iv_go);
        iv_print = (ImageView) findViewById(R.id.iv_print);
        tv_print_name = (TextView) findViewById(R.id.tv_print_name);
        tv_print_price = (TextView) findViewById(R.id.tv_print_price);
        tv_print_type = (TextView) findViewById(R.id.tv_print_type);
        tv_print_time = (TextView) findViewById(R.id.tv_print_time);
        tv_print_money = (TextView) findViewById(R.id.tv_print_money);
        tv_print_num = (TextView) findViewById(R.id.tv_print_num);
        iv_print1 = (ImageView) findViewById(R.id.iv_print1);
        tv_print_name1 = (TextView) findViewById(R.id.tv_print_name1);
        tv_print_price1 = (TextView) findViewById(R.id.tv_print_price1);
        tv_print_type1 = (TextView) findViewById(R.id.tv_print_type1);
        tv_print_time1 = (TextView) findViewById(R.id.tv_print_time1);
        tv_print_money1 = (TextView) findViewById(R.id.tv_print_money1);
        tv_print_mun1 = (TextView) findViewById(R.id.tv_print_mun1);
        tv_send = (TextView) findViewById(R.id.tv_send);
        tv_receipt = (TextView) findViewById(R.id.tv_receipt);
        iv_receipt_go = (ImageView) findViewById(R.id.iv_receipt_go);
        tv_note = (TextView) findViewById(R.id.tv_note);
        et_note = (EditText) findViewById(R.id.et_note);
        tv_submit = (TextView) findViewById(R.id.tv_submit);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finishCurrentActivity();
                break;
            case R.id.iv_go:
                break;
            case R.id.iv_receipt_go:
                break;
            case R.id.tv_submit:
                String note = et_note.getText().toString().trim();

                break;
        }
    }
}
