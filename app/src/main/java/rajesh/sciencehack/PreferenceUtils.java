package rajesh.sciencehack;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by user on 24/01/15.
 */
public class PreferenceUtils {

    public static String getAccessToken(Context context){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString(Constants.ACCESS_TOKEN, null);
    }

    public static void setAccessToken(Context context, String token){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        pref.edit().putString(Constants.ACCESS_TOKEN, token).commit();
    }

    public static String getUserId(Context context){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString(Constants.USER_ID, null);
    }

    public static void setUserId(Context context, String userid){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        pref.edit().putString(Constants.USER_ID, userid).commit();
    }
}
