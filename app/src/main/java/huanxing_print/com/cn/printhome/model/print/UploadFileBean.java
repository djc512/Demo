package huanxing_print.com.cn.printhome.model.print;

import huanxing_print.com.cn.printhome.model.CommonResp;

/**
 * Created by LGH on 2017/3/21.
 */

public class UploadFileBean extends CommonResp {
    private Url data;

    public Url getData() {
        return data;
    }

    public void setData(Url data) {
        this.data = data;
    }

    public class Url {
        String imgUrl;

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }
    }

}
