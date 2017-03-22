package huanxing_print.com.cn.printhome.model.print;

import huanxing_print.com.cn.printhome.model.CommonResp;

/**
 * Created by LGH on 2017/3/21.
 */

public class UploadImgBean extends CommonResp {
    private Url data;

    public Url getData() {
        return data;
    }

    public void setData(Url data) {
        this.data = data;
    }

   public class Url {
        String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

}
