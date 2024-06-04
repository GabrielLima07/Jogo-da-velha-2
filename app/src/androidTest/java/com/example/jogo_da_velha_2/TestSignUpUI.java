package com.example.jogo_da_velha_2;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.espresso.matcher.ViewMatchers;
import com.example.jogo_da_velha_2.activities.SignUp;

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

import java.util.Random;

public class TestSignUpUI {

    @Rule
    public ActivityScenarioRule<SignUp> activityScenarioRule = new ActivityScenarioRule<>(SignUp.class);

    @Test
    public void testSignUpSuccess() throws InterruptedException {
        Random r = new Random();
        int n1 = r.nextInt(300);
        int n2 = r.nextInt(300);

        onView(withId(R.id.suNickname)).perform(replaceText("teste" + n1 + n2));
        onView(withId(R.id.suEmail)).perform(replaceText("teste" + n1 + n2 + "@gmail.com"));
        onView(withId(R.id.suPassword)).perform(replaceText("teste"));
        onView(withId(R.id.suPasswordConfirm)).perform(replaceText("teste"));

        onView(withId(R.id.signupbtn)).perform(click());

        Thread.sleep(5000);

        onView(withText("Cadastro realizado!")).check(matches(ViewMatchers.isDisplayed()));

        onView(withText("OK")).perform(click());

        Thread.sleep(5000);

        onView(withId(R.id.mainLayout))
                .check(matches(ViewMatchers.isDisplayed()));
    }
}
