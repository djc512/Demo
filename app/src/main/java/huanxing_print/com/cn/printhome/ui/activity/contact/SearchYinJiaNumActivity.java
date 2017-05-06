package huanxing_print.com.cn.printhome.ui.activity.contact;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.model.contact.ContactInfo;
import huanxing_print.com.cn.printhome.util.CommonUtils;

/**
 * Created by wanghao on 2017/5/3.
 */

public class SearchYinJiaNumActivity extends BaseActivity implements View.OnClickListener,TextWatcher{
    private EditText searchEt;
    private View show_search_content;
    private TextView hint_content;
    private View del_icon;
    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.activity_search_yinjia_num);

        initView();
        setListener();
    }

    private void initView() {
        searchEt = (EditText) findViewById(R.id.search_et);
        show_search_content = findViewById(R.id.show_search_content);
        hint_content = (TextView) findViewById(R.id.hint_content);
        del_icon = findViewById(R.id.del_content);
    }

    private void setListener() {
        findViewById(R.id.exit_search).setOnClickListener(this);
        show_search_content.setOnClickListener(this);
        searchEt.addTextChangedListener(this);
        del_icon.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.exit_search:
                finishCurrentActivity();
                break;
            case R.id.show_search_content:
                ArrayList<ContactInfo> infos = new ArrayList<ContactInfo>();
                ContactInfo info = new ContactInfo();
                info.setName("陆成宋");
                info.setYjNum("1867989");
                info.setIconPath("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494660151&di=fc28cd4cd681bb1d70df6ff6654791ff&imgtype=jpg&er=1&src=http%3A%2F%2Fimgsrc.baidu.com%2Fforum%2Fw%253D580%2Fsign%3D8c03c118ca8065387beaa41ba7dda115%2Fc17fc0bf6c81800a06c8cd58b13533fa828b4759.jpg");
                info.setAddRequest(false);
                ContactInfo info01 = new ContactInfo();
                info01.setName("陆成宋01");
                info01.setYjNum("1867767");
                info01.setIconPath("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494065434200&di=7c53b18639aa82a8a58a296b9502d4ee&imgtype=0&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fzhidao%2Fwh%253D450%252C600%2Fsign%3D7048a12f9e16fdfad839ceea81bfa062%2F6a63f6246b600c3350e384cc194c510fd9f9a118.jpg");
                info01.setAddRequest(true);
                infos.add(info);
                infos.add(info01);
                Intent intent = new Intent(SearchYinJiaNumActivity.this,SearchAddResultActivity.class);
                intent.putParcelableArrayListExtra("search result",infos);
                startActivity(intent);
                break;
            case R.id.del_content:
                searchEt.setText(null);
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if(charSequence.length() > 0) {
            show_search_content.setVisibility(View.VISIBLE);
            hint_content.setText(charSequence);
            del_icon.setVisibility(View.VISIBLE);
        }else{
            show_search_content.setVisibility(View.GONE);
            hint_content.setText(null);
            del_icon.setVisibility(View.GONE);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }
}
