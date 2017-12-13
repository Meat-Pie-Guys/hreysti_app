package fenrirmma.hreysti_app.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;


import java.util.Objects;

import fenrirmma.hreysti_app.R;
import fenrirmma.hreysti_app.Utils.SessionAccess;
import fenrirmma.hreysti_app.Utils.ssnValidation.Validator;
import fenrirmma.hreysti_app.user.Admin.adminActivity;
import fenrirmma.hreysti_app.user.clientActivity;
import fenrirmma.hreysti_app.user.Coach.coachActivity;

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

        if(Validator.isValidSSN(_ssn)){
            if(_name.trim().length() != 0){setInfo(_name, _ssn, _pw);}
            clearError();
            name.setError("Name cannot be empty!");
        } else{
            clearError();
            ssn.setError("Kennitala is illegal");
        }


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
                        clearError();
                        password.setError("Can't connect to the database!");
                    }
                    else{
                        int code = result.get("error").getAsInt();
                        if(code != 0){
                            //10, 5, 6, 7, 8
                            switch (code){
                                case 10 :
                                    clearError();
                                    name.setError("Missing header fields");
                                    break;
                                case 5 :
                                    clearError();
                                    name.setError("Missing information");
                                    break;
                                case 6 :
                                    clearError();
                                    password.setError("Password must be at least 6 characters long");
                                    break;
                                case 7 :
                                    clearError();
                                    ssn.setError("SSN is illegal");
                                    break;
                                case 8 :
                                    clearError();
                                    name.setError("User already exists in database");
                                    break;
                            }
                        }
                        else{
                            clearError();
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
                        clearError();
                        password.setError("Can't connect to the database!");
                    }
                    else {
                        int code = result.get("error").getAsInt();
                        if (code != 0) {
                            if(code == 4){
                                clearError();
                                password.setError("SSN or password missing");
                            } else {
                                clearError();
                                password.setError("SSN or password wrong!");
                            }
                        }
                        else{
                            clearError();
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
    private void clearError(){
        name.setError(null);
        ssn.setError(null);
        password.setError(null);

    }
}
