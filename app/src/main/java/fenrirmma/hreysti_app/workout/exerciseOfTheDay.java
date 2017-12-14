package fenrirmma.hreysti_app.workout;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import fenrirmma.hreysti_app.R;
import fenrirmma.hreysti_app.Utils.WorkoutHelper;
import fenrirmma.hreysti_app.Utils.SessionAccess;


public class exerciseOfTheDay extends AppCompatActivity {

    private SessionAccess sa;
    private TextView todays_workout;
    private ListView workout_list;
    private ArrayList<WorkoutHelper> list_workout;
    private String date;
    private Button btnDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_of_the_day);
        todays_workout = findViewById(R.id.workout_day);
        workout_list = findViewById(R.id.list_workouts);

        sa = SessionAccess.getInstance(this);
        btnDate = findViewById(R.id.btn_exercise_date);

        Calendar calendar = Calendar.getInstance();

        Date now = new Date();
        setExerciseDate();
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
        String currentDateTime = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH)+1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
        String today = calendar.get(Calendar.DAY_OF_MONTH) + "-" + (calendar.get(Calendar.MONTH)+1) + "-" + calendar.get(Calendar.YEAR) ;
        date = currentDateTime;
        btnDate.setText(today);



        workout_list.setOnItemClickListener((parent, view, pos, id) -> {

            WorkoutHelper curr = (WorkoutHelper) parent.getItemAtPosition(pos);
            if(Objects.equals(sa.getRole(), "Client")){
                try {
                    Date dateGot = formatter.parse(curr.getDate());
                    if(now.before(dateGot)){
                        participateInWorkout(curr.getOpen_id());
                        populateWorkoutList(date);
                        displayWorkout(date + "-06-10");
                    }
                    else{
                        Toast.makeText(this, "Can't attend workouts that are already started", Toast.LENGTH_SHORT).show();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
            else{
                Intent intent = new Intent(this, attendingUsersActivity.class);
                intent.putExtra("ID", curr.getOpen_id());
                startActivity(intent);

            }
        });
        populateWorkoutList(currentDateTime + "-06-10");
        displayWorkout(currentDateTime + "-06-10");
    }

    public void updateDate(View view) {
        String date = btnDate.getText().toString();
        String split[] = date.split("-");
        String day = split[0];
        String month = split[0];
        String year = split[0];
        String _date = year + "-" + month + "-" + day;
        populateWorkoutList(_date + "-06-10");
        displayWorkout(_date + "-06-10");
    }

    private void setExerciseDate() {
        btnDate.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR); // current year
            int mMonth = c.get(Calendar.MONTH); // current month
            int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

            DatePickerDialog datePickerDialog = new DatePickerDialog(exerciseOfTheDay.this,
                    (view, year, monthOfYear, dayOfMonth) -> {
                        btnDate.setText(dayOfMonth + "-"
                                + (monthOfYear + 1) + "-" + year);
                        date =  year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        displayWorkout(date + "-06-10");
                        populateWorkoutList(date);
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        });
    }

    public void populateWorkoutList(String date) {
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
                        todays_workout.setError(null);
                        todays_workout.setError("Cannot connect to database");
                    }else {
                        int code = result.get("error").getAsInt();
                        if (code != 0) {
                            todays_workout.setError(null);
                        }
                        else{
                            todays_workout.setError(null);
                            JsonArray users = result.getAsJsonArray("all_workouts");
                            for(int i = 0; i < users.size(); i++){
                                JsonObject current = users.get(i).getAsJsonObject();
                                list_workout.add( new WorkoutHelper(
                                        current.get("id").getAsString(),
                                        current.get("coach_name").getAsString(),
                                        current.get("description").getAsString(),
                                        current.get("date_time").getAsString(),
                                        current.get("attending").getAsString(),
                                        current.get("coach_id").getAsString()
                                ));
                            }
                        }
                        workout_list.setAdapter(new CustomAdapter(this, list_workout));
                    }
                });
        list_workout.clear();

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
                        todays_workout.setError(null);
                        todays_workout.setError("Cannot connect to database");
                    }else {
                        int code = result.get("error").getAsInt();
                        if (code != 0) {
                            todays_workout.setError(null);
                            todays_workout.setError("Invalid date");
                        }
                        else{
                            todays_workout.setError(null);
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
                        todays_workout.setError(null);
                        todays_workout.setError("Cannot connect to database");
                    }else {
                        int code = result.get("error").getAsInt();
                        if (code != 0) {
                            if(code == 15){
                                todays_workout.setError(null);
                                todays_workout.setError("Workout is full");
                            } else{
                                todays_workout.setError(null);
                                todays_workout.setError("No such workout exists");
                            }

                        }
                        else{
                            todays_workout.setError(null);
                        }
                    }
                });
    }

    @Override
    public void onResume(){
        super.onResume();
        populateWorkoutList(date);
    }

}
