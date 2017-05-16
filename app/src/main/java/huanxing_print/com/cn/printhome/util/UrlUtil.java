package huanxing_print.com.cn.printhome.util;

import android.net.Uri;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by LGH on 2017/3/23.
 */

public class UrlUtil {
    public static final String getUrl(String baseUrl, Map<String, Object> params) {
        String url = baseUrl;
        if (params != null) {
            Iterator<String> it = params.keySet().iterator();
            StringBuffer sb = null;
            while (it.hasNext()) {
                String key = it.next();
                String value = params.get(key).toString();
                if (sb == null) {
                    sb = new StringBuffer();
                    sb.append("?");
                } else {
                    sb.append("&");
                }
                sb.append(key);
                sb.append("=");
                sb.append(value);
            }
            url += sb.toString();
        }
        return url;
    }

    public static String getValueByName(String url, String name) {
        Uri uri = Uri.parse(url);
        String result = uri.getQueryParameter(name);
        return result;
    }
}
