package fenrirmma.hreysti_app.Utils;

import java.util.Locale;


public class UserHelper {

    private String name;
    private String ssn;
    private String openId;
    private String userRole;
    private String expireDate;
    private String startDate;

    public UserHelper(String name, String ssn, String openId, String userRole, String startDate, String expireDate) {
        this.name = name;
        this.ssn = ssn;
        this.openId = openId;
        this.userRole = userRole;
        this.startDate = startDate;
        this.expireDate = expireDate;
    }


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



    @Override
    public String toString() {
        return String.format(Locale.US, "%s", name);
    }

}
