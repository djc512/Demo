package huanxing_print.com.cn.printhome.util.webserver;

import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangjian on 2017/5/16.
 */
public class WebUtil {

    /**
     * 图片后缀
     */
    private static List<String> imageFixes = new ArrayList<String>() {{
        add("BMP");
        add("bmp");
        add("JPG");
        add("jpg");
        add("JPEG");
        add("jpeg");
        add("PNG");
        add("png");
        add("GIF");
        add("gif");
    }};

    /**
     * 获取post参数
     *
     * @param is
     * @return
     */
    public static String getPostParam(InputStream is) {
        StringBuilder result = new StringBuilder();
        try {
            InputStreamReader reader = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(reader);
            String line=null;
            while ((line = br.readLine()) != null) {
                result.append(line);
            }
            br.close();
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result.toString();
    }

    /**
     * 读取文件成txt返回页面
     *
     * @param fileName
     * @param asset
     * @return
     */
    public static String readHtml(String fileName, AssetManager asset) {
        StringBuilder result = new StringBuilder();
        InputStream is = null;
        try {
            is = asset.open(fileName);
            InputStreamReader reader = new InputStreamReader(is, "UTF-8");
            BufferedReader br = new BufferedReader(reader);
            String line;
            while ((line = br.readLine()) != null) {
                result.append(line);
            }
            br.close();
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result.toString();
    }


    /**
     * 判断是否是js静态资源
     *
     * @param uri
     * @return
     */
    public static boolean isJsFile(String uri) {
        return uri.endsWith(".js");
    }

    /**
     * 判断是否是图片
     *
     * @param uri
     * @return
     */
    public static boolean isImage(String uri) {
        for (String fix : imageFixes) {
            if (uri.endsWith(fix)) {
                return true;
            }
        }
        return false;
    }

}
