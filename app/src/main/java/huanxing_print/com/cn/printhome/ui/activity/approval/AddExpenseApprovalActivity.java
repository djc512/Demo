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
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.ui.activity.copy.PhotoPickerActivity;
import huanxing_print.com.cn.printhome.ui.activity.copy.PreviewPhotoActivity;
import huanxing_print.com.cn.printhome.ui.adapter.UpLoadPicAdapter;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.picuplload.Bimp;
import huanxing_print.com.cn.printhome.util.picuplload.UpLoadPicUtil;
import huanxing_print.com.cn.printhome.view.ScrollGridView;
import huanxing_print.com.cn.printhome.view.imageview.RoundImageView;

/**
 * description: 新建报销审批
 * author LSW
 * date 2017/5/6 10:40
 * update 2017/5/6
 */
public class AddExpenseApprovalActivity extends BaseActivity implements View.OnClickListener {

    private List<Bitmap> mResults = new ArrayList<>();
    private List<Bitmap> approvals = new ArrayList<>();
    private List<Bitmap> copys = new ArrayList<>();
    private GridView noScrollgridview;
    private UpLoadPicAdapter adapter;
    public static Bitmap bimap;
    public static Bitmap bimapAdd;
    private Context ctx;
    private static final int PICK_PHOTO = 1;
    private ScrollGridView grid_scroll_approval;//审批人
    private ScrollGridView grid_scroll_copy;//抄送
    private GridViewApprovalAdapter approvalAdapter;
    private GridViewCopyAdapter copyAdapter;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 改变状态栏的颜色使其与APP风格一体化
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.activity_add_expense_approval);

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
        bimap = BitmapFactory.decodeResource(getResources(), R.drawable.add);
        bimapAdd = BitmapFactory.decodeResource(getResources(), R.drawable.add_people);
        mResults.add(bimap);
        approvals.add(bimapAdd);
        copys.add(bimapAdd);

        grid_scroll_approval = (ScrollGridView) findViewById(R.id.grid_scroll_approval);
        grid_scroll_copy = (ScrollGridView) findViewById(R.id.grid_scroll_copy);
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
        adapter = new UpLoadPicAdapter(getSelfActivity(), mResults);
        adapter.update();

        findViewById(R.id.btn_submit_expense_approval).setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit_expense_approval:
                //提交报销审批

                break;
        }
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
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(AddExpenseApprovalActivity.this).inflate(
                        R.layout.item_grid_approval, null);
                holder.round_head_image = (RoundImageView) convertView.findViewById(R.id.round_head_image);
                holder.txt_name = (TextView) convertView.findViewById(R.id.txt_name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
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
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(AddExpenseApprovalActivity.this).inflate(
                        R.layout.item_grid_approval, null);
                holder.round_head_image = (RoundImageView) convertView.findViewById(R.id.round_head_image);
                holder.txt_name = (TextView) convertView.findViewById(R.id.txt_name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
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
