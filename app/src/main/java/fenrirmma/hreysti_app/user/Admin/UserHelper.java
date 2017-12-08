package fenrirmma.hreysti_app.user.Admin;

import java.sql.Date;

/**
 * Created by arnar on 8.12.2017.
 */

public class UserHelper {

    private int id;
    private String name;
    private String ssn;
    private String openId;
    private String userRole;
    private Date startDate;

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    private Date expireDate;

    public UserHelper(int id, String name, String ssn, String openId, String userRole, Date startDate, Date expireDate) {
        this.id = id;
        this.name = name;
        this.ssn = ssn;
        this.openId = openId;
        this.userRole = userRole;
        this.startDate = startDate;
        this.expireDate = expireDate;
    }
}
