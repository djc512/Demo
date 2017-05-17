package huanxing_print.com.cn.printhome.util;

import com.amap.api.maps2d.model.LatLng;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import huanxing_print.com.cn.printhome.log.Logger;

/**
 * Created by LGH on 2017/3/24.
 */

public class StringUtil {

    public static final int stringToInt(String str) {
        int i = -1;
        try {
            i = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return i;
    }

    public static final long stringToLong(String str) {
        long l = -1;
        try {
            l = Long.parseLong(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return l;
    }

    public static final float stringToFloat(String str) {
        float f = -1;
        try {
            f = Float.parseFloat(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }

    public static final double stringToDouble(String str) {
        double d = -1;
        try {
            d = Double.valueOf(str).doubleValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;
    }

    /**
     * 价格两位小数String
     *
     * @param f
     * @return
     */
    public static final String floatToString(float f) {
        DecimalFormat df = new DecimalFormat("0.00");
        String fStr = df.format(f);
        return fStr;
    }

    public static final String getDistance(String dis) {
        Logger.i(dis);
        if (stringToInt(dis) < 0) {
            return "";
        }
        DecimalFormat df = new DecimalFormat("0.00");
        String disStr = df.format((float) stringToInt(dis) / 1000) + "km";
        return disStr;
    }

    public static final LatLng getLatLng(String location) {
        Logger.i(location);
        String str[] = location.split(",");
        double var1 = stringToDouble(str[0]);
        double var2 = stringToDouble(str[1]);
        Logger.i("var1:" + var1);
        Logger.i("var2" + var2);
        if (var1 < 0 || var2 < 0) {
            return null;
        }
        LatLng latLng = new LatLng(var2, var1);
        Logger.i(latLng.toString());
        return latLng;
    }

    public static final String stringToTime(String str) {
        String result = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Long time = new Long(stringToLong(str));
        result= format.format(time);
        return result;
    }
}
