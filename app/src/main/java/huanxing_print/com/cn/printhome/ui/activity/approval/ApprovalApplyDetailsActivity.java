package huanxing_print.com.cn.printhome.ui.activity.approval;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.model.approval.ApprovalDetail;
import huanxing_print.com.cn.printhome.model.approval.ApprovalOrCopy;
import huanxing_print.com.cn.printhome.model.approval.Attachment;
import huanxing_print.com.cn.printhome.model.approval.SubFormItem;
import huanxing_print.com.cn.printhome.net.callback.approval.QueryApprovalDetailCallBack;
import huanxing_print.com.cn.printhome.net.request.approval.ApprovalRequest;
import huanxing_print.com.cn.printhome.ui.adapter.ApprovalCopyMembersAdapter;
import huanxing_print.com.cn.printhome.ui.adapter.ApprovalPersonAdapter;
import huanxing_print.com.cn.printhome.ui.adapter.AttachmentAdatper;
import huanxing_print.com.cn.printhome.ui.adapter.SubFormAdatper;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.Info;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.view.ScrollGridView;
import huanxing_print.com.cn.printhome.view.ScrollListView;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;

/**
 * Created by htj on 2017/5/7.
 */

public class ApprovalApplyDetailsActivity extends BaseActivity implements View.OnClickListener {
    public Context mContext;


//    private ImageView iv_user_name;
//    private ImageView iv_back;
//    private ImageView iv_camera;
//    private Button btn_agree;
//    private Button btn_bohui;
//    private Button btn_commit;
    private TextView iv_name;
//    private TextView tv_use;
    private TextView tv_number;
    private TextView tv_section;
    private TextView tv_total;
//    private TextView tv_kind;
    private TextView iv_isapproval;
//    private TextView tv_apply_money;
//    private TextView tv_overtime;
//    private TextView tv_use_name;
    private ScrollListView ll_detail;
    private CircleImageView iv_user_head;
    private ScrollGridView attachmentGridview;
    private ScrollListView ll_approval_process;
    private ScrollGridView copyScrollgridview;//抄送成员展示
    LinearLayout ll_commit;
    LinearLayout bt_reject_agree;
    //凭证id
    RelativeLayout rl_sertificate;
//
//    ListView ll_person;
//
//    boolean isRequestMoney =false;
//    ArrayList lists = new ArrayList();
//
//    ApprovalPersonAdapter personAdapter;
    private AttachmentAdatper attachmentAdatper;
    ArrayList<Attachment> attachments = new ArrayList<Attachment>();
    private ApprovalPersonAdapter personAdapter;
    ArrayList<ApprovalOrCopy> lists = new ArrayList<ApprovalOrCopy>();
    private ApprovalCopyMembersAdapter copyMembersAdapter;
    ArrayList<ApprovalOrCopy> copyMembers = new ArrayList<ApprovalOrCopy>();
    private SubFormAdatper subFormAdatper;
    ArrayList<SubFormItem> subFormItems = new ArrayList<SubFormItem>();
    String approveId;
    private ApprovalDetail details;
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
        findViewById(R.id.ll_back).setOnClickListener(this);
//        btn_commit.setOnClickListener(this);
//        btn_bohui.setOnClickListener(this);
//        btn_agree.setOnClickListener(this);
//        iv_back.setOnClickListener(this);
//
//        ll_person.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//            }
//        });
    }

    private void initData() {
        approveId = getIntent().getStringExtra("approveId");
        ApprovalRequest.getQueryApprovalDetail(getSelfActivity(),baseApplication.getLoginToken(),approveId,callBack);

    }

    private void showData() {
        //member名称
        iv_name.setText(details.getMemberName().isEmpty() ? "Null" : details.getMemberName());
        //member头像
        Glide.with(mContext).load(details.getMemberUrl()).placeholder(R.drawable.iv_head).into(new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                iv_user_head.setImageDrawable(resource);
            }
        });
        //审批状态
        if(0 == details.getStatus()) {
            iv_isapproval.setText("审批中");
            iv_isapproval.setTextColor(getResources().getColor(R.color.text_yellow));
        }else if(2 == details.getStatus()) {
            iv_isapproval.setText("审批完成");
            iv_isapproval.setTextColor(getResources().getColor(R.color.green));
        }else if(3 == details.getStatus()) {
            iv_isapproval.setText("已驳回");
            iv_isapproval.setTextColor(getResources().getColor(R.color.green));
        }else if(4 == details.getStatus()) {
            iv_isapproval.setText("已撤销");
            iv_isapproval.setTextColor(getResources().getColor(R.color.green));
        }else if(5 == details.getStatus()) {
            iv_isapproval.setText("打印凭证");
            iv_isapproval.setTextColor(getResources().getColor(R.color.green));
        }else if(6 == details.getStatus()) {
            iv_isapproval.setText("已打印");
            iv_isapproval.setTextColor(getResources().getColor(R.color.green));
        }


        tv_number.setText(details.getApproveId().isEmpty() ? "" : details.getApproveId());
        tv_section.setText(details.getDepartment().isEmpty() ? "" : details.getDepartment());
        tv_total.setText(details.getAmountMonney().isEmpty() ? "" : details.getAmountMonney());
        if(null != details.getAttachmentList()) {
            attachmentAdatper.modifyData(details.getAttachmentList());
        }

        //审批人列表审批状态
        ArrayList<ApprovalOrCopy> list =  details.getApproverList();
        if(null != list && list.size() > 0) {
            ApprovalOrCopy approvalOrCopy = new ApprovalOrCopy();
            approvalOrCopy.setName(details.getMemberName());
            approvalOrCopy.setFaceUrl(details.getMemberUrl());
            approvalOrCopy.setUpdateTime(details.getAddTime());
            approvalOrCopy.setStatus("-2");
            lists.clear();
            lists.add(approvalOrCopy);
            lists.addAll(list);
            personAdapter.modifyApprovalPersons(lists);
        }
        //抄送
        ArrayList<ApprovalOrCopy> copylist =  details.getCopyerList();
        if(null != copylist && copylist.size() > 0) {
            copyMembers = copylist;
            copyMembersAdapter.modifyData(copyMembers);
        }

        //明细
        ArrayList<SubFormItem> items = details.getSubFormList();
        if(null != items && items.size() > 0) {
            subFormItems = items;
            subFormAdatper.modifyData(subFormItems);
        }
    }

    private void initView() {
        iv_isapproval = (TextView) findViewById(R.id.iv_isapproval);
        iv_name = (TextView) findViewById(R.id.iv_name);
        iv_user_head = (CircleImageView) findViewById(R.id.iv_user_head);
//        tv_use = (TextView) findViewById(R.id.tv_use);
        tv_section = (TextView) findViewById(R.id.tv_section);
        tv_number = (TextView) findViewById(R.id.tv_number);
//        tv_section = (TextView) findViewById(R.id.tv_section);
////        tv_apply_money = (TextView) findViewById(R.id.tv_apply_money);
//        tv_kind = (TextView) findViewById(R.id.tv_kind);
        tv_total = (TextView) findViewById(R.id.tv_total);
//        //抄送人的名字
//        tv_use_name = (TextView) findViewById(R.id.tv_use_name);
//
//        //展示审批的经过人
//        ll_person = (ListView) findViewById(R.id.ll_person);
//        //抄送人的头像
//        iv_user_name = (ImageView) findViewById(R.id.iv_user_name);
//        iv_back = (ImageView) findViewById(R.id.iv_back);
//        //多张图
//        iv_camera = (ImageView) findViewById(R.id.iv_camera);
//
//        btn_commit = (Button) findViewById(R.id.btn_commit);
//        btn_bohui = (Button) findViewById(R.id.btn_bohui);
//        btn_agree = (Button) findViewById(R.id.btn_agree);
//

        ll_commit = (LinearLayout) findViewById(R.id.ll_remove);

        bt_reject_agree = (LinearLayout) findViewById(R.id.bt_reject_agree);
        rl_sertificate = (RelativeLayout) findViewById(R.id.rl_sertificate);

        //附件
        attachmentGridview = (ScrollGridView) findViewById(R.id.noScrollgridview);
        attachmentAdatper = new AttachmentAdatper(ApprovalApplyDetailsActivity.this, attachments);
        attachmentGridview.setAdapter(attachmentAdatper);

        //审批人列表详情
        ll_approval_process = (ScrollListView) findViewById(R.id.ll_approval_process);
        personAdapter = new ApprovalPersonAdapter(this,lists);
        ll_approval_process.setAdapter(personAdapter);


        copyScrollgridview = (ScrollGridView) findViewById(R.id.gridview_copy_member);
        //展示抄送人员
        copyMembersAdapter = new ApprovalCopyMembersAdapter(this, copyMembers);
        copyScrollgridview.setAdapter(copyMembersAdapter);

        //明细
        ll_detail = (ScrollListView) findViewById(R.id.rl_apply_detail);
        subFormAdatper = new SubFormAdatper(this, subFormItems);
        ll_detail.setAdapter(subFormAdatper);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
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

//    private class DetailAdapter extends BaseAdapter{
//
//        private Context ctx;
//        private List<Info> list;
//
//        public DetailAdapter(Context ctx, List<Info> list) {
//            this.ctx = ctx;
//            this.list = list;
//        }
//
//        @Override
//        public int getCount() {
//            return 3;
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return list.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            MyViewHolder holder = null;
//            if (convertView == null) {
//                convertView = LayoutInflater.from(ctx).inflate(R.layout.item_apply_detail,null);
//                holder = new MyViewHolder();
//                holder.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
//                holder.tv_kind = (TextView) convertView.findViewById(R.id.tv_kind);
//                holder.tv_detail = (TextView) convertView.findViewById(R.id.tv_detail);//明细列表
//
//                convertView.setTag(holder);
//            }else {
//                holder = (MyViewHolder) convertView.getTag();
//            }
//
//            //Info info = list.get(position);
//
//        /*BitmapUtils.displayImage(ctx,info.getUsePic(),holder.iv_user_head);
//        //holder.iv_user_head.setImageResource();
//        holder.tv_name.setText(info.getName());
//        //holder.iv_isapproval
//        holder.tv_time.setText(info.getTime());
//        holder.tv_detail.setText(info.getDetail());*/
//
//            return convertView;
//        }
//        public class MyViewHolder{
//            TextView tv_money;
//            TextView tv_kind;
//            TextView tv_detail;
//        }
//    }

    QueryApprovalDetailCallBack callBack = new QueryApprovalDetailCallBack() {
        @Override
        public void success(String msg, ApprovalDetail approvalDetail) {
            Log.e("wanghao","approvalDetail == " + (approvalDetail == null));
            details = approvalDetail;
            if(null != details) {
                showData();
                if (!ObjectUtils.isNull(details.getType())) {
                    switch (details.getType()) {
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
//                            btn_certificate.setText("生成凭证");
                            rl_sertificate.setVisibility(View.VISIBLE);
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

        }

        @Override
        public void fail(String msg) {

        }

        @Override
        public void connectFail() {

        }
    };

}
