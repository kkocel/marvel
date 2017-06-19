package com.example.kkocel.marvel

import android.content.Intent
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.intent.Intents
import android.support.test.rule.ActivityTestRule
import com.example.kkocel.marvel.list.view.ListActivity
import com.example.kkocel.marvel.network.rest.RetrofitModule
import io.appflate.restmock.RESTMockServer
import io.appflate.restmock.RESTMockServerStarter
import io.appflate.restmock.android.AndroidAssetsFileParser
import io.appflate.restmock.android.AndroidLogger
import org.junit.Rule
import spock.lang.Specification

import static android.support.test.espresso.Espresso.onView
import static android.support.test.espresso.action.ViewActions.click
import static android.support.test.espresso.assertion.ViewAssertions.matches
import static android.support.test.espresso.matcher.ViewMatchers.withId
import static android.support.test.espresso.matcher.ViewMatchers.withText
import static com.example.kkocel.marvel.utils.RequestMatchers.lastPathSegmentIsInteger
import static com.example.kkocel.marvel.utils.RequestMatchers.pathEndsWith
import static com.example.kkocel.marvel.utils.RecyclerViewMatcher.withRecyclerView

class ComicListSpecSpec extends Specification {

    @Rule
    ActivityTestRule<ListActivity> listActivityRule = new ActivityTestRule(ListActivity, true, false)

    def setup() {
        Intents.init()
        RESTMockServerStarter.startSync(new AndroidAssetsFileParser(InstrumentationRegistry.getContext()), new AndroidLogger())
        RetrofitModule.baseUrl = RESTMockServer.getUrl()
    }

    def cleanup() {
        Intents.release()
    }

    def "should display comics list"() {
        given:
        RESTMockServer.whenGET(pathEndsWith("comics")).thenReturnFile(200, "comics.json")

        when:
        listActivityRule.launchActivity(new Intent(Intent.ACTION_MAIN))

        then:
        onView(withRecyclerView(R.id.rclListRecycler)
                .atPositionOnView(0, R.id.txtListComicName))
                .check(matches(withText("Cap Transport (2005) #15")))
        onView(withRecyclerView(R.id.rclListRecycler)
                .atPositionOnView(1, R.id.txtListComicName))
                .check(matches(withText("Cap Transport (2005) #7")))
    }

    def "should display comic details"() {
        given:
        RESTMockServer.whenGET(pathEndsWith("comics")).thenReturnFile(200, "comics.json")
        RESTMockServer.whenGET(lastPathSegmentIsInteger()).thenReturnFile(200, "comic.json")
        listActivityRule.launchActivity(new Intent(Intent.ACTION_MAIN))

        when:
        onView(withId(R.id.rclListRecycler)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, click()))

        then:
        onView(withId(R.id.txtDetailComicName)).check(matches(withText("Deadpool (2012) #15")))
    }
}
