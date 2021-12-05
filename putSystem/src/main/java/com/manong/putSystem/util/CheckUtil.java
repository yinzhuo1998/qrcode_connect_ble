package com.manong.putSystem.util;


import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.ACTIVITY_SERVICE;

public class CheckUtil {

	/**
	 * 判断数组是否为空
	 * S2T3Fo4
	 * @paramr
	 * @return true 为空 false 不为空
	 */
	public static boolean IsEmpty(Object[] ObjIn) {

		return (ObjIn == null || ObjIn.length == 0);
	}

	//
	// /**
	// * 判断一个字符串是否为空
	// *
	// * @param str
	// * @return
	// */
	// public static boolean IsEmpty(String str) {
	// return (str == null || str.length() == 0);
	// }

	/**
	 * 判断一个字符串 是否null,且内容不为null，"" ,"  "等
	 * 
	 * @param str
	 * @return
	 */
	public static boolean IsEmpty(String str) {
		return (str == null || str.trim().length() == 0 || str.trim().equalsIgnoreCase("null"));
	}

	/**
	 * 验证一个对象是否为空
	 * 
	 * @param o
	 * @return
	 */
	public static boolean IsEmpty(Object o) {
		return (o == null);
	}

	/**
	 * 判断数组是否为空
	 * 
	 * @param
	 * @return true 为空 false 不为空
	 */
	@SuppressWarnings("rawtypes")
	public static boolean IsEmpty(Collection colIn) {

		return (colIn == null || colIn.isEmpty());
	}

	/**
	 * 判断Map是否为空
	 * 
	 * @param
	 * @return true 为空 false 不为空
	 */
	@SuppressWarnings("rawtypes")
	public static boolean IsEmpty(Map mapIn) {

		return (mapIn == null || mapIn.isEmpty());
	}

	public static boolean IsEmpty(@SuppressWarnings("rawtypes") List list) {

		return (list == null || list.isEmpty());
	}

	/**
	 * 判断字符串是否为空或者null
	 * 
	 * @param strParam
	 *            验证参数
	 * @return 为空或null时返回True
	 */
	public static boolean isBlankOrNull(String strParam) {
		return (strParam == null) || (strParam.trim().length() == 0);
	}

	/**
	 * 判断是否为整数
	 * 
	 * @param strNum
	 *            待查字符串
	 * @return 是则返回true
	 */
	public static boolean isInteger(String strNum) {

		// 空串不为数字
		if (null == strNum || "".equals(strNum))
			return false;
		Pattern pattern = Pattern.compile("^-?\\d+$");
		Matcher isNum = pattern.matcher(strNum);
		if (isNum.matches()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断是否为（非零的）正整数
	 * 
	 * @param strNum
	 *            待查字符串
	 * @return 是则返回true
	 */
	public static boolean isPosInteger(String strNum) {

		// 空串不为数字
		if (null == strNum || "".equals(strNum))
			return false;
		Pattern pattern = Pattern.compile("^\\+?[1-9][0-9]*$");
		Matcher isNum = pattern.matcher(strNum);
		if (isNum.matches()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断是否为（非零的）负整数
	 * 
	 * @param strNum
	 *            待查字符串
	 * @return 是则返回true
	 */
	public static boolean isNegInteger(String strNum) {

		// 空串不为数字
		if (null == strNum || "".equals(strNum))
			return false;
		Pattern pattern = Pattern.compile("^\\-[1-9][0-9]*$");
		Matcher isNum = pattern.matcher(strNum);
		if (isNum.matches()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断是否为非负整数（正整数 + 0）
	 * 
	 * @param strNum
	 *            待查字符串
	 * @return 是则返回true
	 */
	public static boolean isNonNegInteger(String strNum) {

		// 空串不为数字
		if (null == strNum || "".equals(strNum))
			return false;
		Pattern pattern = Pattern.compile("^\\d+$");
		Matcher isNum = pattern.matcher(strNum);
		if (isNum.matches()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断是否为非正整数（负整数 + 0）
	 * 
	 * @param strNum
	 *            待查字符串
	 * @return 是则返回true
	 */
	public static boolean isNonPosInteger(String strNum) {

		// 空串不为数字
		if (null == strNum || "".equals(strNum))
			return false;
		Pattern pattern = Pattern.compile("^((-\\d+)|(0+))$");
		Matcher isNum = pattern.matcher(strNum);
		if (isNum.matches()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断是否为浮点数
	 * 
	 * @param strNum
	 *            待查字符串
	 * @return 是则返回true
	 */
	public static boolean isFloat(String strNum) {

		// 空串不为数字
		if (null == strNum || "".equals(strNum))
			return false;
		Pattern pattern = Pattern.compile("^(-?\\d+)(\\.\\d+)?");
		Matcher isNum = pattern.matcher(strNum);
		if (isNum.matches()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断是否为非负浮点数（正浮点数 + 0）
	 * 
	 * @param strNum
	 *            待查字符串
	 * @return 是则返回true
	 */
	public static boolean isNonNegFloat(String strNum) {

		// 空串不为数字
		if (null == strNum || "".equals(strNum))
			return false;
		Pattern pattern = Pattern.compile("^\\d+(\\.\\d+)?$");
		Matcher isNum = pattern.matcher(strNum);
		if (isNum.matches()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断是否为正浮点数
	 * 
	 * @param strNum
	 *            待查字符串
	 * @return 是则返回true
	 */
	public static boolean isPosFloat(String strNum) {

		// 空串不为数字
		if (null == strNum || "".equals(strNum))
			return false;
		Pattern pattern = Pattern.compile("^(([0-9]+\\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\\.[0-9]+)|([0-9]*[1-9][0-9]*))$");
		Matcher isNum = pattern.matcher(strNum);
		if (isNum.matches()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断是否为非正浮点数（负浮点数 + 0）
	 * 
	 * @param strNum
	 *            待查字符串
	 * @return 是则返回true
	 */
	public static boolean isNonPosFloat(String strNum) {

		// 空串不为数字
		if (null == strNum || "".equals(strNum))
			return false;
		Pattern pattern = Pattern.compile("^((-\\d+(\\.\\d+)?)|(0+(\\.0+)?))$");
		Matcher isNum = pattern.matcher(strNum);
		if (isNum.matches()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断是否为负浮点数
	 * 
	 * @param strNum
	 *            待查字符串
	 * @return 是则返回true
	 */
	public static boolean isNegFloat(String strNum) {

		// 空串不为数字
		if (null == strNum || "".equals(strNum))
			return false;
		Pattern pattern = Pattern.compile("^(-(([0-9]+\\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\\.[0-9]+)|([0-9]*[1-9][0-9]*)))$");
		Matcher isNum = pattern.matcher(strNum);
		if (isNum.matches()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断是否为数值 包含 正负号和小数点
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		char c;
		int pointNum = 0;
		for (int i = str.length(); --i >= 0;) {
			c = str.charAt(i);
			// 是否为数字
			if ((c <= 0x0039 && c >= 0x0030) == false)
				// 是否为小数点
				if (c == 0x002e) {
					// 是否只有一个小数点
					if (pointNum == 0) {
						pointNum++;
					} else {
						// 超过一个小数点时出错
						return false;
					}
				} else {
					// 是否为减号或加号且在最开头
					if (c != 0x002d && c != 0x002b || i != 0) {
						// 都不是时出错
						return false;
					}
				}
		}
		return true;
	}

	/**
	 * 判断是否为日期
	 * 
	 * @param strDate
	 *            需要解析的日期字符串
	 * @return 合法日期返回true
	 */
	public static boolean isDate(String strDate) {
		if (null == strDate || "".equals(strDate))
			return false;
		Pattern pattern = Pattern
				.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
		Matcher m = pattern.matcher(strDate);
		if (m.matches()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断是否日期一是日期二的未来日或同一时刻 <br />
	 * <br />
	 * 
	 * @param iType
	 *            日期类型 <br />
	 *            ==================================================== <br />
	 *            Type 格式 <br />
	 *            0 yyyyMMddHHmmss <br />
	 *            1 yyyyMMdd HH:mm:ss <br />
	 *            2 yyyyMMdd HH:mm <br />
	 *            3 yyyyMMdd <br />
	 *            ==================================================== <br />
	 * @param strDate1
	 *            日期字符串一 <br />
	 * @param strDate2
	 *            日期字符串二 <br />
	 * @return 是则返回true <br />
	 */
	@SuppressLint("SimpleDateFormat")
	public static boolean checkDateDiff(int iType, String strDate1, String strDate2) throws Exception {

		// 将字符串转为日期类型
		SimpleDateFormat oriDateFormat = new SimpleDateFormat();

		switch (iType) {
		case 0:
			oriDateFormat.applyPattern("yyyyMMddHHmmss");
			break;
		case 1:
			oriDateFormat.applyPattern("yyyyMMdd HH:mm:ss");
			break;
		case 2:
			oriDateFormat.applyPattern("yyyyMMdd HH:mm");
			break;
		case 3:
			oriDateFormat.applyPattern("yyyyMMdd");
			break;
		default:
			oriDateFormat.applyPattern("yyyyMMdd");
		}
		Date date1;
		Date date2;

		try {
			date1 = oriDateFormat.parse(strDate1);
		} catch (Exception e) {
			Log.e("checkDateDiff", "无法将[" + strDate1 + "]转为[yyyyMMdd HH:mm:ss]格式的日期", e);
			throw e;
		}

		try {
			date2 = oriDateFormat.parse(strDate2);
			return (date1.getTime() - date2.getTime()) >= 0;
		} catch (Exception e1) {
			Log.e("checkDateDiff", "无法将[" + strDate2 + "]转为[yyyyMMdd HH:mm:ss]格式的日期", e1);
			throw e1;
		}
	}

	/**
	 * 判断是否为指定格式的日期
	 * 
	 * @param strDate
	 *            需要解析的日期字符串
	 * @param pattern
	 *            日期字符串的格式，默认为“yyyy-MM-dd”的形式
	 * @return 合法日期返回true
	 */
	@SuppressLint("SimpleDateFormat")
	public static boolean checkDateFormat(String strDate, String pattern) {

		// 将字符串转为日期类型
		SimpleDateFormat sdf = new SimpleDateFormat();
		if (CheckUtil.isBlankOrNull(pattern)) {
			sdf.applyPattern("yyyy-MM-dd");
		}
		sdf.setLenient(false);
		try {
			if (strDate.length() == sdf.toPattern().length()) {
				sdf.parse(strDate);
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * 判断字符串长度是否合法
	 * 
	 * @param str
	 *            待检查字符串
	 * @param length
	 *            要求的长度
	 * @return 符合长度要求返回true
	 */
	public static boolean checkStrLength(String str, int length) {
		if (null == str)
			str = "";
		return str.length() <= length;
	}

	/**
	 * 判断字符串范围是否合法
	 * 
	 * @param strNum
	 *            待检查字符串
	 * @param min
	 *            最小值
	 * @param max
	 *            最大值
	 * @return 符合范围返回true
	 */
	public static boolean checkStrRange(String strNum, int min, int max) {
		if (null == strNum)
			strNum = "";
		int intNum;
		try {
			intNum = Integer.parseInt(strNum);
			return intNum >= min && intNum <= max;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 判断性别是否与身份证号中的信息匹配
	 * 
	 * @param strGender
	 *            性别 0：男 1：女
	 * @param strId
	 *            身份证号码
	 * @return 是则返回true
	 */
	public static boolean checkGenderInIDCard(String strGender, String strId) {
		if (null == strGender || null == strId || "".equals(strGender) || strId.length() < 15)

			return false;

		// 获取性别
		String id17 = strId.substring(16, 17);
		String strIDGender = "";
		if (Integer.parseInt(id17) % 2 != 0) {
			strIDGender = "0";
		} else {
			strIDGender = "1";
		}
		return strIDGender.equals(strGender);
	}

	/**
	 * 校验邮箱是否合法
	 * 
	 * @param email
	 *            箱邮
	 * @return true:合法 false:不合法
	 */
	public static boolean isEmailValid(String email) {
		Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}");
		Matcher m = p.matcher(email);
		boolean isValid = m.matches();
		return isValid;
	}

	/**
	 * 电话号码与手机号码同时验证
	 * 
	 * @param phoneNumber
	 * @return true:合法 false:不合法 区号+座机
	 *         号码+分机号码：regexp="^(0[0-9]{2,3}\-)?([2-9][0-9]{6,7})+(\-[0-9]{1,4})?$
	 *         "
	 * 
	 *         手机(中国移动手机号码)：regexp=
	 *         "^((\(\d{3}\))|(\d{3}\-))?13[456789]\d{8}|15[89]\d{8}"
	 * 
	 *         所有手机号码：regexp=
	 *         "^((\(\d{3}\))|(\d{3}\-))?13[0-9]\d{8}|15[89]\d{8}"(新添加了158,159两个
	 *         号 段 )
	 * 
	 */
	public static boolean isPhoneNumberValid(String phoneNumber) {
		boolean isValid = false;
		/*
		 * 可接受的电话格式有: * ^\\(? : 可以使用 "(" 作为开头 * (\\d{3}): 紧接着三个数字 * \\)? :
		 * 可以使用")"接续 * [- ]? : 在上述格式后可以使用具选择性的 "-". * (\\d{3}) : 再紧接着三个数字 * [-
		 * ]? : 可以使用具选择性的 "-" 接续. * (\\d{4})$: 以四个数字结束. * 可以比对下列数字格式: *
		 * (123)456-7890, 123-456-7890, 1234567890, (123)-456-7890
		 */
		String expression1 = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$";
		/*
		 * 可接受的电话格式有: * ^\\(? : 可以使用 "(" 作为开头 * (\\d{2}): 紧接着两个数字 * \\)? :
		 * 可以使用")"接续 * [- ]? : 在上述格式后可以使用具选择性的 "-". * (\\d{4}) : 再紧接着四个数字 * [-
		 * ]? : 可以使用具选择性的 "-" 接续. * (\\d{4})$: 以四个数字结束. * 可以比对下列数字格式: *
		 * (123)456-7890, 123-456-7890, 1234567890, (123)-456-7890
		 */
		String expression2 = "^[1][3,4,7,8,5]+\\d{9}";// "^\\(?(\\d{2})\\)?[- ]?(\\d{4})[- ]?(\\d{4})$";
		String expression3 = "\\d{4}-\\d{8}|\\d{4}-\\d{7}|\\d(3)-\\d(8)";
		String expression4 = "^((\\+86)|(86))?(13)\\d{9}$";// 实现手机号前带86或是+86的情况:^((\+86)|(86))?(13)\d{9}
		String expression5 = "^\\d{3}-\\d{8}|\\d{4}-\\d{7}$";// var m
																// =/^\d{3}-\d{8}|\d{4}-\d{7}$/;//验证电话号码为7-8位数字并带有区号
		String expression6 = "((\\d{11})|^((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1})|(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1}))$) ";
		String expression7 = "(^(\\d{3,4}-)?\\d{7,8})$|(13[0-9]{9})";

		String expression8 = "^(0[0-9]{2,3})?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?$";// 区号+座机号码-分机号码：去区号掉中间-校验 分机带上-

		// String
		// expression8="^(0[0-9]{2,3}\\-)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?$";//区号+座机号码+分机号码：expression8
		// 带-

		// 手机(中国移动手机号码)：regexp="^((\(\d{3}\))|(\d{3}\-))?13[456789]\d{8}|15[89]\d{8}"

		// 所有手机号码：regexp="^((\(\d{3}\))|(\d{3}\-))?13[0-9]\d{8}|15[89]\d{8}"(新添加了158,159两个号段)

		CharSequence phoneNumberChar = phoneNumber;

		Pattern pattern1 = Pattern.compile(expression1);
		Matcher matcher1 = pattern1.matcher(phoneNumberChar);
		Pattern pattern2 = Pattern.compile(expression2);
		Matcher matcher2 = pattern2.matcher(phoneNumberChar);

		Pattern pattern3 = Pattern.compile(expression3);
		Matcher matcher3 = pattern3.matcher(phoneNumberChar);

		Pattern pattern4 = Pattern.compile(expression4);
		Matcher matcher4 = pattern4.matcher(phoneNumberChar);

		Pattern pattern5 = Pattern.compile(expression5);
		Matcher matcher5 = pattern5.matcher(phoneNumberChar);

		Pattern pattern6 = Pattern.compile(expression6);
		Matcher matcher6 = pattern6.matcher(phoneNumberChar);

		Pattern pattern7 = Pattern.compile(expression7);
		Matcher matcher7 = pattern7.matcher(phoneNumberChar);

		Pattern pattern8 = Pattern.compile(expression8);
		Matcher matcher8 = pattern8.matcher(phoneNumberChar);

		if (matcher1.matches() || matcher2.matches() || matcher3.matches() || matcher4.matches() || matcher5.matches() || matcher6.matches() || matcher7.matches() || matcher8.matches()) {
			isValid = true;
		}
		return isValid;
	}

	/**
	 * 法2：校验手机号是否合法
	 * 
	 * @param mobiles
	 *            手机号
	 * @return true:合法 false:不合法
	 */
	public static boolean isMobileNO(String mobiles) {
		// Pattern p =
		// Pattern.compile("^(13[0-9])|(15[^4,\\d])|(18[0,5-9]))\\d{8}$");
		Pattern p = Pattern.compile("1[3,4,5,8]{1}\\d{9}");

		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/**
	 * 法3：校验手机号是否合法
	 * 
	 * @param value
	 *            手机号
	 * @return true:合法 false:不合法
	 */
	public static boolean isPhoneNum(String value) {
		String regExp = "^[1]([3|4|5|7|8][0-9]{1}|59|58|88|89)[0-9]{8}$";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(value);
		return m.find();// boolean
	}

	/**
	 * 校验邮箱是否合法
	 * 
	 * @param value
	 *            箱邮
	 * @return true:合法 false:不合法
	 */
	public static boolean isEmailFormat(String value) {
		String regExp = "^[0-9a-z][a-z0-9\\._-]{1,}@[a-z0-9-]{1,}[a-z0-9]\\.[a-z\\.]{1,}[a-z]$";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(value);
		return m.find();
	}

	/**
	 * 校验身份证是否合法
	 * 
	 * @param value
	 *            身份证
	 * @return true:合法 false:不合法
	 */
	public static boolean isIDFormat(String value) {
		String pattern = "((11|12|13|14|15|21|22|23|31|32|33|34|35|36|37|41|42|43|44|45|46|50|51|52|53|54|61|62|63|64|65|71|81|82|91)\\d{4})((((19|20)(([02468][048])|([13579][26]))0229))|((20[0-9][0-9])|(19[0-9][0-9]))((((0[1-9])|(1[0-2]))((0[1-9])|(1\\d)|(2[0-8])))|((((0[1,3-9])|(1[0-2]))(29|30))|(((0[13578])|(1[02]))31))))((\\d{3}(x|X))|(\\d{4}))";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(value);
		return m.find();
	}

	/**
	 * 特殊字符验证
	 * 
	 * @param validateContent
	 *            要验证的字符串内容
	 * @return true:包含特殊字符串 false:不包含特殊字符串
	 */
	public static boolean isHaveSpecicalChar(String validateContent) {
		String regex = "[/／／々～‖｜〃〈〉《》「」『』〖〗【】［］｛｝々～‖｜〃〈〉《》「」『』〖〗【】［］｛｝々～‖｜〈〉《》「」『』〖〗【】［］｛｝々～‖｜〈〉《》「」『』〖〗【】［］｛｝]{1,}";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(validateContent);
		return m.find();
	}

	/**
	 * 验证年龄是否合法
	 * 
	 * @param ageStr
	 *            年龄字符串
	 * @return true 合法 false 不合法
	 */
	public static boolean isAgeRight(String ageStr) {
		return true;
	}

	@SuppressLint("SimpleDateFormat")
	public static boolean checkDate(String date, String format) {
		DateFormat df = new SimpleDateFormat(format);
		Date d = null;
		try {
			d = df.parse(date);
		} catch (Exception e) {
			// 如果不能转换,肯定是错误格式
			e.printStackTrace();
			return false;
		}
		String s1 = df.format(d);
		return date.equals(s1);
	}

	/**
	 * 判断出生日期是否与身份证号中的信息匹配
	 * 
	 * @param strDate
	 *            出生日期 格式 “yyyyMMdd”
	 * @param strId
	 *            身份证号码
	 * @return 是则返回true
	 */
	@SuppressLint("SimpleDateFormat")
	public static boolean checkBirthIDCard(String strDate, String strId) {
		if (null == strDate || null == strId || "".equals(strDate) || strId.length() < 15)

			return false;

		// 获取出生日期
		String birthday = strId.substring(6, 14);
		try {
			Date date = new SimpleDateFormat("yyyyMMdd").parse(strDate);
			Date birthdate = new SimpleDateFormat("yyyyMMdd").parse(birthday);
			return (birthdate.getTime() - date.getTime()) == 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 判断SIM是否可用
	 * 
	 * @return 可用则返回true
	 */
	public static boolean isSimExist(Context context) {

		TelephonyManager phoneManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		int simState = phoneManager.getSimState();
		switch (simState) {

		case TelephonyManager.SIM_STATE_ABSENT:

			MToast.showToast(context, "无SIM卡", Toast.LENGTH_SHORT);
			return false;

		case TelephonyManager.SIM_STATE_NETWORK_LOCKED:

			MToast.showToast(context, "需要NetworkPIN解锁", Toast.LENGTH_SHORT);
			return false;

		case TelephonyManager.SIM_STATE_PIN_REQUIRED:

			MToast.showToast(context, "需要PIN解锁", Toast.LENGTH_SHORT);
			return false;

		case TelephonyManager.SIM_STATE_PUK_REQUIRED:

			MToast.showToast(context, "需要PUN解锁", Toast.LENGTH_SHORT);
			return false;

		case TelephonyManager.SIM_STATE_READY:

			MToast.showToast(context, "良好", Toast.LENGTH_SHORT);
			return true;

		case TelephonyManager.SIM_STATE_UNKNOWN:
			MToast.showToast(context, "未知状态", Toast.LENGTH_SHORT);
			return false;

		default:

			return false;

		}

	}

	/**
	 * 判断一个Activity是否正在运行
	 * @param pkg
	 * @param cls
	 * @param context
	 * @return
	 */
	public static boolean isClsRunning(String pkg, String cls, Context context) {
		ActivityManager am =(ActivityManager)context.getSystemService(ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
		ActivityManager.RunningTaskInfo task = tasks.get(0);
		if (task != null) {
			return TextUtils.equals(task.topActivity.getPackageName(), pkg) && TextUtils.equals(task.topActivity.getClassName(), cls);
		}
		return false;
	}

}
