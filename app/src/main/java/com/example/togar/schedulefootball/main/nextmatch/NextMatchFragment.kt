package com.example.togar.schedulefootball.main.nextmatch

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import com.example.togar.schedulefootball.R
import com.example.togar.schedulefootball.adapter.NextMatchAdapter
import com.example.togar.schedulefootball.api.ApiRepository
import com.example.togar.schedulefootball.model.EventsItem
import com.example.togar.schedulefootball.utils.invisible
import com.example.togar.schedulefootball.utils.visible
import com.google.gson.Gson
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout

class NextMatchFragment : Fragment(), NextMatchView {
    private var schedule: MutableList<EventsItem> = mutableListOf()
    private lateinit var listTeam: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var presenter: NextMatchPresenter
    private lateinit var adapter: NextMatchAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return UI {
            linearLayout {
                lparams(width = matchParent, height = matchParent)
                orientation = LinearLayout.VERTICAL
                padding = dip(15)

                swipeRefresh = swipeRefreshLayout {
                    id = R.id.swipeRefresh
                    relativeLayout {
                        lparams(width = matchParent, height = wrapContent)
                        listTeam = recyclerView {
                            lparams(width = matchParent, height = wrapContent)
                            layoutManager = LinearLayoutManager(ctx)
                        }

                        progressBar = progressBar {
                            id = R.id.pbNextEvent
                        }.lparams{
                            centerHorizontally()
                        }
                    }
                }
            }
        }.view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initAdapter()

        swipeRefresh.onRefresh {
            presenter.getNextTeamList(getString(R.string.resource_eventnextmatch))
        }
    }

    companion object {
        fun newInstance(): NextMatchFragment = NextMatchFragment()
    }

    private fun initAdapter(){
        adapter = NextMatchAdapter(schedule)
        listTeam.adapter = adapter

        val request = ApiRepository()
        val gson = Gson()
        presenter = NextMatchPresenter(this,request,gson)
        presenter.getNextTeamList(getString(R.string.resource_eventnextmatch))
    }

    override fun showLoading() {
        progressBar.visible()
    }

    override fun hideLoading() {
        progressBar.invisible()
    }

    override fun showEventList(data: List<EventsItem>) {
        swipeRefresh.isRefreshing = false
        schedule.clear()
        schedule.addAll(data)
        adapter.notifyDataSetChanged()
    }
}