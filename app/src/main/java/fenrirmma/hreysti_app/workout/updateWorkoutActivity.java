package fenrirmma.hreysti_app.workout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;
import java.util.ArrayList;
import fenrirmma.hreysti_app.R;
import fenrirmma.hreysti_app.Utils.SessionAccess;
import fenrirmma.hreysti_app.Utils.DateConverter;
import fenrirmma.hreysti_app.Utils.UserHelper;

public class updateWorkoutActivity extends AppCompatActivity {

    private SessionAccess sa;
    private EditText workout;
    private TextView date, coach_name, time;
    private ArrayList<UserHelper> myList;
    private ListView list_coaches;
    private updateWorkoutAdapter adapter;
    private String coach;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_workout);

        sa = SessionAccess.getInstance(this);
        workout = findViewById(R.id.workout);
        list_coaches = findViewById(R.id.listCoachView);
        date = findViewById(R.id.date);
        coach_name = findViewById(R.id.coach_name);
        time = findViewById(R.id._time);
        setList();
        getList();


        list_coaches.setOnItemClickListener((parent, view, pos, id) -> {
            UserHelper curr = (UserHelper) parent.getItemAtPosition(pos);
            coach = curr.getOpenId();
            coach_name.setText(curr.getName());

        });
    }

    private void setList() {
        workout.setText(getIntent().getStringExtra("description"));
        coach_name.setText(getIntent().getStringExtra("coach_name"));
        time.setText(getIntent().getStringExtra("time"));
        date.setText(DateConverter.convert(getIntent().getStringExtra("date")));
        coach = getIntent().getStringExtra("coach_id");
    }

    private void getList() {
        myList = new ArrayList<>();
        Ion.with(this)
                .load("GET", "http://10.0.2.2:5000/user/coaches")
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("fenrir-token", sa.getToken())
                .setTimeout(1000)
                .asJsonObject()
                .setCallback((e, result) -> {
                    if(e != null){
                        workout.setError(null);
                        workout.setError("Cannot connect to database");
                    }else {
                        int code = result.get("error").getAsInt();
                        if (code != 0) {
                            workout.setError(null);
                            workout.setError("Access denied");

                        }
                        else{
                            workout.setError(null);
                            JsonArray users = result.getAsJsonArray("all_users");
                            for(int i = 0; i < users.size(); i++){
                                JsonObject current = users.get(i).getAsJsonObject();
                                myList.add( new UserHelper(
                                        current.get("name").getAsString(),
                                        current.get("ssn").getAsString(),
                                        current.get("open_id").getAsString(),
                                        current.get("user_role").getAsString(),
                                        current.get("start_date").getAsString(),
                                        DateConverter.convert(current.get("expire_date").getAsString())
                                ));
                            }
                            adapter = new updateWorkoutAdapter(this, myList);
                            list_coaches.setAdapter(adapter);
                        }
                    }
                });
    }

    public void submitWorkout(View view) {
        String workout_ID = getIntent().getStringExtra("id");
        JsonObject json = new JsonObject();
        json.addProperty("coach_id", coach);
        json.addProperty("description", workout.getText().toString());
        Ion.with(this)
                .load("PUT", "http://10.0.2.2:5000/admin/workout/update/" + workout_ID)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("fenrir-token", sa.getToken())
                .setTimeout(1000)
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback((e, result) -> {
                    if(e != null){
                        workout.setError(null);
                        workout.setError("Cannot connect to database");
                    }else {
                        int code = result.get("error").getAsInt();
                        if (code != 0) {
                            switch (code){
                                case 11 :
                                    workout.setError(null);
                                    workout.setError("Access denied");
                                    break;
                                case 14 :
                                    workout.setError(null);
                                    workout.setError("There is no such workout in the database");
                                    break;
                                case 10 :
                                    workout.setError(null);
                                    workout.setError("Missing data");
                                    break;
                                case 5 :
                                    workout.setError(null);
                                    workout.setError("Empty data");
                                    break;
                            }
                        }
                        else{
                            workout.setError(null);
                        }
                    }
                });
        finish();
    }
}
