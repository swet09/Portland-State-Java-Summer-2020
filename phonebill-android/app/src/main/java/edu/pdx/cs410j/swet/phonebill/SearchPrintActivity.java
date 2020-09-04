package edu.pdx.cs410j.swet.phonebill;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SearchPrintActivity extends AppCompatActivity {
    String customer;
    String strtTime;
    String endTime;
    TextView searchText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_print);
        searchText=findViewById(R.id.searchPrint);

        Intent intent = getIntent();
        customer =intent.getStringExtra("SearchName");
        strtTime =intent.getStringExtra("SearchStrtTime");
        endTime =intent.getStringExtra("SearchEndTime");
        searchprint();
    }

    public void searchprint()
    {
        String filename = customer +".txt";
        try
        {
            FileInputStream fileInputStream = openFileInput(filename);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();
            String lines;
            PhoneCall call = new PhoneCall();
            String content="";

            while ((lines=bufferedReader.readLine())!=null)
            {

                String str[] = lines.split(",");
                String startSTR[] = str[2].split("\\s+");
                String endSTR[] = str[4].split("\\s+");
                DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
                Date startCall = formatter.parse(str[2]);
                Date endCall =  formatter.parse(str[4]);
                call.setCaller(str[1]);
                call.setStartTimeDate(startSTR[0],startSTR[1],startSTR[2]);
                call.setCallee(str[3]);
                call.setEndTimeDate(endSTR[0],endSTR[1],endSTR[2]);

                if(strtTime.length()==0 && endTime.length()==0)
                {
                    content = "\n"+call.toString()+"\n";
                }
                else if(strtTime.length()!=0 && endTime.length()!=0)
                {

                    Date dataStartlimit = formatter.parse(strtTime);
                    Date dataEndlimit = formatter.parse(endTime);
                    System.out.println("calls" +dataStartlimit+"...."+dataEndlimit+"...."+startCall+"...."+endCall);
                   if((( dataStartlimit.before(startCall)|| ( startCall.compareTo(dataStartlimit)==0 ))&&( dataEndlimit.after(startCall)|| ( startCall.compareTo(dataEndlimit)==0 )))){
                       System.out.println("calls" +call.toString());
                       content = "\n"+call.toString()+"\n";
                }
                   else {
                       content = "\n";
                   }

                }
                stringBuffer.append(content);
            }


            if (stringBuffer.toString().length()==0)
                throw new UnsupportedOperationException("Calls do not exists for given inputs.Please go back to search calls to search again.");
            searchText.setText(stringBuffer.toString());
            searchText.setMovementMethod(new ScrollingMovementMethod());
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
}

