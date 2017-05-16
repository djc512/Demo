package huanxing_print.com.cn.printhome.ui.activity.approval;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.model.approval.ApprovalDetail;
import huanxing_print.com.cn.printhome.model.approval.ApprovalOrCopy;
import huanxing_print.com.cn.printhome.model.approval.Attachment;
import huanxing_print.com.cn.printhome.model.approval.SubFormItem;
import huanxing_print.com.cn.printhome.model.image.HeadImageBean;
import huanxing_print.com.cn.printhome.net.callback.NullCallback;
import huanxing_print.com.cn.printhome.net.callback.approval.QueryApprovalDetailCallBack;
import huanxing_print.com.cn.printhome.net.callback.image.HeadImageUploadCallback;
import huanxing_print.com.cn.printhome.net.request.approval.ApprovalRequest;
import huanxing_print.com.cn.printhome.net.request.image.HeadImageUploadRequest;
import huanxing_print.com.cn.printhome.ui.adapter.ApprovalCopyMembersAdapter;
import huanxing_print.com.cn.printhome.ui.adapter.ApprovalPersonAdapter;
import huanxing_print.com.cn.printhome.ui.adapter.AttachmentAdatper;
import huanxing_print.com.cn.printhome.ui.adapter.SubFormAdatper;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.Info;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;
import huanxing_print.com.cn.printhome.view.ScrollGridView;
import huanxing_print.com.cn.printhome.view.ScrollListView;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;

/**
 * Created by htj on 2017/5/7.
 */

public class ApprovalApplyDetailsActivity extends BaseActivity implements View.OnClickListener {
    public Context mContext;


    private TextView iv_name;
    private TextView tv_number;
    private TextView tv_section;
    private TextView tv_total;
    private TextView iv_isapproval;
    private ScrollListView ll_detail;
    private CircleImageView iv_user_head;
    private ScrollGridView attachmentGridview;
    private ScrollListView ll_approval_process;
    private ScrollGridView copyScrollgridview;//抄送成员展示
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
        findViewById(R.id.btn_revoke).setOnClickListener(this);
        findViewById(R.id.btn_reject).setOnClickListener(this);
        findViewById(R.id.btn_pass).setOnClickListener(this);
        findViewById(R.id.btn_print).setOnClickListener(this);
        findViewById(R.id.btn_look).setOnClickListener(this);
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
        tv_section = (TextView) findViewById(R.id.tv_section);
        tv_number = (TextView) findViewById(R.id.tv_number);
        tv_total = (TextView) findViewById(R.id.tv_total);

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
            case R.id.btn_revoke:
                revoke();
                break;
            case R.id.btn_reject:
                reject();
                break;
            case R.id.btn_pass:
                DialogUtils.showSignatureDialog(getSelfActivity(),
                        new DialogUtils.SignatureDialogCallBack() {
                            @Override
                            public void ok() {
                                Toast.makeText(getSelfActivity(), "保存成功", Toast.LENGTH_SHORT).show();
                                Bitmap bitmp = BitmapFactory.decodeFile("/sdcard/signature.png");
                                if(null!=bitmp){
                                    setPicToView(bitmp,"/sdcard/signature.png");
                                }
                            }

                            @Override
                            public void cancel() {

                            }
                        }).show();
                break;
            case R.id.btn_print:
                createAndLook();
                break;
            case R.id.btn_look:
                createAndLook();
                break;
            default:
                break;


        }

    }

    private void createAndLook() {
        Intent intent = new Intent(ApprovalApplyDetailsActivity.this, VoucherPreviewActivity.class);
        intent.putExtra("approveId", Integer.valueOf(approveId));
        intent.putExtra("type", 1);
        startActivity(intent);
    }

    private void revoke() {
        DialogUtils.showProgressDialog(this, "撤销审批中").show();
        ApprovalRequest.revokeReq(this, baseApplication.getLoginToken(), approveId, revokeCallback);
    }

    /**
     * 同意并签字请求
     * @param signUrl
     */
    private void pass(String signUrl) {
        DialogUtils.showProgressDialog(this, "处理中").show();
        ApprovalRequest.approval(this, baseApplication.getLoginToken(), approveId, 1, signUrl, passCallback);
    }

    /**
     * 驳回
     */
    private void reject() {
        DialogUtils.showProgressDialog(this, "驳回处理中").show();
        ApprovalRequest.approval(this, baseApplication.getLoginToken(), approveId, 2, "", rejectCallBack);
    }

    private void setPicToView(Bitmap bitMap,String filePath ) {
        if (null != bitMap) {
            if (!ObjectUtils.isNull(filePath)) {
                File file = new File(filePath);
                //file转化成二进制
                byte[] buffer = null;
                FileInputStream in ;
                int length = 0;
                try {
                    in = new FileInputStream(file);
                    buffer = new byte[(int) file.length() + 100];
                    length = in.read(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String data = Base64.encodeToString(buffer, 0, length, Base64.DEFAULT);

                DialogUtils.showProgressDialog(getSelfActivity(), "文件上传中").show();
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("fileContent", data);
                params.put("fileName", filePath);
                params.put("fileType", ".jpg");
                HeadImageUploadRequest.upload(getSelfActivity(),  params,
                        new HeadImageUploadCallback() {

                            @Override
                            public void fail(String msg) {
                                toast(msg);
                                DialogUtils.closeProgressDialog();
                            }

                            @Override
                            public void connectFail() {
                                toastConnectFail();
                                DialogUtils.closeProgressDialog();
                            }

                            @Override
                            public void success(String msg, HeadImageBean bean) {
                                //Logger.d("PersonInfoActivity ---------HeadImage--------:" + bean.getImgUrl()
                                // );
                                String imgUrl = bean.getImgUrl()+"";
                                ApprovalRequest.approval(getSelfActivity(),baseApplication.getLoginToken(),
                                        approveId,1,imgUrl,passCallback);
                            }
                        });
            }

        }
    }

    QueryApprovalDetailCallBack callBack = new QueryApprovalDetailCallBack() {
        @Override
        public void success(String msg, ApprovalDetail approvalDetail) {
            details = approvalDetail;
            if(null != details) {
                showData();
                if (!ObjectUtils.isNull(details.getStatus())) {
                    findViewById(R.id.ll_revoke).setVisibility(View.GONE);
                    findViewById(R.id.ll_pass).setVisibility(View.GONE);
                    findViewById(R.id.ll_print).setVisibility(View.GONE);
                    findViewById(R.id.ll_look).setVisibility(View.GONE);
                    findViewById(R.id.rl_sertificate).setVisibility(View.GONE);
                    switch (details.getStatus()) {
                        case 0://审批中
                            if (details.getJobNumber().equals(baseApplication.getMemberId())) {
                                //撤销
                                findViewById(R.id.ll_revoke).setVisibility(View.VISIBLE);
                            }else{
                                //驳回、同意并签字
                                findViewById(R.id.ll_pass).setVisibility(View.VISIBLE);
                            }
                            break;
                        case 2://打印凭证
                            if (details.getJobNumber().equals(baseApplication.getMemberId())) {
                                findViewById(R.id.ll_print).setVisibility(View.VISIBLE);
                            }
                            findViewById(R.id.rl_sertificate).setVisibility(View.VISIBLE);
                            break;
                        case 5://已打印
                            if (details.getJobNumber().equals(baseApplication.getMemberId())) {
                                findViewById(R.id.ll_look).setVisibility(View.VISIBLE);
                            }
                            findViewById(R.id.rl_sertificate).setVisibility(View.VISIBLE);
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

    NullCallback revokeCallback = new NullCallback() {
        @Override
        public void success(String msg) {
            DialogUtils.closeProgressDialog();
            ToastUtil.doToast(ApprovalApplyDetailsActivity.this, "报销审批撤销成功");
            finishCurrentActivity();
        }

        @Override
        public void fail(String msg) {
            DialogUtils.closeProgressDialog();
            ToastUtil.doToast(ApprovalApplyDetailsActivity.this, msg);
        }

        @Override
        public void connectFail() {
            DialogUtils.closeProgressDialog();
            connectFail();
        }
    };

    NullCallback rejectCallBack = new NullCallback() {
        @Override
        public void success(String msg) {
            DialogUtils.closeProgressDialog();
            ToastUtil.doToast(ApprovalApplyDetailsActivity.this, "驳回成功");
            finishCurrentActivity();
        }

        @Override
        public void fail(String msg) {
            DialogUtils.closeProgressDialog();
            ToastUtil.doToast(ApprovalApplyDetailsActivity.this, msg);
        }

        @Override
        public void connectFail() {
            DialogUtils.closeProgressDialog();
            connectFail();
        }
    };

    NullCallback passCallback = new NullCallback() {
        @Override
        public void success(String msg) {
            DialogUtils.closeProgressDialog();
            initData();
        }

        @Override
        public void fail(String msg) {
            DialogUtils.closeProgressDialog();
            ToastUtil.doToast(ApprovalApplyDetailsActivity.this, msg);
        }

        @Override
        public void connectFail() {
            DialogUtils.closeProgressDialog();
            connectFail();
        }
    };

}
