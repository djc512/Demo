package huanxing_print.com.cn.printhome.log;

import android.text.TextUtils;
import android.util.Log;

import huanxing_print.com.cn.printhome.constant.ConFig;
import huanxing_print.com.cn.printhome.util.ObjectUtils;

public class Logger {

	public static String customTagPrefix = "";

	private Logger() {
	}

	public static boolean allowD = true;
	public static boolean allowE = true;
	public static boolean allowI = true;
	public static boolean allowV = true;
	public static boolean allowW = true;
	public static boolean allowWtf = true;

	private static String generateTag(StackTraceElement caller) {
//		isDebug();
		String tag = "%s.%s(L:%d)";
		String callerClazzName = caller.getClassName();
		callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
		tag = String.format(tag, callerClazzName, caller.getMethodName(), caller.getLineNumber());
		tag = TextUtils.isEmpty(customTagPrefix) ? tag : customTagPrefix + ":" + tag;
		return tag;
	}

	private static void isDebug() {
		switch (ConFig.CURRENT_ENVIRONMENT) {
		case RELEASE:
			allowD = false;
			allowE = false;
			allowI = false;
			allowV = false;
			allowW = false;
			allowWtf = false;
			break;

		default:
			break;
		}
	}

	public static CustomLogger customLogger;

	public interface CustomLogger {
		void d(String tag, String content);

		void d(String tag, String content, Throwable tr);

		void e(String tag, String content);

		void e(String tag, String content, Throwable tr);

		void i(String tag, String content);

		void i(String tag, String content, Throwable tr);

		void v(String tag, String content);

		void v(String tag, String content, Throwable tr);

		void w(String tag, String content);

		void w(String tag, String content, Throwable tr);

		void w(String tag, Throwable tr);

		void wtf(String tag, String content);

		void wtf(String tag, String content, Throwable tr);

		void wtf(String tag, Throwable tr);
	}

	public static void d(Object content) {
		if (!allowD)
			return;
		StackTraceElement caller = getCallerStackTraceElement();
		String tag = generateTag(caller);

		if (customLogger != null) {
			customLogger.d(tag, ObjectUtils.object2String(content));
		} else {
			Log.d(tag, ObjectUtils.object2String(content));
		}
	}

	public static void d(String content, Throwable tr) {
		if (!allowD)
			return;
		StackTraceElement caller = getCallerStackTraceElement();
		String tag = generateTag(caller);

		if (customLogger != null) {
			customLogger.d(tag, content, tr);
		} else {
			Log.d(tag, content, tr);
		}
	}

	public static void e(String content) {
		if (!allowE)
			return;
		StackTraceElement caller = getCallerStackTraceElement();
		String tag = generateTag(caller);

		if (customLogger != null) {
			customLogger.e(tag, content);
		} else {
			Log.e(tag, content);
		}
	}

	public static void e(String content, Throwable tr) {
		if (!allowE)
			return;
		StackTraceElement caller = getCallerStackTraceElement();
		String tag = generateTag(caller);

		if (customLogger != null) {
			customLogger.e(tag, content, tr);
		} else {
			Log.e(tag, content, tr);
		}
	}

	public static void i(Object content) {
		if (!allowI)
			return;
		StackTraceElement caller = getCallerStackTraceElement();
		String tag = generateTag(caller);

		if (customLogger != null) {
			customLogger.i(tag, ObjectUtils.object2String(content));
		} else {
			Log.i(tag, ObjectUtils.object2String(content));
		}
	}

	public static void i(String content, Throwable tr) {
		if (!allowI)
			return;
		StackTraceElement caller = getCallerStackTraceElement();
		String tag = generateTag(caller);

		if (customLogger != null) {
			customLogger.i(tag, content, tr);
		} else {
			Log.i(tag, content, tr);
		}
	}

	public static void v(String content) {
		if (!allowV)
			return;
		StackTraceElement caller = getCallerStackTraceElement();
		String tag = generateTag(caller);

		if (customLogger != null) {
			customLogger.v(tag, content);
		} else {
			Log.v(tag, content);
		}
	}

	public static void v(String content, Throwable tr) {
		if (!allowV)
			return;
		StackTraceElement caller = getCallerStackTraceElement();
		String tag = generateTag(caller);

		if (customLogger != null) {
			customLogger.v(tag, content, tr);
		} else {
			Log.v(tag, content, tr);
		}
	}

	public static void w(String content) {
		if (!allowW)
			return;
		StackTraceElement caller = getCallerStackTraceElement();
		String tag = generateTag(caller);

		if (customLogger != null) {
			customLogger.w(tag, content);
		} else {
			Log.w(tag, content);
		}
	}

	public static void w(String content, Throwable tr) {
		if (!allowW)
			return;
		StackTraceElement caller = getCallerStackTraceElement();
		String tag = generateTag(caller);

		if (customLogger != null) {
			customLogger.w(tag, content, tr);
		} else {
			Log.w(tag, content, tr);
		}
	}

	public static void w(Throwable tr) {
		if (!allowW)
			return;
		StackTraceElement caller = getCallerStackTraceElement();
		String tag = generateTag(caller);

		if (customLogger != null) {
			customLogger.w(tag, tr);
		} else {
			Log.w(tag, tr);
		}
	}

	public static void wtf(String content) {
		if (!allowWtf)
			return;
		StackTraceElement caller = getCallerStackTraceElement();
		String tag = generateTag(caller);

		if (customLogger != null) {
			customLogger.wtf(tag, content);
		} else {
			Log.wtf(tag, content);
		}
	}

	public static void wtf(String content, Throwable tr) {
		if (!allowWtf)
			return;
		StackTraceElement caller = getCallerStackTraceElement();
		String tag = generateTag(caller);

		if (customLogger != null) {
			customLogger.wtf(tag, content, tr);
		} else {
			Log.wtf(tag, content, tr);
		}
	}

	public static void wtf(Throwable tr) {
		if (!allowWtf)
			return;
		StackTraceElement caller = getCallerStackTraceElement();
		String tag = generateTag(caller);

		if (customLogger != null) {
			customLogger.wtf(tag, tr);
		} else {
			Log.wtf(tag, tr);
		}
	}

	public static StackTraceElement getCallerStackTraceElement() {
		return Thread.currentThread().getStackTrace()[4];
	}

}
