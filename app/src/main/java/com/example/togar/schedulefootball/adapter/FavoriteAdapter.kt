package com.example.togar.schedulefootball.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.togar.schedulefootball.FootballMatchLayout
import com.example.togar.schedulefootball.R
import com.example.togar.schedulefootball.data.Favorite
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.sdk25.coroutines.onClick
import java.text.SimpleDateFormat
import java.util.*

class FavoriteAdapter (private val matches: List<Favorite>,
                       private val listener: (Favorite) -> Unit) :
        RecyclerView.Adapter<FavoriteHolder>() {
    override fun getItemCount(): Int = matches.size

    override fun onBindViewHolder(holder: FavoriteHolder, position: Int) {
        holder.bindItem(matches[position], listener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteHolder {
        return FavoriteHolder(FootballMatchLayout().createView(AnkoContext.Companion.create(parent.context, parent)))
    }

}

class FavoriteHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val timeMatch: TextView = view.findViewById(R.id.txt_time_match)
    private val homeTeam: TextView = view.findViewById(R.id.txt_home_team)
    private val awayTeam: TextView = view.findViewById(R.id.txt_away_team)
    private val scoreHome: TextView = view.findViewById(R.id.txt_score_home)
    private val scoreAway: TextView = view.findViewById(R.id.txt_score_away)

    fun bindItem(matches: Favorite, listener: (Favorite) -> Unit) {
        val convertWaktu = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(matches.eventTime)

        val matchTime = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault()).format(convertWaktu)

        timeMatch.text = matchTime.toString()
        homeTeam.text = matches.homeTeam
        awayTeam.text = matches.awayTeam
        scoreHome.text = matches.homeScore.toString()
        scoreAway.text = matches.awayScore.toString()

        itemView.onClick {
            listener(matches)
        }
    }
}