package fenrirmma.hreysti_app.workout;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.List;

import fenrirmma.hreysti_app.R;
import fenrirmma.hreysti_app.login.SessionAccess;
import fenrirmma.hreysti_app.user.Admin.DateConverter;
import fenrirmma.hreysti_app.user.Admin.UserHelper;
import fenrirmma.hreysti_app.user.clientActivity;

public class exerciseOfTheDay extends AppCompatActivity {

    private SessionAccess sa;
    private TextView todays_workout, date_view;
    private ListView workout_list;
    private String time;
    private List<WorkoutHelper> list_workout;
    private int day, month, year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_of_the_day);

        todays_workout = findViewById(R.id.workout_day);
        workout_list = findViewById(R.id.list_workouts);
        date_view = findViewById(R.id.date_view);

        sa = SessionAccess.getInstance(this);
        setTime();

    }

    private void setTime() {
        DateTime dt = new DateTime();
        int dow = dt.getDayOfWeek();

        LocalDate today = LocalDate.now();
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        if(Integer.parseInt(DateConverter.parser(today.toDateTimeAtCurrentTime().toString())) < 21){
            day = today.getDayOfMonth();
            month = today.getMonthOfYear();
            year = today.getYear();
            date_view.setText(today.toString());
        } else{
            day = tomorrow.getDayOfMonth();
            month = tomorrow.getMonthOfYear();
            year = tomorrow.getYear();
        }

        StringBuilder sb = new StringBuilder();

        if(dow == 7){
            //sunday, closed....send motivational quote
            sb.append("Það er lokað í dag. En mundu að:\n" + RandomMotivational.getRandomQuote());
            todays_workout.setText(sb.toString());
            /*time = "06-10";
            sb.append(Integer.toString(year) + "-" + Integer.toString(month) + "-" + Integer.toString(day) + "-" + time);
            String date = sb.toString();
            displayWorkout(date);*/
        } else if(dow == 6){
            //get time
            time = "10-30";
            //get date
            sb.append(Integer.toString(year) + "-" + Integer.toString(month) + "-" + Integer.toString(day) + "-" + time);
            String date = sb.toString();
            displayWorkout(date);
            populateWorkoutList(date);
        } else{
            //all other days
            //get time
            time = "06-10";
            //get date
            sb.append(Integer.toString(year) + "-" + Integer.toString(month) + "-" + Integer.toString(day) + "-" + time);
            String date = sb.toString();
            displayWorkout(date);
            populateWorkoutList(date);
        }

    }

    private void populateWorkoutList(String date) {
        list_workout = new ArrayList<>();
        Ion.with(this)
                .load("GET", "http://10.0.2.2:5000/workout/all/" + date)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("fenrir-token", sa.getToken())
                .setTimeout(1000)
                .asJsonObject()
                .setCallback((e, result) -> {
                    if(e != null){
                        Toast.makeText(this, "ION ERROR", Toast.LENGTH_SHORT).show(); //TODO breyta í eitthvað meira hot
                    }else {
                        int code = result.get("error").getAsInt();
                        if (code != 0) {
                            Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show(); //TODO breyta í eitthvað meira hot
                        }
                        else{
                            JsonArray users = result.getAsJsonArray("all_workouts");
                            for(int i = 0; i < users.size(); i++){
                                JsonObject current = users.get(i).getAsJsonObject();
                                list_workout.add( new WorkoutHelper(
                                        current.get("id").getAsString(),
                                        current.get("coach_id").getAsString(),
                                        current.get("description").getAsString(),
                                        current.get("date").getAsString()
                                ));
                            }
                        }
                        ArrayAdapter<WorkoutHelper> adapter = new ArrayAdapter<>(this,
                                android.R.layout.simple_list_item_1, list_workout);
                        workout_list.setAdapter(adapter);
                    }


                });

    }

    private void displayWorkout(String date) {

        Ion.with(this)
                .load("GET", "http://10.0.2.2:5000/workout/" + date)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("fenrir-token", sa.getToken())
                .setTimeout(1000)
                .asJsonObject()
                .setCallback((e, result) -> {
                    if(e != null){
                        Toast.makeText(this, "ION ERROR", Toast.LENGTH_SHORT).show(); //TODO breyta í eitthvað meira hot
                    }else {
                        int code = result.get("error").getAsInt();
                        if (code != 0) {
                            Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show(); //TODO breyta í eitthvað meira hot
                        }
                        else{
                            JsonObject user = result.get("workout").getAsJsonObject();
                            todays_workout.setText(user.get("description").getAsString());
                        }
                    }
                });

    }

}
