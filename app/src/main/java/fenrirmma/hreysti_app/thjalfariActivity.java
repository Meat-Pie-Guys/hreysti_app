package fenrirmma.hreysti_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class thjalfariActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thjalfari);
    }

    public void exerciseOfTheDay(View view){
        Intent intent = new Intent(thjalfariActivity.this, exerciseOfTheDay.class);
        startActivity(intent);
    }
}
