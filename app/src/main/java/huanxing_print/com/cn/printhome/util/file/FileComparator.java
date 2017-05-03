package huanxing_print.com.cn.printhome.util.file;

import java.io.File;
import java.util.Comparator;

/**
 * Created by LGH on 2017/5/2.
 */

public class FileComparator implements Comparator<File> {

    public static final int MODE_TIME = 1;
    public static final int MODE_NAME = 2;
    public static final int MODE_TYPE = 3;

    private int mode;

    public FileComparator(int mode) {
        this.mode = mode;
    }

    @Override
    public int compare(File pFile1, File pFile2) {
        switch (mode) {
            case MODE_TIME:
                long time1 = pFile1.lastModified();
                long time2 = pFile2.lastModified();
                if (pFile1.isDirectory() && pFile2.isDirectory()) {
                    return time1 > time2 ? -1 : 1;
                } else {
                    if (pFile1.isDirectory() && pFile2.isFile()) {
                        return -1;
                    } else if (pFile1.isFile() && pFile2.isDirectory()) {
                        return 1;
                    } else {
                        return time1 > time2 ? -1 : 1;
                    }
                }
            case MODE_NAME:
                if (pFile1.isDirectory() && pFile2.isDirectory()) {
                    return pFile1.getName().compareToIgnoreCase(pFile2.getName());
                } else {
                    if (pFile1.isDirectory() && pFile2.isFile()) {
                        return -1;
                    } else if (pFile1.isFile() && pFile2.isDirectory()) {
                        return 1;
                    } else {
                        return pFile1.getName().compareToIgnoreCase(pFile2.getName());
                    }
                }
            case MODE_TYPE:
                ;

        }
        return -1;
    }
}
