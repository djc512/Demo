package huanxing_print.com.cn.printhome.util;

import java.io.File;

import huanxing_print.com.cn.printhome.R;

/**
 * Created by LGH on 2017/5/18.
 */

public class ImgIconUtil {

    public static int getDrawable(String fileName) {
        if (fileName.endsWith(".doc") || fileName.endsWith(".docx")) {
            return R.drawable.ic_word;
        }
        if (fileName.endsWith(".ppt") || fileName.endsWith(".pptx")) {
            return R.drawable.ic_ppt;
        }
        if (fileName.endsWith(".pdf")) {
            return R.drawable.ic_pdf;
        }
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return R.drawable.ic_jpg;
        }
        if (fileName.endsWith(".png")) {
            return R.drawable.ic_png;
        }
        if (fileName.endsWith(".bmp")) {
            return R.drawable.ic_bmp;
        }
        return R.drawable.ic_defaut_file;
    }

    public static int getDrawable(File file) {
        if (file.isDirectory()) {
            return R.drawable.ic_folder;
        }
        if (FileType.getPrintType(file.getPath()) == FileType.TYPE_DOC || FileType.getPrintType(file.getPath()) ==
                FileType.TYPE_DOCX) {
            return R.drawable.ic_word;
        }
        if (FileType.getPrintType(file.getPath()) == FileType.TYPE_PPT || FileType.getPrintType(file.getPath()) ==
                FileType.TYPE_PPTX) {
            return R.drawable.ic_ppt;
        }
        if (FileType.getPrintType(file.getPath()) == FileType.TYPE_PDF) {
            return R.drawable.ic_pdf;
        }
        if (FileType.getPrintType(file.getPath()) == FileType.TYPE_IMG) {
            return getDrawable(file.getName());
        }
        return R.drawable.ic_defaut_file;
    }
}
