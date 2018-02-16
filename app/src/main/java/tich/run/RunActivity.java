package tich.run;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;
import java.util.ArrayList;
import tich.run.model.Song;
import tich.run.model.Step;
import tich.run.model.Training;


public class RunActivity extends AppCompatActivity {

    private ArrayList<Training> trainingList;
    private ViewFlipper viewFlipper;
    private MyTimerTask timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);

        Preferences.getPreferences(this).sortSongs();

        buildLayout();
    }

    private void buildLayout()
    {
        viewFlipper = findViewById(R.id.view_flipper);

        trainingList = Preferences.getPreferences(this).getTrainings();
        for (int i=0; i<trainingList.size(); i++)
        {
            Training training = trainingList.get(i);

            LinearLayout verticalLayout = new LinearLayout(this);
            verticalLayout.setTag(training);
            verticalLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
            verticalLayout.setOrientation(LinearLayout.VERTICAL);

            RelativeLayout trainingLayout = new RelativeLayout(this);
            trainingLayout.setLayoutParams(new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT));
            trainingLayout.setGravity(Gravity.CENTER_VERTICAL);

            if (i > 0)
            {
                ImageView imgLeft = new ImageView(this);
                imgLeft.setLayoutParams(new ViewGroup.LayoutParams(
                        Preferences.pxFromDp(40, this),
                        Preferences.pxFromDp(40, this)));
                imgLeft.setImageResource(R.drawable.arrow_left);
                imgLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewFlipper.showPrevious();
                    }
                });
                trainingLayout.addView(imgLeft);
            }

            TextView trainingText = new TextView(this);
            trainingText.setLayoutParams(new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT));
            trainingText.setText("Programme " + training.getId() + "  :  " + training.getLength() + " min");
            trainingText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            trainingText.setTextColor(Color.parseColor("#FFFFFF99"));
            ((RelativeLayout.LayoutParams)trainingText.getLayoutParams()).addRule(RelativeLayout.CENTER_HORIZONTAL);
            trainingLayout.addView(trainingText);

            if (i < trainingList.size()-1)
            {
                ImageView imgRight = new ImageView(this);
                imgRight.setLayoutParams(new RelativeLayout.LayoutParams(
                        Preferences.pxFromDp(40, this),
                        Preferences.pxFromDp(40, this)));
                imgRight.setImageResource(R.drawable.arrow_right);
                imgRight.setForegroundGravity(Gravity.END);
                ((RelativeLayout.LayoutParams)imgRight.getLayoutParams()).addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                imgRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewFlipper.showNext();
                    }
                });
                trainingLayout.addView(imgRight);
            }
            verticalLayout.addView(trainingLayout);

            for (Step step : training.getSteps())
            {
                LinearLayout stepLayout = new LinearLayout(this);
                stepLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        Preferences.pxFromDp(40, this)));
                ((LinearLayout.LayoutParams)stepLayout.getLayoutParams()).topMargin = Preferences.pxFromDp(1, this);
                stepLayout.setGravity(Gravity.CENTER_VERTICAL);
                stepLayout.setBackgroundColor(Color.parseColor("#26E5FF"));

                TextView stepIdView = new TextView(this);
                stepIdView.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                ((LinearLayout.LayoutParams)stepIdView.getLayoutParams()).leftMargin = Preferences.pxFromDp(20, this);
                stepIdView.setTextColor(Color.parseColor("#FFFFFF99"));
                stepIdView.setText("Step " + step.getId() + " : ");
                stepLayout.addView(stepIdView);

                TextView stepLengthView = new TextView(this);
                stepLengthView.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1f));
                ((LinearLayout.LayoutParams)stepLengthView.getLayoutParams()).leftMargin = Preferences.pxFromDp(10, this);
                stepLengthView.setTextColor(Color.parseColor("#FFFFFF99"));
                stepLengthView.setText(Integer.toString(step.getLength()) + " min");
                stepLayout.addView(stepLengthView);

                ImageView stepSpeedView = new ImageView(this);
                stepSpeedView.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                switch (step.getSpeed())
                {
                    case Song.UNDEFINED:
                        stepSpeedView.setBackgroundResource(R.drawable.background_undefined);
                        stepSpeedView.setImageResource(R.drawable.undefined);
                        break;
                    case Song.SLOW:
                        stepSpeedView.setBackgroundResource(R.drawable.background_slow);
                        stepSpeedView.setImageResource(R.drawable.slow);
                        break;
                    case Song.MEDIUM:
                        stepSpeedView.setBackgroundResource(R.drawable.background_medium);
                        stepSpeedView.setImageResource(R.drawable.medium);
                        break;
                    case Song.FAST:
                        stepSpeedView.setBackgroundResource(R.drawable.background_fast);
                        stepSpeedView.setImageResource(R.drawable.fast);
                        break;
                }
                stepLayout.addView(stepSpeedView);
                verticalLayout.addView(stepLayout);
            }

            viewFlipper.addView(verticalLayout);
        }

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
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.step2:
                //step2
                intent = new Intent(this, TrainingActivity.class);
                startActivity(intent);
                break;
            case R.id.step3:
                //step3
                break;
            /*case R.id.action_end:
                stopService(playIntent);
                musicSrv=null;
                System.exit(0);
                break;*/
        }
        return super.onOptionsItemSelected(item);
    }

    public void play(View v)
    {
        //initialize the TimerTask's job
        timer = new MyTimerTask(this);
        Training training = (Training) viewFlipper.getCurrentView().getTag();
        timer.setSteps(training.getSteps());
        timer.start();
    }

}
