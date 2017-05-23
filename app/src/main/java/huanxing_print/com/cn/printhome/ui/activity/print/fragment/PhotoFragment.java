package huanxing_print.com.cn.printhome.ui.activity.print.fragment;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.ui.activity.print.ImgPreviewActivity;
import huanxing_print.com.cn.printhome.ui.adapter.PhotoRecylerAdapter;
import huanxing_print.com.cn.printhome.util.FileUtils;
import huanxing_print.com.cn.printhome.util.ShowUtil;

import static huanxing_print.com.cn.printhome.ui.activity.print.ImgPreviewActivity.KEY_IMG_URI;


/**
 * Created by LGH on 2017/4/27.
 */

public class PhotoFragment extends BaseLazyFragment {

    private RecyclerView mRcList;
    private PhotoRecylerAdapter mAdapter;
    private List<String> photoList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_photo, container, false);
            isPrepared = true;
            if (!isLoaded) {
                lazyLoad();
            }
        }
        return view;
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || isLoaded) {
            return;
        }
        isLoaded = true;
        mRcList = (RecyclerView) view.findViewById(R.id.mRecView);
        mRcList.setLayoutManager(new GridLayoutManager(context, 4));
        mRcList.setHasFixedSize(true);
        mRcList.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new PhotoRecylerAdapter(context, photoList);
        mRcList.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new PhotoRecylerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putCharSequence(KEY_IMG_URI, photoList.get(position));
                ImgPreviewActivity.start(context, bundle);
            }
        });
        getImgFile();
    }

    private void updateView() {
        if (photoList == null || photoList.size() == 0) {
            ShowUtil.showToast("没有相关文件");
            return;
        }
        mAdapter.setPhotoList(photoList);
        mAdapter.notifyDataSetChanged();
    }

    private void getImgFile() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                photoList = FileUtils.getImgList(context);
                if (mActivity != null) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateView();
                        }
                    });
                }
            }
        }.start();
    }

}

