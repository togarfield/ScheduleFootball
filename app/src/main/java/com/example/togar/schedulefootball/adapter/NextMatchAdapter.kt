package com.example.togar.schedulefootball.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.togar.schedulefootball.FootballMatchLayout
import com.example.togar.schedulefootball.R
import com.example.togar.schedulefootball.main.detailmatch.DetailActivity
import com.example.togar.schedulefootball.model.EventsItem
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.startActivity
import java.text.SimpleDateFormat
import java.util.*

class NextMatchAdapter (private val matches: List<EventsItem>) : RecyclerView.Adapter<NextMatchHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NextMatchHolder {
        return NextMatchHolder(FootballMatchLayout().createView(AnkoContext.Companion.create(parent.context, parent)))
    }

    override fun getItemCount(): Int = matches.size

    override fun onBindViewHolder(holder: NextMatchHolder, position: Int) {
        holder.bindItem(matches[position])
    }

}

class NextMatchHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val timeMatch: TextView = view.findViewById(R.id.txt_time_match)
    private val homeTeam: TextView = view.findViewById(R.id.txt_home_team)
    private val awayTeam: TextView = view.findViewById(R.id.txt_away_team)

    fun bindItem(matches: EventsItem) {
        val convertWaktu = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(matches.dateEvent)

        val matchTime = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault()).format(convertWaktu)

        timeMatch.text = matchTime.toString()
        homeTeam.text = matches.strHomeTeam
        awayTeam.text = matches.strAwayTeam

        val context = itemView.context

        itemView.setOnClickListener {
            context.startActivity<DetailActivity>(
                    context.getString(R.string.item_eventdetail_id) to matches.idEvent,
                    context.getString(R.string.item_home_id) to matches.idHomeTeam,
                    context.getString(R.string.item_away_id) to matches.idAwayTeam)
        }
    }
}