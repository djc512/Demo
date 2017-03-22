package huanxing_print.com.cn.printhome.net.request.print;

import android.app.Activity;

import java.util.HashMap;
import java.util.Map;

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
    public static final String FILE_UPLOAD = "common/fileUpload";
    public static final String FILE_ADD = "print/file/add";
    public static final String FILE_DEL = "print/file/del";
    public static final String SETTING_MODIFY = "print/file/updateToPrint";
    public static final String QUERY_PRINT_LIST = "print/file/queyList";

    public static final String TOKEN = "33b2abe48a76468682880e86b6fa0c2f";

    public static final Map<String, String> headerMap = new HashMap<String, String>() {
        {
            put("apiversion", "1");
            put("platform", "android");
        }
    };

    public static final Map<String, String> headerTokenMap = new HashMap<String, String>() {
        {
            put("apiversion", "1");
            put("platform", "android");
            put("loginToken", TOKEN);
        }
    };

    public static final void uploadFile(Activity activity, String fileType, String fileContent, String fileName, String
            needWater, final HttpListener callback) {
        String url = BASE_URL + FILE_UPLOAD;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(FILE_TYPE, fileType);
        params.put(FILE_CONTENT, fileContent);
        params.put(FILE_NAME, fileName);
        params.put(NEED_WATER, needWater);
        Http.postString(activity, url, params, headerMap, callback);
    }

    public static final void addFile(Activity activity, String printerNo, String fileName, String fileUrl, final
    HttpListener callback) {
        String url = BASE_URL + FILE_ADD;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(FILE_NAME, fileName);
        params.put(FILE_URL, fileUrl);
        params.put(PRINTER_NO, printerNo);
        Http.postString(activity, url, params, headerTokenMap, callback);
    }

    public static final void delFile(Activity activity, int id, final HttpListener callback) {
        String url = BASE_URL + FILE_DEL;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(ID, id);
        Http.postString(activity, url, params, headerTokenMap, callback);
    }

    public static final void modifySetting(Activity activity, int colourFlag, int directionFlag, int doubleFlag, int
            id, int printCount, int sizeType, final HttpListener callback) {
        String url = BASE_URL + SETTING_MODIFY;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(COLOUR_FLAG, colourFlag);
        params.put(DIRECTION_FLAG, directionFlag);
        params.put(DOUBLE_FLAG, doubleFlag);
        params.put(ID, id);
        params.put(PRINT_COUNT, printCount);
        params.put(SIZE_TYPE, sizeType);
        Http.postString(activity, url, params, headerTokenMap, callback);
    }

    public static final void queryPrintList(Activity activity, int pageNum, int pageSize, final HttpListener callback) {
        String url = BASE_URL + QUERY_PRINT_LIST;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(PAGE_NUM, pageNum);
        params.put(PAGE_SIZE, pageSize);
        url += "?&pageNum=1&pageSize=1";
        Http.get(activity, url, params, headerTokenMap, callback);
//        Http.postString(activity, url, params, headerTokenMap, callback);
    }
}
