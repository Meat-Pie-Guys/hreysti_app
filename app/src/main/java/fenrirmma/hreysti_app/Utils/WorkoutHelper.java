package fenrirmma.hreysti_app.Utils;

import java.util.Locale;

/**
 * Created by Notandi on 10.12.2017.
 */

public class WorkoutHelper {

    private String open_id;
    private String coach_name;
    private String description;
    private String date;
    private String attending;
    private String coach_id;


    public WorkoutHelper(String open_id, String coach_name, String description, String date, String attending, String coach_id){
        this.open_id = open_id;
        this.coach_name = coach_name;
        this.description = description;
        this.date = date;
        this.attending = attending;
        this.coach_id = coach_id;
    }

    public String getCoach_id() {
        return coach_id;
    }
    public String getOpen_id() {
        return open_id;
    }

    public String getCoach_name() {

        return coach_name;
    }

    public String getAttending() {
        return attending;
    }
    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {

        return _getTime(date);
    }

    public String _getTime(String date){
        String split[] = date.split(" ");
        String time = split[4];
        String split2 [] = time.split(":");
        String hour = split2[0];
        String mins = split2[1];
        String secs = split2[2];
        StringBuilder sb = new StringBuilder();
        sb.append(hour).append(":").append(mins);
        return sb.toString();
    }

}
