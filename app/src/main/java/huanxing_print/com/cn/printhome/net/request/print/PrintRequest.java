package huanxing_print.com.cn.printhome.net.request.print;

import android.app.Activity;

import java.util.HashMap;
import java.util.List;
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
    public static final String PRINT_NO = "printNo";
    public static final String ID = "id";
    public static final String DOUBLE_FLAG = "doubleFlag";
    public static final String COLOUR_FLAG = "colourFlag";
    public static final String PRINT_COUNT = "printCount";
    public static final String SIZE_TYPE = "sizeType";
    public static final String DIRECTION_FLAG = "directionFlag";
    public static final String PAGE_NUM = "pageNum";
    public static final String PAGE_SIZE = "pageSize";
    public static final String FILE_LIST = "fileList";
    public static final String ORDER_ID = "orderId";

    public static final String BASE_URL = "http://appprint.inkin.cc/";
    public static final String FILE_UPLOAD = "common/fileUpload";
    public static final String FILE_ADD = "print/file/add";
    public static final String FILE_DEL = "print/file/del";
    public static final String SETTING_MODIFY = "print/file/updateToPrint";
    public static final String QUERY_PRINT_LIST = "print/file/queyList";
    public static final String QUERY_PRINTERS = "print/printer/history";
    public static final String QUERY_PRINTER_PRICE = "print/printer/getDetail";
    public static final String ADD_ORDER = "order/add";
    public static final String PRINT = "order/add";

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

    /**
     * 上传文件
     *
     * @param activity
     * @param fileType
     * @param fileContent
     * @param fileName
     * @param needWater
     * @param callback
     */
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

    /**
     * 添加文件
     *
     * @param activity
     * @param printerNo
     * @param fileName
     * @param fileUrl
     * @param callback
     */
    public static final void addFile(Activity activity, String printerNo, String fileName, String fileUrl, final
    HttpListener callback) {
        String url = BASE_URL + FILE_ADD;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(FILE_NAME, fileName);
        params.put(FILE_URL, fileUrl);
        params.put(PRINTER_NO, printerNo);
        Http.postString(activity, url, params, headerTokenMap, callback);
    }

    /**
     * 删除文件
     *
     * @param activity
     * @param id
     * @param callback
     */
    public static final void delFile(Activity activity, int id, final HttpListener callback) {
        String url = BASE_URL + FILE_DEL;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(ID, id);
        Http.postString(activity, url, params, headerTokenMap, callback);
    }

    /**
     * 修改打印设置
     *
     * @param activity
     * @param colourFlag
     * @param directionFlag
     * @param doubleFlag
     * @param id
     * @param printCount
     * @param sizeType
     * @param callback
     */
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

    /**
     * 获取待打印列表
     *
     * @param activity
     * @param pageNum
     * @param pageSize
     * @param callback
     */
    public static final void queryPrintList(Activity activity, int pageNum, int pageSize, final HttpListener callback) {
        String url = BASE_URL + QUERY_PRINT_LIST;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(PAGE_NUM, pageNum);
        params.put(PAGE_SIZE, pageSize);
        Http.get(activity, url, params, headerTokenMap, callback);
    }

    /**
     * 获取最近使用打印机
     *
     * @param activity
     * @param callback
     */
    public static final void queryRecentPrinters(Activity activity, final HttpListener callback) {
        String url = BASE_URL + QUERY_PRINTERS;
        Http.get(activity, url, null, headerTokenMap, callback);
    }

    /**
     * 获取打印机价格
     *
     * @param activity
     * @param printerNo
     * @param callback
     */
    public static final void queryPrinterPrice(Activity activity, String printerNo, final HttpListener callback) {
        String url = BASE_URL + QUERY_PRINTER_PRICE;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(PRINT_NO, printerNo);
        Http.get(activity, url, params, headerTokenMap, callback);
    }

    /**
     * 下单成功
     *
     * @param activity
     * @param printNo
     * @param fileList
     * @param callback
     */
    public static final void addOrder(Activity activity, String printNo, List fileList, final HttpListener callback) {
        String url = BASE_URL + ADD_ORDER;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(PRINTER_NO, printNo);
        params.put(FILE_LIST, fileList);
        Http.postString(activity, url, params, headerTokenMap, callback);
    }

    public static final void print(Activity activity, int orderId, final HttpListener callback) {
        String url = BASE_URL + PRINT;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(ORDER_ID, orderId);
        Http.postString(activity, url, params, headerTokenMap, callback);
    }
}
