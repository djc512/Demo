package huanxing_print.com.cn.printhome.ui.activity.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.util.CommonUtils;

/**
 * Created by Administrator on 2017/4/28 0028.
 */

public class FileFragment extends Fragment implements View.OnClickListener {
    private RadioButton btn_camera;
    private RadioButton btn_pic;
    private Context ctx;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ctx = getActivity();
        CommonUtils.initSystemBar(getActivity());
        View view = inflater.inflate(R.layout.frag_file, null);
        initView(view);
        initListener();
        return view;
    }

    private void initListener() {
        btn_camera.setOnClickListener(this);
        btn_pic.setOnClickListener(this);
    }

    private void initView(View view) {
        btn_camera = (RadioButton) view.findViewById(R.id.btn_camera);
        btn_pic = (RadioButton) view.findViewById(R.id.btn_pic);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_camera:
                btn_camera.setChecked(true);
                btn_pic.setChecked(false);
                break;
            case R.id.btn_pic:
                btn_camera.setChecked(false);
                btn_pic.setChecked(true);
                break;
        }
    }
}
