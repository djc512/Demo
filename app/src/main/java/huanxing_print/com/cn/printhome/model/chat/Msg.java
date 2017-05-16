package huanxing_print.com.cn.printhome.model.chat;

/**
 * Created by dd on 2017/5/15.
 */

public class Msg {
    private String msgId;//消息id
    private boolean hasDeal;//	true已处理 false未处理

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public boolean isHasDeal() {
        return hasDeal;
    }

    public void setHasDeal(boolean hasDeal) {
        this.hasDeal = hasDeal;
    }
}
