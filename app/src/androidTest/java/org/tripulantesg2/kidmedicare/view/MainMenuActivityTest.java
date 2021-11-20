package org.tripulantesg2.kidmedicare.view;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.ViewAction;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import androidx.test.filters.LargeTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.tripulantesg2.kidmedicare.R;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainMenuActivityTest {

    private String stringToBetyped;
    private String stringToBetyped2;

    @Rule
    public ActivityTestRule<LoginActivity> activityRule
            = new ActivityTestRule<>(LoginActivity.class);

    @Before
    public void initValidString() {
        // Specify a valid string.
        stringToBetyped = "tamatu@gmail.com";
        stringToBetyped2 = "tamatu";
    }

    @Test
    public void changeText_sameActivity() {
        // Type text and then press the button.
        onView(withId(R.id.textEmail))
                .perform(typeText(stringToBetyped), closeSoftKeyboard());
        onView(withId(R.id.textPassword))
                .perform(typeText(stringToBetyped2), closeSoftKeyboard());
        onView(withId(R.id.sign_in_login_btn)).perform(click());

        // Check that the text was changed.
        onView(withId(R.id.textEmail))
                .check(matches(withText(stringToBetyped)));
    }
}