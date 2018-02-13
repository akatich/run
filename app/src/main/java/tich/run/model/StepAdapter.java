package tich.run.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;

import tich.run.R;

public class StepAdapter extends BaseAdapter {

    private LinkedList<Step> steps;
    private LayoutInflater stepsInf;

    public StepAdapter(Context c, LinkedList<Step> theSteps){
        steps = theSteps;
        stepsInf = LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return steps.size();
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
        //map to step layout
        LinearLayout stepsLay = (LinearLayout)stepsInf.inflate
                (R.layout.step, parent, false);

        TextView stepIdView = (TextView)stepsLay.findViewById(R.id.step_id);
        TextView stepLengthView = (TextView)stepsLay.findViewById(R.id.step_length);
        TextView stepSpeedView = (TextView)stepsLay.findViewById(R.id.step_speed);

        Step currStep = steps.get(position);

        stepIdView.setText("Step=" + currStep.getId());
        stepLengthView.setText("XXmin");
        stepSpeedView.setText("SPEED");

        //set position as tag
        stepsLay.setTag(position);

        return stepsLay;
    }
}
