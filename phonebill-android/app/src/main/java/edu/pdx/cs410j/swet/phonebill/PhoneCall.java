package edu.pdx.cs410j.swet.phonebill;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.pdx.cs410J.AbstractPhoneCall;

public class PhoneCall extends AbstractPhoneCall {

    String caller;
    String callee;
    String callEndTime;
    String callStartTime;


    public void setCaller(String caller) {
            this.caller = caller;
    }

    public void setCallee(String callee) {
            this.callee = callee;
    }

    public void setEndTimeDate(String endDate, String endTime, String ampm) {
        DatetimeValidation(endDate, endTime, ampm);
        this.callEndTime = endDate + " " + endTime + " " + ampm;
    }


    public void setStartTimeDate(String startDate, String startTime, String ampm) {
        DatetimeValidation(startDate, startTime, ampm);
        this.callStartTime = startDate + " " + startTime + " " + ampm;
    }


    public static void DatetimeValidation(String date, String time, String ampm) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        String finaldatetime = date + " " + time + " " + ampm;

        try {
            Date d = formatter.parse(finaldatetime);
        } catch (ParseException e) {
            throw new UnsupportedOperationException("Date should be in MM/dd/yyyy hh:mm am/pm format");

        }
    }

    @Override
    public String getCaller() {
        if (caller == null) {
            throw new UnsupportedOperationException("Caller is not set");
        }
        return this.caller;
    }


    @Override
    public String getStartTimeString() {
        if (callStartTime == null) {
            throw new UnsupportedOperationException("Start time is not set");
        }
        String[] START = this.callStartTime.split("\\s+");
        DatetimeValidation(START[0], START[1], START[2]);
        return this.callStartTime;
    }

    @Override
    public String getCallee() {
        if (callee == null) {
            throw new UnsupportedOperationException("Callee is not set");
        }
        return this.callee;
    }


    @Override
    public String getEndTimeString() {
        if (callEndTime == null) {
            throw new UnsupportedOperationException("End time is not set");
        }
        String[] END = this.callEndTime.split("\\s+");
        DatetimeValidation(END[0], END[1], END[2]);
        return this.callEndTime;
    }
}
