package fenrirmma.hreysti_app.user.Coach;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.Calendar;

import fenrirmma.hreysti_app.R;
import fenrirmma.hreysti_app.Utils.SessionAccess;
import fenrirmma.hreysti_app.Utils.WorkoutHelper;

public class getCoachScheduleActivity extends AppCompatActivity {

    private String date;
    private SessionAccess sa;
    private ArrayList<WorkoutHelper> list_workout;
    private Button btnGetSched;
    private RecyclerView recyclerView;
    private getCoachScheduleRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_coach_schedule);
        sa = SessionAccess.getInstance(this);
        btnGetSched = findViewById(R.id.btn_coach_sched);

        setExerciseDate();

        recyclerView = findViewById(R.id.recycle_view_coach_schedule);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        setExerciseDate();
        populateWorkoutList(date);

    }

    private void setExerciseDate() {
        btnGetSched.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR); // current year
            int mMonth = c.get(Calendar.MONTH); // current month
            int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

            DatePickerDialog datePickerDialog = new DatePickerDialog(getCoachScheduleActivity.this,
                    (view, year, monthOfYear, dayOfMonth) -> {
                        btnGetSched.setText(dayOfMonth + "-"
                                + (monthOfYear + 1) + "-" + year);
                        date =  year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        populateWorkoutList(date);
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        });
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
                        //no error appropriate
                    }else {
                        int code = result.get("error").getAsInt();
                        if (code != 0) {
                            //no error appropriate
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
                                        current.get("attending").getAsString(),
                                        current.get("coach_id").getAsString()
                                ));
                            }
                        }
                        adapter = new getCoachScheduleRecyclerAdapter(this, list_workout);
                        recyclerView.setAdapter(adapter);
                    }
                });
    }

    @Override
    public void onResume(){
        super.onResume();
        populateWorkoutList(date);
    }
}
