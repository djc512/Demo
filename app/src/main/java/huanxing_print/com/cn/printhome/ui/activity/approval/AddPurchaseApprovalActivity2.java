package huanxing_print.com.cn.printhome.ui.activity.approval;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.bigkoo.pickerview.TimePickerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.event.approval.AttachmentUpdate;
import huanxing_print.com.cn.printhome.model.approval.AddApprovalObject;
import huanxing_print.com.cn.printhome.model.approval.ApprovalOrCopy;
import huanxing_print.com.cn.printhome.model.approval.Approver;
import huanxing_print.com.cn.printhome.model.approval.ChooseGroupEvent;
import huanxing_print.com.cn.printhome.model.approval.ChooseMemberEvent;
import huanxing_print.com.cn.printhome.model.approval.ImageUrl;
import huanxing_print.com.cn.printhome.model.approval.LastApproval;
import huanxing_print.com.cn.printhome.model.comment.PicDataBean;
import huanxing_print.com.cn.printhome.model.contact.GroupMember;
import huanxing_print.com.cn.printhome.model.image.ImageUploadItem;
import huanxing_print.com.cn.printhome.net.callback.approval.AddApprovalCallBack;
import huanxing_print.com.cn.printhome.net.callback.approval.QueryLastCallBack;
import huanxing_print.com.cn.printhome.net.callback.comment.UpLoadPicCallBack;
import huanxing_print.com.cn.printhome.net.request.approval.ApprovalRequest;
import huanxing_print.com.cn.printhome.net.request.commet.UpLoadPicRequest;
import huanxing_print.com.cn.printhome.ui.activity.copy.PhotoPickerActivity;
import huanxing_print.com.cn.printhome.ui.activity.copy.PreviewPhotoActivity;
import huanxing_print.com.cn.printhome.ui.adapter.ApprovalAndCopyAdapter;
import huanxing_print.com.cn.printhome.ui.adapter.AttachmentAdapter;
import huanxing_print.com.cn.printhome.util.BitmapUtils;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.FileUtils;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;
import huanxing_print.com.cn.printhome.view.ScrollGridView;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;

/**
 * Created by wanghao on 2017/5/22.
 */

public class AddPurchaseApprovalActivity2 extends BaseActivity implements View.OnClickListener {
    private static final int CHOOSE_PIC_MAX = 5;
    private static final int PICK_PHOTO = 1;
    private Context ctx;
    private EditText edt_borrow_department;//请款部门
    private EditText edt_buy_reason;//采购事由
    private EditText edt_purchasing_list;//采购清单
    private EditText editText2;//备注
    private EditText edt_request_num;//请款金额
    private EditText edt_payee;//收款方
    private EditText edt_opening_bank;//开户行
    private EditText edt_account_number;//帐号
    private EditText edt_finish_time;//结束时间

    private LinearLayout lin_payee;//收款方
    private LinearLayout lin_opening_bank;//开户行
    private LinearLayout lin_account;//帐号

    private ToggleButton button;

    private ArrayList<String> attachmentPicPaths = new ArrayList<String>();
    private ImageUploadItem image;
    private ArrayList<GroupMember> approvalFriends = new ArrayList<GroupMember>();//审批人
    private ArrayList<GroupMember> copyFriends = new ArrayList<GroupMember>();//抄送人

    private String groupId;//群组id

    private AttachmentAdapter attachmentAdapter;
    private ApprovalAndCopyAdapter approvalAdapter;
    private ApprovalAndCopyAdapter copyAdapter;
    private ScrollGridView attachmentGridView;//附件
    private ScrollGridView grid_scroll_approval;//审批人
    private ScrollGridView grid_scroll_copy;//抄送
    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 改变状态栏的颜色使其与APP风格一体化
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.activity_add_purchase);
        ctx = this;
        EventBus.getDefault().register(this);
        initView();
        initData();
        functionModule();
    }

    private void initView() {
        findViewById(R.id.ll_back).setOnClickListener(this);
        findViewById(R.id.rel_choose_image).setOnClickListener(this);
        findViewById(R.id.rel_choose_time).setOnClickListener(this);
        findViewById(R.id.btn_submit_purchase_approval).setOnClickListener(this);

        //输入框
        edt_borrow_department = (EditText) findViewById(R.id.edt_borrow_department);
        edt_buy_reason = (EditText) findViewById(R.id.edt_buy_reason);
        edt_purchasing_list = (EditText) findViewById(R.id.edt_purchasing_list);
        editText2 = (EditText) findViewById(R.id.editText2);
        edt_request_num = (EditText) findViewById(R.id.edt_request_num);
        edt_payee = (EditText) findViewById(R.id.edt_payee);
        edt_opening_bank = (EditText) findViewById(R.id.edt_opening_bank);
        edt_account_number = (EditText) findViewById(R.id.edt_account_number);
        edt_finish_time = (EditText) findViewById(R.id.edt_finish_time);

        button = (ToggleButton) findViewById(R.id.toggleButton);

        lin_payee = (LinearLayout) findViewById(R.id.lin_payee);
        lin_opening_bank = (LinearLayout) findViewById(R.id.lin_opening_bank);
        lin_account = (LinearLayout) findViewById(R.id.lin_account);

        View include = findViewById(R.id.item_grid);
        attachmentGridView = (ScrollGridView) include.findViewById(R.id.noScrollgridview);
        attachmentGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        attachmentAdapter = new AttachmentAdapter(this, attachmentPicPaths, CHOOSE_PIC_MAX);
        attachmentGridView.setAdapter(attachmentAdapter);

        grid_scroll_approval = (ScrollGridView) findViewById(R.id.grid_scroll_approval);
        approvalAdapter = new ApprovalAndCopyAdapter(this, approvalFriends);
        grid_scroll_approval.setAdapter(approvalAdapter);

        grid_scroll_copy = (ScrollGridView) findViewById(R.id.grid_scroll_copy);
        copyAdapter = new ApprovalAndCopyAdapter(this, copyFriends);
        grid_scroll_copy.setAdapter(copyAdapter);
    }

    private void initData() {
        DialogUtils.showProgressDialog(this, "加载中").show();
        //请求上次的审批人和联系人
        ApprovalRequest.queryLast(getSelfActivity(), baseApplication.getLoginToken(),
                1, callBack);
    }

    private void functionModule() {
        edt_purchasing_list.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                // 解决scrollView中嵌套EditText导致不能上下滑动的问题
                view.getParent().requestDisallowInterceptTouchEvent(true);
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        view.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });

        button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //开关打开了
                    edt_request_num.setFocusableInTouchMode(true);
                    edt_request_num.setFocusable(true);
                    lin_payee.setVisibility(View.VISIBLE);
                    lin_opening_bank.setVisibility(View.VISIBLE);
                    lin_account.setVisibility(View.VISIBLE);
                } else {
                    //隐藏掉
                    edt_request_num.setFocusableInTouchMode(false);
                    edt_request_num.setFocusable(false);
                    lin_payee.setVisibility(View.GONE);
                    lin_opening_bank.setVisibility(View.GONE);
                    lin_account.setVisibility(View.GONE);
                }
            }
        });

        attachmentGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > attachmentPicPaths.size() - 1) {
                    choosePic();
                } else {
                    Intent intent = new Intent(ctx,
                            PreviewPhotoActivity.class);
                    intent.putExtra("position", "1");
                    intent.putExtra("ID", i);
                    intent.putExtra("isApproval", true);
                    intent.putStringArrayListExtra("attachmentPaths", attachmentPicPaths);
                    startActivity(intent);
                }
            }
        });

        grid_scroll_approval.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > approvalFriends.size() - 1) {
                    chooseGroupOrMember("approvalFriends");
                } else {
                    approvalFriends.remove(i);
                    approvalAdapter.modifyData(approvalFriends);
                }
            }
        });

        grid_scroll_copy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > copyFriends.size() - 1) {
                    chooseGroupOrMember("copyFriends");
                } else {
                    copyFriends.remove(i);
                    copyAdapter.modifyData(copyFriends);
                }
            }
        });
    }

    private void chooseGroupOrMember(String type) {
        if (0 == approvalFriends.size() &&
                0 == copyFriends.size()) {
            //跳转到选择群界面然后去选择人员
            Intent intent = new Intent(getSelfActivity(), ChooseGroupActivity.class);
            intent.putExtra("type", type);//区别审批人和抄送人
            startActivity(intent);
        } else {
            Intent intent = new Intent();
            //将群id传过去
            intent.setClass(getSelfActivity(), ChoosePeopleOfAddressActivity.class);
            intent.putExtra("groupId", groupId);
            intent.putExtra("type", type);//区别审批人和抄送人
            startActivity(intent);
        }
    }

    @Subscribe
    public void onUpdateAttachment(AttachmentUpdate attachmentUpdate) {
        if("update".equals(attachmentUpdate.getTag())) {
            attachmentPicPaths.clear();;
            attachmentPicPaths.addAll(attachmentUpdate.getAttachments());
            attachmentAdapter.modifyData(attachmentPicPaths);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChooseEvent(ChooseMemberEvent event) {
        if (0x11 == event.getMsgCode()) {
            //审批人选择
            ArrayList<GroupMember> infos = event.getGroupMembers();
            //判断一下抄送人中是否包含审批人
            if (0 != copyFriends.size()) {
                //审批人不为空,判断审批人和传过来的抄送人是否重复
                for (int i = 0; i < infos.size(); i++) {
                    int num = 0;//重复次数
                    for (GroupMember friendInfo : copyFriends) {
                        if (infos.get(i).getMemberId().equals(friendInfo.getMemberId())) {
                            //重复次数计算
                            num++;
                        }
                    }
                    if (num > 0) {
                        ToastUtil.doToast(getSelfActivity(), "审批人和抄送人不能相同!");
                        return;
                    }
                }
            }
            //剔除重复的审批人数据
            if (0 == approvalFriends.size()) {
                approvalFriends.addAll(infos);
            } else {
                for (int i = 0; i < infos.size(); i++) {
                    int num = 0;
                    for (GroupMember friendInfo : approvalFriends) {
                        if (infos.get(i).getMemberId().equals(friendInfo.getMemberId())) {
                            //重复次数计算
                            num++;
                        }
                    }
                    //判断
                    if (0 == num) {
                        //不重复
                        approvalFriends.add(infos.get(i));
                    }
                }
            }
            //刷新数据
            approvalAdapter.modifyData(approvalFriends);
        } else if (0x12 == event.getMsgCode()) {
            //抄送
            Log.i("CMCC", "抄送人返回");
            //抄送人选择
            ArrayList<GroupMember> infos = event.getGroupMembers();
            //判断一下审批人中是否包含抄送人
            if (0 != approvalFriends.size()) {
                //审批人不为空,判断审批人和传过来的抄送人是否重复
                for (int i = 0; i < infos.size(); i++) {
                    int num = 0;//重复次数
                    for (GroupMember friendInfo : approvalFriends) {
                        if (infos.get(i).getMemberId().equals(friendInfo.getMemberId())) {
                            //重复次数计算
                            num++;
                        }
                    }
                    if (num > 0) {
                        ToastUtil.doToast(getSelfActivity(), "审批人和抄送人不能相同!");
                        return;
                    }
                }
            }

            //剔除重复的抄送人数据
            if (0 == copyFriends.size()) {
                //抄送人为空直接添加
                copyFriends.addAll(infos);
            } else {
                for (int i = 0; i < infos.size(); i++) {
                    int num = 0;//重复次数
                    for (GroupMember friendInfo : copyFriends) {
                        if (infos.get(i).getMemberId().equals(friendInfo.getMemberId())) {
                            //重复次数计算
                            num++;
                        }
                    }
                    //判断
                    if (0 == num) {
                        //不重复
                        copyFriends.add(infos.get(i));
                    }
                }
            }
            //刷新数据
            copyAdapter.modifyData(copyFriends);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGroupEvent(ChooseGroupEvent event) {
        groupId = event.getGroupId();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finishCurrentActivity();
                break;
            case R.id.rel_choose_image:
                choosePic();
                break;
            case R.id.rel_choose_time:
                //时间选择器
                TimePickerView pvTime = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
                //控制时间范围
                Calendar calendar = Calendar.getInstance();
                pvTime.setRange(calendar.get(Calendar.YEAR) - 20, calendar.get(Calendar.YEAR));//要在setTime 之前才有效果哦
                pvTime.setTime(new Date());
                pvTime.setCyclic(false);
                pvTime.setCancelable(true);
                //时间选择后回调
                pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

                    @Override
                    public void onTimeSelect(Date date) {
                        Date curDate = new Date(System.currentTimeMillis());
                        String curDateString = getTime(curDate);
                        String chooseDateString = getTime(date);
                        if(curDateString.compareTo(chooseDateString) > 0) {
                            ToastUtil.doToast(AddPurchaseApprovalActivity2.this,"不可以选择今日之前的日期");
                        }else{
                            edt_finish_time.setText(chooseDateString);
                        }
                    }
                });
                pvTime.show();
                break;
            case R.id.btn_submit_purchase_approval:
                //新建采购审批
                createPurchaseApproval();
                break;
        }
    }

    /**
     * 格式化时间
     *
     * @param date
     * @return
     */
    private String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    private void choosePic() {
        int havePicSize = attachmentPicPaths.size();
        if(CHOOSE_PIC_MAX > havePicSize) {
            int chooseNum = CHOOSE_PIC_MAX - havePicSize;
            //选择图片
            Intent intent = new Intent(ctx, PhotoPickerActivity.class);
            intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, true);
            intent.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE, PhotoPickerActivity.MODE_MULTI);
            intent.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN, chooseNum);
            // 总共选择的图片数量
//            intent.putExtra(PhotoPickerActivity.TOTAL_MAX_MUN, Bimp.tempSelectBitmap.size());
            startActivityForResult(intent, PICK_PHOTO);
        }else{
            ToastUtil.doToast(AddPurchaseApprovalActivity2.this,"选择已经达到上限");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO) {
            if (resultCode == RESULT_OK) {
                ArrayList<String> result = data.getStringArrayListExtra(PhotoPickerActivity.KEY_RESULT);
                for(String picPath : result) {
                    if(!attachmentPicPaths.contains(picPath)){
                        attachmentPicPaths.add(picPath);

                    }
                }

                attachmentAdapter.modifyData(attachmentPicPaths);
            }
        }
    }

    QueryLastCallBack callBack = new QueryLastCallBack() {
        @Override
        public void success(String msg, LastApproval approval) {
            DialogUtils.closeProgressDialog();
            //ToastUtil.doToast(getSelfActivity(), "请求上次的审批人和抄送人成功");
            //转为FriendInfo对象
            if(null!=approvalFriends&&approvalFriends.size()>0){
                approvalFriends.clear();
            }
            if(null!=copyFriends&&copyFriends.size()>0){
                copyFriends.clear();
            }
            if (!ObjectUtils.isNull(approval)) {
                groupId = approval.getGroupId();
                if (!ObjectUtils.isNull(groupId)) {
                    Log.i("CMCC", "groupId:" + groupId);
                }
                ArrayList<ApprovalOrCopy> approvals = approval.getApproverList();
                ArrayList<ApprovalOrCopy> copys = approval.getCopyList();
                if (!ObjectUtils.isNull(approvals)) {
                    for (ApprovalOrCopy approvalOrCopy : approvals) {
                        GroupMember info = new GroupMember();
                        info.setMemberId(approvalOrCopy.getJobNumber());
                        info.setMemberName(approvalOrCopy.getName());
                        info.setMemberUrl(approvalOrCopy.getFaceUrl());
                        approvalFriends.add(info);
                    }
                }
                if (!ObjectUtils.isNull(copys)) {
                    for (ApprovalOrCopy orCopy : copys) {
                        GroupMember info = new GroupMember();
                        info.setMemberId(orCopy.getJobNumber());
                        info.setMemberName(orCopy.getName());
                        info.setMemberUrl(orCopy.getFaceUrl());
                        copyFriends.add(info);
                    }
                }
            }
            //更新UI
            approvalAdapter.modifyData(approvalFriends);
            copyAdapter.modifyData(copyFriends);
//            approvalAdapter.notifyDataSetChanged();
//            copyAdapter.notifyDataSetChanged();
        }

        @Override
        public void fail(String msg) {
            DialogUtils.closeProgressDialog();
            ToastUtil.doToast(getSelfActivity(), "请求上次的审批人和抄送人失败," + msg);
        }

        @Override
        public void connectFail() {
            DialogUtils.closeProgressDialog();
            ToastUtil.doToast(getSelfActivity(), "请求上次的审批人和抄送人connectFail");
        }
    };

    private ArrayList<Approver> approvers = new ArrayList<>();//上传的审批人集合
    private ArrayList<Approver> copyApprovers = new ArrayList<>();//上传的抄送人集合
    private AddApprovalObject object = new AddApprovalObject();//提交的审批对象
    private ArrayList<String> imageUrls = new ArrayList<>();
    private List<ImageUploadItem> imageitems = new ArrayList<>();
    /**
     * 提交采购审批
     */
    private void createPurchaseApproval() {

        if (ObjectUtils.isNull(edt_borrow_department.getText().toString())) {
            toast("请款部门不能为空!");
            return;
        }
        if (ObjectUtils.isNull(edt_buy_reason.getText().toString())) {
            toast("用途说明不能为空!");
            return;
        }
        if (ObjectUtils.isNull(edt_purchasing_list.getText().toString())) {
            toast("采购清单不能为空!");
            return;
        }
//        if (ObjectUtils.isNull(edt_finish_time.getText().toString())) {
//            toast("完成日期不能为空!");
//            return;
//        }
        //构建审批人列表和抄送人列表
        if (0 == approvalFriends.size()) {
            ToastUtil.doToast(getSelfActivity(), "审批人列表不能为空");
            return;
        }
        if (approvalFriends.size() > 0) {
            for (int i = 0; i < approvalFriends.size(); i++) {
                Approver approver = new Approver();
                approver.setJobNumber(approvalFriends.get(i).getMemberId());
                approver.setPriority(i + 1);
                approvers.add(approver);
            }
            object.setApproverList(approvers);
        }
//        if (0 == copyFriends.size()) {
//            ToastUtil.doToast(getSelfActivity(), "抄送人列表不能为空");
//            return;
//        }
        if (copyFriends.size() > 0) {
            for (int i = 0; i < copyFriends.size(); i++) {
                Approver approver = new Approver();
                approver.setJobNumber(copyFriends.get(i).getMemberId());
                approver.setPriority(i + 1);
                copyApprovers.add(approver);
            }
            object.setCopyerList(copyApprovers);
        } else {
            object.setCopyerList(null);
        }

        //判断请款金额开关是否打开
        if (button.isChecked()) {
            //打开的
            if (ObjectUtils.isNull(edt_request_num.getText().toString())) {
                toast("请款金额不能为空!");
                return;
            }
            if (ObjectUtils.isNull(edt_payee.getText().toString())) {
                toast("收款方不能为空!");
                return;
            }
            if (ObjectUtils.isNull(edt_opening_bank.getText().toString())) {
                toast("开户行不能为空!");
                return;
            }
            if (ObjectUtils.isNull(edt_account_number.getText().toString())) {
                toast("帐号不能为空!");
                return;
            }

//            if (ObjectUtils.isNull(imageUrls)) {
//                ToastUtil.doToast(getSelfActivity(), "上传图片失败!");
//                return;
//            }

            object.setAmountMonney(edt_request_num.getText().toString());
            object.setBankAccount(edt_account_number.getText().toString());
            object.setBankName(edt_opening_bank.getText().toString());
            object.setBankPerson(edt_payee.getText().toString());
        }

        object.setAttachmentList(null);
        object.setDepartment(edt_borrow_department.getText().toString());
        object.setFinishTime(edt_finish_time.getText().toString());
        object.setPurchaseList(edt_purchasing_list.getText().toString());
        object.setRemark(editText2.getText().toString());
        object.setSubFormList(null);
        object.setTitle(edt_buy_reason.getText().toString());
        object.setType(1);
        object.setGroupId(groupId);

        //提交图片获得图片url
        if (attachmentPicPaths.size() >= 1) {
           // Log.i("CMCC", "图片不为空," + attachmentPicPaths.size());
            DialogUtils.showProgressDialog(getSelfActivity(), "正在提交中").show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    getUrl(attachmentPicPaths);
                    uploadPic();
                }
            }).start();

        } else {
            //Log.i("CMCC", "新建采购审批22222222222");
            object.setAttachmentList(null);
            DialogUtils.showProgressDialog(getSelfActivity(), "正在提交中").show();
            ApprovalRequest.addApproval(getSelfActivity(), baseApplication.getLoginToken(),
                    1, object, addCallBack);
        }


    }

    /**
     * 获取上传图片的url
     *
     * @param attachments
     */
    private void getUrl(ArrayList<String> attachments) {
        for (int i = 0; i < attachments.size(); i++) {
            // 压缩图片
            //Bitmap bitmap = BitmapLoadUtils.decodeSampledBitmapFromFd(attachments.get(i), 400, 500);
            Bitmap bitmap = null;
            try {
                bitmap = BitmapUtils.revitionImageSize(attachments.get(i));
            } catch (IOException e) {
                e.printStackTrace();
            }
            //if(null!=bitmap)
            setPicToView(bitmap, i);
        }
    }

    /**
     * 上传图片
     */
    private void uploadPic() {
        Map<String, Object> map = new HashMap<>();
        map.put("files", imageitems);
        UpLoadPicRequest.request(getSelfActivity(), map, new UpLoadPicCallBack() {
            @Override
            public void success(List<PicDataBean> bean) {
                //DialogUtils.closeProgressDialog();
                if (null != bean && bean.size() > 0) {
                    for (int i = 0; i < bean.size(); i++) {
                        String imgUrl = bean.get(i).getImgUrl();
                        imageUrls.add(imgUrl);
                    }
                }

                ArrayList<ImageUrl> urls = new ArrayList<ImageUrl>();
                for (String img : imageUrls) {
                    ImageUrl url = new ImageUrl();
                    url.setFileUrl(img);
                    urls.add(url);
                }

                object.setAttachmentList(urls);
                //DialogUtils.showProgressDialog(getSelfActivity(), "正在提交中...");
                ApprovalRequest.addApproval(getSelfActivity(), baseApplication.getLoginToken(),
                        1, object, addCallBack);

            }

            @Override
            public void fail(String msg) {
                Log.i("CMCC", "uploadPic:" + msg);
                DialogUtils.closeProgressDialog();
            }

            @Override
            public void connectFail() {
                Log.i("CMCC", "connectFail:");
                DialogUtils.closeProgressDialog();
            }
        });
    }

    private void setPicToView(Bitmap bitmap, int pos) {
         image = new ImageUploadItem();
        //String filename = System.currentTimeMillis() + ".jpg";
        //String filePath = FileUtils.savePic(getSelfActivity(), filename, bitmap);
        String filePath = FileUtils.saveFile(getSelfActivity(), "img"+pos+".jpg", bitmap);

        if (!ObjectUtils.isNull(filePath)) {
            File file = new File(filePath);
            //file转化成二进制
            byte[] buffer = null;
            FileInputStream in;
            int length = 0;
            try {
                in = new FileInputStream(file);
                buffer = new byte[(int) file.length() + 100];
                length = in.read(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String data = Base64.encodeToString(buffer, 0, length, Base64.NO_WRAP);

            image.setFileContent(data);
            image.setFileId(pos + "");
            image.setFileName(filePath);
            image.setFileType(".jpg");

            imageitems.add(image);
        }
    }

    AddApprovalCallBack addCallBack = new AddApprovalCallBack() {
        @Override
        public void success(String msg, String data) {
            DialogUtils.closeProgressDialog();
            toast("新建采购审批成功!");
            finish();
        }

        @Override
        public void fail(String msg) {
            //Log.i("CMCC", "新建采购审批失败," + msg);
            DialogUtils.closeProgressDialog();
        }

        @Override
        public void connectFail() {
            //Log.i("CMCC", "新建采购审批connectFail");
            DialogUtils.closeProgressDialog();
        }
    };
}
