package fenrirmma.hreysti_app.user.Admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import fenrirmma.hreysti_app.R;
import fenrirmma.hreysti_app.login.MainActivity;
import fenrirmma.hreysti_app.Utils.SessionAccess;
import fenrirmma.hreysti_app.workout.createWorkoutActivity;
import fenrirmma.hreysti_app.workout.exerciseOfTheDay;

public class adminActivity extends AppCompatActivity {

    private SessionAccess sa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        sa = SessionAccess.getInstance(this);
    }

    public void signOut(View view) {
        sa.clearToken();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void workoutOfTheDay(View view) {
        startActivity(new Intent(this, exerciseOfTheDay.class));
    }

    public void getAllUsers(View view) {
        startActivity(new Intent(this, allUsersAdminActivity.class));
    }

    public void createWorkout(View view) {
        startActivity(new Intent(this, createWorkoutActivity.class));
    }
}
