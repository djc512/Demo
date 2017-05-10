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

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.util.CircleTransform;
import huanxing_print.com.cn.printhome.util.GroupRedEnvelopesListener;

/**
 * 单个红包Dialog(单聊有效)
 * Created by dd on 2017/5/10.
 */

public class GroupRedEnvelopesDialog extends Dialog implements View.OnClickListener {

    private Context context;
    private String imgUrl;//图片地址
    private String redPackageSender;//名字
    private String leaveMsg;//留言
    private double moneryNum;//金额
    private ImageView img_head_portrait;
    private TextView txt_name;
    private TextView txt_leave_msg;
    private TextView txt_num;
    private RelativeLayout rel_close;
    private GroupRedEnvelopesListener listener;

    public GroupRedEnvelopesDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public GroupRedEnvelopesDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    protected GroupRedEnvelopesDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }

    public void setClickListener(GroupRedEnvelopesListener listener) {
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

    public double getMoneryNum() {
        return moneryNum;
    }

    public void setMoneryNum(double moneryNum) {
        this.moneryNum = moneryNum;
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
        setContentView(R.layout.layout_red_package_group_dialog);
        init();
    }

    private void init() {
        img_head_portrait = (ImageView) findViewById(R.id.img_head_portrait);
        txt_name = (TextView) findViewById(R.id.txt_name);
        txt_num = (TextView) findViewById(R.id.txt_num);
        txt_leave_msg = (TextView) findViewById(R.id.txt_leave_msg);
        rel_close = (RelativeLayout) findViewById(R.id.rel_close);

        Glide.with(context)
                .load(imgUrl)
                .centerCrop()
                .transform(new CircleTransform(context))
                .crossFade()
                .placeholder(R.drawable.iv_head)
                .error(R.drawable.iv_head)
                .into(img_head_portrait);
        txt_name.setText(redPackageSender);
        txt_leave_msg.setText(leaveMsg);
        txt_num.setText(moneryNum + "");

        //关闭红包
        rel_close.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rel_close:
                listener.closeDialog();
                break;
        }
    }
}
