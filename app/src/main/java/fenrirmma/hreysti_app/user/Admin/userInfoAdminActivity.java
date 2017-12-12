package fenrirmma.hreysti_app.user.Admin;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;

import java.util.Calendar;

import fenrirmma.hreysti_app.R;
import fenrirmma.hreysti_app.Utils.SessionAccess;

public class userInfoAdminActivity extends AppCompatActivity {

    private TextView name, ssn, startDate, expireDate;
    private EditText role;
    private SessionAccess sa;
    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_admin);
        sa = SessionAccess.getInstance(this);
        setFields();

        expireDate.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR); // current year
            int mMonth = c.get(Calendar.MONTH); // current month
            int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
            datePickerDialog = new DatePickerDialog(userInfoAdminActivity.this,
                    (view, year, monthOfYear, dayOfMonth) -> {
                        expireDate.setText(dayOfMonth + "/"
                                + (monthOfYear + 1) + "/" + year);

                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        });
    }

    public void confirmChanges(View view) {
        String url = "http://10.0.2.2:5000/admin/user/name/update/" + getIntent().getStringExtra("OPENID");
        JsonObject json = new JsonObject();
        json.addProperty("expire_date", expireDate.getText().toString());
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
                        Toast.makeText(this, "ION ERROR", Toast.LENGTH_SHORT).show(); //TODO breyta í eitthvað meira hot
                    }else {
                        int code = result.get("error").getAsInt();
                        if (code != 0) {
                            Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show(); //TODO breyta í eitthvað meira hot
                        }
                        else{
                            Toast.makeText(this, "Hello THERE", Toast.LENGTH_SHORT).show();
                            this.recreate();
                        }
                    }
                });
        finish();
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
