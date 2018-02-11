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

    public int getSongSpeed(String songTitle)
    {
        String songSpeed = sharedPreferences.getString(RUN + "." + encodeSongTitle(songTitle), "0");
        if (songTitle.equals("Alarms on call"))
            System.out.println("songSpeed = " + songSpeed);
        return Integer.parseInt(songSpeed);
    }

    public void updateSong(String songTitle, int songSpeed)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(RUN + "." + encodeSongTitle(songTitle), Integer.toString(songSpeed));
        editor.commit();
    }

    private String encodeSongTitle(String songTitle)
    {
        return songTitle.replace(" ", "").replace("'", "");
    }
}
