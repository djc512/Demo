package huanxing_print.com.cn.printhome.ui.activity.Copy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xlhratingbar_lib.XLHRatingBar;

import java.util.ArrayList;
import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.model.picupload.ImageItem;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.picuplload.Bimp;
import huanxing_print.com.cn.printhome.util.picuplload.BitmapLoadUtils;

/**
 * Created by Administrator on 2017/5/4 0004.
 */

public class CommentActivity extends BaseActivity implements View.OnClickListener {
    public static Bitmap bimap;
    private static final int PICK_PHOTO = 1;
    private List<Bitmap> mResults = new ArrayList<>();
    private GridView noScrollgridview;
    private GridAdapter adapter;
    private Context ctx;
    private ImageView iv_back;
    private LinearLayout ll_back;
    private EditText et_comment_content;
    private TextView tv_num;
    private TextView tv_pic_num;
    private XLHRatingBar rb_comment;
    private XLHRatingBar rb_speed;
    private XLHRatingBar rb_qulity;
    private XLHRatingBar rb_handle;
    private XLHRatingBar rb_price;
    private EditText et_commnet;
    private TextView tv_submit;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.activity_comment);
        ctx = this;
        bimap = BitmapFactory.decodeResource(getResources(), R.drawable.add);
        mResults.add(bimap);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
        noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));

        iv_back = (ImageView) findViewById(R.id.iv_back);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        et_comment_content = (EditText) findViewById(R.id.et_comment_content);
        tv_num = (TextView) findViewById(R.id.tv_num);
        tv_pic_num = (TextView) findViewById(R.id.tv_pic_num);
        rb_comment = (XLHRatingBar) findViewById(R.id.rb_comment);
        rb_speed = (XLHRatingBar) findViewById(R.id.rb_speed);
        rb_qulity = (XLHRatingBar) findViewById(R.id.rb_qulity);
        rb_handle = (XLHRatingBar) findViewById(R.id.rb_handle);
        rb_price = (XLHRatingBar) findViewById(R.id.rb_price);
        et_commnet = (EditText) findViewById(R.id.et_commnet);
        tv_submit = (TextView) findViewById(R.id.tv_submit);
    }

    private void initData() {
        adapter = new GridAdapter(this);
        adapter.update();
        noScrollgridview.setAdapter(adapter);
        noScrollgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == mResults.size() - 1 || i == Bimp.tempSelectBitmap.size()) {
                    Intent intent = new Intent(ctx, PhotoPickerActivity.class);
                    intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, true);
                    intent.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE, PhotoPickerActivity.MODE_MULTI);
                    intent.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN, PhotoPickerActivity.DEFAULT_NUM);
                    // 总共选择的图片数量
                    intent.putExtra(PhotoPickerActivity.TOTAL_MAX_MUN, Bimp.tempSelectBitmap.size());
                    startActivityForResult(intent, PICK_PHOTO);
                } else {
                    Intent intent = new Intent(ctx,
                            PreviewPhotoActivity.class);
                    intent.putExtra("position", "1");
                    intent.putExtra("ID", i);
                    startActivity(intent);
                }
            }
        });
    }

    private int commentStar;
    private int speedStar;
    private int qulityStar;
    private int handleStar;
    private int priceStar;

    private void initListener() {
        iv_back.setOnClickListener(this);
        tv_submit.setOnClickListener(this);

        rb_comment.setOnRatingChangeListener(new XLHRatingBar.OnRatingChangeListener() {
            @Override
            public void onChange(int countSelected) {
                commentStar = countSelected;
            }
        });
        rb_speed.setOnRatingChangeListener(new XLHRatingBar.OnRatingChangeListener() {
            @Override
            public void onChange(int countSelected) {
                speedStar = countSelected;
            }
        });
        rb_qulity.setOnRatingChangeListener(new XLHRatingBar.OnRatingChangeListener() {
            @Override
            public void onChange(int countSelected) {
                qulityStar = countSelected;
            }
        });
        rb_handle.setOnRatingChangeListener(new XLHRatingBar.OnRatingChangeListener() {
            @Override
            public void onChange(int countSelected) {
                handleStar = countSelected;
            }
        });
        rb_price.setOnRatingChangeListener(new XLHRatingBar.OnRatingChangeListener() {
            @Override
            public void onChange(int countSelected) {
                priceStar = countSelected;
            }
        });

        et_comment_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.length();
                if (length > 200) {
                    Toast.makeText(ctx, "最多输入200字", Toast.LENGTH_SHORT).show();
                    return;
                }
                tv_num.setText(length+"/"+200);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO) {
            if (resultCode == RESULT_OK) {
                ArrayList<String> result = data.getStringArrayListExtra(PhotoPickerActivity.KEY_RESULT);
                showResult(result);
            }
        }
    }

    private void showResult(ArrayList<String> paths) {
        if (mResults == null) {
            mResults = new ArrayList<>();
        }
        if (paths.size() != 0) {
            mResults.remove(mResults.size() - 1);
        }
        for (int i = 0; i < paths.size(); i++) {
            // 压缩图片
            Bitmap bitmap = BitmapLoadUtils.decodeSampledBitmapFromFd(paths.get(i), 400, 500);
            // 针对小图也可以不压缩
//            Bitmap bitmap = BitmapFactory.decodeFile(paths.get(i));
            mResults.add(bitmap);

            ImageItem takePhoto = new ImageItem();
            takePhoto.setBitmap(bitmap);
            Bimp.tempSelectBitmap.add(takePhoto);
        }
        mResults.add(BitmapFactory.decodeResource(getResources(), R.drawable.add));
        int gvHeight = 0;
        if (mResults.size() < 5) {
            gvHeight = dip2px(ctx, 60);
        } else if (mResults.size() < 9) {
            gvHeight = dip2px(ctx, 125);
        } else {
            gvHeight = dip2px(ctx, 190);
        }

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, gvHeight);
        noScrollgridview.setLayoutParams(lp);
        adapter.notifyDataSetChanged();
    }

    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finishCurrentActivity();
                break;
            case R.id.tv_submit:
                String content = et_comment_content.getText().toString().trim();//打印感受
                String commentStr = et_commnet.getText().toString().trim();//评论
//                ArrayList<ImageItem> items = Bimp.tempSelectBitmap;//图片
//                UpLoadBitmap upLoadBitmap = new UpLoadBitmap(ctx);
//                for (int i = 0; i < items.size(); i++) {
//                    Bitmap bitmap = items.get(i).getBitmap();
//                    upLoadBitmap.setPicToView(bitmap);
//                }
                Toast.makeText(ctx, "发表成功", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 适配器
     */
    public class GridAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        public GridAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void update() {
            loading();
        }

        public int getCount() {
            if (Bimp.tempSelectBitmap.size() == 9) {
                return 9;
            }
            return (Bimp.tempSelectBitmap.size() + 1);
        }

        public Object getItem(int arg0) {
            return mResults.get(arg0);
        }

        public long getItemId(int arg0) {
            return arg0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_gridview, null);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView
                        .findViewById(R.id.imageView1);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (position == Bimp.tempSelectBitmap.size()) {
                holder.image.setImageBitmap(BitmapFactory.decodeResource(
                        getResources(), R.drawable.add));
                if (position == 9) {
                    holder.image.setVisibility(View.GONE);
                }
            } else {
                holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position).getBitmap());
            }
            return convertView;
        }

        public class ViewHolder {
            public ImageView image;
        }

        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        adapter.notifyDataSetChanged();
                        break;
                }
                super.handleMessage(msg);
            }
        };

        public void loading() {
            new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        if (Bimp.max == Bimp.tempSelectBitmap.size()) {
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                            break;
                        } else {
                            Bimp.max += 1;
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                        }
                    }
                }
            }).start();
        }
    }

    protected void onRestart() {
        adapter.update();
        super.onRestart();
    }
}
