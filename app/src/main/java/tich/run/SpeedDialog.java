package tich.run;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.ToggleButton;

import tich.run.model.Song;

public class SpeedDialog extends DialogFragment {

    private MainActivity activity;
    private int songId;
    private ImageView songSpeedView;


    public SpeedDialog(MainActivity activity, int songId, ImageView songSpeedView)
    {
        super();
        this.activity = activity;
        this.songId = songId;
        this.songSpeedView = songSpeedView;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        final View optView = inflater.inflate(R.layout.speed_dialog, null);
        builder.setView(optView);
        AlertDialog dialog = builder.create();

        final ImageView imgUndefined = optView.findViewById(R.id.undefined);
        imgUndefined.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v)
            {
                Song song = activity.getSongList().get(songId);
                song.setSpeed(Song.UNDEFINED);
                songSpeedView.setBackgroundResource(R.drawable.background_undefined);
                songSpeedView.setImageResource(R.drawable.undefined);
                Preferences.getPreferences(activity).updateSong(song.getTitle(), Song.UNDEFINED);
                getDialog().cancel();
            }
        });

        final ImageView imgSlow = optView.findViewById(R.id.slow);
        imgSlow.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v)
            {
                Song song = activity.getSongList().get(songId);
                song.setSpeed(Song.SLOW);
                songSpeedView.setBackgroundResource(R.drawable.background_slow);
                songSpeedView.setImageResource(R.drawable.slow);
                Preferences.getPreferences(activity).updateSong(song.getTitle(), Song.SLOW);
                getDialog().cancel();
            }
        });

        final ImageView imgMedium = optView.findViewById(R.id.medium);
        imgMedium.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v)
            {
                Song song = activity.getSongList().get(songId);
                song.setSpeed(Song.MEDIUM);
                songSpeedView.setBackgroundResource(R.drawable.background_medium);
                songSpeedView.setImageResource(R.drawable.medium);
                Preferences.getPreferences(activity).updateSong(song.getTitle(), Song.MEDIUM);
                getDialog().cancel();
            }
        });

        final ImageView imgFast = optView.findViewById(R.id.fast);
        imgFast.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v)
            {
                Song song = activity.getSongList().get(songId);
                song.setSpeed(Song.FAST);
                songSpeedView.setBackgroundResource(R.drawable.background_fast);
                songSpeedView.setImageResource(R.drawable.fast);
                Preferences.getPreferences(activity).updateSong(song.getTitle(), Song.FAST);
                getDialog().cancel();
            }
        });

        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        dialog.show();

        Button btnPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button btnNegative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        btnPositive.setVisibility(View.INVISIBLE);
        btnNegative.setVisibility(View.INVISIBLE);

        // Create the AlertDialog object and return it
//        dialog.getWindow().setBackgroundDrawableResource(R.drawable.parchemin_horizontal);
//        dialog.getWindow().setWindowAnimations(R.style.DialogAnimation);

        return dialog;
    }

    public void slow(View v)
    {

    }

    @Override
    public void onStop() {
        super.onStop();

    }
}
