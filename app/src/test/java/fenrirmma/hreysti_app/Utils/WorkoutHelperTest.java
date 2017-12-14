package fenrirmma.hreysti_app.Utils;

import org.junit.Test;

import fenrirmma.hreysti_app.Utils.UserHelper;
import fenrirmma.hreysti_app.Utils.WorkoutHelper;

import static org.junit.Assert.assertEquals;

/**
 * Created by Notandi on 14.12.2017.
 */

public class WorkoutHelperTest {
    @Test
    public void getOpenIDTest() throws Exception {
        WorkoutHelper workout = new WorkoutHelper("dfgh", "Balli", "10xhopp", "05/03/2000", "5", "3");

        assertEquals("dfgh", workout.getOpen_id());
    }
    @Test
    public void getCoachNameTest() throws Exception {
        WorkoutHelper workout = new WorkoutHelper("dfgh", "Balli", "10xhopp", "05/03/2000", "5", "3");

        assertEquals("Balli", workout.getCoach_name());
    }
    @Test
    public void getDescriptionTest() throws Exception {
        WorkoutHelper workout = new WorkoutHelper("dfgh", "Balli", "10xhopp", "05/03/2000", "5", "3");

        assertEquals("10xhopp", workout.getDescription());
    }
    @Test
    public void getDateTest() throws Exception {
        WorkoutHelper workout = new WorkoutHelper("dfgh", "Balli", "10xhopp", "05/03/2000", "5", "3");

        assertEquals("05/03/2000", workout.getDate());
    }
    @Test
    public void getAttendingTest() throws Exception {
        WorkoutHelper workout = new WorkoutHelper("dfgh", "Balli", "10xhopp", "05/03/2000", "5", "3");

        assertEquals("5", workout.getAttending());
    }
    @Test
    public void getCoachIDTest() throws Exception {
        WorkoutHelper workout = new WorkoutHelper("dfgh", "Balli", "10xhopp", "05/03/2000", "5", "3");

        assertEquals("3", workout.getCoach_id());
    }
}
