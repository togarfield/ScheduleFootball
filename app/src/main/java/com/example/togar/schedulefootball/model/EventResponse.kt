package com.example.togar.schedulefootball.model

import com.google.gson.annotations.SerializedName

data class EventResponse(

	@field:SerializedName("events")
	val events: List<EventsItem>
)