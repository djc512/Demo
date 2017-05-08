package huanxing_print.com.cn.printhome.ui.activity.approval;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.ui.activity.copy.PhotoPickerActivity;
import huanxing_print.com.cn.printhome.ui.activity.copy.PreviewPhotoActivity;
import huanxing_print.com.cn.printhome.ui.adapter.UpLoadPicAdapter;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.picuplload.Bimp;
import huanxing_print.com.cn.printhome.util.picuplload.UpLoadPicUtil;


/**
 * 新增采购审批
 */
public class AddPurchaseActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout lin_request_money;//请款金额
    private EditText edt_request_num;
    private LinearLayout lin_payee;//收款方
    private LinearLayout lin_opening_bank;//开户行
    private LinearLayout lin_account;//帐号
    private EditText edt_finish_time;//结束时间
    private List<Bitmap> mResults = new ArrayList<>();
    private GridView noScrollgridview;
    private UpLoadPicAdapter adapter;
    public static Bitmap bimap;
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
        bimap = BitmapFactory.decodeResource(getResources(), R.drawable.add);
        mResults.add(bimap);
        initView();
        initData();
    }

    private void initData() {
        adapter = new UpLoadPicAdapter(getSelfActivity(), mResults);
        adapter.update();
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
    }

    public void initView() {
        lin_request_money = (LinearLayout) findViewById(R.id.lin_request_money);
        edt_request_num = (EditText) findViewById(R.id.edt_request_num);
        lin_payee = (LinearLayout) findViewById(R.id.lin_payee);
        lin_opening_bank = (LinearLayout) findViewById(R.id.lin_opening_bank);
        lin_account = (LinearLayout) findViewById(R.id.lin_account);
        edt_finish_time = (EditText) findViewById(R.id.edt_finish_time);
        findViewById(R.id.rel_choose_image).setOnClickListener(this);
        findViewById(R.id.rel_choose_time).setOnClickListener(this);
        //返回
        View view = findViewById(R.id.back);
        view.findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ToggleButton button = (ToggleButton) findViewById(R.id.toggleButton);
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
                    edt_request_num.setKeyListener(null);
                    lin_payee.setVisibility(View.GONE);
                    lin_opening_bank.setVisibility(View.GONE);
                    lin_account.setVisibility(View.GONE);
                }
            }
        });

        noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
        noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rel_choose_image:
                break;
            case R.id.rel_choose_time:
                //选择时间
                Calendar startDate = Calendar.getInstance();
                Calendar endDate = Calendar.getInstance();
                endDate.set(2020, 1, 1);
                break;
        }
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

}
