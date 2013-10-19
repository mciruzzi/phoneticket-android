package com.cinemar.phoneticket.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

public class UIDateUtil {

	private int day = 1;
	private int month = 0;
	private int year = 1980;

	private TextView mBirthDay;
	private ImageButton mPickDate;

	private DatePickerDialog.OnDateSetListener mDateListener;
	private Activity activity;

	public static final int DATE_DIALOG_ID = 8888;


	public UIDateUtil(TextView mBirthDay, ImageButton mPickDate, final Activity activity)
	{
		this.mBirthDay = mBirthDay;
		this.mPickDate = mPickDate;
		this.activity = activity;

		this.mPickDate.setOnClickListener(new View.OnClickListener() {
			@SuppressWarnings("deprecation")
			public void onClick(View arg0) {
				activity.showDialog(DATE_DIALOG_ID);
			}
		});

		update();

		mDateListener = new DatePickerDialog.OnDateSetListener() {

			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

				setDate(dayOfMonth, monthOfYear, year);
				update();
			}
		};
	}

	public Dialog createDialogWindow(int id) {

		if(id == UIDateUtil.DATE_DIALOG_ID){
			return new DatePickerDialog(activity, mDateListener, getYear(), getMonth(), getDay());
		}

		return null;
	}

	public UIDateUtil(int day, int month, int year) {

		setDate(day, month, year);
	}

	public String getDate() {

		return (day+"-"+(month + 1)+"-"+year);
	}

	public void update() {

		mBirthDay.setText(getDate());
	}

	public void setDate(int day, int month, int year) {
		setDay(day);
		setMonth(month);
		setYear(year);
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public void setDate(Date birthDay) {
		if (birthDay != null) {
			Calendar date = Calendar.getInstance();
			date.setTime(birthDay);

			setDay(date.get(Calendar.DAY_OF_MONTH));
			setMonth(this.month = date.get(Calendar.MONTH));
			setYear(date.get(Calendar.YEAR));
		} else {
			setDate(1980, 1, 1);
		}

	}

	public static Date getDateFromString(String date) throws ParseException {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		date = date.replaceAll("Z", "-0300");  //esto es porque viene la Z en lugar del número
		return dateFormat.parse(date);
	}
	
	public static String getStringFromDate(Date date) {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd-MM-yyyy");
		return dateFormat.format(date);
	}
}
