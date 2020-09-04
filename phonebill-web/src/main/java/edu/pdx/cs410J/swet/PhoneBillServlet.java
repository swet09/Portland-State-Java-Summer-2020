package edu.pdx.cs410J.swet;

import com.google.common.annotations.VisibleForTesting;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This servlet ultimately provides a REST API for working with an
 * <code>PhoneBill</code>.  However, in its current state, it is an example
 * of how to use HTTP and Java servlets to store simple dictionary of words
 * and their definitions.
 */
public class PhoneBillServlet extends HttpServlet
{
    static final String CUSTOMER_PARAMETER = "customer";
    static final String DEFINITION_PARAMETER = "definition";
    private static final String CALLER_PARAMETER = "caller";
    private static final String CALLEE_PARAMETER = "callee";
    public static final String START_TIME_PARAMETER = "start";
    public static final String END_TIME_PARAMETER = "end";

    private static String DATE_TIME_REGEX = "(\\d{1,2}/){1}(\\d{1,2}/){1}\\d{4}\\s+(\\d{1,2}:\\d{2}\\s+)(am|pm|AM|PM)";
    private Map<String, PhoneBill> bills = new HashMap<>();

    /**
     * Handles an HTTP GET request from a client by writing the definition of the
     * word specified in the "word" HTTP parameter to the HTTP response.  If the
     * "word" parameter is not specified, all of the entries in the dictionary
     * are written to the HTTP response.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
        // So this function needs to figure out:
        // 1. Are simply returning all calls for a customer?
        //    http://localhost:8080/phonebill/calls?customer=Customer
        // OR...
        // 2. Are we searching for all calls for a customer within a time period?
        //    http://localhost:8080/phonebill/calls?customer=Customer&start=9/21/2018&end=9/22/2018

        response.setContentType( "text/plain" );

        String customer = getParameter(CUSTOMER_PARAMETER, request );
        String start = getParameter(START_TIME_PARAMETER, request);
        String end = getParameter(END_TIME_PARAMETER, request);

        if (customer == null)
        {
            missingRequiredParameter(response, CUSTOMER_PARAMETER);
        }
        else if (start == null && end == null)
        {
            // 1. Simply returning all calls for a customer
            //    http://localhost:8080/phonebill/calls?customer=Customer
            writePrettyPhoneBill(customer, response);
        }
        else if (start == null)
        {
            missingRequiredParameter(response, START_TIME_PARAMETER);
        }
        else if (end == null)
        {
            missingRequiredParameter(response, END_TIME_PARAMETER);
        }
        else
        {
            // 2. We searching for all calls for a customer within a time period
            //    http://localhost:8080/phonebill/calls?customer=Customer&start=9/21/2018&end=9/22/2018
            writePrettyPhoneBill(customer, start, end, response);
        }
    }

    /**
     * Print out a Pretty Phone Bill of all calls for a customer between start and endTime
     * @param customer
     * @param start
     * @param end
     * @param response
     * @throws IOException
     */
    private void writePrettyPhoneBill(String customer, String start, String end, HttpServletResponse response) throws IOException
    {
        PhoneBill bill = getPhoneBill(customer);

        if (bill == null)
        {
            String mes = Messages.noPhoneBillForCustomer(customer);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND,mes);
        }
        else
        {
            if(check(start)&&check(end)){

                    Collection<PhoneCall> calls = bill.getPhoneCallsByDate(start, end);
                    if(calls.isEmpty()){
                        String mes = "No calls in the given interval";
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND,mes);
                    }
                    else
                    {
                        PrintWriter writer = response.getWriter();
                        TextDumper pretty = new TextDumper(writer);
                        pretty.dump(customer, calls);
                        response.setStatus(HttpServletResponse.SC_OK);
                    }

               }
            else
            {
                String mes = "Date format is incorrect";
                response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED,mes);
            }
        }
    }

    /**
     * Print out a Pretty Phone Bill of all calls for a customer
     * @param customer
     * @param response
     * @throws IOException
     */
    private void writePrettyPhoneBill(String customer, HttpServletResponse response) throws IOException
    {
        PhoneBill bill = getPhoneBill(customer);

        if (bill == null)
        {
            String mes = Messages.noPhoneBillForCustomer(customer);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND,mes);
        }
        else
        {

            PrintWriter writer = response.getWriter();
            TextDumper pretty = new TextDumper(writer);
            pretty.dump(bill);
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }

    /**
     * Handles an HTTP POST request by storing the dictionary entry for the
     * "word" and "definition" request parameters.  It writes the dictionary
     * entry to the HTTP response.
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException //, PhoneBillException
    {
        response.setContentType( "text/plain" );

        String customer = getParameter(CUSTOMER_PARAMETER, request );
        if (customer == null) {
            missingRequiredParameter(response, CUSTOMER_PARAMETER);
            return;
        }

        PhoneBill bill = getPhoneBill(customer);
        if (bill == null) {
            bill = new PhoneBill(customer);
            addPhoneBill(bill);
        }

        String caller = getParameter(CALLER_PARAMETER, request);
        String callee = getParameter(CALLEE_PARAMETER, request);
        String start = getParameter(START_TIME_PARAMETER, request);
        String end = getParameter(END_TIME_PARAMETER, request);
        PhoneCall call = new PhoneCall(caller, callee, start, end);
        bill.addPhoneCall(call);

        response.setStatus(HttpServletResponse.SC_OK);
    }

    /**
     * Handles an HTTP DELETE request by removing all dictionary entries.  This
     * behavior is exposed for testing purposes only.  It's probably not
     * something that you'd want a real application to expose.
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");

        this.bills.clear();

        PrintWriter pw = response.getWriter();
        pw.println("All phone calls and bills deleted");
        pw.flush();

        response.setStatus(HttpServletResponse.SC_OK);
    }

    /**
     * Writes an error message about a missing parameter to the HTTP response.
     * The text of the error message is created by {@link Messages#missingRequiredParameter(String)}
     * @param response
     * @param parameterName
     * @throws IOException
     */
    private void missingRequiredParameter( HttpServletResponse response, String parameterName )
            throws IOException
    {
        String message = Messages.missingRequiredParameter(parameterName);
        response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, message);
    }


    /**
     * Returns the value of the HTTP request parameter with the given name.
     *
     * @return <code>null</code> if the value of the parameter is
     *         <code>null</code> or is the empty string
     */
    private String getParameter(String name, HttpServletRequest request) {
        String value = request.getParameter(name);
        if (value == null || "".equals(value)) {
            return null;

        } else {
            return value;
        }
    }

    private Boolean check(String start)
    {
        String arg [] = start.split(" ");

        Boolean valid =true;
        //check for date
        if(!arg[0].matches("(0?[1-9]|1[012])/(0?[1-9]|[12][0-9]|3[01])/((19|20)\\d\\d)"))
        {
            valid = false;
        }

        //check for time
        if(!arg[1].matches("([01]?[0-9]|2[0-3]):[0-5][0-9]"))
        {
            valid = false;
        }

        //check for am/pm
        if(!arg[2].matches("([AaPp][Mm])"))
        {
           valid = false;
        }

        return valid;
    }

   /* private Boolean checkTime(String start, String end)
    {
        boolean valid = true;
        Date startDate = new Date();
        Date endDate = new Date();
        String dur;
        SimpleDateFormat sd = new SimpleDateFormat("MM/dd/yyyy, hh:mm aa");
        try {
            startDate = sd.parse(start);
            endDate = sd.parse(end);
        } catch (ParseException e) {
            System.out.println("could not convert date");
        }

        if((endDate.getTime()- startDate.getTime())<0)
        {
            valid =false;
        }

        return valid;
    }*/

    /**
     * Get PhoneBill of the specified customer
     * @param customer
     * @return
     */
    @VisibleForTesting
    PhoneBill getPhoneBill(String customer) {
        return this.bills.get(customer);
    }

    /**
     * Add PhoneBill
     * @param bill
     */
    @VisibleForTesting
    void addPhoneBill(PhoneBill bill) {
        this.bills.put(bill.getCustomer(), bill);
    }
}
