package huanxing_print.com.cn.printhome.event.approval;

import java.util.ArrayList;

/**
 * Created by wanghao on 2017/5/22.
 */

public class AttachmentUpdate {
    private String tag;
    private ArrayList<String> attachments;
    public AttachmentUpdate(String t, ArrayList<String> attachments) {
        this.tag = t;
        this.attachments = attachments;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public ArrayList<String> getAttachments() {
        return attachments;
    }

    public void setAttachments(ArrayList<String> attachments) {
        this.attachments = attachments;
    }
}
