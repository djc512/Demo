package huanxing_print.com.cn.printhome.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import huanxing_print.com.cn.printhome.model.image.HeadImageBean;
import huanxing_print.com.cn.printhome.net.callback.image.HeadImageUploadCallback;
import huanxing_print.com.cn.printhome.net.request.image.HeadImageUploadRequest;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;

/**
 * Created by Administrator on 2017/5/5 0005.
 */

public class UpLoadBitmap {
    private Context ctx;

    public UpLoadBitmap(Context ctx) {
        this.ctx = ctx;
    }

    public void setPicToView(Bitmap bitMap) {
        //Bundle extras = picdata.getExtras();
        if (null != bitMap) {
            //Bitmap photo = extras.getParcelable("data");
            //Bitmap photo =filterColor(phmp);
            String filePath = FileUtils.savePic(ctx, "headImg.jpg", bitMap);
            if (!ObjectUtils.isNull(filePath)) {
                File file = new File(filePath);
                //file转化成二进制
                byte[] buffer = null;
                FileInputStream in ;
                int length = 0;
                try {
                    in = new FileInputStream(file);
                    buffer = new byte[(int) file.length() + 100];
                    length = in.read(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String data = Base64.encodeToString(buffer, 0, length, Base64.DEFAULT);

                DialogUtils.showProgressDialog(ctx, "文件上传中").show();
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("fileContent", data);
                params.put("fileName", filePath);
                params.put("fileType", ".jpg");
                HeadImageUploadRequest.upload(ctx,  params,
                        new HeadImageUploadCallback() {

                            @Override
                            public void fail(String msg) {
                                DialogUtils.closeProgressDialog();
                            }

                            @Override
                            public void connectFail() {
                                DialogUtils.closeProgressDialog();
                            }

                            @Override
                            public void success(String msg, HeadImageBean bean) {

                            }
                        });
            }
        }
    }
}
