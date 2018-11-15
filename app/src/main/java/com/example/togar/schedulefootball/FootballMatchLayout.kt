package com.example.togar.schedulefootball

import android.graphics.Typeface
import android.support.design.R.attr.colorPrimary
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import org.jetbrains.anko.*
import org.jetbrains.anko.cardview.v7.cardView

class FootballMatchLayout: AnkoComponent<ViewGroup> {
    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {
        cardView {
            lparams(width = matchParent, height = wrapContent) {
                bottomMargin = dip(10)
            }

            linearLayout {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER
                padding = dip(15)

                textView {
                    id = R.id.txt_time_match
                    gravity = Gravity.CENTER
                    textColor = colorPrimary
                    textSize = 16F
                    setTypeface(null, Typeface.BOLD)
                }.lparams{
                    bottomMargin = dip(10)
                }

                linearLayout {
                    lparams(width = matchParent, height = matchParent) {
                        topMargin = dip(10)
                    }
                    orientation = LinearLayout.HORIZONTAL
                    gravity = Gravity.CENTER_HORIZONTAL

                    textView {
                        id = R.id.txt_home_team
                        textSize = 16F
                        gravity = Gravity.END
                    }.lparams(width = matchParent, height = wrapContent){
                        weight = 1F
                    }

                    textView {
                        text = "VS"
                        gravity = Gravity.CENTER
                        textSize = 20F
                        setTypeface(null, Typeface.BOLD)
                    }.lparams(width = matchParent, height = wrapContent) {
                        weight = 1F
                    }

                    textView {
                        id = R.id.txt_away_team
                        textSize = 16F
                    }.lparams(width = matchParent, height = wrapContent) {
                        weight = 1F
                    }
                }

                linearLayout {
                    lparams(width = matchParent, height = matchParent)
                    orientation = LinearLayout.HORIZONTAL
                    gravity = Gravity.CENTER_HORIZONTAL

                    textView {
                        id = R.id.txt_score_home
                        textSize = 16F
                        gravity = Gravity.END
                    }.lparams(width = matchParent, height = wrapContent) {
                        weight = 1F
                    }

                    textView {
                        text = ""
                        gravity = Gravity.CENTER
                        textSize = 16F
                    }.lparams(width = matchParent, height = wrapContent) {
                        weight = 1F
                    }

                    textView {
                        id = R.id.txt_score_away
                        textSize = 16F
                    }.lparams(width = matchParent, height = wrapContent) {
                        weight = 1F
                    }
                }
            }
        }
    }
}