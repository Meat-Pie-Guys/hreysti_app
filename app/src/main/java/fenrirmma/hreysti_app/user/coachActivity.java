package fenrirmma.hreysti_app.user;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import fenrirmma.hreysti_app.R;
import fenrirmma.hreysti_app.workout.createWorkoutActivity;
import fenrirmma.hreysti_app.workout.exerciseOfTheDay;

public class coachActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thjalfari);
    }

    public void exerciseOfTheDay(View view){
        Intent intent = new Intent(coachActivity.this, exerciseOfTheDay.class);
        startActivity(intent);
    }

    public void MakeWorkout(View view) {
        Intent intent = new Intent(coachActivity.this, createWorkoutActivity.class);
        startActivity(intent);
    }
}
