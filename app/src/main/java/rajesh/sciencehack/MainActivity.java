package rajesh.sciencehack;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
    private Dialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button b = (Button) findViewById(R.id.loginbutton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login(){
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        LiveWebView wv = new LiveWebView(this);
        wv.loadUrl("https://science-hack.herokuapp.com/auth/springrole");
        wv.getSettings().setJavaScriptEnabled(true);
        wv.setLoginListener(new LiveWebView.LoginListener() {
            @Override
            public void success(String userid, String token) {
                if (userid != null && token != null) {
                    mDialog.dismiss();
                    PreferenceUtils.setAccessToken(MainActivity.this, token);
                    PreferenceUtils.setUserId(MainActivity.this, userid);
                    startActivity(new Intent(MainActivity.this, JobList.class));
                    finish();
                }
            }
        });
        alert.setView(wv);
        wv.requestFocus();
        mDialog = alert.create();
        mDialog.show();
    }
}
