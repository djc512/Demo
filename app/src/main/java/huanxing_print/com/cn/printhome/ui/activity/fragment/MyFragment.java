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
import huanxing_print.com.cn.printhome.constant.HttpUrl;
import huanxing_print.com.cn.printhome.model.my.MyInfoBean;
import huanxing_print.com.cn.printhome.net.callback.my.MyInfoCallBack;
import huanxing_print.com.cn.printhome.net.request.my.MyInfoRequest;
import huanxing_print.com.cn.printhome.ui.activity.copy.CommentActivity;
import huanxing_print.com.cn.printhome.ui.activity.copy.CommentListActivity;
import huanxing_print.com.cn.printhome.ui.activity.my.AccountActivity;
import huanxing_print.com.cn.printhome.ui.activity.my.MyActivity;
import huanxing_print.com.cn.printhome.ui.activity.my.MyContactActivity;
import huanxing_print.com.cn.printhome.ui.activity.my.WebViewCommunityActivity;
import huanxing_print.com.cn.printhome.util.BitmapUtils;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.util.SharedPreferencesUtils;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;

public class MyFragment extends BaseFragment implements OnClickListener {

    private CircleImageView iv_head;
    private TextView tv_uniqueid, tv_name, tv_print_count, tv_totle_balance;
    private String token;
    private String headUrl;
    private String nickName;
    private String phone;
    private String totleBalance;
    private String printCount;
    private String wechatId;

    @Override
    protected void init() {
        EventBus.getDefault().register(this);

        initViews();
        // initData();
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
        return R.layout.frag_usercenter;

    }

    private void initData() {
        token = SharedPreferencesUtils.getShareString(getActivity(), ConFig.SHAREDPREFERENCES_NAME,
                "loginToken");
        DialogUtils.showProgressDialog(getActivity(), "加载中");
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
                // monthConsume = bean.getMonthConsume();
                wechatId = bean.getWechatId();

                tv_name.setText(nickName);
                tv_uniqueid.setText(phone);
                tv_print_count.setText(printCount);
                tv_totle_balance.setText(totleBalance);
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
        tv_uniqueid = (TextView) findViewById(R.id.tv_uniqueid);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_print_count = (TextView) findViewById(R.id.tv_print_count);
        tv_totle_balance = (TextView) findViewById(R.id.tv_totle_balance);
        iv_head = (CircleImageView) findViewById(R.id.iv_head);

    }

    private void setListener() {

        findViewById(R.id.ll_my_account).setOnClickListener(this);
        findViewById(R.id.ll_my_contact).setOnClickListener(this);
//        findViewById(R.id.ll_my_dy).setOnClickListener(this);
//        findViewById(R.id.ll_my_mx).setOnClickListener(this);
//        findViewById(R.id.ll_my_set).setOnClickListener(this);
//        findViewById(R.id.ll_my_share).setOnClickListener(this);
//        findViewById(R.id.rl_userMsg).setOnClickListener(this);
        findViewById(R.id.ll_station).setOnClickListener(this);
        findViewById(R.id.ll_join).setOnClickListener(this);
        findViewById(R.id.ll_my_community).setOnClickListener(this);
        findViewById(R.id.iv_set).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.rl_userMsg:
//                Intent intent = new Intent(getActivity(), MyActivity.class);
//                intent.putExtra("nickName",nickName);
//                intent.putExtra("wechatId",wechatId);
//                startActivity(intent);
//                break;
            case R.id.iv_set:
                startActivity(new Intent(getActivity(), MyActivity.class));
                break;
            case R.id.ll_my_account:
                Intent accIntent = new Intent(getActivity(), AccountActivity.class);
                accIntent.putExtra("totleBalance",totleBalance);
                startActivity(accIntent);
                break;
            case R.id.ll_station://布点建议
                //startActivity(new Intent(getActivity(), HandWriteActivity.class));
                startActivity(new Intent(getActivity(), CommentListActivity.class));
                break;
            case R.id.ll_join://打印点加盟
                startActivity(new Intent(getActivity(), CommentActivity.class));
                break;
            case R.id.ll_my_contact:
                startActivity(new Intent(getActivity(), MyContactActivity.class));
                break;
            case R.id.ll_my_community:
                //startActivity(new Intent(getActivity(), CommunityListActivity.class));
                Intent communityIntent=new Intent(getActivity(), WebViewCommunityActivity.class);
                communityIntent.putExtra("webUrl", HttpUrl.community);
                startActivity(communityIntent);
                break;
//            case R.id.ll_my_set:
////                startActivity(new Intent(getActivity(), SettingActivity.class));
////                startActivity(new Intent(getActivity(), ApprovalActivity.class));
//                startActivity(new Intent(getActivity(), CommentActivity.class));
//                break;


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
