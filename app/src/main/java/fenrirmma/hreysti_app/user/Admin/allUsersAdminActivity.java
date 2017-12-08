package fenrirmma.hreysti_app.user.Admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;


import java.util.ArrayList;
import java.util.List;

import fenrirmma.hreysti_app.R;
import fenrirmma.hreysti_app.login.SessionAccess;


public class allUsersAdminActivity extends AppCompatActivity {
    private SessionAccess sa;
    private ListView userListView;
    private List<UserHelper> userList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users_admin);
        sa = SessionAccess.getInstance(this);
        userListView = findViewById(R.id.admin_user_list);
        populateList();

        userListView.setOnItemClickListener((parent, view, pos, id) -> {
            UserHelper curr = (UserHelper)parent.getItemAtPosition(pos);
            Intent intent = new Intent(this, userInfoAdminActivity.class);
            intent.putExtra("NAME", curr.getName());
            intent.putExtra("SSN", curr.getSsn());
            intent.putExtra("OPENID", curr.getOpenId());
            intent.putExtra("ROLE", curr.getUserRole());
            intent.putExtra("STARTDATE", curr.getStartDate());
            intent.putExtra("EXPIREDATE", curr.getExpireDate());
            startActivity(intent);
        });
    }

    private void populateList() {
        userList = new ArrayList<>();
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
                            JsonArray users = result.getAsJsonArray("all_users");
                            for(int i = 0; i < users.size(); i++){
                                JsonObject current = users.get(i).getAsJsonObject();
                                userList.add( new UserHelper(
                                        current.get("name").getAsString(),
                                        current.get("ssn").getAsString(),
                                        current.get("open_id").getAsString(),
                                        current.get("user_role").getAsString(),
                                        current.get("start_date").getAsString(),
                                        current.get("expire_date").getAsString()
                                        ));
                            }
                        }
                        ArrayAdapter<UserHelper> arrayAdapter = new ArrayAdapter<>(this,
                                android.R.layout.simple_list_item_1, userList);
                        userListView.setAdapter(arrayAdapter);
                    }


                });
    }
}
