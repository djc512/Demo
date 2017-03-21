package huanxing_print.com.cn.printhome.util;

/**
 * Created by LGH on 2017/3/17.
 */

public class DocTypeUtil {
    public static final int TYPE_DOC = 1;
    public static final int TYPE_DOCX = 2;
    public static final int TYPE_PDF = 3;
    public static final int TYPE_PPT = 4;
    public static final int TYPE_PPTX = 5;
    public static final int TYPE_IMG = 6;

    public static final int getPrintType(String path) {
        FileType fileType = new FileType();
        fileType.initReflect();
        int type = fileType.getMediaFileType(path);
        if (fileType.isImageFile(type)) {
            return TYPE_IMG;
        }
        String[] strs = path.split("\\.");
        String extension = strs[strs.length - 1];
        if ("doc".equalsIgnoreCase(extension))
            return TYPE_DOC;
        if ("docx".equalsIgnoreCase(extension))
            return TYPE_DOCX;
        if ("pdf".equalsIgnoreCase(extension))
            return TYPE_PDF;
        if ("ppt".equalsIgnoreCase(extension))
            return TYPE_PPT;
        if ("pptx".equalsIgnoreCase(extension))
            return TYPE_PPTX;
        return -1;
    }

    public static final boolean isPrintType(String path) {
        if (getPrintType(path) > 0) {
            return true;
        }
        return false;
    }
}
