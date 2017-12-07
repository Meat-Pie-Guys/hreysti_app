package fenrirmma.hreysti_app.user;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import fenrirmma.hreysti_app.R;
import fenrirmma.hreysti_app.workout.exerciseOfTheDay;

public class clientActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idkandi);
    }

    public void exerciseOfTheDay(View view){
        Intent intent = new Intent(clientActivity.this, exerciseOfTheDay.class);
        startActivity(intent);
    }
}
