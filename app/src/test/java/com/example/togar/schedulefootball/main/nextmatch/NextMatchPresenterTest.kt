package com.example.togar.schedulefootball.main.nextmatch

import com.example.togar.schedulefootball.api.ApiRepository
import com.example.togar.schedulefootball.api.TheSportDBApi
import com.example.togar.schedulefootball.model.EventResponse
import com.example.togar.schedulefootball.model.EventsItem
import com.example.togar.schedulefootball.utils.CoroutineContextProvider
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class NextMatchPresenterTest {
    private lateinit var presenter: NextMatchPresenter
    @Mock
    private
    lateinit var view: NextMatchView

    @Mock
    private
    lateinit var gson: Gson

    @Mock
    private
    lateinit var apiRepository: ApiRepository

    @Mock
    private
    lateinit var context: CoroutineContextProvider

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = NextMatchPresenter(view, apiRepository, gson, context)
    }
    @Test
    fun testGetTeamList() {
        val teams: MutableList<EventsItem> = mutableListOf()
        val response = EventResponse(teams)
        val league = "eventsnextleague"

        GlobalScope.launch {
            Mockito.`when`(gson.fromJson(apiRepository
                    .doRequest(TheSportDBApi.getScheduleTeam(league)),
                    EventResponse::class.java
            )).thenReturn(response)

            presenter.getNextTeamList(league)

            Mockito.verify(view).showLoading()
            Mockito.verify(view).showEventList(teams)
            Mockito.verify(view).hideLoading()
        }
    }
}