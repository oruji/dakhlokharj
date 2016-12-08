package org.oruji.dakhlokharj;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

public class Jalali {
	private int day;;
	private int month;
	private int year;

	private int jY;
	private int jM;
	private int jD;

	private int gY;
	private int gM;
	private int gD;

	private int leap;
	private int march;

	/**
	 * Calculates the Julian Day number (JG2JD) from Gregorian or Julian
	 * calendar dates. This integer number corresponds to the noon of the date
	 * (i.e. 12 hours of Universal Time). The procedure was tested to be good
	 * since 1 March, -100100 (of both the calendars) up to a few millions
	 * (10**6) years into the future. The algorithm is based on D.A. Hatcher,
	 * Q.Jl.R.Astron.Soc. 25(1984), 53-55 slightly modified by me (K.M.
	 * Borkowski, Post.Astron. 25(1987), 275-279).
	 * 
	 * @param year
	 *            <code>int</code>
	 * @param month
	 *            <code>int</code>
	 * @param day
	 *            <code>int</code>
	 * @param J1G0
	 *            to be set to 1 for Julian and to 0 for Gregorian calendar
	 * @return Julian Day number
	 */
	private int JG2JD(int year, int month, int day, int J1G0) {
		int jd = (1461 * (year + 4800 + (month - 14) / 12)) / 4 + (367 * (month - 2 - 12 * ((month - 14) / 12))) / 12
				- (3 * ((year + 4900 + (month - 14) / 12) / 100)) / 4 + day - 32075;

		if (J1G0 == 0)
			jd = jd - (year + 100100 + (month - 8) / 6) / 100 * 3 / 4 + 752;

		return jd;
	}

	/**
	 * Calculates Gregorian and Julian calendar dates from the Julian Day number
	 * (JD) for the period since JD=-34839655 (i.e. the year -100100 of both the
	 * calendars) to some millions (10**6) years ahead of the present. The
	 * algorithm is based on D.A. Hatcher, Q.Jl.R.Astron.Soc. 25(1984), 53-55
	 * slightly modified by me (K.M. Borkowski, Post.Astron. 25(1987), 275-279).
	 * 
	 * @param JD
	 *            Julian day number as <code>int</code>
	 * @param J1G0
	 *            to be set to 1 for Julian and to 0 for Gregorian calendar
	 */
	private void JD2JG(int JD, int J1G0) {
		int i, j;

		j = 4 * JD + 139361631;

		if (J1G0 == 0) {
			j = j + (4 * JD + 183187720) / 146097 * 3 / 4 * 4 - 3908;
		}

		i = (j % 1461) / 4 * 5 + 308;
		gD = (i % 153) / 5 + 1;
		gM = ((i / 153) % 12) + 1;
		gY = j / 1461 - 100100 + (8 - gM) / 6;
	}

	/**
	 * Converts the Julian Day number to a date in the Jalali calendar
	 * 
	 * @param JDN
	 *            the Julian Day number
	 */
	private void JD2Jal(int JDN) {
		JD2JG(JDN, 0);

		jY = gY - 621;
		JalCal(jY);

		int JDN1F = JG2JD(gY, 3, march, 0);
		int k = JDN - JDN1F;
		if (k >= 0) {
			if (k <= 185) {
				jM = 1 + k / 31;
				jD = (k % 31) + 1;
				return;
			} else {
				k = k - 186;
			}
		} else {
			jY = jY - 1;
			k = k + 179;
			if (leap == 1)
				k = k + 1;
		}

		jM = 7 + k / 30;
		jD = (k % 30) + 1;
	}

	/**
	 * Converts a date of the Jalali calendar to the Julian Day Number
	 * 
	 * @param Jy
	 *            Jalali year as <code>int</code>
	 * @param Jm
	 *            Jalali month as <code>int</code>
	 * @param Jd
	 *            Jalali day as <code>int</code>
	 * @return Julian day number
	 */
	private int Jal2JD(int year, int month, int day) {
		JalCal(year);
		int jd = JG2JD(gY, 3, march, 1) + (month - 1) * 31 - month / 7 * (month - 7) + day - 1;
		return jd;
	}

	/**
	 * This procedure determines if the Jalali (Persian) year is leap (366-day
	 * long) or is the common year (365 days), and finds the day in March
	 * (Gregorian calendar) of the first day of the Jalali year (jY)
	 * 
	 * @param jY
	 *            Jalali calendar year (-61 to 3177)
	 */
	private void JalCal(int jY) {
		march = 0;
		leap = 0;

		int[] breaks = { -61, 9, 38, 199, 426, 686, 756, 818, 1111, 1181, 1210, 1635, 2060, 2097, 2192, 2262, 2324,
				2394, 2456, 3178 };

		gY = jY + 621;
		int leapJ = -14;
		int jp = breaks[0];

		int jump = 0;
		for (int j = 1; j <= 19; j++) {
			int jm = breaks[j];
			jump = jm - jp;
			if (jY < jm) {
				int N = jY - jp;
				leapJ = leapJ + N / 33 * 8 + (N % 33 + 3) / 4;

				if ((jump % 33) == 4 && (jump - N) == 4)
					leapJ = leapJ + 1;

				int leapG = (gY / 4) - (gY / 100 + 1) * 3 / 4 - 150;

				march = 20 + leapJ - leapG;

				if ((jump - N) < 6)
					N = N - jump + (jump + 4) / 33 * 33;

				leap = ((((N + 1) % 33) - 1) % 4);

				if (leap == -1)
					leap = 4;
				break;
			}

			leapJ = leapJ + jump / 33 * 8 + (jump % 33) / 4;
			jp = jm;
		}
	}

	/**
	 * Modified <code>toString()</code> method that represents date string
	 * 
	 * @return Date as <code>String</code>
	 */
	@Override
	public String toString() {
		return String.format("%04d-%02d-%02d", getYear(), getMonth(), getDay());
	}

	/**
	 * Converts Gregorian date to Persian(Jalali) date
	 * 
	 * @param year
	 *            <code>int</code>
	 * @param month
	 *            <code>int</code>
	 * @param day
	 *            <code>int</code>
	 */
	public void GregorianToPersian(int year, int month, int day) {
		int jd = JG2JD(year, month, day, 0);
		JD2Jal(jd);
		this.year = jY;
		this.month = jM;
		this.day = jD;
	}

	/**
	 * Converts Persian(Jalali) date to Gregorian date
	 * 
	 * @param year
	 *            <code>int</code>
	 * @param month
	 *            <code>int</code>
	 * @param day
	 *            <code>int</code>
	 */
	public DateTime PersianToGregorian(int year, int month, int day, int hour, int minute) {
		int jd = Jal2JD(year, month, day);
		JD2JG(jd, 0);
		this.year = gY;
		this.month = gM;
		this.day = gD;

		return new DateTime(gY, gM, gD, hour, minute);
	}

	/**
	 * Get manipulated day
	 * 
	 * @return Day as <code>int</code>
	 */
	public int getDay() {
		return day;
	}

	/**
	 * Get manipulated month
	 * 
	 * @return Month as <code>int</code>
	 */
	public int getMonth() {
		return month;
	}

	/**
	 * Get manipulated year
	 * 
	 * @return Year as <code>int</code>
	 */
	public int getYear() {
		return year;
	}

	public static DateTime toGregorian(String str) {
		Jalali jcal = new Jalali();
		List<Integer> dList = stringToDate(str);
		DateTime date = jcal.PersianToGregorian(dList.get(0), dList.get(1), dList.get(2), dList.get(3), dList.get(4));

		return date;
	}

	public static String toJalali(DateTime date) {
		Jalali jcal = new Jalali();
		jcal.GregorianToPersian(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth());
		return jcal.toString() + " " + date.getHourOfDay() + ":" + date.getMinuteOfHour();

	}

	public static List<Integer> stringToDate(String str) {
		List<Integer> date = new ArrayList<Integer>();

		date.add(Integer.parseInt(str.split(" ")[0].split("-")[0]));
		date.add(Integer.parseInt(str.split(" ")[0].split("-")[1]));
		date.add(Integer.parseInt(str.split(" ")[0].split("-")[2]));
		date.add(Integer.parseInt(str.split(" ")[1].split(":")[0]));
		date.add(Integer.parseInt(str.split(" ")[1].split(":")[1]));

		return date;
	}
}