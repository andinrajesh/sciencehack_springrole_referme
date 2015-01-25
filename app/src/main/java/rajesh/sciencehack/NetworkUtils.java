package rajesh.sciencehack;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by user on 24/01/15.
 */
public class NetworkUtils {

    private static String url = "https://api.springrole.com/beta/jobs?access_token=";


    interface NetworkCallback<T>{
        public void onSuccess(ArrayList<T> data);
        public void onError();

    }

    public static class Jobs {
        public ArrayList<Job> data;
    }

    public static class Users {
        public ArrayList<User> data;
    }



    public static void getJobs(Context context, final NetworkCallback mcallback){
        url = url + PreferenceUtils.getAccessToken(context) + "&user_id=" + PreferenceUtils.getUserId(context) + "&page_size=100&city=Bengaluru";
        JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ArrayList<Job> data = new Gson().fromJson(response.toString(), Jobs.class).data;
                mcallback.onSuccess(data);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mcallback.onError();
            }
        });

        ApplicationController.getInstance().addToRequestQueue(request);
    }

    public static void getBestMatchUser(Context context, final NetworkCallback mcallback){
        String userUrl = "https://api.springrole.com/beta/users?access_token=";
        userUrl = userUrl + PreferenceUtils.getAccessToken(context) + "&user_id=" + PreferenceUtils.getUserId(context) + "&page_size=5";
        JsonObjectRequest request = new JsonObjectRequest(userUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ArrayList<User> data = new Gson().fromJson(response.toString(), Users.class).data;
                mcallback.onSuccess(data);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mcallback.onError();
            }
        });

        ApplicationController.getInstance().addToRequestQueue(request);
    }
}
