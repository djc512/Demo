package huanxing_print.com.cn.printhome.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.github.chrisbanes.photoview.PhotoView;

import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.util.ImageUtil;

/**
 * Created by LGH on 2017/5/9.
 */

public class DocPreViewpageAdapter extends PagerAdapter {

    private List<String> urlList;
    private Context context;

    public DocPreViewpageAdapter(Context context, List<String> urlList) {
        this.context = context;
        this.urlList = urlList;
        this.urlList.add("http://i.imgur.com/DvpvklR.png");
    }

    @Override
    public int getCount() {
        return urlList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = View.inflate(context, R.layout.item_doc_viewpager, null);
        PhotoView photoView = (PhotoView) view.findViewById(R.id.photoView);
        ImageUtil.showImageView(context, urlList.get(position), photoView);
        container.addView(view);
        return view;
    }
}
