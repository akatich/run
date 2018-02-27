package tich.run;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
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



    public void onCreate()
    {
        //create the service
        super.onCreate();
        //initialize position
        songPosn=0;
        //create player
        player = new MyTimerTask();

        initMusicPlayer();
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

    public void setList(ArrayList<Song> theSongs){
        songs=theSongs;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
       player.checkChrono();
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
