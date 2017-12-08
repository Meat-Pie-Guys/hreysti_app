package fenrirmma.hreysti_app.user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;

import java.util.Objects;

import fenrirmma.hreysti_app.R;
import fenrirmma.hreysti_app.login.MainActivity;
import fenrirmma.hreysti_app.login.SessionAccess;
import fenrirmma.hreysti_app.workout.exerciseOfTheDay;

public class clientActivity extends AppCompatActivity {

    private SessionAccess sa;
    private TextView client_ssn;
    private TextView client_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idkandi);

        sa = SessionAccess.getInstance(this);
        client_name = findViewById(R.id.client_name);
        client_ssn = findViewById(R.id.client_ssn);

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
                        Toast.makeText(this, "ION ERROR", Toast.LENGTH_SHORT).show(); //TODO breyta í eitthvað meira hot
                    }else {
                        int code = result.get("error").getAsInt();
                        if (code != 0) {
                            Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show(); //TODO breyta í eitthvað meira hot
                        }
                        else{
                            JsonObject user = result.get("user").getAsJsonObject();
                            client_name.setText(user.get("name").getAsString());
                            client_ssn.setText(user.get("ssn").getAsString());
                        }
                    }
                });
    }

    public void exerciseOfTheDay(View view){
        Intent intent = new Intent(clientActivity.this, exerciseOfTheDay.class);
        startActivity(intent);
    }

    public void signOut(View view) {
        sa.clearToken();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void changeName(View view) {

    }

}
