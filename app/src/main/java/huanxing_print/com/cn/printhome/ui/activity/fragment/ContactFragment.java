package huanxing_print.com.cn.printhome.ui.activity.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import de.hdodenhof.circleimageview.CircleImageView;
import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseFragment;
import huanxing_print.com.cn.printhome.constant.ConFig;
import huanxing_print.com.cn.printhome.model.my.MyInfoBean;
import huanxing_print.com.cn.printhome.net.callback.my.MyInfoCallBack;
import huanxing_print.com.cn.printhome.net.request.my.MyInfoRequest;
import huanxing_print.com.cn.printhome.ui.activity.my.AccountActivity;
import huanxing_print.com.cn.printhome.ui.activity.my.ContactActivity;
import huanxing_print.com.cn.printhome.ui.activity.my.DaYinActivity;
import huanxing_print.com.cn.printhome.ui.activity.my.MingXiActivity;
import huanxing_print.com.cn.printhome.ui.activity.my.MyActivity;
import huanxing_print.com.cn.printhome.ui.activity.my.NoticeActivity;
import huanxing_print.com.cn.printhome.ui.activity.my.SettingActivity;
import huanxing_print.com.cn.printhome.ui.activity.my.ShareActivity;
import huanxing_print.com.cn.printhome.util.BitmapUtils;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.util.SharedPreferencesUtils;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;

import static huanxing_print.com.cn.printhome.R.id.iv_notice;

public class ContactFragment extends BaseFragment implements OnClickListener {

    private CircleImageView iv_head;
    private TextView tv_phone, tv_name, tv_account_money, tv_month_money;
    private String token;
    private String headUrl;
    private String nickName;
    private String phone;
    private String totleBalance;
    private String monthConsume;
    private String wechatId;

    @Override
    protected void init() {
        EventBus.getDefault().register(this);

        initViews();
        initData();
        setListener();
    }

    private Bitmap bitMap;

    @Override
    public void onResume() {
        super.onResume();
        if (bitMap != null) {
            iv_head.setImageBitmap(bitMap);
        }
        if (name != null) {
            tv_name.setText(name);
        }
    }

    @Subscriber(tag = "head")
    private void getHead(Bitmap bitMap) {
        // 这里实现你的逻辑即可
        this.bitMap = bitMap;
    }

    private String name;

    @Subscriber(tag = "name")
    private void getname(String name) {
        this.name = name;
    }

    @Override
    protected int getContextView() {
        // 改变状态栏的颜色使其与APP风格一体化
        CommonUtils.initSystemBarGreen(getActivity());
        return R.layout.frag_usercenter;
    }

    private void initData() {
        token = SharedPreferencesUtils.getShareString(getActivity(), ConFig.SHAREDPREFERENCES_NAME,
                "loginToken");
        DialogUtils.showProgressDialog(getActivity(),"加载中");
        //网络请求，获取用户信息
        MyInfoRequest.getMyInfo(getActivity(), token, new MyMyInfoCallBack());
    }

    public class MyMyInfoCallBack extends MyInfoCallBack {

        @Override
        public void success(String msg, MyInfoBean bean) {
            DialogUtils.closeProgressDialog();

            if (!ObjectUtils.isNull(bean)) {
                headUrl = bean.getFaceUrl();
                nickName = bean.getNickName();
                phone = bean.getMobileNumber();
                totleBalance = bean.getTotleBalance();
                monthConsume = bean.getMonthConsume();
                wechatId = bean.getWechatId();

                tv_name.setText(nickName);
                tv_phone.setText(phone);
                tv_account_money.setText("￥" + totleBalance);
                tv_month_money.setText("本月打印消费￥" + monthConsume);
                //设置用户头像
                BitmapUtils.displayImage(getActivity(), headUrl, R.drawable.iv_head, iv_head);
            }
        }

        @Override
        public void fail(String msg) {

        }

        @Override
        public void connectFail() {

        }
    }

    private void initViews() {
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_account_money = (TextView) findViewById(R.id.tv_account_money);
        tv_month_money = (TextView) findViewById(R.id.tv_month_money);
        iv_head = (CircleImageView) findViewById(R.id.iv_head);

    }

    private void setListener() {

        findViewById(R.id.ll_my_account).setOnClickListener(this);
        findViewById(R.id.ll_my_contact).setOnClickListener(this);
        findViewById(R.id.ll_my_dy).setOnClickListener(this);
        findViewById(R.id.ll_my_mx).setOnClickListener(this);
        findViewById(R.id.ll_my_set).setOnClickListener(this);
        findViewById(R.id.ll_my_share).setOnClickListener(this);
        findViewById(R.id.rl_userMsg).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_userMsg:
                Intent intent = new Intent(getActivity(), MyActivity.class);
                intent.putExtra("headUrl",headUrl);
                intent.putExtra("wechatId",headUrl);
                startActivity(intent);
                break;
            case iv_notice:
                startActivity(new Intent(getActivity(), NoticeActivity.class));
                break;
            case R.id.ll_my_account:
                startActivity(new Intent(getActivity(), AccountActivity.class));
                break;
            case R.id.ll_my_mx:
                startActivity(new Intent(getActivity(), MingXiActivity.class));
                break;
            case R.id.ll_my_dy:
                startActivity(new Intent(getActivity(), DaYinActivity.class));
                break;
            case R.id.ll_my_contact:
                startActivity(new Intent(getActivity(), ContactActivity.class));
                break;
            case R.id.ll_my_share:
                startActivity(new Intent(getActivity(), ShareActivity.class));
                break;
            case R.id.ll_my_set:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;

            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
