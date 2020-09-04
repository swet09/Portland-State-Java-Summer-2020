package edu.pdx.cs410j.swet.phonebill;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class AddPhoneCallActivity extends AppCompatActivity
{
    EditText input_customerName;
    EditText input_caller;
    EditText input_callee;
    EditText input_startDT;
    EditText input_endDT;
    Button save;
    private static final Pattern DATE_TIME_PATTERN =
            Pattern.compile("^(0?[1-9]|1[0-2])/(0?[1-9]|1\\d|2\\d|3[01])/(19|20)\\d{2}\\s(((0?[1-9])|(1[0-2])):([0-5])([0-9])\\s[PpAa][Mm])$");
    private static final Pattern PHONE_NUMBER_PATTERN =
            Pattern.compile("^\\d{3}[\\s-]\\d{3}[\\s-]\\d{4}$");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_phonecall);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        input_customerName = findViewById(R.id.name);
        input_caller = findViewById(R.id.Caller);
        input_callee = findViewById(R.id.Callee);
        input_startDT = findViewById(R.id.SDT);
        input_endDT = findViewById(R.id.EDT);
        save = findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateName() & validateCallerPhoneNumber() & validateCalleePhoneNumber() & validateStart() & validateEnd()) {
                    if (startTimeBeforeEndTime()) {
                        writefile();
                    }


                }

            }
        });
    }

    public void writefile()
    {
        try
        {
            String CustName = input_customerName.getText().toString();
            String CALLER = input_caller.getText().toString();
            String CALLEE = input_callee.getText().toString();
            String StartTD = input_startDT.getText().toString();
            String EndTD = input_endDT.getText().toString();

            PhoneBill bill = new PhoneBill(CustName);
            PhoneCall calls = new PhoneCall();
            calls.setCaller(CALLER);
            calls.setCallee(CALLEE);
            String[] startStr = StartTD.split("\\s+");
            String[] endStr = EndTD.split("\\s+");
            if(startStr.length!=3 || endStr.length!=3)
                throw new UnsupportedOperationException("Date should be in MM/dd/yyyy hh:mm am/pm format");
            calls.setStartTimeDate(startStr[0],startStr[1],startStr[2]);
            calls.setEndTimeDate(endStr[0],endStr[1],endStr[2]);
            bill.addPhoneCall(calls);

            DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
            Date STRT = formatter.parse(calls.getStartTimeString());
            Date END =  formatter.parse(calls.getEndTimeString());
            long duration =END.getTime()-STRT.getTime();
            long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
            if(diffInMinutes<=0)
                throw  new UnsupportedOperationException("END time must be greater than StartTD time");

            String content = bill.getCustomer() + "," + CALLER + "," + calls.getStartTimeString()+ "," + CALLEE + "," + calls.getEndTimeString()+"\n";
            String filename = CustName+".txt";
            FileOutputStream fileOut = openFileOutput(filename,MODE_APPEND);
            fileOut.write(content.getBytes());
            fileOut.close();

            Toast toast = Toast.makeText(getApplicationContext(),"Call added successfully",Toast.LENGTH_SHORT);
            LinearLayout toastLayout = (LinearLayout) toast.getView();
            TextView toastTV = (TextView) toastLayout.getChildAt(0);
            toastTV.setTextSize(20);
            toastTV.setBackgroundColor(Color.LTGRAY);
            toast.show();
            input_customerName.setText("");
            input_caller.setText("");
            input_callee.setText("");
            input_startDT.setText("");
            input_endDT.setText("");

        }
        catch (Exception e)
        {

            Toast toast=Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG);
            LinearLayout toastLayout = (LinearLayout) toast.getView();
            TextView toastTV = (TextView) toastLayout.getChildAt(0);
            toastTV.setTextSize(20);
            toastTV.setBackgroundColor(Color.LTGRAY);
            toast.show();
        }
    }



    private void closeKeyboard()
    {
        View view = this.getCurrentFocus();
        if (view!=null)
        {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }
        private boolean validateName(){
        String customerNameInput = input_customerName.getText().toString().trim();
        if(customerNameInput.isEmpty()){
            input_customerName.setError("Field can't be Empty");
            return false;
        }else{
            input_customerName.setError(null);
            return true;
        }
    }

        private boolean validateCallerPhoneNumber(){
        String callerNumberInput = input_caller.getText().toString().trim();
        if(callerNumberInput.isEmpty()){
            input_caller.setError("Field can't be Empty");
            return false;
        }else if(!PHONE_NUMBER_PATTERN.matcher(callerNumberInput).matches()){
            openDialog("Error", "Given Caller Number is not in correct format\nPlease follow the format\nnnn-nnn-nnnn\nwhere n= number [0-9]");
            return false;
        }else{
            input_caller.setError(null);
            return true;
        }
    }

        private boolean validateCalleePhoneNumber(){
        String calleeNumberInput = input_callee.getText().toString().trim();
        if(calleeNumberInput.isEmpty()){
            input_callee.setError("Field can't be Empty");
            return false;
        }else if(!PHONE_NUMBER_PATTERN.matcher(calleeNumberInput).matches()){
            openDialog("Error","Given Callee Number is in wrong format\nPlease follow the format\nnnn-nnn-nnnn where n= number [0-9]\nyou entered:\n" + calleeNumberInput);
            return false;
        }else{
            input_callee.setError(null);
            return true;
        }
    }

        private boolean validateStart(){
        String startInput = input_startDT.getText().toString().trim();
        if(startInput.isEmpty()){
            input_startDT.setError("Field can't be Empty");
            return false;
        }else if(!DATE_TIME_PATTERN.matcher(startInput).matches()){
            openDialog("Error", "Given start date and time are in wrong format\nPlease follow the format\nmm/dd/yyyy hh:mm am/pm\nyou entered:\n" + startInput);
            return false;
        }else{
            input_startDT.setError(null);
            return true;
        }
    }

        private boolean validateEnd(){
        String endInput = input_endDT.getText().toString().trim();
        if(endInput.isEmpty()){
            input_endDT.setError("Field can't be Empty");
            return false;
        }else if(!DATE_TIME_PATTERN.matcher(endInput).matches()){
            openDialog("Error","Given end date and time are in wrong format\nPlease follow the format\nmm/dd/yyyy hh:mm am/pm\nyou entered:\n" + endInput);
            return false;
        }else{
            input_endDT.setError(null);
            return true;
        }
    }

        private boolean startTimeBeforeEndTime(){
        String startInput = input_startDT.getText().toString().trim();
        String endInput = input_endDT.getText().toString().trim();
        Date startTime = getDateAndTimeInDate(startInput);
        Date endTime = getDateAndTimeInDate(endInput);
        if(!startTime.before(endTime)){
            openDialog("Error", "The end date and time should not be equal or before the start date.");
            return false;
        }
        return true;
    }
    public Date getDateAndTimeInDate(String dateTime) {
        try {
            SimpleDateFormat formatter1 = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
            Date date1 = formatter1.parse(dateTime);
            return date1;
        } catch (ParseException e){
            openDialog("Error","date parsing error");
            return null;
        }
    }

    public void openDialog(String t, String s){
        DialogsInPhoneBillApp dialog = new DialogsInPhoneBillApp(t, s);
        dialog.show(getSupportFragmentManager(), "Alert Dialog");
    }

}
