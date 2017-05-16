package huanxing_print.com.cn.printhome.model.print;

import huanxing_print.com.cn.printhome.model.CommonResp;

/**
 * Created by LGH on 2017/5/16.
 */

public class IsOnlineResp extends CommonResp {
    public boolean getData() {
        return data;
    }

    public void setData(boolean data) {
        this.data = data;
    }

    private boolean data;
}
