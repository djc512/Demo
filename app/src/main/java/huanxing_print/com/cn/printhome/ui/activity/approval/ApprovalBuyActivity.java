package huanxing_print.com.cn.printhome.ui.activity.approval;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import huanxing_print.com.cn.printhome.R;


/**
 * Created by Administrator on 2017/5/5 0005.
 */
public class ApprovalBuyActivity extends Activity implements View.OnClickListener{

    public Context context;

    ImageView iv_camera;
    ImageView iv_off;
    ImageView iv_time;
    ImageView iv_back;
    Button bt_commit;
    LinearLayout ll_id;

    boolean isRequestMoney =false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval_buy);
        context = this;
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        iv_back.setOnClickListener(this);
        iv_camera.setOnClickListener(this);
        iv_off.setOnClickListener(this);
        iv_time.setOnClickListener(this);
        bt_commit.setOnClickListener(this);
        ll_id.setOnClickListener(this);
    }

    private void initData() {
    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_camera = (ImageView) findViewById(R.id.iv_camera);
        iv_off = (ImageView) findViewById(R.id.iv_off);
        iv_time = (ImageView) findViewById(R.id.iv_time);
        bt_commit = (Button) findViewById(R.id.bt_commit);
        ll_id = (LinearLayout) findViewById(R.id.ll_id);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                break;
            case R.id.iv_camera:
                break;
            case R.id.iv_off:
                if (isRequestMoney){
                    bankInformationOpen();
                    isRequestMoney = false;
                } else{
                    bankInformationClose();
                    isRequestMoney =true;
                }

                break;
            case R.id.iv_time:
                break;
            case R.id.bt_commit:
                break;
            case R.id.ll_id:

                break;
            default:
                break;
        }

    }




        public void bankInformationOpen(){
            ll_id.setVisibility(View.VISIBLE);
        }
        public void bankInformationClose(){
            ll_id.setVisibility(View.GONE);
        }


}
