package com.example.togar.schedulefootball.main.detailmatch

import com.example.togar.schedulefootball.api.ApiRepository
import com.example.togar.schedulefootball.api.TheSportDBApi
import com.example.togar.schedulefootball.model.EventResponse
import com.example.togar.schedulefootball.model.TeamResponse
import com.google.gson.Gson
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class DetailPresenter (private val view: DetailView,
                       private val apiRepository: ApiRepository,
                       private val gson: Gson){

    fun getMatchDetail(idEvent: String?, idHomeTeam: String?, idAwayTeam: String?) {
        view.showLoading()
        doAsync {
            val detailMatch = gson.fromJson(apiRepository.doRequest(TheSportDBApi.getDetailMatch(idEvent)),
                    EventResponse::class.java)

            val pictureHome = gson.fromJson(apiRepository.doRequest(TheSportDBApi.getHomePicture(idHomeTeam)),
                    TeamResponse::class.java)

            val pictureAway = gson.fromJson(apiRepository.doRequest(TheSportDBApi.getAwayPicture(idAwayTeam)),
                    TeamResponse::class.java)

            uiThread {
                view.hideLoading()
                view.showEventList(detailMatch.events, pictureHome.teams, pictureAway.teams)
            }
        }
    }
}