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
import fenrirmma.hreysti_app.user.clientActivity;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;

import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.hasFocus;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
public class newSignupTest {

    private MockWebServer server;
    private String TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJvcGVuX2lkIjoiM2Y1Y2JjZjgtNjdhNi00Y2I1LTlkMmItMzJkMmExODA3ZDg2IiwiZXhwIjoxNTQzNDQzNzg2fQ.WTjaoMNMm9g8iG-lACBh2Jd85g1MbUY14ge12ziMZeE";

    @Rule
    public final ActivityTestRule<clientActivity> cActivity
            = new ActivityTestRule<>(clientActivity.class, true, false);
    @Rule
    public final ActivityTestRule<newSignUp> mActivity
            = new ActivityTestRule<>(newSignUp.class, true, false);

    private ViewInteraction name;
    private ViewInteraction btn_sign_up;
    private ViewInteraction ssn;
    private ViewInteraction pwd;
    private Context context;

    @Before
    public void setUp() throws Exception{
        server = new MockWebServer();
        server.start();

        newSignUp.setPath(server.url("/user").toString());
        mActivity.launchActivity(new Intent());
        noSleep(mActivity.getActivity());

        name = onView(withId(R.id.signup_name));
        btn_sign_up = onView(withId(R.id.signup_btn));
        ssn = onView(withId(R.id.signup_ssn));
        pwd = onView(withId(R.id.signup_password));
    }

    @After
    public void tearDown() throws Exception {

        SessionAccess.getInstance(context).clearToken();
        newSignUp.setPath("http://10.0.2.2:5000/user");
    }

    @Test
    public void validSignUpButtonTest() {
        SessionAccess.getInstance(context).setToken(TOKEN);
        String jsonBody = "{'error': 0}";
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader("Content-Type", "application/json")
                        .setHeader("Accept", "application/json")
                        .setBody(jsonBody)

        );

        name.perform(replaceText("Dirlewanger"));
        ssn.perform(replaceText("3011873949"));
        pwd.perform(replaceText("123456"));
        btn_sign_up.perform(click());
        newSignUp.setPath(server.url("/login").toString());
        cActivity.launchActivity(new Intent());
        noSleep(mActivity.getActivity());
        onView(withId(R.id.client_home)).check(matches(isDisplayed()));
    }

    @Test
    public void InvalidSignUpNoPassword() {
        String jsonBody = "{\"error\": 6}";
        server.enqueue(
                new MockResponse()
                        .setResponseCode(400)
                        .setHeader("Content-Type", "application/json")
                        .setHeader("Accept", "application/json")
                        .setBody(jsonBody)

        );

        SessionAccess.getInstance(context).clearToken();
        name.perform(replaceText("Dirlewanger"));
        ssn.perform(replaceText("3011873949"));
        pwd.perform(replaceText(""));
        btn_sign_up.perform(click());
        pwd.check(matches(hasErrorText("Password must be at least 6 characters long")));
        pwd.check(matches(withText("")));
    }

    @Test
    public void InvalidSignUpNoSSN() {
        String jsonBody = "{\"error\": 7}";
        server.enqueue(
                new MockResponse()
                        .setResponseCode(400)
                        .setHeader("Content-Type", "application/json")
                        .setHeader("Accept", "application/json")
                        .setBody(jsonBody)

        );

        SessionAccess.getInstance(context).clearToken();
        name.perform(replaceText("Dirlewanger"));
        ssn.perform(replaceText(""));
        pwd.perform(replaceText("checkyourselfbeforeyouwreckyourself"));
        btn_sign_up.perform(click());
        ssn.check(matches(hasErrorText("Kennitala is illegal")));
        ssn.check(matches(withText("")));
    }

    @Test
    public void InvalidSignUpNoName() {
        String jsonBody = "{\"error\": 7}";
        server.enqueue(
                new MockResponse()
                        .setResponseCode(400)
                        .setHeader("Content-Type", "application/json")
                        .setHeader("Accept", "application/json")
                        .setBody(jsonBody)

        );

        SessionAccess.getInstance(context).clearToken();
        name.perform(replaceText(""));
        ssn.perform(replaceText("3011873949"));
        pwd.perform(replaceText("checkyourselfbeforeyouwreckyourself"));
        btn_sign_up.perform(click());
        name.check(matches(hasErrorText("Name cannot be empty!")));
        name.check(matches(withText("")));
    }


    private void noSleep(final newSignUp activity){
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

    private void noSleep(final clientActivity activity){
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
