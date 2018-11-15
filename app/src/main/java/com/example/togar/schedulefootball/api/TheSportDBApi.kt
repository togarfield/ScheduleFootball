package com.example.togar.schedulefootball.api

import com.example.togar.schedulefootball.BuildConfig

object TheSportDBApi {
    private const val idLeague = ".php?id=4328"
    private const val strLookUpTeam = "lookupteam.php?id="
    private const val strLookUpEvent = "lookupevent.php?id="

    fun getScheduleTeam(eventLeague: String?): String {
        return BuildConfig.BASE_URL + eventLeague + idLeague
    }

    fun getDetailMatch(idLeague: String?) : String {
        return BuildConfig.BASE_URL + strLookUpEvent + idLeague
    }

    fun getHomePicture(idHome: String?) : String {
        return BuildConfig.BASE_URL + strLookUpTeam + idHome
    }

    fun getAwayPicture(idAway: String?) : String {
        return BuildConfig.BASE_URL + strLookUpTeam + idAway
    }
}