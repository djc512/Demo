package huanxing_print.com.cn.printhome.net.callback.comment;

import java.util.List;

import huanxing_print.com.cn.printhome.model.comment.PicDataBean;
import huanxing_print.com.cn.printhome.net.callback.BaseCallback;

/**
 * Created by Administrator on 2017/5/11 0011.
 */

public abstract class UpLoadPicCallBack implements BaseCallback {
    public abstract void success(List<PicDataBean> bean);
}
