package com.example.togar.schedulefootball.main.lastmatch

import com.example.togar.schedulefootball.model.EventsItem

interface LastMatchView {
    fun showLoading()
    fun hideLoading()
    fun showEventList(data: List<EventsItem>)
}