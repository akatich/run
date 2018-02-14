package tich.run;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import tich.run.model.Step;
import tich.run.model.Training;

public class Preferences {

    private static SharedPreferences sharedPreferences = null;
    private static Preferences myPreferences = null;
    private final static String SONGS = "tich.run.songs";
    private final static String TRAINING = "tich.run.training";
    private ArrayList<Training> trainings = new ArrayList<Training>();

    private Preferences()
    {
        loadTrainings();

        /*Training t1 = new Training();
        Step s1 = new Step();
        s1.setId(5);
        t1.addStep(s1);
        trainings.add(t1);

        Training t2 = new Training();
        Step s2 = new Step();
        s2.setId(3);
        t2.addStep(s2);
        Step s3 = new Step();
        s3.setId(7);
        t2.addStep(s3);
        trainings.add(t2);*/
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

    private void loadTrainings()
    {
        int trainingId = 1;
        String trainingStr = "null";
        do {
            // Récupérer les fichiers training
            // Ex : tich.run.training.1 = dureeStep1,allureStep1 ; dureeStep2,allureStep2 ; ...
            trainingStr = sharedPreferences.getString(TRAINING + "." + trainingId, "");
            if (!trainingStr.equals(""))
            {
                Training training = new Training();
                training.setId(trainingId);

                String[] steps = trainingStr.split(";");
                for (String stepDetails : steps)
                {
                    String[] stepDetail = stepDetails.split(",");
                    Step step = new Step();
                    step.setLength(Integer.parseInt(stepDetail[0]));
                    step.setSpeed(Integer.parseInt(stepDetail[1]));
                    training.addStep(step);
                }
                trainings.add(training);
                trainingId++;
            }
        }
        while (!trainingStr.equals(""));
    }

    public int getSongSpeed(String songTitle)
    {
        String songSpeed = sharedPreferences.getString(SONGS + "." + encodeSongTitle(songTitle), "0");
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

    public ArrayList<Training> getTrainings() {
        return trainings;
    }

    public static int pxFromDp(float dp, Context mContext) {
        return (int) ((int) dp * mContext.getResources().getDisplayMetrics().density);
    }

    public void saveTrainings()
    {
        for (int i=0; i<trainings.size(); i++)
        {
            Training training = trainings.get(i);
            StringBuffer buff = new StringBuffer();
            ListIterator<Step> iter = training.getSteps().listIterator();
            while (iter.hasNext())
            {
                Step step = iter.next();
                if (buff.length() > 0)
                    buff.append(";");
                buff.append(Integer.toString(step.getLength()) + "," + step.getSpeed());
            }
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(TRAINING + "." + (i + 1), buff.toString());
            editor.commit();
        }
    }
}
