package huanxing_print.com.cn.printhome.util;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LGH on 2017/3/6.
 */

public class FileType {

    Class<?> mMediaFile, mMediaFileType;
    Method getFileTypeMethod, isAudioFileTypeMethod, isVideoFileTypeMethod, isImageFileTypeMethod;
    String methodName = "getBoolean";
    String getFileType = "getFileType";

    String isAudioFileType = "isAudioFileType";
    String isVideoFileType = "isVideoFileType";
    String isImageFileType = "isImageFileType";

    Field fileType;

    public void initReflect() {
        try {
            mMediaFile = Class.forName("android.media.MediaFile");
            mMediaFileType = Class.forName("android.media.MediaFile$MediaFileType");

            fileType = mMediaFileType.getField("fileType");

            getFileTypeMethod = mMediaFile.getMethod(getFileType, String.class);

            isAudioFileTypeMethod = mMediaFile.getMethod(isAudioFileType, int.class);
            isVideoFileTypeMethod = mMediaFile.getMethod(isVideoFileType, int.class);
            isImageFileTypeMethod = mMediaFile.getMethod(isImageFileType, int.class);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

    }

    public int getMediaFileType(String path) {
        int type = 0;
        try {
            Object obj = getFileTypeMethod.invoke(mMediaFile, path);
            if (obj == null) {
                type = -1;
            } else {
                type = fileType.getInt(obj);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return type;
    }

    public boolean isAudioFile(int fileType) {
        boolean isAudioFile = false;
        try {
            isAudioFile = (Boolean) isAudioFileTypeMethod.invoke(mMediaFile, fileType);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return isAudioFile;
    }

    public boolean isVideoFile(int fileType) {
        boolean isVideoFile = false;
        try {
            isVideoFile = (Boolean) isVideoFileTypeMethod.invoke(mMediaFile, fileType);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return isVideoFile;
    }

    public boolean isImageFile(int fileType) {
        boolean isImageFile = false;
        try {
            isImageFile = (Boolean) isImageFileTypeMethod.invoke(mMediaFile, fileType);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return isImageFile;
    }

    public static final int TYPE_DOC = 1;
    public static final int TYPE_DOCX = 2;
    public static final int TYPE_PDF = 3;
    public static final int TYPE_PPT = 4;
    public static final int TYPE_PPTX = 5;

    public static final int getPrintType(String path) {
        FileType fileType = new FileType();
        fileType.initReflect();
        int type = fileType.getMediaFileType(path);
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

    public static final String getType(String path) {
        FileType fileType = new FileType();
        fileType.initReflect();
        int type = fileType.getMediaFileType(path);
        String[] strs = path.split("\\.");
        String extension = strs[strs.length - 1];
        return "."+extension;
    }

    public static final List<File> getFiltFileList(List<File> list, int type) {
        List<File> typeList = new ArrayList<File>();
        if (type == FileType.TYPE_DOC || type == FileType.TYPE_DOCX) {
            for (File file : list) {
                if (FileType.getPrintType(file.getPath()) == FileType.TYPE_DOC || FileType.getPrintType(file.getPath())
                        == FileType.TYPE_DOCX) {
                    typeList.add(file);
                }
            }
        }
        if (type == FileType.TYPE_PDF) {
            for (File file : list) {
                if (FileType.getPrintType(file.getPath()) == FileType.TYPE_PDF) {
                    typeList.add(file);
                }
            }
        }
        if (type == FileType.TYPE_PPT || type == FileType.TYPE_PPTX) {
            for (File file : list) {
                if (FileType.getPrintType(file.getPath()) == FileType.TYPE_PPT || FileType.getPrintType(file.getPath())
                        == FileType.TYPE_PPTX) {
                    typeList.add(file);
                }
            }
        }
        return typeList;
    }
}
