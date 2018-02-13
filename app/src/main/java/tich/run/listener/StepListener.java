package tich.run.listener;

import android.view.View;
import android.widget.ImageView;

import tich.run.SpeedDialog;
import tich.run.TrainingActivity;
import tich.run.model.Step;

public class StepListener implements View.OnClickListener {

    private TrainingActivity activity;
    private Step step;

    public StepListener(TrainingActivity activity, Step step)
    {
        this.activity = activity;
        this.step = step;
    }

    @Override
    public void onClick(View v)
    {
        SpeedDialog dialog = new SpeedDialog(activity, step, (ImageView) v);
        dialog.show(activity.getSupportFragmentManager(), "sp");

    }
}
