package fenrirmma.hreysti_app.workout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import fenrirmma.hreysti_app.R;
import fenrirmma.hreysti_app.Utils.SessionAccess;
import fenrirmma.hreysti_app.Utils.DateConverter;
import fenrirmma.hreysti_app.Utils.UserHelper;
import fenrirmma.hreysti_app.Utils.WorkoutHelper;

public class updateWorkoutActivity extends AppCompatActivity {

    private SessionAccess sa;
    private EditText workout;
    private TextView date, coach_name, time;
    private ArrayList<UserHelper> myList;
    private UserHelper coaches;
    private ListView list_coaches;
    private updateWorkoutAdapter adapter;
    private RecyclerView recyclerView;
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
            // TODO if user is client then attending workout
            // TODO if user is coach/admin then they can update
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
                        Toast.makeText(this, "ION ERROR", Toast.LENGTH_SHORT).show(); //TODO breyta í eitthvað meira hot
                    }else {
                        int code = result.get("error").getAsInt();
                        if (code != 0) {
                            Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show(); //TODO breyta í eitthvað meira hot
                        }
                        else{
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
                        Toast.makeText(this, "ION ERROR", Toast.LENGTH_SHORT).show(); //TODO breyta í eitthvað meira hot
                    }else {
                        int code = result.get("error").getAsInt();
                        if (code != 0) {
                            Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show(); //TODO breyta í eitthvað meira hot
                        }
                        else{
                            Toast.makeText(this, "SUCCESS", Toast.LENGTH_SHORT).show();
                            // TODO Eitthvað success skilabð betra en Toastið
                        }
                    }
                });
        finish();
    }
}
