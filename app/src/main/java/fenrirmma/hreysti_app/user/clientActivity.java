package fenrirmma.hreysti_app.user;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import fenrirmma.hreysti_app.R;
import fenrirmma.hreysti_app.login.MainActivity;
import fenrirmma.hreysti_app.login.SessionAccess;
import fenrirmma.hreysti_app.workout.exerciseOfTheDay;

public class clientActivity extends AppCompatActivity {

    private SessionAccess ta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idkandi);

        ta = SessionAccess.getInstance(this);

    }

    public void exerciseOfTheDay(View view){
        Intent intent = new Intent(clientActivity.this, exerciseOfTheDay.class);
        startActivity(intent);
    }

    public void signOut(View view) {
        ta.clearToken();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
