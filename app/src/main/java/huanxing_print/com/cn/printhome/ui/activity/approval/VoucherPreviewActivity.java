package huanxing_print.com.cn.printhome.ui.activity.approval;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.net.callback.approval.CheckVoucherCallBack;
import huanxing_print.com.cn.printhome.net.request.approval.ApprovalRequest;
import huanxing_print.com.cn.printhome.ui.activity.print.PickPrinterActivity;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;

/**
 * description: 凭证预览界面
 * author LSW
 * date 2017/5/8 20:08
 * update 2017/5/8
 */
public class VoucherPreviewActivity extends BaseActivity implements View.OnClickListener {

    private ImageView img_voucher;
    private String picPath = "";

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 改变状态栏的颜色使其与APP风格一体化
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.activity_voucher_preview);

        init();
    }

    private void init() {
        int approveId = getIntent().getIntExtra("approveId", 0);
        int type = getIntent().getIntExtra("type", 0);
        img_voucher = (ImageView) findViewById(R.id.img_voucher);
        findViewById(R.id.btn_print_proof).setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);
        ApprovalRequest.checkVoucher(getSelfActivity(), baseApplication.getLoginToken(),
                approveId, type, checkVoucherCallBack);
    }

    CheckVoucherCallBack checkVoucherCallBack = new CheckVoucherCallBack() {
        @Override
        public void success(String msg, String data) {
            if (!ObjectUtils.isNull(data)) {
                Glide.with(getSelfActivity())
                        .load(data)
                        .placeholder(R.drawable.iv_head)
                        .error(R.drawable.iv_head)
                        .into(img_voucher);

                savePic(data);
            }
        }

        @Override
        public void fail(String msg) {
            ToastUtil.doToast(VoucherPreviewActivity.this, msg);
        }

        @Override
        public void connectFail() {

        }
    };

    private void savePic(final String data) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Bitmap bitmap = Glide.with(getSelfActivity())
                            .load(data)
                            .asBitmap()
                            .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            .get();
                    if (bitmap != null){
                        // 在这里执行图片保存方法
                        saveImageToGallery(getSelfActivity(),bitmap);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finishCurrentActivity();
                break;
            case R.id.btn_print_proof:
                EventBus.getDefault().postSticky(new Integer(1));
                Intent intent = new Intent(VoucherPreviewActivity.this, PickPrinterActivity.class);
                intent.putExtra("imagepath", picPath);
                intent.putExtra("authflag", true);
                startActivity(intent);
                break;
        }
    }

    private File currentFile;
    public void saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsoluteFile();//注意小米手机必须这样获得public绝对路径
        String fileName = "新建文件夹";
        File appDir = new File(file ,fileName);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        String picName = System.currentTimeMillis() + ".jpg";
        currentFile = new File(appDir, picName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(currentFile);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            picPath = currentFile.getPath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
