package huanxing_print.com.cn.printhome.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.util.FileType;
import huanxing_print.com.cn.printhome.util.ImageUtil;
import huanxing_print.com.cn.printhome.view.RectangleLayout;

import static com.baidu.location.b.k.co;
import static huanxing_print.com.cn.printhome.R.string.confirm_forward_to;
import static huanxing_print.com.cn.printhome.R.string.file;

/**
 * Created by LGH on 2017/5/20.
 */

public class ChatFilesRecylerAdapter extends RecyclerView.Adapter<ChatFilesRecylerAdapter.ViewHolder> {

    private Context context;
    private List<File> fileList;

    public List<File> getFileList() {
        return fileList;
    }

    public void setFileList(List<File> fileList) {
        this.fileList = fileList;
    }

    public ChatFilesRecylerAdapter(Context context, List<File> fileList) {
        this.context = context;
        this.fileList = fileList;
    }

    public OnItemClickListener itemClickListener;

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView photoImg;
        public RectangleLayout rectangleLayout;
        public RelativeLayout fileRyt;
        public TextView titleTv;
        public TextView typeTv;

        public ViewHolder(View itemView) {
            super(itemView);
            photoImg = (ImageView) itemView.findViewById(R.id.photoImg);
            rectangleLayout = (RectangleLayout) itemView.findViewById(R.id.rectangleLayout);
            fileRyt = (RelativeLayout) itemView.findViewById(R.id.fileRyt);
            titleTv = (TextView) itemView.findViewById(R.id.titleTv);
            typeTv = (TextView) itemView.findViewById(R.id.typeTv);
            rectangleLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(v, getLayoutPosition());
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat_filelist, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        File file = fileList.get(i);
        if (FileType.getPrintType(file.getPath()) == FileType.TYPE_IMG) {
            viewHolder.photoImg.setVisibility(View.VISIBLE);
            viewHolder.fileRyt.setVisibility(View.GONE);
            ImageUtil.showImageView(context, fileList.get(i).getPath(), viewHolder.photoImg);
        } else {
            viewHolder.photoImg.setVisibility(View.GONE);
            viewHolder.fileRyt.setVisibility(View.VISIBLE);
            if (FileType.getPrintType(file.getPath()) == FileType.TYPE_DOC || FileType.getPrintType(file.getPath()) ==
                    FileType.TYPE_DOCX) {
                viewHolder.fileRyt.setBackgroundColor(ContextCompat.getColor(context, R.color.file_blue));
                viewHolder.titleTv.setText(file.getName());
                viewHolder.typeTv.setText("WORD");
                return;
            }
            if (FileType.getPrintType(file.getPath()) == FileType.TYPE_PPT || FileType.getPrintType(file.getPath()) ==
                    FileType.TYPE_PPTX) {
                viewHolder.fileRyt.setBackgroundColor(ContextCompat.getColor(context, R.color.file_red));
                viewHolder.titleTv.setText(file.getName());
                viewHolder.typeTv.setText("PPT");
                return;
            }
            if (FileType.getPrintType(file.getPath()) == FileType.TYPE_PDF) {
                viewHolder.fileRyt.setBackgroundColor(ContextCompat.getColor(context, R.color.file_red));
                viewHolder.titleTv.setText(file.getName());
                viewHolder.typeTv.setText("PDF");
                return;
            }
            viewHolder.fileRyt.setBackgroundColor(ContextCompat.getColor(context, R.color.file_blue));
            viewHolder.titleTv.setText(file.getName());
            viewHolder.typeTv.setText("未知类型");
        }
    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }
}