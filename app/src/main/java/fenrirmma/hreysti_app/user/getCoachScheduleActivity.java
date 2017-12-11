package fenrirmma.hreysti_app.user;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import fenrirmma.hreysti_app.R;
import fenrirmma.hreysti_app.login.SessionAccess;
import fenrirmma.hreysti_app.workout.CustomAdapter;
import fenrirmma.hreysti_app.workout.WorkoutHelper;

public class getCoachScheduleActivity extends AppCompatActivity {

    private String date;
    private DatePicker coachDate;
    private ListView coachList;
    private SessionAccess sa;
    private ArrayList<WorkoutHelper> list_workout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_coach_schedule);
        sa = SessionAccess.getInstance(this);
        coachDate = findViewById(R.id.coach_datepicker);
        coachList = findViewById(R.id.coach_listview);
        makeDateString();
        populateWorkoutList(date);
        Calendar calendar = Calendar.getInstance();
        coachDate.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {
                date = year + "-" + (month+1) + "-" + dayOfMonth;
                populateWorkoutList(date);
            }
        });

    }

    private void makeDateString() {
        int day = coachDate.getDayOfMonth();
        int month = coachDate.getMonth() + 1;
        int year = coachDate.getYear();
        date = year + "-" + month + "-" + day;
    }


    private void populateWorkoutList(String date) {
        list_workout = new ArrayList<>();
        Ion.with(this)
                .load("GET", "http://10.0.2.2:5000/workout/coach/" + date)
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

                        coachList.setAdapter(new CustomAdapter(this, list_workout));
                    }


                });

    }


}
