package huanxing_print.com.cn.printhome.ui.activity.my;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.simple.eventbus.EventBus;

import java.io.File;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.util.BitmapUtils;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.ObjectUtils;


/**
 * Created by Administrator on 2017/3/20 0020.
 */

public class MyActivity extends BaseActivity implements View.OnClickListener {

    //请求相机
    private static final int REQUEST_CAPTURE = 100;
    //请求相册
    private static final int REQUEST_PICK = 101;
    //请求截图
    private static final int REQUEST_CROP_PHOTO = 102;

    private ImageView iv_user_head;
    private LinearLayout ll_back;

    private File tempFile;
    private PopupWindow popupWindow;
    private LinearLayout ll_userInfo_name;
    private LinearLayout ll_userInfo_wechat;
    private String wechatId;
    private TextView tv_userInfo_wechat;
    private String cropImagePath;
    private String headImg;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBarGreen(this);
        setContentView(R.layout.activity_user_my);
        EventBus.getDefault().register(this);
        //创建拍照存储的临时文件
        createCameraTempFile(savedInstanceState);
        initView();
        initData();
        setListener();
    }

    /**
     * 获取用户信息
     */
    private void initData() {
        Intent intent = getIntent();
        headImg = intent.getStringExtra("headUrl");
        wechatId = intent.getStringExtra("wechatId");
        BitmapUtils.displayImage(getSelfActivity(), headImg, R.drawable.iv_head, iv_user_head);
        if (ObjectUtils.isNull(wechatId)) {
            tv_userInfo_wechat.setText("未绑定");
        } else {
            tv_userInfo_wechat.setText("已绑定");
        }
    }

    private void createCameraTempFile(Bundle savedInstanceState) {

        if (savedInstanceState != null && savedInstanceState.containsKey("tempFile")) {
            tempFile = (File) savedInstanceState.getSerializable("tempFile");
        } else {
            tempFile = new File(checkDirPath(Environment.getExternalStorageDirectory().getPath() + "/image/"),
                    System.currentTimeMillis() + ".jpg");
        }
    }

    /**
     * 检查文件是否存在
     */
    private static String checkDirPath(String dirPath) {
        if (TextUtils.isEmpty(dirPath)) {
            return "";
        }
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dirPath;
    }

    private void initView() {

        iv_user_head = (ImageView) findViewById(R.id.iv_user_head);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        ll_userInfo_name = (LinearLayout) findViewById(R.id.ll_userInfo_name);
        ll_userInfo_wechat = (LinearLayout) findViewById(R.id.ll_userInfo_wechat);

        tv_userInfo_wechat = (TextView) findViewById(R.id.tv_userInfo_wechat);
    }

    private void setListener() {
        iv_user_head.setOnClickListener(this);
        ll_back.setOnClickListener(this);

        ll_userInfo_name.setOnClickListener(this);
        ll_userInfo_wechat.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_user_head:
                showSelectImage();
                break;
            case R.id.btn_camera:
                //跳转到调用系统相机
                gotoCarema();
                popupWindow.dismiss();
                break;
            case R.id.btn_photo:
                //跳转到调用系统图库
                gotoPhoto();
                popupWindow.dismiss();
                break;
            case R.id.btn_cancel:
                popupWindow.dismiss();
                break;
            case R.id.ll_back:
                finish();
                break;
            case R.id.ll_userInfo_name:
                Intent intent = new Intent(getSelfActivity(), MyModifyNameActivty.class);
                intent.putExtra("cropImagePath", cropImagePath);
                startActivity(intent);
                break;
            case R.id.ll_userInfo_wechat:

                break;
        }
    }

    private void gotoPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "请选择图片"), REQUEST_PICK);
    }

    private void gotoCarema() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        startActivityForResult(intent, REQUEST_CAPTURE);
    }

    private void showSelectImage() {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_popupwindow, null);
        TextView btnCarema = (TextView) view.findViewById(R.id.btn_camera);
        TextView btnPhoto = (TextView) view.findViewById(R.id.btn_photo);
        TextView btnCancel = (TextView) view.findViewById(R.id.btn_cancel);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        View parent = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        //popupWindow在弹窗的时候背景半透明
        final WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.5f;
        getWindow().setAttributes(params);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params.alpha = 1.0f;
                getWindow().setAttributes(params);
            }
        });

        btnCarema.setOnClickListener(this);
        btnPhoto.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("tempFile", tempFile);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case REQUEST_CAPTURE: //调用系统相机返回
                if (resultCode == RESULT_OK) {
                    gotoClipActivity(Uri.fromFile(tempFile));
                }
                break;
            case REQUEST_PICK:  //调用系统相册返回
                if (resultCode == RESULT_OK) {
                    Uri uri = intent.getData();
                    gotoClipActivity(uri);
                }
                break;
            case REQUEST_CROP_PHOTO:  //剪切图片返回
                if (resultCode == RESULT_OK) {
                    final Uri uri = intent.getData();
                    if (uri == null) {
                        return;
                    }
                    cropImagePath = getRealFilePathFromUri(getApplicationContext(), uri);

                    BitmapUtils.displayImage(this, cropImagePath, R.drawable.iv_head, iv_user_head);

                    Bitmap bitMap = BitmapFactory.decodeFile(cropImagePath);
                    EventBus.getDefault().post(bitMap, "head");
                }
                break;
        }
    }

    /**
     * 打开截图界面
     *
     * @param uri
     */
    public void gotoClipActivity(Uri uri) {
        if (uri == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(this, ClipeImageActivity.class);
        intent.setData(uri);
        startActivityForResult(intent, REQUEST_CROP_PHOTO);
    }

    /**
     * 根据Uri返回文件绝对路径
     * 兼容了file:///开头的 和 content://开头的情况
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getRealFilePathFromUri(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
