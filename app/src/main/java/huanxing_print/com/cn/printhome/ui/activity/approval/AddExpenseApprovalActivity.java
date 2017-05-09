package huanxing_print.com.cn.printhome.ui.activity.approval;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.base.BaseActivity;
import huanxing_print.com.cn.printhome.model.contact.FriendInfo;
import huanxing_print.com.cn.printhome.ui.activity.copy.PhotoPickerActivity;
import huanxing_print.com.cn.printhome.ui.activity.copy.PreviewPhotoActivity;
import huanxing_print.com.cn.printhome.ui.adapter.UpLoadPicAdapter;
import huanxing_print.com.cn.printhome.util.CircleTransform;
import huanxing_print.com.cn.printhome.util.CommonUtils;
import huanxing_print.com.cn.printhome.util.ObjectUtils;
import huanxing_print.com.cn.printhome.util.picuplload.Bimp;
import huanxing_print.com.cn.printhome.util.picuplload.UpLoadPicUtil;
import huanxing_print.com.cn.printhome.view.ScrollGridView;
import huanxing_print.com.cn.printhome.view.imageview.RoundImageView;

/**
 * description: 新建报销审批
 * author LSW
 * date 2017/5/6 10:40
 * update 2017/5/6
 */
public class AddExpenseApprovalActivity extends BaseActivity implements View.OnClickListener {

    private List<Bitmap> mResults = new ArrayList<>();
    private GridView noScrollgridview;
    private UpLoadPicAdapter adapter;
    public static Bitmap bimap;
    private Context ctx;
    private static final int PICK_PHOTO = 1;
    private ScrollGridView grid_scroll_approval;//审批人
    private ScrollGridView grid_scroll_copy;//抄送
    private GridViewApprovalAdapter approvalAdapter;
    private GridViewCopyAdapter copyAdapter;
    private ArrayList<FriendInfo> friends = new ArrayList<FriendInfo>();//生成的假数据
    private ArrayList<FriendInfo> approvalFriends = new ArrayList<FriendInfo>();//审批人
    private ArrayList<FriendInfo> copyFriends = new ArrayList<FriendInfo>();//抄送人
    private int CODE_APPROVAL_REQUEST = 0X11;//审批人请求码
    private int CODE_COPY_REQUEST = 0X12;//抄送人人请求码

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 改变状态栏的颜色使其与APP风格一体化
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.activity_add_expense_approval);

        ctx = this;
        initData();
        getData();
        functionModule();
    }

    private void functionModule() {

        noScrollgridview.setAdapter(adapter);
        noScrollgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == mResults.size() - 1 || i == Bimp.tempSelectBitmap.size()) {
                    Intent intent = new Intent(ctx, PhotoPickerActivity.class);
                    intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, true);
                    intent.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE, PhotoPickerActivity.MODE_MULTI);
                    intent.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN, 5 - mResults.size() + 1);
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

        //审批人
        grid_scroll_approval.setAdapter(approvalAdapter);
        grid_scroll_approval.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == approvalFriends.size()) {
                    //跳转到选择联系人界面
                    Intent intent = new Intent(getSelfActivity(), ChoosePeopleOfAddressActivity.class);
                    intent.putParcelableArrayListExtra("friends", friends);
                    startActivityForResult(intent, CODE_APPROVAL_REQUEST);
//                    //点击了添加按钮
//                    approvals.set(position,
//                            BitmapFactory.decodeResource(getResources(), R.drawable.iv_head));
//                    approvals.add(bimapAdd);
//                    approvalAdapter.notifyDataSetChanged();
                } else {
                    approvalFriends.remove(position);
                    approvalAdapter.notifyDataSetChanged();
                }
            }
        });

        //抄送人
        grid_scroll_copy.setAdapter(copyAdapter);
        grid_scroll_copy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == (copyFriends.size())) {
                    //跳转到选择联系人界面
                    Intent intent = new Intent(getSelfActivity(), ChoosePeopleOfAddressActivity.class);
                    intent.putParcelableArrayListExtra("friends", friends);
                    startActivityForResult(intent, CODE_COPY_REQUEST);
//                    //点击了添加按钮
//                    copys.set(position,
//                            BitmapFactory.decodeResource(getResources(), R.drawable.iv_head));
//                    copys.add(bimapAdd);
//                    copyAdapter.notifyDataSetChanged();
                } else {
                    copyFriends.remove(position);
                    copyAdapter.notifyDataSetChanged();
                }
            }
        });
    }


    private void initData() {
        bimap = BitmapFactory.decodeResource(getResources(), R.drawable.add);
        mResults.add(bimap);

        grid_scroll_approval = (ScrollGridView) findViewById(R.id.grid_scroll_approval);
        grid_scroll_copy = (ScrollGridView) findViewById(R.id.grid_scroll_copy);
        //返回
        View view = findViewById(R.id.back);
        view.findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
        noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));

        copyAdapter = new GridViewCopyAdapter();
        approvalAdapter = new GridViewApprovalAdapter();
        adapter = new UpLoadPicAdapter(getSelfActivity(), mResults);
        adapter.update();

        findViewById(R.id.btn_submit_expense_approval).setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO) {
            if (resultCode == RESULT_OK) {
                ArrayList<String> result = data.getStringArrayListExtra(PhotoPickerActivity.KEY_RESULT);
                UpLoadPicUtil upLoadPicUtil = new UpLoadPicUtil(ctx, mResults, adapter);
                upLoadPicUtil.showResult(result);
            }
        }
        if (requestCode == CODE_APPROVAL_REQUEST &&
                resultCode == RESULT_OK) {
            Log.i("CMCC", "审批人返回");
            if (data != null) {
                //审批人选择
                ArrayList<FriendInfo> infos = data.getParcelableArrayListExtra("FriendInfo");
                //剔除重复的审批人数据
                if (!ObjectUtils.isNull(approvalFriends)) {
                    for (FriendInfo friendInfo : infos) {
                        if (!approvalFriends.contains(friendInfo)) {
                            approvalFriends.add(friendInfo);
                        }
                    }
                }

                //刷新数据
                approvalAdapter.notifyDataSetChanged();
            }
        }
        if (requestCode == CODE_COPY_REQUEST &&
                resultCode == RESULT_OK) {
            Log.i("CMCC", "抄送人返回");
            //抄送人选择
            ArrayList<FriendInfo> infos = data.getParcelableArrayListExtra("FriendInfo");
//            //判断审批人中是否包含抄送人
//            if (!ObjectUtils.isNull(approvalFriends)) {
//                for (FriendInfo friendInfo : infos) {
//                    if (approvalFriends.contains(friendInfo)) {
//                        ToastUtil.doToast(getSelfActivity(), "抄送人不能和审批人相同!!");
//                        return;
//                    }
//                }
//            }


            //剔除重复的审批人数据
            for (FriendInfo friendInfo : infos) {
                if (!copyFriends.contains(friendInfo)) {
                    copyFriends.add(friendInfo);
                }
            }
            //刷新数据
            copyAdapter.notifyDataSetChanged();

        }
    }


    protected void onRestart() {
        adapter.update();
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        int gvHeight = 0;
        if (adapter.getCount() < 5) {
            gvHeight = CommonUtils.dip2px(ctx, 60);
        } else if (adapter.getCount() < 9) {
            gvHeight = CommonUtils.dip2px(ctx, 125);
        } else {
            gvHeight = CommonUtils.dip2px(ctx, 190);
        }

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, gvHeight);
        noScrollgridview.setLayoutParams(lp);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit_expense_approval:
                //提交报销审批

                break;
        }
    }

    /**
     * description: 审批人
     * author LSW
     * date 2017/5/8 11:17
     * update 2017/5/8
     */
    private class GridViewApprovalAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return approvalFriends.size() + 1;
        }

        @Override
        public Object getItem(int position) {
            return approvalFriends.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            GridViewApprovalAdapter.ViewHolder holder = null;
            if (convertView == null) {
                holder = new GridViewApprovalAdapter.ViewHolder();
                convertView = LayoutInflater.from(getSelfActivity()).inflate(
                        R.layout.item_grid_approval, null);
                holder.round_head_image = (RoundImageView) convertView.findViewById(R.id.round_head_image);
                holder.txt_name = (TextView) convertView.findViewById(R.id.txt_name);
                convertView.setTag(holder);
            } else {
                holder = (GridViewApprovalAdapter.ViewHolder) convertView.getTag();
            }
            //holder.round_head_image.setImageBitmap(approvals.get(position));
            if (position == approvalFriends.size()) {
                //这里是加号
                Glide.with(getSelfActivity())
                        .load(R.drawable.add_people)
                        .centerCrop()
                        .transform(new CircleTransform(getSelfActivity()))
                        .crossFade()
                        .into(holder.round_head_image);
                //添加图标
                holder.txt_name.setVisibility(View.GONE);
            } else {
                Glide.with(getSelfActivity())
                        .load(approvalFriends.get(position).getMemberUrl())
                        .placeholder(R.drawable.iv_head)
                        .centerCrop()
                        .transform(new CircleTransform(getSelfActivity()))
                        .crossFade()
                        .into(holder.round_head_image);
                holder.txt_name.setVisibility(View.VISIBLE);
                holder.txt_name.setText(approvalFriends.get(position).getMemberName());
            }

            return convertView;
        }

        class ViewHolder {
            RoundImageView round_head_image;
            TextView txt_name;
        }
    }

    /**
     * description: 抄送
     * author LSW
     * date 2017/5/8 11:17
     * update 2017/5/8
     */
    private class GridViewCopyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return copyFriends.size() + 1;
        }

        @Override
        public Object getItem(int position) {
            return copyFriends.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            GridViewCopyAdapter.ViewHolder holder = null;
            if (convertView == null) {
                holder = new GridViewCopyAdapter.ViewHolder();
                convertView = LayoutInflater.from(getSelfActivity()).inflate(
                        R.layout.item_grid_approval, null);
                holder.round_head_image = (RoundImageView) convertView.findViewById(R.id.round_head_image);
                holder.txt_name = (TextView) convertView.findViewById(R.id.txt_name);
                convertView.setTag(holder);
            } else {
                holder = (GridViewCopyAdapter.ViewHolder) convertView.getTag();
            }
            if (position == copyFriends.size()) {
                //这里是加号
                Glide.with(getSelfActivity())
                        .load(R.drawable.add_people)
                        .centerCrop()
                        .transform(new CircleTransform(getSelfActivity()))
                        .crossFade()
                        .into(holder.round_head_image);
                //添加图标
                holder.txt_name.setVisibility(View.GONE);
            } else {
                Glide.with(getSelfActivity())
                        .load(copyFriends.get(position).getMemberUrl())
                        .centerCrop()
                        .transform(new CircleTransform(getSelfActivity()))
                        .crossFade()
                        .placeholder(R.drawable.iv_head)
                        .into(holder.round_head_image);
                holder.txt_name.setVisibility(View.VISIBLE);
                holder.txt_name.setText(copyFriends.get(position).getMemberName());
            }
            return convertView;
        }

        class ViewHolder {
            RoundImageView round_head_image;
            TextView txt_name;
        }
    }

    /**
     * 假数据，后面要删除
     */
    private ArrayList<FriendInfo> getData() {
        FriendInfo data01 = new FriendInfo();
        data01.setMemberName("汪浩");
        data01.setMemberId("1");
        data01.setMemberUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494660151&di=fc28cd4cd681bb1d70df6ff6654791ff&imgtype=jpg&er=1&src=http%3A%2F%2Fimgsrc.baidu.com%2Fforum%2Fw%253D580%2Fsign%3D8c03c118ca8065387beaa41ba7dda115%2Fc17fc0bf6c81800a06c8cd58b13533fa828b4759.jpg");

        FriendInfo data02 = new FriendInfo();
        data02.setMemberName("陆成宋");
        data02.setMemberId("2");
        data02.setMemberUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494065434200&di=7c53b18639aa82a8a58a296b9502d4ee&imgtype=0&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fzhidao%2Fwh%253D450%252C600%2Fsign%3D7048a12f9e16fdfad839ceea81bfa062%2F6a63f6246b600c3350e384cc194c510fd9f9a118.jpg");

        FriendInfo data03 = new FriendInfo();
        data03.setMemberName("123");
        data03.setMemberId("3");
        data03.setMemberUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494065546496&di=a861d2debdefd088f50efa05393043dc&imgtype=jpg&src=http%3A%2F%2Fimg3.imgtn.bdimg.com%2Fit%2Fu%3D893187487%2C386198762%26fm%3D214%26gp%3D0.jpg");

        FriendInfo data04 = new FriendInfo();
        data04.setMemberName("汪浩01");
        data04.setMemberId("4");
        data04.setMemberUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494065434199&di=85b82a89a5b9cb3403033e90dc2dc2a1&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fforum%2Fw%253D580%2Fsign%3Dddf0103f252dd42a5f0901a3333a5b2f%2Fa4a8805494eef01f30f35d93e0fe9925bd317da3.jpg");

        FriendInfo data05 = new FriendInfo();
        data05.setMemberName("陆成宋01");
        data05.setMemberId("5");
        data05.setMemberUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494065434198&di=83e45ffe0f07e6336bbbd1cdc284e9a5&imgtype=0&src=http%3A%2F%2Fwenwen.soso.com%2Fp%2F20140404%2F20140404111309-1793362574.jpg");

        FriendInfo data06 = new FriendInfo();
        data06.setMemberName("陆成宋02");
        data06.setMemberId("6");
        data06.setMemberUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494065730069&di=12540a26599230b583e0bf4f477cc8d7&imgtype=0&src=http%3A%2F%2Fwww.qxjlm.com%2Ftupians%2Fbd16072941.jpg");

        FriendInfo data07 = new FriendInfo();
        data07.setMemberName("陆成宋03");
        data07.setMemberId("7");
        data07.setMemberUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494065434198&di=2271e06f1c39be89ce18f3ab9746c7d1&imgtype=0&src=http%3A%2F%2Fwww.lsswgs.com%2Fqqwebhimgs%2Fuploads%2Fbd24449651.jpg");

        FriendInfo data08 = new FriendInfo();
        data08.setMemberName("陆成宋04");
        data08.setMemberId("8");
        data08.setMemberUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494065662993&di=1d4fabe377e894277d005e17818ea64b&imgtype=jpg&src=http%3A%2F%2Fimg3.imgtn.bdimg.com%2Fit%2Fu%3D575752541%2C1102211525%26fm%3D214%26gp%3D0.jpg");

        FriendInfo data09 = new FriendInfo();
        data09.setMemberName("陆成宋05");
        data09.setMemberId("9");
        data09.setMemberUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494065434197&di=9043dfda20c52ecfa1676e3999658669&imgtype=0&src=http%3A%2F%2Fwenwen.soso.com%2Fp%2F20111030%2F20111030173922-1579981974.jpg");

        FriendInfo data10 = new FriendInfo();
        data10.setMemberName("陆成宋06");
        data10.setMemberId("10");
        data10.setMemberUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494065434197&di=af5f1a36863ac751b71b1e167e57a8e7&imgtype=0&src=http%3A%2F%2Fwenwen.soso.com%2Fp%2F20131201%2F20131201114242-1190841548.jpg");

        FriendInfo data11 = new FriendInfo();
        data11.setMemberName("陆成宋07");
        data11.setMemberId("11");
        data11.setMemberUrl("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3609186921,1453144627&fm=23&gp=0.jpg");

        FriendInfo data12 = new FriendInfo();
        data12.setMemberName("陆成宋08");
        data12.setMemberId("12");
        data12.setMemberUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494065730070&di=b4ae9186fcb1795ee5bec334ec893997&imgtype=0&src=http%3A%2F%2Fwww.qxjlm.com%2Ftupians%2Fbd13706313.jpg");
        friends.add(data01);
        friends.add(data02);
        friends.add(data03);
        friends.add(data04);
        friends.add(data05);
        friends.add(data06);
        friends.add(data07);
        friends.add(data08);
        friends.add(data09);
        friends.add(data10);
        friends.add(data11);
        friends.add(data12);
        return friends;
    }
}
