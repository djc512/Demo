package huanxing_print.com.cn.printhome.ui.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseFragment;
import huanxing_print.com.cn.printhome.constant.ConFig;
import huanxing_print.com.cn.printhome.log.Logger;
import huanxing_print.com.cn.printhome.model.contact.FriendSearchInfo;
import huanxing_print.com.cn.printhome.model.print.IsOnlineResp;
import huanxing_print.com.cn.printhome.model.print.PcLoginResp;
import huanxing_print.com.cn.printhome.model.print.PrintInfoResp;
import huanxing_print.com.cn.printhome.net.callback.contact.FriendSearchCallback;
import huanxing_print.com.cn.printhome.net.request.contact.FriendManagerRequest;
import huanxing_print.com.cn.printhome.net.request.print.HttpListener;
import huanxing_print.com.cn.printhome.net.request.print.PrintRequest;
import huanxing_print.com.cn.printhome.ui.activity.contact.SearchAddResultActivity;
import huanxing_print.com.cn.printhome.ui.activity.print.AddFileActivity;
import huanxing_print.com.cn.printhome.util.GsonUtil;
import huanxing_print.com.cn.printhome.util.SharedPreferencesUtils;
import huanxing_print.com.cn.printhome.util.ShowUtil;
import huanxing_print.com.cn.printhome.util.StepViewUtil;
import huanxing_print.com.cn.printhome.util.ToastUtil;
import huanxing_print.com.cn.printhome.util.UrlUtil;
import huanxing_print.com.cn.printhome.view.StepLineView;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;

import static huanxing_print.com.cn.printhome.R.id.iv_notice;


public class PrintFragment extends BaseFragment implements OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

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


    private void turnPickFile(PrintInfoResp.PrinterPrice printerPrice) {
        EventBus.getDefault().postSticky(printerPrice);
        Bundle bundle = new Bundle();
        bundle.putInt(AddFileActivity.INDEX, 0);
        AddFileActivity.start(getActivity(), bundle);
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
                    String uniqueCode = UrlUtil.getValueByName(result, "uniqueCode");
                    if (uniqueCode != null) {
                        pcLogin(uniqueCode);
                        return;
                    }
                    if (result.startsWith("MINE:")) {
                        String subResultString = result.replace("MINE:", "");
                        searchFriend(subResultString);
                        return;
                    }
                    ShowUtil.showToast("无效的二维码");
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    ShowUtil.showToast("解析二维码失败");
                }
            }
        }
    }

    private void pcLogin(String uniqueCode) {
        PrintRequest.pcLogin(getActivity(), uniqueCode, new HttpListener() {
            @Override
            public void onSucceed(String content) {
                PcLoginResp pcLoginResp = GsonUtil.GsonToBean(content, PcLoginResp.class);
                if (pcLoginResp != null && pcLoginResp.isSuccess()) {
                    if (pcLoginResp.isSuccess()) {
                        ShowUtil.showToast("登录成功");
                    }
                }
                if (pcLoginResp != null && !pcLoginResp.isSuccess()) {
                    ShowUtil.showToast(pcLoginResp.getErrorMsg());
                }
            }

            @Override
            public void onFailed(String exception) {
                ShowUtil.showToast(getString(R.string.net_error));
            }
        });
    }

    private void searchFriend(String searchContent) {
        String token = SharedPreferencesUtils.getShareString(getActivity(), ConFig.SHAREDPREFERENCES_NAME,
                "loginToken");
        DialogUtils.showProgressDialog(getActivity(), "加载中").show();
        FriendManagerRequest.friendSearch(getActivity(), token, searchContent, friendSearchCallback);
    }

    FriendSearchCallback friendSearchCallback = new FriendSearchCallback() {
        @Override
        public void success(String msg, FriendSearchInfo friendSearchInfo) {
            DialogUtils.closeProgressDialog();
            if (null != friendSearchInfo) {
                ArrayList<FriendSearchInfo> infos = new ArrayList<FriendSearchInfo>();
                infos.add(friendSearchInfo);
                Intent intent = new Intent(getActivity(), SearchAddResultActivity.class);
                intent.putParcelableArrayListExtra("search result", infos);
                startActivity(intent);
            }
        }

        @Override
        public void fail(String msg) {
            DialogUtils.closeProgressDialog();
            ToastUtil.doToast(getActivity(), msg);
        }

        @Override
        public void connectFail() {
            DialogUtils.closeProgressDialog();
            ToastUtil.doToast(getActivity(), "网络连接超时");
        }
    };
}
