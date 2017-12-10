package fenrirmma.hreysti_app.workout;

/**
 * Created by Notandi on 10.12.2017.
 */

public class WorkoutHelper {

    private String open_id;
    private String coach_id;
    private String description;
    private String date;

    public WorkoutHelper(String open_id, String coach_id, String description, String date){
        this.open_id = open_id;
        this.coach_id = coach_id;
        this.description = description;
        this.date = date;
    }

    public String getOpen_id() {
        return open_id;
    }

    public String getCoach_id() {
        return coach_id;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }
}
