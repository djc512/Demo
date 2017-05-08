package huanxing_print.com.cn.printhome.ui.activity.approval;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.ui.adapter.ApprovalPersonAdapter;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;

/**
 * Created by htj on 2017/5/7.
 */

public class ApprovalApplyDetailsActivity extends BaseActivity implements View.OnClickListener {
    public Context mContext;


    ImageView iv_user_name;
    ImageView iv_back;
    ImageView iv_camera;
    Button btn_agree;
    Button btn_bohui;
    Button btn_commit;
    TextView iv_name;
    TextView tv_use;
    TextView tv_number;
    TextView tv_section;
    TextView tv_total;
    TextView tv_kind;
    TextView iv_isapproval;
    TextView tv_apply_money;
    TextView tv_overtime;
    TextView tv_use_name;

    LinearLayout ll_commit;
    LinearLayout bt_reject_agree;
    //凭证id
    RelativeLayout rl_sertificate;

    ListView ll_person;

    boolean isRequestMoney =false;
    ArrayList lists = new ArrayList();

    ApprovalPersonAdapter personAdapter;


    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval_apply_detail);
        CommonUtils.initSystemBar(this);
        mContext = ApprovalApplyDetailsActivity.this;
        initView();
        initData();
        initListener();
    }

    private void initListener() {

        btn_commit.setOnClickListener(this);
        btn_bohui.setOnClickListener(this);
        btn_agree.setOnClickListener(this);
        iv_back.setOnClickListener(this);

        ll_person.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

    private void initData() {
        if (!ObjectUtils.isNull(getIntent().getStringExtra("what"))){
            switch (getIntent().getStringExtra("what")){
                //驳回，同意签字
                case "1":
                    ll_commit.setVisibility(View.GONE);
                    bt_reject_agree.setVisibility(View.VISIBLE);
                    rl_sertificate.setVisibility(View.GONE);
                    break;
                //生成凭证
                case "2":
                    ll_commit.setVisibility(View.VISIBLE);
                    bt_reject_agree.setVisibility(View.GONE);
                    btn_commit.setText("生成凭证");
                    rl_sertificate.setVisibility(View.VISIBLE);
                    break;
                //撤回
                case "3":
                    ll_commit.setVisibility(View.VISIBLE);
                    bt_reject_agree.setVisibility(View.GONE);
                    rl_sertificate.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        }

        personAdapter = new ApprovalPersonAdapter(this,lists);
        ll_person.setAdapter(personAdapter);


    }

    private void initView() {
        iv_isapproval = (TextView) findViewById(R.id.iv_isapproval);
        iv_name = (TextView) findViewById(R.id.iv_name);
        tv_use = (TextView) findViewById(R.id.tv_use);
        tv_section = (TextView) findViewById(R.id.tv_section);
        tv_number = (TextView) findViewById(R.id.tv_number);
        tv_section = (TextView) findViewById(R.id.tv_section);
        tv_apply_money = (TextView) findViewById(R.id.tv_apply_money);
        tv_kind = (TextView) findViewById(R.id.tv_kind);
        tv_total = (TextView) findViewById(R.id.tv_total);
        //抄送人的名字
        tv_use_name = (TextView) findViewById(R.id.tv_use_name);

        //展示审批的经过人
        ll_person = (ListView) findViewById(R.id.ll_person);
        //抄送人的头像
        iv_user_name = (ImageView) findViewById(R.id.iv_user_name);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        //多张图
        iv_camera = (ImageView) findViewById(R.id.iv_camera);

        btn_commit = (Button) findViewById(R.id.btn_commit);
        btn_bohui = (Button) findViewById(R.id.btn_bohui);
        btn_agree = (Button) findViewById(R.id.btn_agree);

        ll_commit = (LinearLayout) findViewById(R.id.ll_remove);
        bt_reject_agree = (LinearLayout) findViewById(R.id.bt_reject_agree);
        rl_sertificate = (RelativeLayout) findViewById(R.id.rl_sertificate);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finishCurrentActivity();
                break;
            case R.id.bt_commit:

                break;
            case R.id.btn_bohui:
                finishCurrentActivity();
                //同时反馈数据给服务器


                break;
            case R.id.btn_agree:
                //startActivity(new Intent(getSelfActivity(), HandWriteActivity.class));
//                if (mPathView.getTouched()) {
//                    try {
//                        mPathView.save("/sdcard/qm.png", true, 10);
//                        Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//
//                    Toast.makeText(this, "您没有签名~", Toast.LENGTH_SHORT).show();
//                }

                DialogUtils.showSignatureDialog(getSelfActivity(),
                        new DialogUtils.SignatureDialogCallBack() {
                            @Override
                            public void ok() {
                                Toast.makeText(getSelfActivity(), "保存成功", Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void cancel() {

                            }
                        }).show();

                break;
            default:
                break;


        }

    }
}
