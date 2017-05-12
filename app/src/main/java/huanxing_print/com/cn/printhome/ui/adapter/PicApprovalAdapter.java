package huanxing_print.com.cn.printhome.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.util.picuplload.Bimp;

/**
 * Created by Administrator on 2017/5/5 0005.
 */

public class PicApprovalAdapter extends BaseAdapter {


    private LayoutInflater inflater;
    private List<Bitmap> mResults;
    private Context ctx;

    public PicApprovalAdapter(Context context, List<Bitmap> mResults) {
        ctx = context;
        this.mResults = mResults;
        inflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return mResults.size();
    }

    public Object getItem(int arg0) {
        return mResults.get(arg0);
    }

    public long getItemId(int arg0) {
        return arg0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_gridview, null);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView
                    .findViewById(R.id.imageView1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        /*if (position == Bimp.tempSelectBitmap.size()) {
            holder.image.setImageBitmap(BitmapFactory.decodeResource(
                    ctx.getResources(), R.drawable.add));
            if (position == 9) {
                holder.image.setVisibility(View.GONE);
            }
        } else {*/
            holder.image.setImageBitmap(mResults.get(position));
       // }
        return convertView;
    }

    public static class ViewHolder {
        public ImageView image;
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    notifyDataSetChanged();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public void loading() {
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    if (Bimp.max == Bimp.tempSelectBitmap.size()) {
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                        break;
                    } else {
                        Bimp.max += 1;
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                    }
                }
            }
        }).start();
    }
}
