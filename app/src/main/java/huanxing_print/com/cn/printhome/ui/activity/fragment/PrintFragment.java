package huanxing_print.com.cn.printhome.ui.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseFragment;
import huanxing_print.com.cn.printhome.log.Logger;
import huanxing_print.com.cn.printhome.model.print.IsOnlineResp;
import huanxing_print.com.cn.printhome.model.print.PrintInfoResp;
import huanxing_print.com.cn.printhome.net.request.print.HttpListener;
import huanxing_print.com.cn.printhome.net.request.print.PrintRequest;
import huanxing_print.com.cn.printhome.ui.activity.print.AddFileActivity;
import huanxing_print.com.cn.printhome.util.GsonUtil;
import huanxing_print.com.cn.printhome.util.ShowUtil;
import huanxing_print.com.cn.printhome.util.StepViewUtil;
import huanxing_print.com.cn.printhome.util.UrlUtil;
import huanxing_print.com.cn.printhome.view.StepLineView;

import static huanxing_print.com.cn.printhome.R.id.iv_notice;


public class PrintFragment extends BaseFragment implements OnClickListener {

    @Override
    protected void init() {
        StepViewUtil.init(getActivity(), findViewById(R.id.step), StepLineView.STEP_DEFAULT);
        findViewById(R.id.ll_file).setOnClickListener(this);
        findViewById(R.id.ll_weixin).setOnClickListener(this);
        findViewById(R.id.ll_qq).setOnClickListener(this);
        findViewById(R.id.ll_photo).setOnClickListener(this);
        findViewById(R.id.ll_computer).setOnClickListener(this);
        findViewById(R.id.ll_wifi).setOnClickListener(this);
        findViewById(iv_notice).setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected int getContextView() {
        return R.layout.frag_print;
    }

    public static final int REQUEST_CODE = 1;

    @Override
    public void onClick(View v) {
        int index = 0;
        if (v.getId() == R.id.iv_notice) {
            Intent intent = new Intent(getActivity(), CaptureActivity.class);
            startActivityForResult(intent, REQUEST_CODE);
            return;
        }
        switch (v.getId()) {
            case R.id.ll_file:
                index = 0;
                //startActivity(new Intent(getActivity(), CopyActivity.class));
                break;
            case R.id.ll_weixin:
                index = 1;
                //startActivity(new Intent(getActivity(), AddFileActivity.class));
                break;
            case R.id.ll_qq:
                index = 2;
                //startActivity(new Intent(getActivity(), OperatingInstructionsActivity.class));
                break;
            case R.id.ll_photo:
                index = 3;
                //startActivity(new Intent(getActivity(), CopyActivity.class));
                break;
            case R.id.ll_computer:
                index = 4;
                //startActivity(new Intent(getActivity(), AddFileActivity.class));
                break;
            case R.id.ll_wifi:
                index = 5;
                //startActivity(new Intent(getActivity(), OperatingInstructionsActivity.class));
                break;
            default:
                break;
        }
        Bundle bundle = new Bundle();
        bundle.putInt(AddFileActivity.INDEX, index);
        AddFileActivity.start(getActivity(), bundle);
    }

    public void requeryIsOnline(final String printerNo) {
        PrintRequest.queryIsOnline(getActivity(), printerNo, new HttpListener() {
            @Override
            public void onSucceed(String content) {
                IsOnlineResp isOnlineResp = GsonUtil.GsonToBean(content, IsOnlineResp.class);
                if (isOnlineResp != null) {
                    if (isOnlineResp.isSuccess()) {
                        if (isOnlineResp.getData() == true) {
                            requeryPrice(printerNo);
                        } else {
                            ShowUtil.showToast(isOnlineResp.getErrorMsg());
                        }
                    } else {
                        ShowUtil.showToast(isOnlineResp.getErrorMsg());
                    }
                }
            }

            @Override
            public void onFailed(String exception) {
                ShowUtil.showToast(getString(R.string.net_error));
            }
        });
    }

    public void requeryPrice(String printerNo) {
        PrintRequest.queryPrinterPrice(getActivity(), printerNo, new HttpListener() {
            @Override
            public void onSucceed(String content) {
                PrintInfoResp printInfoResp = GsonUtil.GsonToBean(content, PrintInfoResp.class);
                if (printInfoResp != null && printInfoResp.isSuccess()) {
                    PrintInfoResp.PrinterPrice printPrinterPrice = printInfoResp.getData();
                    if (printPrinterPrice != null) {
                        turnPickFile(printPrinterPrice);
                    }
                }
                if (printInfoResp != null && !printInfoResp.isSuccess()) {
                    ShowUtil.showToast(printInfoResp.getErrorMsg());
                }
            }

            @Override
            public void onFailed(String exception) {
                ShowUtil.showToast(getString(R.string.net_error));
            }
        });
        Logger.i(printerNo);
    }


    private void turnPickFile(PrintInfoResp.PrinterPrice printPrinterPrice) {
        Bundle bundle = new Bundle();
        bundle.putParcelable();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    Logger.i(result);
                    String printNo = UrlUtil.getValueByName(result, "printNo");
                    if (printNo != null) {
                        requeryIsOnline(printNo);
                        return;
                    }
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    ShowUtil.showToast("解析二维码失败");
                }
            }
        }
    }
}
