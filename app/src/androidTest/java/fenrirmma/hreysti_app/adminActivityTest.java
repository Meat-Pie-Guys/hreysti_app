package fenrirmma.hreysti_app;


import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.ViewInteractionModule_ProvideNeedsActivityFactory;
import android.support.test.espresso.intent.Intents;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import fenrirmma.hreysti_app.login.MainActivity;
import fenrirmma.hreysti_app.user.Admin.adminActivity;
import fenrirmma.hreysti_app.user.Admin.allUsersAdminActivity;
import fenrirmma.hreysti_app.workout.exerciseOfTheDay;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class adminActivityTest {

    private ViewInteraction createButton;
    private ViewInteraction workoutOfTheDayButton;
    private ViewInteraction getUserListButton;
    private ViewInteraction signOutButton;

    @Rule
    public ActivityTestRule<adminActivity> mActivityRule = new ActivityTestRule<>(
            adminActivity.class);

    @Before
    public void setup() throws Exception{
        createButton = onView(withId(R.id.create_new_workout));
        workoutOfTheDayButton = onView(withId(R.id.assign_coach_btn));
        getUserListButton = onView(withId(R.id.get_user_list));
        signOutButton = onView(withId(R.id.signOut));

    }

    @Test
    public void workoutOfTheDayButtonPressedTest() {
        Intents.init();
        workoutOfTheDayButton.perform(click());
        intended(hasComponent(exerciseOfTheDay.class.getName()));
        Intents.release();
    }

    @Test
    public void getUserListButtonPressedTest() {
        Intents.init();
        getUserListButton.perform(click());
        intended(hasComponent(allUsersAdminActivity.class.getName()));
        Intents.release();
    }

    @Test
    public void signOutButtonPressedTest() {
        Intents.init();
        signOutButton.perform(click());
        intended(hasComponent(MainActivity.class.getName()));
        Intents.release();
    }
}
