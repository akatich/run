package tich.run;

import android.Manifest;
import android.content.pm.PackageManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import android.net.Uri;
import android.content.ContentResolver;
import android.database.Cursor;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.ListView;
import tich.run.model.Song;
import tich.run.model.SongAdapter;
import android.os.IBinder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Song> songList;
    private ListView songView;
    public static MusicService musicSrv;
    private Intent playIntent;
    private boolean musicBound=false;
    //connect to the service
    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            //get service
            musicSrv = binder.getService();
            //pass list
            musicSrv.setList(songList);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        songView = (ListView)findViewById(R.id.song_list);
        songList = new ArrayList<Song>();
        createSongList();

        Collections.sort(songList, new Comparator<Song>(){
            public int compare(Song a, Song b){
                return a.getTitle().compareTo(b.getTitle());
            }
        });

        SongAdapter songAdt = new SongAdapter(this, songList);
        songView.setAdapter(songAdt);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //ajoute les entrées de menu_test à l'ActionBar
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //menu item selected
        switch (item.getItemId()) {
            case R.id.step1:
                //step1
                break;
            case R.id.step2:
                //step2
                Intent intent = new Intent(this, TrainingActivity.class);
                startActivity(intent);
                break;
            case R.id.step3:
                //step3
                intent = new Intent(this, RunActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(playIntent==null){
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    public void checkUserPermission()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE))
            {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {
                // No explanation needed, we can request the permission.
                final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

    }

    public void createSongList() {

        checkUserPermission();

        //retrieve song info
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        try {
            Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
            if(musicCursor!=null && musicCursor.moveToFirst()){
                //get columns
                int titleColumn = musicCursor.getColumnIndex
                        (android.provider.MediaStore.Audio.Media.TITLE);
                int idColumn = musicCursor.getColumnIndex
                        (android.provider.MediaStore.Audio.Media._ID);
                int artistColumn = musicCursor.getColumnIndex
                        (android.provider.MediaStore.Audio.Media.ARTIST);
                //add songs to list
                do {
                    int thisId = musicCursor.getInt(idColumn);
                    String thisTitle = musicCursor.getString(titleColumn);
                    String thisArtist = musicCursor.getString(artistColumn);
                    int songSpeed = Preferences.getPreferences(this).getSongSpeed(thisTitle);
                    songList.add(new Song(thisId, thisTitle, thisArtist, songSpeed));
                }
                while (musicCursor.moveToNext());
            }

            Preferences.getPreferences(this).setSongList(songList);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void songPicked(View view){
        int songId = Integer.parseInt(view.getTag().toString());
        if (songId == musicSrv.getSong() && musicSrv.isPlaying())
            musicSrv.stopSong();
        else {
            musicSrv.setSong(songId);
            musicSrv.playSong();
        }
    }

    public void defineSpeed(View view)
    {
        SpeedDialog dialog = new SpeedDialog(this, Integer.parseInt(((View)view.getParent()).getTag().toString()), (ImageView) view);
        dialog.show(getSupportFragmentManager(), "sp");
    }

    @Override
    protected void onDestroy() {
        stopService(playIntent);
        musicSrv=null;
        super.onDestroy();
    }

    public ArrayList<Song> getSongList() {
        return songList;
    }
}
