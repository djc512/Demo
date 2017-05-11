package huanxing_print.com.cn.printhome.ui.activity.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseFragment;
import huanxing_print.com.cn.printhome.ui.activity.print.AddFileActivity;
import huanxing_print.com.cn.printhome.util.StepViewUtil;
import huanxing_print.com.cn.printhome.view.StepLineView;


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
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected int getContextView() {
        return R.layout.frag_print;
    }

    @Override
    public void onClick(View v) {
        int index = 0;
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
}
