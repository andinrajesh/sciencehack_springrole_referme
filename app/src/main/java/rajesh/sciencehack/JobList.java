package rajesh.sciencehack;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;


public class JobList extends ActionBarActivity {

    ListView mListview;
    ProgressBar mProgress;
    JobAdapter mAdapter;
    ArrayList<Job> data;

    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(android.R.color.white) // resource or drawable
            .showImageForEmptyUri(android.R.color.white) // resource or drawable
            .showImageOnFail(android.R.color.white) // resource or drawable
            .delayBeforeLoading(1000)
            .cacheInMemory(true) // default
            .cacheOnDisk(true) // default
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        toolbar.setTitle("Spring role");
        init();
        NetworkUtils.getJobs(this, new NetworkUtils.NetworkCallback<Job>() {
            @Override
            public void onSuccess(ArrayList<Job> jobs) {
                mProgress.setVisibility(View.GONE);
                data.addAll(jobs);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError() {

            }
        });
    }

    private void init(){
        data = new ArrayList<Job>();
        mAdapter = new JobAdapter(this, R.layout.joblist_item, data);
        mListview = (ListView) findViewById(R.id.joblist);
        mListview.setAdapter(mAdapter);
        mProgress = (ProgressBar) findViewById(R.id.progress);
        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(JobList.this, JobDetail.class);
                intent.putExtra("job", data.get(position));
                startActivity(intent);
            }
        });
    }

    class JobAdapter extends ArrayAdapter<Job>{

        private LayoutInflater mInflater;
        public JobAdapter(Context context, int resource, ArrayList<Job> objects) {
            super(context, resource, objects);
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Holder mHolder;
            if(convertView == null){
                convertView = mInflater.inflate(R.layout.joblist_item, null);
                mHolder = new Holder();
                mHolder.mBannerImage = (ImageView)convertView.findViewById(R.id.jobbanner);
                mHolder.mCompany = (TextView) convertView.findViewById(R.id.jobcompany);
                mHolder.mTitle = (TextView) convertView.findViewById(R.id.jobtitle);
                convertView.setTag(mHolder);
            }else{
                mHolder = (Holder)convertView.getTag();
            }

            Job job = getItem(position);
            mHolder.mTitle.setText(job.title);
            mHolder.mCompany.setText(job.company + " - " + job.city + ", " + job.country);
            ImageLoader.getInstance().displayImage(job.background_photo_mobile, mHolder.mBannerImage, options);
            return convertView;
        }

        class Holder{
           public ImageView mBannerImage;
           public TextView mTitle;
           public TextView mCompany;
        }
    }



//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_job_list, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
