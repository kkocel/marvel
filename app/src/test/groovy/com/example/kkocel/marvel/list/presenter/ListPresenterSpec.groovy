package com.example.kkocel.marvel.list.presenter

import com.example.kkocel.marvel.list.state.CorrectListPageViewState
import com.example.kkocel.marvel.list.view.ListView
import com.example.kkocel.marvel.model.Comic
import com.example.kkocel.marvel.network.MarvelRepository
import com.example.kkocel.marvel.network.Rx
import com.example.kkocel.marvel.state.ConnectionErrorViewState
import io.reactivex.Observable
import spock.lang.Specification

import static org.mockito.BDDMockito.given
import static org.mockito.Matchers.anyInt
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.verify

class ListPresenterSpec extends Specification {

    ListView listView = mock(ListView)
    MarvelRepository marvelRepository = mock(MarvelRepository)
    ListPresenter listPresenter = new ListPresenter(listView, marvelRepository)

    def setup() {
        Rx.unitTestMode = true
    }

    def "should get correct state during onCreate"() {
        given:
        def state = new CorrectListPageViewState(0, new ArrayList<Comic>())
        given(marvelRepository.getComicsForCharacter(anyInt(), anyInt(), anyInt())).willReturn(Observable.just(state))

        when:
        listPresenter.onCreate()

        then:
        verify(listView).onPageLoaded(state)
    }

    def "should handle connection error state during onCreate"() {
        given:
        def state = new ConnectionErrorViewState(new IOException())
        given(marvelRepository.getComicsForCharacter(anyInt(), anyInt(), anyInt())).willReturn(Observable.just(state))

        when:
        listPresenter.onCreate()

        then:
        verify(listView).onNetworkError(state)
    }

    def "should handle unrecoverable errors"() {
        given:
        def error = new StackOverflowError()
        given(marvelRepository.getComicsForCharacter(anyInt(), anyInt(), anyInt())).willReturn(Observable.error(error))

        when:
        listPresenter.onCreate()

        then:
        verify(listView).onUnrecoverableError(error)
    }

    def "should dispose subscriptions during onViewDestroyed"() {
        given:
        def listPresenter = new ListPresenter(listView, marvelRepository)

        when:
        listPresenter.onViewDestroyed()

        then:
        listPresenter.subscriptions.disposed
    }

    def "should convert page to offset"() {
        given:
        def page = 5

        when:
        def offset = ListPresenter.pageToOffset(page)

        then:
        offset == 500
    }

}
