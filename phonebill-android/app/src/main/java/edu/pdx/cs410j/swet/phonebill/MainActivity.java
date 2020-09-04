package edu.pdx.cs410j.swet.phonebill;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void launchReadMe(View view) {
        Intent intent = new Intent(this,ReadMeActivity.class);
        startActivity(intent);
    }

    public void launchaddphonecall(View view) {
        Intent intent = new Intent(this,AddPhoneCallActivity.class);
        startActivity(intent);
    }

    public void launchSearch(View view) {
        Intent intent = new Intent(this, SearchPhoneCallActivity.class);
        startActivity(intent);
    }
}