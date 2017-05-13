package huanxing_print.com.cn.printhome.ui.activity.approval;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bigkoo.pickerview.TimePickerView;
import com.bumptech.glide.Glide;

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
import huanxing_print.com.cn.printhome.log.Logger;
import huanxing_print.com.cn.printhome.model.approval.AddApprovalObject;
import huanxing_print.com.cn.printhome.model.approval.ApprovalOrCopy;
import huanxing_print.com.cn.printhome.model.approval.Approver;
import huanxing_print.com.cn.printhome.model.approval.LastApproval;
import huanxing_print.com.cn.printhome.model.comment.PicDataBean;
import huanxing_print.com.cn.printhome.model.contact.FriendInfo;
import huanxing_print.com.cn.printhome.model.image.ImageUploadItem;
import huanxing_print.com.cn.printhome.model.picupload.ImageItem;
import huanxing_print.com.cn.printhome.net.callback.approval.AddApprovalCallBack;
import huanxing_print.com.cn.printhome.net.callback.approval.QueryLastCallBack;
import huanxing_print.com.cn.printhome.net.callback.comment.UpLoadPicCallBack;
import huanxing_print.com.cn.printhome.net.request.approval.ApprovalRequest;
import huanxing_print.com.cn.printhome.net.request.commet.UpLoadPicRequest;
import huanxing_print.com.cn.printhome.ui.activity.copy.PhotoPickerActivity;
import huanxing_print.com.cn.printhome.ui.activity.copy.PreviewPhotoActivity;
import huanxing_print.com.cn.printhome.util.CircleTransform;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.FileUtils;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;
import huanxing_print.com.cn.printhome.util.picuplload.Bimp;
import huanxing_print.com.cn.printhome.util.picuplload.BitmapLoadUtils;
import huanxing_print.com.cn.printhome.view.ScrollGridView;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;
import huanxing_print.com.cn.printhome.view.imageview.RoundImageView;


/**
 * 新增采购审批
 */
public class AddPurchaseApprovalActivity extends BaseActivity implements View.OnClickListener {

    private EditText edt_borrow_department;//请款部门
    private EditText edt_buy_reason;//采购事由
    private EditText edt_purchasing_list;//采购清单
    private EditText edt_request_num;//请款金额
    private LinearLayout lin_payee;//收款方
    private LinearLayout lin_opening_bank;//开户行
    private LinearLayout lin_account;//帐号
    private EditText edt_finish_time;//结束时间
    private EditText edt_payee;//收款方
    private EditText edt_opening_bank;//开户行
    private EditText edt_account_number;//帐号
    private EditText editText2;//备注
    private ToggleButton button;
    private List<Bitmap> mResults = new ArrayList<>();
    private GridView noScrollgridview;
    private ScrollGridView grid_scroll_approval;//审批人
    private ScrollGridView grid_scroll_copy;//抄送
    private GridViewApprovalAdapter approvalAdapter;
    private GridViewCopyAdapter copyAdapter;
    private GridAdapter adapter;
    public static Bitmap bimap;
    private Context ctx;
    private static final int PICK_PHOTO = 1;
    private ArrayList<FriendInfo> friends = new ArrayList<FriendInfo>();//生成的假数据
    private ArrayList<FriendInfo> approvalFriends = new ArrayList<FriendInfo>();//审批人
    private ArrayList<FriendInfo> copyFriends = new ArrayList<FriendInfo>();//抄送人
    private int CODE_APPROVAL_REQUEST = 0X11;//审批人请求码
    private int CODE_COPY_REQUEST = 0X12;//抄送人人请求码
    private List<ImageUploadItem> imageitems = new ArrayList<>();
    private List<String> imageUrls = new ArrayList<>();
    private ArrayList<Approver> approvers = new ArrayList<>();//上传的审批人集合
    private ArrayList<Approver> copyApprovers = new ArrayList<>();//上传的抄送人集合
    private AddApprovalObject object = new AddApprovalObject();//提交的审批对象
    private String groupId;//群组id


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

        initData();
        //getData();
        functionModule();
    }

    private void functionModule() {

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

        noScrollgridview.setAdapter(adapter);
        noScrollgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == mResults.size() - 1 || i == Bimp.tempSelectBitmap.size()) {
                    Intent intent = new Intent(ctx, PhotoPickerActivity.class);
                    intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, true);
                    intent.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE, PhotoPickerActivity.MODE_MULTI);
                    intent.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN, PhotoPickerActivity.DEFAULT_NUM);
                    // 总共选择的图片数量
                    intent.putExtra(PhotoPickerActivity.TOTAL_MAX_MUN, Bimp.tempSelectBitmap.size());
                    startActivityForResult(intent, PICK_PHOTO);
                } else {
                    Intent intent = new Intent(ctx,
                            PreviewPhotoActivity.class);
                    intent.putExtra("position", "1");
                    intent.putExtra("ID", i);
                    startActivity(intent);
                }
            }
        });

        //审批人
        grid_scroll_approval.setAdapter(approvalAdapter);
        grid_scroll_approval.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == approvalFriends.size()) {
                    //跳转到选择联系人界面
                    Intent intent = new Intent(getSelfActivity(), ChoosePeopleOfAddressActivity.class);
                    intent.putExtra("groupId",groupId);
                    startActivityForResult(intent, CODE_APPROVAL_REQUEST);
                } else {
                    approvalFriends.remove(position);
                    approvalAdapter.notifyDataSetChanged();
                }
            }
        });

        //抄送人
        grid_scroll_copy.setAdapter(copyAdapter);
        grid_scroll_copy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == (copyFriends.size())) {
                    //跳转到选择联系人界面
                    Intent intent = new Intent(getSelfActivity(), ChoosePeopleOfAddressActivity.class);
                    intent.putExtra("groupId",groupId);
                    startActivityForResult(intent, CODE_COPY_REQUEST);
                } else {
                    copyFriends.remove(position);
                    copyAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void initData() {

        grid_scroll_approval = (ScrollGridView) findViewById(R.id.grid_scroll_approval);
        grid_scroll_copy = (ScrollGridView) findViewById(R.id.grid_scroll_copy);
        edt_request_num = (EditText) findViewById(R.id.edt_request_num);
        lin_payee = (LinearLayout) findViewById(R.id.lin_payee);
        lin_opening_bank = (LinearLayout) findViewById(R.id.lin_opening_bank);
        lin_account = (LinearLayout) findViewById(R.id.lin_account);
        edt_finish_time = (EditText) findViewById(R.id.edt_finish_time);
        edt_borrow_department = (EditText) findViewById(R.id.edt_borrow_department);
        edt_buy_reason = (EditText) findViewById(R.id.edt_buy_reason);
        edt_purchasing_list = (EditText) findViewById(R.id.edt_purchasing_list);
        edt_payee = (EditText) findViewById(R.id.edt_payee);
        edt_opening_bank = (EditText) findViewById(R.id.edt_opening_bank);
        edt_account_number = (EditText) findViewById(R.id.edt_account_number);
        editText2 = (EditText) findViewById(R.id.editText2);

        findViewById(R.id.rel_choose_image).setOnClickListener(this);
        findViewById(R.id.rel_choose_time).setOnClickListener(this);
        findViewById(R.id.btn_submit_purchase_approval).setOnClickListener(this);
        //返回
        View view = findViewById(R.id.back);
        view.findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        button = (ToggleButton) findViewById(R.id.toggleButton);

        View include = findViewById(R.id.item_grid);
        noScrollgridview = (GridView) include.findViewById(R.id.noScrollgridview);
        noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));

        adapter = new GridAdapter(this);
        adapter.update();

        approvalAdapter = new GridViewApprovalAdapter();
        copyAdapter = new GridViewCopyAdapter();

        bimap = BitmapFactory.decodeResource(getResources(), R.drawable.add);
        mResults.add(bimap);

        //请求上次的审批人和联系人
        ApprovalRequest.queryLast(getSelfActivity(), baseApplication.getLoginToken(),
                1, callBack);

    }

    QueryLastCallBack callBack = new QueryLastCallBack() {
        @Override
        public void success(String msg, LastApproval approval) {
            //ToastUtil.doToast(getSelfActivity(), "请求上次的审批人和抄送人成功");
            //转为FriendInfo对象
            if (!ObjectUtils.isNull(approval)) {
                groupId=approval.getGroupId();
                ArrayList<ApprovalOrCopy> approvals = approval.getApproverList();
                ArrayList<ApprovalOrCopy> copys = approval.getCopyList();
                if (!ObjectUtils.isNull(approvals)) {
                    for (ApprovalOrCopy approvalOrCopy : approvals) {
                        FriendInfo info = new FriendInfo();
                        info.setMemberId(approvalOrCopy.getJobNumber());
                        info.setMemberName(approvalOrCopy.getName());
                        info.setMemberUrl(approvalOrCopy.getFaceUrl());
                        approvalFriends.add(info);
                    }
                }
                if (!ObjectUtils.isNull(copys)) {
                    for (ApprovalOrCopy orCopy : copys) {
                        FriendInfo info = new FriendInfo();
                        info.setMemberId(orCopy.getJobNumber());
                        info.setMemberName(orCopy.getName());
                        info.setMemberUrl(orCopy.getFaceUrl());
                        copyFriends.add(info);
                    }
                }
            }
            //更新UI
            approvalAdapter.notifyDataSetChanged();
            copyAdapter.notifyDataSetChanged();
        }

        @Override
        public void fail(String msg) {
            ToastUtil.doToast(getSelfActivity(), "请求上次的审批人和抄送人失败," + msg);
        }

        @Override
        public void connectFail() {
            ToastUtil.doToast(getSelfActivity(), "请求上次的审批人和抄送人connectFail");
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rel_choose_image:
                //选择图片
                Intent intent = new Intent(ctx, PhotoPickerActivity.class);
                intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, true);
                intent.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE, PhotoPickerActivity.MODE_MULTI);
                intent.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN, 5);
                // 总共选择的图片数量
                intent.putExtra(PhotoPickerActivity.TOTAL_MAX_MUN, Bimp.tempSelectBitmap.size());
                startActivityForResult(intent, PICK_PHOTO);
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
                        edt_finish_time.setText(getTime(date));
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
     * 提交采购审批
     */
    private void createPurchaseApproval() {

        if (ObjectUtils.isNull(edt_borrow_department.getText().toString())) {
            toast("请款部门不能为空!");
            return;
        }
        if (ObjectUtils.isNull(edt_buy_reason.getText().toString())) {
            toast("采购事项不能为空!");
            return;
        }
        if (ObjectUtils.isNull(edt_purchasing_list.getText().toString())) {
            toast("采购清单不能为空!");
            return;
        }
        if (ObjectUtils.isNull(edt_finish_time.getText().toString())) {
            toast("完成日期不能为空!");
            return;
        }
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
        }
        if (0 == copyFriends.size()) {
            ToastUtil.doToast(getSelfActivity(), "抄送人列表不能为空");
            return;
        }
        if (copyFriends.size() > 0) {
            for (int i = 0; i < copyFriends.size(); i++) {
                Approver approver = new Approver();
                approver.setJobNumber(copyFriends.get(i).getMemberId());
                approver.setPriority(i + 1);
                copyApprovers.add(approver);
            }
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

            if (ObjectUtils.isNull(imageUrls)) {
                ToastUtil.doToast(getSelfActivity(), "上传图片失败!");
                return;
            }
            object.setAmountMonney(edt_request_num.getText().toString());
            object.setBankAccount(edt_account_number.getText().toString());
            object.setBankName(edt_opening_bank.getText().toString());
            object.setBankPerson(edt_payee.getText().toString());

        }

        object.setApproverList(approvers);
        //object.setAttachmentList(null);
        object.setCopyerList(copyApprovers);
        object.setDepartment(edt_borrow_department.getText().toString());
        object.setFinishTime(edt_finish_time.getText().toString());
        object.setPurchaseList(edt_purchasing_list.getText().toString());
        object.setRemark(editText2.getText().toString());
        //object.setSubFormList(null);
        object.setTitle(edt_buy_reason.getText().toString());
        object.setType(1);

        //提交图片获得图片url
        if (mResults.size() > 0) {
            DialogUtils.showProgressDialog(getSelfActivity(), "正在提交中").show();
            ArrayList<ImageItem> items = Bimp.tempSelectBitmap;
            getUrl(items);
            uploadPic();
        }


    }

    AddApprovalCallBack addCallBack = new AddApprovalCallBack() {
        @Override
        public void success(String msg, String data) {
            DialogUtils.closeProgressDialog();
            Logger.i("新建采购审批id:" + data);
            //ToastUtil.doToast(getSelfActivity(), );
        }

        @Override
        public void fail(String msg) {
            Logger.i("新建采购审批失败," + msg);
            //ToastUtil.doToast(getSelfActivity(), "新建采购审批失败," + msg);
            DialogUtils.closeProgressDialog();
        }

        @Override
        public void connectFail() {
            Logger.i("新建采购审批connectFail");
            //ToastUtil.doToast(getSelfActivity(), "新建采购审批connectFail");
            DialogUtils.closeProgressDialog();
        }
    };

    /**
     * 获取上传图片的url
     *
     * @param items
     */
    private void getUrl(ArrayList<ImageItem> items) {
        for (int i = 0; i < items.size(); i++) {
            Bitmap bitmap = items.get(i).getBitmap();
            setPicToView(bitmap, i + "");
        }
    }

    private void setPicToView(Bitmap bitmap, String fileid) {
        ImageUploadItem image = new ImageUploadItem();
        String filename = System.currentTimeMillis() + "";
        String filePath = FileUtils.savePic(getSelfActivity(), filename + ".jpg", bitmap);
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
            String data = Base64.encodeToString(buffer, 0, length, Base64.DEFAULT);
            image.setFileContent(data);
            image.setFileId(fileid + "");
            image.setFileName(filename);
            image.setFileType(".jpg");

            imageitems.add(image);
        }
    }

    /**
     * 上传图片
     */
    private void uploadPic() {
        Map<String, Object> map = new HashMap<>();
        map.put("files", imageitems);
        //DialogUtils.showProgressDialog(getSelfActivity(), "正在上传图片中...");
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

                if (imageUrls.size() > 0) {
                    object.setAttachmentList((ArrayList<String>) imageUrls);
                    ApprovalRequest.addApproval(getSelfActivity(), baseApplication.getLoginToken(),
                            1, object, addCallBack);
                }
            }

            @Override
            public void fail(String msg) {
                //DialogUtils.closeProgressDialog();
            }

            @Override
            public void connectFail() {
                //DialogUtils.closeProgressDialog();
            }
        });
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO) {
            if (resultCode == RESULT_OK) {
                ArrayList<String> result = data.getStringArrayListExtra(PhotoPickerActivity.KEY_RESULT);
                showResult(result);
            }
        }
        if (requestCode == CODE_APPROVAL_REQUEST &&
                resultCode == RESULT_OK) {
            Log.i("CMCC", "审批人返回");
            if (data != null) {
                //审批人选择
                ArrayList<FriendInfo> infos = data.getParcelableArrayListExtra("FriendInfo");
                //判断一下抄送人中是否包含审批人
                if (0 != copyFriends.size()) {
                    //审批人不为空,判断审批人和传过来的抄送人是否重复
                    for (int i = 0; i < infos.size(); i++) {
                        int num = 0;//重复次数
                        for (FriendInfo friendInfo : copyFriends) {
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
                        for (FriendInfo friendInfo : approvalFriends) {
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
                approvalAdapter.notifyDataSetChanged();
            }
        }
        if (requestCode == CODE_COPY_REQUEST &&
                resultCode == RESULT_OK) {
            Log.i("CMCC", "抄送人返回");
            //抄送人选择
            ArrayList<FriendInfo> infos = data.getParcelableArrayListExtra("FriendInfo");
            //判断一下审批人中是否包含抄送人
            if (0 != approvalFriends.size()) {
                //审批人不为空,判断审批人和传过来的抄送人是否重复
                for (int i = 0; i < infos.size(); i++) {
                    int num = 0;//重复次数
                    for (FriendInfo friendInfo : approvalFriends) {
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
                    for (FriendInfo friendInfo : copyFriends) {
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
            copyAdapter.notifyDataSetChanged();

        }
    }

    private void showResult(ArrayList<String> paths) {
        if (mResults == null) {
            mResults = new ArrayList<>();
        }
        if (paths.size() != 0) {
            mResults.remove(mResults.size() - 1);
        }
        for (int i = 0; i < paths.size(); i++) {
            // 压缩图片
            Bitmap bitmap = BitmapLoadUtils.decodeSampledBitmapFromFd(paths.get(i), 400, 500);
            // 针对小图也可以不压缩
//            Bitmap bitmap = BitmapFactory.decodeFile(paths.get(i));
            mResults.add(bitmap);

            ImageItem takePhoto = new ImageItem();
            takePhoto.setBitmap(bitmap);
            Bimp.tempSelectBitmap.add(takePhoto);
        }
        mResults.add(BitmapFactory.decodeResource(getResources(), R.drawable.add));
        adapter.notifyDataSetChanged();
    }


    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    protected void onRestart() {
        adapter.update();
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        int gvHeight = 0;
        if (adapter.getCount() < 5) {
            gvHeight = dip2px(ctx, 60);
        } else if (adapter.getCount() < 9) {
            gvHeight = dip2px(ctx, 125);
        } else {
            gvHeight = dip2px(ctx, 190);
        }

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, gvHeight);
        noScrollgridview.setLayoutParams(lp);
    }

    /**
     * description: 审批人
     * author LSW
     * date 2017/5/8 11:17
     * update 2017/5/8
     */
    private class GridViewApprovalAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return approvalFriends.size() + 1;
        }

        @Override
        public Object getItem(int position) {
            return approvalFriends.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            GridViewApprovalAdapter.ViewHolder holder = null;
            if (convertView == null) {
                holder = new GridViewApprovalAdapter.ViewHolder();
                convertView = LayoutInflater.from(AddPurchaseApprovalActivity.this).inflate(
                        R.layout.item_grid_approval, null);
                holder.round_head_image = (RoundImageView) convertView.findViewById(R.id.round_head_image);
                holder.txt_name = (TextView) convertView.findViewById(R.id.txt_name);
                convertView.setTag(holder);
            } else {
                holder = (GridViewApprovalAdapter.ViewHolder) convertView.getTag();
            }
            //holder.round_head_image.setImageBitmap(approvals.get(position));
            if (position == approvalFriends.size()) {
                //这里是加号
                Glide.with(getSelfActivity())
                        .load(R.drawable.add_people)
                        .centerCrop()
                        .transform(new CircleTransform(getSelfActivity()))
                        .crossFade()
                        .into(holder.round_head_image);
                //添加图标
                holder.txt_name.setVisibility(View.GONE);
            } else {
                Glide.with(getSelfActivity())
                        .load(approvalFriends.get(position).getMemberUrl())
                        .centerCrop()
                        .transform(new CircleTransform(getSelfActivity()))
                        .crossFade()
                        .placeholder(R.drawable.iv_head)
                        .into(holder.round_head_image);
                holder.txt_name.setVisibility(View.VISIBLE);
                holder.txt_name.setText(approvalFriends.get(position).getMemberName());
            }

            return convertView;
        }

        class ViewHolder {
            RoundImageView round_head_image;
            TextView txt_name;
        }
    }

    /**
     * description: 抄送
     * author LSW
     * date 2017/5/8 11:17
     * update 2017/5/8
     */
    private class GridViewCopyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return copyFriends.size() + 1;
        }

        @Override
        public Object getItem(int position) {
            return copyFriends.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            GridViewCopyAdapter.ViewHolder holder = null;
            if (convertView == null) {
                holder = new GridViewCopyAdapter.ViewHolder();
                convertView = LayoutInflater.from(AddPurchaseApprovalActivity.this).inflate(
                        R.layout.item_grid_approval, null);
                holder.round_head_image = (RoundImageView) convertView.findViewById(R.id.round_head_image);
                holder.txt_name = (TextView) convertView.findViewById(R.id.txt_name);
                convertView.setTag(holder);
            } else {
                holder = (GridViewCopyAdapter.ViewHolder) convertView.getTag();
            }
            if (position == copyFriends.size()) {
                //这里是加号
                Glide.with(getSelfActivity())
                        .load(R.drawable.add_people)
                        .centerCrop()
                        .transform(new CircleTransform(getSelfActivity()))
                        .crossFade()
                        .into(holder.round_head_image);
                //添加图标
                holder.txt_name.setVisibility(View.GONE);
            } else {
                Glide.with(getSelfActivity())
                        .load(copyFriends.get(position).getMemberUrl())
                        .placeholder(R.drawable.iv_head)
                        .centerCrop()
                        .transform(new CircleTransform(getSelfActivity()))
                        .crossFade()
                        .into(holder.round_head_image);
                holder.txt_name.setVisibility(View.VISIBLE);
                holder.txt_name.setText(copyFriends.get(position).getMemberName());
            }
            return convertView;
        }

        class ViewHolder {
            RoundImageView round_head_image;
            TextView txt_name;
        }
    }


    /**
     * 适配器
     */
    public class GridAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        public GridAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void update() {
            loading();
        }

        public int getCount() {
            if (Bimp.tempSelectBitmap.size() == 9) {
                return 9;
            }
            return (Bimp.tempSelectBitmap.size() + 1);
        }

        public Object getItem(int arg0) {
            return mResults.get(arg0);
        }

        public long getItemId(int arg0) {
            return arg0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_gridview, null);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView
                        .findViewById(R.id.imageView1);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (position == Bimp.tempSelectBitmap.size()) {
                holder.image.setImageBitmap(BitmapFactory.decodeResource(
                        getResources(), R.drawable.add));
                if (position == 9) {
                    holder.image.setVisibility(View.GONE);
                }
            } else {
                holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position).getBitmap());
            }
            return convertView;
        }

        public class ViewHolder {
            public ImageView image;
        }

        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        adapter.notifyDataSetChanged();
                        break;
                }
                super.handleMessage(msg);
            }
        };

        public void loading() {
            new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        if (Bimp.max == Bimp.tempSelectBitmap.size()) {
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                            break;
                        } else {
                            Bimp.max += 1;
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                        }
                    }
                }
            }).start();
        }
    }

    /**
     * 假数据，后面要删除
     */
    private ArrayList<FriendInfo> getData() {
        FriendInfo data01 = new FriendInfo();
        data01.setMemberName("汪浩");
        data01.setMemberId("1");
        data01.setMemberUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494660151&di=fc28cd4cd681bb1d70df6ff6654791ff&imgtype=jpg&er=1&src=http%3A%2F%2Fimgsrc.baidu.com%2Fforum%2Fw%253D580%2Fsign%3D8c03c118ca8065387beaa41ba7dda115%2Fc17fc0bf6c81800a06c8cd58b13533fa828b4759.jpg");

        FriendInfo data02 = new FriendInfo();
        data02.setMemberName("陆成宋");
        data02.setMemberId("2");
        data02.setMemberUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494065434200&di=7c53b18639aa82a8a58a296b9502d4ee&imgtype=0&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fzhidao%2Fwh%253D450%252C600%2Fsign%3D7048a12f9e16fdfad839ceea81bfa062%2F6a63f6246b600c3350e384cc194c510fd9f9a118.jpg");

        FriendInfo data03 = new FriendInfo();
        data03.setMemberName("123");
        data03.setMemberId("3");
        data03.setMemberUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494065546496&di=a861d2debdefd088f50efa05393043dc&imgtype=jpg&src=http%3A%2F%2Fimg3.imgtn.bdimg.com%2Fit%2Fu%3D893187487%2C386198762%26fm%3D214%26gp%3D0.jpg");

        FriendInfo data04 = new FriendInfo();
        data04.setMemberName("汪浩01");
        data04.setMemberId("4");
        data04.setMemberUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494065434199&di=85b82a89a5b9cb3403033e90dc2dc2a1&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fforum%2Fw%253D580%2Fsign%3Dddf0103f252dd42a5f0901a3333a5b2f%2Fa4a8805494eef01f30f35d93e0fe9925bd317da3.jpg");

        FriendInfo data05 = new FriendInfo();
        data05.setMemberName("陆成宋01");
        data05.setMemberId("5");
        data05.setMemberUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494065434198&di=83e45ffe0f07e6336bbbd1cdc284e9a5&imgtype=0&src=http%3A%2F%2Fwenwen.soso.com%2Fp%2F20140404%2F20140404111309-1793362574.jpg");

        FriendInfo data06 = new FriendInfo();
        data06.setMemberName("陆成宋02");
        data06.setMemberId("6");
        data06.setMemberUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494065730069&di=12540a26599230b583e0bf4f477cc8d7&imgtype=0&src=http%3A%2F%2Fwww.qxjlm.com%2Ftupians%2Fbd16072941.jpg");

        FriendInfo data07 = new FriendInfo();
        data07.setMemberName("陆成宋03");
        data07.setMemberId("7");
        data07.setMemberUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494065434198&di=2271e06f1c39be89ce18f3ab9746c7d1&imgtype=0&src=http%3A%2F%2Fwww.lsswgs.com%2Fqqwebhimgs%2Fuploads%2Fbd24449651.jpg");

        FriendInfo data08 = new FriendInfo();
        data08.setMemberName("陆成宋04");
        data08.setMemberId("8");
        data08.setMemberUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494065662993&di=1d4fabe377e894277d005e17818ea64b&imgtype=jpg&src=http%3A%2F%2Fimg3.imgtn.bdimg.com%2Fit%2Fu%3D575752541%2C1102211525%26fm%3D214%26gp%3D0.jpg");

        FriendInfo data09 = new FriendInfo();
        data09.setMemberName("陆成宋05");
        data09.setMemberId("9");
        data09.setMemberUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494065434197&di=9043dfda20c52ecfa1676e3999658669&imgtype=0&src=http%3A%2F%2Fwenwen.soso.com%2Fp%2F20111030%2F20111030173922-1579981974.jpg");

        FriendInfo data10 = new FriendInfo();
        data10.setMemberName("陆成宋06");
        data10.setMemberId("10");
        data10.setMemberUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494065434197&di=af5f1a36863ac751b71b1e167e57a8e7&imgtype=0&src=http%3A%2F%2Fwenwen.soso.com%2Fp%2F20131201%2F20131201114242-1190841548.jpg");

        FriendInfo data11 = new FriendInfo();
        data11.setMemberName("陆成宋07");
        data11.setMemberId("11");
        data11.setMemberUrl("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3609186921,1453144627&fm=23&gp=0.jpg");

        FriendInfo data12 = new FriendInfo();
        data12.setMemberName("陆成宋08");
        data12.setMemberId("12");
        data12.setMemberUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494065730070&di=b4ae9186fcb1795ee5bec334ec893997&imgtype=0&src=http%3A%2F%2Fwww.qxjlm.com%2Ftupians%2Fbd13706313.jpg");
        friends.add(data01);
        friends.add(data02);
        friends.add(data03);
        friends.add(data04);
        friends.add(data05);
        friends.add(data06);
        friends.add(data07);
        friends.add(data08);
        friends.add(data09);
        friends.add(data10);
        friends.add(data11);
        friends.add(data12);
        return friends;
    }

}
