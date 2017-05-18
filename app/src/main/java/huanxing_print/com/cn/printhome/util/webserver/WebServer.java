package huanxing_print.com.cn.printhome.util.webserver;

import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import huanxing_print.com.cn.printhome.log.Logger;
import huanxing_print.com.cn.printhome.util.FileUtils;
import huanxing_print.com.cn.printhome.util.encrypt.Base64;

public class WebServer extends NanoHTTPD {

    private final String TAG = "imWebService";

    public static final int PORT = 8899;
    public static String rootDir = Environment.getExternalStorageDirectory().getPath();
    private static Boolean serverState = false;
    private List<File> fileList;

    public static final String FILE_LIST = "file_list";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        fileList = (List<File>) intent.getExtras().getSerializable(FILE_LIST);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            start();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Response serve(IHTTPSession session) {
        Log.i(TAG, "页面请求后台：" + session.getUri());
        if (session.getMethod() == Method.GET) {
            // 处理静态资源 js
            if (WebUtil.isJsFile(session.getUri())) {
                return showJsFile(session.getUri());
            }
            // 获取文件列表
            if (session.getUri().contains("getfileList")) {
                return getfileList();
            }
            // 处理静态资源
            return newFixedLengthResponse(Response.Status.OK, NanoHTTPD.MIME_HTML, WebUtil.readHtml("index.html",
                    getAssets()));
        } else {
            // POST 处理文件上传
            Map<String, String> files = new HashMap<String, String>();
            JSONObject result = new JSONObject();
            try {
                session.parseBody(files);
                String postData = files.get("postData");
                result.put("success", true);
                JSONObject fileParamDto = new JSONObject(postData);
                Logger.i(fileParamDto.toString());
                // TODO 存文件 处理成html要的结果
                Logger.i(fileParamDto.getString("fileName"));
                String fileName =new String(Base64.decode(fileParamDto.getString("fileName")));
                Logger.i(fileName);
                String content = fileParamDto.getString("content");
                FileUtils.makeFile(FileUtils.getWifiUploadPath() + fileName);
                File target = new File(FileUtils.getWifiUploadPath() + fileName);
                FileUtils.base64ToFile(content, target);
                JSONObject fileResult = new JSONObject();
                fileResult.put("fileName", target.getName());
                fileResult.put("fileSize", FileUtils.prettySize(target.length()));
                fileResult.put("updateTime", new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault()).format(target
                        .lastModified()));
                result.put("fileInfo", fileResult);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return newFixedLengthResponse(Response.Status.OK, NanoHTTPD.MIME_HTML, result.toString());
        }
    }

    /**
     * 处理静态资源js
     *
     * @param uri
     * @return
     */
    private Response showJsFile(String uri) {
        return newFixedLengthResponse(WebUtil.readHtml(uri.substring(1), getAssets()));
    }

    /**
     * TODO
     * 处理获取文件列表接口
     *
     * @return
     */
    private Response getfileList() {
        JSONArray param = new JSONArray();
        if (fileList != null) {
            try {
                for (File file : fileList) {
                    JSONObject ob = new JSONObject();
                    ob.put("fileName", file.getName());
                    ob.put("fileSize", FileUtils.prettySize(file.length()));
                    ob.put("updateTime", new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault()).format(file
                            .lastModified()));
                    param.put(ob);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return newFixedLengthResponse(param.toString());
    }
}
