package huanxing_print.com.cn.printhome.ui.activity.approval;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
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
import huanxing_print.com.cn.printhome.model.approval.ApprovalDetail;
import huanxing_print.com.cn.printhome.net.callback.approval.QueryApprovalDetailCallBack;
import huanxing_print.com.cn.printhome.net.request.approval.ApprovalRequest;
import huanxing_print.com.cn.printhome.ui.adapter.ApprovalPersonAdapter;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.ImageUtil;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;


/**
 * Created by Administrator on 2017/5/5 0005.
 */
public class ApprovalBuyAddOrRemoveActivity extends BaseActivity implements View.OnClickListener{

    public Context mContext;

    private List<Bitmap> mResults = new ArrayList<>();
    public static Bitmap bimap;

    ImageView iv_user_name;
    ImageView iv_user_head;
    ImageView iv_back;
    Button btn_agree;
    Button btn_bohui;
    Button btn_certificate;
    TextView iv_name;
    TextView tv_use;//用途说明
    TextView tv_number;
    TextView tv_section;//请款部门
    TextView tv_detail;//采购清单
    TextView iv_isapproval;
    TextView tv_money;//请款金额
    TextView tv_overtime;//完成日期
    TextView tv_copy_name;

    LinearLayout ll_commit;
    LinearLayout bt_reject_agree;
    //凭证id
    RelativeLayout rl_sertificate;//凭证布局
    ListView ll_approval_process;
    private GridView noScrollgridview;//采购展示图片的GridView
    boolean isRequestMoney =false;

    ApprovalPersonAdapter personAdapter;
    private PicAdapter adapter;
    ArrayList lists = new ArrayList();
    String approveId;

    private ApprovalDetail details;


    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval_buy_add_or_move);
        CommonUtils.initSystemBar(this);
        mContext = ApprovalBuyAddOrRemoveActivity.this;
        initView();
        initData();
        initListener();
    }

    private void initListener() {

        btn_certificate.setOnClickListener(this);
        btn_bohui.setOnClickListener(this);
        btn_agree.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        //rl_sertificate.setOnClickListener(this);
    }

    QueryApprovalDetailCallBack callBack = new QueryApprovalDetailCallBack() {
        @Override
        public void success(String msg, ApprovalDetail approvalDetail) {
            details = approvalDetail;
            if (!ObjectUtils.isNull(approvalDetail.getType())){
                switch (approvalDetail.getType()){
                    //驳回，同意签字
                    case 1:
                        ll_commit.setVisibility(View.GONE);
                        bt_reject_agree.setVisibility(View.VISIBLE);
                        rl_sertificate.setVisibility(View.GONE);
                        break;
                    //生成凭证
                    case 2:
                        ll_commit.setVisibility(View.VISIBLE);
                        bt_reject_agree.setVisibility(View.GONE);
                        btn_certificate.setText("生成凭证");
                        //rl_sertificate.setVisibility(View.VISIBLE);
                        break;
                    //撤回
                    case 3:
                        ll_commit.setVisibility(View.VISIBLE);
                        bt_reject_agree.setVisibility(View.GONE);
                        rl_sertificate.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }
            }

        }

        @Override
        public void fail(String msg) {

        }

        @Override
        public void connectFail() {

        }
    };
    private void initData() {
        approveId = getIntent().getStringExtra("approveId");
        ApprovalRequest.getQueryApprovalDetail(getSelfActivity(),baseApplication.getLoginToken(),approveId,callBack);

        personAdapter = new ApprovalPersonAdapter(this,lists);
        ll_approval_process.setAdapter(personAdapter);;
        //横向图片展示
        /*//假数据
        bimap = BitmapFactory.decodeResource(getResources(), R.drawable.add_people);
        mResults.add(bimap);
        bimap = BitmapFactory.decodeResource(getResources(), R.drawable.arrow_go);

        mResults.add(bimap);*/

        //展示采购图片的gridview
        adapter = new PicAdapter();
        //adapter.update();
        noScrollgridview.setAdapter(adapter);

        showData();
    }

    private void showData() {
        //展示 数据
    }

    private void initView() {
        iv_isapproval = (TextView) findViewById(R.id.iv_isapproval);
        iv_name = (TextView) findViewById(R.id.iv_name);
        tv_use = (TextView) findViewById(R.id.tv_use);
        tv_section = (TextView) findViewById(R.id.tv_section);
        tv_number = (TextView) findViewById(R.id.tv_number);
        tv_detail = (TextView) findViewById(R.id.tv_detail);
        tv_money = (TextView) findViewById(R.id.tv_money);
        tv_overtime = (TextView) findViewById(R.id.tv_overtime);
        tv_copy_name = (TextView) findViewById(R.id.tv_copy_name);

        ll_approval_process = (ListView) findViewById(R.id.ll_approval_process);
        iv_back = (ImageView) findViewById(R.id.iv_back);

        btn_certificate = (Button) findViewById(R.id.btn_certificate);
        btn_bohui = (Button) findViewById(R.id.btn_bohui);
        btn_agree = (Button) findViewById(R.id.btn_agree);

        ll_commit = (LinearLayout) findViewById(R.id.ll_remove);
        bt_reject_agree = (LinearLayout) findViewById(R.id.bt_reject_agree);
        rl_sertificate = (RelativeLayout) findViewById(R.id.rl_sertificate);

        noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
        noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        iv_user_head = (ImageView) findViewById(R.id.iv_user_head);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finishCurrentActivity();
                break;
            case R.id.btn_certificate:
                //点击生成凭证
                rl_sertificate.setVisibility(View.VISIBLE);

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
                                Toast.makeText(getSelfActivity(), "请签名", Toast.LENGTH_SHORT).show();

                            }
                        }).show();

                break;
            default:
                break;


        }

    }

    private class PicAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return details.getAttachmentList().size();
        }

        @Override
        public Object getItem(int position) {
            return details.getAttachmentList().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new PicAdapter.ViewHolder();
                convertView = LayoutInflater.from(ApprovalBuyAddOrRemoveActivity.this).inflate(
                        R.layout.item_pic_show, null);
                holder.image = (ImageView) convertView.findViewById(R.id.iv_pic);

            } else {
                holder = (PicAdapter.ViewHolder) convertView.getTag();
            }
            //请求网络拿图片
            //请求的url前面加什么？、？？？第二个参数
            ImageUtil.showImageView(ApprovalBuyAddOrRemoveActivity.this,details.getAttachmentList().get(position),holder.image);

            return convertView;
        }

        class ViewHolder {
            ImageView image;
        }
    }
}
