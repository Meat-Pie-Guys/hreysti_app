package fenrirmma.hreysti_app.workout;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import fenrirmma.hreysti_app.R;
import fenrirmma.hreysti_app.Utils.SessionAccess;
import fenrirmma.hreysti_app.Utils.DateConverter;
import fenrirmma.hreysti_app.Utils.UserHelper;

public class createWorkoutActivity extends AppCompatActivity {

    private SessionAccess sa;
    private EditText workout;
    private String _time;
    private TextView coach_name;
    private DatePickerDialog datePickerDialog;
    private List<UserHelper> list;
    private UserHelper coach;
    private ListView coachList;
    private Button date, submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        sa = SessionAccess.getInstance(this);
        workout = findViewById(R.id.workout);
        date = findViewById(R.id.btn_pick_date);
        submit = findViewById(R.id.submit);
        coach_name = findViewById(R.id.coach_name);
        coachList = findViewById(R.id.listCoachView);

        setDate();
        setTime();
        getList();
        coachList.setOnItemClickListener((parent, view, pos, id) -> {
            coach = (UserHelper)parent.getItemAtPosition(pos);
            coach_name.setText(coach.getName());

        });

    }

    private void getList() {
        list = new ArrayList<>();
        Ion.with(this)
                .load("GET", "http://10.0.2.2:5000/user/coaches")
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("fenrir-token", sa.getToken())
                .setTimeout(1000)
                .asJsonObject()
                .setCallback((e, result) -> {
                    if(e != null){
                        date.setError(null);
                        date.setError("Can't connect to the database!");
                    }else {
                        int code = result.get("error").getAsInt();
                        if (code != 0) {
                            date.setError(null);
                            date.setError("Access denied!");
                        }
                        else{
                            JsonArray users = result.getAsJsonArray("all_users");
                            for(int i = 0; i < users.size(); i++){
                                JsonObject current = users.get(i).getAsJsonObject();
                                list.add( new UserHelper(
                                        current.get("name").getAsString(),
                                        current.get("ssn").getAsString(),
                                        current.get("open_id").getAsString(),
                                        current.get("user_role").getAsString(),
                                        current.get("start_date").getAsString(),
                                        DateConverter.convert(current.get("expire_date").getAsString())
                                ));
                            }
                        }
                        coach = list.get(0);
                        ArrayAdapter<UserHelper> adapter = new ArrayAdapter<>(this,
                                android.R.layout.simple_list_item_1, list);
                        coachList.setAdapter(adapter);
                    }


                });
    }


    private void setDate() {

        date.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR); // current year
            int mMonth = c.get(Calendar.MONTH); // current month
            int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
            datePickerDialog = new DatePickerDialog(createWorkoutActivity.this,
                    (view, year, monthOfYear, dayOfMonth) -> {
                        date.setText(dayOfMonth + "/"
                                + (monthOfYear + 1) + "/" + year);

                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        });
    }

    private void setTime() {
        //The only times that workouts can occur
        String time[] = new String[]{"06:10", "10:30", "12:10", "17:15"};
        ArrayAdapter<String> aAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, time);
        Spinner spinner = findViewById(R.id.time_spinner);
        spinner.setAdapter(aAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                _time = parent.getItemAtPosition(position).toString();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void submitWorkout(View view) {

        JsonObject json = new JsonObject();
        json.addProperty("coach_id", coach.getOpenId());
        json.addProperty("description", workout.getText().toString());
        json.addProperty("date", date.getText().toString());
        json.addProperty("time", _time);
        Ion.with(this)
                .load("POST", "http://10.0.2.2:5000/workout")
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("fenrir-token", sa.getToken())
                .setTimeout(1000)
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback((e, result) -> {
                    if(e != null){
                        submit.setError(null);
                        submit.setError("Can't connect to the database!");
                    }else {
                        int code = result.get("error").getAsInt();
                        if (code != 0) {
                            switch (code){
                                case 11 :
                                    submit.setError(null);
                                    submit.setError("Access denied");
                                    break;
                                case 10 :
                                    submit.setError(null);
                                    submit.setError("Missing data");
                                    break;
                                case 5 :
                                    submit.setError(null);
                                    submit.setError("Fields cannot be empty");
                                    break;
                                case 17 :
                                    submit.setError(null);
                                    submit.setError("Workout with that time already exists");
                                    break;
                            }
                        }
                        else{
                            submit.setError(null);
                            Toast.makeText(this, "Workout successfully created for" + _time + ". If you want to create another workout for this day then select a different time", Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }
}
