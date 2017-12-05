package fenrirmma.hreysti_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class idkandiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idkandi);
    }

    public void exerciseOfTheDay(View view){
        Intent intent = new Intent(idkandiActivity.this, exerciseOfTheDay.class);
        startActivity(intent);
    }
}
