package huanxing_print.com.cn.printhome.util.contact;


import java.util.Comparator;

/**
 * Created by wanghao on 2017/5/3.
 */

public class ContactComparator implements Comparator<String> {
    @Override
    public int compare(String s, String t1) {
        int c1 = (s.charAt(0) + "").toUpperCase().hashCode();
        int c2 = (t1.charAt(0) + "").toUpperCase().hashCode();

        boolean c1Flag = (c1 < "A".hashCode() || c1 > "Z".hashCode()); // 不是字母
        boolean c2Flag = (c2 < "A".hashCode() || c2 > "Z".hashCode()); // 不是字母
        if (c1Flag && !c2Flag) {
            return -1;
        } else if (!c1Flag && c2Flag) {
            return 1;
        }

        return c1 - c2;
    }
}
