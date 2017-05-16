package huanxing_print.com.cn.printhome.ui.activity.copy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.ui.activity.print.PickPrinterActivity;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.copy.BitmpaUtil;
import huanxing_print.com.cn.printhome.util.copy.ClipPicUtil;
import huanxing_print.com.cn.printhome.util.copy.OpenCVCallback;
import huanxing_print.com.cn.printhome.util.copy.PicSaveUtil;
import huanxing_print.com.cn.printhome.view.SelectionImageView;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;
import timber.log.Timber;

import static huanxing_print.com.cn.printhome.util.copy.ClipPicUtil.perspectiveTransform;

/**
 * Created by Administrator on 2017/4/26 0026.
 */

public class PreviewActivity extends BaseActivity implements View.OnClickListener {
    private TextView btn_adjust;
    private TextView btn_confirm;
    private Bitmap mBitmap;
    private static final int MAX_HEIGHT = 500;
    private Context ctx;
    //    private ProgressDialog pd;
    private BitmpaUtil bitmpaUtil;
    private Uri uri;
    private SelectionImageView selectionView;
    private OpenCVCallback mOpenCVLoaderCallback;
    private PhotoView iv;
    private LinearLayout ll;
    private LinearLayout ll1;
    private Bitmap mResult;
    private TextView btn_gray;
    private TextView btn_black;
    private TextView btn_original;
    private TextView btn_reset;
    private TextView btn_photoconfirm;
    //请求相机
    private static final int REQUEST_CAPTURE = 100;
    private File tempFile;
    private TextView btn_save;
    private PicSaveUtil saveUtil;
    private Bitmap compBitmap;
    private TextView btn_reset1;
    private TextView tv_back;
    private String path;
    private Bitmap rotateBitmap;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBarBlack(this);
        setContentView(R.layout.activity_preview);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        uri = bundle.getParcelable("uri");
        bitmpaUtil = new BitmpaUtil();
        ctx = this;
        ClipPicUtil.ctx = ctx;
        saveUtil = new huanxing_print.com.cn.printhome.util.copy.PicSaveUtil(ctx);
        tempFile = saveUtil.createCameraTempFile(savedInstanceState);

        initView();
        initData();
        initListener();
        initOpenCV();
    }

    private void initView() {
        btn_adjust = (TextView) findViewById(R.id.btn_adjust);
        btn_confirm = (TextView) findViewById(R.id.btn_confirm);
        selectionView = (SelectionImageView) findViewById(R.id.selectView);
        ll = (LinearLayout) findViewById(R.id.ll);
        ll1 = (LinearLayout) findViewById(R.id.ll1);
        iv = (PhotoView) findViewById(R.id.imageView);
        btn_original = (TextView) findViewById(R.id.btn_original);
        btn_gray = (TextView) findViewById(R.id.btn_gray);
        btn_black = (TextView) findViewById(R.id.btn_black);
        btn_reset = (TextView) findViewById(R.id.btn_reset);
        btn_photoconfirm = (TextView) findViewById(R.id.btn_photoconfirm);
        btn_save = (TextView) findViewById(R.id.btn_save);
        btn_reset1 = (TextView) findViewById(R.id.btn_reset1);
        tv_back = (TextView) findViewById(R.id.tv_back);
    }

    private void initData() {
        Drawable dpower = getResources().getDrawable(R.drawable.power);
        Drawable dgray = getResources().getDrawable(R.drawable.gray);
        dpower.setBounds(0, 0, 40, 40);
        dgray.setBounds(0, 0, 50, 50);
        btn_gray.setCompoundDrawables(dgray, null, null, null);
        btn_black.setCompoundDrawables(dpower, null, null, null);

        try {
            mBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Glide.with(getSelfActivity()).load(uri).into(selectionView);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        pd = new ProgressDialog(ctx);
//        pd.setProgress(ProgressDialog.STYLE_SPINNER);
//        pd.setCanceledOnTouchOutside(false);
//        if (!pd.isShowing()) {
//            pd.show();
//        }
        show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                pd.dismiss();
                close();
                btn_adjust.performClick();
            }
        }, 1000);
        btn_adjust.setVisibility(View.GONE);
    }

    private void initListener() {
        btn_adjust.setOnClickListener(this);
        btn_confirm.setOnClickListener(this);
        btn_gray.setOnClickListener(this);
        btn_black.setOnClickListener(this);
        btn_original.setOnClickListener(this);
        btn_reset.setOnClickListener(this);
        btn_reset1.setOnClickListener(this);
        btn_photoconfirm.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        tv_back.setOnClickListener(this);
    }

    private String saveName;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back:
                finishCurrentActivity();
                break;
            case R.id.btn_adjust://调整
                selectionView.setVisibility(View.VISIBLE);
                try {
                    clipPic();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_reset1://重拍
                selectionView.setVisibility(View.VISIBLE);
                try {
                    clipPic();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                gotoCamera();
                break;
            case R.id.btn_confirm:
                ll.setVisibility(View.GONE);
                ll1.setVisibility(View.VISIBLE);
                iv.setVisibility(View.INVISIBLE);
//                pd.show();
                show();
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        List<PointF> pointOriginal = selectionView.getPoints();
                        if (null != mBitmap) {
                            mBitmap = bitmpaUtil.comp(mBitmap);
                            Mat orig = new Mat();
                            Utils.bitmapToMat(mBitmap, orig);
                            Mat transformed = perspectiveTransform(orig, pointOriginal);
                            mResult = ClipPicUtil.applyThresholdOriginal(transformed);
                            compBitmap = bitmpaUtil.comp(mResult);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                                  pd.dismiss();
                                    close();
                                    DialogUtils.closeProgressDialog();
                                    iv.setVisibility(View.VISIBLE);
                                    ll.setVisibility(View.GONE);
                                    ll1.setVisibility(View.VISIBLE);
                                    iv.setImageBitmap(compBitmap);
                                }
                            });
                            orig.release();
                            transformed.release();
                        }
                    }
                }.start();
                break;
            case R.id.btn_black:
//                DialogUtils.showProgressDialog(ctx, "正在加载...");
                show();
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        List<PointF> pointOriginal = selectionView.getPoints();
                        if (mBitmap != null) {
                            mBitmap = bitmpaUtil.comp(mBitmap);
                            Mat orig = new Mat();
                            Utils.bitmapToMat(mBitmap, orig);
                            Mat transformed = perspectiveTransform(orig, pointOriginal);
                            mResult = ClipPicUtil.applyThresholdBlack(transformed);
                            compBitmap = bitmpaUtil.comp(mResult);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                                    pd.dismiss();
                                    close();
                                    ll.setVisibility(View.GONE);
                                    ll1.setVisibility(View.VISIBLE);
                                    iv.setImageBitmap(compBitmap);
                                }
                            });
                            orig.release();
                            transformed.release();
                        }
                    }
                }.start();
                break;
            case R.id.btn_gray:
//                pd.show();
                show();
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        List<PointF> pointOriginal = selectionView.getPoints();
                        if (mBitmap != null) {
                            mBitmap = bitmpaUtil.comp(mBitmap);
                            Mat orig = new Mat();
                            Utils.bitmapToMat(mBitmap, orig);
                            Mat transformed = perspectiveTransform(orig, pointOriginal);
                            mResult = ClipPicUtil.applyThresholdGray(transformed);
                            compBitmap = bitmpaUtil.comp(mResult);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                                    pd.dismiss();
                                    close();
                                    ll.setVisibility(View.GONE);
                                    ll1.setVisibility(View.VISIBLE);
                                    iv.setImageBitmap(compBitmap);
                                }
                            });
                            orig.release();
                            transformed.release();
                        }
                    }
                }.start();
                break;
            case R.id.btn_original:
//                pd.show();
                show();
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        List<PointF> pointOriginal = selectionView.getPoints();
                        if (mBitmap != null) {
                            mBitmap = bitmpaUtil.comp(mBitmap);
                            Mat orig = new Mat();
                            Utils.bitmapToMat(mBitmap, orig);
                            Mat transformed = perspectiveTransform(orig, pointOriginal);
                            mResult = ClipPicUtil.applyThresholdOriginal(transformed);
                            compBitmap = bitmpaUtil.comp(mResult);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                                    pd.dismiss();
                                    close();
                                    ll.setVisibility(View.GONE);
                                    ll1.setVisibility(View.VISIBLE);
                                    iv.setImageBitmap(compBitmap);
                                }
                            });
                            orig.release();
                            transformed.release();
                        }
                    }
                }.start();
                break;
            case R.id.btn_reset://从新拍照
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
                startActivityForResult(intent, REQUEST_CAPTURE);
                break;
            case R.id.btn_photoconfirm://跳转到印家打印
                if (compBitmap != null) {
                    String nameConfirm = System.currentTimeMillis() + ".jpg";
                    saveUtil.saveClipPic(compBitmap, nameConfirm);
                    String path = Environment.getExternalStorageDirectory().getPath() + "/image/" + nameConfirm;
                    Intent printIntent = new Intent(getSelfActivity(), PickPrinterActivity.class);
                    printIntent.putExtra("imagepath", path);
                    printIntent.putExtra("copyfile", true);
                    startActivity(printIntent);
                    finishCurrentActivity();
                }
                break;
            case R.id.btn_save:
                if (compBitmap != null) {
                    String nameSave = System.currentTimeMillis() + ".jpg";
                    saveUtil.saveClipPic(compBitmap, nameSave);
                }
                break;
        }
    }

    /**
     * 识别图片
     *
     * @throws IOException
     */
    private void clipPic() throws IOException {
        selectionView.setImageBitmap(getResizedBitmap(mBitmap, MAX_HEIGHT));
        Mat images = new Mat();
        Utils.bitmapToMat(mBitmap, images);
        PointF[] point = ClipPicUtil.getPoints(images);
        List<PointF> points = Arrays.asList(point);
        selectionView.setPoints(points);
    }

    /**
     * 比例缩放图片
     *
     * @param bitmap
     * @param maxHeight
     * @return
     */
    private Bitmap getResizedBitmap(Bitmap bitmap, int maxHeight) {
        double ratio = bitmap.getHeight() / (double) maxHeight;
        int width = (int) (bitmap.getWidth() / ratio);
        return Bitmap.createScaledBitmap(bitmap, width, maxHeight, false);
    }

    private void initOpenCV() {

        mOpenCVLoaderCallback = new OpenCVCallback(this) {
            @Override
            public void onManagerConnected(int status) {
                switch (status) {
                    case LoaderCallbackInterface.SUCCESS: {
                        break;
                    }
                    default: {
                        super.onManagerConnected(status);
                    }
                }
            }
        };

        if (!OpenCVLoader.initDebug()) {
            Timber.d("Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, mOpenCVLoaderCallback);
        } else {
            Timber.d("OpenCV library found inside package. Using it!");
            mOpenCVLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAPTURE && resultCode == RESULT_OK) {
            uri = Uri.fromFile(tempFile);
//            path = BitmapCorrectUtil.uriTopath(ctx,uri);
        }
        ll.setVisibility(View.VISIBLE);
        ll1.setVisibility(View.GONE);
        try {
            mBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
//            trunBitmap();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (mBitmap != null) {
            selectionView.setImageBitmap(mBitmap);
        }
    }

    /**
     * 判断是否需要旋转图片
     */
//    private void trunBitmap() {
//        int bitmapDegree = BitmapCorrectUtil.getBitmapDegree(path);
//        if (bitmapDegree == 90) {
//            rotateBitmap = BitmapCorrectUtil.rotateBitmapByDegree(mBitmap, 0);
//            mBitmap = rotateBitmap;
//        }
//    }

    /**
     * 拍照
     */
    private void gotoCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        startActivityForResult(intent, REQUEST_CAPTURE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mResult) {
            mResult.recycle();
            mResult = null;
        }
        if (null != mBitmap) {
            mBitmap.recycle();
            mBitmap = null;
        }
        if (null != compBitmap) {
            compBitmap.recycle();
            compBitmap = null;
        }
        System.gc();
    }

    /**
     * 显示进度条
     */
    private void show() {
        DialogUtils.showProgressDialog(ctx, "正在加载...").show();
    }

    /**
     * 关闭进度条
     */
    private void close() {
        DialogUtils.closeProgressDialog();
    }
}
