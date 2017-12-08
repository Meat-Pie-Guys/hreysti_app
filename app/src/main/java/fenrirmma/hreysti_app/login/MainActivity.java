package fenrirmma.hreysti_app.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import fenrirmma.hreysti_app.R;
import fenrirmma.hreysti_app.user.clientActivity;

public class MainActivity extends AppCompatActivity {

    private SessionAccess sa;
    private EditText username;
    private EditText password;
    private RelativeLayout load;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        username = findViewById(R.id.et_username);
        password = findViewById(R.id.et_password);
        sa = SessionAccess.getInstance(this);

        if(sa.hasToken()) proceed();

    }

    public void signIn(View view){

        String un = username.getText().toString();
        String pw = password.getText().toString();

    }

    public void signUp(View view){
        startActivity(new Intent(this, newSignUp.class));
        finish();

    }

    private void proceed() {
        startActivity(new Intent(this, clientActivity.class));
        finish();

    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}

