package tich.run;

import android.os.SystemClock;
import android.widget.Chronometer;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;

import tich.run.model.Song;
import tich.run.model.Step;

public class MyTimerTask
{
    private RunActivity activity;
    private LinkedList<Step> steps;
    private Chronometer chrono;
    private Step currentStep;
    private int stepPosition = 0;
    private TextView runStatus;
    private int threshold;
    public static MusicService musicSrv;
    private ArrayList<Song> songListUndefined;
    private int songUndefinedPos = 0;
    private ArrayList<Song> songListSlow;
    private int songSlowPos = 0;
    private ArrayList<Song> songListMedium;
    private int songMediumPos = 0;
    private ArrayList<Song> songListFast;
    private int songFastPos = 0;
    private TextView songTitleView;
    private TextView songArtistView;


    public MyTimerTask(final RunActivity activity)
    {
        this.activity = activity;
        runStatus = (TextView) activity.findViewById(R.id.run_status);

        songListUndefined = Preferences.getPreferences(activity).getSongListUndefined();
        songListSlow = Preferences.getPreferences(activity).getSongListSlow();
        songListMedium = Preferences.getPreferences(activity).getSongListMedium();
        songListFast = Preferences.getPreferences(activity).getSongListFast();

        songTitleView = activity.findViewById(R.id.current_song_title);
        songArtistView = activity.findViewById(R.id.current_song_artist);

        chrono = activity.findViewById(R.id.chrono);
        chrono.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer)
            {
                long elapsedMillis = SystemClock.elapsedRealtime() - chronometer.getBase();
                if(elapsedMillis > threshold * 1000 * 60)
                {
                    runStatus.setText("Step " + currentStep.getId() + " done !");
                    stepPosition++;
                    if (stepPosition == steps.size())
                        chrono.stop();
                    else {
                        boolean changeMusic = false;
                        if (currentStep.getSpeed() != steps.get(stepPosition).getSpeed())
                            changeMusic = true;
                        currentStep = steps.get(stepPosition);
                        threshold += currentStep.getLength();
                        if (changeMusic)
                        {
                            changeMusic(currentStep.getSpeed());
                        }
                        else
                        {
                            stillPlay();
                        }
                    }
                }
                else if (elapsedMillis > 1000)
                {
                    stillPlay();
                }
            }
        });
    }

    public void stillPlay()
    {
        if (!MainActivity.musicSrv.isPlaying())
        {
            switch (currentStep.getSpeed())
            {
                case Song.UNDEFINED:
                    songUndefinedPos++;
                    Song song = songListUndefined.get(songUndefinedPos);
                    playSongwithSongId(song);
                    break;
                case Song.SLOW:
                    songSlowPos++;
                    song = songListSlow.get(songSlowPos);
                    playSongwithSongId(song);
                    break;
                case Song.MEDIUM:
                    songMediumPos++;
                    song = songListMedium.get(songMediumPos);
                    playSongwithSongId(song);
                    break;
                case Song.FAST:
                    songFastPos++;
                    song = songListFast.get(songFastPos);
                    playSongwithSongId(song);
                    break;
            }
        }
    }


    public void start()
    {
        currentStep = steps.getFirst();
        threshold = currentStep.getLength();
        changeMusic(currentStep.getSpeed());

        // start chrono
        chrono.setBase(SystemClock.elapsedRealtime());
        chrono.start();
    }

    private void changeMusic(int songSpeed)
    {
        switch (songSpeed)
        {
            case Song.UNDEFINED:
                Song song = songListUndefined.get(songUndefinedPos);
                playSongwithSongId(song);
                break;
            case Song.SLOW:
                song = songListSlow.get(songSlowPos);
                playSongwithSongId(song);
                break;
            case Song.MEDIUM:
                song = songListMedium.get(songMediumPos);
                playSongwithSongId(song);
                break;
            case Song.FAST:
                song = songListFast.get(songFastPos);
                playSongwithSongId(song);
                break;
        }
    }

    private void playSongwithSongId(Song song)
    {
        songTitleView.setText(song.getTitle());
        songArtistView.setText(song.getArtist());
        MainActivity.musicSrv.setSongId(song.getId());
        MainActivity.musicSrv.playSong();
    }

    public void stop()
    {
        chrono.stop();
    }


    public void setSteps(LinkedList<Step> steps) {
        this.steps = steps;
    }
}
