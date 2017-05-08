package huanxing_print.com.cn.printhome.ui.activity.approval;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bigkoo.pickerview.TimePickerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.ui.activity.copy.PhotoPickerActivity;
import huanxing_print.com.cn.printhome.ui.activity.copy.PreviewPhotoActivity;
import huanxing_print.com.cn.printhome.ui.adapter.UpLoadPicAdapter;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.util.picuplload.Bimp;
import huanxing_print.com.cn.printhome.util.picuplload.UpLoadPicUtil;
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
    private ToggleButton button;
    private List<Bitmap> mResults = new ArrayList<>();
    private List<Bitmap> approvals = new ArrayList<>();
    private List<Bitmap> copys = new ArrayList<>();
    private GridView noScrollgridview;
    private ScrollGridView grid_scroll_approval;//审批人
    private ScrollGridView grid_scroll_copy;//抄送
    private GridViewApprovalAdapter approvalAdapter;
    private GridViewCopyAdapter copyAdapter;
    private UpLoadPicAdapter adapter;
    public static Bitmap bimap;
    public static Bitmap bimapAdd;
    private Context ctx;
    private static final int PICK_PHOTO = 1;

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
                    intent.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN, 5 - mResults.size() + 1);
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
                if (position == (approvals.size() - 1)) {
                    //点击了添加按钮
                    approvals.set(position,
                            BitmapFactory.decodeResource(getResources(), R.drawable.iv_head));
                    approvals.add(bimapAdd);
                    approvalAdapter.notifyDataSetChanged();
                } else {
                    approvals.remove(position);
                    approvalAdapter.notifyDataSetChanged();
                }
            }
        });

        //抄送人
        grid_scroll_copy.setAdapter(copyAdapter);
        grid_scroll_copy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == (copys.size() - 1)) {
                    //点击了添加按钮
                    copys.set(position,
                            BitmapFactory.decodeResource(getResources(), R.drawable.iv_head));
                    copys.add(bimapAdd);
                    copyAdapter.notifyDataSetChanged();
                } else {
                    copys.remove(position);
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

        noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
        noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));

        adapter = new UpLoadPicAdapter(getSelfActivity(), mResults);
        adapter.update();

        approvalAdapter = new GridViewApprovalAdapter();
        copyAdapter = new GridViewCopyAdapter();

        bimap = BitmapFactory.decodeResource(getResources(), R.drawable.add);
        bimapAdd = BitmapFactory.decodeResource(getResources(), R.drawable.add_people);
        mResults.add(bimap);
        approvals.add(bimapAdd);
        copys.add(bimapAdd);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rel_choose_image:
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
            DialogUtils.showProgressDialog(getSelfActivity(), "提交中").show();
            //AddAprovalRequest.addApproval();
        }

        if (!button.isChecked()) {
            DialogUtils.showProgressDialog(getSelfActivity(), "提交中").show();
            //AddAprovalRequest.addApproval();
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO) {
            if (resultCode == RESULT_OK) {
                ArrayList<String> result = data.getStringArrayListExtra(PhotoPickerActivity.KEY_RESULT);
                UpLoadPicUtil upLoadPicUtil = new UpLoadPicUtil(ctx, mResults, adapter);
                upLoadPicUtil.showResult(result);
            }
        }
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
            gvHeight = CommonUtils.dip2px(ctx, 60);
        } else if (adapter.getCount() < 9) {
            gvHeight = CommonUtils.dip2px(ctx, 125);
        } else {
            gvHeight = CommonUtils.dip2px(ctx, 190);
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
            return approvals.size();
        }

        @Override
        public Object getItem(int position) {
            return approvals.get(position);
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
            if (position == (approvals.size() - 1)) {
                //添加图标
                holder.txt_name.setVisibility(View.GONE);
            } else {
                holder.txt_name.setVisibility(View.VISIBLE);
            }
            holder.round_head_image.setImageBitmap(approvals.get(position));
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
            return copys.size();
        }

        @Override
        public Object getItem(int position) {
            return copys.get(position);
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
            if (position == (copys.size() - 1)) {
                //添加图标
                holder.txt_name.setVisibility(View.GONE);
            } else {
                holder.txt_name.setVisibility(View.VISIBLE);
            }
            holder.round_head_image.setImageBitmap(copys.get(position));
            return convertView;
        }

        class ViewHolder {
            RoundImageView round_head_image;
            TextView txt_name;
        }
    }

}
