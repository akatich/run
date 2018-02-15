package tich.run.listener;

import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import tich.run.Preferences;
import tich.run.TrainingActivity;
import tich.run.model.Step;

public class StepLengthListener implements TextWatcher {

    private AppCompatActivity activity;
    private Step step;

    public StepLengthListener(AppCompatActivity activity, Step step)
    {
        this.activity = activity;
        this.step = step;
    }

    @Override
    public void afterTextChanged(Editable s) {}

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
        if(s.length() != 0)
        {
            step.setLength(Integer.parseInt(s.toString()));
            Preferences.getPreferences(activity).saveTrainings();
        }
    }
}
