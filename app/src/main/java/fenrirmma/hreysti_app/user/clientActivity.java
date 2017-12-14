package fenrirmma.hreysti_app.user;

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
import fenrirmma.hreysti_app.workout.exerciseOfTheDay;

public class clientActivity extends AppCompatActivity {

    private SessionAccess sa;
    private TextView client_ssn, client_name_show_card, client_ssn_show_card, client_role_show_card;
    private EditText client_name;
    private String role;
    private Button change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idkandi);

        sa = SessionAccess.getInstance(this);
        client_name = findViewById(R.id.client_name);
        client_ssn = findViewById(R.id.client_ssn);
        client_name_show_card = findViewById(R.id.client_name_show_card);
        client_ssn_show_card = findViewById(R.id.client_ssn_show_card);
        client_role_show_card = findViewById(R.id.client_role_show_card);
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
                        client_name.setError(null);
                        client_name.setError("Can't connect to the database!");
                    }else {
                        int code = result.get("error").getAsInt();
                        if (code != 0) {
                            client_name.setError(null);
                            client_name.setError("No such user");
                        }
                        else{
                            client_name.setError(null);
                            JsonObject user = result.get("user").getAsJsonObject();
                            client_ssn.setText(user.get("ssn").getAsString());
                            client_name_show_card.setText(user.get("name").getAsString());
                            client_ssn_show_card.setText(user.get("ssn").getAsString());
                            client_role_show_card.setText(user.get("role").getAsString());
                            role = user.get("role").getAsString();
                        }
                    }
                });
    }


    public void exerciseOfTheDay(View view){
        Intent intent = new Intent(clientActivity.this, exerciseOfTheDay.class);
        intent.putExtra("ROLE", role);
        Toast.makeText(this, role, Toast.LENGTH_LONG).show();
        startActivity(intent);
    }

    public void signOut(View view) {
        sa.clearToken();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void changeName(View view) {
        JsonObject json = new JsonObject();
        String name = client_name.getText().toString();
        if(name.trim().length() == 0){
            client_name.setError(null);
            client_name.setError("Name cannot be only white space!");
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
                            client_name.setError(null);
                            client_name.setError("Can't connect to the database!");
                        } else {
                            int code = result.get("error").getAsInt();
                            if (code != 0) {
                                if (code == 5) {
                                    client_name.setError(null);
                                    client_name.setError("Name cannot be empty");
                                }
                                client_name.setError(null);
                                client_name.setError("Name missing");
                            } else {
                                client_name.setError(null);
                                Toast.makeText(this, "Name changed", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
            this.recreate();
        }
    }

}
