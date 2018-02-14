package tich.run.model;

import java.util.LinkedList;
import java.util.ListIterator;

public class Training {

    private int id;
    private LinkedList<Step> steps = new LinkedList<Step>();

    public void addStep(Step step)
    {
        step.setId(steps.size() + 1);
        steps.add(step);
    }

    public void addStep(int position, Step step)
    {
        steps.add(position, step);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LinkedList<Step> getSteps() {
        return steps;
    }

    public void setSteps(LinkedList<Step> steps) {
        this.steps = steps;
    }

    public int getLength()
    {
        int length = 0;
        ListIterator<Step> iter = steps.listIterator();
        while (iter.hasNext())
        {
            Step step = iter.next();
            length += step.getLength();
        }
        return length;
    }
}
