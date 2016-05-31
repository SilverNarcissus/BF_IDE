package toolKit;

import javax.print.attribute.standard.RequestingUserName;

public class Time implements Comparable<Time> {
	private int year;
	private int month;
	private int day;
	private int hour;
	private int minute;
	private int second;
	private String tag;

	public Time(String time) {
		String[] row1 = time.split("~");
		tag=row1[0];
		year = Integer.parseInt(row1[1].split("-")[0]);
		month = Integer.parseInt(row1[1].split("-")[1]);
		day = Integer.parseInt(row1[1].split("-")[2]);
		hour = Integer.parseInt(row1[2].split("-")[0]);
		minute = Integer.parseInt(row1[2].split("-")[1]);
		second = Integer.parseInt(row1[2].split("-")[2]);
	}

	@Override
	public int compareTo(Time time) {
		// TODO Auto-generated method stub
		if (year != time.getYear()) {
			return time.getYear() - year;
		}
		if (month != time.getMonth()) {
			return time.getMonth() - month;
		}
		if (day != time.getDay()) {
			return time.getDay() - day;
		}
		if (hour != time.getHour()) {
			return time.getHour() - hour;
		}
		if (minute != time.getMinute()) {
			return time.getMinute() - minute;
		} else {
			return time.getSecond() - second;
		}
	}

	public int getYear() {
		return year;
	}

	public int getMonth() {
		return month;
	}

	public int getDay() {
		return day;
	}

	public int getHour() {
		return hour;
	}

	public int getMinute() {
		return minute;
	}

	public int getSecond() {
		return second;
	}
	@Override
	public String toString(){
		return tag+"_"+String.valueOf(year)+"-"+String.valueOf(month)+"-"+String.valueOf(day)+"~"+String.valueOf(hour)+"-"+String.valueOf(minute)+"-"+String.valueOf(second);
	}
}
