package tich.run.model;

import android.graphics.Color;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;

import tich.run.Preferences;
import tich.run.R;
import tich.run.TrainingActivity;
import tich.run.listener.StepDeleteListener;
import tich.run.listener.StepLengthListener;
import tich.run.listener.StepSpeedListener;

public class TrainingAdapter extends BaseAdapter {

    private ArrayList<Training> trainings;
    private LayoutInflater trainingInf;
    private TrainingActivity activity;
    private StepAdapter stepAdt;

    public TrainingAdapter(TrainingActivity ta, ArrayList<Training> theTrainings){
        trainings = theTrainings;
        activity = ta;
        trainingInf = LayoutInflater.from(activity);
    }

    @Override
    public int getCount() {
        return trainings.size();
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
        //map to training layout
        LinearLayout trainingLay = (LinearLayout)trainingInf.inflate
                (R.layout.training, parent, false);

        Training currTraining = trainings.get(position);

        TextView trainingIdView = (TextView)trainingLay.findViewById(R.id.training_id);
        trainingIdView.setText("Programme " + currTraining.getId() + " : ");

        TextView trainingLengthView = (TextView)trainingLay.findViewById(R.id.training_length);
        trainingLengthView.setText(Integer.toString(currTraining.getLength()) + " min");

        //map the steps of the training
        LinearLayout stepsListLayout = (LinearLayout)trainingLay.findViewById(R.id.steps_list);
        LinkedList stepsList = trainings.get(position).getSteps();
        /*stepAdt = new StepAdapter(activity, stepsList);
        stepsView.setAdapter(stepAdt);*/
        ListIterator<Step> iter = stepsList.listIterator();
        while (iter.hasNext())
        {
            Step step = iter.next();
            LinearLayout stepLayout = new LinearLayout(activity);
            stepLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    Preferences.pxFromDp(40, activity)));
            stepLayout.setGravity(Gravity.CENTER_VERTICAL);

            ImageView imgDelete = new ImageView(activity);
            imgDelete.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            ((LinearLayout.LayoutParams)imgDelete.getLayoutParams()).leftMargin = Preferences.pxFromDp(5, activity);
            imgDelete.setImageResource(R.drawable.delete);
            imgDelete.setOnClickListener(new StepDeleteListener(activity, position, step.getId() - 1));
            stepLayout.addView(imgDelete);

            TextView stepIdView = new TextView(activity);
            stepIdView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            ((LinearLayout.LayoutParams)stepIdView.getLayoutParams()).leftMargin = Preferences.pxFromDp(20, activity);
            stepIdView.setTextColor(Color.parseColor("#FFFFFF99"));
            stepIdView.setText("Step " + step.getId() + " : ");
            stepLayout.addView(stepIdView);

            EditText stepLengthView = new EditText(activity);
            stepLengthView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            ((LinearLayout.LayoutParams)stepLengthView.getLayoutParams()).leftMargin = Preferences.pxFromDp(10, activity);
            stepLengthView.setTextColor(Color.parseColor("#FFFFFF99"));
            stepLengthView.setInputType(InputType.TYPE_CLASS_NUMBER);
            stepLengthView.setText(Integer.toString(step.getLength()));
            stepLengthView.addTextChangedListener(new StepLengthListener(activity, step));
            stepLayout.addView(stepLengthView);

            TextView stepLengthTextView = new TextView(activity);
            stepLengthTextView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f));
            ((LinearLayout.LayoutParams)stepLengthTextView.getLayoutParams()).leftMargin = Preferences.pxFromDp(10, activity);
            stepLengthTextView.setTextColor(Color.parseColor("#FFFFFF99"));
            stepLengthTextView.setText("min");
            stepLayout.addView(stepLengthTextView);

            ImageView stepSpeedView = new ImageView(activity);
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
            stepSpeedView.setOnClickListener(new StepSpeedListener(activity, step));
            stepLayout.addView(stepSpeedView);
            stepsListLayout.addView(stepLayout);
        }

        LinearLayout addNewStepLayout = new LinearLayout(activity);
        addNewStepLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                Preferences.pxFromDp(35, activity)));
        ((LinearLayout.LayoutParams)addNewStepLayout.getLayoutParams()).leftMargin = Preferences.pxFromDp(30, activity);
        addNewStepLayout.setGravity(Gravity.CENTER_VERTICAL);
        addNewStepLayout.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v)
            {
                activity.addStep(v);
            }
        });

        ImageView imgPlus = new ImageView(activity);
        imgPlus.setLayoutParams(new LinearLayout.LayoutParams(
                Preferences.pxFromDp(25, activity),
                Preferences.pxFromDp(25, activity)));
        imgPlus.setImageResource(R.drawable.plus);
        addNewStepLayout.addView(imgPlus);

        TextView addNewStep = new TextView(activity);
        addNewStep.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        ((LinearLayout.LayoutParams)addNewStep.getLayoutParams()).leftMargin = 40;
        addNewStep.setTextColor(Color.parseColor("#FFFFFF99"));
        addNewStep.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        addNewStep.setText("Nouvelle étape");
        addNewStepLayout.addView(addNewStep);

        stepsListLayout.addView(addNewStepLayout);

        //set position as tag
        trainingLay.setTag(position);

        return trainingLay;
    }
}
