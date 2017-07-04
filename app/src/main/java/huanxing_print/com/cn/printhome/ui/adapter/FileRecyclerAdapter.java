package huanxing_print.com.cn.printhome.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.util.FileType;
import huanxing_print.com.cn.printhome.util.FileUtils;
import huanxing_print.com.cn.printhome.util.image.ImgIconUtil;

public class FileRecyclerAdapter extends RecyclerView.Adapter<FileRecyclerAdapter.ViewHolder> {

    private List<File> fileList;
    private Context context;

    public FileRecyclerAdapter(List<File> fileList, Context context) {
        this.fileList = fileList;
        this.context = context;
    }

    public void clearData() {
        fileList.clear();
    }

    public void setFileList(List<File> fileList) {
        this.fileList = fileList;
    }

    public List<File> getFileList() {
        return fileList;
    }

    public void clear() {
        fileList.clear();
    }

    public OnItemClickListener itemClickListener;

    public void setItemLongClickListener(OnItemLongClickListener itemLongClickListener) {
        this.itemLongClickListener = itemLongClickListener;
    }

    public OnItemLongClickListener itemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public TextView nameTv;
        public TextView timeTv;
        public TextView sizeTv;
        public LinearLayout lv;
        public ImageView icImg;


        public ViewHolder(View itemView) {
            super(itemView);
            lv = (LinearLayout) itemView.findViewById(R.id.lv);
            nameTv = (TextView) itemView.findViewById(R.id.nameTv);
            timeTv = (TextView) itemView.findViewById(R.id.timeTv);
            sizeTv = (TextView) itemView.findViewById(R.id.sizeTv);
            icImg = (ImageView) itemView.findViewById(R.id.fileImg);
            lv.setOnClickListener(this);
            lv.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(v, getLayoutPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (itemLongClickListener != null) {
                itemLongClickListener.onItemLongClick(v, getLayoutPosition());
            }
            return true;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_filelist, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        File file = fileList.get(i);
        viewHolder.nameTv.setText(file.getName());
        if (!FileType.isPrintType(file.getPath())) {
            viewHolder.nameTv.setTextColor(ContextCompat.getColor(context, R.color.text_gray));
        } else {
            viewHolder.nameTv.setTextColor(ContextCompat.getColor(context, R.color.text_black));
        }
        viewHolder.timeTv.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(file
                .lastModified()));
        if (!file.isDirectory()) {
            viewHolder.sizeTv.setText(FileUtils.prettySize(file.length()));
        }
        viewHolder.icImg.setImageResource(ImgIconUtil.getDrawable(file));
    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }
}
