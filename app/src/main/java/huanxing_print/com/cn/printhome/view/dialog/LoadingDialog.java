package huanxing_print.com.cn.printhome.view.dialog;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.ImageView;

import huanxing_print.com.cn.printhome.R;

/**
 * Created by LGH on 2017/5/9.
 */

public class LoadingDialog extends AlertDialog {

    private ImageView progressImg;
    private AnimationDrawable animation;

    public LoadingDialog(Context context) {
        super(context, R.style.LoadingDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_dialog);
        setCanceledOnTouchOutside(false);
        progressImg = (ImageView) findViewById(R.id.refreshing_drawable_img);
        animation = (AnimationDrawable) progressImg.getDrawable();
    }

    @Override
    protected void onStart() {
        super.onStart();
        animation.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        animation.stop();
    }


}