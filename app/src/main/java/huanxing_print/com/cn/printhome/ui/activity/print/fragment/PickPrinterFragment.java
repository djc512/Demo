package huanxing_print.com.cn.printhome.ui.activity.print.fragment;

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

import java.util.ArrayList;
import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.ui.adapter.PrinterFragmentAdapter;

public class PickPrinterFragment extends BaseLazyFragment {

    private ViewPager viewpager;
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
        viewpager = (ViewPager) view.findViewById(R.id.viewpager);
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
        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
        titleTv.setTextColor(ContextCompat.getColor(context, R.color.text_black));
        if (titleTv.getText().toString().equals("已用打印机")) {
            icImg.setImageResource(R.drawable.ic_printer_used_selected);
        } else if (titleTv.getText().toString().equals("找打印机")) {
            icImg.setImageResource(R.drawable.ic_printer_search_selected);
        } else {
            icImg.setImageResource(R.drawable.ic_printer_scan_selected);
        }
    }

    private void changeTabNormal(TabLayout.Tab tab) {
        View view = tab.getCustomView();
        ImageView icImg = (ImageView) view.findViewById(R.id.icImg);
        TextView titleTv = (TextView) view.findViewById(R.id.titleTv);
        titleTv.setTextColor(ContextCompat.getColor(context, R.color.text_gray));
        if (titleTv.getText().toString().equals("已用打印机")) {
            icImg.setImageResource(R.drawable.ic_printer_used_unselected);
        } else if (titleTv.getText().toString().equals("找打印机")) {
            icImg.setImageResource(R.drawable.ic_printer_search_unselected);
        } else {
            icImg.setImageResource(R.drawable.ic_printer_scan_unselected);
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

}
