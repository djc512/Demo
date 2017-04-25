package huanxing_print.com.cn.printhome.ui.activity.main;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.ActivityHelper;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.base.BaseFragment;
import huanxing_print.com.cn.printhome.ui.activity.fragment.CenterFragment;
import huanxing_print.com.cn.printhome.ui.activity.fragment.ContactFragment;
import huanxing_print.com.cn.printhome.ui.activity.fragment.FindFragment;
import huanxing_print.com.cn.printhome.ui.activity.fragment.HomeFragment;

/**
 * 主界面
 * 
 * @author Administrator
 * 
 */
public class MainActivity extends BaseActivity implements OnCheckedChangeListener
{
    private Context mContext;
    private RadioGroup rgp;
    
    private HomeFragment fragHome;
    
    private ContactFragment fragContact;
    
    private FindFragment fragFind;
    
    private CenterFragment fragCenter;
    
    private BaseFragment fragTemp;
    
    private FragmentManager fragMananger;
    

    private NotificationManager manager;
    
//    private VersionUpdateDialog dlg;
    
    private long exitTime;
    
    private String url;
    
	@Override
	protected BaseActivity getSelfActivity() {
		return this;
	}
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = MainActivity.this;

        init();
    }
    
    private void init()
    {
        manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        rgp = (RadioGroup)findViewById(R.id.rgp_option);
        fragMananger = getFragmentManager();
        fragHome = new HomeFragment();
        fragContact = new ContactFragment();
        fragFind = new FindFragment();
        fragCenter = new CenterFragment();
        rgp.setOnCheckedChangeListener(this);
        onCheckedChanged(rgp, R.id.rgb_home);

    }
    
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId)
    {
        FragmentTransaction tran = fragMananger.beginTransaction();
        if(fragTemp != null && !fragTemp.isHidden()) {
            tran.hide(fragTemp);
        }
        switch (checkedId)
        {
            case R.id.rgb_home:
                fragTemp = fragHome;
                break;
            case R.id.rgb_contacts:
                fragTemp = fragContact;
                break;
            case R.id.rgb_find:
                fragTemp = fragFind;
                break;
            case R.id.rgb_center:
                fragTemp = fragCenter;
                break;
        }
        if(fragTemp.isAdded()) {
            tran.show(fragTemp);
        } else {
            tran.add(R.id.fl_main_context, fragTemp);
        }
        tran.commitAllowingStateLoss();
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2)
    {
        fragHome.onActivityResult(arg0, arg1, arg2);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                ActivityHelper.getInstance().finishAllActivity();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }


	
}
