package fenrirmma.hreysti_app.user.Admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import fenrirmma.hreysti_app.R;
import fenrirmma.hreysti_app.login.SessionAccess;


public class allUsersAdminActivity extends AppCompatActivity {
    private SessionAccess sa;
    private ListView userListView;
    private List<String> userList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users_admin);
        sa = SessionAccess.getInstance(this);
        userList = new ArrayList<>();
        userListView = findViewById(R.id.admin_user_list);
        populateList(userList);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, userList);
        userListView.setAdapter(arrayAdapter);
    }

    private void populateList(List<String> list) {
        Ion.with(this)
                .load("GET", "http://10.0.2.2:5000/user/all")
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("fenrir-token", sa.getToken())
                .setTimeout(1000)
                .asJsonObject()
                .setCallback((e, result) -> {
                    if(e != null){
                        Toast.makeText(this, "ION ERROR", Toast.LENGTH_SHORT).show(); //TODO breyta í eitthvað meira hot
                    }else {
                        int code = result.get("error").getAsInt();
                        if (code != 0) {
                            Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show(); //TODO breyta í eitthvað meira hot
                        }
                        else{
                            JsonArray users = result.get("all_users").getAsJsonArray();
                            for(int i = 0; i < users.size(); i++){
                                JsonObject current = users.get(i).getAsJsonObject();
                                list.add(current.get("name").getAsString());
                            }

                        }
                    }
                });
    }
}
