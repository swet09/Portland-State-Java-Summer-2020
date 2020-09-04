package edu.pdx.cs410J.swet;

import edu.pdx.cs410J.PhoneBillDumper;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Class that manages persisting phone billing data to a text file
 */
public class TextDumper implements PhoneBillDumper<PhoneBill>
{
    private final PrintWriter writer;

    /**
     * Constructor for Pretty Printer
     * @param writer
     */
    public TextDumper(PrintWriter writer)
    {
        this.writer = writer;
    }

    /**
     * Pretty Prints a PhoneBill
     * @param bill
     * @throws IOException
     */
    public void dump(PhoneBill bill) throws IOException
    {
        writer.println(bill.getCustomer());
        bill.getPhoneCalls().forEach((call) -> writer.println(call.getCaller()+" "+call.getCallee()+" "+call.getStartTimeString()+" "+call.getEndTimeString()));
        //bill.getPhoneCalls().forEach((call) -> writer.println(call.toString()));
    }

    /**
     * Pretty Prints a PhoneBill for a specific customer
     * @param customer
     * @param calls
     * @throws IOException
     */
    public void dump(String customer, Collection<PhoneCall> calls) throws IOException
    {
        writer.println(customer);
        calls.forEach((call) -> writer.println(call.getCaller()+" "+call.getCallee()+" "+call.getStartTimeString()+" "+call.getEndTimeString()));
        //calls.forEach((call) -> writer.println(call.toString()));
    }
}