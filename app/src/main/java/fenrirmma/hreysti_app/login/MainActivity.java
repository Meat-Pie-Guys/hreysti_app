package fenrirmma.hreysti_app.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import com.koushikdutta.ion.Ion;
import fenrirmma.hreysti_app.R;
import fenrirmma.hreysti_app.Utils.SessionAccess;
import fenrirmma.hreysti_app.user.Admin.adminActivity;
import fenrirmma.hreysti_app.user.clientActivity;
import fenrirmma.hreysti_app.user.Coach.coachActivity;

public class MainActivity extends AppCompatActivity {

    private SessionAccess sa;
    private EditText ssn;
    private EditText password;
    private RelativeLayout load;
    private static String URL = "http://10.0.2.2:5000/login";
    public static void setPath(String newUrl){
        URL = newUrl;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ssn = findViewById(R.id.sign_in_ssn);
        password = findViewById(R.id.sign_in_pw);
        sa = SessionAccess.getInstance(this);
        load = findViewById(R.id.loadingPanel);

        if(sa.hasToken()) proceed();

    }

    public void signIn(View view){
        load.setVisibility(view.VISIBLE);
        String _ssn = ssn.getText().toString();
        String pw = password.getText().toString();
        getToken(_ssn, pw);
        load.setVisibility(view.GONE);
    }

    private void getToken(String ssn, String pw) {
        String credentials = new String(Base64.encode(String.format("%s:%s", ssn, pw).getBytes(), Base64.DEFAULT));
        Ion.with(this)
                .load("GET", URL)
                .addHeader("Authorization", String.format("Basic %s", credentials))
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .setTimeout(1000)
                .asJsonObject()
                .setCallback((e, result) -> {
                    if(e != null){
                        password.setError(null);
                        password.setError("Can't connect to the database!");
                    }
                    else {
                        int code = result.get("error").getAsInt();
                        if (code != 0) {
                            if(code == 4){
                                password.setError(null);
                                password.setError("SSN or password missing");
                            } else {
                                password.setError(null);
                                password.setError("SSN or password wrong!");
                            }
                        }
                        else{
                            password.setError(null);
                            sa.setRole(result.get("role").getAsString());
                            sa.setToken(result.get("token").getAsString());
                            proceed();
                        }
                    }
                });

    }

    public void signUp(View view){
        startActivity(new Intent(this, newSignUp.class));

    }

    private void proceed() {
        String role = sa.getRole();
        if(role.equals("Admin")){
            startActivity(new Intent(this, adminActivity.class));
            finish();
        } else if(role.equals("Coach")){
            startActivity(new Intent(this, coachActivity.class));
            finish();
        } else{
            startActivity(new Intent(this, clientActivity.class));
            finish();
        }

    }

}

