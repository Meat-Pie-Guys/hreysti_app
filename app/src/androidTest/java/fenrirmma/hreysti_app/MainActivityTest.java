package fenrirmma.hreysti_app.login;


import android.content.Context;
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

import fenrirmma.hreysti_app.R;
import fenrirmma.hreysti_app.Utils.SessionAccess;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    private MockWebServer server;

    @Rule
    public final ActivityTestRule<MainActivity> mActivity
            = new ActivityTestRule<>(MainActivity.class, true, false);

    private ViewInteraction btn_sign_in;
    private ViewInteraction btn_sign_up;
    private ViewInteraction ssn;
    private ViewInteraction pwd;
    private Context context;

    @Before
    public void setUp() throws Exception{
        server = new MockWebServer();
        server.start();

        MainActivity.setPath(server.url("/").toString());
        mActivity.launchActivity(new Intent());
        noSleep(mActivity.getActivity());

        btn_sign_in = onView(withId(R.id.btn_signin));
        btn_sign_up = onView(withId(R.id.btn_signup));
        ssn = onView(withId(R.id.sign_in_ssn));
        pwd = onView(withId(R.id.sign_in_pw));
    }

    @After
    public void tearDown() throws Exception {

        SessionAccess.getInstance(context).clearToken();
        MainActivity.setPath("http://10.0.2.2:5000/login");
    }

    @Test
    public void validSignInButtonTest() {

        String jsonBody = "{\"error\": 0, \"role\": \"Client\", \"token\": \"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJvcGVuX2lkIjoiM2Y1Y2JjZjgtNjdhNi00Y2I1LTlkMmItMzJkMmExODA3ZDg2IiwiZXhwIjoxNTQzNDQzNzg2fQ.WTjaoMNMm9g8iG-lACBh2Jd85g1MbUY14ge12ziMZeE\"}";


        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader("Content-Type", "application/json")
                        .setBody(jsonBody)

        );

        ssn.perform(replaceText("3011873949"));
        pwd.perform(replaceText("123456"));
        btn_sign_in.perform(click());
        onView(withId(R.id.client_home)).check(matches(isDisplayed()));


    }

    @Test
    public void SignUpButtonTest() {
        btn_sign_up.perform(click());
        onView(withId(R.id.signup_root)).check(matches(isDisplayed()));
    }

    @Test
    public void InvalidSignUpButtonTest() {
        SessionAccess.getInstance(context).clearToken();
        ssn.perform(replaceText("69"));
        pwd.perform(replaceText("69"));
        btn_sign_in.perform(click());
        onView(withId(R.id.signup_root)).check(matches(isDisplayed()));
    }


    private void noSleep(final MainActivity activity){
        Runnable wakeUp = new Runnable() {
            @Override
            public void run() {
                activity.getWindow().addFlags(
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                );
            }
        };
        activity.runOnUiThread(wakeUp);
    }
}
