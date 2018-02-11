package tich.run.model;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import tich.run.R;

public class SongAdapter extends BaseAdapter {

    private ArrayList<Song> songs;
    private LayoutInflater songInf;

    public SongAdapter(Context c, ArrayList<Song> theSongs){
        songs=theSongs;
        songInf=LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //map to song layout
        LinearLayout songLay = (LinearLayout)songInf.inflate
                (R.layout.song, parent, false);
        //get title and artist views
        TextView songView = (TextView)songLay.findViewById(R.id.song_title);
        TextView artistView = (TextView)songLay.findViewById(R.id.song_artist);
        //get song using position
        Song currSong = songs.get(position);
        //get title and artist strings
        songView.setText(currSong.getTitle());
        artistView.setText(currSong.getArtist());
        //set position as tag
        songLay.setTag(position);
        //display correct image
        ImageView songSpeed = (ImageView) songLay.findViewById(R.id.song_speed);
        switch (currSong.getSpeed())
        {
            case Song.UNDEFINED:
                songSpeed.setBackgroundResource(R.drawable.background_undefined);
                songSpeed.setImageResource(R.drawable.undefined);
                break;
            case Song.SLOW:
                songSpeed.setBackgroundResource(R.drawable.background_slow);
                songSpeed.setImageResource(R.drawable.slow);
                break;
            case Song.MEDIUM:
                songSpeed.setBackgroundResource(R.drawable.background_medium);
                songSpeed.setImageResource(R.drawable.medium);
                break;
            case Song.FAST:
                songSpeed.setBackgroundResource(R.drawable.background_fast);
                songSpeed.setImageResource(R.drawable.fast);
                break;
        }
        return songLay;
    }
}
