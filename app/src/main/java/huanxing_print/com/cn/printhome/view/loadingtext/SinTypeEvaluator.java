package huanxing_print.com.cn.printhome.view.loadingtext;

import com.nineoldandroids.animation.TypeEvaluator;

public class SinTypeEvaluator implements TypeEvaluator<Number> {
    @Override
    public Number evaluate(float fraction, Number from, Number to) {
        return Math.max(0, Math.sin(fraction * Math.PI * 2)) * (to.floatValue() - from.floatValue());
    }
}
