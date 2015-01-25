package rajesh.sciencehack;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;


public class JobDetail extends ActionBarActivity {

    Job job;
    TextView mDescription, mRequirements, mCompensation, mReferme, mApply, mJobname, mJobLocation;
    ImageView mJobImage;
    ProgressDialog mProgress;
    Dialog mDialog;
    LiveWebView wv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        toolbar.setTitle("Job Detail");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Applying");
        job = (Job) getIntent().getSerializableExtra("job");
        mDescription = (TextView) findViewById(R.id.job_description);
        mRequirements = (TextView) findViewById(R.id.job_requirements);
        mCompensation = (TextView) findViewById(R.id.job_compensation);
        mJobImage = (ImageView) findViewById(R.id.job_image);
        mJobname = (TextView) findViewById(R.id.job_title);
        mJobLocation = (TextView) findViewById(R.id.job_location);
        mDescription.setText(job.description);
        mRequirements.setText(job.requirements);
        mCompensation.setText(job.compensation);
        mJobname.setText(job.title);
        mJobLocation.setText(job.company + " - " + job.city + ", " + job.state + ", " + job.country);
        mReferme = (TextView) findViewById(R.id.referme);
        mReferme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, job.job_url);
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Ask referral via"));
            }
        });

        mApply = (TextView) findViewById(R.id.apply);
        mApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgress.show();
                applyJob();
            }
        });

        ImageLoader.getInstance().displayImage(job.background_photo, mJobImage);
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_job_detail, menu);
//        return true;
//    }

    private void applyJob() {
        NetworkUtils.getBestMatchUser(this, new NetworkUtils.NetworkCallback<User>() {
            @Override
            public void onSuccess(ArrayList<User> data) {
                mProgress.dismiss();
                //http://springrole.com/jobs/844-technical-product-delivery-manager-at-sidebench-studios/apply.htm?user_id=ln_ts7Px5SyX6&apply=1&utm_source=science-hack
                String url = job.job_url + "/apply.htm?user_id=" + data.get(0).profile_url.replace("http://springrole.com/profile/", "") + "&apply=1&utm_source=science-hack&access_token=" + PreferenceUtils.getAccessToken(JobDetail.this);
                sendApply(url);
            }

            @Override
            public void onError() {
                mProgress.dismiss();
                Toast.makeText(JobDetail.this, "Something fucked up", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void sendApply(String url) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        wv = new LiveWebView(this);
        wv.loadUrl(url);
        wv.getSettings().setJavaScriptEnabled(true);
        alert.setView(wv);
        wv.requestFocus();
        mDialog = alert.create();
        mDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != LiveWebView.INPUT_FILE_REQUEST_CODE || wv.mFilePathCallback == null) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }

        Uri[] results = null;

        // Check that the response is a good one
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) {
//                // If there is not data, then we may have taken a photo
//                if(mCameraPhotoPath != null) {
//                    results = new Uri[]{Uri.parse(mCameraPhotoPath)};
//                }
            } else {
                String dataString = data.getDataString();
                if (dataString != null) {
                    results = new Uri[]{Uri.parse(dataString)};
                }
            }
        }

        wv.mFilePathCallback.onReceiveValue(results);
        wv.mFilePathCallback = null;
        return;
    }
}
