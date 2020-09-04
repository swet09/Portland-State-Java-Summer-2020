package edu.pdx.cs410j.swet.phonebill;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.pdx.cs410J.AbstractPhoneBill;
import edu.pdx.cs410J.AbstractPhoneCall;

public class PhoneBill extends AbstractPhoneBill {

    String custName;

    public PhoneBill(String custName)
    {
        if (custName.length() == 0)
        {
            throw new UnsupportedOperationException("Please enter customer name");
        }
        this.custName = custName;
    }



    /**
     * get function for customer name
     * @return - customer name
     */

    @Override
    public String getCustomer() {
        return custName;
    }

    List<AbstractPhoneCall> calls = new ArrayList<>();

    @Override
    public void addPhoneCall(AbstractPhoneCall abstractPhoneCall) {
        calls.add(abstractPhoneCall);
    }


    public Collection getPhoneCalls() {
        return calls;
    }



}


