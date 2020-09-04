package edu.pdx.cs410J.swet;

/**
 * PhoneBill Project Custom Exception class
 */
public class PhoneBillException extends RuntimeException{

    /**
     * Constructor for PhoneBill Exception Class
     * @param s String of the message to be reported
     */
    public PhoneBillException(String s)
    {
        // Call constructor of parent Exception
        super(s);
    }
}
