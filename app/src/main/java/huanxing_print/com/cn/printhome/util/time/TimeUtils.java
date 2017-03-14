package huanxing_print.com.cn.printhome.util.time;

import java.text.SimpleDateFormat;
import java.util.Date;

import huanxing_print.com.cn.printhome.log.Logger;

public class TimeUtils {
	public static long beginTime;

	public static long endTime;

	public static long beginTime() {
		beginTime = System.currentTimeMillis();
		return beginTime;
	}

	public static long endTime() {
		endTime = System.currentTimeMillis();
		return endTime;
	}

	public static long subTime() {

		Logger.d("subTime:" + (endTime - beginTime));
		return endTime - beginTime;
	}

	public static long currentTime() {
		return System.currentTimeMillis();
	}

	public static String getTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}

	public static String getTime(String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date());
	}

}
