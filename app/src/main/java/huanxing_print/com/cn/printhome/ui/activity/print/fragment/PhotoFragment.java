package huanxing_print.com.cn.printhome.ui.activity.print.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.log.Logger;
import huanxing_print.com.cn.printhome.ui.activity.print.ImgPreviewActivity;
import huanxing_print.com.cn.printhome.ui.adapter.PhotoRecylerAdapter;

import static huanxing_print.com.cn.printhome.ui.activity.print.ImgPreviewActivity.KEY_IMG_URI;


/**
 * Created by LGH on 2017/4/27.
 */

public class PhotoFragment extends BaseLazyFragment {

    private RecyclerView mRcList;
    private PhotoRecylerAdapter mAdapter;

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
      final List<String> photoList = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null,
                null, null);
        cursor.moveToLast();
        while (cursor.moveToPrevious()) {
            String name = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
            String desc = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DESCRIPTION));
            byte[] data = cursor.getBlob(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            String photoPath = new String(data, 0, data.length - 1);
            photoList.add(photoPath);
            Logger.i(name + photoPath);
        }
        mRcList = (RecyclerView) view.findViewById(R.id.mRecView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        mRcList.setLayoutManager(new GridLayoutManager(context, 4));
        mRcList.setHasFixedSize(true);
        mRcList.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new PhotoRecylerAdapter(context, photoList);
        mRcList.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new PhotoRecylerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final View view, int position) {
                Intent intent = new Intent(context, ImgPreviewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putCharSequence(KEY_IMG_URI, photoList.get(position));
//                bundle.putParcelable(KEY_IMG_URI, photoList.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        isLoaded = true;
    }

}

