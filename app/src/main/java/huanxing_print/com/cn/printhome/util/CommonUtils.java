/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package huanxing_print.com.cn.printhome.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;

import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import huanxing_print.com.cn.printhome.R;
import huanxing_print.com.cn.printhome.constant.ConFig;
import huanxing_print.com.cn.printhome.util.FileUtils.FileDeleteCallback;

public class CommonUtils {
	private static final String KEY = "GjZbaGry7m7sfTy0WllHvfyH";
	private final static String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDO++7KywM8g/51KQacb4GRC7fJDZLU1KdlZJwB6ROy7As7bSr8H2q4INtOu12uBNPVvozOVdoanybgvTiISyvj1pw8a5/fgYAbdiUam3FWkoKCI9v2rxJMEhvql+aMws7diCXqovnMgT3AO0hIrTK5o9+dfvpveeTbkPDhGSkSlQIDAQAB";
	/**
	 * 检测网络是否可用?
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetWorkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}

		return false;
	}

	public static void autoBindBaiduYunTuiSong(Context context) {

		// if (!HttpUrls.bind) {
		PushManager.startWork(context, PushConstants.LOGIN_TYPE_API_KEY, KEY);
		// }
	}

	/**
	 * 检测Sdcard是否存在
	 * 
	 * @return
	 */
	public static boolean isExitsSdcard() {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED))
			return true;
		else
			return false;
	}

	static String getString(Context context, int resId) {
		return context.getResources().getString(resId);
	}

	public static String getTopActivity(Context context) {
		ActivityManager manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);

		if (runningTaskInfos != null)
			return runningTaskInfos.get(0).topActivity.getClassName();
		else
			return "";
	}

	/**
	 * 判断邀请码格式只能为字母和数字
	 * 
	 */
	public static boolean isInviteCode(String inviteCode) {
		Pattern p = Pattern.compile("^[0-9a-zA-Z]{6}$");
		Matcher m = p.matcher(inviteCode);
		return m.matches();
	}

	/**
	 * 判断是否为手机号
	 * 
	 */
//	public static boolean isPhone(String phone) {
//		Pattern p = Pattern
//				.compile("^((13[0-9])|(14[0-9])|(15[^4,\\D])|(17[0-9])|(18[0-9]))\\d{8}$");
//		Matcher m = p.matcher(phone);
//		return m.matches();
//	}
	public static boolean isPhone(String phone) {
	Pattern p = Pattern.compile("^(1[0-9])\\d{9}$");
	Matcher m = p.matcher(phone);
	return m.matches();
}

	// MD5加密
	public final static String MD5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'A', 'B', 'C', 'D', 'E', 'F' };
		try {
			byte[] btInput = s.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static int isYesterday(String date) {
		int day = 0;
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date nowtime = new Date();// 当前时间
			Date d1 = sdf.parse(sdf.format(nowtime));
			Date d2 = sdf.parse(date);// 传进的时间
			long cha = d2.getTime() - d1.getTime();
			day = (int) (cha / (24 * 60 * 60 * 1000));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return day;
	}

	public static double getDistance(LatLng strLatLng, LatLng endLatLng) {
		double distance = 0;

		try {
			distance = DistanceUtil.getDistance(strLatLng, endLatLng);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return distance;
	}

	/** 
     * 判断 用户是否安装微信客户端 
    */  
   public static boolean isWeixinAvilible(Context context) {  
       final PackageManager packageManager = context.getPackageManager();// 获取packagemanager  
       List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息  
       if (pinfo != null) {  
           for (int i = 0; i < pinfo.size(); i++) {  
               String pn = pinfo.get(i).packageName;  
               if (pn.equals("com.tencent.mm")) {  
                   return true;  
               }  
           }  
       }  
       return false;  
   }  
	/** * 根据手机的分辨率从dp 的单位 转成为px(像素) */ 
   public static int dip2px(Context context, float dpValue) { 
           final float scale = context.getResources().getDisplayMetrics().density; 
           return (int) (dpValue * scale + 0.5f); 
   } 

   /** * 根据手机的分辨率从px(像素) 的单位 转成为dp */ 
   public static int px2dip(Context context, float pxValue) { 
           final float scale = context.getResources().getDisplayMetrics().density; 
           return (int) (pxValue / scale + 0.5f); 
   } 

/*	*//**
	 * 将日期信息转换成今天、明天、后天、星期
	 * 
	 * @param date
	 * @return
	 *//*
	public static String getDateDetail(String date) {
		Calendar today = Calendar.getInstance();
		Calendar target = Calendar.getInstance();

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date d1 = new Date();// 当前时间
		String nowtime = String.valueOf(d1.getTime());
		try {
			today.setTime(df.parse(nowtime));
			today.set(Calendar.HOUR, 0);
			today.set(Calendar.MINUTE, 0);
			today.set(Calendar.SECOND, 0);
			target.setTime(df.parse(date));
			target.set(Calendar.HOUR, 0);
			target.set(Calendar.MINUTE, 0);
			target.set(Calendar.SECOND, 0);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		long intervalMilli = target.getTimeInMillis() - today.getTimeInMillis();
		int xcts = (int) (intervalMilli / (24 * 60 * 60 * 1000));
		return showDateDetail(xcts, target);

	}*/

	/**
	 * 将日期差显示为日期或者星期
	 *
	 * @return
	 *//*
	private static String showDateDetail(int xcts, Calendar target) {
		switch (xcts) {
		case 0:
			return Constant.TODAY;
		case 1:
			return Constant.TOMORROW;
		case 2:
			return Constant.AFTER_TOMORROW;
		case -1:
			return Constant.YESTERDAY;
		case -2:
			return Constant.BEFORE_YESTERDAY;
		default:
			int dayForWeek = 0;
			dayForWeek = target.get(Calendar.DAY_OF_WEEK);
			switch (dayForWeek) {
			case 1:
				return Constant.SUNDAY;
			case 2:
				return Constant.MONDAY;
			case 3:
				return Constant.TUESDAY;
			case 4:
				return Constant.WEDNESDAY;
			case 5:
				return Constant.THURSDAY;
			case 6:
				return Constant.FRIDAY;
			case 7:
				return Constant.SATURDAY;
			default:
				return null;
			}

		}
	}*/

	public static String changeTime(int value) {
		return value < 10 ? ("0" + value) : (value + "");
	}
	/**
	 * 设置银行卡号中间为"*"号
	 */
	public static String changeBankCardNO(String bankCardNO) {
		 if(!TextUtils.isEmpty(bankCardNO) ){
			 int bankCardNOlenth = bankCardNO.length()-4;
			 bankCardNO = bankCardNO.substring(0, 4)+"****"+bankCardNO.substring(bankCardNOlenth);
	     }
		return bankCardNO;
	}
	
	/**
	 * 设置银行卡号中间为"*"号
	 */
	public static String changePhone(String phone) {
		 if(!TextUtils.isEmpty(phone) ){
			 int bankCardNOlenth = phone.length()-4;
			 phone = phone.substring(0, 3)+"****"+phone.substring(bankCardNOlenth);
	     }
		return phone;
	}
	
	/**
	 * 设置我的钱包<收入明细>大客户订单号中间为"*"号
	 */
	public static String changeOrderCode(String orderCode) {
		 if(!TextUtils.isEmpty(orderCode) ){
			 int bankCardNOlenth = orderCode.length()-5;
			 orderCode = orderCode.substring(0, 3)+"***"+orderCode.substring(bankCardNOlenth);
	     }
		return orderCode;
	}
	/**
	 * 铃声提示
	 */
	public static void mediaplayer(Context context) {
		MediaPlayer mp = new MediaPlayer();
		try {
			Ringtone rt = RingtoneManager.getRingtone(context, RingtoneManager
					.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
			rt.play();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 判定输入汉字
	 * 
	 * 
	 * 
	 * @param c
	 * 
	 * @return
	 */

	public static boolean isChinese(char c) {

		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);

		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {

			return true;

		}

		return false;

	}

	/**
	 * 
	 * 检测String是否全是中文
	 * 
	 * 
	 * 
	 * @param name
	 * 
	 * @return
	 */

	public static boolean checkNameChese(String name) {

		boolean res = true;

		char[] cTemp = name.toCharArray();

		for (int i = 0; i < name.length(); i++) {

			if (!isChinese(cTemp[i])) {

				res = false;

				break;

			}

		}

		return res;

	}
	
	
	/**
	 * 
	 * 检测Str是否有中文
	 * 
	 * 
	 * 
	 * @param name
	 * 
	 * @return
	 */

	public static boolean checkStrChese(String name) {

		boolean res = false;

		char[] cTemp = name.toCharArray();

		for (int i = 0; i < name.length(); i++) {

			if (isChinese(cTemp[i])) {

				res = true;

				break;

			}

		}

		return res;

	}

	public static void initSystemBar(Activity activity) {

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

			setTranslucentStatus(activity, true);

		}

		SystemBarTintManager tintManager = new SystemBarTintManager(activity);

		tintManager.setStatusBarTintEnabled(true);

		// 使用颜色资源

		tintManager.setStatusBarTintResource(R.color.white);

	}
	public static void initSystemBarGreen(Activity activity) {

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

			setTranslucentStatus(activity, true);

		}

		SystemBarTintManager tintManager = new SystemBarTintManager(activity);

		tintManager.setStatusBarTintEnabled(true);

		// 使用颜色资源

		tintManager.setStatusBarTintResource(R.color.yellow);

	}

	@SuppressLint("InlinedApi")
	private static void setTranslucentStatus(Activity activity, boolean on) {

		Window win = activity.getWindow();

		WindowManager.LayoutParams winParams = win.getAttributes();

		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;

		if (on) {

			winParams.flags |= bits;

		} else {

			winParams.flags &= ~bits;

		}

		win.setAttributes(winParams);

	}

    /**
     * 图片圆形处理
     * 
     * @param img
     * @return
     */
    public static Bitmap bitmapCircle(Bitmap img)
    {
        if (img == null)
        {
            return null;
        }
        int w = img.getWidth();
        int h = img.getHeight();
        int size = w > h ? h : w;
        Paint p = new Paint();
        p.setColor(Color.WHITE);
        p.setAntiAlias(false);
        Bitmap bitmap = Bitmap.createBitmap(size, size, Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawCircle(size / 2, size / 2, size / 2, p);
        p.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(img, 0, 0, p);
        p.setXfermode(null);
        p.setStrokeWidth(10);
        p.setStyle(Paint.Style.STROKE);
        
        return bitmap;
    }
//	public static String sig(String value) {
//		
//		try {
////			value = DigestUtils.shaHex(Base64.encode(value.getBytes()));
//			value = new String(Hex.encodeHex(DigestUtils.sha(Base64.encode(value.getBytes()))));
//			return	Base64.encode(RSAEncrypt.encrypt(RSAEncrypt.loadPublicKeyByStr(PUBLIC_KEY), value.getBytes()));
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return	null;
//		}
//		
//	}
    
    public static void clearCache() {
		FileUtils.deleteFile(ConFig.IMG_CACHE_PATH, new FileDeleteCallback() {
			@Override
			public void result(int state) {

			}
		}, true);
	}
	
}
