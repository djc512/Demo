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
import huanxing_print.com.cn.printhome.util.NormalRedEnvelopesListener;

/**
 * 单个红包Dialog(单聊有效)
 * Created by dd on 2017/5/10.
 */

public class SingleRedEnvelopesDialog extends Dialog implements View.OnClickListener {

    private Context context;
    private String imgUrl;//图片地址
    private String redPackageSender;//名字
    private String leaveMsg;//留言
    private ImageView img_head_portrait;
    private ImageView img_open;
    private TextView txt_name;
    private TextView txt_default_msg;
    private TextView txt_leave_msg;
    private TextView txt_look_detail;
    private RelativeLayout rel_close;
    private NormalRedEnvelopesListener listener;

    public SingleRedEnvelopesDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public SingleRedEnvelopesDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    protected SingleRedEnvelopesDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }

    public void setClickListener(NormalRedEnvelopesListener listener) {
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

    public String getLeaveMsg() {
        return leaveMsg;
    }

    public void setLeaveMsg(String leaveMsg) {
        this.leaveMsg = leaveMsg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_red_package_dialog);
        init();
    }

    private void init() {
        img_head_portrait = (ImageView) findViewById(R.id.img_head_portrait);
        img_open = (ImageView) findViewById(R.id.img_open);
        txt_name = (TextView) findViewById(R.id.txt_name);
        txt_default_msg = (TextView) findViewById(R.id.txt_default_msg);
        txt_leave_msg = (TextView) findViewById(R.id.txt_leave_msg);
        txt_look_detail = (TextView) findViewById(R.id.txt_look_detail);
        rel_close = (RelativeLayout) findViewById(R.id.rel_close);

        setImg(imgUrl, img_head_portrait);
        txt_name.setText(redPackageSender);
        txt_leave_msg.setText(leaveMsg);

        //拆开的点击事件
        img_open.setOnClickListener(this);
        //查看详情的点击事件
        txt_look_detail.setOnClickListener(this);
        //关闭红包
        rel_close.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_open:
                listener.open();
                break;
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
