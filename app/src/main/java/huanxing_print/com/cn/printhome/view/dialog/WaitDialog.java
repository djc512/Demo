/*
 * Copyright 2015 Yan Zhenjie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package huanxing_print.com.cn.printhome.view.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import com.zhy.http.okhttp.request.RequestCall;

import huanxing_print.com.cn.printhome.log.Logger;

/**
 * Created by LGH on 2017/3/20.
 */
public class WaitDialog extends ProgressDialog {

    private RequestCall request;

    public WaitDialog(Context context, RequestCall request, OnCancelListener onCancelListener) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(false);
        setProgressStyle(STYLE_SPINNER);
        setMessage("请稍后");
        setOnCancelListener(onCancelListener);
        this.request = request;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWaitDialog = this;
    }

    public static WaitDialog mWaitDialog;

    public synchronized static WaitDialog getInstance(Context context, RequestCall request, OnCancelListener onCancelListener) {
        return new WaitDialog(context, request, onCancelListener);
    }

    public static void showDialog(Context context, final RequestCall request,OnCancelListener onCancelListener) {
        Logger.i("showDialog");
        if (mWaitDialog != null) {
            mWaitDialog.show();
        } else {
            getInstance(context, request, onCancelListener).show();
        }
    }

    public static void dismissDialog() {
        if (mWaitDialog != null) {
            Logger.i("dismiss");
            mWaitDialog.dismiss();
            mWaitDialog = null;
        }
    }
}
