package com.example.togar.schedulefootball.main.nextmatch

import com.example.togar.schedulefootball.model.EventsItem

interface NextMatchView {
    fun showLoading()
    fun hideLoading()
    fun showEventList(data: List<EventsItem>)
}