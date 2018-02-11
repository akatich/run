package tich.run;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preferences {

    private static SharedPreferences sharedPreferences = null;
    private static Preferences myPreferences = null;
    private final static String SONGS = "tich.run.songs";
    private final static String STEPS = "tich.run.steps";

    private Preferences()
    {
        loadSteps();
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

    private void loadSteps()
    {

    }

    public int getSongSpeed(String songTitle)
    {
        String songSpeed = sharedPreferences.getString(SONGS + "." + encodeSongTitle(songTitle), "0");
        if (songTitle.equals("Alarms on call"))
            System.out.println("songSpeed = " + songSpeed);
        return Integer.parseInt(songSpeed);
    }

    public void updateSong(String songTitle, int songSpeed)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SONGS + "." + encodeSongTitle(songTitle), Integer.toString(songSpeed));
        editor.commit();
    }

    private String encodeSongTitle(String songTitle)
    {
        return songTitle.replace(" ", "").replace("'", "");
    }
}
