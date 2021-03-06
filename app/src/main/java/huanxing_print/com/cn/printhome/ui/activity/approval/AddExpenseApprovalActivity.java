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
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.model.approval.AddApprovalObject;
import huanxing_print.com.cn.printhome.model.approval.ApprovalCopyPeopleItem;
import huanxing_print.com.cn.printhome.model.approval.ApprovalPeopleItem;
import huanxing_print.com.cn.printhome.model.approval.Approver;
import huanxing_print.com.cn.printhome.model.approval.ChooseGroupEvent;
import huanxing_print.com.cn.printhome.model.approval.ChooseMemberEvent;
import huanxing_print.com.cn.printhome.model.approval.ImageUrl;
import huanxing_print.com.cn.printhome.model.approval.LastApproval;
import huanxing_print.com.cn.printhome.model.approval.SubFormItem;
import huanxing_print.com.cn.printhome.model.comment.PicDataBean;
import huanxing_print.com.cn.printhome.model.contact.GroupInfo;
import huanxing_print.com.cn.printhome.model.contact.GroupMember;
import huanxing_print.com.cn.printhome.model.image.ImageUploadItem;
import huanxing_print.com.cn.printhome.model.picupload.ImageItem;
import huanxing_print.com.cn.printhome.net.callback.approval.AddApprovalCallBack;
import huanxing_print.com.cn.printhome.net.callback.approval.QueryLastCallBack;
import huanxing_print.com.cn.printhome.net.callback.comment.UpLoadPicCallBack;
import huanxing_print.com.cn.printhome.net.request.approval.ApprovalRequest;
import huanxing_print.com.cn.printhome.net.request.commet.UpLoadPicRequest;
import huanxing_print.com.cn.printhome.ui.activity.copy.PhotoPickerActivity;
import huanxing_print.com.cn.printhome.ui.activity.copy.PreviewPhotoActivity;
import huanxing_print.com.cn.printhome.util.CashierInputFilter;
import huanxing_print.com.cn.printhome.util.CircleTransform;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.FileUtils;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;
import huanxing_print.com.cn.printhome.util.picuplload.Bimp;
import huanxing_print.com.cn.printhome.util.picuplload.BitmapLoadUtils;
import huanxing_print.com.cn.printhome.view.ScrollGridView;
import huanxing_print.com.cn.printhome.view.ScrollListView;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;
import huanxing_print.com.cn.printhome.view.imageview.RoundImageView;

/**
 * description: 新建报销审批
 * author LSW
 * date 2017/5/6 10:40
 * update 2017/5/6
 */
public class AddExpenseApprovalActivity extends BaseActivity implements View.OnClickListener {
    private static final int CHOOSE_PIC_MAX = 5;
    private List<Bitmap> mResults = new ArrayList<>();
    private GridView noScrollgridview;
    private GridAdapter adapter;
    public static Bitmap bimap;
    private Context ctx;
    private static final int PICK_PHOTO = 1;
    private ScrollGridView grid_scroll_approval;//审批人
    private ScrollGridView grid_scroll_copy;//抄送
    private ScrollListView scroll_lv;//报销条目
    private GridViewApprovalAdapter approvalAdapter;
    private GridViewCopyAdapter copyAdapter;
    private ListViewExpenseAdapter edtAdapter;
    private ArrayList<GroupMember> approvalFriends = new ArrayList<GroupMember>();//审批人
    private ArrayList<GroupMember> copyFriends = new ArrayList<GroupMember>();//抄送人
    private ArrayList<SubFormItem> subFormItems = new ArrayList<>();//报销条目集合(这里未包含第一条记录)
    private int CODE_APPROVAL_REQUEST = 0X11;//审批人请求码
    private int CODE_COPY_REQUEST = 0X12;//抄送人人请求码
    private List<ImageUploadItem> imageitems = new ArrayList<>();
    private ArrayList<String> imageUrls = new ArrayList<>();
    private ArrayList<Approver> approvers = new ArrayList<>();//上传的审批人集合
    private ArrayList<Approver> copyApprovers = new ArrayList<>();//上传的抄送人集合
    private EditText edt_expense_department;//报销部门
    private EditText editText2;//备注
    private EditText edt_request_num;//报销金额
    private AddApprovalObject object = new AddApprovalObject();//提交的审批对象
    private String groupId;//群组id

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense_approval);
        // 改变状态栏的颜色使其与APP风格一体化
        CommonUtils.initSystemBar(this);
        EventBus.getDefault().register(this);
        ctx = this;
        initData();
        functionModule();
    }

    private void functionModule() {

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
                    intent.putExtra("isApproval", true);
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
                    //这里审批任何抄送人都为空才去群选择界面
                    if (0 == approvalFriends.size() &&
                            0 == copyFriends.size()) {
                        //跳转到选择联系人界面
                        Intent intent = new Intent(getSelfActivity(), ChooseGroupActivity.class);
                        intent.putExtra("type", "approvalFriends");//区别审批人和抄送人
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent();
                        //将群id传过去
                        intent.setClass(getSelfActivity(), ChoosePeopleOfAddressActivity.class);
                        intent.putExtra("groupId", groupId);
                        intent.putExtra("type", "approvalFriends");//区别审批人和抄送人
                        startActivity(intent);
                    }
//                    //这里还要判断一下,抄送人为空那就重新选择群组
//                    if (approvalFriends.size() > 0) {
//                        //跳转到选择联系人界面或者选择联系人界面
//                        Intent intent = new Intent();
//                        if (ObjectUtils.isNull(groupId)) {
//                            //跳到群选择界面
//                            intent.setClass(getSelfActivity(), ChooseGroupActivity.class);
//                        } else {
//                            //将群id传过去
//                            intent.setClass(getSelfActivity(), ChoosePeopleOfAddressActivity.class);
//                            intent.putExtra("groupId", groupId);
//                        }
//                        //将群id传过去
//                        intent.setClass(getSelfActivity(), ChoosePeopleOfAddressActivity.class);
//                        intent.putExtra("groupId", groupId);
//                        intent.putExtra("type", "approvalFriends");//区别审批人和抄送人
//                        startActivity(intent);
//                    } else {
//                        //跳转到选择联系人界面
//                        Intent intent = new Intent(getSelfActivity(), ChooseGroupActivity.class);
//                        intent.putExtra("type", "approvalFriends");//区别审批人和抄送人
//                        startActivity(intent);
//                    }
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
                    if (0 == approvalFriends.size() &&
                            0 == copyFriends.size()) {
                        //跳转到选择群界面然后去选择人员
                        Intent intent = new Intent(getSelfActivity(), ChooseGroupActivity.class);
                        intent.putExtra("type", "copyFriends");//区别审批人和抄送人
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent();
                        //将群id传过去
                        intent.setClass(getSelfActivity(), ChoosePeopleOfAddressActivity.class);
                        intent.putExtra("groupId", groupId);
                        intent.putExtra("type", "copyFriends");//区别审批人和抄送人
                        startActivity(intent);
                    }
//                    if (copyFriends.size() > 0) {
//                        //跳转到选择联系人界面
//                        Intent intent = new Intent();
//                        if (ObjectUtils.isNull(groupId)) {
//                            //跳到群选择界面
//                            intent.setClass(getSelfActivity(), ChooseGroupActivity.class);
//                        } else {
//                            //将群id传过去
//                            intent.setClass(getSelfActivity(), ChoosePeopleOfAddressActivity.class);
//                            intent.putExtra("groupId", groupId);
//                        }
//                        //将群id传过去
//                        intent.setClass(getSelfActivity(), ChoosePeopleOfAddressActivity.class);
//                        intent.putExtra("groupId", groupId);
//                        intent.putExtra("type", "copyFriends");//区别审批人和抄送人
//                        startActivity(intent);
//                    } else {
//                        //跳转到选择群界面然后去选择人员
//                        Intent intent = new Intent(getSelfActivity(), ChooseGroupActivity.class);
//                        intent.putExtra("type", "copyFriends");//区别审批人和抄送人
//                        startActivity(intent);
//                    }
                } else {
                    copyFriends.remove(position);
                    copyAdapter.notifyDataSetChanged();
                }
            }
        });
    }


    private void initData() {
        bimap = BitmapFactory.decodeResource(getResources(), R.drawable.add);
        mResults.add(bimap);

        grid_scroll_approval = (ScrollGridView) findViewById(R.id.grid_scroll_approval);
        grid_scroll_copy = (ScrollGridView) findViewById(R.id.grid_scroll_copy);
        scroll_lv = (ScrollListView) findViewById(R.id.scroll_lv);
        edt_expense_department = (EditText) findViewById(R.id.edt_expense_department);
        editText2 = (EditText) findViewById(R.id.editText2);
        edt_request_num = (EditText) findViewById(R.id.edt_request_num);

        //设置金额填框必须只能填写金额数字 EditText要先设置
        //android:inputType="numberDecimal"或者setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL)
        InputFilter[] filters={new CashierInputFilter()};
        edt_request_num.setFilters(filters);
        //返回
        View view = findViewById(R.id.back);
        view.findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
        noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));

        copyAdapter = new GridViewCopyAdapter();
        approvalAdapter = new GridViewApprovalAdapter();
        //模拟第一次数据
        subFormItems.add(new SubFormItem());
        edtAdapter = new ListViewExpenseAdapter();
        scroll_lv.setAdapter(edtAdapter);

        adapter = new GridAdapter(this);
        adapter.update();

        findViewById(R.id.btn_submit_expense_approval).setOnClickListener(this);
        findViewById(R.id.rel_choose_image).setOnClickListener(this);
        findViewById(R.id.rel_add_expense).setOnClickListener(this);

        //请求上次的审批人和联系人
        ApprovalRequest.queryLast(getSelfActivity(), baseApplication.getLoginToken(),
                2, callBack);
    }

    QueryLastCallBack callBack = new QueryLastCallBack() {
        @Override
        public void success(String msg, LastApproval approval) {
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
                ArrayList<ApprovalPeopleItem> approvals = approval.getApproverList();
                ArrayList<ApprovalCopyPeopleItem> copys = approval.getCopyList();
                if (!ObjectUtils.isNull(approvals)) {
                    for (ApprovalPeopleItem approvalOrCopy : approvals) {
                        GroupMember info = new GroupMember();
                        info.setMemberId(approvalOrCopy.getJobNumber());
                        info.setMemberName(approvalOrCopy.getName());
                        info.setMemberUrl(approvalOrCopy.getFaceUrl());
                        approvalFriends.add(info);
                    }
                }
                if (!ObjectUtils.isNull(copys)) {
                    for (ApprovalCopyPeopleItem orCopy : copys) {
                        GroupMember info = new GroupMember();
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
            approvalAdapter.notifyDataSetChanged();
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
            copyAdapter.notifyDataSetChanged();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGroupEvent(ChooseGroupEvent event) {
        groupId = event.getGroupId();
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
                resultCode == 0x11) {
            //选择了群,得到群id
            if (data != null) {
                GroupInfo info = data.getParcelableExtra("GroupInfo");
                if (info != null) {
                    groupId = info.getGroupId();
                    Log.i("CMCC", "groupId:" + groupId);
                }
            }
        }
        if (requestCode == CODE_APPROVAL_REQUEST &&
                resultCode == 0x12) {
            Log.i("CMCC", "审批人返回");
            if (data != null) {
                //审批人选择
                ArrayList<GroupMember> infos = data.getParcelableArrayListExtra("FriendInfo");
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
                approvalAdapter.notifyDataSetChanged();
            }
        }
        if (requestCode == CODE_COPY_REQUEST &&
                resultCode == 0x11) {
            //选择了群,得到群id
            if (data != null) {
                GroupInfo info = data.getParcelableExtra("GroupInfo");
                if (info != null) {
                    groupId = info.getGroupId();
                    Log.i("CMCC", "groupId:" + groupId);
                }
            }
        }
        if (requestCode == CODE_COPY_REQUEST &&
                resultCode == 0x12) {
            Log.i("CMCC", "抄送人返回");
            //抄送人选择
            ArrayList<GroupMember> infos = data.getParcelableArrayListExtra("FriendInfo");
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit_expense_approval:
                //提交报销审批
                createPurchaseApproval();
                break;
            case R.id.rel_choose_image:
                int havePicSize = mResults.size() - 1;
                if (CHOOSE_PIC_MAX > havePicSize) {
                    int chooseNum = CHOOSE_PIC_MAX - havePicSize;
                    //选择图片
                    Intent intent = new Intent(ctx, PhotoPickerActivity.class);
                    intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, true);
                    intent.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE, PhotoPickerActivity.MODE_MULTI);
                    intent.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN, chooseNum);
                    // 总共选择的图片数量
                    intent.putExtra(PhotoPickerActivity.TOTAL_MAX_MUN, Bimp.tempSelectBitmap.size());
                    startActivityForResult(intent, PICK_PHOTO);
                } else {
                    ToastUtil.doToast(AddExpenseApprovalActivity.this,"选择已经达到上限");
                }
                break;
            case R.id.rel_add_expense:
                //添加报销条目(检查上次添加的item是否为空)
                //不是第一次添加
                SubFormItem last = subFormItems.get(subFormItems.size() - 1);
                if (ObjectUtils.isNull(last.getAmount()) ||
                        ObjectUtils.isNull(last.getType())) {
                    ToastUtil.doToast(getSelfActivity(), "请填写完毕上面的报销条目!");
                } else {
                    subFormItems.add(new SubFormItem());
                    edtAdapter.notifyDataSetChanged();
                }
                break;
        }
    }


    /**
     * 提交采购审批
     */
    private void createPurchaseApproval() {

        if (ObjectUtils.isNull(edt_expense_department.getText().toString())) {
            toast("报销部门不能为空!");
            return;
        }
        if (ObjectUtils.isNull(edt_request_num.getText().toString())) {
            toast("报销金额不能为空!");
            return;
        }
        object.setDepartment(edt_expense_department.getText().toString());
        object.setAmountMonney(edt_request_num.getText().toString());
        object.setRemark(editText2.getText().toString());

        ArrayList<SubFormItem> subItems = new ArrayList<>();
        //判断报销条目
        SubFormItem last = subFormItems.get(subFormItems.size() - 1);
        if (ObjectUtils.isNull(last.getAmount()) ||
                ObjectUtils.isNull(last.getType())) {
            //如果这是第一条数据,那就提醒用户去填写
            if (1 == subFormItems.size()) {
                ToastUtil.doToast(getSelfActivity(), "请填写完毕上面的报销条目!");
                return;
            }
            if (subFormItems.size() > 1) {
                //取出除了最后一条消息
                for (int i = 0; i < (subFormItems.size() - 1); i++) {
                    subItems.add(subFormItems.get(i));
                }
                object.setSubFormList(subItems);
            }
        }
        object.setSubFormList(subFormItems);
        object.setGroupId(groupId);


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
        object.setApproverList(approvers);


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
        object.setType(2);
        object.setGroupId(groupId);

        //提交图片获得图片url
        if (mResults.size() > 1) {
            DialogUtils.showProgressDialog(getSelfActivity(), "正在提交中").show();
            ArrayList<ImageItem> items = Bimp.tempSelectBitmap;
            getUrl(items);
            uploadPic();
        } else {
            object.setAttachmentList(null);
            DialogUtils.showProgressDialog(getSelfActivity(), "正在提交中").show();
            ApprovalRequest.addApproval(getSelfActivity(), baseApplication.getLoginToken(),
                    2, object, addCallBack);
        }

    }

    AddApprovalCallBack addCallBack = new AddApprovalCallBack() {
        @Override
        public void success(String msg, String data) {
            DialogUtils.closeProgressDialog();
            Log.i("CMCC", "新建报销审批,id:" + data);
            finish();
            ToastUtil.doToast(getSelfActivity(), "新建报销审批成功!");
            //ToastUtil.doToast(getSelfActivity(), "新建采购审批,id:" + data);
        }

        @Override
        public void fail(String msg) {
            Log.i("CMCC", "新建报销审批失败," + msg);
            //ToastUtil.doToast(getSelfActivity(), "新建采购审批失败," + msg);
            DialogUtils.closeProgressDialog();
        }

        @Override
        public void connectFail() {
            Log.i("CMCC", "新建报销审批connectFail");
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
        String filename = System.currentTimeMillis()+".jpg";
        String filePath = FileUtils.savePic(getSelfActivity(), filename, bitmap);
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
                Log.d("TAG","------bean------------"+bean);
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

                if (imageUrls.size() > 0) {
                    object.setAttachmentList(urls);
                    ApprovalRequest.addApproval(getSelfActivity(), baseApplication.getLoginToken(),
                            2, object, addCallBack);
                }

            }

            @Override
            public void fail(String msg) {
                DialogUtils.closeProgressDialog();
            }

            @Override
            public void connectFail() {
                DialogUtils.closeProgressDialog();
            }
        });
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
                convertView = LayoutInflater.from(getSelfActivity()).inflate(
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
                        .placeholder(R.drawable.iv_head)
                        .centerCrop()
                        .transform(new CircleTransform(getSelfActivity()))
                        .crossFade()
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
                convertView = LayoutInflater.from(getSelfActivity()).inflate(
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
                        .centerCrop()
                        .transform(new CircleTransform(getSelfActivity()))
                        .crossFade()
                        .placeholder(R.drawable.iv_head)
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
     * description: 维持edittext输入值的Adapter
     * author LSW
     * date 2017/5/10 15:21
     * update 2017/5/10
     */
    private class ListViewExpenseAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return subFormItems.size();
        }

        @Override
        public Object getItem(int position) {
            return subFormItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(getSelfActivity()).inflate(R.layout.item_expense, null);
                holder.edt_expense_type = (EditText) convertView.findViewById(R.id.edt_expense_type);
                holder.edt_expense_num = (EditText) convertView.findViewById(R.id.edt_expense_num);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final SubFormItem item = (SubFormItem) getItem(position);
            //This is important. An EditText just one TextWatcher.
            if (holder.edt_expense_type.getTag() instanceof TextWatcher) {
                holder.edt_expense_type.removeTextChangedListener((TextWatcher) holder.edt_expense_type.getTag());
            }
            holder.edt_expense_type.setText(item.getType());
            TextWatcher watcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (TextUtils.isEmpty(s)) {
                        item.setType("");
                    } else {
                        item.setType(s.toString());
                    }
                }
            };
            holder.edt_expense_type.addTextChangedListener(watcher);
            holder.edt_expense_type.setTag(watcher);

            //设置金额填框必须只能填写金额数字 EditText要先设置
            //android:inputType="numberDecimal"或者setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL)
            InputFilter[] filters={new CashierInputFilter()};
            holder.edt_expense_num.setFilters(filters);
            //This is important. An EditText just one TextWatcher.
            if (holder.edt_expense_num.getTag() instanceof TextWatcher) {
                holder.edt_expense_num.removeTextChangedListener((TextWatcher) holder.edt_expense_num.getTag());
            }
            holder.edt_expense_num.setText(item.getAmount());
            TextWatcher watcher1 = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (TextUtils.isEmpty(s)) {
                        item.setAmount("");
                    } else {
                        item.setAmount(s.toString());
                    }
                    updateRequestNum();
                }
            };
            holder.edt_expense_num.addTextChangedListener(watcher1);
            holder.edt_expense_num.setTag(watcher1);
            return convertView;
        }

        class ViewHolder {
            EditText edt_expense_type;
            EditText edt_expense_num;
        }
    }

    private void updateRequestNum(){
        double requestNum = 0;
        for (SubFormItem item : subFormItems) {
            if(item.getAmount().length() > 0) {
                requestNum += Double.parseDouble(item.getAmount());
            }
        }
        if (requestNum == 0) {
            edt_request_num.setText("");
        }else {
            edt_request_num.setText(String.valueOf(requestNum));
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
