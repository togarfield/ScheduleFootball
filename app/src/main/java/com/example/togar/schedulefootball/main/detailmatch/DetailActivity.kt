package com.example.togar.schedulefootball.main.detailmatch

import android.database.sqlite.SQLiteConstraintException
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.support.design.R.attr.colorPrimary
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.example.togar.schedulefootball.R
import com.example.togar.schedulefootball.api.ApiRepository
import com.example.togar.schedulefootball.data.Favorite
import com.example.togar.schedulefootball.data.database
import com.example.togar.schedulefootball.model.EventsItem
import com.example.togar.schedulefootball.model.Team
import com.example.togar.schedulefootball.utils.invisible
import com.example.togar.schedulefootball.utils.visible
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import org.jetbrains.anko.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout
import java.text.SimpleDateFormat
import java.util.*

class DetailActivity: AppCompatActivity(), DetailView {

    private lateinit var eventDetail: EventsItem
    private lateinit var pictureHome: Team
    private lateinit var pictureAway: Team

    private lateinit var linearDetail: LinearLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefresh: SwipeRefreshLayout

    private lateinit var presenter: DetailPresenter

    private lateinit var idDetail: String
    private var idHome: String? = null
    private var idAway: String? = null

    private lateinit var imageHome: ImageView
    private lateinit var imageAway: ImageView

    private lateinit var txtDateMatch: TextView
    private lateinit var txtGoalsHome: TextView
    private lateinit var txtGoalsAway: TextView
    private lateinit var txtScoreHome: TextView
    private lateinit var txtScoreAway: TextView
    private lateinit var txtNameHome: TextView
    private lateinit var txtNameAway: TextView
    private lateinit var txtNameScoreHome: TextView
    private lateinit var txtNameScoreAway: TextView
    private lateinit var txtKeeperHome: TextView
    private lateinit var txtKeeperAway: TextView
    private lateinit var txtDefenceHome: TextView
    private lateinit var txtDefenceAway: TextView
    private lateinit var txtMidfieldHome: TextView
    private lateinit var txtMidfieldAway: TextView
    private lateinit var txtForwardHome: TextView
    private lateinit var txtForwardAway: TextView
    private lateinit var txtHomeSubstitutes: TextView
    private lateinit var txtAwaySubstitutes: TextView

    private var menuItem: Menu? = null
    private var isFavorite: Boolean = false

    override fun hideLoading() {
        progressBar.invisible()
    }

    override fun showLoading() {
        progressBar.visible()
    }

    override fun showEventList(data: List<EventsItem>, home: List<Team>, awayy: List<Team>) {
        eventDetail = data[0]
        pictureHome = home[0]
        pictureAway = awayy[0]
        initItem()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent.extras !=null) {
            idDetail = intent.getStringExtra(getString(R.string.item_eventdetail_id))
            idHome = intent.getStringExtra(getString(R.string.item_home_id))
            idAway = intent.getStringExtra(getString(R.string.item_away_id))
        }

        initView()
        getMatchDetail()
    }

    private fun initItem(){
        swipeRefresh.isRefreshing = false

        Picasso.get().load(pictureHome.teamBadge).into(imageHome)
        Picasso.get().load(pictureAway.teamBadge).into(imageAway)

        val timeEvent = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .parse(eventDetail.dateEvent)
        val dateEvent = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault())
                .format(timeEvent)

        txtDateMatch.text = dateEvent
        txtGoalsHome.text = setPlayer(eventDetail.strHomeGoalDetails)
        txtGoalsAway.text = setPlayer(eventDetail.strAwayGoalDetails)
        txtScoreHome.text = eventDetail.intHomeScore
        txtScoreAway.text = eventDetail.intAwayScore
        txtNameHome.text = eventDetail.strHomeTeam
        txtNameAway.text = eventDetail.strAwayTeam
        txtNameScoreHome.text = eventDetail.intHomeScore
        txtNameScoreAway.text = eventDetail.intAwayScore
        txtKeeperHome.text = setPlayer(eventDetail.strHomeLineupGoalkeeper)
        txtKeeperAway.text = setPlayer(eventDetail.strAwayLineupGoalkeeper)
        txtDefenceHome.text = setPlayer(eventDetail.strHomeLineupDefense)
        txtDefenceAway.text = setPlayer(eventDetail.strAwayLineupDefense)
        txtMidfieldHome.text = setPlayer(eventDetail.strHomeLineupMidfield)
        txtMidfieldAway.text = setPlayer(eventDetail.strAwayLineupMidfield)
        txtForwardHome.text = setPlayer(eventDetail.strHomeLineupForward)
        txtForwardAway.text = setPlayer(eventDetail.strAwayLineupForward)
        txtHomeSubstitutes.text = setPlayer(eventDetail.strHomeLineupSubstitutes)
        txtAwaySubstitutes.text = setPlayer(eventDetail.strAwayLineupSubstitutes)
    }

    private fun initView(){
        linearLayout {
            supportActionBar?.title = "Team Detail"
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            lparams(width = matchParent, height = wrapContent)
            orientation = LinearLayout.VERTICAL
            backgroundColor = Color.WHITE

            swipeRefresh = swipeRefreshLayout {
                scrollView {
                    lparams(width = matchParent, height = matchParent)
                    backgroundColor = Color.WHITE
                    relativeLayout {
                        lparams(width = matchParent, height = matchParent)
                        linearDetail = linearLayout {
                            id = R.id.linearDetail
                            lparams(width = matchParent, height = matchParent)
                            orientation = LinearLayout.VERTICAL
                            gravity = Gravity.CENTER
                            padding = dip(15)

                            txtDateMatch = textView {
                                gravity = Gravity.CENTER
                                textColor = colorPrimary
                                textSize = 20F
                                setTypeface(null, Typeface.BOLD)
                            }.lparams(width = wrapContent, height = wrapContent)

                            //Tampilan Foto dan Score
                            linearLayout {
                                orientation = LinearLayout.HORIZONTAL
                                gravity = Gravity.CENTER
                                lparams(width = matchParent, height = wrapContent) {
                                    topMargin = dip(10)
                                }

                                imageHome = imageView().lparams(width = dip(75), height = dip(75)) {
                                    gravity = Gravity.CENTER
                                    weight = 1F
                                }

                                txtScoreHome = textView {
                                    gravity = Gravity.CENTER
                                    textSize = 30F
                                }.lparams(width = wrapContent, height = wrapContent) {
                                    weight = 1F
                                }

                                textView {
                                    gravity = Gravity.CENTER
                                    textSize = 20F
                                    text = "VS"
                                }.lparams(width = wrapContent, height = wrapContent) {
                                    weight = 1F
                                }

                                txtScoreAway = textView {
                                    gravity = Gravity.CENTER
                                    textSize = 30F
                                }.lparams(width = wrapContent, height = wrapContent) {
                                    weight = 1F
                                }

                                imageAway = imageView().lparams(width = dip(75), height = dip(75)) {
                                    gravity = Gravity.CENTER
                                    weight = 1F
                                }
                            }

                            //nama team
                            linearLayout {
                                orientation = LinearLayout.HORIZONTAL
                                gravity = Gravity.CENTER
                                lparams(width = matchParent, height = wrapContent) {
                                    topMargin = dip(10)
                                }

                                txtNameHome = textView {
                                    gravity = Gravity.START
                                    textSize = 16F
                                }.lparams(width = matchParent, height = wrapContent) {
                                    leftMargin = dip(15)
                                    weight = 1F
                                }

                                txtNameAway = textView {
                                    gravity = Gravity.END
                                    textSize = 16F
                                }.lparams(width = matchParent, height = wrapContent) {
                                    rightMargin = dip(15)
                                    weight = 1F
                                }
                            }

                            view {
                                backgroundColorResource = android.R.color.darker_gray
                            }.lparams(width = matchParent, height = dip(1)) {
                                topMargin = dip(20)
                            }

                            //goals
                            linearLayout {
                                orientation = LinearLayout.HORIZONTAL
                                gravity = Gravity.CENTER
                                lparams(width = matchParent, height = wrapContent) {
                                    topMargin = dip(10)
                                }

                                txtGoalsHome = textView {
                                    gravity = Gravity.START
                                    textSize = 16F
                                }.lparams(width = matchParent, height = wrapContent) {
                                    leftMargin = dip(15)
                                    weight = 1F
                                }

                                textView {
                                    gravity = Gravity.CENTER
                                    textSize = 12F
                                    text = "Goals"
                                }.lparams(width = matchParent, height = wrapContent) {
                                    weight = 1F
                                }

                                txtGoalsAway = textView {
                                    gravity = Gravity.END
                                    textSize = 16F
                                }.lparams(width = matchParent, height = wrapContent) {
                                    rightMargin = dip(15)
                                    weight = 1F
                                }
                            }

                            //scores
                            linearLayout {
                                orientation = LinearLayout.HORIZONTAL
                                gravity = Gravity.CENTER
                                lparams(width = matchParent, height = wrapContent) {
                                    topMargin = dip(10)
                                }

                                txtNameScoreHome = textView {
                                    gravity = Gravity.START
                                    textSize = 16F
                                }.lparams(width = matchParent, height = wrapContent) {
                                    leftMargin = dip(15)
                                    weight = 1F
                                }

                                textView {
                                    gravity = Gravity.CENTER
                                    textSize = 12F
                                    text = "Shoots"
                                }.lparams(width = matchParent, height = wrapContent) {
                                    weight = 1F
                                }

                                txtNameScoreAway = textView {
                                    gravity = Gravity.END
                                    textSize = 16F
                                }.lparams(width = matchParent, height = wrapContent) {
                                    rightMargin = dip(15)
                                    weight = 1F
                                }
                            }

                            view {
                                backgroundColorResource = android.R.color.darker_gray
                            }.lparams(width = matchParent, height = dip(1)) {
                                topMargin = dip(5)
                                bottomMargin = dip(10)
                            }

                            textView {
                                gravity = Gravity.CENTER
                                textSize = 12F
                                text = "Lineups"
                            }.lparams(width = matchParent, height = wrapContent)

                            //kiper
                            linearLayout {
                                orientation = LinearLayout.HORIZONTAL
                                gravity = Gravity.CENTER
                                lparams(width = matchParent, height = wrapContent) {
                                    topMargin = dip(10)
                                }

                                txtKeeperHome = textView {
                                    gravity = Gravity.START
                                    textSize = 16F
                                }.lparams(width = matchParent, height = wrapContent) {
                                    leftMargin = dip(15)
                                    weight = 1F
                                }

                                textView {
                                    gravity = Gravity.CENTER
                                    textSize = 12F
                                    text = "Goal Keeper"
                                }.lparams(width = matchParent, height = wrapContent) {
                                    weight = 1F
                                }

                                txtKeeperAway = textView {
                                    textSize = 16F
                                }.lparams(width = matchParent, height = wrapContent) {
                                    rightMargin = dip(15)
                                    weight = 1F
                                }
                            }

                            //bek
                            linearLayout {
                                orientation = LinearLayout.HORIZONTAL
                                gravity = Gravity.CENTER
                                lparams(width = matchParent, height = wrapContent) {
                                    topMargin = dip(10)
                                }

                                txtDefenceHome = textView {
                                    gravity = Gravity.START
                                    textSize = 16F
                                }.lparams(width = matchParent, height = wrapContent) {
                                    leftMargin = dip(15)
                                    weight = 1F
                                }

                                textView {
                                    gravity = Gravity.CENTER
                                    textSize = 12F
                                    text = "Defence"
                                }.lparams(width = matchParent, height = wrapContent) {
                                    weight = 1F
                                }

                                txtDefenceAway = textView {
                                    textSize = 16F
                                }.lparams(width = matchParent, height = wrapContent) {
                                    rightMargin = dip(15)
                                    weight = 1F
                                }
                            }

                            //tengah
                            linearLayout {
                                orientation = LinearLayout.HORIZONTAL
                                gravity = Gravity.CENTER
                                lparams(width = matchParent, height = wrapContent) {
                                    topMargin = dip(10)
                                }

                                txtMidfieldHome = textView {
                                    gravity = Gravity.START
                                    textSize = 16F
                                }.lparams(width = matchParent, height = wrapContent) {
                                    leftMargin = dip(15)
                                    weight = 1F
                                }

                                textView {
                                    gravity = Gravity.CENTER
                                    textSize = 12F
                                    text = "Midfield"
                                }.lparams(width = matchParent, height = wrapContent) {
                                    weight = 1F
                                }

                                txtMidfieldAway = textView {
                                    textSize = 16F
                                }.lparams(width = matchParent, height = wrapContent) {
                                    rightMargin = dip(15)
                                    weight = 1F
                                }
                            }

                            //penyerang
                            linearLayout {
                                orientation = LinearLayout.HORIZONTAL
                                gravity = Gravity.CENTER
                                lparams(width = matchParent, height = wrapContent) {
                                    topMargin = dip(10)
                                }

                                txtForwardHome = textView {
                                    gravity = Gravity.START
                                    textSize = 16F
                                }.lparams(width = matchParent, height = wrapContent) {
                                    leftMargin = dip(15)
                                    weight = 1F
                                }

                                textView {
                                    gravity = Gravity.CENTER
                                    textSize = 12F
                                    text = "Forward"
                                }.lparams(width = matchParent, height = wrapContent) {
                                    weight = 1F
                                }

                                txtForwardAway = textView {
                                    textSize = 16F
                                }.lparams(width = matchParent, height = wrapContent) {
                                    rightMargin = dip(15)
                                    weight = 1F
                                }
                            }

                            //penyerang
                            linearLayout {
                                orientation = LinearLayout.HORIZONTAL
                                gravity = Gravity.CENTER
                                lparams(width = matchParent, height = wrapContent) {
                                    topMargin = dip(10)
                                }

                                txtHomeSubstitutes = textView {
                                    gravity = Gravity.START
                                    textSize = 16F
                                }.lparams(width = matchParent, height = wrapContent) {
                                    leftMargin = dip(15)
                                    weight = 1F
                                }

                                textView {
                                    gravity = Gravity.CENTER
                                    textSize = 12F
                                    text = "Substitutes"
                                }.lparams(width = matchParent, height = wrapContent) {
                                    weight = 1F
                                }

                                txtAwaySubstitutes = textView {
                                    textSize = 16F
                                }.lparams(width = matchParent, height = wrapContent) {
                                    rightMargin = dip(15)
                                    weight = 1F
                                }
                            }
                        }
                        progressBar = progressBar {
                            id = R.id.pbDetail
                        }.lparams {
                            centerInParent()
                        }
                    }
                }
            }
        }
    }

    private fun getMatchDetail(){
        favoriteState()
        presenter = DetailPresenter(this, ApiRepository(), Gson())
        presenter.getMatchDetail(idDetail, idHome, idAway)

        swipeRefresh.onRefresh {
            presenter.getMatchDetail(idDetail, idHome, idAway)
        }
    }

    private fun setPlayer(playerName: String?): String? {
        val bulkPlayer = playerName?.split(";".toRegex())?.dropLastWhile {
            it.isEmpty()
        }?.map { it.trim() }?.toTypedArray()?.joinToString("\n")

        return bulkPlayer
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        menuItem = menu
        setFavorite()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.add_to_favorite -> {
                if (isFavorite) removeFromFavorite() else addToFavorite()

                isFavorite = !isFavorite
                setFavorite()

                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun favoriteState(){
        database.use {
            val result = select(Favorite.TABLE_FAVORITE)
                    .whereArgs("(EVENT_ID = {id})",
                            "id" to idDetail)
            val favorite = result.parseList(classParser<Favorite>())
            if (!favorite.isEmpty()) isFavorite = true
        }
    }

    private fun addToFavorite(){
        try {
            database.use {
                insert(Favorite.TABLE_FAVORITE,
                        Favorite.EVENT_ID to eventDetail.idEvent,
                        Favorite.EVENT_TIME to eventDetail.dateEvent,
                        Favorite.HOME_TEAM to eventDetail.strHomeTeam,
                        Favorite.HOME_SCORE to eventDetail.intHomeScore,
                        Favorite.AWAY_TEAM to eventDetail.strAwayTeam,
                        Favorite.AWAY_SCORE to eventDetail.intAwayScore,
                        Favorite.HOME_TEAM_ID to idHome,
                        Favorite.AWAY_TEAM_ID to idAway)
            }
            snackbar(swipeRefresh, "Added to favorite").show()
        } catch (e: SQLiteConstraintException){
            snackbar(swipeRefresh, e.localizedMessage).show()
        }
    }

    private fun removeFromFavorite(){
        try {
            database.use {
                delete(Favorite.TABLE_FAVORITE, "(EVENT_ID = {id})",
                        "id" to idDetail)
            }
            snackbar(swipeRefresh, "Removed to favorite").show()
        } catch (e: SQLiteConstraintException){
            snackbar(swipeRefresh, e.localizedMessage).show()
        }
    }

    private fun setFavorite() {
        if (isFavorite)
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this,
                    R.drawable.ic_added_to_favorites)
        else
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this,
                    R.drawable.ic_add_to_favorites)
    }
}