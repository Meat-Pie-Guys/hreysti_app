package fenrirmma.hreysti_app.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import fenrirmma.hreysti_app.R;
import fenrirmma.hreysti_app.user.adminActivity;
import fenrirmma.hreysti_app.user.clientActivity;
import fenrirmma.hreysti_app.user.coachActivity;

public class newSignUp extends AppCompatActivity {

    private SessionAccess sa;
    private EditText name;
    private EditText ssn;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_sign_up);

        name = findViewById(R.id.signup_name);
        ssn = findViewById(R.id.signup_ssn);
        password = findViewById(R.id.signup_password);
        sa = SessionAccess.getInstance(this);

    }

    public void menu(View view) {
        String _name = name.getText().toString();
        String _ssn = ssn.getText().toString();
        String _pw = password.getText().toString();
        setInfo(_name, _ssn, _pw);
    }

    private void setInfo(String _name, String _ssn, String _pw) {

        JsonObject json = new JsonObject();
        json.addProperty("name", _name);
        json.addProperty("ssn", _ssn);
        json.addProperty("password", _pw);
        Ion.with(this)
                .load("POST", "http://10.0.2.2:5000/user")
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .setTimeout(1000)
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback((e, result) -> {
                    if(e != null){
                        Toast.makeText(this, "Ion error", Toast.LENGTH_SHORT).show(); //TODO breyta í eitthvað meira hot
                    }
                    else{
                        String code = result.get("error").getAsString();
                        if(!Objects.equals(code, "0")){
                            Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show(); //TODO breyta í eitthvað meira hot
                        }
                        else{
                            getSession(_ssn, _pw);
                        }
                    }
                });

    }

    private void getSession(String ssn, String pw) {
        String credentials = new String(Base64.encode(String.format("%s:%s", ssn, pw).getBytes(), Base64.DEFAULT));
        Ion.with(this)
                .load("GET", "http://10.0.2.2:5000/login")
                .addHeader("Authorization", String.format("Basic %s", credentials))
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .setTimeout(1000)
                .asJsonObject()
                .setCallback((e, result) -> {
                    if(e != null){
                        Toast.makeText(this, "Ion error", Toast.LENGTH_SHORT).show(); //TODO breyta í eitthvað meira hot
                    }
                    else {
                        String code = result.get("error").getAsString();
                        if (!Objects.equals(code, "0")) {
                            Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show(); //TODO breyta í eitthvað meira hot
                        }
                        else{
                            sa.setRole(result.get("role").getAsString());
                            sa.setToken(result.get("token").getAsString());
                            proceed();
                        }
                    }

                });
    }

    private void proceed() {
        String role = sa.getRole();
        if(role.equals("admin")){
            startActivity(new Intent(this, adminActivity.class));
            finish();
        } else if(role.equals("coach")){
            startActivity(new Intent(this, coachActivity.class));
            finish();
        } else{
            startActivity(new Intent(this, clientActivity.class));
            finish();
        }
    }
}
