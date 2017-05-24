package huanxing_print.com.cn.printhome.ui.activity.copy;

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
import android.util.Base64;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.model.comment.PicDataBean;
import huanxing_print.com.cn.printhome.model.image.ImageUploadItem;
import huanxing_print.com.cn.printhome.model.picupload.ImageItem;
import huanxing_print.com.cn.printhome.net.callback.NullCallback;
import huanxing_print.com.cn.printhome.net.callback.comment.UpLoadPicCallBack;
import huanxing_print.com.cn.printhome.net.request.commet.CommentRequest;
import huanxing_print.com.cn.printhome.net.request.commet.UpLoadPicRequest;
import huanxing_print.com.cn.printhome.util.BitmapUtils;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.FileUtils;
import huanxing_print.com.cn.printhome.util.picuplload.Bimp;
import huanxing_print.com.cn.printhome.view.dialog.DialogUtils;

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
    private TextView tv_submit;
    private ImageView iv_comment;
    private String content;
    private List<ImageUploadItem> imageitems = new ArrayList<>();
    private List<String> imageUrls = new ArrayList<>();
    private long orderid;
    private TextView tv_address;
    private TextView tv_printNum;
    private String printNum;
    private String printLocation;
    private ArrayList<ImageItem> selectBitmap;

    private boolean listNull =true;
    private List<ImageUploadItem> imageUploadlist;

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
        selectBitmap = Bimp.tempSelectBitmap;
        selectBitmap.clear();
        mResults.clear();
        bimap = BitmapFactory.decodeResource(getResources(), R.drawable.add);
        mResults.add(bimap);
        orderid = getIntent().getExtras().getLong("order_id");
        //获取打印机编号
        printNum = getIntent().getExtras().getString("printNum");
        //打印机所在位置
        printLocation = getIntent().getExtras().getString("location");

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
        tv_submit = (TextView) findViewById(R.id.tv_submit);
        iv_comment = (ImageView) findViewById(R.id.iv_comment);

        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_printNum = (TextView) findViewById(R.id.tv_printNum);
    }

    private void initData() {
        tv_printNum.setText("编号:" + printNum);
        tv_address.setText(printLocation);

        adapter = new GridAdapter(this);
        noScrollgridview.setAdapter(adapter);
        noScrollgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == mResults.size() - 1 || i == selectBitmap.size()) {
                    Intent intent = new Intent(ctx, PhotoPickerActivity.class);
                    intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, true);
                    intent.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE, PhotoPickerActivity.MODE_MULTI);
                    intent.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN, PhotoPickerActivity.DEFAULT_NUM);
                    // 总共选择的图片数量
                    intent.putExtra(PhotoPickerActivity.TOTAL_MAX_MUN, selectBitmap.size());
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
        iv_comment.setOnClickListener(this);

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
                tv_num.setText(length + "/" + 200);
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
            Bitmap bitmap = null;
            try {
                bitmap = BitmapUtils.revitionImageSize(paths.get(i));
            } catch (IOException e) {
                e.printStackTrace();
            }
            mResults.add(bitmap);

            ImageItem takePhoto = new ImageItem();
            takePhoto.setBitmap(bitmap);
            selectBitmap.add(takePhoto);
        }
        mResults.add(BitmapFactory.decodeResource(getResources(), R.drawable.add));
        adapter.notifyDataSetChanged();
    }

    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private boolean isHideName = true;
    private int anonymous = -1;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finishCurrentActivity();
                break;
            case R.id.tv_submit:
                //打印感受
                content = et_comment_content.getText().toString().trim();
                submitComment();
                break;
            case R.id.iv_comment:
                if (isHideName) {
                    iv_comment.setImageResource(R.drawable.select_no);
                    anonymous = 1;
                } else {
                    iv_comment.setImageResource(R.drawable.select);
                    anonymous = 0;
                }
                isHideName = !isHideName;
                break;
        }
    }

    /**
     * 添加评论
     */
    private void submitComment() {

        imageUploadlist= new ArrayList<ImageUploadItem>();
        ImageUploadItem imageUploadItem ;
        if (Bimp.tempSelectBitmap.size()>0) {
            listNull=false;
            for (int i = 0; i < Bimp.tempSelectBitmap.size(); i++) {
                // 高清的压缩图片全部就在  list 路径里面了
                // 高清的压缩过的 bmp 对象  都在 Bimp.bmp里面
                // 完成上传服务器后 .........FileUtils.deleteDir();
                imageUploadItem = new ImageUploadItem();
                String filePath = FileUtils.saveFile(getSelfActivity(), "img"+i+".jpg", Bimp.tempSelectBitmap.get(i).getBitmap());
                File file = new File(filePath);
                //file转化成二进制
                byte[] buffer = null;
                FileInputStream in ;
                int length = 0;
                try {
                    in = new FileInputStream(file);
                    buffer = new byte[(int) file.length() + 100];
                    length = in.read(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String data = Base64.encodeToString(buffer, 0, length, Base64.NO_WRAP);
                imageUploadItem.setFileContent(data);
                imageUploadItem.setFileId(i+"");
                imageUploadItem.setFileName(filePath);
                imageUploadItem.setFileType(".jpg");
                imageUploadlist.add(imageUploadItem);
            }
            upLoadImageList(imageUploadlist);

        }else{
            listNull=true;
            sendNote(imageUrls);
        }
    }

//    /**
//     * 获取上传图片的url
//     *
//     * @param items
//     */
//    private void getUrl(ArrayList<ImageItem> items) {
//        for (int i = 0; i < items.size(); i++) {
//            Bitmap bitmap = items.get(i).getBitmap();
//            setPicToView(bitmap, i );
//        }
//    }
//
//    private void setPicToView(Bitmap bitmap, int pos) {
//        ImageUploadItem image = new ImageUploadItem();
//        String filePath = FileUtils.saveFile(getSelfActivity(), "img"+pos+".jpg", bitmap);
//        if (!ObjectUtils.isNull(filePath)) {
//            File file = new File(filePath);
//            //file转化成二进制
//            byte[] buffer = null;
//            FileInputStream in;
//            int length = 0;
//            try {
//                in = new FileInputStream(file);
//                buffer = new byte[(int) file.length() + 100];
//                length = in.read(buffer);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            String data = Base64.encodeToString(buffer, 0, length, Base64.NO_WRAP);
//            image.setFileContent(data);
//            image.setFileId(pos + "");
//            image.setFileName(filePath);
//            image.setFileType(".jpg");
//
//            imageitems.add(image);
//        }
//    }

    /**
     * 上传图片
     */
    private void upLoadImageList(List<ImageUploadItem> images) {
        DialogUtils.showProgressDialog(getSelfActivity(), "正在发表").show();
        Map<String, Object> map = new HashMap<>();
        map.put("files", images);
        UpLoadPicRequest.request(getSelfActivity(), map, new UpLoadPicCallBack() {
            @Override
            public void success(List<PicDataBean> bean) {
                if (null != bean && bean.size() > 0) {
                    for (int i = 0; i < bean.size(); i++) {
                        String imgUrl = bean.get(i).getImgUrl();
                        imageUrls.add(imgUrl);
                    }

                    sendNote(imageUrls);
                }
            }



            @Override
            public void fail(String msg) {
                toast(msg);
                DialogUtils.closeProgressDialog();
            }

            @Override
            public void connectFail() {
                toastConnectFail();
                DialogUtils.closeProgressDialog();
            }
        });
    }
    private void sendNote(List<String> imageUrls) {
        if (listNull) {
            DialogUtils.showProgressDialog(getSelfActivity(), "正在发表").show();
        }

        Map<String, Object> params = new HashMap<>();
        params.put("anonymous", anonymous);
        params.put("convenienceScore", rb_handle.getCountNum());
        params.put("orderId", orderid);
        params.put("imgList", imageUrls);
        params.put("priceScore", rb_price.getCountNum());
        params.put("remark", content);
        params.put("speedScore", rb_speed.getCountNum());
        params.put("totalScore", rb_comment.getCountNum());
        params.put("qualityScore", rb_qulity.getCountNum());

        CommentRequest.submit(getSelfActivity(), baseApplication.getLoginToken(), params, new NullCallback() {
            @Override
            public void success(String msg) {
                FileUtils.deleteDir();
                DialogUtils.closeProgressDialog();
                toast("发表成功");
                Bimp.tempSelectBitmap.clear();
                Bimp.max = 0;
                Intent intent = new Intent(getSelfActivity(), CommentListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("printer_id", printNum + "");
                intent.putExtras(bundle);
                startActivity(intent);
                finishCurrentActivity();
            }

            @Override
            public void fail(String msg) {

            }

            @Override
            public void connectFail() {

            }
        });


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
            if (selectBitmap.size() == 9) {
                return 9;
            }
            return (selectBitmap.size() + 1);
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

            if (position == selectBitmap.size()) {
                holder.image.setImageBitmap(BitmapFactory.decodeResource(
                        getResources(), R.drawable.add));
                if (position == 9) {
                    holder.image.setVisibility(View.GONE);
                }
            } else {
                holder.image.setImageBitmap(selectBitmap.get(position).getBitmap());
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
                        if (Bimp.max == selectBitmap.size()) {
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

    @Override
    protected void onResume() {
        super.onResume();
        int gvHeight = 0;
        if (adapter.getCount() < 5) {
            gvHeight = dip2px(ctx, 60);
        } else if (adapter.getCount() < 9) {
            gvHeight = dip2px(ctx, 125);
        } else {
            gvHeight = dip2px(ctx, 190);
        }

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, gvHeight);
        noScrollgridview.setLayoutParams(lp);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != Bimp.tempSelectBitmap) {
            Bimp.tempSelectBitmap.clear();
        }
        if (null != mResults) {
            mResults.clear();
        }
    }
}
