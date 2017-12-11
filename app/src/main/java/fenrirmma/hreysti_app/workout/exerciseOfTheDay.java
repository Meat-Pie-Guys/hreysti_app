package fenrirmma.hreysti_app.workout;


import android.content.Intent;
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
import fenrirmma.hreysti_app.user.Admin.userInfoAdminActivity;
import fenrirmma.hreysti_app.user.clientActivity;

public class exerciseOfTheDay extends AppCompatActivity {

    private SessionAccess sa;
    private TextView todays_workout, date_view;
    private ListView workout_list;
    ArrayAdapter<WorkoutHelper> adapter;
    private String time;
    private ArrayList<WorkoutHelper> list_workout;
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

        workout_list.setOnItemClickListener((parent, view, pos, id) -> {
            //if user is client then attending workout
            //if user is coach/admin then they can update
            WorkoutHelper curr = (WorkoutHelper) parent.getItemAtPosition(pos);
            participateInWorkout(curr.getOpen_id());
            setTime();

        });


    }

    private void setTime() {
        DateTime dt = new DateTime();
        int dow = dt.getDayOfWeek();

        LocalDate today = LocalDate.now();
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        StringBuilder sb_display_date = new StringBuilder();
        if(Integer.parseInt(DateConverter.parser(today.toDateTimeAtCurrentTime().toString())) < 21){
            day = today.getDayOfMonth();
            month = today.getMonthOfYear();
            year = today.getYear();
            sb_display_date.append(Integer.toString(day) + "/" + Integer.toString(month) + "/" + Integer.toString(year));
        } else{
            day = tomorrow.getDayOfMonth();
            month = tomorrow.getMonthOfYear();
            year = tomorrow.getYear();
            sb_display_date.append(Integer.toString(day) + "/" + Integer.toString(month) + "/" + Integer.toString(year));
        }

        date_view.setText(sb_display_date.toString());
        StringBuilder sb = new StringBuilder();
        //sunday, closed....send motivational quote
        if(dow == 7){
            sb.append("Það er lokað í dag. En mundu að:\n" + RandomMotivational.getRandomQuote());
            todays_workout.setText(sb.toString());
            //todays_workout.setText(DateConverter.parser(today.toDateTimeAtCurrentTime().toString()));
            /*time = "12-10";
            sb.append(Integer.toString(year) + "-" + Integer.toString(month) + "-" + Integer.toString(day) + "-" + time);
            String date = sb.toString();
            displayWorkout(date);*/

        } else if(dow == 6){
            //saturday...only one training
            //get time
            time = "10-30";
            //get date
            sb.append(Integer.toString(year) + "-" + Integer.toString(month) + "-" + Integer.toString(day) + "-" + time);
            String date = sb.toString();
            displayWorkout(date);
            populateWorkoutList(date);

        } else{
            //normal weekday...3 trainings
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
                        Toast.makeText(this, "ION ERROR cock", Toast.LENGTH_SHORT).show(); //TODO breyta í eitthvað meira hot
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
                                        current.get("coach_name").getAsString(),
                                        current.get("description").getAsString(),
                                        current.get("date_time").getAsString(),
                                        current.get("attending").getAsString()
                                ));
                            }
                        }

                        workout_list.setAdapter(new CustomAdapter(this, list_workout));
                    }


                });

    }

    private void displayWorkout(String date) {

        Ion.with(this)
                .load("GET", "http://10.0.2.2:5000/workout/today/" + date)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("fenrir-token", sa.getToken())
                .setTimeout(1000)
                .asJsonObject()
                .setCallback((e, result) -> {
                    if(e != null){
                        Toast.makeText(this, "ION ERROR not cock", Toast.LENGTH_SHORT).show(); //TODO breyta í eitthvað meira hot
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

    private void participateInWorkout(String workoutId){
        Ion.with(this)
                .load("GET", "http://10.0.2.2:5000/workout/" + workoutId)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("fenrir-token", sa.getToken())
                .setTimeout(1000)
                .asJsonObject()
                .setCallback((e, result) -> {
                    if(e != null){
                        Toast.makeText(this, "ION ERROR not cock", Toast.LENGTH_SHORT).show(); //TODO breyta í eitthvað meira hot
                    }else {
                        int code = result.get("error").getAsInt();
                        if (code != 0) {
                            Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show(); //TODO breyta í eitthvað meira hot
                        }
                        else{
                            Toast.makeText(this, "ATTENDED WORKOUT", Toast.LENGTH_SHORT).show(); //TODO breyta í eitthvað meira hot
                        }
                    }
                });
    }

}
