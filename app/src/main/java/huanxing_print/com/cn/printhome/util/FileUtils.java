package huanxing_print.com.cn.printhome.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import huanxing_print.com.cn.printhome.constant.ConFig;

public class FileUtils {

    public static final String FILE_SEPARATOR = File.separator;
    private static Object filePath;

    public static boolean isSDCardEnable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    }

    public static String getWifiUploadPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "ecostar" + File
                .separator + "WiFi" + File.separator;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static long getSDCardAllSize() {
        if (isSDCardEnable()) {
            StatFs stat = new StatFs(getSDCardPath());
            // 获取空闲的数据块的数量
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                long availableBlocks = stat.getAvailableBlocksLong() - 4;
                // 获取单个数据块的大小（byte）
                long freeBlocks = stat.getAvailableBlocksLong();
                return freeBlocks * availableBlocks;

            } else {
                long availableBlocks = stat.getAvailableBlocks() - 4;
                // 获取单个数据块的大小（byte）
                long freeBlocks = stat.getAvailableBlocks();
                return freeBlocks * availableBlocks;
            }
        }
        return 0;
    }

    public static String getRootDirectoryPath() {
        return Environment.getRootDirectory().getAbsolutePath();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static long getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            long blockSize = stat.getBlockSizeLong();
            long availableBlocks = stat.getAvailableBlocksLong();
            return availableBlocks * blockSize;
        } else {
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return availableBlocks * blockSize;
        }
    }

    public static String prettySize(long size) {
        int unitNum = 1000;
        String result = null;
        if (size < unitNum) {
            return size + "B";
        }
        float newSize = size;
        String[] units = {"KB", "MB", "GB", "TB"};
        for (String unit : units) {
            newSize = newSize / (float) unitNum;
            if (newSize < unitNum) {
                result = String.format(Locale.getDefault(), "%.02f", newSize) + unit;
                break;
            }
        }
        return result;
    }

    public static boolean isFileExist(String path) {
        File file = new File(path);
        if (file.exists()) {
            return true;
        }
        return false;
    }

    public static boolean isFileExist(File file) {
        if (null != file && file.exists()) {
            return true;
        }
        return false;
    }

    public static boolean makeFile(String path) {
        File file = new File(path);
        if (path.endsWith(FILE_SEPARATOR)) {
            return false;
        }
        // 判断目标文件所在的目录是否存在
        if (!file.getParentFile().exists()) {
            // 如果目标文件所在的目录不存在，则创建父目录
            if (!file.getParentFile().mkdirs()) {
                return false;
            }
        }
        // 创建目标文件
        try {
            if (file.exists()) {
                file.delete();
            }
            if (file.createNewFile()) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean makeDir(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        file.mkdirs();
        return isFileExist(path);
    }

    public static boolean deleteFile(String path, FileDeleteCallback fileDeleteCallback, boolean... isDeleteDir) {

        File file = new File(path);
        if (!file.exists()) {

            if (null != fileDeleteCallback) {
                fileDeleteCallback.result(2);
            }
        }
        if (!file.isDirectory()) {
            file.delete();
        } else if (file.isDirectory()) {
            String[] fileList = file.list();
            for (int i = 0; i < fileList.length; i++) {
                File delfile = new File(path + FILE_SEPARATOR + fileList[i]);
                if (!delfile.isDirectory()) {
                    delfile.delete();
                } else if (delfile.isDirectory()) {
                    deleteFile(path + FILE_SEPARATOR + fileList[i], fileDeleteCallback);
                }
            }
            if (isDeleteDir.length > 0 && isDeleteDir[0]) {
                file.delete();
            }

            if (file.getAbsolutePath() != null && file.getAbsolutePath().equals(path)) {

                if (null != fileDeleteCallback) {
                    fileDeleteCallback.result(1);
                }
            }
        }

        return true;

    }

    public static long getFileSize(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return 0;
        }
        return getFileSize(file);
    }

    public static long getFileSize(File file) {
        if (file.isFile())
            return file.length();
        File[] children = file.listFiles();
        long total = 0;
        if (children != null)
            for (File child : children)
                total += getFileSize(child);
        return total;
    }

    public static void renameFile(String srcPath, String dstpath) {
        File srcFile = new File(srcPath);
        File dstFile = new File(dstpath);
        if (srcFile.exists()) {
            srcFile.renameTo(dstFile);
        }
    }

    public static String savePic(Context c, String fileName, Bitmap bitmap) {
        String filePath = ConFig.IMG_CACHE_PATH + File.separator + fileName;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();
        FileOutputStream fos = null;
        try {
            makeFile(filePath);
            File file = new File(filePath);
            fos = new FileOutputStream(file);
            fos.write(bytes);

        } catch (Exception e) {
            filePath = "";
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    filePath = "";
                }
            }
        }
        return filePath;
    }

    public interface FileDeleteCallback {
        /**
         * @param state(1,成功;2,没有可清除)
         */
        public void result(int state);
    }

    /**
     * 搜索文件列表
     *
     * @param keyword
     * @param list
     * @param path
     */
    public static final void searchFileList(String keyword, List list, String path) {
        File file = new File(path);
        File[] files = file.listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                searchFileList(keyword, list, f.getPath());
            } else if (f.getName().indexOf(keyword) >= 0) {
                list.add(f);
            }
        }
    }

    /**
     * 根据路径获取路径下可打印文件列表
     *
     * @param path
     * @return
     */
    public static final List getFileList(String path) {
        List<File> fileList = new ArrayList<File>();
        try {
            File qqFile = new File(path);
            if (qqFile == null) {
                return null;
            }
            File[] files = qqFile.listFiles();
            if (files != null) {
                int fileLength = files.length;
                for (int i = 0; i < fileLength; i++) {
                    File file = files[i];
                    if (file.isFile() && FileType.isPrintType(file.getPath())) {
                        fileList.add(file);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return fileList;
    }

    public static final String getBase64(File file) {
        FileInputStream inputFile = null;
        String base = null;
        try {
            inputFile = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            inputFile.read(buffer);
            inputFile.close();
            base = Base64.encodeToString(buffer, Base64.NO_WRAP);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return base;
    }
}
