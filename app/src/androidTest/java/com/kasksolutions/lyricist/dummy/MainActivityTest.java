package com.kasksolutions.lyricist.dummy;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.kasksolutions.lyricist.allclasses.MainActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void mainActivityTest() {
        ViewInteraction appCompatImageView = onView(
                allOf(withId(R.id.thumbnail), withContentDescription("Movie Name:"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.card_view),
                                        0),
                                0),
                        isDisplayed()));
        appCompatImageView.perform(click());

        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.single_songlist), withText("Sardaar Gabbar Singh"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.v7.widget.CardView")),
                                        0),
                                1),
                        isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.recycler_view),
                        childAtPosition(
                                withClassName(is("android.widget.RelativeLayout")),
                                0)));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction appCompatTextView2 = onView(
                allOf(withId(R.id.two), withText("English"),
                        childAtPosition(
                                allOf(withId(R.id.tlll),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                1)),
                                1),
                        isDisplayed()));
        appCompatTextView2.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.dateyaer), withText("2016"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.table),
                                        2),
                                1)));
        textView.perform(scrollTo(), click());

        ViewInteraction cardView = onView(
                allOf(withId(R.id.card_view),
                        childAtPosition(
                                allOf(withId(R.id.seeAllcardll),
                                        childAtPosition(
                                                withId(R.id.yearsrecv),
                                                3)),
                                0),
                        isDisplayed()));
        cardView.perform(click());

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.recycler_view),
                        childAtPosition(
                                withClassName(is("android.widget.RelativeLayout")),
                                0)));
        recyclerView2.perform(actionOnItemAtPosition(3, click()));

        ViewInteraction appCompatTextView3 = onView(
                allOf(withId(R.id.one), withText("తెలుగు"),
                        childAtPosition(
                                allOf(withId(R.id.tlll),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                1)),
                                0),
                        isDisplayed()));
        appCompatTextView3.perform(click());

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.Teldateyaer), withText("2016"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.Teltable),
                                        2),
                                1)));
        textView2.perform(scrollTo(), click());

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.Teldateyaer), withText("2016"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.Teltable),
                                        2),
                                1)));
        textView3.perform(scrollTo(), click());

        ViewInteraction cardView2 = onView(
                allOf(withId(R.id.card_view),
                        childAtPosition(
                                allOf(withId(R.id.seeAllcardll),
                                        childAtPosition(
                                                withId(R.id.yearsrecv),
                                                0)),
                                0),
                        isDisplayed()));
        cardView2.perform(click());

        ViewInteraction recyclerView3 = onView(
                allOf(withId(R.id.recycler_view),
                        childAtPosition(
                                withClassName(is("android.widget.RelativeLayout")),
                                0)));
        recyclerView3.perform(actionOnItemAtPosition(5, click()));

        ViewInteraction appCompatTextView4 = onView(
                allOf(withId(R.id.two), withText("English"),
                        childAtPosition(
                                allOf(withId(R.id.tlll),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                1)),
                                1),
                        isDisplayed()));
        appCompatTextView4.perform(click());

        pressBack();

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        ViewInteraction appCompatTextView5 = onView(
                allOf(withId(R.id.title), withText("Home"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.v7.view.menu.ListMenuItemView")),
                                        0),
                                0),
                        isDisplayed()));
        appCompatTextView5.perform(click());

        ViewInteraction appCompatTextView6 = onView(
                allOf(withId(R.id.recentSeeAll), withText("See All >"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.TableLayout")),
                                        0),
                                2),
                        isDisplayed()));
        appCompatTextView6.perform(click());

        ViewInteraction recyclerView4 = onView(
                allOf(withId(R.id.recentSeeAllRecv),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                0)));
        recyclerView4.perform(actionOnItemAtPosition(1, click()));

        ViewInteraction appCompatTextView7 = onView(
                allOf(withId(R.id.alerteng), withText("English"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.custom),
                                        0),
                                1),
                        isDisplayed()));
        appCompatTextView7.perform(click());

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.textLyricsName), withText("Ramajogayya Sastry"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.table),
                                        1),
                                1)));
        textView4.perform(scrollTo(), click());

        ViewInteraction appCompatTextView8 = onView(
                allOf(withId(R.id.single_songlist), withText("Andhagadu"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.v7.widget.CardView")),
                                        0),
                                1),
                        isDisplayed()));
        appCompatTextView8.perform(click());

        ViewInteraction recyclerView5 = onView(
                allOf(withId(R.id.recycler_view),
                        childAtPosition(
                                withClassName(is("android.widget.RelativeLayout")),
                                0)));
        recyclerView5.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction appCompatTextView9 = onView(
                allOf(withId(R.id.two), withText("English"),
                        childAtPosition(
                                allOf(withId(R.id.tlll),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                1)),
                                1),
                        isDisplayed()));
        appCompatTextView9.perform(click());

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.dateyaer), withText("2017"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.table),
                                        2),
                                1)));
        textView5.perform(scrollTo(), click());

        ViewInteraction cardView3 = onView(
                allOf(withId(R.id.card_view),
                        childAtPosition(
                                allOf(withId(R.id.seeAllcardll),
                                        childAtPosition(
                                                withId(R.id.yearsrecv),
                                                4)),
                                0),
                        isDisplayed()));
        cardView3.perform(click());

        ViewInteraction recyclerView6 = onView(
                allOf(withId(R.id.recycler_view),
                        childAtPosition(
                                withClassName(is("android.widget.RelativeLayout")),
                                0)));
        recyclerView6.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction appCompatTextView10 = onView(
                allOf(withId(R.id.one), withText("తెలుగు"),
                        childAtPosition(
                                allOf(withId(R.id.tlll),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                1)),
                                0),
                        isDisplayed()));
        appCompatTextView10.perform(click());

        ViewInteraction textView6 = onView(
                allOf(withId(R.id.TeltextLyricsName), withText("Srimani"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.Teltable),
                                        1),
                                1)));
        textView6.perform(scrollTo(), click());

        ViewInteraction appCompatTextView11 = onView(
                allOf(withId(R.id.single_songlist), withText("Premam"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.v7.widget.CardView")),
                                        0),
                                1),
                        isDisplayed()));
        appCompatTextView11.perform(click());

        ViewInteraction recyclerView7 = onView(
                allOf(withId(R.id.recycler_view),
                        childAtPosition(
                                withClassName(is("android.widget.RelativeLayout")),
                                0)));
        recyclerView7.perform(actionOnItemAtPosition(1, click()));

        ViewInteraction appCompatTextView12 = onView(
                allOf(withId(R.id.one), withText("తెలుగు"),
                        childAtPosition(
                                allOf(withId(R.id.tlll),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                1)),
                                0),
                        isDisplayed()));
        appCompatTextView12.perform(click());

        ViewInteraction textView7 = onView(
                allOf(withId(R.id.TeltextLyricsName), withText("Srimani"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.Teltable),
                                        1),
                                1)));
        textView7.perform(scrollTo(), click());

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        ViewInteraction appCompatTextView13 = onView(
                allOf(withId(R.id.title), withText("Home"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.v7.view.menu.ListMenuItemView")),
                                        0),
                                0),
                        isDisplayed()));
        appCompatTextView13.perform(click());

        ViewInteraction appCompatTextView14 = onView(
                allOf(withId(R.id.recentSeeAll), withText("See All >"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.TableLayout")),
                                        0),
                                2),
                        isDisplayed()));
        appCompatTextView14.perform(click());

        ViewInteraction recyclerView8 = onView(
                allOf(withId(R.id.recentSeeAllRecv),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                0)));
        recyclerView8.perform(actionOnItemAtPosition(4, click()));

        ViewInteraction appCompatTextView15 = onView(
                allOf(withId(R.id.alerttel), withText("తెలుగు"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.custom),
                                        0),
                                0),
                        isDisplayed()));
        appCompatTextView15.perform(click());

        pressBack();

        ViewInteraction recyclerView9 = onView(
                allOf(withId(R.id.recentSeeAllRecv),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                0)));
        recyclerView9.perform(actionOnItemAtPosition(2, click()));

        ViewInteraction appCompatTextView16 = onView(
                allOf(withId(R.id.alerteng), withText("English"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.custom),
                                        0),
                                1),
                        isDisplayed()));
        appCompatTextView16.perform(click());

        pressBack();

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        ViewInteraction appCompatTextView17 = onView(
                allOf(withId(R.id.title), withText("Home"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.v7.view.menu.ListMenuItemView")),
                                        0),
                                0),
                        isDisplayed()));
        appCompatTextView17.perform(click());

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
