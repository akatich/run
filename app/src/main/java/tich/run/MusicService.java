package tich.run;

import android.app.Service;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.IBinder;
import java.util.ArrayList;
import java.util.HashMap;
import android.content.ContentUris;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Binder;
import android.os.PowerManager;
import android.util.Log;
import tich.run.model.Song;
import tich.run.model.Training;

public class MusicService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {

    //media player
    private MyTimerTask player;
    //song list
    private ArrayList<Song> songs;
    private HashMap<String, String> songsPositionFromId;
    //current position
    private int songPosn;
    private final IBinder musicBind = new MusicBinder();
    private SoundPool sp;
    int trainingDoneSound;


    public void onCreate()
    {
        //create the service
        super.onCreate();
        //initialize position
        songPosn=0;
        //create player
        player = new MyTimerTask();

        initMusicPlayer();
        initSoundPool();
    }

    private void initSoundPool()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            AudioAttributes attr = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            sp = new SoundPool.Builder()
                    .setAudioAttributes(attr)
                    .setMaxStreams(5)
                    .build();
        }
        else
        {
            sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }
        trainingDoneSound = sp.load(this, R.raw.training_done, 1);
    }

    public void initMusicPlayer()
    {
        //set player properties
        player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);

        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    public void initTimer(RunActivity activity, Training training)
    {
        player.init(activity);
        player.setSteps(training.getSteps());
        player.startPlaySong();
    }

    public void alertTrainingDone()
    {
        // pause play of current song
        player.pause();

        // play sound to alert training is done
        sp.play(trainingDoneSound, 1, 1, 1, 0, 1f);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {}

        //resume play of current song
        player.start();
    }

    public void setList(ArrayList<Song> theSongs){
        songs=theSongs;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
       player.stillPlay();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        //start playback
        mp.start();
    }

    public class MusicBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent){
        player.stop();
        player.release();
        return false;
    }

    public void playSong(){
        //play a song
        player.reset();
        //get song
        Song playSong = songs.get(songPosn);
        //get id
        long currSong = playSong.getId();
        //set uri
        Uri trackUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                currSong);
        try{
            player.setDataSource(getApplicationContext(), trackUri);
        }
        catch(Exception e){
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }
        player.prepareAsync();
    }

    public void pauseSong()
    {
        player.pause();
    }

    public void resumeSong()
    {
        player.start();
    }

    public void checkChrono(long elapsedMillis)
    {
        player.checkChrono(elapsedMillis);
    }

    public void stopSong()
    {
        player.stop();
    }

    public void setSong(int songIndex){
        songPosn=songIndex;
    }

    public int getSong()
    {
        return songPosn;
    }

    public boolean isPlaying()
    {
        return player.isPlaying();
    }

    public void indexSongs()
    {
        songsPositionFromId = new HashMap<String, String>(songs.size());
        for (int i=0; i<songs.size(); i++)
        {
            Song song = songs.get(i);
            songsPositionFromId.put(Integer.toString(song.getId()), Integer.toString(i));
        }
    }

    public void setSongId(int songId)
    {
        songPosn = Integer.parseInt(songsPositionFromId.get(Integer.toString(songId)));
    }
}
