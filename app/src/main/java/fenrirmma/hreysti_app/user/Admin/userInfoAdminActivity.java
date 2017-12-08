package fenrirmma.hreysti_app.user.Admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;

import fenrirmma.hreysti_app.R;
import fenrirmma.hreysti_app.login.SessionAccess;

public class userInfoAdminActivity extends AppCompatActivity {

    private TextView name, ssn, startDate;
    private EditText role, expireDate;
    private SessionAccess sa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_admin);
        sa = SessionAccess.getInstance(this);
        setFields();


    }

    public void confirmChanges(View view) {
        String url = "http://10.0.2.2:5000/admin/user/name/update/" + getIntent().getStringExtra("OPENID");
        JsonObject json = new JsonObject();
        json.addProperty("expire_date", expireDate.getText().toString());
        json.addProperty("role", role.getText().toString());
        Ion.with(this)
                .load("GET", url)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("fenrir-token", sa.getToken())
                .setTimeout(1000)
                .setJsonObjectBody(json)
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
                            this.recreate();
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
                        Toast.makeText(this, "ION ERROR", Toast.LENGTH_SHORT).show(); //TODO breyta í eitthvað meira hot
                    }else {
                        int code = result.get("error").getAsInt();
                        if (code != 0) {
                            Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show(); //TODO breyta í eitthvað meira hot
                        }
                        else{
                            finish();
                        }
                    }


                });
    }
    private void setFields() {
        role = findViewById(R.id.edit_user_role);
        expireDate = findViewById(R.id.edit_user_expire_date);
        name = findViewById(R.id.edit_user_name);
        ssn = findViewById(R.id.edit_user_ssn);
        startDate = findViewById(R.id.edit_user_start_date);

        String _name = String.format(getResources().getString(R.string.admin_user_name), getIntent().getStringExtra("NAME"));
        String _ssn = String.format(getResources().getString(R.string.admin_user_ssn), getIntent().getStringExtra("SSN"));
        String _startDate = String.format(getResources().getString(R.string.admin_user_startdate), getIntent().getStringExtra("STARTDATE"));

        role.setText(getIntent().getStringExtra("ROLE"));
        expireDate.setText(getIntent().getStringExtra("EXPIREDATE"));
        name.setText(_name);
        ssn.setText(_ssn);
        startDate.setText(_startDate);
    }
}
