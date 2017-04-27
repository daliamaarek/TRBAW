package tweets.utils;

import java.util.ArrayList;

/**
 * Created by daliamaarek on 31/05/16.
 */
public class UsersVennDiagram {

    ArrayList<Integer> values; //
    ArrayList<Integer> join;
    String text;


    public UsersVennDiagram(int values, int joined, String text) {
        this.values = new ArrayList<Integer>();
        this.values.add(values);
        this.join = new ArrayList<Integer>();
        this.join.add(joined);
        this.text = text;
    }
    public UsersVennDiagram(ArrayList<Integer> values, ArrayList<Integer> joined, String text) {
        this.values = values;
        this.join = joined;
        this.text = text;
    }

    public ArrayList<Integer> getValues() {
        return values;
    }

    public void setValues(ArrayList<Integer> values) {
        this.values = values;
    }

    public ArrayList<Integer> getJoin() {
        return join;
    }

    public void setJoin(ArrayList<Integer> join) {
        this.join = join;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
