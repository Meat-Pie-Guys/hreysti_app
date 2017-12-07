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

import fenrirmma.hreysti_app.R;
import fenrirmma.hreysti_app.user.clientActivity;

public class MainActivity extends AppCompatActivity {

    private TokenAccess ta;
    private EditText username;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.et_username);
        password = findViewById(R.id.et_password);
        ta = TokenAccess.getInstance(this);

        if(ta.hasToken()) proceed();

    }
    public void signIn(View view){
        String un = username.getText().toString();
        String pw = password.getText().toString();
        getToken(un, pw);
    }


    public void signUp(View view){
        String un = username.getText().toString();
        String pw = password.getText().toString();
        createUser(un,pw);
    }


    private void createUser(String username, String pw){
        JsonObject json = new JsonObject();
        json.addProperty("name", username);
        json.addProperty("password", pw);
        Ion.with(this)
                .load("POST", "http://10.0.2.2:5000/user")
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback((e, result) -> {
                    if(e != null) {
                        toast(e.getMessage());
                    } else if(!"success".equals(result.get("message").getAsString())){
                        toast(result.get("message").getAsString());
                    } else{
                        toast("getting token");
                        getToken(username, pw);
                    }
                });
    }

    private void getToken(String username, String pw) {

        String credentials = new String(Base64.encode(String.format("%s:%s", username, pw).getBytes(), Base64.DEFAULT));
        Ion.with(this)
                .load("GET", "http://10.0.2.2:5000/login")
                .addHeader("Authorization", String.format("Basic %s", credentials))
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .asJsonObject()
                .setCallback((e, result) -> {
                    if(e != null) {
                        toast(e.getMessage());
                    }else if(result.get("message") != null){
                        toast(result.get("message").getAsString());
                    } else{
                        ta.setToken(result.get("token").getAsString());
                        proceed();
                    }
                });



    }

    private void proceed() {
        startActivity(new Intent(this, clientActivity.class));
        finish();

    }


    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}

