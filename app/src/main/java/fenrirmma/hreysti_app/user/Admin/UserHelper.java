package fenrirmma.hreysti_app.user.Admin;

import java.sql.Date;
import java.util.Locale;

/**
 * Created by arnar on 8.12.2017.
 */

public class UserHelper {


    private String name;
    private String ssn;
    private String openId;
    private String userRole;

    public UserHelper(String name, String ssn, String openId, String userRole, String startDate, String expireDate) {
        this.name = name;
        this.ssn = ssn;
        this.openId = openId;
        this.userRole = userRole;
        this.startDate = startDate;
        this.expireDate = expireDate;
    }

    private String startDate;

    public String getName() {
        return name;
    }

    public String getSsn() {
        return ssn;
    }

    public String getOpenId() {
        return openId;
    }

    public String getUserRole() {
        return userRole;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getExpireDate() {
        return expireDate;
    }

    private String expireDate;

    @Override
    public String toString() {
        return String.format(Locale.US, "%s", name);
    }

}
