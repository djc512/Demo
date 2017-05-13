package huanxing_print.com.cn.printhome.ui.activity.copy;

import android.graphics.Rect;

/**
 * Created by Administrator on 2017/5/13 0013.
 */

public class ClipUtil {

    private double a4Width = 210;//a4纸的宽
    private double a4Height = 297;//a4纸的高
    private double picWidth;//图片的实际高
    private double picHeight;//图片的实际宽

    public ClipUtil(double picWidth, double picHeight) {
        this.picWidth = picWidth;
        this.picHeight = picHeight;
    }

    private void drawImage(){
        Rect src = new Rect();// 图片 >>原矩形
        Rect dst = new Rect();// 屏幕 >>目标矩形
    }
}
