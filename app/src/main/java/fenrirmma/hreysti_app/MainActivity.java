package fenrirmma.hreysti_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;
import java.util.Observable;

public class MainActivity extends AppCompatActivity {

    private EditText name;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.et_username);
        password = findViewById(R.id.et_password);
    }
    public void validate(View view){
        validateSignIn(name.getText().toString(), password.getText().toString());
    }

    public void signUp(View view){
        Intent intent = new Intent(MainActivity.this, newSignUp.class);
        startActivity(intent);
    }
    private void validateSignIn(String username, String pw){
        if(Objects.equals(username, "Cock") && Objects.equals(pw, "1234")){
            Intent intent = new Intent(MainActivity.this, idkandiActivity.class);
            startActivity(intent);
        }
        else if(Objects.equals(username, "BiggerCock") && Objects.equals(pw, "1234")){
            Intent intent = new Intent(MainActivity.this, thjalfariActivity.class);
            startActivity(intent);
        }
        else if(Objects.equals(username, "BiggestCock") && Objects.equals(pw, "1234")){
            Intent intent = new Intent(MainActivity.this, adminActivity.class);
            startActivity(intent);
        }
        else{
            Toast.makeText(getApplicationContext(),"invalid!",Toast.LENGTH_SHORT).show();
        }
    }
}

