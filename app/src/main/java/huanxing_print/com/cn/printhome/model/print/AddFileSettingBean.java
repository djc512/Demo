package huanxing_print.com.cn.printhome.model.print;

import java.util.List;

import huanxing_print.com.cn.printhome.model.CommonResp;

/**
 * Created by LGH on 2017/3/20.
 */

public class AddFileSettingBean extends CommonResp {

    private List<PrintSetting> data;

    public List<PrintSetting> getData() {
        return data;
    }

    public void setData(List<PrintSetting> data) {
        this.data = data;
    }


}
