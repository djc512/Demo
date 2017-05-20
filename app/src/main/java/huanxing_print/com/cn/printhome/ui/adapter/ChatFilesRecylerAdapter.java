package huanxing_print.com.cn.printhome.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.util.ImageUtil;

/**
 * Created by LGH on 2017/5/20.
 */

public class ChatFilesRecylerAdapter  extends RecyclerView.Adapter<ChatFilesRecylerAdapter.ViewHolder> {

    private Context context;
    private List<String> photoList;

    public ChatFilesRecylerAdapter(Context context, List<String> fileList) {
        this.context = context;
        this.photoList = fileList;
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

        public ViewHolder(View itemView) {
            super(itemView);
            photoImg = (ImageView) itemView.findViewById(R.id.photoImg);
            photoImg.setOnClickListener(this);
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
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_photolist, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        ImageUtil.showImageView(context, photoList.get(i), viewHolder.photoImg);
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }
}