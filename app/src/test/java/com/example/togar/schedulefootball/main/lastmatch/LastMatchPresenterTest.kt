package com.example.togar.schedulefootball.main.lastmatch

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

class LastMatchPresenterTest {
    private lateinit var presenter: LastMatchPresenter
    @Mock
    private
    lateinit var view: LastMatchView

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
        presenter = LastMatchPresenter(view, apiRepository, gson, context)
    }
    @Test
    fun testGetTeamList() {
        val teams: MutableList<EventsItem> = mutableListOf()
        val response = EventResponse(teams)
        val league = "eventspastleague"

        GlobalScope.launch {
            Mockito.`when`(gson.fromJson(apiRepository
                    .doRequest(TheSportDBApi.getScheduleTeam(league)),
                    EventResponse::class.java
            )).thenReturn(response)

            presenter.getLastTeamList(league)

            Mockito.verify(view).showLoading()
            Mockito.verify(view).showEventList(teams)
            Mockito.verify(view).hideLoading()
        }
    }
}