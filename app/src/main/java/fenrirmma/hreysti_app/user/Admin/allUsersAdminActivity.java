package fenrirmma.hreysti_app.user.Admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;


import java.util.ArrayList;
import java.util.List;

import fenrirmma.hreysti_app.R;
import fenrirmma.hreysti_app.Utils.DateConverter;
import fenrirmma.hreysti_app.Utils.UserHelper;
import fenrirmma.hreysti_app.Utils.SessionAccess;


public class allUsersAdminActivity extends AppCompatActivity {
    private SessionAccess sa;
    private ListView userListView;
    private List<UserHelper> userList;
    private EditText search;
    private ArrayAdapter<UserHelper> arrayAdapter;
    private RecyclerView recyclerView;
    private ArrayList<UserHelper> userArrayList;
    private AllUsersRecyclerAdapter adapter;
    private SearchView sv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users_admin);
        sa = SessionAccess.getInstance(this);
        recyclerView = findViewById(R.id.recycle_view_admin);
        sv = findViewById(R.id.mSearch);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        userArrayList = new ArrayList<>();
        populateList();

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                adapter.getFilter().filter(query);
                return false;
            }
        });

    }




    private void populateList() {
        userArrayList = new ArrayList<>();
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
                                userArrayList.add( new UserHelper(
                                        current.get("name").getAsString(),
                                        current.get("ssn").getAsString(),
                                        current.get("open_id").getAsString(),
                                        current.get("user_role").getAsString(),
                                        current.get("start_date").getAsString(),
                                        DateConverter.convert(current.get("expire_date").getAsString())
                                        ));
                            }
                        }

                        adapter = new AllUsersRecyclerAdapter(this, userArrayList);
                        recyclerView.setAdapter(adapter);
                    }


                });
    }
    @Override
    protected void onResume() {
        super.onResume();
        populateList();
        //startSearchText();
    }
}
