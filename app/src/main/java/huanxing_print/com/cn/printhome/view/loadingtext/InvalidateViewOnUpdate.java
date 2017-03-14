package huanxing_print.com.cn.printhome.view.loadingtext;

import android.view.View;
import java.lang.ref.WeakReference;

import com.nineoldandroids.animation.ValueAnimator;

public class InvalidateViewOnUpdate implements ValueAnimator.AnimatorUpdateListener {
    private final WeakReference<View> viewRef;

    public InvalidateViewOnUpdate(View view) {
        this.viewRef = new WeakReference<>(view);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        final View view = viewRef.get();

        if (view == null) {
            return;
        }

        view.invalidate();
    }
}
