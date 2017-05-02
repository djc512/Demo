package huanxing_print.com.cn.printhome.ui.activity.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.ui.activity.Copy.IDPreviewActivity;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.copy.PicSaveUtil;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Administrator on 2017/4/28 0028.
 */

public class HuKouFragment extends Fragment implements View.OnClickListener{
    private RadioButton btn_camera;
    private RadioButton btn_galley;
    private RadioButton btn_cameraf;
    private RadioButton btn_galleyf;
    private TextView btn_preview;
    private int PICK_IMAGE_REQUEST = 1;
    //调用照相机返回图片临时文件
    private File tempFile;
    //请求相机
    private static final int REQUEST_CAPTURE = 100;
    private PicSaveUtil saveUtil;
    private Context ctx;
    private ImageView iv_preview;
    private ImageView iv_previewf;
    private ReceiveBroadCast receiveBroadCast;
    private byte[] bytes;
    private byte[] bytesf;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ctx = getActivity();
        CommonUtils.initSystemBar(getActivity());
        saveUtil = new PicSaveUtil(ctx);
        tempFile = saveUtil.createCameraTempFile(savedInstanceState);
        View view = inflater.inflate(R.layout.frag_hukou, null);
        initView(view);
        initData();
        initListener();
        return view;
    }
    private void initView(View view) {

        btn_camera = (RadioButton) view.findViewById(R.id.btn_camera);
        btn_galley = (RadioButton) view.findViewById(R.id.btn_galley);
        btn_cameraf = (RadioButton) view.findViewById(R.id.btn_cameraf);
        btn_galleyf = (RadioButton) view.findViewById(R.id.btn_galleyf);
        btn_preview = (TextView) view.findViewById(R.id.btn_preview);
        iv_preview = (ImageView) view.findViewById(R.id.iv_preview);
        iv_previewf = (ImageView) view.findViewById(R.id.iv_previewf);
    }

    private void initData() {
    }

    private void initListener() {
        btn_camera.setOnClickListener(this);
        btn_galley.setOnClickListener(this);
        btn_cameraf.setOnClickListener(this);
        btn_galleyf.setOnClickListener(this);
        btn_preview.setOnClickListener(this);
    }

    private String tag;//标识
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_camera:
                btn_camera.setChecked(true);
                btn_galley.setChecked(false);
                tag ="1";
                gotoCarema();
                break;
            case R.id.btn_galley:
                btn_camera.setChecked(false);
                btn_galley.setChecked(true);
                tag ="1";
                gotoGalley();
                break;
            case R.id.btn_cameraf:
                btn_cameraf.setChecked(true);
                btn_galleyf.setChecked(false);
                tag ="2";
                gotoCarema();
                break;
            case R.id.btn_galleyf:
                btn_cameraf.setChecked(false);
                btn_galleyf.setChecked(true);
                tag ="2";
                gotoGalley();
                break;
            case R.id.btn_preview:
                if (bytes == null && bytesf == null) {
                    Toast.makeText(ctx, "请先上传图片", Toast.LENGTH_SHORT).show();
                    return;
                }
//                Intent intent = new Intent(ctx, IDClipActivity.class);
//                intent.putExtra("bytes",bytes);
//                intent.putExtra("bytesf",bytesf);
//                startActivity(intent);
                break;
        }
    }

    /**
     * 调用相机
     */
    private void gotoCarema() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        startActivityForResult(intent, REQUEST_CAPTURE);
    }

    /**
     * 本地图库
     */
    public void gotoGalley() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            getBitMap(uri);
        } else if (requestCode == REQUEST_CAPTURE && resultCode == RESULT_OK) {
            Uri uri = Uri.fromFile(tempFile);
            getBitMap(uri);
        }
    }

    /**
     * 获取照片
     *
     * @param uri
     */
    private void getBitMap(Uri uri) {
        try {
            Bundle bundle = new Bundle();
            bundle.putParcelable("uri", uri);
            Intent intent = new Intent(ctx, IDPreviewActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        receiveBroadCast = new ReceiveBroadCast();
        IntentFilter filter = new IntentFilter();
        filter.addAction("bitmap");    //只有持有相同的action的接受者才能接收此广播
        ctx.registerReceiver(receiveBroadCast, filter);
    }

    public class ReceiveBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //得到广播中得到的数据，并显示出来
            if (tag.equals("1")) {
                bytes = intent.getByteArrayExtra("bytes");
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                iv_preview.setImageBitmap(bitmap);
            }else if (tag.equals("2")){
                bytesf = intent.getByteArrayExtra("bytes");
                Bitmap bitmapf = BitmapFactory.decodeByteArray(bytesf, 0, bytesf.length);
                iv_previewf.setImageBitmap(bitmapf);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiveBroadCast != null) {
            ctx.unregisterReceiver(receiveBroadCast);
        }
    }
}
