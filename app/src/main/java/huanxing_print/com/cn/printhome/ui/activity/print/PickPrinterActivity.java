package huanxing_print.com.cn.printhome.ui.activity.print;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.ui.activity.print.fragment.AllFileFragment;
import huanxing_print.com.cn.printhome.ui.activity.print.fragment.PickPrinterFragment;
import huanxing_print.com.cn.printhome.util.StepViewUtil;
import huanxing_print.com.cn.printhome.view.StepLineView;

public class PickPrinterActivity extends BasePrintActivity {

    private TextView titleTv;
    private Button imageBtn;
    private Button qqBtn;
    private Button wechatBtn;
    private Button pcBtn;
    private StepLineView stepView;
    private ViewPager viewpager;
    private TabLayout tabs;
    private AllFileFragment allFileFragment;

    private static final int REQUEST_CODE = 1;
    private static final int REQUEST_IMG = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_printer);
        initStepLine();
        initView();
        initFragment();
    }

    private void initView() {
        titleTv = (TextView) findViewById(R.id.titleTv);
        titleTv.setText("打印");
    }

    private void initStepLine() {
        StepViewUtil.init(context, findViewById(R.id.step), StepLineView.STEP_PICK_PRINTER);
    }

    private void initFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        WindowManager wm = getWindowManager();
        Fragment fragment = new PickPrinterFragment();
        fragmentTransaction.add(R.id.content, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
