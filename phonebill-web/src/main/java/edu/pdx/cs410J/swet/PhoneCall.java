package edu.pdx.cs410J.swet;

import edu.pdx.cs410J.AbstractPhoneCall;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneCall extends AbstractPhoneCall implements Comparable<PhoneCall>
{
    private static String DATE_TIME_FORMAT = "M/d/yyyy h:mm a";
    private static String DATE_TIME_REGEX = "(\\d{1,2}/){1}(\\d{1,2}/){1}\\d{4}\\s+(\\d{1,2}:\\d{2}\\s+)(am|pm|AM|PM)";

    private final String callerNumber;
    private final String calleeNumber;
    private final Date startTime;
    private final Date endTime;

    /**
     * Constructor for PhoneCall = Takes numbers and datetimes as args
     * @param callerNum The caller phonenumber
     * @param calleeNum The call-EE phonenumber
     * @param start Start Date/Time of the Call
     * @param end End Date/Time of the Call
     * @throws PhoneBillException Thrown by this.validate()
     */
    public PhoneCall(String callerNum, String calleeNum, String start, String end) throws PhoneBillException
    {
        this.callerNumber = callerNum;
        this.calleeNumber = calleeNum;
        this.startTime = parseDate(start);
        this.endTime = parseDate(end);
        this.validate();
    }

    /**
     * This method is used by my Phonebill Sort method to compare phonecalls
     * Implements compareTo interface java.lang.Comparable
     * @param o
     * @return
     */
    @Override
    public int compareTo(PhoneCall o)
    {
        int time = startTime.compareTo(o.getStartTime());

        if (time != 0)
        {
            return time;
        }

        int caller = callerNumber.compareTo(o.getCaller());

        if (caller != 0)
        {
            return caller;
        }

        return 0;
    }

    /**
     * Overrides getCaller method of AbstractPhoneCall method
     * @return String of the caller's phone number
     */
    @Override
    public String getCaller()
    {
        return this.callerNumber;
    }

    /**
     * Overrides getCallee method of AbstractPhoneCall method
     * @return String of the callee phone number
     */
    @Override
    public String getCallee()
    {
        return this.calleeNumber;
    }

    /**
     * Converts the Start Date object to a String
     * @return String of startTime
     */
    @Override
    public String getStartTimeString()
    {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
        return sdf.format(this.startTime);
    }

    /**
     * Converts the End Date object to a String
     * @return String of endTime
     */
    @Override
    public String getEndTimeString()
    {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
        return sdf.format(this.endTime);
    }

    // Validation Methods

    /**
     * Validate Method for PhoneCall - Calls all private validate methods
     * @throws PhoneBillException thrown by validation of class propoerties
     */
    private void validate() throws PhoneBillException
    {
        this.validatePhoneNumber(this.callerNumber);
        this.validatePhoneNumber(this.calleeNumber);

        this.validateDate(this.startTime);
        this.validateDate(this.endTime);
        this.validateDuration(this.startTime,this.endTime);
    }

    /**
     * Validates a Date/Time String
     * @param s String representing a date/time
     * @return Returns true if the date is valie
     */
    private boolean validateDateTime(String s)
    {
        Pattern pattern = Pattern.compile(DATE_TIME_REGEX);
        Matcher matcher = pattern.matcher(s);

        return matcher.find();
    }

    /**
     * Validates a Date - Makes sure the year date is at least 4 digits and not greater than 2099
     * @param datetime Date object of the date to be validated
     * @throws PhoneBillException thrown if the year is off
     */
    private void validateDate(Date datetime) throws PhoneBillException
    {
        LocalDate localDate = datetime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int year  = localDate.getYear();

        if (year < 1000 || year > 2099)
        {
            throw new PhoneBillException("'" + datetime +"' is not a valid date/time");
        }
    }

    /**
     * Validates a Phone number
     * @param phoneNumber String representing a phone number
     * @throws PhoneBillException Thrown if the phone number pattern is not matched
     */
    private void validatePhoneNumber(String phoneNumber) throws PhoneBillException
    {
        //nnn-nnn-nnnn
        //(?:\d{3}-){2}\d{4}
        //String pattern = "^/d(?:-/d{3}){3}/d$";
        String pattern = "[0-9]{3}-[0-9]{3}-[0-9]{4}";

        if (!phoneNumber.matches(pattern))
        {
            throw new PhoneBillException("'" + phoneNumber +"' is not a valid phone number");
        }
    }

    private void validateDuration(Date start, Date end) throws PhoneBillException
    {
        if(end.getTime() -start.getTime()<0){
            throw new PhoneBillException("End time should be after start time.");
        }
    }

    //// Private Methods ///////////////////////

    /**
     * Parses a string into a Date
     * @param s String representation of a date
     * @return Date object
     * @throws PhoneBillException if date can't be parsed
     */
    private Date parseDate(String s) throws PhoneBillException
    {
        try
        {
            if (!validateDateTime(s))
            {
                throw new PhoneBillException("'" + s + "' is not a valid date/time in the format of '" + DATE_TIME_FORMAT + "'");
            }

            SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.US);
            sdf.setLenient(false);

            return sdf.parse(s);
        }
        catch (java.time.format.DateTimeParseException e)
        {
            throw new PhoneBillException("DateTimeParseException: '" + s + "' is not a valid date/time in the format of '" + DATE_TIME_FORMAT + "'");
        }
        catch (ParseException e)
        {
            throw new PhoneBillException("ParseException: '" + s + "' is not a valid date/time in the format of '" + DATE_TIME_FORMAT + "'");
        }
    }

    public String duration(){
        long duration =startTime.getTime()-endTime.getTime();
        long diffMinutes = duration / (60 * 1000) % 60;
        long diffHours = duration / (60 * 60 * 1000);
        if(diffHours ==0)
            return " "+-1*diffMinutes + " minutes";
        else
            return "  "+-1*diffHours+"hr and "+ -1*diffMinutes +"min";
    }

    /**
     * Gets the Start Date/Time
     * @return Date Ojbect
     */
    public Date getStartTime()
    {
        return this.startTime;
    }

    /**
     * Gets the End Date/Time
     * @return Date Ojbect
     */
    public Date getEndTime()
    {
        return this.endTime;
    }
/*
    public String getDateShort(Date input){
        String dateShort = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT).format(input);
        return dateShort;
    }*/
}