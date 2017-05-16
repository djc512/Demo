package huanxing_print.com.cn.printhome.ui.activity.approval;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
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
import java.util.List;
import java.util.Map;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.model.approval.ApprovalDetail;
import huanxing_print.com.cn.printhome.model.approval.ApprovalOrCopy;
import huanxing_print.com.cn.printhome.model.approval.Attachment;
import huanxing_print.com.cn.printhome.model.image.HeadImageBean;
import huanxing_print.com.cn.printhome.net.callback.NullCallback;
import huanxing_print.com.cn.printhome.net.callback.approval.QueryApprovalDetailCallBack;
import huanxing_print.com.cn.printhome.net.callback.image.HeadImageUploadCallback;
import huanxing_print.com.cn.printhome.net.request.approval.ApprovalRequest;
import huanxing_print.com.cn.printhome.net.request.image.HeadImageUploadRequest;
import huanxing_print.com.cn.printhome.ui.adapter.ApprovalCopyMembersAdapter;
import huanxing_print.com.cn.printhome.ui.adapter.ApprovalPersonAdapter;
import huanxing_print.com.cn.printhome.ui.adapter.AttachmentAdatper;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.view.ScrollGridView;
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
    TextView iv_name;
    TextView tv_use;//用途说明
    TextView tv_number;
    TextView tv_section;//请款部门
    TextView tv_detail;//采购清单
    TextView iv_isapproval;
    TextView tv_money;//请款金额
    TextView tv_overtime;//完成日期
    TextView tv_copy_name;

    //凭证id
    RelativeLayout rl_sertificate;//凭证布局
    ListView ll_approval_process;
    private ScrollGridView noScrollgridview;//采购展示图片的GridView
    private ScrollGridView copyScrollgridview;//抄送成员展示
    boolean isRequestMoney =false;

    ApprovalPersonAdapter personAdapter;
//    private PicAdapter adapter;
    private AttachmentAdatper attachmentAdatper;
    ArrayList<Attachment> attachments = new ArrayList<Attachment>();
    private ApprovalCopyMembersAdapter copyMembersAdapter;
    ArrayList<ApprovalOrCopy> lists = new ArrayList<ApprovalOrCopy>();
    ArrayList<ApprovalOrCopy> copyMembers = new ArrayList<ApprovalOrCopy>();
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
        findViewById(R.id.ll_back).setOnClickListener(this);
        findViewById(R.id.btn_revoke).setOnClickListener(this);
        findViewById(R.id.btn_reject).setOnClickListener(this);
        findViewById(R.id.btn_pass).setOnClickListener(this);
        findViewById(R.id.btn_print).setOnClickListener(this);
        findViewById(R.id.btn_look).setOnClickListener(this);
    }

    QueryApprovalDetailCallBack callBack = new QueryApprovalDetailCallBack() {
        @Override
        public void success(String msg, ApprovalDetail approvalDetail) {
            DialogUtils.closeProgressDialog();
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
            DialogUtils.closeProgressDialog();
            toast(msg);
        }

        @Override
        public void connectFail() {
            DialogUtils.closeProgressDialog();
            toastConnectFail();
        }
    };
    private void initData() {
        approveId = getIntent().getStringExtra("approveId");
        DialogUtils.showProgressDialog(getSelfActivity(), "正在加载").show();
        ApprovalRequest.getQueryApprovalDetail(getSelfActivity(),baseApplication.getLoginToken(),
                approveId,callBack);

        personAdapter = new ApprovalPersonAdapter(this,lists);
        ll_approval_process.setAdapter(personAdapter);;
//        //横向图片展示
//        /*//假数据
//        bimap = BitmapFactory.decodeResource(getResources(), R.drawable.add_people);
//        mResults.add(bimap);
//        bimap = BitmapFactory.decodeResource(getResources(), R.drawable.arrow_go);
//
//        mResults.add(bimap);*/
//
        //展示采购图片的gridview
//        adapter = new PicAdapter();
        //adapter.update();
        attachmentAdatper = new AttachmentAdatper(this, attachments);
        noScrollgridview.setAdapter(attachmentAdatper);
//
////        showData();
        //展示抄送人员
        copyMembersAdapter = new ApprovalCopyMembersAdapter(this, copyMembers);
        copyScrollgridview.setAdapter(copyMembersAdapter);
    }

    private void showData() {
        //展示 数据

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

        tv_number.setText(details.getApproveId().isEmpty() ? "" : details.getApproveId());
        tv_section.setText(details.getDepartment().isEmpty() ? "" : details.getDepartment());
        tv_use.setText(details.getRemark().isEmpty() ? "" : details.getRemark());
        tv_detail.setText(details.getPurchaseList().isEmpty() ? "" : details.getPurchaseList());
        tv_money.setText(details.getAmountMonney().isEmpty() ? "" : details.getAmountMonney());
        tv_overtime.setText(details.getAddTime().isEmpty() ? "" : details.getAddTime());
        if(null != details.getAttachmentList()) {
            attachmentAdatper.modifyData(details.getAttachmentList());
        }

        ArrayList<ApprovalOrCopy> copyMemberList =  details.getCopyerList();
        if(null != copyMemberList && copyMemberList.size() > 0) {
            copyMembers = copyMemberList;
            copyMembersAdapter.modifyData(copyMembers);
        }
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

        rl_sertificate = (RelativeLayout) findViewById(R.id.rl_sertificate);

        noScrollgridview = (ScrollGridView) findViewById(R.id.noScrollgridview);
        noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        iv_user_head = (ImageView) findViewById(R.id.iv_user_head);
        copyScrollgridview = (ScrollGridView) findViewById(R.id.gridview_copy_member);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
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
        Intent intent = new Intent(this, VoucherPreviewActivity.class);
        intent.putExtra("approveId", Integer.valueOf(approveId));
        intent.putExtra("type", 0);
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

    private NullCallback nullcallback = new NullCallback() {

        @Override
        public void fail(String msg) {
            toast(msg);
            DialogUtils.closeProgressDialog();

        }

        @Override
        public void connectFail() {
            DialogUtils.closeProgressDialog();
            toastConnectFail();
        }

        @Override
        public void success(String msg) {
            toast("驳回成功");
            DialogUtils.closeProgressDialog();
            finishCurrentActivity();
        }
    };

    NullCallback revokeCallback = new NullCallback() {
        @Override
        public void success(String msg) {
            DialogUtils.closeProgressDialog();
            toast("撤销成功");
            finishCurrentActivity();
        }

        @Override
        public void fail(String msg) {
            DialogUtils.closeProgressDialog();
            toast(msg);
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
            toast("驳回成功");
            finishCurrentActivity();
        }

        @Override
        public void fail(String msg) {
            DialogUtils.closeProgressDialog();
            toast(msg);
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
            toast(msg);
        }

        @Override
        public void connectFail() {
            DialogUtils.closeProgressDialog();
            connectFail();
        }
    };

    private void setPicToView(Bitmap bitMap,String filePath ) {
        //Bundle extras = picdata.getExtras();
        if (null != bitMap) {
            //Bitmap photo = extras.getParcelable("data");
            //Bitmap photo =filterColor(phmp);
            iv_user_head.setImageBitmap(bitMap);
            //String filePath = FileUtils.savePic(getSelfActivity(), "signImg.jpg", bitMap);
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
                                        approveId,1,imgUrl,nullcallback);
                            }
                        });
            }

        }
    }
}
