package tich.run.listener;

import android.view.View;
import tich.run.Preferences;
import tich.run.TrainingActivity;

public class StepDeleteListener implements View.OnClickListener {

    private TrainingActivity activity;
    private int trainingPosition;
    private int stepPosition;

    public StepDeleteListener(TrainingActivity activity, int trainingPosition, int stepPosition)
    {
        this.activity = activity;
        this.trainingPosition = trainingPosition;
        this.stepPosition = stepPosition;
    }

    @Override
    public void onClick(View v)
    {
        Preferences.getPreferences(activity).getTrainings().get(trainingPosition).getSteps().remove(stepPosition);
        Preferences.getPreferences(activity).saveTrainings();
        v.requestLayout();
    }
}
