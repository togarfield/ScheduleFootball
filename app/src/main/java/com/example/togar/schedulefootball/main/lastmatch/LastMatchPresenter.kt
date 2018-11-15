package com.example.togar.schedulefootball.main.lastmatch

import com.example.togar.schedulefootball.api.ApiRepository
import com.example.togar.schedulefootball.api.TheSportDBApi
import com.example.togar.schedulefootball.model.EventResponse
import com.google.gson.Gson
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class LastMatchPresenter( private val view: LastMatchView,
                          private val apiRepository: ApiRepository,
                          private val gson: Gson) {

    fun getLastTeamList(match: String?) {
        view.showLoading()
        doAsync {
            val data = gson.fromJson(apiRepository.doRequest(TheSportDBApi.getScheduleTeam(match)),
                    EventResponse::class.java)

            uiThread {
                view.hideLoading()
                view.showEventList(data.events)
            }
        }

    }
}