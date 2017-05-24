package huanxing_print.com.cn.printhome.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.util.CircleTransform;
import huanxing_print.com.cn.printhome.util.FailureRedEnvelopesListener;

/**
 * 红包Dialog(红包已抢完)
 * Created by dd on 2017/5/10.
 */

public class GoneRedEnvelopesDialog extends Dialog implements View.OnClickListener {

    private Context context;
    private String imgUrl;//图片地址
    private String redPackageSender;//名字
    private ImageView img_head_portrait;
    private TextView txt_name;
    private TextView txt_leave_msg;
    private TextView txt_look_detail;
    private RelativeLayout rel_close;
    private FailureRedEnvelopesListener listener;

    public GoneRedEnvelopesDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public GoneRedEnvelopesDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    protected GoneRedEnvelopesDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }

    public void setClickListener(FailureRedEnvelopesListener listener) {
        this.listener = listener;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getRedPackageSender() {
        return redPackageSender;
    }

    public void setRedPackageSender(String redPackageSender) {
        this.redPackageSender = redPackageSender;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_red_package_dialog_gone);
        init();
    }

    private void init() {
        img_head_portrait = (ImageView) findViewById(R.id.img_head_portrait);
        txt_name = (TextView) findViewById(R.id.txt_name);
        txt_leave_msg = (TextView) findViewById(R.id.txt_leave_msg);
        txt_look_detail = (TextView) findViewById(R.id.txt_look_detail);
        rel_close = (RelativeLayout) findViewById(R.id.rel_close);

        setImg(imgUrl, img_head_portrait);

        txt_name.setText(redPackageSender);

        //查看详情的点击事件
        txt_look_detail.setOnClickListener(this);
        //关闭红包
        rel_close.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_look_detail:
                listener.checkDetail();
                break;
            case R.id.rel_close:
                listener.closeDialog();
                break;
        }
    }

    public void setImg(String imgUrl, final ImageView imageView) {
        Glide.with(context)
                .load(imgUrl)
                .centerCrop()
                .transform(new CircleTransform(context))
                .crossFade()
                .placeholder(R.drawable.iv_head)
                .error(R.drawable.iv_head)
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        imageView.setImageDrawable(resource);
                    }
                });
    }
}
