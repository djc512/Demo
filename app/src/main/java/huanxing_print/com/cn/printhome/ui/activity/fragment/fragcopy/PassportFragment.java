package huanxing_print.com.cn.printhome.ui.activity.fragment.fragcopy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
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
import huanxing_print.com.cn.printhome.ui.activity.copy.IDPreviewActivity;
import huanxing_print.com.cn.printhome.ui.activity.copy.PassportClipActivity;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.copy.PicSaveUtil;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Administrator on 2017/4/28 0028.
 */

public class PassportFragment extends Fragment implements View.OnClickListener {
    private RadioButton btn_camera;
    private RadioButton btn_galley;
    private TextView btn_preview;
    private int PICK_IMAGE_REQUEST = 1;
    //调用照相机返回图片临时文件
    private File tempFile;
    //请求相机
    private static final int REQUEST_CAPTURE = 100;
    private PicSaveUtil saveUtil;
    private Context ctx;
    private ImageView iv_preview;
    private ReceiveBroadCast receiveBroadCast;
    private byte[] bytes;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ctx = getActivity();
        saveUtil = new PicSaveUtil(ctx);
        CommonUtils.initSystemBar(getActivity());
        tempFile = saveUtil.createCameraTempFile(savedInstanceState);
        View view = inflater.inflate(R.layout.frag_passport, null);
        initView(view);
        initData();
        initListener();
        return view;
    }

    private void initView(View view) {

        btn_camera = (RadioButton) view.findViewById(R.id.btn_camera);
        btn_galley = (RadioButton) view.findViewById(R.id.btn_galley);
        btn_preview = (TextView) view.findViewById(R.id.btn_preview);
        iv_preview = (ImageView) view.findViewById(R.id.iv_preview);
    }

    private void initData() {
    }

    private void initListener() {
        btn_camera.setOnClickListener(this);
        btn_galley.setOnClickListener(this);
        btn_preview.setOnClickListener(this);
    }

    private String tag;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_camera:
                btn_camera.setChecked(true);
                btn_galley.setChecked(false);
                tag = "1";
                gotoCarema();
                break;
            case R.id.btn_galley:
                btn_camera.setChecked(false);
                btn_galley.setChecked(true);
                tag = "1";
                gotoGalley();
                break;
            case R.id.btn_preview:
                if (bytes == null) {
                    Toast.makeText(ctx, "请先上传图片", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(ctx, PassportClipActivity.class);
                intent.putExtra("bytes", bytes);
                startActivity(intent);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bytes = null;
                        iv_preview.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.passport));
                    }
                }, 200);
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

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onReceive(Context context, Intent intent) {
            //得到广播中得到的数据，并显示出来
            if ("1".equals(tag)) {
                bytes = intent.getByteArrayExtra("bytes");
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                iv_preview.setImageBitmap(bitmap);
                initBtnPreview();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void initBtnPreview() {
        btn_preview.setBackground(getResources().getDrawable(R.drawable.shape_preview_finish_bg));
        btn_preview.setTextColor(getResources().getColor(R.color.black));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiveBroadCast != null) {
            ctx.unregisterReceiver(receiveBroadCast);
        }
    }

    /**
     * 获取手机的屏幕状态
     * true为打开，false为关闭
     */
    public boolean isScreenon() {
        PowerManager powerManager = (PowerManager) getActivity()
                .getSystemService(Context.POWER_SERVICE);
        boolean ifOpen = powerManager.isScreenOn();
        return ifOpen;
    }
}
