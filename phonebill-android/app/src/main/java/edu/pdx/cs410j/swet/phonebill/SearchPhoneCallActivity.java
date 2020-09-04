package edu.pdx.cs410j.swet.phonebill;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class SearchPhoneCallActivity extends AppCompatActivity {

    Button prettyButton;
    Button searchButton;
    EditText customer;
    EditText strtTime;
    EditText endTime;
    private static final Pattern DATE_TIME_PATTERN =
            Pattern.compile("^(0?[1-9]|1[0-2])/(0?[1-9]|1\\d|2\\d|3[01])/(19|20)\\d{2}\\s(((0?[1-9])|(1[0-2])):([0-5])([0-9])\\s[PpAa][Mm])$");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_phonecall);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        customer = findViewById(R.id.SearchName) ;
        strtTime = findViewById(R.id.SearchStrtTime) ;
        endTime = findViewById(R.id.SearchEndTime);
        customer.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        strtTime.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        endTime.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        prettyButton=findViewById(R.id.prettyPrint);
        searchButton=findViewById(R.id.searchButton);
        prettyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        String filename = customer.getText().toString() + ".txt";
                        File file = getBaseContext().getFileStreamPath(filename);
                        boolean flag = true;
                        if (flag && !file.exists()) {
                            Toast toast = Toast.makeText(getApplicationContext(), "Customer " + customer.getText().toString() + " does not exists.", Toast.LENGTH_LONG);
                            LinearLayout toastLayout = (LinearLayout) toast.getView();
                            TextView toastTV = (TextView) toastLayout.getChildAt(0);
                            toastTV.setTextSize(20);
                            toastTV.setBackgroundColor(Color.LTGRAY);
                            toast.show();
                            openDialog("Error","File Doesnot exist");
                            flag = false;

                        }
                        if (flag && customer.getText().toString().length() == 0) {
                            if (validateName()){
                                Toast toast = Toast.makeText(getApplicationContext(), "Please Enter Customer Name", Toast.LENGTH_SHORT);
                                LinearLayout toastLayout = (LinearLayout) toast.getView();
                                TextView toastTV = (TextView) toastLayout.getChildAt(0);
                                toastTV.setTextSize(20);
                                toast.show();
                                flag = false;
                            }
                        }
                        if(flag && customer.getText().toString().length() != 0 && strtTime.getText().toString().length()==0 && endTime.getText().toString().length()== 0){

                            Intent intent = new Intent(getApplicationContext(), PrettyPrintActivity.class);
                            intent.putExtra("SearchName", customer.getText().toString());
                            intent.putExtra("SearchStrtTime", strtTime.getText().toString());
                            intent.putExtra("SearchEndTime", endTime.getText().toString());
                            startActivity(intent);
                        }


//                if(flag && strtTime.getText().toString().length()!=0 && endTime.getText().toString().length()== 0)
//                {
//
//                    Toast toast = Toast.makeText(getApplicationContext(),"Please enter valid phonenumber",Toast.LENGTH_SHORT);
//                    LinearLayout toastLayout = (LinearLayout) toast.getView();
//                    TextView toastTV = (TextView) toastLayout.getChildAt(0);
//                    toastTV.setTextSize(20);
//                    toast.show();
//                    flag=false;
//                }
//
//                if(flag && endTime.getText().toString().length()!=0)
//                {
//                    Toast toast = Toast.makeText(getApplicationContext(),"Please enter valid phonenumber",Toast.LENGTH_SHORT);
//                    LinearLayout toastLayout = (LinearLayout) toast.getView();
//                    TextView toastTV = (TextView) toastLayout.getChildAt(0);
//                    toastTV.setTextSize(20);
//                    toast.show();
//                    flag=false;
//                }




                        if (flag ) {
                            if (validateStart() & validateEnd()) {
                                    if (startTimeBeforeEndTime()) {
                                        Intent intent = new Intent(getApplicationContext(), PrettyPrintActivity.class);
                                        intent.putExtra("SearchName", customer.getText().toString());
                                        intent.putExtra("SearchStrtTime", strtTime.getText().toString());
                                        intent.putExtra("SearchEndTime", endTime.getText().toString());
                                        startActivity(intent);
                                    }
                            }
                        }
                    }


        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filename = customer.getText().toString()+".txt";
                File file = getBaseContext().getFileStreamPath(filename);
                boolean flag = true;
                if (flag && !file.exists()) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Customer " + customer.getText().toString() + " does not exists.", Toast.LENGTH_LONG);
                    LinearLayout toastLayout = (LinearLayout) toast.getView();
                    TextView toastTV = (TextView) toastLayout.getChildAt(0);
                    toastTV.setTextSize(20);
                    toastTV.setBackgroundColor(Color.LTGRAY);
                    toast.show();
                    openDialog("Error","File Doesnot exist");
                    flag = false;

                }
                if (flag && customer.getText().toString().length() == 0) {
                    if (validateName()){
                        Toast toast = Toast.makeText(getApplicationContext(), "Please Enter Customer Name", Toast.LENGTH_SHORT);
                        LinearLayout toastLayout = (LinearLayout) toast.getView();
                        TextView toastTV = (TextView) toastLayout.getChildAt(0);
                        toastTV.setTextSize(20);
                        toast.show();
                        flag = false;
                    }
                }
                if(flag && customer.getText().toString().length() != 0 && strtTime.getText().toString().length()==0 && endTime.getText().toString().length()== 0){

                    Intent intent = new Intent(getApplicationContext(), SearchPrintActivity.class);
                    intent.putExtra("SearchName", customer.getText().toString());
                    intent.putExtra("SearchStrtTime", strtTime.getText().toString());
                    intent.putExtra("SearchEndTime", endTime.getText().toString());
                    startActivity(intent);
                }


                if (flag) {
                    if (validateStart() & validateEnd()) {
                        if (startTimeBeforeEndTime()) {
                            Intent intent = new Intent(getApplicationContext(), SearchPrintActivity.class);
                            intent.putExtra("SearchName", customer.getText().toString());
                            intent.putExtra("SearchStrtTime", strtTime.getText().toString());
                            intent.putExtra("SearchEndTime", endTime.getText().toString());
                            startActivity(intent);
                        }
                    }
                }
            }
        });
    }
    private boolean validateStart(){
        String startInput = strtTime.getText().toString().trim();
        if(startInput.isEmpty()){
            strtTime.setError("Field can't be Empty");
            return false;
        }else if(!DATE_TIME_PATTERN.matcher(startInput).matches()){
            openDialog("Error", "Given start date and time are in wrong format\nPlease follow the format\nmm/dd/yyyy hh:mm am/pm\nyou entered:\n" + startInput);
            return false;
        }else{
            strtTime.setError(null);
            return true;
        }
    }

    private boolean validateEnd(){
        String endInput = endTime.getText().toString().trim();
        if(endInput.isEmpty()){
            endTime.setError("Field can't be Empty");
            return false;
        }else if(!DATE_TIME_PATTERN.matcher(endInput).matches()){
            openDialog("Error","Given end date and time are in wrong format\nPlease follow the format\nmm/dd/yyyy hh:mm am/pm\nyou entered:\n" + endInput);
            return false;
        }else{
            endTime.setError(null);
            return true;
        }
    }

    private boolean startTimeBeforeEndTime(){
        String startInput = strtTime.getText().toString().trim();
        String endInput = endTime.getText().toString().trim();
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
    private boolean validateName(){
        String customerNameInput = customer.getText().toString().trim();
        if(customerNameInput.isEmpty()){
            customer.setError("Field can't be Empty");
            return false;
        }else{
            customer.setError(null);
            return true;
        }
    }
    public void openDialog(String t, String s){
        DialogsInPhoneBillApp dialog = new DialogsInPhoneBillApp(t, s);
        dialog.show(getSupportFragmentManager(), "Alert Dialog");
    }
}

