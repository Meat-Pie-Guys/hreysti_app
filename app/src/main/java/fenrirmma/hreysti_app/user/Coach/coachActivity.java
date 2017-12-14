package fenrirmma.hreysti_app.user.Coach;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;

import fenrirmma.hreysti_app.R;
import fenrirmma.hreysti_app.login.MainActivity;
import fenrirmma.hreysti_app.Utils.SessionAccess;
import fenrirmma.hreysti_app.workout.createWorkoutActivity;
import fenrirmma.hreysti_app.workout.exerciseOfTheDay;

public class coachActivity extends AppCompatActivity {

    private SessionAccess sa;
    private EditText new_name;
    private TextView coach_ssn, coach_name;
    private Button change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thjalfari);

        sa = SessionAccess.getInstance(this);
        new_name = findViewById(R.id.new_name);
        coach_name = findViewById(R.id.coach_name_show);
        coach_ssn = findViewById(R.id.coach_ssn);
        change = findViewById(R.id.btn_change_name);

        populateView();
    }

    private void populateView() {
        Ion.with(this)
                .load("GET", "http://10.0.2.2:5000/get_user")
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("fenrir-token", sa.getToken())
                .setTimeout(1000)
                .asJsonObject()
                .setCallback((e, result) -> {
                    if(e != null){
                        clearError();
                        new_name.setError("Can't connect to the database!");
                    }else {
                        int code = result.get("error").getAsInt();
                        if (code != 0) {
                            clearError();
                            new_name.setError("No such user");
                        }
                        else{
                            JsonObject user = result.get("user").getAsJsonObject();
                            coach_name.setText(user.get("name").getAsString());
                            coach_ssn.setText(user.get("ssn").getAsString());
                        }
                    }
                });
    }

    private void clearError() {
        new_name.setError(null);
    }

    public void exerciseOfTheDay(View view){
        Intent intent = new Intent(coachActivity.this, exerciseOfTheDay.class);
        startActivity(intent);
    }

    public void MakeWorkout(View view) {
        Intent intent = new Intent(coachActivity.this, createWorkoutActivity.class);
        startActivity(intent);
    }

    public void signOut(View view) {
        sa.clearToken();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }


    public void editName(View view) {
        JsonObject json = new JsonObject();
        String name = new_name.getText().toString();
        if(name.trim().length() == 0){
            clearError();
            new_name.setError("Name cannot be only white space!");
        }
        else {
            json.addProperty("name", name);
            Ion.with(this)
                    .load("PUT", "http://10.0.2.2:5000/user/name/update")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .addHeader("fenrir-token", sa.getToken())
                    .setTimeout(1000)
                    .setJsonObjectBody(json)
                    .asJsonObject()
                    .setCallback((e, result) -> {
                        if (e != null) {
                            clearError();
                            new_name.setError("Can't connect to the database!");
                        } else {
                            int code = result.get("error").getAsInt();
                            if (code != 0) {
                                clearError();
                                if (code == 5) {
                                    new_name.setError("Name cannot be empty");
                                }
                                clearError();
                                new_name.setError("Name missing");
                            } else {
                                Toast.makeText(this, "Name changed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            this.recreate();
        }
    }

    public void getSchedule(View view) {
        Intent intent = new Intent(this, getCoachScheduleActivity.class);
        startActivity(intent);
    }
}
