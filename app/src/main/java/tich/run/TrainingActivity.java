package tich.run;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import java.util.ArrayList;

import tich.run.model.Step;
import tich.run.model.Training;
import tich.run.model.TrainingAdapter;

public class TrainingActivity extends AppCompatActivity {

    private ArrayList<Training> trainingList;
    private ListView trainingView;
    private TrainingAdapter trainingAdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        trainingView = (ListView)findViewById(R.id.training_list);
        trainingList = Preferences.getPreferences(this).getTrainings();

        trainingAdt = new TrainingAdapter(this, trainingList);
        trainingView.setAdapter(trainingAdt);
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
                break;
            case R.id.step3:
                intent = new Intent(this, RunActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addTraining(View v)
    {
        Training training = new Training();
        training.setId(trainingList.size() + 1);
        trainingList.add(training);
        Preferences.getPreferences(this).saveTrainings();
        trainingView.setAdapter(trainingAdt);
    }

    public void addStep(View v)
    {
        int position = (int) ((LinearLayout)v.getParent().getParent()).getTag();
        Training training = trainingList.get(position);
        training.addStep(new Step());
        Preferences.getPreferences(this).saveTrainings();
        v.requestLayout();
    }

    public void deleteTraining(View v)
    {
        int position = (int) ((LinearLayout)v.getParent().getParent()).getTag();
        trainingList.remove(position);
        Preferences.getPreferences(this).saveTrainings();
        v.requestLayout();
    }

}
