package huanxing_print.com.cn.printhome.ui.activity.approval;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.ui.adapter.ApprovalPersonAdapter;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.Info;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.view.ScrollListView;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;

/**
 * Created by htj on 2017/5/7.
 */

public class ApprovalApplyDetailsActivity extends BaseActivity implements View.OnClickListener {
    public Context mContext;


    private ImageView iv_user_name;
    private ImageView iv_back;
    private ImageView iv_camera;
    private Button btn_agree;
    private Button btn_bohui;
    private Button btn_commit;
    private TextView iv_name;
    private TextView tv_use;
    private TextView tv_number;
    private TextView tv_section;
    private TextView tv_total;
    private TextView tv_kind;
    private TextView iv_isapproval;
    private TextView tv_apply_money;
    private TextView tv_overtime;
    private TextView tv_use_name;
    private ScrollListView ll_detail;

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

        //明细的listview

        ll_detail.setAdapter(new DetailAdapter(mContext,lists));


    }

    private void initView() {
        iv_isapproval = (TextView) findViewById(R.id.iv_isapproval);
        iv_name = (TextView) findViewById(R.id.iv_name);
        tv_use = (TextView) findViewById(R.id.tv_use);
        tv_section = (TextView) findViewById(R.id.tv_section);
        tv_number = (TextView) findViewById(R.id.tv_number);
        tv_section = (TextView) findViewById(R.id.tv_section);
//        tv_apply_money = (TextView) findViewById(R.id.tv_apply_money);
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
        ll_detail = (ScrollListView) findViewById(R.id.rl_apply_detail);


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
    private class DetailAdapter extends BaseAdapter{

        private Context ctx;
        private List<Info> list;

        public DetailAdapter(Context ctx, List<Info> list) {
            this.ctx = ctx;
            this.list = list;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(ctx).inflate(R.layout.item_apply_detail,null);
                holder = new MyViewHolder();
                holder.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
                holder.tv_kind = (TextView) convertView.findViewById(R.id.tv_kind);
                holder.tv_detail = (TextView) convertView.findViewById(R.id.tv_detail);//明细列表

                convertView.setTag(holder);
            }else {
                holder = (MyViewHolder) convertView.getTag();
            }

            //Info info = list.get(position);

        /*BitmapUtils.displayImage(ctx,info.getUsePic(),holder.iv_user_head);
        //holder.iv_user_head.setImageResource();
        holder.tv_name.setText(info.getName());
        //holder.iv_isapproval
        holder.tv_time.setText(info.getTime());
        holder.tv_detail.setText(info.getDetail());*/

            return convertView;
        }
        public class MyViewHolder{
            TextView tv_money;
            TextView tv_kind;
            TextView tv_detail;
        }
    }

}
