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
import huanxing_print.com.cn.printhome.net.callback.my.DebitValueCallBack;
import huanxing_print.com.cn.printhome.net.request.my.CompanyAddressListRequest;
import huanxing_print.com.cn.printhome.net.request.my.DebitValueRequest;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.util.RegexUtils;
import huanxing_print.com.cn.printhome.util.SharedPreferencesUtils;
import huanxing_print.com.cn.printhome.util.ToastUtil;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;

/**
 * Created by Administrator on 2017/3/24 0024.
 */

public class DebitValueFragment extends Fragment implements View.OnClickListener {


    private EditText et_debit_value_companyName;
    private EditText et_debit_value_ratepayerId;
    private EditText et_debit_value_companyAddress;
    private EditText et_debit_value_companyPhone;
    private EditText et_debit_value_bankName;
    private EditText et_debit_value_bankAccount;
    private TextView tv_bill_billContext;
    private TextView tv_debit_value_amount;
    private TextView textView;
    private EditText et_debit_value_receiver;
    private EditText et_debit_value_telPhone;
    private TextView tv_debit_value_city;
    private EditText et_debit_value_address;
    private ImageView iv_value_wechat;
    private ImageView iv_value_alipay;
    private Button btn_value_submit;
    private LinearLayout ll_value_city;

    private String companyName;
    private String ratepayerId;
    private String companyAddress;
    private String companyPhone;
    private String bankName;
    private String bankAccount;
    private String receiver;
    private String telPhone;
    private String city;
    private String address;
    private String billContext;
    private String amount;
    private String billValue;
    private List<String> citys;
    private String expAmount;
    private String editString;
    private String companyNum;

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

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activty_debit_value, null);
        billValue = getArguments().getString("billValue");
        initView(view);
        setListener();
        initData();
        return view;
    }

    private void initData() {
        token = SharedPreferencesUtils.getShareString(getActivity(), ConFig.SHAREDPREFERENCES_NAME,
                "loginToken");
        DialogUtils.showProgressDialog(getActivity(), "数据加载中").show();
        CompanyAddressListRequest.verify(getActivity(),token,companyAddressListCallback);
    }

    private void setListener() {
        iv_value_wechat.setOnClickListener(this);
        iv_value_alipay.setOnClickListener(this);
        btn_value_submit.setOnClickListener(this);
        ll_value_city.setOnClickListener(this
        );

        et_debit_value_companyPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (companyRun != null) {
                    handler.removeCallbacks(companyRun);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                companyNum = s.toString().trim();
                handler.postDelayed(companyRun,800);
            }
        });

        et_debit_value_telPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(phoneRun!=null){
                    //每次editText有变化的时候，则移除上次发出的延迟线程
                    handler.removeCallbacks(phoneRun);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                editString = s.toString().trim();
                //延迟800ms，如果不再输入字符，则执行该线程的run方法
                handler.postDelayed(phoneRun, 800);
            }
        });
    }
    private Handler handler = new Handler();

    private Runnable phoneRun = new Runnable() {
        @Override
        public void run() {
            if (!ObjectUtils.isNull(editString)&&!RegexUtils.isMobileSimple(editString)){
                ToastUtil.doToast(getActivity(),"手机号码格式不正确");
                return;
            }
        }
    };
    private Runnable companyRun = new Runnable() {
        @Override
        public void run() {
            if (!ObjectUtils.isNull(companyNum) && !RegexUtils.isMobileSimple(companyNum)) {
                ToastUtil.doToast(getActivity(),"手机号码格式不正确");
                return;
            }
        }
    };

    private void initView(View view) {
        ll_value_city= (LinearLayout) view.findViewById(R.id.ll_value_city);
        et_debit_value_companyName = (EditText) view.findViewById(R.id.et_debit_value_companyName);
        et_debit_value_ratepayerId = (EditText) view.findViewById(R.id.et_debit_value_ratepayerId);
        et_debit_value_companyAddress = (EditText) view.findViewById(R.id.et_debit_value_companyAddress);
        et_debit_value_companyPhone = (EditText) view.findViewById(R.id.et_debit_value_companyPhone);
        et_debit_value_bankName = (EditText) view.findViewById(R.id.et_debit_value_bankName);
        et_debit_value_bankAccount = (EditText) view.findViewById(R.id.et_debit_value_bankAccount);
        tv_bill_billContext = (TextView) view.findViewById(R.id.tv_value_billContext);
        tv_debit_value_amount = (TextView) view.findViewById(R.id.tv_debit_value_amount);
        textView = (TextView) view.findViewById(R.id.textView);
        et_debit_value_receiver = (EditText) view.findViewById(R.id.et_debit_value_receiver);
        et_debit_value_telPhone = (EditText) view.findViewById(R.id.et_debit_value_telPhone);
        tv_debit_value_city = (TextView) view.findViewById(R.id.tv_debit_value_city);
        et_debit_value_address = (EditText) view.findViewById(R.id.et_debit_value_address);
        iv_value_wechat = (ImageView) view.findViewById(R.id.iv_value_wechat);
        iv_value_alipay = (ImageView) view.findViewById(R.id.iv_value_alipay);
        btn_value_submit = (Button) view.findViewById(R.id.btn_value_submit);

        tv_debit_value_amount.setText(billValue);
    }
    private int payType;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_value_alipay:
                iv_value_alipay.setBackgroundResource(R.drawable.check_2x);
                iv_value_wechat.setBackgroundResource(R.drawable.uncheck_2x);
                payType = 1;
                break;
            case R.id.iv_value_wechat:
                iv_value_wechat.setBackgroundResource(R.drawable.check_2x);
                iv_value_alipay.setBackgroundResource(R.drawable.uncheck_2x);
                payType = 0;
                break;
            case R.id.btn_value_submit:
                getData();
                sendValue();
                break;
            case R.id.ll_value_city:
                mOpv.show();
                break;
        }
    }

//    address	收件人地址	string
//    amount	发票金额	string
//    bankAccount	开户行账号	string
//    bankName	开户行	string
//    billContext	发票内容0-打印费	number
//    city	城市	string
//    companyAddress	发票公司地址	string
//    companyName	发票公司名称	string
//    companyPhone	发票公司电话	string
//    expAmount	邮费	string
//    fileSize	文件份数	string
//    payType	支付方式0-微信 1-支付宝 2-货到付款	number
//    ratepayerId	纳税人识别码	string
//    receiver	快递接收人	string
//    telPhone	联系人号码	string

    private void sendValue() {
        DebitValueRequest.sendValueBack(
                getActivity(),
                address,//收件人地址
                billValue,//发票金额
                bankAccount,//开户行账号
                bankName,//开户行
                "0",//发票内容0-打印费	number
                city,//城市
                companyAddress,//发票公司地址
                companyName,//发票公司名称
                companyPhone,// 发票公司电话
                expAmount,//邮费
                null,//文件份数
                payType,//支付方式0-微信 1-支付宝 2-货到付款	number
                ratepayerId,//纳税人识别号
                receiver,//接收人
                telPhone,//接受人电话
                new DebitValueCallBack() {
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

    private void getData() {

        billContext = tv_bill_billContext.getText().toString().trim();

        companyName = et_debit_value_companyName.getText().toString().trim();
        if (TextUtils.isEmpty(companyName)) {
            Toast.makeText(getContext(), "填写公司名称", Toast.LENGTH_SHORT).show();
            return;
        }

        ratepayerId = et_debit_value_ratepayerId.getText().toString().trim();
        if (TextUtils.isEmpty(ratepayerId)) {
            Toast.makeText(getContext(), "填写纳税人识别码", Toast.LENGTH_SHORT).show();
            return;
        }

        companyAddress = et_debit_value_companyAddress.getText().toString().trim();
        if (TextUtils.isEmpty(companyAddress)) {
            Toast.makeText(getContext(), "填写收票单位注册地址", Toast.LENGTH_SHORT).show();
            return;
        }

        companyPhone = et_debit_value_companyPhone.getText().toString().trim();
        if (TextUtils.isEmpty(companyPhone)) {
            Toast.makeText(getContext(), "填写公司电话号码", Toast.LENGTH_SHORT).show();
            return;
        }

        bankName = et_debit_value_bankName.getText().toString().trim();
        if (TextUtils.isEmpty(bankName)) {
            Toast.makeText(getContext(), "填写收票单位开户银行", Toast.LENGTH_SHORT).show();
            return;
        }

        bankAccount = et_debit_value_bankAccount.getText().toString().trim();
        if (TextUtils.isEmpty(bankAccount)) {
            Toast.makeText(getContext(), "填写收票单位银行账户", Toast.LENGTH_SHORT).show();
            return;
        }

        receiver = et_debit_value_receiver.getText().toString().trim();
        if (TextUtils.isEmpty(receiver)) {
            Toast.makeText(getContext(), "填写收件人", Toast.LENGTH_SHORT).show();
            return;
        }

        telPhone = et_debit_value_telPhone.getText().toString().trim();
        if (TextUtils.isEmpty(telPhone)) {
            Toast.makeText(getContext(), "填写联系方式", Toast.LENGTH_SHORT).show();
            return;
        }
        citys = Arrays.asList("江苏", "浙江", "上海");
        city = tv_debit_value_city.getText().toString().trim();
        String citySub = city.substring(0, 1);
        if (TextUtils.isEmpty(citySub)) {
            Toast.makeText(getContext(), "填写收票单位注册地址", Toast.LENGTH_SHORT).show();
            return;
        }else if (citys.contains(citySub)){
            expAmount = "6";
        }else {
            expAmount ="15";
        }

        address = et_debit_value_address.getText().toString().trim();
        if (TextUtils.isEmpty(address)) {
            Toast.makeText(getContext(), "填写详细地址", Toast.LENGTH_SHORT).show();
            return;
        }

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
                tv_debit_value_city.setText(comAreaAddress);
            }
        });
    }
}
