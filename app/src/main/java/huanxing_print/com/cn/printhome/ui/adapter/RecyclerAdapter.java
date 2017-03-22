package huanxing_print.com.cn.printhome.ui.adapter;

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

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<File> fileList;

    public RecyclerAdapter(List<File> fileList) {
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

        public TextView textView;
        public RelativeLayout rlyt;
        public ImageView icImg;


        public ViewHolder(View itemView) {
            super(itemView);
            rlyt = (RelativeLayout) itemView.findViewById(R.id.rlayt);
            textView = (TextView) itemView.findViewById(R.id.nameTv);
            icImg = (ImageView) itemView.findViewById(R.id.icImg);
            rlyt.setOnClickListener(this);
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
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_filelist, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.textView.setText(fileList.get(i).getName());
//        if (FileType.TYPE_IMG == FileType.getPrintType(fileList.get(i).getPath())) {
//            Uri uri = Uri.fromFile(new File(fileList.get(i).getPath()));
//            viewHolder.icImg.setImageURI(uri);
//        } else {
        viewHolder.icImg.setImageResource(R.drawable.ic_word);
//        }

    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }
}
