package com.example.togar.schedulefootball.main.detailmatch

import com.example.togar.schedulefootball.model.EventsItem
import com.example.togar.schedulefootball.model.Team

interface DetailView {
    fun hideLoading()
    fun showLoading()
    fun showEventList(data: List<EventsItem>, home: List<Team>, awayy: List<Team>)
}