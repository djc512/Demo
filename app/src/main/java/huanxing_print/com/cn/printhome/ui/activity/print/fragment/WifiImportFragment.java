package huanxing_print.com.cn.printhome.ui.activity.print.fragment;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.util.StorageUtil;
import huanxing_print.com.cn.printhome.util.WifiUtil;

/**
 * Created by LGH on 2017/4/27.
 */

public class WifiImportFragment extends BaseLazyFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_wifi_file, container, false);
            isPrepared = true;
            if (!isLoaded) {
                lazyLoad();
            }
        }
        return view;
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
        TextView wifiTv = (TextView) view.findViewById(R.id.wifiTv);
        wifiTv.setText("已连接" + WifiUtil.getConnectWifiSsid(context));
        TextView sdTv = (TextView) view.findViewById(R.id.sdTv);
        sdTv.setText(StorageUtil.getSdInfo(context));
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        progressBar.setProgress(StorageUtil.getSdUsablePercent(context));
    }

    @Override
    public void startIntentSenderForResult(IntentSender intent, int requestCode, @Nullable Intent fillInIntent, int
            flagsMask, int flagsValues, int extraFlags, Bundle options) throws IntentSender.SendIntentException {
        super.startIntentSenderForResult(intent, requestCode, fillInIntent, flagsMask, flagsValues, extraFlags,
                options);
    }
}