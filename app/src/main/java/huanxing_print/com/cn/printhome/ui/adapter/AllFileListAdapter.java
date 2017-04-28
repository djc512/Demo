package huanxing_print.com.cn.printhome.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.util.FileType;

/**
 * Created by LGH on 16/11/19.
 */
public class AllFileListAdapter extends BaseAdapter {

    public static final String FILE_NAME = "FILE_NAME";
    public static final String FILE_SIZE = "FILE_SIZE";
    public static final String FILE_UPDATE_TIME = "FILE_UPDATE_TIME";
    public static final String FILE_TYPE = "FILE_TYPE";
    public static final String FILE_TYPE_DIR = "dir";
    public static final String FILE_TYPE_FILE = "file";
    public static final String FILE_OBJ = "FILE_OBJ";
    public static final String APP_IMG = "APP_IMG";

    private LayoutInflater mInflater;
    private List<HashMap<String, Object>> mData;
    private boolean showHiddenFiles = false;
    private List<Integer> notHiddenFileIndexList; //非隐藏文件列表

    public AllFileListAdapter(Context context) {
        mData = new ArrayList<>();
        mInflater = LayoutInflater.from(context);
        notHiddenFileIndexList = new ArrayList<>();
    }

    public void setShowHiddenFiles(boolean enable) {
        this.showHiddenFiles = enable;
    }

    public void addData(HashMap<String, Object> singleData) {
        mData.add(singleData);
        String fileName = (String) singleData.get(FILE_NAME);
        if (!fileName.startsWith(".")) {
            notHiddenFileIndexList.add(mData.size() - 1);
        }
    }

    @Override
    public int getCount() {
        if (showHiddenFiles) {
            return mData.size();
        } else {
            return notHiddenFileIndexList.size();
        }
    }

    @Override
    public HashMap<String, Object> getItem(int position) {
        if (showHiddenFiles) {
            return mData.get(position);
        } else {
            return mData.get(notHiddenFileIndexList.get(position));
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = mInflater.inflate(R.layout.list_file_item, parent, false);
            holder = new ViewHolder(convertView);
            holder.fileName = (TextView) convertView.findViewById(R.id.file_name);
            holder.fileSize = (TextView) convertView.findViewById(R.id.file_size);
            holder.fileUpdateTime = (TextView) convertView.findViewById(R.id.file_update_time);
            holder.fileImg = (ImageView) convertView.findViewById(R.id.file_img);
            convertView.setTag(holder);
        }
        HashMap<String, Object> data = getItem(position);

        String fileName = (String) data.get(FILE_NAME);
        holder.fileName.setText(fileName);
        holder.fileSize.setText((CharSequence) data.get(FILE_SIZE));
        holder.fileUpdateTime.setText((CharSequence) data.get(FILE_UPDATE_TIME));
        File file = (File) data.get(FILE_OBJ);
        int fileImgId = getFileImgId(file);
        holder.fileImg.setImageResource(fileImgId);
        return convertView;
    }

    private int getFileImgId(File file) {
        if (file.isDirectory()) {
            return R.drawable.ic_folder;
        }
        if (FileType.getPrintType(file.getPath()) == FileType.TYPE_DOC || FileType.getPrintType(file.getPath()) ==
                FileType.TYPE_DOCX) {
            return R.drawable.ic_word;
        }
        if (FileType.getPrintType(file.getPath()) == FileType.TYPE_PDF) {
            return R.drawable.ic_pdf;
        }
        return R.drawable.ic_defaut_file;
    }

    static class ViewHolder {
        TextView fileName;
        TextView fileSize;
        TextView fileUpdateTime;
        ImageView fileImg;

        public ViewHolder(View view) {
        }
    }

    public interface BackHandledInterface {
        boolean onBackPressed();
    }

}