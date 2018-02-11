package tich.run;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preferences {

    private static SharedPreferences sharedPreferences = null;
    private static Preferences myPreferences = null;
    private final static String RUN = "tich.run";

    private Preferences()
    {
    }

    public static synchronized Preferences getPreferences()
    {
        if (myPreferences == null)
            myPreferences = new Preferences();
        return myPreferences;
    }

    public static Preferences getPreferences(Activity activity)
    {
        if (sharedPreferences == null)
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        return getPreferences();
    }

}
