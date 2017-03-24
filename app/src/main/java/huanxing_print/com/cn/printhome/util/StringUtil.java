package huanxing_print.com.cn.printhome.util;

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
        return -1;
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
}
