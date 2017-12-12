package fenrirmma.hreysti_app.workout;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import fenrirmma.hreysti_app.R;
import fenrirmma.hreysti_app.login.SessionAccess;
import fenrirmma.hreysti_app.user.Admin.DateConverter;
import fenrirmma.hreysti_app.user.Admin.UserHelper;
import fenrirmma.hreysti_app.user.Admin.userInfoAdminActivity;
import fenrirmma.hreysti_app.user.clientActivity;

public class exerciseOfTheDay extends AppCompatActivity {

    private SessionAccess sa;
    private TextView todays_workout;
    private ListView workout_list;
    private DatePicker exercisePicker;
    private String time, role;
    private ArrayList<WorkoutHelper> list_workout;
    private int day, month, year;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_of_the_day);

        todays_workout = findViewById(R.id.workout_day);
        workout_list = findViewById(R.id.list_workouts);
        role = getIntent().getStringExtra("ROLE");
        sa = SessionAccess.getInstance(this);
        exercisePicker = findViewById(R.id.eotd_date_picker);
        Calendar calendar = Calendar.getInstance();
        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
        String currentDateTime = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH)+1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
        workout_list.setOnItemClickListener((parent, view, pos, id) -> {
            // TODO if user is client then attending workout
            // TODO if user is coach/admin then they can update
            WorkoutHelper curr = (WorkoutHelper) parent.getItemAtPosition(pos);
            if(Objects.equals(role, "Client")){
                try {
                    Date dateGot = formatter.parse(curr.getDate());
                    System.out.println(dateGot.toString());
                    System.out.println(now.toString());
                    if(now.after(dateGot)){
                        participateInWorkout(curr.getOpen_id());
                        populateWorkoutList(date);
                        displayWorkout(date);
                    }
                    else{
                        // TODO láta user vita að þetta var ekkihægt því þetta var gömul dagsetning?
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
            else{
                Intent intent = new Intent(this, updateWorkoutActivity.class);
                intent.putExtra("ID", curr.getOpen_id());
                intent.putExtra("coach_name", curr.getCoach_name());
                intent.putExtra("description", curr.getDescription());
                intent.putExtra("date", curr.getDate());
                intent.putExtra("time", curr.getTime());
                startActivity(intent);
            }
        });
        displayWorkout(currentDateTime +  "-06-10");
        populateWorkoutList(currentDateTime + "-06-10");
        exercisePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {
                date = year + "-" + (month+1) + "-" + dayOfMonth;
                displayWorkout(date + "-06-10");
                populateWorkoutList(date + "-06-10");
            }
        });


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
