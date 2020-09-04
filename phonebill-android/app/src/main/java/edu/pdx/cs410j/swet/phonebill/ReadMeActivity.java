package edu.pdx.cs410j.swet.phonebill;

import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

public class ReadMeActivity extends AppCompatActivity {
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_me);
        webView = findViewById(R.id.readMe);
        String str = getString(R.string.readme);
        Spanned htmlAsSpanned = Html.fromHtml(str);
        webView.loadDataWithBaseURL(null, str, "text/html", "utf-8", null);
    }
}

