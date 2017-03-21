package huanxing_print.com.cn.printhome.net.request.print;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import huanxing_print.com.cn.printhome.net.HttpCallBack;
import huanxing_print.com.cn.printhome.net.request.BaseRequst;

/**
 * Created by LGH on 2017/3/20.
 */

public class PrintRequest extends BaseRequst {

    public static final String FILE_NAME = "fileName";
    public static final String FILE_URL = "fileUrl";
    public static final String FILE_TYPE = "fileType";
    public static final String FILE_CONTENT = "fileContent";
    public static final String NEED_WATER = "needWater";
    public static final String PRINTER_NO = "printerNo";
    public static final String ID = "id";
    public static final String DOUBLE_FLAG = "doubleFlag";
    public static final String COLOUR_FLAG = "colourFlag";
    public static final String PRINT_COUNT = "printCount";
    public static final String SIZE_TYPE = "sizeType";
    public static final String DIRECTION_FLAG = "directionFlag";
    public static final String PAGE_NUM = "pageNum";
    public static final String PAGE_SIZE = "pageSize";

    public static final String BASE_URL = "http://appprint.inkin.cc/";
    public static final String UPLOAD = "common/fileUpload";

    public static final void uploadFile(Context context, String fileType, String fileContent, String fileName, String
            needWater
            , final HttpCallBack callback) {
        String url = BASE_URL + UPLOAD;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(FILE_TYPE, fileType);
        params.put(FILE_CONTENT, fileContent);
        params.put(FILE_NAME, fileName);
        params.put(NEED_WATER, needWater);
        Http.post(context, url, params, callback);
    }

    public static final void addFile(Context context, String printerNo, String fileName, String fileUrl, final
    HttpCallBack callback) {

    }
}
