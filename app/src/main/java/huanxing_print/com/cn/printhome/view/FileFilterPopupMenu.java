package huanxing_print.com.cn.printhome.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.PopupWindow;
import android.widget.TextView;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.util.DisplayUtil;
import huanxing_print.com.cn.printhome.util.file.FileComparator;

public class FileFilterPopupMenu extends PopupWindow implements OnClickListener {

    private Activity activity;
    private View fragmentView;
    private View popView;

    private TextView timeTv;
    private TextView nameTv;
    private TextView typeTv;

    private OnItemClickListener onItemClickListener;

    public FileFilterPopupMenu(Activity activity, View view) {
        super(activity);
        this.activity = activity;
        this.fragmentView = view;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popView = inflater.inflate(R.layout.popup_file_filter, null);
        this.setContentView(popView);
        this.setWidth(DisplayUtil.dip2px(activity, 150));
        this.setHeight(DisplayUtil.dip2px(activity, 150));
        this.setFocusable(true);
        this.setTouchable(true);
        this.setOutsideTouchable(true);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        this.setBackgroundDrawable(dw);
        timeTv = (TextView) popView.findViewById(R.id.timeTv);
        nameTv = (TextView) popView.findViewById(R.id.nameTv);
        typeTv = (TextView) popView.findViewById(R.id.typeTv);
        timeTv.setOnClickListener(this);
        nameTv.setOnClickListener(this);
        typeTv.setOnClickListener(this);
    }

    public void setTextColor(int mode) {
        switch (mode) {
            case FileComparator.MODE_TIME:
                timeTv.setTextColor(ContextCompat.getColor(activity, R.color.theme_yellow));
                nameTv.setTextColor(ContextCompat.getColor(activity, R.color.white));
                typeTv.setTextColor(ContextCompat.getColor(activity, R.color.white));
                break;
            case FileComparator.MODE_NAME:
                nameTv.setTextColor(ContextCompat.getColor(activity, R.color.theme_yellow));
                timeTv.setTextColor(ContextCompat.getColor(activity, R.color.white));
                typeTv.setTextColor(ContextCompat.getColor(activity, R.color.white));
                break;
            case FileComparator.MODE_TYPE:
                typeTv.setTextColor(ContextCompat.getColor(activity, R.color.theme_yellow));
                timeTv.setTextColor(ContextCompat.getColor(activity, R.color.white));
                nameTv.setTextColor(ContextCompat.getColor(activity, R.color.white));
                break;
        }
    }


    public void showLocation(int resourId) {
        showAsDropDown(fragmentView.findViewById(resourId), dip2px(activity, -120), dip2px(activity, 0));
    }

    @Override
    public void onClick(View v) {
        if (onItemClickListener != null) {
            onItemClickListener.onClick(v.getId());
        }
        dismiss();
    }

    public int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public interface OnItemClickListener {
        void onClick(int id);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}
