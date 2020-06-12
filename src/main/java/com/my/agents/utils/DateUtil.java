package com.my.agents.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	public static final String FORMAT1 = "yyyy-MM-dd HH:mm:ss";
	public static final String FORMAT2 = "yyyy-MM-dd";
	public static final String FORMAT3 = "yyyy-MM";

	/**
	 * 时间转换为字符串
	 * 
	 * @param date
	 *            String
	 * @return Date
	 */
	public static String getFormattedDateStr(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String dateStr = sdf.format(date);
		return dateStr;
	}

}
