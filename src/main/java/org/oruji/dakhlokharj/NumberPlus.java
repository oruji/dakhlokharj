package org.oruji.dakhlokharj;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class NumberPlus {
	private static final int convertNumber = 1728;
	private final DecimalFormatSymbols separateSymbol = new DecimalFormatSymbols(Locale.US);

	private final char[] value;

	private enum langStat {
		PERSIAN, ENGLISH
	}

	public enum sepStat {
		COMMA, DOT, SLASH
	}

	@SuppressWarnings("unused")
	private NumberPlus() {
		this.value = new char[0];
	}

	public NumberPlus(String value) throws NumberFormatException {
		value = value.trim();
		value = value.replaceAll(",", "");
		this.value = value.toCharArray();
	}

	public NumberPlus(Integer value) {
		this.value = String.valueOf(value).toCharArray();
	}

	public NumberPlus(Long value) {
		this.value = String.valueOf(value).toCharArray();
	}

	public NumberPlus(Float value) {
		this.value = String.valueOf(value).toCharArray();
	}

	public NumberPlus(Double value) {
		this.value = String.valueOf(value).toCharArray();
	}

	public String getEnglish() {
		return getEnglish(value);
	}

	private String getEnglish(char[] charArr) {
		for (int i = 0; i < charArr.length; i++) {
			if (charArr[i] >= 1776 && charArr[i] <= 1785) {
				charArr[i] = (char) (charArr[i] - convertNumber);
			}
		}

		return new String(charArr);
	}

	private String getSeparate(langStat language) {
		separateSymbol.setDecimalSeparator('.');
		separateSymbol.setGroupingSeparator(',');

		DecimalFormat formatter = new DecimalFormat("", separateSymbol);

		String str = formatter.format(Double.parseDouble(getEnglish(value)));

		if (language == langStat.PERSIAN)
			return getPersian(str.toCharArray());

		return str;
	}

	private String getSeparate(sepStat decimalSep, sepStat groupSep, langStat language) {
		switch (decimalSep) {
		case COMMA:
			separateSymbol.setDecimalSeparator(',');
			break;
		case DOT:
			separateSymbol.setDecimalSeparator('.');
			break;
		case SLASH:
			separateSymbol.setDecimalSeparator('/');
			break;

		default:
			break;
		}

		switch (groupSep) {
		case COMMA:
			separateSymbol.setGroupingSeparator(',');
			break;
		case DOT:
			separateSymbol.setGroupingSeparator('.');
			break;
		case SLASH:
			separateSymbol.setGroupingSeparator('/');
			break;

		default:
			break;
		}

		DecimalFormat formatter = new DecimalFormat("", separateSymbol);

		String str = formatter.format(Double.parseDouble(getEnglish(value)));

		if (language == langStat.PERSIAN)
			return getPersian(str.toCharArray());

		return str;
	}

	public String getEnglishSep() {
		return getSeparate(langStat.ENGLISH);
	}

	public String getEnglishSep(sepStat decimalSep, sepStat groupSep) {
		return getSeparate(decimalSep, groupSep, langStat.ENGLISH);
	}

	public String getPersian() {
		return getPersian(value);
	}

	private String getPersian(char[] charArr) {
		for (int i = 0; i < charArr.length; i++) {
			if (charArr[i] >= 48 && charArr[i] <= 57) {
				charArr[i] = (char) (charArr[i] + convertNumber);
			}
		}

		return new String(charArr);
	}

	public String getPersianSep() {
		return getSeparate(langStat.PERSIAN);
	}

	public String getPersianSep(sepStat decimalSep, sepStat groupSep) {
		return getSeparate(decimalSep, groupSep, langStat.PERSIAN);
	}

	public Integer getInteger() {
		return Double.valueOf(getEnglish(value)).intValue();
	}

	public Long getLong() {
		return Double.valueOf(getEnglish(value)).longValue();
	}

	public Double getDouble() {
		return Double.valueOf(getEnglish(value));
	}

	public Float getFloat() {
		return Float.valueOf(getEnglish(value));
	}
}