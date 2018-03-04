package tich.run;

import android.media.MediaPlayer;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;

import tich.run.model.Song;
import tich.run.model.Step;

public class MyTimerTask extends MediaPlayer
{
    private RunActivity activity;
    private LinkedList<Step> steps;
    private TextView chrono;
    private Step currentStep;
    private int stepPosition = 0;
    private TextView runStatus;
    private int threshold;
    private boolean trainingDone = false;
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


    public void init(final RunActivity activity)
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
    }

    public void checkChrono(long elapsedMillis)
    {
        if(!trainingDone && elapsedMillis > threshold * 1000 * 60)
        {
            runStatus.setText("Step " + currentStep.getId() + " done !");
            stepPosition++;
            if (stepPosition == steps.size())
            {
                // training is complete
                trainingDone = true;
                runStatus.setText("Training done !");
                MainActivity.musicSrv.alertTrainingDone();
            }
            else
            {
                // training is not complete
                boolean changeMusic = false;
                if (currentStep.getSpeed() != steps.get(stepPosition).getSpeed())
                    changeMusic = true;
                currentStep = steps.get(stepPosition);
                threshold += currentStep.getLength();
                if (changeMusic)
                {
                    changeMusic(currentStep.getSpeed());
                }
            }
        }
    }

    public void stillPlay()
    {
        playSong(currentStep.getSpeed());
    }

    private void changeMusic(int songSpeed)
    {
        playSong(songSpeed);
    }

    public void playSong(int songSpeed)
    {
        switch (songSpeed)
        {
            case Song.UNDEFINED:
                if (songUndefinedPos == songListUndefined.size())
                    songUndefinedPos = 0;
                Song song = songListUndefined.get(songUndefinedPos++);
                playSongwithSongId(song);
                break;
            case Song.SLOW:
                if (songSlowPos == songListSlow.size())
                    songSlowPos = 0;
                song = songListSlow.get(songSlowPos++);
                playSongwithSongId(song);
                break;
            case Song.MEDIUM:
                if (songMediumPos == songListMedium.size())
                    songMediumPos = 0;
                song = songListMedium.get(songMediumPos++);
                playSongwithSongId(song);
                break;
            case Song.FAST:
                if (songFastPos == songListFast.size())
                    songFastPos = 0;
                song = songListFast.get(songFastPos++);
                playSongwithSongId(song);
                break;
        }
    }

    public void startPlaySong()
    {
        currentStep = steps.getFirst();
        threshold = currentStep.getLength();
        changeMusic(currentStep.getSpeed());
        trainingDone = false;
    }

    private void playSongwithSongId(Song song)
    {
        songTitleView.setText(song.getTitle());
        songArtistView.setText(song.getArtist());
        MainActivity.musicSrv.setSongId(song.getId());
        MainActivity.musicSrv.playSong();
    }

    public void setSteps(LinkedList<Step> steps) {
        this.steps = steps;
    }
}
