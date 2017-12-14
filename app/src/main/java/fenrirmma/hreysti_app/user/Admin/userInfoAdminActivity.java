package fenrirmma.hreysti_app.user.Admin;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;
import fenrirmma.hreysti_app.R;
import fenrirmma.hreysti_app.Utils.SessionAccess;

public class userInfoAdminActivity extends AppCompatActivity {

    private TextView name, ssn;
    private EditText role;
    private SessionAccess sa;
    private Button confirm, remove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_admin);
        sa = SessionAccess.getInstance(this);
        confirm = findViewById(R.id.edit_user_change_confirm);
        remove = findViewById(R.id.edit_user_remove);
        setFields();

    }

    public void confirmChanges(View view) {
        String url = "http://10.0.2.2:5000/admin/user/name/update/" + getIntent().getStringExtra("OPENID");
        JsonObject json = new JsonObject();
        json.addProperty("role", role.getText().toString());
        Ion.with(this)
                .load("PUT", url)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("fenrir-token", sa.getToken())
                .setTimeout(1000)
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback((e, result) -> {
                    if(e != null){
                        clearError();
                        role.setError("Can't connect to the database!");
                    }else {
                        int code = result.get("error").getAsInt();
                        if (code != 0) {
                            switch (code){
                                case 11 :
                                    clearError();
                                    role.setError("Missing data");
                                    break;
                                case 2 :
                                    clearError();
                                    role.setError("There is no such user");
                                    break;
                                case 5 :
                                    clearError();
                                    role.setError("Field cannot be empty!");
                                    break;
                                case 16 :
                                    clearError();
                                    role.setError("That is an invalid role!");
                                    break;
                            }
                        }
                        else{
                            startActivity(new Intent(this, allUsersAdminActivity.class));
                            finish();
                        }
                    }
                });
    }

    public void removeUser(View view) {
        String url = "http://10.0.2.2:5000/admin/user/delete/" + getIntent().getStringExtra("OPENID");
        Ion.with(this)
                .load("DELETE", url)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("fenrir-token", sa.getToken())
                .setTimeout(1000)
                .asJsonObject()
                .setCallback((e, result) -> {
                    if(e != null){
                        clearError();
                        role.setError("Can't connect to the database!");
                    }else {
                        int code = result.get("error").getAsInt();
                        if (code != 0) {
                            if(code == 11) {
                                clearError();
                                role.setError("Cannot delete admins!");
                            } else{
                                clearError();
                                role.setError("There is no such user");
                            }
                        }
                        else{
                            startActivity(new Intent(this, allUsersAdminActivity.class));
                            finish();
                        }
                    }
                });
    }
    private void clearError(){
        role.setError(null);

    }
    private void setFields() {
        //Setting all the appropriate text fields in the activity
        role = findViewById(R.id.edit_user_role);
        //expireDate = findViewById(R.id.edit_user_expire_date);
        name = findViewById(R.id.edit_user_name);
        ssn = findViewById(R.id.edit_user_ssn);
        //startDate = findViewById(R.id.edit_user_start_date);

        role.setText(getIntent().getStringExtra("ROLE"));
        name.setText(String.format(getResources().getString(R.string.admin_user_name), getIntent().getStringExtra("NAME")));
        ssn.setText(String.format(getResources().getString(R.string.admin_user_ssn), getIntent().getStringExtra("SSN")));
        //startDate.setText(tring.format(getResources().getString(R.string.admin_user_startdate), getIntent().getStringExtra("STARTDATE"));
        //expireDate.setText(getIntent().getStringExtra("EXPIREDATE"));
    }

    @Override
    public void onBackPressed(){
        startActivity(new Intent(this, allUsersAdminActivity.class));
        finish();
    }

}
