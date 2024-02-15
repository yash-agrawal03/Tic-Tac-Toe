package androidsamples.java.tictactoe;

import org.junit.Test;

import static org.junit.Assert.*;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testInitialState() {
        Espresso.onView(ViewMatchers.withId(R.id.txt_result))
                .check(ViewAssertions.matches(ViewMatchers.withText("")));

        for (int i = 0; i < 9; i++) {
            Espresso.onView(ViewMatchers.withId(getButtonId(i)))
                    .check(ViewAssertions.matches(ViewMatchers.withText("")));
        }
    }

    private int getButtonId(int index) {
        switch (index) {
            case 0:
                return R.id.button0;
            case 1:
                return R.id.button1;
            case 2:
                return R.id.button2;
            case 3:
                return R.id.button3;
            case 4:
                return R.id.button4;
            case 5:
                return R.id.button5;
            case 6:
                return R.id.button6;
            case 7:
                return R.id.button7;
            case 8:
                return R.id.button8;
            default:
                throw new IllegalArgumentException("Invalid button index");
        }
        import androidx.test.espresso.action.ViewActions;

// ...

        @Test
        public void testButtonClickUpdatesButton() {
            Espresso.onView(ViewMatchers.withId(R.id.button0))
                    .perform(ViewActions.click())
                    .check(ViewAssertions.matches(ViewMatchers.withText("X")));

            Espresso.onView(ViewMatchers.withId(R.id.button1))
                    .perform(ViewActions.click())
                    .check(ViewAssertions.matches(ViewMatchers.withText("O")));
        }

        @Test
        public void testWinUpdatesResultTextView() {
            // Simulate a winning game
            Espresso.onView(ViewMatchers.withId(R.id.button0))
                    .perform(ViewActions.click());
            Espresso.onView(ViewMatchers.withId(R.id.button1))
                    .perform(ViewActions.click());
            Espresso.onView(ViewMatchers.withId(R.id.button4))
                    .perform(ViewActions.click());
            Espresso.onView(ViewMatchers.withId(R.id.button2))
                    .perform(ViewActions.click());
            Espresso.onView(ViewMatchers.withId(R.id.button8))
                    .perform(ViewActions.click());

            Espresso.onView(ViewMatchers.withId(R.id.txt_result))
                    .check(ViewAssertions.matches(ViewMatchers.withText("Player X wins!")));
        }




}