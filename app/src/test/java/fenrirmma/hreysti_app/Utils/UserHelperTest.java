package fenrirmma.hreysti_app.Utils;

import org.junit.Test;

import fenrirmma.hreysti_app.Utils.UserHelper;

import static org.junit.Assert.assertEquals;

/**
 * Created by Notandi on 14.12.2017.
 */

public class UserHelperTest {
    @Test
    public void getNameTest() throws Exception {
        UserHelper user = new UserHelper("Gunni", "2810842759", "2dsfs", "Client", "12/12/2002", "12/11/2020");

        assertEquals("Gunni", user.getName());
    }
    @Test
    public void getSSNTest() throws Exception {
        UserHelper user = new UserHelper("Gunni", "2810842759", "2dsfs", "Client", "12/12/2002", "12/11/2020");

        assertEquals("2810842759", user.getSsn());
    }
    @Test
    public void getOpenIDTest() throws Exception {
        UserHelper user = new UserHelper("Gunni", "2810842759", "2dsfs", "Client", "12/12/2002", "12/11/2020");

        assertEquals("2dsfs", user.getOpenId());
    }
    @Test
    public void getUserRoleTest() throws Exception {
        UserHelper user = new UserHelper("Gunni", "2810842759", "2dsfs", "Client", "12/12/2002", "12/11/2020");

        assertEquals("Client", user.getUserRole());
    }
    @Test
    public void getStartDateTest() throws Exception {
        UserHelper user = new UserHelper("Gunni", "2810842759", "2dsfs", "Client", "12/12/2002", "12/11/2020");

        assertEquals("12/12/2002", user.getStartDate());
    }
    @Test
    public void getExpireDateTest() throws Exception {
        UserHelper user = new UserHelper("Gunni", "2810842759", "2dsfs", "Client", "12/12/2002", "12/11/2020");

        assertEquals("12/11/2020", user.getExpireDate());
    }



}
