package huanxing_print.com.cn.printhome.ui.activity.print.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.log.Logger;
import huanxing_print.com.cn.printhome.ui.activity.print.PickPrinterActivity;
import huanxing_print.com.cn.printhome.ui.adapter.PrinterFragmentAdapter;
import huanxing_print.com.cn.printhome.util.ShowUtil;
import huanxing_print.com.cn.printhome.util.UrlUtil;
import huanxing_print.com.cn.printhome.view.viewpager.NoScrollViewPager;

import static huanxing_print.com.cn.printhome.R.id.textView;

public class PickPrinterFragment extends BaseLazyFragment {

    private NoScrollViewPager viewpager;
    private TabLayout tabs;
    private List<String> titles = new ArrayList<>();
    private int[] tabIconsUnselected = {
            R.drawable.ic_printer_used_unselected,
            R.drawable.ic_printer_search_unselected,
            R.drawable.ic_printer_scan_unselected
    };
    private int[] tabIconsSelected = {
            R.drawable.ic_printer_used_selected,
            R.drawable.ic_printer_search_selected,
            R.drawable.ic_printer_scan_selected
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_pick_printer, container, false);
            initView(view);
            isPrepared = true;
            if (!isLoaded) {
                lazyLoad();
            }
        }
        return view;
    }

    private void initView(View view) {
        viewpager = (NoScrollViewPager) view.findViewById(R.id.viewpager);
        tabs = (TabLayout) view.findViewById(R.id.tabs);
        titles.add("已用打印机");
        titles.add("找打印机");
        titles.add("扫码连机");
        for (int i = 0; i < titles.size(); i++) {
            tabs.addTab(tabs.newTab().setText(titles.get(i)));
        }
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new UsedPrinterFragment());
        fragments.add(new FindPrinterFragment());
        fragments.add(new PcFileFragment());
        PrinterFragmentAdapter mFragmentAdapteradapter = new PrinterFragmentAdapter(getFragmentManager(),
                fragments, titles);
        viewpager.setAdapter(mFragmentAdapteradapter);
        tabs.setupWithViewPager(viewpager);
        setupTabIcons();
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                changeTabSelect(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                changeTabNormal(tab);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        setTabIndex(1);
        setTabIndex(0);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 2) {
                    turnScan();
                    setTabIndex(0);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        TabLayout.Tab tab = tabs.getTabAt(2);
        tab.getCustomView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnScan();
            }
        });
    }

    public static final int REQUEST_CODE = 1;

    private void turnScan() {
        Intent intent = new Intent(context, CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
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
                    if (printNo == null) {
                        ShowUtil.showToast("无效的二维码");
                        return;
                    }
                    ((PickPrinterActivity) getActivity()).requeryIsOnline(printNo);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    ShowUtil.showToast("解析二维码失败");
                }
            }
        }
    }

    private void setTabIndex(int index) {
        viewpager.setCurrentItem(index);
        tabs.getTabAt(index).select();
    }

    private void setupTabIcons() {
        tabs.getTabAt(0).setCustomView(getTabView(0));
        tabs.getTabAt(1).setCustomView(getTabView(1));
        tabs.getTabAt(2).setCustomView(getTabView(2));
    }

    private void changeTabSelect(TabLayout.Tab tab) {
        View view = tab.getCustomView();
        ImageView icImg = (ImageView) view.findViewById(R.id.icImg);
        TextView titleTv = (TextView) view.findViewById(R.id.titleTv);
        titleTv.getPaint().setFakeBoldText(true);
        titleTv.setTextColor(ContextCompat.getColor(context, R.color.text_black));
        int position = tab.getPosition();
        switch (position) {
            case 0:
                icImg.setImageResource(R.drawable.ic_printer_used_selected);
                break;
            case 1:
                icImg.setImageResource(R.drawable.ic_printer_search_selected);
                break;
            case 2:
                icImg.setImageResource(R.drawable.ic_printer_scan_selected);
                break;
        }
    }

    private void changeTabNormal(TabLayout.Tab tab) {
        View view = tab.getCustomView();
        ImageView icImg = (ImageView) view.findViewById(R.id.icImg);
        TextView titleTv = (TextView) view.findViewById(R.id.titleTv);
        titleTv.getPaint().setFakeBoldText(false);
        titleTv.setTextColor(ContextCompat.getColor(context, R.color.text_gray));
        int position = tab.getPosition();
        switch (position) {
            case 0:
                icImg.setImageResource(R.drawable.ic_printer_used_unselected);
                break;
            case 1:
                icImg.setImageResource(R.drawable.ic_printer_search_unselected);
                break;
            case 2:
                icImg.setImageResource(R.drawable.ic_printer_scan_unselected);
                break;
        }
    }

    public View getTabView(int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tab_pick_printer, null);
        TextView titleTv = (TextView) view.findViewById(R.id.titleTv);
        titleTv.setText(titles.get(position));
        ImageView icImg = (ImageView) view.findViewById(R.id.icImg);
        icImg.setImageResource(tabIconsUnselected[position]);
        return view;
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || isLoaded) {
            return;
        }
        isLoaded = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


}
