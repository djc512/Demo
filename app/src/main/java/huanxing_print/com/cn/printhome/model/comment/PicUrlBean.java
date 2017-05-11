package huanxing_print.com.cn.printhome.model.comment;

import java.util.List;

/**
 * Created by Administrator on 2017/5/11 0011.
 */

public class PicUrlBean {

    /**
     * data : [{"fileId":"1","imgUrl":"http://139.196.224.235:12003/file/df_/g1/M00/00/CF/Ci-4rlhTUTaAfjOTAAAAlNOMQYY705.png"},{"fileId":"2","imgUrl":"http://139.196.224.235:12003/file/df_/g1/M00/00/CF/Ci-4rlhTUTaAIlMkAAAAlNOMQYY647.png"}]
     * errorCode : 0
     * errorMsg :
     * success : true
     */

    private List<PicDataBean> data;

    public List<PicDataBean> getData() {
        return data;
    }

    public void setData(List<PicDataBean> data) {
        this.data = data;
    }

}
