package edu.pdx.cs410J.swet;

import java.util.ArrayList;
import java.util.Arrays;

public class commandLineCheck {

    private ArrayList<String> arguments;

    // Arguments
    protected String customer;
    protected String callerNumber;
    protected String calleeNumber;
    protected String startTime;
    protected String endTime;

    // Rest Arguments
    protected Boolean host=Boolean.FALSE;
    protected Boolean port=Boolean.FALSE;
    protected String hostname;
    protected int portNumber;

    // Search Arguments
    protected Boolean search=Boolean.FALSE;
    protected String searchCustomer;

    // Optional Arguments
    protected Boolean print=Boolean.FALSE;
    protected Boolean readme=Boolean.FALSE;

    /**
     * Constructor for CLI - Processes Command Line Arguments
     * @param args Command Line Arguments
     * @throws PhoneBillException Thrown if arguments are invalid
     */
    public commandLineCheck(String[] args) throws PhoneBillException
    {
        this.host = Arrays.stream(args).anyMatch(s -> s.equals("-host"));
        this.port = Arrays.stream(args).anyMatch(s -> s.equals("-port"));
        this.search = Arrays.stream(args).anyMatch(s -> s.equals("-search"));

        this.readme = Arrays.stream(args).anyMatch(s -> s.equals("-README"));
        this.print = Arrays.stream(args).anyMatch(s -> s.equals("-print"));

        arguments = new ArrayList<>(Arrays.asList(args));

        if(this.readme)
        {
            return;
        }
        else {

           /* if((Arrays.stream(args).anyMatch(s -> s.startsWith("-")) && (!host ||!port || !search || !print) ))

            {
                throw new PhoneBillException("Option can be of -host, -port, -search and -print. Invalid option.");
            }

            else*/ {


                if (this.host) {
                    int i = arguments.indexOf("-host");

                    if (i + 1 > arguments.size() - 1) {
                        throw new PhoneBillException("Missing hostname argument");
                    }

                    this.hostname = arguments.get(i + 1);

                    arguments.remove("-host");
                    arguments.remove(this.hostname);
                }

                if (this.port) {
                    int i = arguments.indexOf("-port");

                    if (i + 1 > arguments.size() - 1) {
                        throw new PhoneBillException("Missing port number argument");
                    }

                    this.portNumber = Integer.parseInt(arguments.get(i + 1));

                    arguments.remove("-port");
                    arguments.remove(Integer.toString(this.portNumber));
                }

                if (this.search) {
                    int i = arguments.indexOf("-search");

                    if (i + 1 > arguments.size() - 1) {
                        throw new PhoneBillException("Missing search arguments - customer name, start and end time");
                    }

                    arguments.remove("-search");
                    if (arguments.size() == 7) {
                        this.customer = arguments.get(0);
                        this.startTime = arguments.get(1) + " " + arguments.get(2) + " " + arguments.get(3);
                        this.endTime = arguments.get(4) + " " + arguments.get(5) + " " + arguments.get(6);
                    } else if (arguments.size() < 7) {
                        throw new PhoneBillException("Search should have customer name, start and end date.Missing arguments");
                    } else {
                        throw new PhoneBillException("Search should have only customer name, start and end date.Too many command line arguments");
                    }

                } else {
                    /*arguments.remove("-");*/
                    arguments.remove("-print");
                    arguments.remove("-README");

                    if (arguments.size() == 1) {
                        this.customer = arguments.get(0);
                    } else // Add phone call
                    {
                        if (arguments.size() == 9) {
                            this.customer = arguments.get(0);
                            this.callerNumber = arguments.get(1);
                            this.calleeNumber = arguments.get(2);
                            this.startTime = arguments.get(3) + " " + arguments.get(4) + " " + arguments.get(5);
                            this.endTime = arguments.get(6) + " " + arguments.get(7) + " " + arguments.get(8);
                        } else if (arguments.size() < 9) {
                            throw new PhoneBillException("Missing command line arguments");
                        } else {
                            throw new PhoneBillException("Too many command line arguments");
                        }
                    }

                }

            }
        }
    }
}
