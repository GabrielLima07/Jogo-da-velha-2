package com.example.jogo_da_velha_2;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.espresso.matcher.ViewMatchers;
import com.example.jogo_da_velha_2.activities.Login;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

import android.view.View;

@RunWith(AndroidJUnit4.class)
public class TestLoginUI {

    @Rule
    public ActivityScenarioRule<Login> activityScenarioRule = new ActivityScenarioRule<>(Login.class);

    @Test
    public void testLoginSuccess() throws InterruptedException {
        onView(withId(R.id.lsNickname)).perform(replaceText("teste"));
        onView(withId(R.id.lsPassword)).perform(replaceText("teste"));

        onView(withId(R.id.loginBtn)).perform(click());

        Thread.sleep(5000);

        onView(withText("Logado com sucesso")).check(matches(ViewMatchers.isDisplayed()));

        onView(withText("OK")).perform(click());

        Thread.sleep(5000);

        onView(withId(R.id.mainLayout))
                .check(matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testLoginWithWrongNickname() throws InterruptedException {
        onView(withId(R.id.lsNickname)).perform(replaceText("FULANO_DE_TAL"));

        onView(withId(R.id.lsPassword)).perform(replaceText("teste"));

        onView(withId(R.id.loginBtn)).perform(click());

        Thread.sleep(7000);

        onView(withId(R.id.lsLayout))
                .check(matches(ViewMatchers.isDisplayed()));
    }

}
