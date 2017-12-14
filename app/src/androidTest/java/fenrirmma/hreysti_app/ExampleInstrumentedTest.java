package fenrirmma.hreysti_app;


import android.content.Intent;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.WindowManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import fenrirmma.hreysti_app.user.Admin.adminActivity;
import fenrirmma.hreysti_app.workout.createWorkoutActivity;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    private MockWebServer mock;
    @Rule
    public final ActivityTestRule<adminActivity> aActivity =
            new ActivityTestRule<>(adminActivity.class, false, false);
    @Rule
    public final ActivityTestRule<createWorkoutActivity> createActivity =
            new ActivityTestRule<>(createWorkoutActivity.class, false, false);

    @Before
    public void setUp() throws Exception {
        mock = new MockWebServer();
        mock.start();
        String freddyVsjSon = "{\n" +
                "    \"all_users\": [\n" +
                "        {\n" +
                "            \"expire_date\": \"Wed, 13 Dec 2017 16:07:39 GMT\",\n" +
                "            \"name\": \"2005893869\",\n" +
                "            \"open_id\": \"3f5cbcf8-67a6-4cb5-9d2b-32d2a1807d86\",\n" +
                "            \"ssn\": \"2005893869\",\n" +
                "            \"start_date\": \"Wed, 13 Dec 2017 16:07:39 GMT\",\n" +
                "            \"user_role\": \"Admin\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"error\": 0\n" +
                "}";
        MockResponse response = new MockResponse().setResponseCode(200)
                .setHeader("Accept", "application/json")
                .setHeader("Content-Type:", "application/json")
                .setBody(freddyVsjSon);
        mock.enqueue(response);
        createWorkoutActivity.setUrl(mock.url("/").toString());
        aActivity.launchActivity(new Intent());
        noSleep(aActivity.getActivity());
    }

    @After
    public void tearDown() throws Exception {
        mock.close();
        createWorkoutActivity.setUrl("http://10.0.2.2:5000/user/coaches");
    }

    @Test
    public void testClickCreateWorkut() throws  Exception{
        ViewInteraction createWorkoutButton = onView(withId(R.id.create_new_workout));
        createWorkoutButton.perform(click())
                .check(withId(R.id.create_new_workout_root))
                ;

    }
    private void noSleep(final adminActivity activity){
        Runnable wakeUpDevice = new Runnable() {
            @Override
            public void run() {
                activity.getWindow().addFlags(
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                );
            }
        };
        activity.runOnUiThread(wakeUpDevice);
    }
    }
}
