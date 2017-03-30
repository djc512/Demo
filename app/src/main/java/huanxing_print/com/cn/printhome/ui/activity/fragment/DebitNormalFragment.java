package huanxing_print.com.cn.printhome.ui.activity.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.constant.ConFig;
import huanxing_print.com.cn.printhome.log.Logger;
import huanxing_print.com.cn.printhome.model.address.CityModel;
import huanxing_print.com.cn.printhome.model.address.DistrictModel;
import huanxing_print.com.cn.printhome.model.address.ProvinceModel;
import huanxing_print.com.cn.printhome.net.callback.my.CompanyAddressListCallback;
import huanxing_print.com.cn.printhome.net.callback.my.DebitNormalCallBack;
import huanxing_print.com.cn.printhome.net.request.my.CompanyAddressListRequest;
import huanxing_print.com.cn.printhome.net.request.my.DebitNormalRequest;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.util.RegexUtils;
import huanxing_print.com.cn.printhome.util.SharedPreferencesUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;

/**
 * Created by Administrator on 2017/3/24 0024.
 */

public class DebitNormalFragment extends Fragment implements View.OnClickListener {
    private LinearLayout ll_address_city;
    private Button btn_normal_submit;
    private EditText et_debit_normal_companyName;
    private TextView tv_bill_billContext;
    private TextView tv_debit_normal_amount;
    private TextView textView,tv_debit_normal_city;
    private EditText et_debit_normal_receiver;
    private EditText et_debit_normal_telPhone;
    private EditText et_debit_normal_address;
    private ImageView iv_normal_wechat;
    private ImageView iv_normal_alipay;

    private String companyName;
    private String receiver;
    private String telPhone;
    private String city;
    private String address;
    private String billContext;
    private String amount;

    private String billValue;
    private List<String> citys;
    private String expAmount;
    private String editStr;
    // 省数据集合
    private ArrayList<String> mListProvince = new ArrayList<String>();
    // 市数据集合
    private ArrayList<ArrayList<String>> mListCiry = new ArrayList<ArrayList<String>>();
    // 区数据集合
    private ArrayList<ArrayList<ArrayList<String>>> mListArea = new ArrayList<ArrayList<ArrayList<String>>>();

    private List<ProvinceModel> provinceList =  new ArrayList<ProvinceModel>();
    private List<CityModel> cityList =  new ArrayList<CityModel>();
    private List<DistrictModel> areaList =  new ArrayList<DistrictModel>();

    private OptionsPickerView<String> mOpv;
    //   private JSONObject mJsonObj;
    private String comAreaAddress,comAreaAddressId,comDetailAddress;
    private String token;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activty_debit_normal, null);
        billValue = getArguments().getString("billValue");
        initView(view);
        setListener();
        initData();
        return view;
    }

    private void initView(View view) {
        ll_address_city= (LinearLayout) view.findViewById(R.id.ll_address_city);
        btn_normal_submit = (Button) view.findViewById(R.id.btn_normal_submit);
        et_debit_normal_companyName = (EditText) view.findViewById(R.id.et_debit_normal_companyName);
        tv_bill_billContext = (TextView) view.findViewById(R.id.tv_bill_billContext);
        tv_debit_normal_amount = (TextView) view.findViewById(R.id.tv_debit_normal_amount);
        textView = (TextView) view.findViewById(R.id.textView);
        et_debit_normal_receiver = (EditText) view.findViewById(R.id.et_debit_normal_receiver);
        et_debit_normal_telPhone = (EditText) view.findViewById(R.id.et_debit_normal_telPhone);
        tv_debit_normal_city = (TextView) view.findViewById(R.id.tv_debit_normal_city);
        et_debit_normal_address = (EditText) view.findViewById(R.id.et_debit_normal_address);
        iv_normal_wechat = (ImageView) view.findViewById(R.id.iv_normal_wechat);
        iv_normal_alipay = (ImageView) view.findViewById(R.id.iv_normal_alipay);

        tv_debit_normal_amount.setText(billValue);
    }
    private void initData() {
        token = SharedPreferencesUtils.getShareString(getActivity(), ConFig.SHAREDPREFERENCES_NAME,
                "loginToken");
        DialogUtils.showProgressDialog(getActivity(), "数据加载中").show();
        CompanyAddressListRequest.verify(getActivity(),token,companyAddressListCallback);
    }
    private void getData() {
        citys = Arrays.asList("上海", "浙江", "江苏");
        billContext = tv_bill_billContext.getText().toString().trim();

        companyName = et_debit_normal_companyName.getText().toString().trim();
        if (TextUtils.isEmpty(companyName)) {
            Toast.makeText(getContext(), "填写公司名称或者个人", Toast.LENGTH_SHORT).show();
            return;
        }

        receiver = et_debit_normal_receiver.getText().toString().trim();
        if (TextUtils.isEmpty(receiver)) {
            Toast.makeText(getContext(), "填写收件人", Toast.LENGTH_SHORT).show();
            return;
        }

        telPhone = et_debit_normal_telPhone.getText().toString().trim();
        if (TextUtils.isEmpty(telPhone)) {
            Toast.makeText(getContext(), "填写联系方式", Toast.LENGTH_SHORT).show();
            return;
        }
        city = tv_debit_normal_city.getText().toString().trim();
        String citySub = city.substring(0, 1);
        if (TextUtils.isEmpty(citySub)) {
            Toast.makeText(getContext(), "选择地区信息", Toast.LENGTH_SHORT).show();
            return;
        }else if(citys.contains(citySub)){
            expAmount = "6";
        }else {
            expAmount ="15";
        }

        address = et_debit_normal_address.getText().toString().trim();
        if (TextUtils.isEmpty(address)) {
            Toast.makeText(getContext(), "填写详细地址", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private void setListener() {
        iv_normal_wechat.setOnClickListener(this);
        iv_normal_alipay.setOnClickListener(this);
        btn_normal_submit.setOnClickListener(this);

        et_debit_normal_telPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(editStr!=null){
                    //每次editText有变化的时候，则移除上次发出的延迟线程
                    handler.removeCallbacks(phoneRun);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                editStr = s.toString().trim();
                //延迟800ms，如果不再输入字符，则执行该线程的run方法
                handler.postDelayed(phoneRun, 800);
            }
        });
        ll_address_city.setOnClickListener(this);
    }

    private Handler handler = new Handler();

    private Runnable phoneRun = new Runnable() {
        @Override
        public void run() {
            if (!ObjectUtils.isNull(editStr) && !RegexUtils.isMobileSimple(editStr)) {
                ToastUtil.doToast(getActivity(), "手机号码格式不正确");
                return;
            }
        }
    };
    private int payType;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_normal_wechat:
                iv_normal_wechat.setImageResource(R.drawable.check_2x);
                iv_normal_alipay.setImageResource(R.drawable.uncheck_2x);
                payType = 0;
                break;
            case R.id.iv_normal_alipay:
                iv_normal_alipay.setImageResource(R.drawable.check_2x);
                iv_normal_wechat.setImageResource(R.drawable.uncheck_2x);
                payType = 1;
                break;
            case R.id.ll_address_city:
                mOpv.show();
                break;

            case R.id.btn_normal_submit:
                getData();
                senRequest();
                break;
            default:
                break;
        }
    }

//    address	收件地址	string
//    amount	发票金额	string
//    billContext	发票内容	string
//    city	城市	string
//    companyName	发票抬头	string
//    expAmount	邮费	string
//    fileSize	文件份数	string
//    payType	支付方式0-微信 1-支付宝 2-货到付款	number
//    receiver	接收人	string
//    telPhone	联系电话	string

    private void senRequest() {
        DialogUtils.showProgressDialog(getActivity(), "正在提交");
        DebitNormalRequest.sendNormalBack(
                getActivity(),
                address,//详细地址
                billValue,//发票金额
                billContext,//发票内容
                city,//所在城市
                companyName,//发票抬头
                expAmount,//邮费
                null,//文件份数
                payType,//支付方式0-微信 1-支付宝 2-货到付款	number
                receiver,//收件人
                telPhone,//联系方式
                new DebitNormalCallBack() {
                    @Override
                    public void success(String msg, String bean) {

                    }

                    @Override
                    public void fail(String msg) {

                    }

                    @Override
                    public void connectFail() {

                    }
                }
        );
    }

    //查询接口返回
    private CompanyAddressListCallback companyAddressListCallback = new CompanyAddressListCallback() {

        @Override
        public void success(List<ProvinceModel> bean) {
            DialogUtils.closeProgressDialog();
            provinceList = bean;
            if (null!=provinceList&&provinceList.size()>0) {
                initJsonDatas();

            }

        }

        @Override
        public void fail(String msg) {
            DialogUtils.closeProgressDialog();

        }

        @Override
        public void connectFail() {
            Logger.d("connectFail:");
            DialogUtils.closeProgressDialog();
        }

    };

    /** 初始化Json数据，并释放Json对象 */
    private void initJsonDatas(){
        for (int i = 0; i < provinceList.size(); i++) {
            String province = provinceList.get(i).getAreaName();
            ArrayList<String> options2Items_01 = new ArrayList<String>();
            ArrayList<ArrayList<String>> options3Items_01 = new ArrayList<ArrayList<String>>();
            cityList = provinceList.get(i).getChildren();
            for (int j = 0; j < cityList.size(); j++) {
                String city = cityList.get(j).getAreaName();// 获取每个市的Json对象
                options2Items_01.add(city);// 添加市数据

                ArrayList<String> options3Items_01_01 = new ArrayList<String>();
                areaList = cityList.get(j).getChildren();
                for (int k = 0; k < areaList.size(); k++) {
                    options3Items_01_01.add(areaList.get(k).getAreaName());// 添加区数据
                }
                options3Items_01.add(options3Items_01_01);
            }
            mListProvince.add(province);// 添加省数据
            mListCiry.add(options2Items_01);
            mListArea.add(options3Items_01);
        }

        // 创建选项选择器对象
        mOpv = new OptionsPickerView<String>(getActivity());

        // 设置标题
        mOpv.setTitle("选择城市");

        // 设置三级联动效果
        mOpv.setPicker(mListProvince, mListCiry, mListArea, true);

        // 设置是否循环滚动
        mOpv.setCyclic(false, false, false);

        // 设置默认选中的三级项目
        mOpv.setSelectOptions(0, 0, 0);

        // 监听确定选择按钮
        mOpv.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                // 返回的分别是三个级别的选中位置
                comAreaAddress = mListProvince.get(options1) + mListCiry.get(options1).get(option2) + mListArea.get(options1).get(option2).get(options3);
                comAreaAddressId = provinceList.get(options1).getAreaId()+","+provinceList.get(options1).getChildren().get(option2).getAreaId()+","
                        +provinceList.get(options1).getChildren().get(option2).getChildren().get(options3).getAreaId();
                tv_debit_normal_city.setText(comAreaAddress);
            }
        });


    }
}
