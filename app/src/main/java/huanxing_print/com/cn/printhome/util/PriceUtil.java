package huanxing_print.com.cn.printhome.util;

import java.text.DecimalFormat;

import huanxing_print.com.cn.printhome.model.print.PrintInfoResp;
import huanxing_print.com.cn.printhome.model.print.PrintSetting;

/**
 * Created by LGH on 2017/5/11.
 */

public class PriceUtil {

//{colourFlag=1, directionFlag=1, doubleFlag=1, fileName='XP0F78FIBWS9YITGFXOEKDC.png', filePage=1,
// fileUrl='http://139.196.224.235:12003/file/df_/g1/M00/00/64/Ci-4nVkTu9WAHFjIAAADstAedTk772.png', id=4256,
// printCount=1, printerType=0, sizeType=0}

    //"a3BlackPrice":"0.3","a3ColorPrice":"0.4","a4BlackPrice":"0.1","a4ColorPrice":"0.2","addTime":1490925707000,
    // "capability":"黑白  彩色","companyName":"南京田中机电再制造有限公司高淳分公司","delFlag":0,"id":48,
    // "printAddress":"江苏省南京市秦淮区东瓜匙路与明匙路交叉口西南150米","printName":"测试-教育机","printerNo":"zwf001","printerType":"1",
    // "resolution":"1200*1200dpi","technicalType":"仅支持激光","updateTime":1490925707000

    public static float getPrice(final PrintSetting printSetting, final PrintInfoResp.PrinterPrice printerPrice) {
        float price;
        if (PrintUtil.SETTING_COLOR == printSetting.getColourFlag()) {
            if (PrintUtil.SETTING_SIZE_A4 == printSetting.getSizeType()) {
                price = StringUtil.stringToFloat(printerPrice.getA4ColorPrice());
                if (PrintUtil.SETTING_PHOTO == printSetting.getPaperType()) {
                    price = price * 1.1f;
                }
            } else {
                price = StringUtil.stringToFloat(printerPrice.getA3ColorPrice());
            }
        } else {
            if (PrintUtil.SETTING_SIZE_A4 == printSetting.getSizeType()) {
                price = StringUtil.stringToFloat(printerPrice.getA4BlackPrice());
            } else {
                price = StringUtil.stringToFloat(printerPrice.getA3BlackPrice());
            }
        }
        if (PrintUtil.SETTING_DOUBLE_FLAG_YES == printSetting.getDoubleFlag()) {
            price = (float) (price * 1.5);
        }
        price = price * printSetting.getPrintCount() * printSetting.getFilePage();
        DecimalFormat df = new DecimalFormat("0.00");
        float f = StringUtil.stringToFloat(df.format(price));
        return f;
    }
}
