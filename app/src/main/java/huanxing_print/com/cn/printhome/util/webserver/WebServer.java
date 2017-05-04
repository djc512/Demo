package huanxing_print.com.cn.printhome.util.webserver;

import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import huanxing_print.com.cn.printhome.log.Logger;
import huanxing_print.com.cn.printhome.util.FileUtils;

public class WebServer extends NanoHTTPD {
    private final String TAG = "imWebService";

    public static final int PORT = 8899;
    public static String rootDir = Environment.getExternalStorageDirectory().getPath();
    private static Boolean serverState = false;
    private static String uploadFileHTML =
            " <html>  <head> <meta http-equiv=\\\"content-type\\\" content=\\\"text/html; charset=UTF-8\\\"></head>\n" +
                    "<form method='post' enctype='multipart/form-data' action='/u'>" +
                    "    Step 1. Choose a file: <input type='file' name='upload' /><br />" +
                    "    Step 2. Click Send to upload file: <input type='submit' value='Send' /><br />" +
                    "</form></html>";

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

    private Response respondUpload(IHTTPSession session) {
        if (session.getMethod() == Method.GET) {
            return newFixedLengthResponse(Response.Status.OK, NanoHTTPD.MIME_HTML, uploadFileHTML);
        } else {
            String msg = "upload failure";
            Map<String, String> files = new HashMap<String, String>();
            try {
                session.parseBody(files);
                Logger.i(session);
                Map<String, String> parms = session.getParms();
                Set<String> keys = files.keySet();
                for (String str : keys) {
                    Logger.i("SetKey:" + str);
                }
                for (String key : keys) {
                    String location = files.get(key);
                    File temp = new File(location);
//                    String afileName = new String(session.getParms().get("upload").getBytes("ISO-8859-1"), "UTF-8");
                    String afileName = new String(session.getParms().get("upload").toString());
                    FileUtils.makeFile(FileUtils.getWifiUploadPath() + afileName);
                    File target = new File(FileUtils.getWifiUploadPath() + afileName);
                    msg += "upload success to " + target.getPath();
                    InputStream in = new FileInputStream(temp);
                    OutputStream out = new FileOutputStream(target);
                    byte[] buf = new byte[1024 * 8];
                    int len;
                    BufferedOutputStream outb = new BufferedOutputStream(out);
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    in.close();
                    out.close();
                    outb.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (ResponseException e1) {
                e1.printStackTrace();
            }
            return newFixedLengthResponse(Response.Status.OK, NanoHTTPD.MIME_HTML, msg);
        }
    }

    private Response respond(Map<String, String> headers, IHTTPSession session, String uri) {
        Response r;
        Log.i(TAG, "WebServer.respond(" + rootDir + "," + uri + ") without cors");
        return respondUpload(session);
    }

    @Override
    public Response serve(IHTTPSession session) {
        String uri = session.getUri();
        Map<String, String> headers = session.getHeaders();
        return respond(Collections.unmodifiableMap(headers), session, uri);
    }
}
