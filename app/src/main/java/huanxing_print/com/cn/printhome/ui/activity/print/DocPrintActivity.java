package huanxing_print.com.cn.printhome.ui.activity.print;

import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.model.print.AddOrderRespBean;
import huanxing_print.com.cn.printhome.model.print.PrintSetting;
import huanxing_print.com.cn.printhome.net.request.print.HttpListener;
import huanxing_print.com.cn.printhome.net.request.print.PrintRequest;
import huanxing_print.com.cn.printhome.util.ShowUtil;
import huanxing_print.com.cn.printhome.util.ToastUtil;

public class DocPrintActivity extends BasePrintActivity {

    private PrintSetting printSetting;
    private String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_print);
        initData();
    }

    private void initData() {
        printSetting = getIntent().getExtras().getParcelable(DocPreviewActivity.PRINT_SETTING);
    }

    private void delFile() {
        PrintRequest.delFile(activity, printSetting.getId(), new HttpListener() {
            @Override
            public void onSucceed(String content) {
                finish();
            }

            @Override
            public void onFailed(String exception) {
                ShowUtil.showToast(getString(R.string.net_error));
            }
        });
    }

    public void onDel(View view) {
        delFile();
    }

    public void onModify(View view) {
        PrintRequest.modifySetting(activity, printSetting.getColourFlag(), printSetting.getDirectionFlag(),
                printSetting.getDoubleFlag(), printSetting.getId(), printSetting.getPrintCount(), printSetting
                        .getSizeType(), new
                        HttpListener() {
                            @Override
                            public void onSucceed(String content) {
                                ShowUtil.showToast("修改成功");
                            }

                            @Override
                            public void onFailed(String exception) {
                                ShowUtil.showToast(getString(R.string.net_error));
                            }
                        });
    }

    public void onAddOrder(View view) {
        List fileList = new ArrayList();
        fileList.add(printSetting.getId());
        PrintRequest.addOrder(activity, "48TZ-13102-1251581193", fileList, new
                HttpListener() {
                    @Override
                    public void onSucceed(String content) {
                        AddOrderRespBean addOrderRespBean = new Gson().fromJson(content, AddOrderRespBean.class);
                        if (addOrderRespBean.isSuccess()) {
                            orderId = addOrderRespBean.getData().getOrderId();
                        } else {
                            ShowUtil.showToast(addOrderRespBean.getErrorMsg());
                        }
                    }

                    @Override
                    public void onFailed(String exception) {
                        ShowUtil.showToast(getString(R.string.net_error));
                    }
                });
    }

    public void onPrint(View view) {
        if (orderId == null) {
            return;
        }
        String str = "123";
        int orderId = -1;
//        try {
//            orderId = Integer.parseInt(str);
//        } catch (NumberFormatException e) {
//            e.printStackTrace();
//        }
        if (orderId < 0) {
            ShowUtil.showToast("参数错误");
            return;
        }
        PrintRequest.print(activity, 1, new
                HttpListener() {
                    @Override
                    public void onSucceed(String content) {
                        AddOrderRespBean addOrderRespBean = new Gson().fromJson(content, AddOrderRespBean.class);
                        if (addOrderRespBean.isSuccess()) {
                        } else {
                            ToastUtil.doToast(context, getString(R.string.upload_failure));
                        }
                    }

                    @Override
                    public void onFailed(String exception) {
                        ShowUtil.showToast(getString(R.string.net_error));
                    }
                });
    }

}
