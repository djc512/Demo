package huanxing_print.com.cn.printhome.ui.activity.copy;

import android.content.Context;

/**
 * Description :
 */
public class Application extends android.app.Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this.getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }

}
