package huanxing_print.com.cn.printhome.util;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by LGH on 2017/4/28.
 */

public class WifiUtil {

    public static String getConnectWifiSsid(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        Log.d("wifiInfo", wifiInfo.toString());
        Log.d("SSID", wifiInfo.getSSID());
        return wifiInfo.getSSID();
    }
}
