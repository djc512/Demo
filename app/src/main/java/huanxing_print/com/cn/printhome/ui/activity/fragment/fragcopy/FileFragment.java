package huanxing_print.com.cn.printhome.ui.activity.fragment.fragcopy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.File;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.ui.activity.copy.PreviewActivity;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.copy.PicSaveUtil;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Administrator on 2017/4/28 0028.
 */

public class FileFragment extends Fragment implements View.OnClickListener {
    private RadioButton btn_camera;
    private RadioButton btn_pic;
    private int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_CAPTURE = 100;
    private File tempFile;
    private Context ctx;
    private PicSaveUtil saveUtil;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ctx = getActivity();
        CommonUtils.initSystemBar(getActivity());
        saveUtil = new PicSaveUtil(ctx);
        tempFile = saveUtil.createCameraTempFile(savedInstanceState);
        View view = inflater.inflate(R.layout.frag_file, null);
        initView(view);
        initData();
        initListener();
        return view;
    }

    private void initData() {
    }

    private void initListener() {
        btn_camera.setOnClickListener(this);
        btn_pic.setOnClickListener(this);
    }

    private void initView(View view) {
        btn_camera = (RadioButton) view.findViewById(R.id.btn_camera);
        btn_pic = (RadioButton) view.findViewById(R.id.btn_pic);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_camera:
                btn_camera.setChecked(true);
                btn_pic.setChecked(false);
                gotoCamera();
                break;
            case R.id.btn_pic:
                btn_camera.setChecked(false);
                btn_pic.setChecked(true);
                gotoPic();
                break;
        }
    }

    /**
     * 图库
     */
    private void gotoPic() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    /**
     * 拍照
     */
    private void gotoCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        startActivityForResult(intent, REQUEST_CAPTURE);
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
            Bitmap mBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
            int m = mBitmap.getByteCount() / 1024 / 1024;
            if (m > 6) {
                Toast.makeText(ctx, "图片过大，请重新拍照或者选择", Toast.LENGTH_SHORT).show();
            } else {
                Bundle bundle = new Bundle();
                bundle.putParcelable("uri", uri);
                Intent intent = new Intent(ctx, PreviewActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
