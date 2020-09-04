package edu.pdx.cs410J.swet;

import edu.pdx.cs410J.web.HttpRequestHelper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * The main class that parses the command line and communicates with the
 * Phone Bill server using REST.
 */
public class Project4
{
    static final String USAGE =
            "usage: java edu.pdx.cs410J.<login-id>.Project4 [options] <args>\n" +
                    "  args are (in this order):\n" +
                    "\tcustomer        Person whose phone bill we’re modeling\n" +
                    "\tcallerNumber    Phone number of caller\n" +
                    "\tcalleeNumber    Phone number of person who was called\n" +
                    "\tstartTime       Date and time (am/pm) call began\n" +
                    "\tendTime         Date and time (am/pm) call ended\n" +
                    "  options are (options may appear in any order):\n" +
                    "\t-host hostname  Host computer on which the server runs\n" +
                    "\t-port port      Port on which the server is listening\n" +
                    "\t-search         Phone calls should be searched for\n" +
                    "\t-print          Prints a description of the new phone call\n" +
                    "\t-README         Prints a README for this project and exits\n\n";

    public static void printline(String textDumper){

        String lines[] = textDumper.split("\n");
        if(lines.length>1) {
            for (int i = 1; i < lines.length; i++) {
                String argument[] = lines[i].split(" ");
                String start = argument[2] + " " + argument[3] + " " + argument[4];
                String end = argument[5] + " " + argument[6] + " " + argument[7];
                Date startDate = new Date();
                Date endDate = new Date();
                String dur;
                SimpleDateFormat sd = new SimpleDateFormat("MM/dd/yyyy hh:mm aa");
                try {
                    startDate = sd.parse(start);

                } catch (ParseException e) {
                    System.out.println("start time "+start);
                }
                try {
                    endDate = sd.parse(end);
                } catch (ParseException e) {
                    System.out.println("endDate "+end);
                }
                long duration = startDate.getTime() - endDate.getTime();
                long diffMinutes = duration / (60 * 1000) % 60;
                long diffHours = duration / (60 * 60 * 1000);
                if (diffHours == 0)
                    dur = " " + -1 * diffMinutes + " minutes";
                else
                    dur = "  " + -1 * diffHours + "hr and " + -1 * diffMinutes + "min";

                String result = "Phone call from " + argument[0] + " to " + argument[1] + " from " + start + " to " + end + " duration " + dur;
                System.out.println(result);
            }
        }
    }


    /**
     * Main function for Project 4
     * @param args Takes in arguments from the command line
     * @throws Exception Can throw PhonebillException, ParseException, IOException
     */
    public static void main(String[] args) throws Exception
    {
        try
        {
            commandLineCheck cli = new commandLineCheck(args);

            // 1. Show README and exit
            if (cli.readme)
            {
                System.out.println(USAGE);
                System.exit(0);
            }
            // 2. Search
            else if (cli.search)
            {
                PhoneBillRestClient client = new PhoneBillRestClient(cli.hostname, cli.portNumber);

                String message = client.searchPhoneCalls(cli.customer, cli.startTime, cli.endTime);
                if(message.equals("412")){
                    System.out.println("Date format is incorrect");
                }
                else
                {
                    printline(message);
                    System.exit(0);
                }

            }
            // 3. Get all calls for customer
            else if (cli.customer != null &&
                    cli.callerNumber == null && cli.calleeNumber == null &&
                    cli.startTime == null && cli.endTime == null)
            {
                PhoneBillRestClient client = new PhoneBillRestClient(cli.hostname, cli.portNumber);
                String message = client.getPrettyPhoneBill(cli.customer);

                printline(message);
                System.exit(0);
            }
            // 4. Add Phone Call
            else
            {
                try
                {
                    PhoneBillRestClient client = new PhoneBillRestClient(cli.hostname, cli.portNumber);

                    PhoneCall call = new PhoneCall(cli.callerNumber, cli.calleeNumber, cli.startTime, cli.endTime);
                    client.addPhoneCall(cli.customer, call);

                    if (cli.print)
                    {
                        System.out.println(call.toString());
                    }
                }
                catch (RuntimeException e)
                {
                    System.err.println(e.getMessage());
                    System.exit(1);
                }

                System.exit(0);
            }
        }
        catch (NoSuchPhoneBillException e)
        {
            System.out.println(e.getMessage());
            System.exit(0);
        }
        catch (PhoneBillException e)
        {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}