package fenrirmma.hreysti_app.workout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

import fenrirmma.hreysti_app.R;
import fenrirmma.hreysti_app.Utils.DateConverter;
import fenrirmma.hreysti_app.Utils.SessionAccess;
import fenrirmma.hreysti_app.Utils.UserHelper;

public class attendingUsersActivity extends AppCompatActivity {

    private ArrayList<UserHelper> list_attending;
    private RecyclerView recyclerView;
    private attendingUsersRecyclerAdapter adapter;
    private SessionAccess sa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attending_users);

        sa = SessionAccess.getInstance(this);

        recyclerView = findViewById(R.id.recycle_view_attending);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        String id = getIntent().getStringExtra("ID");
        populateAttendingUsers(id);
    }

    private void populateAttendingUsers(String workout_id) {
        list_attending = new ArrayList<>();
        Ion.with(this)
                .load("GET", "http://10.0.2.2:5000/workout/users/" + workout_id)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("fenrir-token", sa.getToken())
                .setTimeout(1000)
                .asJsonObject()
                .setCallback((e, result) -> {
                    if(e != null){
                        Toast.makeText(this, "Can't connect to the database!", Toast.LENGTH_SHORT).show();
                    }else {
                        int code = result.get("error").getAsInt();
                        if (code != 0) {
                            Toast.makeText(this, "Not allowed for clients", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            JsonArray users = result.getAsJsonArray("all_users");
                            for(int i = 0; i < users.size(); i++){
                                JsonObject current = users.get(i).getAsJsonObject();
                                list_attending.add( new UserHelper(
                                        current.get("name").getAsString(),
                                        current.get("ssn").getAsString(),
                                        current.get("open_id").getAsString(),
                                        current.get("user_role").getAsString(),
                                        current.get("start_date").getAsString(),
                                        DateConverter.convert(current.get("expire_date").getAsString())
                                ));
                            }
                        }

                        adapter = new attendingUsersRecyclerAdapter(this, list_attending);
                        recyclerView.setAdapter(adapter);
                    }

                });
    }
}
