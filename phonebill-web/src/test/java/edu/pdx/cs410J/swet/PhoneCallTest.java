package edu.pdx.cs410J.swet;

import org.junit.Test;

public class PhoneCallTest {

    @Test
    public void test1(){
    PhoneCall call = new PhoneCall("123-123-1234","098-098-1234","10/10/2020 10:00 AM","10/10/2020 11:00 AM");
        PhoneCall call1 = new PhoneCall("123-123-1234","098-098-1234","10/10/2020 10:00 AM","10/10/2020 11:00 AM");
    call.compareTo(call1);
    }

    @Test(expected = PhoneBillException.class)
    public void test2(){
        PhoneCall call = new PhoneCall("123-123-1234","098-098-1234","10/10/2020 10:00 AM","10/10/2020 09:00 AM");
        //PhoneCall call1 = new PhoneCall("123-123-1234","098-098-1234","10/10/2020 10:00 AM","10/10/2020 11:00 AM");
        //call.compareTo(call1);
    }
    @Test(expected = PhoneBillException.class)
    public void test3(){
        PhoneCall call = new PhoneCall("123123-1234","098-098-1234","10/10/2020 10:00 AM","10/10/2020 09:00 AM");
        //PhoneCall call1 = new PhoneCall("123-123-1234","098-098-1234","10/10/2020 10:00 AM","10/10/2020 11:00 AM");
        //call.compareTo(call1);
    }

    @Test(expected = PhoneBillException.class)
    public void test4(){
        PhoneCall call = new PhoneCall("123123-1234","098-098-1234","10/10/3020 10:00 AM","10/10/2020 09:00 AM");
        //PhoneCall call1 = new PhoneCall("123-123-1234","098-098-1234","10/10/2020 10:00 AM","10/10/2020 11:00 AM");
        //call.compareTo(call1);
    }
    @Test
    public void test5(){
        PhoneCall call = new PhoneCall("123-123-1234","098-098-1234","10/10/2020 08:00 AM","10/10/2020 10:00 AM");
        //PhoneCall call1 = new PhoneCall("123-123-1234","098-098-1234","10/10/2020 10:00 AM","10/10/2020 11:00 AM");
        //call.compareTo(call1);
        String dur = call.duration();
    }
}

