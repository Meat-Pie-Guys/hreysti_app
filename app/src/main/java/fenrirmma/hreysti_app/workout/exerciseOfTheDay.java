package fenrirmma.hreysti_app.workout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import fenrirmma.hreysti_app.R;
import fenrirmma.hreysti_app.user.clientActivity;

public class exerciseOfTheDay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_of_the_day);
    }

    public void goHome(View view) {
        Intent intent = new Intent(exerciseOfTheDay.this, clientActivity.class);
        startActivity(intent);
    }
}
