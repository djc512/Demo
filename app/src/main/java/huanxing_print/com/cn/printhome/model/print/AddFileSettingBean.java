package huanxing_print.com.cn.printhome.model.print;

import huanxing_print.com.cn.printhome.model.CommonResp;

/**
 * Created by LGH on 2017/3/20.
 */

public class AddFileSettingBean extends CommonResp {

    private PrintSetting data;

    public PrintSetting getData() {
        return data;
    }

    public void setData(PrintSetting data) {
        this.data = data;
    }


}
