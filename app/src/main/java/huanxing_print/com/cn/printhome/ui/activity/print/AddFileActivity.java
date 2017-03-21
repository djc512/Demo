package huanxing_print.com.cn.printhome.ui.activity.print;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.log.Logger;
import huanxing_print.com.cn.printhome.util.FileUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;
import pub.devrel.easypermissions.EasyPermissions;

public class AddFileActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks, View.OnClickListener {

    private Button imageBtn;
    private Button qqBtn;
    private Button wechatBtn;
    private Button pcBtn;
    private Context context;
    private static final int REQUEST_CODE = 1;

    private static final int REQUEST_IMG = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_file);
        context = AddFileActivity.this;
        initView();
    }

    private boolean isPermissionsGranted() {
        boolean isPermission;
        if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            isPermission = true;
        } else {
            EasyPermissions.requestPermissions(this, "需要请求权限", REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
            isPermission = false;
        }
        return isPermission;
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    private void initView() {
        imageBtn = (Button) findViewById(R.id.imageBtn);
        qqBtn = (Button) findViewById(R.id.qqBtn);
        wechatBtn = (Button) findViewById(R.id.wechatBtn);
        pcBtn = (Button) findViewById(R.id.pcBtn);

        imageBtn.setOnClickListener(this);
        qqBtn.setOnClickListener(this);
        wechatBtn.setOnClickListener(this);
        pcBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageBtn:
                if (isPermissionsGranted()) {
                    getImage();
                }
                break;
            case R.id.qqBtn:
                if (isPermissionsGranted()) {
                    getFileList(PATH_QQ_FILE, SOURCE_QQ);
                }
                break;
            case R.id.wechatBtn:
                if (isPermissionsGranted()) {
                    getFileList(PATH_WECHAT_FILE, SOURCE_WECHAT);
                }
                break;
            case R.id.pcBtn:
                break;
        }
    }

    public void onPreviewBtn(View view) {
        turnImgPreview(uri);
    }


    private void getImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    private static final String PATH_QQ_FILE = Environment.getExternalStorageDirectory().getPath() + "/tencent/QQfile_recv/";
    private static final String PATH_WECHAT_FILE = Environment.getExternalStorageDirectory().getPath() + "/tencent/MicroMsg/Download/";
    public static final String KEY_FILE = "file";
    public static final String KEY_SOURCE = "source";
    public static final int SOURCE_QQ = 1;
    public static final int SOURCE_WECHAT = 2;

    private void getFileList(String path, int source) {
        List<File> fileList = FileUtils.getFileList(path);
        if (fileList == null) {
            ToastUtil.showToast("file error");
        } else {
            Intent intent = new Intent(AddFileActivity.this, FileListActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(KEY_FILE, (Serializable) fileList);
            bundle.putInt(KEY_SOURCE, source);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    public static final String KEY_IMG_URI = "image";

    private void turnImgPreview(Uri uri) {
        Intent intent = new Intent(context, ImgPreviewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_IMG_URI, uri);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private Uri uri;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMG:
                    Logger.i(data.toString());
                    uri = data.getData();
                    turnImgPreview(data.getData());
                    break;
            }
        }
    }


}
