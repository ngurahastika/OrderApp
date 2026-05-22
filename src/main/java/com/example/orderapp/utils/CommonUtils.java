package com.example.orderapp.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CommonUtils {

	private static final SecureRandom RANDOM = new SecureRandom();
	private static final String ALPHANUM = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

	public static String generateId() {
		long time = System.currentTimeMillis();
		String uuid = UUID.randomUUID().toString().replace("-", "");
		return Long.toHexString(time) + uuid;
	}

	public static String generateSession() {
		String session = String.valueOf(System.currentTimeMillis());
		return session.concat(UUID.randomUUID().toString().replace("-", ""));
	}

	public static String generateUUID(String prefix) {

		String date = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
		String uuid = UUID.randomUUID().toString().replace("-", "");

		return prefix + date + uuid;
	}

	public static String randomAlpaNumeric(int length) {
		StringBuilder randomPart = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			randomPart.append(ALPHANUM.charAt(RANDOM.nextInt(ALPHANUM.length())));
		}
		return randomPart.toString();
	}

	public static String dashFormat(String value) {
		StringBuilder result = new StringBuilder(value);
		for (int i = value.length(); i > 0; i--) {
			if (i % 4 == 0 && i != value.length()) {
				result.insert(i, "-");
			}
		}
		return result.toString();
	}

	public static String hourMinuteSeparator(String hour) {
		String[] splitTime = hour.split(":");
		return splitTime[0] + " H : " + splitTime[1] + " M";
	}

	public static List<String> usingSplitMethod(String text, int n) {
		List<String> result = new ArrayList<>();
		for (int i = 0; i < text.length(); i += n) {
			result.add(text.substring(i, Math.min(i + n, text.length())));
		}
		return result;
	}

	public static String formatNoHp(String noHp) {
		if (noHp == null)
			return "";
		String[] split = noHp.split(" ");
		if (split.length > 1) {
			return split[0] + " " + dashFormat(split[1]);
		} else {
			return dashFormat(noHp);
		}
	}

	public static boolean isNotEmpty(String value) {
		return value != null && !value.trim().isEmpty();
	}

	// ======================= Numbers =======================

	public static String currencyNumberFormatter(BigDecimal number) {
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		DecimalFormatSymbols dfs = ((DecimalFormat) nf).getDecimalFormatSymbols();
		dfs.setCurrencySymbol("");
		((DecimalFormat) nf).setDecimalFormatSymbols(dfs);
		((DecimalFormat) nf).setNegativePrefix("-");
		((DecimalFormat) nf).setNegativeSuffix("");
		nf.setRoundingMode(RoundingMode.DOWN);
		return nf.format(number);
	}

	public static int compareVersion(String vOld, String vNew) {
		String[] oldV = vOld.split("\\.");
		String[] newV = vNew.split("\\.");
		int length = Math.max(oldV.length, newV.length);
		for (int i = 0; i < length; i++) {
			int oldNum = i < oldV.length ? Integer.parseInt(oldV[i]) : 0;
			int newNum = i < newV.length ? Integer.parseInt(newV[i]) : 0;
			if (oldNum < newNum)
				return 1;
			if (oldNum > newNum)
				return -1;
		}
		return 0;
	}

}
