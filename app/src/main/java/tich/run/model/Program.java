package tich.run.model;

import java.util.LinkedList;
import java.util.List;

public class Program {

    private int id;
    private LinkedList<Step> steps;

    public void addStep(Step step)
    {
        if (steps == null)
            steps = new LinkedList<Step>();
        steps.add(step);
    }

    public void addStep(int position, Step step)
    {
        if (steps == null)
            steps = new LinkedList<Step>();
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
}
