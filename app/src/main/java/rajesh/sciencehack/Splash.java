package rajesh.sciencehack;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class Splash extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(PreferenceUtils.getAccessToken(Splash.this) != null){
                    startActivity(new Intent(Splash.this, JobList.class));
                    finish();
                }else{
                    startActivity(new Intent(Splash.this, MainActivity.class));
                    finish();
                }
            }
        }, 2000);

    }
}
