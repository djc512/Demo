package huanxing_print.com.cn.printhome.ui.activity.my;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;

import java.util.ArrayList;
import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.base.BaseApplication;
import huanxing_print.com.cn.printhome.log.Logger;
import huanxing_print.com.cn.printhome.model.address.CityModel;
import huanxing_print.com.cn.printhome.model.address.DistrictModel;
import huanxing_print.com.cn.printhome.model.address.ProvinceModel;
import huanxing_print.com.cn.printhome.net.callback.my.CompanyAddressListCallback;
import huanxing_print.com.cn.printhome.net.callback.my.DebitNormalCallBack;
import huanxing_print.com.cn.printhome.net.request.my.CompanyAddressListRequest;
import huanxing_print.com.cn.printhome.net.request.my.DebitNormalRequest;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;

/**
 * Created by Administrator on 2017/5/8 0008.
 */

public class ReceiptNewActivity extends BaseActivity implements View.OnClickListener {

    private ImageView iv_back;
    private EditText et_companyName;
    private TextView tv_money;
    private EditText et_receiver;
    private EditText et_phone;
    private TextView tv_location;
    private TextView et_detail_location;
    private OptionsPickerView<String> mOpv;
    private Context ctx;
    private List<ProvinceModel> provinceList = new ArrayList<ProvinceModel>();
    private List<CityModel> cityList = new ArrayList<CityModel>();
    private List<DistrictModel> areaList = new ArrayList<DistrictModel>();
    // 省数据集合
    private ArrayList<String> mListProvince = new ArrayList<String>();
    // 市数据集合
    private ArrayList<ArrayList<String>> mListCiry = new ArrayList<ArrayList<String>>();
    // 区数据集合
    private ArrayList<ArrayList<ArrayList<String>>> mListArea = new ArrayList<ArrayList<ArrayList<String>>>();
    private String comAreaAddress, comAreaAddressId, comDetailAddress;
    private String billValue;
    private String companyName;
    private String receiver;
    private String phone;
    private TextView tv_confirm;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_debit_value2);
        ctx = this;
        initView();
        initData();
        initListener();
    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        et_companyName = (EditText) findViewById(R.id.et_companyName);
        tv_money = (TextView) findViewById(R.id.tv_money);
        et_receiver = (EditText) findViewById(R.id.et_receiver);
        et_phone = (EditText) findViewById(R.id.et_phone);
        tv_location = (TextView) findViewById(R.id.tv_location);
        et_detail_location = (TextView) findViewById(R.id.et_detail_location);
        tv_confirm = (TextView) findViewById(R.id.tv_confirm);
    }

    private void initData() {
        billValue = getIntent().getStringExtra("billValue");
        tv_money.setText(billValue);
        CompanyAddressListRequest.verify(getSelfActivity(), BaseApplication.getInstance().getLoginToken(), companyAddressListCallback);
    }

    private void initListener() {
        iv_back.setOnClickListener(this);
        tv_location.setOnClickListener(this);
        tv_confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finishCurrentActivity();
                break;
            case R.id.tv_location:
                showLocation();
                break;
            case R.id.tv_confirm:
                submit();
                go2Receipt();
                break;
        }
    }

    /**
     * 城市列表
     */
    private void showLocation() {
        mOpv.show();
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

    /**
     * 开发票
     */
    private void go2Receipt() {
        String city = tv_location.getText().toString().trim();
        DialogUtils.showProgressDialog(getSelfActivity(), "数据加载中").show();
        DebitNormalRequest.sendNormalBack(getSelfActivity(),
                "",
                billValue,
                "打印费",
                city,
                companyName,
                "",
                "",
                "",
                receiver,
                phone,
                new DebitNormalCallBack() {
                    @Override
                    public void success(String msg, String bean) {
                        DialogUtils.closeProgressDialog();
                        Toast.makeText(ctx, "开票成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void fail(String msg) {

                    }

                    @Override
                    public void connectFail() {
                        Toast.makeText(ctx, "网络连接失败", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void submit() {
        companyName = et_companyName.getText().toString().trim();
        if (TextUtils.isEmpty(companyName)) {
            Toast.makeText(this, "填写公司名称或个人", Toast.LENGTH_SHORT).show();
            return;
        }

        receiver = et_receiver.getText().toString().trim();
        if (TextUtils.isEmpty(receiver)) {
            Toast.makeText(this, "填写收件人", Toast.LENGTH_SHORT).show();
            return;
        }

        phone = et_phone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "填写联系方式", Toast.LENGTH_SHORT).show();
            return;
        } else if (!CommonUtils.isPhone(phone)) {
            Toast.makeText(ctx, "手机号码不正确", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    //查询接口返回
    private CompanyAddressListCallback companyAddressListCallback = new CompanyAddressListCallback() {

        @Override
        public void success(List<ProvinceModel> bean) {
            DialogUtils.closeProgressDialog();
            provinceList = bean;
            if (null != provinceList && provinceList.size() > 0) {
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


    /**
     * 初始化Json数据，并释放Json对象
     */
    private void initJsonDatas() {
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
        mOpv = new OptionsPickerView<String>(ctx);

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
                comAreaAddressId = provinceList.get(options1).getAreaId() + "," + provinceList.get(options1).getChildren().get(option2).getAreaId() + ","
                        + provinceList.get(options1).getChildren().get(option2).getChildren().get(options3).getAreaId();
                tv_location.setText(comAreaAddress);
                tv_location.setTextColor(getResources().getColor(R.color.black2));
            }
        });
    }
}
